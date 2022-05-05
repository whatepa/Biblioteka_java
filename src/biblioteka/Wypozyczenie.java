/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteka;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Kuba
 */
public class Wypozyczenie {

    int Id_wypozyczenia;
    int Id_ksiazki;
    int Id_uzytkownika;
    String Data_pobrania;
    String Termin_Do_zwrotu;
    String Data_oddania;

    public Wypozyczenie() {
    }


    Wypozyczenie(Ksiazka ksiazka, Uzytkownik uzytkownik) {

        int idUzytkownika = uzytkownik.Id_uzytkownika;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Calendar teraz = Calendar.getInstance();

        Calendar zwrot = Calendar.getInstance();
        zwrot.add(Calendar.DAY_OF_MONTH, 14);


        if (!ksiazka.czyWypozyczona(ksiazka.Id_ksiazki)) {
            try {
                RandomAccessFile plik = new RandomAccessFile("Wypozyczenia.bin", "rw");

                if (ksiazka.Id_ksiazki != 0) {

                    plik.seek(plik.length());
                    plik.writeUTF("idWypozyczenia");
                    plik.writeInt(ostatniaId());
                    plik.writeUTF("idKsiazki");
                    plik.writeInt(ksiazka.Id_ksiazki);
                    plik.writeUTF("idUzytkownika");
                    plik.writeInt(idUzytkownika);
                    plik.writeUTF("dataPobrania");
                    plik.writeUTF(simpleDateFormat.format(teraz.getTime()));
                    plik.writeUTF("terminZwrotu");
                    plik.writeUTF(simpleDateFormat.format(zwrot.getTime()));
                    plik.writeUTF("dataOddania");
                    plik.writeUTF("----Nie oddano-----");

                } else {
                    System.out.println("Nie ma książki o id=0");
                }


                plik.close();
            } catch (FileNotFoundException ex) {
                System.err.println("Nie mogę zrealizować dostępu do pliku...");
            } catch (IOException ex) {

            }

            System.out.println("Poprawnie wypozyczono ksiazke.");

        } else {
            System.out.println("Książka jest niedostępna");
        }

    }


    public static int ostatniaId() {
        int ostatni = 0;


        try {
            RandomAccessFile plik1 = new RandomAccessFile("Wypozyczenia.bin", "r");


            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    if (plik1.readUTF().contains("idWypozyczenia")) {
                        ostatni = plik1.readInt();
                    } else {
                        plik1.readInt();
                    }
                    plik1.readUTF();
                    plik1.readInt();
                    plik1.readUTF();
                    plik1.readInt();

                    plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();

                }
                while (plik1.getFilePointer() < plik1.length());

                plik1.close();
                return ostatni + 1;

            }
            plik1.close();
            return 0;

        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
            return ostatni;
        } catch (IOException ex) {


            return ostatni + 1;
        }

    }


    public static void wypozyczoneKsiazki() {


        Scanner klawiatura = new Scanner(System.in);

        int idUzytkownika = 0;
        boolean isOk;
        do {
            try {
                System.out.print("Podaj id uzytkownika: ");
                idUzytkownika = klawiatura.nextInt();
                isOk = true;
            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();
                System.out.print("\nWprowadź dane poprawnie \n");
            }

        } while (!isOk);


        try {
            RandomAccessFile plik1 = new RandomAccessFile("Wypozyczenia.bin", "r");

            int idTmp;
            int idWypTmp;
            String nazwaTmp;

            Ksiazka ks = new Ksiazka();
            System.out.format("|%1$-15s|%2$-15s|%3$-45s|%4$-30s|%5$-30s|%6$-30s|\n", "Id wypożyczenia", "Id książki", "Nazwa", "Data pobrania", "Data zwrotu/Czy oddano", "Należy oddać przed");


            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();
                    idWypTmp = plik1.readInt();
                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    if (plik1.readInt() == idUzytkownika) {

                        String dataPob;
                        String dataZwrotu;
                        plik1.readUTF();
                        dataPob = plik1.readUTF();
                        plik1.readUTF();
                        String terminDoZwr = plik1.readUTF();
                        plik1.readUTF();
                        dataZwrotu = plik1.readUTF();

                        System.out.format("|%1$-15s|%2$-15s|%3$-45s|%4$-30s|%5$-30s|%6$-30s|\n", idWypTmp, idTmp, ks.ksiazkaOId(idTmp), dataPob, dataZwrotu, terminDoZwr);


                    } else {
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                    }

                }
                while (plik1.getFilePointer() < plik1.length());
            }

            plik1.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            System.err.println("Błąd...");

        }


    }


    public static void wypozyczoneKsiazki(Uzytkownik user) {

        try {
            RandomAccessFile plik1 = new RandomAccessFile("Wypozyczenia.bin", "r");

            int idTmp;
            int idWypTmp;
            String nazwaTmp;

            Ksiazka ks = new Ksiazka();
            System.out.format("|%1$-15s|%2$-15s|%3$-45s|%4$-30s|%5$-30s|%6$-30s|\n", "Id wypożyczenia", "Id książki", "Nazwa", "Data pobrania", "Data zwrotu/Czy oddano", "Należy oddać przed");


            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();
                    idWypTmp = plik1.readInt();
                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    if (plik1.readInt() == user.Id_uzytkownika) {

                        String dataPob;
                        String dataZwrotu;
                        plik1.readUTF();
                        dataPob = plik1.readUTF();
                        plik1.readUTF();
                        String terminDoZwrotu = plik1.readUTF();
                        plik1.readUTF();
                        dataZwrotu = plik1.readUTF();

                        System.out.format("|%1$-15s|%2$-15s|%3$-45s|%4$-30s|%5$-30s|%6$-30s|\n", idWypTmp, idTmp, ks.ksiazkaOId(idTmp), dataPob, dataZwrotu, terminDoZwrotu);


                    } else {
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                    }


                }
                while (plik1.getFilePointer() < plik1.length());
            }

            plik1.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            System.err.println("Błąd...");


        }


    }


    public static void historiaKsiazki() {


        Scanner klawiatura = new Scanner(System.in);

        int idKsiazki = 0;
        boolean isOk;
        do {
            try {
                System.out.print("Podaj id książki: ");
                idKsiazki = klawiatura.nextInt();
                isOk = true;
            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();
                System.out.print("\nWprowadź dane poprawnie \n");
            }

        } while (!isOk);


        try {
            RandomAccessFile plik1 = new RandomAccessFile("Wypozyczenia.bin", "r");
            int idTmp;
            int idWypTmp;
            int userId;
            String nazwaTmp;

            System.out.println("| Nazwa książki: " + Ksiazka.ksiazkaOId(idKsiazki));

            System.out.format("|%1$-15s|%2$-50s|%3$-30s|%4$-30s|\n", "Id wypożyczenia", "Czytelnik", "Data pobrania", "Data zwrotu/Czy oddano");


            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();
                    idWypTmp = plik1.readInt();
                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    userId = plik1.readInt();


                    String dataPob;
                    String dataZwrotu;
                    plik1.readUTF();
                    dataPob = plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    dataZwrotu = plik1.readUTF();

                    if (idTmp == idKsiazki) {
                        System.out.format("|%1$-15s|%2$-50s|%3$-30s|%4$-30s|\n", idWypTmp, Uzytkownik.uzytkownikOId(userId), dataPob, dataZwrotu);

                    }

                }
                while (plik1.getFilePointer() < plik1.length());
            }

            plik1.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            System.err.println("Błąd...a");


        }


    }


    public static void poTerminieKsiazki(Uzytkownik user) {

        try {
            RandomAccessFile plik1 = new RandomAccessFile("Wypozyczenia.bin", "r");
            int idTmp;
            int idWypTmp;
            String nazwaTmp;

            Ksiazka ks = new Ksiazka();
            System.out.format("|%1$-15s|%2$-15s|%3$-45s|%4$-30s|%5$-30s|%6$-30s|\n", "Id wypożyczenia", "Id książki", "Nazwa", "Data pobrania", "Data zwrotu/Czy oddano", "Data zwrotu/Czy oddano");


            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();
                    idWypTmp = plik1.readInt();
                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    if (plik1.readInt() == user.Id_uzytkownika) {

                        String dataPob;
                        String dataZwrotu;
                        plik1.readUTF();
                        dataPob = plik1.readUTF();
                        plik1.readUTF();
                        String terminDoZwr = plik1.readUTF();
                        plik1.readUTF();
                        dataZwrotu = plik1.readUTF();

                        if (Ksiazka.czyPoTerminie(idTmp))
                            System.out.format("|%1$-15s|%2$-15s|%3$-45s|%4$-30s|%5$-30s|%6$-30s|\n", idWypTmp, idTmp, ks.ksiazkaOId(idTmp), dataPob, dataZwrotu, terminDoZwr);


                    } else {
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                    }


                }
                while (plik1.getFilePointer() < plik1.length());
            }

            plik1.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            System.err.println("Błąd...");


        }


    }


    public static void poTerminieKsiazki() {

        try {
            RandomAccessFile plik1 = new RandomAccessFile("Wypozyczenia.bin", "r");
            int idTmp;
            int idWypTmp;
            String nazwaTmp;

            Ksiazka ks = new Ksiazka();
            System.out.format("|%1$-15s|%2$-15s|%3$-45s|%4$-30s|%5$-30s|\n", "Id wypożyczenia", "Id książki", "Nazwa", "Data pobrania", "Data zwrotu/Czy oddano");


            plik1.seek(0);
            if (plik1.length() > 0) {
                do {

                    plik1.readUTF();
                    idWypTmp = plik1.readInt();
                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();


                    String dataPob;
                    String dataZwrotu;
                    plik1.readUTF();
                    dataPob = plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    dataZwrotu = plik1.readUTF();

                    if (Ksiazka.czyPoTerminie(idTmp))
                        System.out.format("|%1$-15s|%2$-15s|%3$-45s|%4$-30s|%5$-30s|\n", idWypTmp, idTmp, ks.ksiazkaOId(idTmp), dataPob, dataZwrotu);


                }
                while (plik1.getFilePointer() < plik1.length());
            }

            plik1.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            System.err.println("Błąd...");

        }

    }


    public static void oddaneKsiazki(Uzytkownik user) {

        try {
            RandomAccessFile plik1 = new RandomAccessFile("Wypozyczenia.bin", "r");
            int idTmp;
            String nazwaTmp;

            Ksiazka ks = new Ksiazka();
            System.out.format("|%1$-15s|%2$-45s|%3$-30s|%4$-30s|\n", "Id", "Nazwa", "Data pobrania", "Data zwrotu/Czy oddano");


            plik1.seek(0);
            if (plik1.length() > 0) {
                do {

                    plik1.readUTF();
                    plik1.readInt();
                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    if (plik1.readInt() == user.Id_uzytkownika) {

                        String dataPob;
                        String dataZwrotu;
                        plik1.readUTF();
                        dataPob = plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        dataZwrotu = plik1.readUTF();
                        if (!dataZwrotu.contains("Nie oddano"))
                            System.out.format("|%1$-15s|%2$-45s|%3$-30s|%4$-30s|\n", idTmp, ks.ksiazkaOId(idTmp), dataPob, dataZwrotu);


                    } else {
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                        plik1.readUTF();
                    }

                }
                while (plik1.getFilePointer() < plik1.length());
            }

            plik1.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            System.err.println("Błąd..");

        }

    }


    public static void oddajKsiazke(Uzytkownik user) {


        int idWypozyczenia = 1;

        Scanner klawiatura = new Scanner(System.in);

        boolean isOk;
        do {
            try {

                System.out.println("(Jeśli chcesz powrócić wpisz 00)");
                System.out.print("Wybierz z powyższych książkę którą chcesz zwrócić i podaj jej id wypożyczenia: ");
                idWypozyczenia = klawiatura.nextInt();
                if (idWypozyczenia == 0) {
                    Biblioteka.menuGlowne(user);
                }


                isOk = true;

            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();

                System.out.print("\nWprowadź dane poprawnie \n");
            }

        } while (!isOk);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Calendar teraz = Calendar.getInstance();
        try {
            RandomAccessFile plik1 = new RandomAccessFile("Wypozyczenia.bin", "rw");
            int idWypTmp;
            int idTmp;
            int userIdTmp;

            Ksiazka ks = new Ksiazka();


            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();
                    idWypTmp = plik1.readInt();
                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    userIdTmp = plik1.readInt();

                    String dataPob;
                    String dataZwrotu;
                    plik1.readUTF();
                    dataPob = plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    plik1.readUTF();
                    String a;
                    a = plik1.readUTF();

                    if (userIdTmp == user.Id_uzytkownika && idWypTmp == idWypozyczenia) {
                        plik1.seek(plik1.getFilePointer() - 2 - a.length());
                        plik1.writeUTF(simpleDateFormat.format(teraz.getTime()));

                    }


                }
                while (plik1.getFilePointer() < plik1.length());
            }

            plik1.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            System.err.println("Błąd...");


        }


    }

}
    
    
    
    
    
    
    
    

