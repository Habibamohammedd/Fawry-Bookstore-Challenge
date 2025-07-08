### Quantum Bookstore Java Project


# 📚 Quantum Bookstore

A Java console application that simulates an online bookstore. It handles different types of books (Paper books, EBooks, and Showcase books), manages inventory, validates sales, and supports book delivery via email or shipping address.

---

## 🚀 Features

- Add books to inventory
- Buy paper or e-books
- Check if books are outdated
- Remove outdated books
- Reject showcase book purchases

---

## 🧠 OOP Concepts Used

| Concept              | Implementation Example                                |
|----------------------|--------------------------------------------------------|
| *Abstraction*       | Book is an abstract class                           |
| *Inheritance*       | PaperBook, EBook, ShowcaseBook extend Book    |
| *Polymorphism*      | deliver() method is overridden in subclasses        |
| *Encapsulation*     | Fields are private/final with public getters        |
| *Interfaces*        | ShippingService, MailService used for delivery    |

---

## 🏗️ Main Classes

| Class                  | Responsibility                                      |
|------------------------|------------------------------------------------------|
| Book (abstract)      | Base class for all books                            |
| PaperBook            | Represents a physical book                          |
| EBook                | Represents a digital e-book                         |
| ShowcaseBook         | Represents a non-sellable demo book                 |
| ShippingService      | Interface to define book shipping                   |
| MailService          | Interface to define book emailing                   |
| Bookstore            | Manages book inventory and handles sales            |
| QuantumBookstoreDemo | Main method for testing functionality               |

---

## 💻 How to Run

1. Ensure you have *JDK 17+* installed.
2. Open the project folder in *VS Code*.
3. Ensure this is set in .vscode/launch.json:

json
{
  "type": "java",
  "name": "Launch QuantumBookstoreDemo",
  "request": "launch",
  "mainClass": "QuantumBookstoreDemo",
  "projectName": "Bookstore"
}
`

4. Press F5 or click ▶️ Run to execute the code.

---

## 📦 Sample Output


Quantum book store :  
--------------------------Try buying PaperBook--------------------------
Quantum Book Store: Shipping 'paperbook' to address: madinaty
Quantum book store: Paid 240 EGP for paper book
--------------------------Try buying EBook--------------------------
Quantum Book Store: Sending 'Ebook.try' to email: habiba@gmail.com
Quantum book store: Paid 150 EGP for ebook
--------------------------Try buying demo book (should fail)--------------------------
 Quantum book store: Expected error buying demo: Warning! : This book is not for sale
--------------------------Try buying unknown ISBN Book --------------------------
Quantum book store: Error for unknown ISBN: Warning! : Book not found in inventory
 Quantum book store: Removed outdated book: paperbook (2008)
