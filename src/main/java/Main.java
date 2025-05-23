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
        System.out.println("Enter Book ISBN:");
        String isbn = scanner.nextLine();
        System.out.println("Enter Title:");
        String title = scanner.nextLine();
        System.out.println("Enter Year:");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter Pages:");
        int pages = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter Author:");
        String author = scanner.nextLine();
        System.out.println("Enter Genre:");
        String genre = scanner.nextLine();

        Book book = new Book(isbn, title, year, pages, author, genre);
        archive.addElement(book);
        System.out.println("Book added successfully.");
    }

    private static void addMagazine(Scanner scanner) {
        System.out.println("Enter Magazine ISBN:");
        String isbn = scanner.nextLine();
        System.out.println("Enter Title:");
        String title = scanner.nextLine();
        System.out.println("Enter Year:");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter Pages:");
        int pages = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter Periodicity (SETTIMANALE, MENSILE, SEMESTRALE):");
        String periodicityStr = scanner.nextLine().toUpperCase();

        Periodicity periodicity;
        try {
            periodicity = Periodicity.valueOf(periodicityStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid periodicity. Defaulting to 'MENSILE'.");
            periodicity = Periodicity.MENSILE;
        }

        Magazine magazine = new Magazine(isbn, title, year, pages, periodicity);
        archive.addElement(magazine);
        System.out.println("Magazine added successfully.");
    }

    private static void removeElement(Scanner scanner) {
        System.out.println("Enter ISBN of the Element to remove:");
        String isbn = scanner.nextLine();
        archive.removeElementByISBN(isbn);
        System.out.println("Element removed if existed.");
    }

    private static void searchElementByISBN(Scanner scanner) {
        System.out.println("Enter ISBN to search:");
        String isbn = scanner.nextLine();
        Element element = archive.searchByISBN(isbn);
        if (element != null) {
            System.out.println("Found: " + element);
        } else {
            System.out.println("No element found with that ISBN.");
        }
    }

    private static void searchElementsByYear(Scanner scanner) {
        System.out.println("Enter Year to search:");
        int year = Integer.parseInt(scanner.nextLine());
        List<Element> elements = archive.searchByYear(year);
        if (elements.isEmpty()) {
            System.out.println("No elements found for that year.");
        } else {
            elements.forEach(System.out::println);
        }
    }

    private static void searchElementsByTitle(Scanner scanner) {
        System.out.println("Enter partial Title to search:");
        String title = scanner.nextLine();
        List<Element> elements = archive.searchByTitle(title);
        if (elements.isEmpty()) {
            System.out.println("No elements found matching the title.");
        } else {
            elements.forEach(System.out::println);
        }
    }

    private static void searchBooksByAuthor(Scanner scanner) {
        System.out.println("Enter Author to search:");
        String author = scanner.nextLine();
        List<Element> books = archive.searchByAuthor(author);
        if (books.isEmpty()) {
            System.out.println("No books found by that author.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private static void addUser(Scanner scanner) {
        System.out.println("Enter First Name:");
        String firstName = scanner.nextLine();
        System.out.println("Enter Last Name:");
        String lastName = scanner.nextLine();
        System.out.println("Enter Birthdate (YYYY-MM-DD):");
        LocalDate birthDate = LocalDate.parse(scanner.nextLine());
        System.out.println("Enter Card Number:");
        String cardNumber = scanner.nextLine();

        User user = new User(firstName, lastName, birthDate, cardNumber);
        archive.addUser(user);
        System.out.println("User added successfully.");
    }

    private static void searchUserByCardNumber(Scanner scanner) {
        System.out.println("Enter Card Number to search:");
        String cardNumber = scanner.nextLine();
        User user = archive.getUserByCardNumber(cardNumber);
        if (user != null) {
            System.out.println("Found: " + user);
        } else {
            System.out.println("No user found with that card number.");
        }
    }

    private static void registerLoan(Scanner scanner) {
        System.out.println("Enter User Card Number:");
        String cardNumber = scanner.nextLine();
        User user = archive.getUserByCardNumber(cardNumber);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("Enter ISBN of the Element to loan:");
        String isbn = scanner.nextLine();
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
        System.out.println("Enter User Card Number:");
        String cardNumber = scanner.nextLine();
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
}
