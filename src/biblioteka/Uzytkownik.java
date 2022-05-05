/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteka;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Kuba
 */
public class Uzytkownik {
    int Id_uzytkownika;
    String Imie;
    String Nazwisko;
    private String Haslo;
    boolean isAdmin;
    boolean czyZablokowany;

    Uzytkownik() {
    }


    public void Logowanie() {

        String imie = null;
        String nazwisko = null;
        String haslo = null;


        Scanner klawiatura = new Scanner(System.in);

        boolean isOk;
        do {
            try {
                System.out.print("\n________Logowanie________");
                System.out.print("\nPodaj imie: ");
                imie = klawiatura.nextLine();
                System.out.print("\nPodaj nazwisko: ");
                nazwisko = klawiatura.nextLine();
                System.out.print("\nPodaj haslo: ");
                haslo = klawiatura.nextLine();

                isOk = true;
            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();

                System.out.print("\nWprowadź dane poprawnie \n");
            }

        } while (!isOk);


        boolean zalogowany = false;
        int idTmp;
        String imieTmp;
        String nazwiskoTmp;
        String hasloTmp;
        boolean isAdmTmp;
        try {
            RandomAccessFile plik1 = new RandomAccessFile("Uzytkownicy.bin", "rw");


            plik1.seek(0);
            if (plik1.length() > 0) {
                do {

                    plik1.readUTF();
                    idTmp = plik1.readInt();

                    plik1.readUTF();
                    imieTmp = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - imieTmp.length() - 2));
                    plik1.readUTF();


                    nazwiskoTmp = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - nazwiskoTmp.length() - 2));
                    plik1.readUTF();
                    hasloTmp = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - hasloTmp.length() - 2));
                    plik1.readUTF();
                    isAdmTmp = plik1.readBoolean();
                    plik1.seek(plik1.getFilePointer() + (40 - 1));
                    plik1.readUTF();
                    boolean isBlocked = plik1.readBoolean();
                    if (imieTmp.equals(imie) && hasloTmp.equals(haslo)) {

                        if (!isBlocked) {
                            System.out.println("Udało się zalogować");
                            Id_uzytkownika = idTmp;
                            Imie = imie;
                            Nazwisko = nazwisko;
                            zalogowany = true;

                            isAdmin = isAdmTmp;
                            break;
                        } else {
                            System.err.println("Użytkownik zablokowany");
                            break;
                        }


                    }

                }
                while (plik1.getFilePointer() < plik1.length());
            }
            plik1.close();
            if (zalogowany == false) {
                System.err.println("Nie udało się zalogować.");
                Logowanie();
            }

        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            //System.err.println("Błąd zapisu...");

        }

    }


    //dodawanie uzytkownika
    Uzytkownik(String imie, String nazwisko, String haslo, boolean isAdmin) {


        try {
            RandomAccessFile plik = new RandomAccessFile("Uzytkownicy.bin", "rw");

            plik.seek(plik.length());
            plik.writeUTF("id");
            plik.writeInt(ostatniaId() + 1);
            plik.writeUTF("imie");
            plik.writeUTF(imie);
            plik.seek(plik.getFilePointer() + (40 - imie.length() - 2));
            plik.writeUTF("nazwisko");
            plik.writeUTF(nazwisko);
            plik.seek(plik.getFilePointer() + (40 - nazwisko.length() - 2));
            plik.writeUTF("haslo");
            plik.writeUTF(haslo);
            plik.seek(plik.getFilePointer() + (40 - haslo.length() - 2));
            plik.writeUTF("isAdmin");
            plik.writeBoolean(isAdmin);
            plik.seek(plik.getFilePointer() + (40 - 1));
            plik.writeUTF("isBlocked");
            plik.writeBoolean(false);
            System.out.println("Dodano pozycję: " + (ostatniaId() + 1) + " " + imie + " " + nazwisko + " " + haslo);

            plik.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            // System.err.println("Błąd zapisu...");
        }

        System.out.println("Poprawnie zarejestrowano uzytkownika.");
    }


    public static void rejestracja(boolean czyAdmin, Uzytkownik user) {
        Scanner klawiatura = new Scanner(System.in);

        String Imie;
        String Nazwisko;
        String Haslo;
        boolean isAdmin = false;
        boolean czyZablokowany;

        boolean isOk;
        do {
            try {
                System.out.println("Aby powrócić wpisz 00");
                System.out.print("Podaj imie: ");
                Imie = klawiatura.nextLine();
                if (Imie.equalsIgnoreCase("00") && user != null) Biblioteka.menuGlowne(user);
                System.out.print("Podaj nazwisko: ");
                Nazwisko = klawiatura.nextLine();
                if (Nazwisko.equalsIgnoreCase("00") && user != null) Biblioteka.menuGlowne(user);
                System.out.print("Podaj hasło: ");
                Haslo = klawiatura.nextLine();
                if (Haslo.equalsIgnoreCase("00") && user != null) Biblioteka.menuGlowne(user);

                if (czyAdmin) {

                    System.out.println("Czy to będzie konto admina(pracownika)?");
                    System.out.print("Wpisz T jeśli tak i N jeśli nie: ");
                    String tCzyN = klawiatura.nextLine();
                    if (tCzyN.equalsIgnoreCase("T")) {
                        isAdmin = true;
                    } else {
                        isAdmin = false;
                    }

                }
                Uzytkownik newUser = new Uzytkownik(Imie, Nazwisko, Haslo, isAdmin);

                isOk = true;
            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();

                System.out.print("\nWprowadź dane poprawnie \n");
            }

        } while (!isOk);


    }


    public static boolean czyZablokowany(int id) {

        try {
            RandomAccessFile plik1 = new RandomAccessFile("Uzytkownicy.bin", "r");

            int idTmp;
            String haslo;

            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    String imie = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - imie.length() - 2));
                    plik1.readUTF();
                    String nazwisko = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - nazwisko.length() - 2));


                    plik1.readUTF();
                    haslo = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - haslo.length() - 2));
                    plik1.readUTF();
                    plik1.readBoolean();
                    plik1.seek(plik1.getFilePointer() + (40 - 1));
                    plik1.readUTF();
                    boolean b = plik1.readBoolean();
                    if (id == idTmp) {
                        plik1.close();
                        return b;
                    }

                }
                while (plik1.getFilePointer() < plik1.length());
            }

            plik1.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
            return false;
        } catch (IOException ex) {
            System.err.println("Błąd...");
            return false;
        }
        return false;
    }


    public static int ostatniaId() {
        int ostatni = 0;
        try {
            RandomAccessFile plik1 = new RandomAccessFile("Uzytkownicy.bin", "r");


            String imieTmp;
            String nazwiskoTmp;
            String hasloTmp;

            plik1.seek(0);
            if (plik1.length() > 0) {
                do {

                    plik1.readUTF();

                    ostatni = plik1.readInt();

                    plik1.readUTF();
                    imieTmp = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - imieTmp.length() - 2));
                    plik1.readUTF();
                    nazwiskoTmp = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - nazwiskoTmp.length() - 2));
                    plik1.readUTF();
                    hasloTmp = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - hasloTmp.length() - 2));
                    plik1.readUTF();
                    plik1.readBoolean();
                    plik1.seek(plik1.getFilePointer() + (40 - 1));
                    plik1.readUTF();
                    plik1.readBoolean();

                }
                while (plik1.getFilePointer() < plik1.length());
            }
            plik1.close();
            return ostatni;


        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
            return ostatni;
        } catch (IOException ex) {
            //System.err.println("Błąd zapisu...");

            return ostatni;
        }

    }


    public static String uzytkownikOId(int id) {

        String imie;
        String nazwisko;
        int idTmp;
        try {
            RandomAccessFile plik1 = new RandomAccessFile("Uzytkownicy.bin", "r");

            String haslo;

            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    imie = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - imie.length() - 2));
                    plik1.readUTF();
                    nazwisko = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - nazwisko.length() - 2));

                    if (id == idTmp) {
                        plik1.close();
                        return imie + " " + nazwisko;
                    }

                    plik1.readUTF();
                    haslo = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - haslo.length() - 2));
                    plik1.readUTF();
                    plik1.readBoolean();
                    plik1.seek(plik1.getFilePointer() + (40 - 1));
                    plik1.readUTF();
                    plik1.readBoolean();

                }
                while (plik1.getFilePointer() < plik1.length());
            }

            plik1.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
            return null;
        } catch (IOException ex) {
            System.err.println("Błąd...");
            return null;
        }
        return null;

    }


    public static void wszyscyUzytkownicy() {

        try {
            RandomAccessFile plik1 = new RandomAccessFile("Uzytkownicy.bin", "r");

            System.out.format("|%1$-90s\n", "Użytkownicy");
            System.out.format("|%1$-15s|%2$-45s|%3$-45s|%4$-30s\n", "Id użytkownika", "Imie", "Nazwisko", "Haslo");
            String imie;
            String nazwisko;
            int idTmp;
            String haslo;
            plik1.seek(0);
            if (plik1.length() > 0) {
                do {

                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    imie = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - imie.length() - 2));
                    plik1.readUTF();
                    nazwisko = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - nazwisko.length() - 2));


                    plik1.readUTF();
                    haslo = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - haslo.length() - 2));
                    plik1.readUTF();
                    plik1.readBoolean();
                    plik1.seek(plik1.getFilePointer() + (40 - 1));
                    plik1.readUTF();
                    plik1.readBoolean();

                    System.out.format("|%1$-15s|%2$-45s|%3$-45s|%4$-30s\n", idTmp, imie, nazwisko, "**********");

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


    public static void edytujUzytkownika(Uzytkownik user) {


        Scanner klawiatura = new Scanner(System.in);


        String imie = "";
        String nazwisko = "";
        String haslo = "";
        int id = 0;
        boolean isOk;
        do {
            try {


                System.out.println("Aby powrócić wpisz 00");
                System.out.println("Podaj nowe haslo: ");
                haslo = klawiatura.nextLine();
                if (haslo.equalsIgnoreCase("00")) {
                    Biblioteka.menuGlowne(user);
                }

                isOk = true;
            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();
                System.out.print("\nWprowadź dane poprawnie \n");
            }

        } while (!isOk);


        try {
            RandomAccessFile plik = new RandomAccessFile("Uzytkownicy.bin", "rw");


            plik.seek(0);
            if (plik.length() > 0) {
                do {


                    plik.readUTF();
                    if (plik.readInt() == user.Id_uzytkownika) {

                        plik.readUTF();
                        String imieTmp = plik.readUTF();
                        plik.seek(plik.getFilePointer() + 40 - imieTmp.length() - 2);

                        plik.readUTF();

                        String nazwiskoTmp = plik.readUTF();
                        plik.seek(plik.getFilePointer() + 40 - nazwiskoTmp.length() - 2);

                        plik.readUTF();

                        plik.writeUTF(haslo);
                        plik.seek(plik.getFilePointer() + 40 - haslo.length() - 2);

                    } else {
                        plik.readUTF();
                        String imieTmp = plik.readUTF();
                        plik.seek(plik.getFilePointer() + 40 - imieTmp.length() - 2);
                        plik.readUTF();
                        String nazwiskoTmp = plik.readUTF();
                        plik.seek(plik.getFilePointer() + 40 - nazwiskoTmp.length() - 2);
                        plik.readUTF();
                        String katTmp = plik.readUTF();
                        plik.seek(plik.getFilePointer() + 40 - katTmp.length() - 2);
                    }


                    plik.readUTF();
                    plik.readBoolean();
                    plik.seek(plik.getFilePointer() + (40 - 1));
                    plik.readUTF();
                    plik.readBoolean();

                }
                while (plik.getFilePointer() < plik.length());
            }

            plik.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Nie mogę zrealizować dostępu do pliku...");
        } catch (IOException ex) {
            System.err.println("Błąd zapisu...");
        }

    }


    public static void zablokujUzytkownika(Uzytkownik user) {

        int userId = -1;
        Scanner klawiatura = new Scanner(System.in);


        boolean isOk;
        do {
            try {

                System.out.println("Aby powrócić wpisz 00");
                System.out.println("Podaj id uzytkownika którego chcesz zablokować: ");
                userId = klawiatura.nextInt();
                if (userId == 0) {
                    Biblioteka.menuGlowne(user);
                }


                isOk = true;
            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();
                System.out.print("\nWprowadź dane poprawnie \n");
            }

        } while (!isOk);


        String imie;
        String nazwisko;
        int idTmp;
        try {
            RandomAccessFile plik1 = new RandomAccessFile("Uzytkownicy.bin", "rw");

            String haslo;

            plik1.seek(0);
            if (plik1.length() > 0) {
                do {


                    plik1.readUTF();
                    idTmp = plik1.readInt();
                    plik1.readUTF();
                    imie = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - imie.length() - 2));
                    plik1.readUTF();
                    nazwisko = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - nazwisko.length() - 2));


                    plik1.readUTF();
                    haslo = plik1.readUTF();
                    plik1.seek(plik1.getFilePointer() + (40 - haslo.length() - 2));
                    plik1.readUTF();
                    plik1.readBoolean();
                    plik1.seek(plik1.getFilePointer() + (40 - 1));
                    plik1.readUTF();


                    if (userId == idTmp) {
                        long a = plik1.getFilePointer();
                        boolean b = plik1.readBoolean();
                        plik1.seek(a);
                        if (b == true) {
                            plik1.writeBoolean(false);
                        } else {
                            plik1.writeBoolean(true);
                        }
                    } else {
                        plik1.readBoolean();
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



