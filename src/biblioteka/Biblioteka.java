/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteka;


import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Jakub Góra i Józef Grzesik
 */
public class Biblioteka {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ekranStartowy();


    }

    public static void ekranStartowy() {

        Uzytkownik currentUser = new Uzytkownik();

        Scanner klawiatura = new Scanner(System.in);


        int liczba;
        boolean isOk;
        do {
            try {

                System.out.println("_____________Biblioteka_____________ ");
                System.out.println("");
                System.out.println("____Zaloguj się lub zarejestruj_____ ");
                System.out.println("1. Logowanie");
                System.out.println("2. Rejestracja ");
                System.out.println("0. Zamknij program ");
                System.out.println("_____________________________________ ");
                System.out.print("Wybieram: ");

                liczba = klawiatura.nextInt();


                switch (liczba) {
                    case 1:

                        currentUser.Logowanie();

                        menuGlowne(currentUser);
                        ekranStartowy();
                        break;

                    case 2:

                        Uzytkownik.rejestracja(false, null);
                        ekranStartowy();
                        break;
                    case 0:


                        break;


                    default:


                }


                isOk = true;
            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();

                System.out.print("\nWprowadź dane poprawnie \n");
            }

        } while (!isOk);


    }


    public static void menuGlowne(Uzytkownik user) {


        if (Uzytkownik.czyZablokowany(user.Id_uzytkownika)) {

            user = new Uzytkownik();
            user.Logowanie();
            if (user.Imie != null)
                menuGlowne(user);
        }

        if (user.Imie != null)
            if (!user.isAdmin) {


                Scanner klawiatura = new Scanner(System.in);

                int liczba = 9;
                boolean isOk;
                do {
                    try {
                        // Runtime.getRuntime().exec("cls");
                        System.out.println("_____________Biblioteka_____________ ");
                        System.out.println("Jesteś zalogowany jako: " + user.Imie + " " + user.Nazwisko);
                        System.out.println("_____Wybierz co chcesz zrobić_______ ");
                        System.out.println("1. Wykaz książek według kategorii");
                        System.out.println("2. Wykaz książek ");
                        System.out.println("3. Twoje wypożyczenia ");
                        System.out.println("4. Twoje oddane książki ");
                        System.out.println("5. Wypożycz nową książkę ");
                        System.out.println("6. Oddaj książkę ");
                        System.out.println("7. Twoje książki po terminie ");
                        System.out.println("8. Zmień hasło ");
                        System.out.println("0. Wyloguj się ");
                        System.out.println("_____________________________________ ");
                        System.out.print("Wybieram: ");
                        liczba = klawiatura.nextInt();


                        switch (liczba) {
                            case 1:
                                wybieraniePoKategorii(user);
                                kliknijAbyPrzejsc();
                                menuGlowne(user);
                                break;

                            case 2:

                                Ksiazka.wykazZliczoneKsiazki();
                                kliknijAbyPrzejsc();
                                menuGlowne(user);
                                break;

                            case 3:

                                Wypozyczenie.wypozyczoneKsiazki(user);
                                kliknijAbyPrzejsc();
                                menuGlowne(user);

                                break;
                            case 4:

                                Wypozyczenie.oddaneKsiazki(user);

                                kliknijAbyPrzejsc();
                                menuGlowne(user);
                                break;
                            case 5:


                                Ksiazka.wykazZliczoneKsiazki();
                                int idKsiazki;
                                System.out.println("Jeśli chcesz powrócić wpisz 00");
                                System.out.print("Wybierz książkę którą chcesz wypożyczyć z powyższych i podaj jej id: ");
                                idKsiazki = klawiatura.nextInt();

                                if (idKsiazki == 0) {
                                    menuGlowne(user);
                                }

                                Ksiazka ksiazkaDoWyp = new Ksiazka();
                                ksiazkaDoWyp.ustawKsiazkeOId(idKsiazki);


                                Wypozyczenie wypK = new Wypozyczenie(ksiazkaDoWyp, user);

                                kliknijAbyPrzejsc();
                                menuGlowne(user);

                                break;
                            case 6:


                                Wypozyczenie.wypozyczoneKsiazki(user);

                                Wypozyczenie.oddajKsiazke(user);

                                kliknijAbyPrzejsc();
                                menuGlowne(user);

                                break;
                            case 7:

                                Wypozyczenie.poTerminieKsiazki(user);

                                kliknijAbyPrzejsc();
                                menuGlowne(user);

                                break;
                            case 8:


                                Uzytkownik.edytujUzytkownika(user);

                                kliknijAbyPrzejsc();
                                menuGlowne(user);

                                break;
                            case 0:

                                ekranStartowy();

                                break;


                            default:


                        }


                        isOk = true;
                    } catch (InputMismatchException | NumberFormatException ex) {
                        isOk = false;
                        klawiatura.nextLine();

                        System.out.print("\nWprowadź dane poprawnie \n");
                    }


                } while (!isOk || liczba < 0 || liczba > 8);


            } else {


                Scanner klawiatura = new Scanner(System.in);

                int liczba = 9;
                boolean isOk;
                do {
                    try {

                        System.out.println("______________________Biblioteka_____________________ ");
                        System.out.println("Jesteś zalogowany jako: Bibliotekarz");
                        System.out.println("______________Wybierz co chcesz zrobić______________ ");
                        System.out.println("1. Wykaz czytelników");
                        System.out.println("2. Wykaz książek ");
                        System.out.println("3. Wykaz książek z przeterminowanymi zwrotami  ");
                        System.out.println("4. Historia książki ");
                        System.out.println("5. Dodaj książki ");
                        System.out.println("6. Edytuj książkę ");
                        System.out.println("7. Usuń książkę ");
                        System.out.println("8. Znajdź książkę ");
                        System.out.println("9. Dodaj czytelnika ");
                        System.out.println("10. Zablokuj czytelnika ");
                        System.out.println("11. Książki wypożyczone przez czytelnika ");
                        System.out.println("0. Wyloguj ");
                        System.out.println("_____________________________________________________ ");
                        System.out.println("Wybieram: ");
                        liczba = klawiatura.nextInt();


                        switch (liczba) {
                            case 1:
                                Uzytkownik.wszyscyUzytkownicy();
                                kliknijAbyPrzejsc();
                                menuGlowne(user);
                                break;

                            case 2:

                                Ksiazka.wykazZliczoneKsiazki();
                                kliknijAbyPrzejsc();
                                menuGlowne(user);
                                break;

                            case 3:

                                Wypozyczenie.poTerminieKsiazki();
                                kliknijAbyPrzejsc();
                                menuGlowne(user);

                                break;
                            case 4:

                                Wypozyczenie.historiaKsiazki();
                                kliknijAbyPrzejsc();
                                menuGlowne(user);
                                break;
                            case 5:

                                Ksiazka.dodawanieKsiazek(user);
                                kliknijAbyPrzejsc();
                                menuGlowne(user);

                                break;
                            case 6:


                                Ksiazka.edytujKsiazke(user);
                                kliknijAbyPrzejsc();
                                menuGlowne(user);

                                break;
                            case 7:

                                Ksiazka.usunKsiazke(user);
                                kliknijAbyPrzejsc();
                                menuGlowne(user);

                                break;

                            case 8:

                                Ksiazka.znajdzKsiazke(user);
                                kliknijAbyPrzejsc();
                                menuGlowne(user);
                                break;
                            case 9:

                                Uzytkownik.rejestracja(true, user);
                                kliknijAbyPrzejsc();
                                menuGlowne(user);
                                break;

                            case 10:

                                Uzytkownik.wszyscyUzytkownicy();
                                Uzytkownik.zablokujUzytkownika(user);

                                kliknijAbyPrzejsc();
                                menuGlowne(user);

                                break;
                            case 11:

                                Uzytkownik.wszyscyUzytkownicy();
                                Wypozyczenie.wypozyczoneKsiazki();
                                kliknijAbyPrzejsc();
                                menuGlowne(user);

                                break;

                            case 0:
                                ekranStartowy();

                                break;


                            default:


                        }


                        isOk = true;
                    } catch (InputMismatchException | NumberFormatException ex) {
                        isOk = false;
                        klawiatura.nextLine();

                        System.out.print("\nWprowadź dane poprawnie \n");
                    }

                } while (!isOk || liczba < 0 || liczba > 13);


            }


    }


    public static void wybieraniePoKategorii(Uzytkownik user) {


        Scanner klawiatura = new Scanner(System.in);
        String kategoria;
        boolean isOk;
        do {
            try {
                System.out.println("Wpisz 00 by powrócić do menu");
                System.out.println("Podaj kategorię: ");
                kategoria = klawiatura.nextLine();

                if (kategoria.equalsIgnoreCase("00"))
                    menuGlowne(user);

                Ksiazka.wykazKategorie(kategoria);


                isOk = true;

            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();

                System.out.print("\nWprowadź dane poprawnie \n");
            }

        } while (!isOk);


    }


    public static void kliknijAbyPrzejsc() {


        //Wciśnij aby przejść dalej
        Scanner klawiatura = new Scanner(System.in);

        boolean isOk;
        do {
            try {

                System.out.println("Wciśnij enter aby wrócić do menu głównego...");
                klawiatura.nextLine();

                isOk = true;
            } catch (InputMismatchException | NumberFormatException ex) {
                isOk = false;
                klawiatura.nextLine();

                System.out.print("\nWprowadź dane poprawnie \n");
            }

        } while (!isOk);

    }


}