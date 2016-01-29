package budjettikirjanpito.logiikka.kayttajat;

import budjettikirjanpito.logiikka.rahaliikenne.Ostos;
import budjettikirjanpito.logiikka.rahaliikenne.Saasto;
import budjettikirjanpito.logiikka.rahaliikenne.Tapahtuma;
import budjettikirjanpito.logiikka.rahaliikenne.Tulo;
import budjettikirjanpito.logiikka.rahaliikenne.Velka;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Kayttaja {

    public ArrayList<Tapahtuma> tapahtumat;
    public Scanner lukija;

    public Kayttaja() {
        this.lukija = new Scanner(System.in);
        tapahtumat = new ArrayList<>();

    }

    public void lisaaTapahtuma() {
        System.out.println("Valitse tapahtuma: ");
        System.out.println("1. Kertaostos");
        System.out.println("2. Tulo");
        System.out.println("3. Velka");
        System.out.println("4. Säästö");
        int luku = Integer.parseInt(lukija.nextLine());
        if (luku == 1) {
            lisaaOstos();
        } else if (luku == 2) {
            lisaaTulo();
        } else if (luku == 3) {
            lisaaVelka();
        }  else if (luku == 4) {
            lisaaSaasto();
        } else {
            System.out.println("Anna kelvollinen syöte.");
        }
    }

    public ArrayList<Tapahtuma> getVelat() {
        ArrayList<Tapahtuma> listattavatTapahtumat = new ArrayList<>();
        for (Tapahtuma t : tapahtumat) {
            if (t instanceof Velka) {
                listattavatTapahtumat.add(t);
            }
        }
        return listattavatTapahtumat;
    }

    public ArrayList<Tapahtuma> getTulot() {
        ArrayList<Tapahtuma> listattavatTapahtumat = new ArrayList<>();
        for (Tapahtuma t : tapahtumat) {
            if (t instanceof Tulo) {
                listattavatTapahtumat.add(t);
            }
        }
        return listattavatTapahtumat;
    }

    public ArrayList<Tapahtuma> getSaastot() {
        ArrayList<Tapahtuma> listattavatTapahtumat = new ArrayList<>();
        for (Tapahtuma t : tapahtumat) {
            if (t instanceof Saasto) {
                listattavatTapahtumat.add(t);
            }
        }
        return listattavatTapahtumat;
    }

    public ArrayList<Tapahtuma> getOstokset() {
        ArrayList<Tapahtuma> listattavatTapahtumat = new ArrayList<>();
        for (Tapahtuma t : tapahtumat) {
            if (t instanceof Ostos) {
                listattavatTapahtumat.add(t);
            }
        }
        return listattavatTapahtumat;
    }

    public void lisaaOstos() {
        System.out.println("");
    }

    public void lisaaTulo() {
        System.out.println("Maksaja: ");
        String maksaja = lukija.nextLine();
        System.out.println("Tulon suuruus: ");
        double maara = lukija.nextDouble();
        System.out.println("Selitys: ");
        String selitys = lukija.nextLine();
        Tulo tulo = new Tulo(maksaja, maara, selitys);
        tapahtumat.add(tulo);
    }

    public void lisaaVelka() {
        System.out.println("Velan suuruus: ");
        double maara = lukija.nextDouble();
        System.out.println("Vuosikorko (prosentteina): ");
        double korko = lukija.nextDouble();
        System.out.println("Lyhennysaika (kuukausina): ");
        int kk = lukija.nextInt();
        Velka velka = new Velka(maara, "", kk, korko);
        velka.setAihe();
        velka.setSelitys();
        tapahtumat.add(velka);
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

            tapahtumat.add(saasto);
        }
    }

    @Override
    public String toString() {
        String tulostettava = "";
        if (getVelat().isEmpty()) {
            tulostettava += "Ei velkoja. \n";
        } else {
            tulostettava += "Velat: \n";
            for (Tapahtuma c : getVelat()) {
                tulostettava += c.toString() + " \n";
            }
        }
        if (getTulot().isEmpty()) {
            tulostettava += "Ei tuloja. \n";
        } else {
            tulostettava += "Tulot: \n";
            for (Tapahtuma c : getTulot()) {
                tulostettava += c.toString() + " \n";
            }
        }
        if (getSaastot().isEmpty()) {
            tulostettava += "Ei säästöjä. \n";
        } else {
            tulostettava += "Säästöt: \n";
            for (Tapahtuma c : getSaastot()) {
                tulostettava += c.toString() + " \n";
            }
        }
        if (getOstokset().isEmpty()) {
            tulostettava += "Ei ostoksia. \n";
        } else {
            tulostettava += "Ostokset: \n";
            for (Tapahtuma c : getSaastot()) {
                tulostettava += c.toString() + " \n";
            }
        }

        return tulostettava;
    }

}
