import java.time.LocalDate;
import java.util.*;

interface ShippingService {
    void dispatchTo(Book book, String address);
}

interface MailService {
    void sendToMail(Book book, String email);
}

class ConsoleShippingService implements ShippingService {
    @Override
    public void dispatchTo(Book book, String address) {
        System.out.println("Quantum Book Store: Shipping '" + book.retrieveTitle().toLowerCase() + "' to address: " + address);
    }
}

class ConsoleMailService implements MailService {
    @Override
    public void sendToMail(Book book, String email) {
        System.out.println("Quantum Book Store: Sending '" + book.retrieveTitle() + ".try' to email: " + email);
    }
}

abstract class Book {
    protected final String isbn;
    protected final String title;
    protected final int year;
    protected final double price;
    protected int quantity;
    protected final String author;

    public Book(String isbn, String title, int year, double price, int quantity, String author) {
        if (isbn == null || isbn.isBlank()) throw new IllegalArgumentException("ISBN cannot be null");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title cannot be null");
        if (author == null || author.isBlank()) throw new IllegalArgumentException("Author cannot be null");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");

        this.isbn = isbn;
        this.title = title;
        this.year = year;
        this.price = price;
        this.quantity = quantity;
        this.author = author;
    }

    public String retrieveISBN() { return isbn; }
    public String retrieveTitle() { return title; }
    public int getPublicationYear() { return year; }
    public double getCost() { return price; }
    public int availableStock() { return quantity; }

    public void decreaseStock(int qty) {
        if (qty > quantity)
            throw new IllegalArgumentException("Warning! : Not enough quantity in stock");
        quantity -= qty;
    }

    public boolean checkIfOutdated(int maxAge, int currentYear) {
        return (currentYear - year) > maxAge;
    }

    public abstract void fulfillDelivery(String email, String address);
}

class PaperBook extends Book {
    private final ShippingService shippingService;

    public PaperBook(String isbn, String title, int year, double price, int quantity, String author, ShippingService shippingService) {
        super(isbn, title, year, price, quantity, author);
        this.shippingService = shippingService;
    }

    @Override
    public void fulfillDelivery(String email, String address) {
        shippingService.dispatchTo(this, address);
    }
}

class EBook extends Book {
    private final MailService mailService;

    public EBook(String isbn, String title, int year, double price, int quantity, String author, MailService mailService) {
        super(isbn, title, year, price, quantity, author);
        this.mailService = mailService;
    }

    @Override
    public void fulfillDelivery(String email, String address) {
        mailService.sendToMail(this, email);
    }
}

class ShowcaseBook extends Book {
    public ShowcaseBook(String isbn, String title, int year, double price, int quantity, String author) {
        super(isbn, title, year, price, quantity, author);
    }

    @Override
    public void fulfillDelivery(String email, String address) {
        throw new UnsupportedOperationException("Warning! : This book is not for sale");
    }
}

class Bookstore {
    private final Map<String, Book> inventory = new HashMap<>();
    private final int currentYear;

    public Bookstore(int currentYear) {
        this.currentYear = currentYear;
    }

    public void registerBook(Book book) {
        inventory.put(book.retrieveISBN(), book);
    }

    public List<Book> clearOldBooks(int maxAge) {
        List<Book> removed = new ArrayList<>();
        Iterator<Book> it = inventory.values().iterator();
        while (it.hasNext()) {
            Book book = it.next();
            if (book.checkIfOutdated(maxAge, currentYear)) {
                removed.add(book);
                it.remove();
            }
        }
        return removed;
    }

    public double processPurchase(String isbn, int quantity, String email, String address) {
        Book book = inventory.get(isbn);
        if (book == null)
            throw new IllegalArgumentException("Warning! : Book not found in inventory");
        if (book instanceof ShowcaseBook)
            throw new IllegalArgumentException("Warning! : This book is not for sale");

        book.decreaseStock(quantity);
        double total = quantity * book.getCost();
        book.fulfillDelivery(email, address);

        String type = (book instanceof EBook) ? "ebook" : "paper book";
        System.out.printf("Quantum book store: Paid %.0f EGP for %s%n", total, type);
        return total;
    }
}

public class QuantumBookstoreDemo {
    public static void main(String[] args) {
        int year = LocalDate.now().getYear();
        ShippingService shipService = new ConsoleShippingService();
        MailService mailService = new ConsoleMailService();
        Bookstore store = new Bookstore(year);

        Book paper = new PaperBook("P001", "paperbook", 2008, 120.0, 3, "Khaled", shipService);
        Book ebook = new EBook("E001", "Ebook", 2023, 150.0, 2, "Mai", mailService);
        Book demo = new ShowcaseBook("D001", "Demo", 2020, 0.0, 1, "Unknown");

        store.registerBook(paper);
        store.registerBook(ebook);
        store.registerBook(demo);

        System.out.println("Quantum book store :  ");
        System.out.println("--------------------------Try buying PaperBook--------------------------");
        try {
            store.processPurchase("P001", 2, "habiba@gmail.com", "madinaty");
        } catch (Exception e) {
            System.out.println("Quantum book store: " + e.getMessage());
        }

        System.out.println("--------------------------Try buying EBook--------------------------");
        try {
            store.processPurchase("E001", 1, "habiba@gmail.com", "");
        } catch (Exception e) {
            System.out.println("Quantum book storee: " + e.getMessage());
        }

        System.out.println("--------------------------Try buying demo book (should fail)--------------------------");
        try {
            store.processPurchase("D001", 1, "hh@gmail.com", "whatever");
        } catch (Exception e) {
            System.out.println(" Quantum book store: Expected error buying demo: " + e.getMessage());
        }

        System.out.println("--------------------------Try buying unknown ISBN Book --------------------------");
        try {
            store.processPurchase("UNKNOWN", 1, "hh@gmail.com", "whatever");
        } catch (Exception e) {
            System.out.println("Quantum book store: Error for unknown ISBN: " + e.getMessage());
        }

        for (Book b : store.clearOldBooks(3)) {
            System.out.printf(" Quantum book store: Removed outdated book: %s (%d)%n", b.retrieveTitle(), b.getPublicationYear());
        }
    }
}
