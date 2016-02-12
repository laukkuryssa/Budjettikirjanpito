package budjettikirjanpito.gui;

import budjettikirjanpito.logiikka.kayttajat.Henkilo;
import budjettikirjanpito.logiikka.kayttajat.Kayttaja;
import budjettikirjanpito.logiikka.kayttajat.Perhe;
import budjettikirjanpito.logiikka.kayttajat.Yritys;
import budjettikirjanpito.logiikka.rahaliikenne.Ostos;
import budjettikirjanpito.logiikka.rahaliikenne.Saasto;
import budjettikirjanpito.logiikka.rahaliikenne.Tapahtuma;
import budjettikirjanpito.logiikka.rahaliikenne.Tulo;
import budjettikirjanpito.logiikka.rahaliikenne.Velka;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Kayttoliittyma {

    public ArrayList<Kayttaja> kayttajat;
    public Kayttaja current;
    public Scanner lukija;
    public String tiedostonNimi;

    public Kayttoliittyma() {
        kayttajat = new ArrayList<>();
        current = null;
        lukija = new Scanner(System.in);
        tiedostonNimi = "";
    }

    public void kaynnista() throws IOException, FileNotFoundException, ClassNotFoundException {
        tietojenLataus();
        System.out.println("Tervetuloa!");
        while (true) {
            System.out.println("");
            if (current == null) {
                System.out.println("Lisää käyttäjä syöttämällä 1");
                System.out.println("Kirjaudu sisään syöttämällä 2");
                System.out.println("Poista käyttäjä syöttämällä 3");
                System.out.println("Lopeta syöttämällä x");
                String syote = lukija.nextLine();
                if (syote.equals("x")) {
                    break;
                } else if (syote.equals("1")) {
                    uudenKayttajanLisays();
                } else if (syote.equals("2")) {
                    sisaanKirjautuminen();
                } else if (syote.equals("3")) {
                    poistaKayttaja();
                }
            } else {
                System.out.println("Lisää käyttäjä syöttämällä 1");
                System.out.println("Poista käyttäjä syöttämällä 2");
                System.out.println("Lisää tapahtuma syöttämällä 3");
                System.out.println("Poista tapahtuma syöttämällä 4");
                System.out.println("Tulostus syöttämällä 5");
                System.out.println("Kirjaudu ulos syöttämällä 6");
                System.out.println("Lopeta syöttämällä x");
                String syote = lukija.nextLine();
                if (syote.equals("x")) {
                    break;
                } else if (syote.equals("1")) {
                    uudenKayttajanLisays();
                } else if (syote.equals("2")) {
                    poistaKayttaja();
                } else if (syote.equals("3")) {
                    lisaaTapahtuma();
                } else if (syote.equals("4")) {
                    poistaTapahtuma();
                } else if (syote.equals("5")) {
                    System.out.println(current.toString());
                } else if (syote.equals("6")) {
                    current = null;
                } else {
                    System.out.println("Syötä kelvollinen syöte.");
                }
            }
        }
        tietojenTallennus();
    }

    public void sisaanKirjautuminen() {
        System.out.println("Syötä salasanasi: ");
        String salasana = lukija.nextLine();
        for (Kayttaja k : kayttajat) {
            if (k.salasana.equals(salasana)) {
                current = k;
                System.out.println("Tervetuloa " + k.toString());
            }
        }
        if (current == null) {
            System.out.println("Kirjautuminen epäonnistui.");
        }
    }

    public void tietojenLataus() throws FileNotFoundException, IOException, ClassNotFoundException {
        if (new File("kayttajat.ser").exists()) {
            FileInputStream fis = new FileInputStream("kayttajat.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            kayttajat = (ArrayList<Kayttaja>) ois.readObject();
            ois.close();
            fis.close();
        }
    }

    public void tietojenTallennus() throws FileNotFoundException, IOException {
        System.out.println("Tallenetaanko muutokset? (k/e)");
        while (true) {
            String vastaus = lukija.nextLine();
            if (vastaus.equals("k")) {
                System.out.println("Tallennetaan...");
                FileOutputStream fos = new FileOutputStream("kayttajat.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(kayttajat);
                oos.close();
                fos.close();
                System.out.println("Tallennettu.");
                break;
            } else if (vastaus.equals("e")) {
                break;
            } else {
                System.out.println("Vastaa k tai e.");
            }
        }
    }

    public void poistaOstos() {
        int i = 1;
        for (Tapahtuma ostos : current.getOstokset()) {
            System.out.print(i + ". " + ostos);
            i++;
        }
        System.out.println("Syötä ostoksen järjestysnumero: ");
        while (true) {
            int syote = Integer.parseInt(lukija.nextLine());
            if (syote > 0 && syote < current.getOstokset().size() + 1) {
                current.tapahtumat.remove(current.getOstokset().get(i - 1));
                System.out.println("Ostos poistettu.");
                break;
            } else {
                System.out.println("Anna kunnon syöte!");
            }
        }
    }

    public void poistaTulo() {
        int i = 1;
        for (Tapahtuma tulo : current.getTulot()) {
            System.out.print(i + ". " + tulo);
            i++;
        }
        System.out.println("Syötä tulon järjestysnumero: ");
        while (true) {
            int syote = Integer.parseInt(lukija.nextLine());
            if (syote > 0 && syote < current.getTulot().size() + 1) {
                current.tapahtumat.remove(current.getTulot().get(i - 1));
                System.out.println("Tulo poistettu.");
                break;
            } else {
                System.out.println("Anna kunnon syöte!");
            }
        }
    }

    public void lisaaOstos() {
        System.out.println("Ostoksen suuruus: ");
        double maara = lukija.nextDouble();
        System.out.println("Selitys: ");
        String selitys = lukija.nextLine();
        Ostos ostos = new Ostos(maara, selitys);
        current.tapahtumat.add(ostos);
    }

    public void lisaaTapahtuma() {
        System.out.println("Valitse tapahtuma: ");
        System.out.println("1. Kertaostos");
        System.out.println("2. Tulo");
        System.out.println("3. Velka");
        System.out.println("4. Säästö");
        String luku = lukija.nextLine();
        if (luku.equals("1")) {
            lisaaOstos();
        } else if (luku.equals("2")) {
            lisaaTulo();
        } else if (luku.equals("3")) {
            lisaaVelka();
        } else if (luku.equals("4")) {
            lisaaSaasto();
        } else {
            System.out.println("Anna kelvollinen syöte.");
        }
    }

    public void poistaTapahtuma() {
        System.out.println("Valitse tapahtuma: ");
        System.out.println("1. Kertaostos");
        System.out.println("2. Tulo");
        System.out.println("3. Velka");
        System.out.println("4. Säästö");
        int luku = Integer.parseInt(lukija.nextLine());
        if (luku == 1) {
            poistaOstos();
        } else if (luku == 2) {
            poistaTulo();
        } else if (luku == 3) {
            poistaVelka();
        } else if (luku == 4) {
            poistaSaasto();
        } else {
            System.out.println("Anna kelvollinen syöte.");
        }
    }

    public void lisaaTulo() {
        System.out.println("Maksaja: ");
        String maksaja = lukija.nextLine();
        System.out.println("Tulon suuruus: ");
        double maara = lukija.nextDouble();
        System.out.println("Selitys: ");
        String selitys = lukija.nextLine();
        Tulo tulo = new Tulo(maksaja, maara, selitys);
        current.tapahtumat.add(tulo);
    }

    public void poistaVelka() {
        int i = 1;
        for (Tapahtuma velka : current.getVelat()) {
            System.out.print(i + ". " + velka);
            i++;
        }
        System.out.println("Syötä velan järjestysnumero: ");
        while (true) {
            int syote = Integer.parseInt(lukija.nextLine());
            if (syote > 0 && syote < current.getVelat().size() + 1) {
                current.tapahtumat.remove(current.getVelat().get(i - 1));
                System.out.println("Velka poistettu.");
                break;
            } else {
                System.out.println("Anna kunnon syöte!");
            }
        }
    }

    public void lisaaVelka() {
        System.out.println("Velan suuruus: ");
        double maara = lukija.nextDouble();
        System.out.println("Vuosikorko (prosentteina): ");
        double korko = lukija.nextDouble();
        System.out.println("Lyhennysaika (kuukausina): ");
        int kk = lukija.nextInt();
        Velka velka = new Velka(maara, "", kk, korko);
        System.out.println("Velan aihe: ");
        String aihe = lukija.nextLine();
        velka.setAihe(aihe);
        System.out.println("Velan selitys: ");
        String selitys = lukija.nextLine();
        velka.setSelitys(selitys);
        current.tapahtumat.add(velka);
    }

    public void poistaSaasto() {
        int i = 1;
        for (Tapahtuma saasto : current.getSaastot()) {
            System.out.print(i + ". " + saasto);
            i++;
        }
        System.out.println("Syötä säästön järjestysnumero: ");
        while (true) {
            int syote = Integer.parseInt(lukija.nextLine());
            if (syote > 0 && syote < current.getSaastot().size() + 1) {
                current.tapahtumat.remove(current.getSaastot().get(i - 1));
                System.out.println("Säästö poistettu.");
                break;
            } else {
                System.out.println("Anna kunnon syöte!");
            }
        }
    }

    public void lisaaSaasto() {
        System.out.println("Säästön suuruus: ");
        double maara = lukija.nextDouble();
        System.out.println("Säästön selitys: ");
        String selitys = lukija.nextLine();
        Saasto saasto = new Saasto(maara, selitys);
        System.out.println("Haluatko määritellä kuukausisumman (syötä 1) vai säästämisjakson pituuden (syötä 2)?");
        String syote = lukija.nextLine();
        while (true) {
            if (syote.equals("1")) {
                System.out.println("Syötä kuukaudessa maksettava määrä: ");
                double summa = lukija.nextDouble();
                saasto.setKuukausiSumma(summa);
                break;
            } else if (syote.equals("2")) {
                System.out.println("Syötä kuukausien määrä: ");
                int kk = Integer.parseInt(lukija.nextLine());
                saasto.setKuukausiMaara(kk);
                break;
            } else {
                System.out.println("Syötä joko 1 tai 2.");
            }

            current.tapahtumat.add(saasto);
        }
    }

    public String listaaKaikkiTapahtumat() {
        String tulostettava = "";
        if (current.getVelat().isEmpty()) {
            tulostettava += "Ei velkoja. \n";
        } else {
            tulostettava += "Velat: \n";
            for (Tapahtuma c : current.getVelat()) {
                tulostettava += c.toString() + " \n";
            }
        }
        if (current.getTulot().isEmpty()) {
            tulostettava += "Ei tuloja. \n";
        } else {
            tulostettava += "Tulot: \n";
            for (Tapahtuma c : current.getTulot()) {
                tulostettava += c.toString() + " \n";
            }
        }
        if (current.getSaastot().isEmpty()) {
            tulostettava += "Ei säästöjä. \n";
        } else {
            tulostettava += "Säästöt: \n";
            for (Tapahtuma c : current.getSaastot()) {
                tulostettava += c.toString() + " \n";
            }
        }
        if (current.getOstokset().isEmpty()) {
            tulostettava += "Ei ostoksia. \n";
        } else {
            tulostettava += "Ostokset: \n";
            for (Tapahtuma c : current.getSaastot()) {
                tulostettava += c.toString() + " \n";
            }
        }

        return tulostettava;
    }

    public boolean salasananTarkistus(String s) {
        for (Kayttaja k : kayttajat) {
            if (k.salasana.equals(s)) {
                return false;
            }
        }
        return true;
    }

    public void vaihdaSalasana(String salasana) {
        System.out.println("Syötä vanha salasana: ");

        String vanha = lukija.nextLine();
        {
            if (current.salasana.equals(vanha)) {
                System.out.println("Syötä uusi salasana: ");
                String uusi = lukija.nextLine();
                current.setSalasana(uusi);
            } else {
                System.out.println("Salasana on virheellinen.");
            }

        }

    }

    public void uudenKayttajanLisays() {
        while (true) {
            System.out.println("Henkilo 1");
            System.out.println("Perhe 2");
            System.out.println("Yritys 3");
            System.out.println("Takaisin x");
            String tyyppi = lukija.nextLine();
            if (tyyppi.equals("1")) {
                uudenHenkilonLisays();
                break;
            } else if (tyyppi.equals("2")) {
                uudenPerheenLisays();
                break;
            } else if (tyyppi.equals("3")) {
                uudenYrityksenLisays();
                break;
            } else if (tyyppi.equals("x")) {
                break;
            } else {
                System.out.println("Valitse jokin alla olevista vaihtoehdoista:");
            }
        }
    }

    public void poistaKayttaja() {
        System.out.println("Syötä poistettavan käyttäjän salasana: ");
        String salasana = lukija.nextLine();
        boolean onnistui = false;
        for (Kayttaja kayttaja : kayttajat) {
            if (kayttaja.salasana.equals(salasana)) {
                if (current == kayttaja) {
                    current = null;
                }
                kayttajat.remove(kayttaja);
                System.out.println("Käyttäjä poistettu.");
                onnistui = true;
                break;
            }
        }
        if (!onnistui) {
            System.out.println("Salasana on virheellinen.");
        }
    }

    public void uudenHenkilonLisays() {

        System.out.println("Etunimi: ");
        String etunimi = lukija.nextLine();
        System.out.println("Sukunimi: ");
        String sukunimi = lukija.nextLine();
        Henkilo henkilo = new Henkilo(etunimi, sukunimi);
        System.out.println("Anna salasana: ");
        while (true) {
            String salasana = lukija.nextLine();
            if (salasananTarkistus(salasana)) {
                henkilo.salasana = salasana;
                System.out.println("Henkilö " + henkilo + " luotu.");
                kayttajat.add(henkilo);
                current = henkilo;
                break;
            }
            System.out.println("Salasana käytössä. Anna toinen salasana: ");
        }
    }

    public void uudenPerheenLisays() {
        Perhe perhe = new Perhe();
        while (true) {
            System.out.println("Lisää henkilö perheeseen 1");
            System.out.println("Valmis x");
            String syote2 = lukija.nextLine();
            if (syote2.equals("x")) {
                System.out.println("Anna perheellesi salasana: ");
                while (true) {
                    String salasana = lukija.nextLine();
                    if (salasananTarkistus(salasana)) {
                        perhe.salasana = salasana;
                        kayttajat.add(perhe);
                        current = perhe;
                        System.out.println("Perhe luotu.");
                        break;
                    }
                    System.out.println("Salasana käytössä. Anna toinen salasana: ");
                }
                break;
            } else if (syote2.equals("1")) {
                System.out.println("Henkilön salasana: ");
                String salasana = lukija.nextLine();
                boolean onnistui = false;
                for (Kayttaja kayttaja : kayttajat) {
                    if (kayttaja.salasana.equals(salasana) && kayttaja instanceof Henkilo) {
                        onnistui = true;
                        System.out.println(kayttaja + " lisätty perheeseen.");
                        perhe.lisaaHenkilo((Henkilo) kayttaja);
                        break;
                    }
                }
                if (!onnistui) {
                    System.out.println("Antamallasi salasanalla ei löydy henkilöä.");
                }

            } else {
                System.out.println("Syötä 1 tai x.");
            }
        }
    }

    public void uudenYrityksenLisays() {
        System.out.println("Anna yrityksen nimi: ");
        String nimi = lukija.nextLine();
        System.out.println("Anna yrityksen y-tunnus: ");
        String ytunnus = lukija.nextLine();
        System.out.println("Anna yrityksellesi salasana: ");
        String salasana = lukija.nextLine();
        Yritys yritys = new Yritys(nimi, ytunnus);
        yritys.salasana = salasana;
        System.out.println("Yritys " + yritys + " luotu.");
        kayttajat.add(yritys);
        current = yritys;
    }

}
