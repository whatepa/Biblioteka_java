/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteka;

import static biblioteka.Biblioteka.menuGlowne;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Ksiazka {
    int Id_ksiazki;
    String Nazwa_ksiazki;
    String Autor;
    String Kategoria;

    public Ksiazka() {
    }

    //dodawanie ksiazki
    Ksiazka(String nazwa, String autor, String kategoria) {


        try {
            RandomAccessFile plik = new RandomAccessFile("Ksiazki.bin", "rw");

            plik.seek(plik.length());
            plik.writeUTF("id");
            plik.writeInt(ostatniaId() + 1);
            plik.writeUTF("nazwa");
            plik.writeUTF(nazwa);
            plik.seek(plik.getFilePointer() + (40 - nazwa.length() - 2));
            plik.writeUTF("autor");
            plik.writeUTF(autor);
            plik.seek(plik.getFilePointer() + (40 - autor.length() - 2));
            plik.writeUTF("kategoria");
            plik.writeUTF(kategoria);

            plik.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            // System.err.println("Błąd zapisu...");
        }


    }

    public static void dodawanieKsiazek(Uzytkownik user) {


        Scanner klawiatura = new Scanner(System.in);

        String nazwa;
        String autor;
        String kategoria;
        int ilosc;
        boolean isOk;
        do {
            try {
                System.out.println("Aby powrócić wpisz 00");
                System.out.print("Podaj nazwę książki: ");
                nazwa = klawiatura.nextLine();
                if (nazwa.equalsIgnoreCase("00")) menuGlowne(user);
                System.out.print("Podaj autora książki: ");
                autor = klawiatura.nextLine();
                if (autor.equalsIgnoreCase("00")) menuGlowne(user);
                System.out.print("Podaj kategorię książki: ");
                kategoria = klawiatura.nextLine();
                if (kategoria.equalsIgnoreCase("00")) menuGlowne(user);
                System.out.print("Podaj ilość książek: ");
                ilosc = klawiatura.nextInt();

                if (ilosc == 0) {

                    menuGlowne(user);
                }


                for (int c = 1; c <= ilosc; c++) {

                    Ksiazka nowaKsiazka = new Ksiazka(nazwa, autor, kategoria);
                }

                isOk = true;
            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();

                System.out.print("\nWprowadź dane poprawnie \n");
            }


        } while (!isOk);


    }


    public void ustawKsiazkeOId(int id) {

        try {
            RandomAccessFile plik1 = new RandomAccessFile("Ksiazki.bin", "r");

            String nazwaTmp;
            String autor;
            String kat;
            plik1.seek(0);

            if (plik1.length() > 0) {
                do {

                    plik1.readUTF();
                    if (id == plik1.readInt()) {
                        plik1.readUTF();
                        Id_ksiazki = id;
                        Nazwa_ksiazki = plik1.readUTF();
                        plik1.seek(plik1.getFilePointer() + (40 - Nazwa_ksiazki.length() - 2));
                        plik1.readUTF();
                        Autor = plik1.readUTF();
                        plik1.seek(plik1.getFilePointer() + (40 - Autor.length() - 2));
                        plik1.readUTF();
                        Kategoria = plik1.readUTF();
                        break;
                    } else {
                        plik1.readUTF();
                        nazwaTmp = plik1.readUTF();
                        plik1.seek(plik1.getFilePointer() + (40 - nazwaTmp.length() - 2));
                        plik1.readUTF();
                        autor = plik1.readUTF();
                        plik1.seek(plik1.getFilePointer() + (40 - autor.length() - 2));
                        plik1.readUTF();
                        kat = plik1.readUTF();
                    }

                }
                while (plik1.getFilePointer() < plik1.length());
            }
            plik1.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            System.err.println("Błąd...");
        }
    }


    public static int ostatniaId() {
        int ostatni = 0;
        try {

            RandomAccessFile plik1 = new RandomAccessFile("Ksiazki.bin", "rw");
            String nazwaTmp;
            String autor;
            String kat;

            plik1.seek(0);
            if (plik1.length() > 0) {
                do {
                    if (plik1.readUTF().contains("id")) {
                        ostatni = plik1.readInt();
                    }
                    plik1.readUTF();
                    nazwaTmp = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + 40 - nazwaTmp.length() - 2);
                    plik1.readUTF();
                    autor = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + 40 - autor.length() - 2);
                    plik1.readUTF();
                    kat = plik1.readUTF();
                }
                while (plik1.getFilePointer() < plik1.length() - 4);
            }

            plik1.close();
            return ostatni;

        } catch (FileNotFoundException ex) {
            return ostatni;
        } catch (IOException ex) {
            System.err.println("Błąd zapisu...");

            return ostatni;
        }

    }


    public static void wykazKategorie(String kat) {

        try {
            RandomAccessFile plik1 = new RandomAccessFile("Ksiazki.bin", "r");

            int idTmp;
            String nazwaTmp;
            String autor;
            if (kat != "")
                System.out.println("\t   Kategoria: " + kat);
            else
                System.out.println("\t   Kategoria: Wszystko");
            System.out.format("|%1$-10s|%2$-45s|%3$-45s|%4$-45s\n", "Id książki", "Nazwa", "Autor", "Kategoria");

            String katTmp;
            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    nazwaTmp = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + 40 - nazwaTmp.length() - 2);
                    plik1.readUTF();
                    autor = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + 40 - autor.length() - 2);
                    plik1.readUTF();
                    katTmp = plik1.readUTF();

                    if (idTmp > 0)
                        if (katTmp.contains(kat)) {
                            System.out.format("|%1$-10s|%2$-70s|%3$-50s|%4$-30s\n", idTmp, nazwaTmp, autor, katTmp);
                        }

                }
                while (plik1.getFilePointer() < plik1.length());
            }


            plik1.close();
            Biblioteka.kliknijAbyPrzejsc();


        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            System.err.println("Błąd...");


        }


    }


    public static void wykazZliczoneKsiazki() {


        ArrayList<Ksiazka> ksiazki = new ArrayList<Ksiazka>();
        ArrayList<Ksiazka> ksiazkiB = new ArrayList<Ksiazka>();
        ArrayList<Integer> liczba = new ArrayList<Integer>();


        try {
            RandomAccessFile plik1 = new RandomAccessFile("Ksiazki.bin", "r");

            int idTmp;
            String nazwaTmp;
            String autor;

            System.out.println("|\tWykaz książek");
            System.out.format("|%1$-25s|%2$-45s|%3$-45s|%4$-30s|%5$-25s|%6$s\n", "Id książki ", "Nazwa", "Autor", "Kategoria", "Ilość książek", "Książki dostępne");

            String katTmp;
            plik1.seek(0);

            if (plik1.length() > 0) {
                do {

                    Ksiazka ksiazka = new Ksiazka();

                    plik1.readUTF();
                    idTmp = plik1.readInt();

                    ksiazka.ustawKsiazkeOId(idTmp);

                    plik1.readUTF();
                    nazwaTmp = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + 40 - nazwaTmp.length() - 2);
                    plik1.readUTF();
                    autor = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + 40 - autor.length() - 2);
                    plik1.readUTF();
                    katTmp = plik1.readUTF();

                    if (idTmp > 0) {

                        boolean isOk = true;
                        for (Ksiazka ksi : ksiazki) {
                            if (ksi.Nazwa_ksiazki.equals(ksiazka.Nazwa_ksiazki))
                                isOk = false;

                        }

                        if (isOk) {
                            ksiazki.add(ksiazka);
                        }

                        ksiazkiB.add(ksiazka);

                    }

                }
                while (plik1.getFilePointer() < plik1.length());
            }

            Comparator<Ksiazka> compareById = (Ksiazka o1, Ksiazka o2) -> o1.Nazwa_ksiazki.compareTo(o2.Nazwa_ksiazki);
            Collections.sort(ksiazki, compareById);


            for (Ksiazka k : ksiazki) {

                int ilosc = 0;
                int iloscDost = 0;
                int idOstatniejDostepnej = 0;

                for (Ksiazka ks : ksiazkiB) {

                    if (k.Nazwa_ksiazki.equals(ks.Nazwa_ksiazki)) {
                        ilosc++;

                        if (!czyWypozyczona(ks.Id_ksiazki)) {
                            iloscDost++;
                            idOstatniejDostepnej = ks.Id_ksiazki;
                        }

                    }

                }

                System.out.format("|%1$-25s|%2$-45s|%3$-45s|%4$-30s|%5$-25s|%6$s\n", idOstatniejDostepnej, k.Nazwa_ksiazki, k.Autor, k.Kategoria, ilosc, iloscDost);

            }

            plik1.close();

        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {
            System.err.println("Błąd...");
        }


    }


    public static void znajdzKsiazke(Uzytkownik user) {

        String kat = null;

        Scanner klawiatura = new Scanner(System.in);


        boolean isOk;
        do {
            try {
                System.out.println("Aby powrócić wpisz 00");
                System.out.print("Podaj frazę po której chcesz znaleźć książkę: ");
                kat = klawiatura.nextLine();

                if (kat.equalsIgnoreCase("00")) {
                    menuGlowne(user);
                }

                isOk = true;
            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();

                System.out.println("Wprowadź dane poprawnie ");
            }

        } while (!isOk);


        try {
            RandomAccessFile plik1 = new RandomAccessFile("Ksiazki.bin", "r");

            int idTmp;
            String nazwaTmp;
            String autor;
            if (!kat.equals(""))
                System.out.println("\t   Szukana fraza: " + kat);
            else
                System.out.println("\t   Szukana fraza: Wszystko");
            System.out.format("|%1$-15s|%2$-45s|%3$-45s|%4$-45s\n", "Id książki", "Nazwa", "Autor", "Kategoria");

            String katTmp;
            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    nazwaTmp = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + 40 - nazwaTmp.length() - 2);
                    plik1.readUTF();
                    autor = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + 40 - autor.length() - 2);
                    plik1.readUTF();
                    katTmp = plik1.readUTF();

                    if (idTmp > 0)
                        if (katTmp.toLowerCase().contains(kat.toLowerCase()) || nazwaTmp.toLowerCase().contains(kat.toLowerCase()) || autor.toLowerCase().contains(kat.toLowerCase())) {
                            System.out.format("|%1$-15s|%2$-45s|%3$-45s|%4$-45s\n", idTmp, nazwaTmp, autor, katTmp);
                        }

                }
                while (plik1.getFilePointer() < plik1.length());
            }

            plik1.close();

        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            //  System.err.println("Błąd...");
        }


    }

    public static String ksiazkaOId(int id) {


        try {
            RandomAccessFile plik1 = new RandomAccessFile("Ksiazki.bin", "r");

            String nazwaTmp;
            String autor;
            String kat;

            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();

                    if (id == plik1.readInt() && id != 0) {
                        plik1.readUTF();
                        return plik1.readUTF();
                    } else {
                        plik1.readUTF();
                        nazwaTmp = plik1.readUTF();
                        plik1.seek(plik1.getFilePointer() + (40 - nazwaTmp.length() - 2));
                    }

                    plik1.readUTF();
                    autor = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - autor.length() - 2));
                    plik1.readUTF();
                    kat = plik1.readUTF();

                }
                while (plik1.getFilePointer() < plik1.length());
            }

            plik1.close();
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            System.err.println("Błąd...");
            return null;
        }
        return null;

    }


    public static void usunKsiazke(Uzytkownik user) {

        Scanner klawiatura = new Scanner(System.in);

        int idKsiazki = -1;
        boolean isOk;
        do {
            try {

                System.out.println("Aby powrócić wpisz 00");
                System.out.print("Podaj id książki którą chcesz usunąć: ");
                idKsiazki = klawiatura.nextInt();

                if (idKsiazki == 0) {
                    menuGlowne(user);
                }
                isOk = true;

            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();
                System.out.print("\nWprowadź dane poprawnie \n");
            }

        } while (!isOk);

        try {
            RandomAccessFile plik1 = new RandomAccessFile("Ksiazki.bin", "rw");


            String nazwaTmp;
            String autor;
            String kat;

            plik1.seek(0);
            if (plik1.length() > 0) {
                do {
                    long poz;


                    plik1.readUTF();
                    poz = plik1.getFilePointer();
                    int idTmp = plik1.readInt();
                    plik1.readUTF();
                    nazwaTmp = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - nazwaTmp.length() - 2));
                    plik1.readUTF();
                    autor = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - autor.length() - 2));
                    plik1.readUTF();
                    kat = plik1.readUTF();
                    long pozB = plik1.getFilePointer();

                    if (idKsiazki == idTmp) {
                        plik1.seek(poz);
                        plik1.writeInt(0);
                        plik1.seek(poz);
                        break;
                    }

                }
                while (plik1.getFilePointer() < plik1.length());
            }

            plik1.close();
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {
            System.err.println("Błąd...");
        }

    }


    public static boolean czyWypozyczona(int id) {

        try {
            RandomAccessFile plik1 = new RandomAccessFile("Wypozyczenia.bin", "r");

            int idTmp;
            String nazwaTmp;

            Ksiazka ks = new Ksiazka();

            boolean czyWyp = false;

            plik1.seek(0);

            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();
                    plik1.readInt();
                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    plik1.readInt();

                    String dataPob;
                    String dataZwrotu;
                    plik1.readUTF();
                    dataPob = plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    dataZwrotu = plik1.readUTF();

                    if (idTmp == id) {

                        if (dataZwrotu.contains("Nie oddano"))
                            czyWyp = true;
                        else {
                            czyWyp = false;
                        }

                    }


                }
                while (plik1.getFilePointer() < plik1.length());

                plik1.close();
                if (czyWyp) {
                    return true;
                } else {
                    return false;
                }
            }


        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        }

        return false;
    }


    public static boolean czyPoTerminie(int id) {

        try {
            RandomAccessFile plik1 = new RandomAccessFile("Wypozyczenia.bin", "r");

            int idTmp;
            String nazwaTmp;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            Date teraz = new Date();

            Ksiazka ks = new Ksiazka();
            boolean czyPo = false;

            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();
                    plik1.readInt();
                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    plik1.readInt();

                    String terminZwrotu = "00";
                    plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    terminZwrotu = plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    if (idTmp == id) {
                        Date terminZwr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(terminZwrotu);

                        if (teraz.after(terminZwr))
                            czyPo = true;
                        else {
                            czyPo = false;
                        }

                    }

                }
                while (plik1.getFilePointer() < plik1.length());

                plik1.close();
                if (czyPo) {
                    return true;
                } else {
                    return false;
                }

            }


        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {
            // System.err.println("Błąd...");
        } catch (ParseException p) {
            System.err.println("Zły format daty.");
        }
        return false;
    }


    public static void edytujKsiazke(Uzytkownik user) {


        Scanner klawiatura = new Scanner(System.in);

        int id = 0;
        String nazwA = "";
        String autor = "n";


        boolean isOk;
        do {
            try {
                System.out.println("Aby powrócić wpisz 00");
                System.out.print("Podaj id książki którą chcesz edytować: ");
                id = klawiatura.nextInt();
                if (id != 0) {

                    klawiatura.nextLine();
                    System.out.println("Podaj nową nazwę książki lub wpisz N aby przejść do kolejnego atrybutu: ");
                    nazwA = klawiatura.nextLine();

                    System.out.println("Podaj nowego autora książki lub wpisz N aby przejść do kolejnego atrybutu: ");
                    // autor = klawiatura.nextLine();

                } else {
                    menuGlowne(user);
                }


                isOk = true;
            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;

                System.out.print("\nWprowadź dane poprawnie \n");
            }

        } while (!isOk);


        try {
            RandomAccessFile plik = new RandomAccessFile("Ksiazki.bin", "rw");

            long point;

            plik.seek(0);
            if (plik.length() > 0) {
                do {

                    plik.readUTF();
                    int idTmp = plik.readInt();
                    plik.readUTF();
                    long poz = plik.getFilePointer();
                    String naz = plik.readUTF();
                    plik.seek(plik.getFilePointer() + (40 - naz.length() - 2));
                    plik.readUTF();
                    String aut = plik.readUTF();
                    plik.seek(plik.getFilePointer() + (40 - aut.length() - 2));
                    plik.readUTF();
                    plik.readUTF();


                    if (id == idTmp) {
                        plik.seek(poz);

                        if (!nazwA.equalsIgnoreCase("N")) {
                            plik.writeUTF(nazwA);
                            plik.seek(plik.getFilePointer() + (40 - nazwA.length() - 2));
                        } else {
                            String nazq = plik.readUTF();
                            plik.seek(plik.getFilePointer() + (40 - nazq.length() - 2));
                        }
                        plik.readUTF();
                        if (!autor.equalsIgnoreCase("N")) {
                            plik.writeUTF(autor);
                            plik.seek(plik.getFilePointer() + (40 - autor.length() - 2));
                        } else {
                            String autq = plik.readUTF();
                            plik.seek(plik.getFilePointer() + (40 - autq.length() - 2));
                        }
                        plik.readUTF();
                        String katq = plik.readUTF();


                        break;

                    }


                }
                while (plik.getFilePointer() < plik.length());
            }

            plik.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            // System.err.println("Błąd zapisu...");
        }


    }


}
