import entities.*;
import enumerating.Periodicity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Archive archive;

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("library");
        EntityManager em = emf.createEntityManager();

        archive = new Archive(em);
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            printMenu();
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addBook(scanner);
                case "2" -> addMagazine(scanner);
                case "3" -> removeElement(scanner);
                case "4" -> searchElementByISBN(scanner);
                case "5" -> searchElementsByYear(scanner);
                case "6" -> searchElementsByTitle(scanner);
                case "7" -> searchBooksByAuthor(scanner);
                case "8" -> addUser(scanner);
                case "9" -> searchUserByCardNumber(scanner);
                case "10" -> registerLoan(scanner);
                case "11" -> listLoansByUser(scanner);
                case "12" -> listOverdueLoans();
                case "0" -> exit = true;
                default -> System.out.println("Invalid option. Try again.");
            }
        }

        em.close();
        emf.close();
        scanner.close();
        System.out.println("Program terminated.");
    }

    private static void printMenu() {
        System.out.println("\n--- LIBRARY ARCHIVE MENU ---");
        System.out.println("1 - Add a new Book");
        System.out.println("2 - Add a new Magazine");
        System.out.println("3 - Remove an Element by ISBN");
        System.out.println("4 - Search Element by ISBN");
        System.out.println("5 - Search Elements by Year");
        System.out.println("6 - Search Elements by Title");
        System.out.println("7 - Search Books by Author");
        System.out.println("8 - Add a new User");
        System.out.println("9 - Search User by Card Number");
        System.out.println("10 - Register a Loan");
        System.out.println("11 - List Loans by User Card Number");
        System.out.println("12 - List overdue Loans (not returned)");
        System.out.println("0 - Exit");
    }

    private static void addBook(Scanner scanner) {
        String isbn = readLine(scanner, "Enter Book ISBN:");
        String title = readLine(scanner, "Enter Title:");
        int year = readInt(scanner, "Enter Year:");
        int pages = readInt(scanner, "Enter Pages:");
        String author = readLine(scanner, "Enter Author:");
        String genre = readLine(scanner, "Enter Genre:");

        Book book = new Book(isbn, title, year, pages, author, genre);
        try {
            archive.addElement(book);
            System.out.println("Book added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void addMagazine(Scanner scanner) {
        String isbn = readLine(scanner, "Enter Magazine ISBN:");
        String title = readLine(scanner, "Enter Title:");
        int year = readInt(scanner, "Enter Year:");
        int pages = readInt(scanner, "Enter Pages:");
        String periodicityStr = readLine(scanner, "Enter Periodicity (SETTIMANALE, MENSILE, SEMESTRALE):").toUpperCase();

        Periodicity periodicity;
        try {
            periodicity = Periodicity.valueOf(periodicityStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid periodicity. Defaulting to 'MENSILE'.");
            periodicity = Periodicity.MENSILE;
        }

        Magazine magazine = new Magazine(isbn, title, year, pages, periodicity);
        try {
            archive.addElement(magazine);
            System.out.println("Magazine added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void removeElement(Scanner scanner) {
        String isbn = readLine(scanner, "Enter ISBN of the Element to remove:");
        boolean removed = archive.removeElementByISBN(isbn);
        System.out.println(removed ? "Element removed." : "Element not found.");
    }

    private static void searchElementByISBN(Scanner scanner) {
        String isbn = readLine(scanner, "Enter ISBN to search:");
        Element element = archive.searchByISBN(isbn);
        if (element != null) {
            System.out.println("Found: " + element);
        } else {
            System.out.println("No element found with that ISBN.");
        }
    }

    private static void searchElementsByYear(Scanner scanner) {
        int year = readInt(scanner, "Enter Year to search:");
        List<Element> elements = archive.searchByYear(year);
        if (elements.isEmpty()) {
            System.out.println("No elements found for that year.");
        } else {
            elements.forEach(System.out::println);
        }
    }

    private static void searchElementsByTitle(Scanner scanner) {
        String title = readLine(scanner, "Enter partial Title to search:");
        List<Element> elements = archive.searchByTitle(title);
        if (elements.isEmpty()) {
            System.out.println("No elements found matching the title.");
        } else {
            elements.forEach(System.out::println);
        }
    }

    private static void searchBooksByAuthor(Scanner scanner) {
        String author = readLine(scanner, "Enter Author to search:");
        List<Element> books = archive.searchByAuthor(author);
        if (books.isEmpty()) {
            System.out.println("No books found by that author.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private static void addUser(Scanner scanner) {
        String firstName = readLine(scanner, "Enter First Name:");
        String lastName = readLine(scanner, "Enter Last Name:");
        LocalDate birthDate = readDate(scanner, "Enter Birthdate (YYYY-MM-DD):");
        String cardNumber = readLine(scanner, "Enter Card Number:");

        User user = new User(firstName, lastName, birthDate, cardNumber);
        try {
            archive.addUser(user);
            System.out.println("User added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void searchUserByCardNumber(Scanner scanner) {
        String cardNumber = readLine(scanner, "Enter Card Number to search:");
        User user = archive.getUserByCardNumber(cardNumber);
        if (user != null) {
            System.out.println("Found: " + user);
        } else {
            System.out.println("No user found with that card number.");
        }
    }

    private static void registerLoan(Scanner scanner) {
        String cardNumber = readLine(scanner, "Enter User Card Number:");
        User user = archive.getUserByCardNumber(cardNumber);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        String isbn = readLine(scanner, "Enter ISBN of the Element to loan:");
        Element item = archive.searchByISBN(isbn);
        if (item == null) {
            System.out.println("Element not found.");
            return;
        }

        Loan loan = new Loan(user, item, LocalDate.now());
        archive.registerLoan(loan);
        System.out.println("Loan registered successfully. Due in 30 days.");
    }

    private static void listLoansByUser(Scanner scanner) {
        String cardNumber = readLine(scanner, "Enter User Card Number:");
        List<Loan> loans = archive.getLoansByUserCard(cardNumber);
        if (loans.isEmpty()) {
            System.out.println("No loans found for that user.");
        } else {
            loans.forEach(System.out::println);
        }
    }

    private static void listOverdueLoans() {
        List<Loan> loans = archive.getExpiredAndNotReturnedLoans();
        if (loans.isEmpty()) {
            System.out.println("No overdue loans found.");
        } else {
            loans.forEach(System.out::println);
        }
    }



    private static int readInt(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.println(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private static LocalDate readDate(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.println(message);
                return LocalDate.parse(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }

    private static String readLine(Scanner scanner, String message) {
        System.out.println(message);
        return scanner.nextLine();
    }
}
