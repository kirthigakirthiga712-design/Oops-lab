import java.io.*;
import java.util.*;

/**
 * Contact Management System
 * Features: add, view, search, update, delete, sort, save/load from file.
 */

class Contact {
    private String name;
    private String phone;
    private String email;
    private String address;

    public Contact(String name, String phone, String email, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getName()    { return name; }
    public String getPhone()   { return phone; }
    public String getEmail()   { return email; }
    public String getAddress() { return address; }

    public void setName(String name)       { this.name = name; }
    public void setPhone(String phone)     { this.phone = phone; }
    public void setEmail(String email)     { this.email = email; }
    public void setAddress(String address) { this.address = address; }

    // Used for saving to file (fields separated by '|')
    public String toFileString() {
        return name + "|" + phone + "|" + email + "|" + address;
    }

    public static Contact fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 4) return null;
        return new Contact(p[0], p[1], p[2], p[3]);
    }

    @Override
    public String toString() {
        return String.format("%-20s %-15s %-28s %s", name, phone, email, address);
    }
}

class ContactManager {
    private final List<Contact> contacts = new ArrayList<>();
    private final String fileName;

    public ContactManager(String fileName) {
        this.fileName = fileName;
        load();
    }

    // ---------- Validation ----------
    public static boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}");
    }

    public static boolean isValidEmail(String email) {
        return email.isEmpty() || email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    }

    // ---------- Core operations ----------
    public boolean addContact(Contact c) {
        if (findByPhone(c.getPhone()) != null) {
            return false; // duplicate phone number
        }
        contacts.add(c);
        save();
        return true;
    }

    public Contact findByPhone(String phone) {
        for (Contact c : contacts) {
            if (c.getPhone().equals(phone)) return c;
        }
        return null;
    }

    public List<Contact> searchByName(String keyword) {
        List<Contact> result = new ArrayList<>();
        for (Contact c : contacts) {
            if (c.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(c);
            }
        }
        return result;
    }

    public boolean updateContact(String phone, String newName, String newPhone,
                                 String newEmail, String newAddress) {
        Contact c = findByPhone(phone);
        if (c == null) return false;

        if (!newName.isEmpty())    c.setName(newName);
        if (!newPhone.isEmpty())   c.setPhone(newPhone);
        if (!newEmail.isEmpty())   c.setEmail(newEmail);
        if (!newAddress.isEmpty()) c.setAddress(newAddress);
        save();
        return true;
    }

    public boolean deleteContact(String phone) {
        Contact c = findByPhone(phone);
        if (c == null) return false;
        contacts.remove(c);
        save();
        return true;
    }

    public List<Contact> getAllSorted() {
        List<Contact> sorted = new ArrayList<>(contacts);
        sorted.sort(Comparator.comparing(c -> c.getName().toLowerCase()));
        return sorted;
    }

    public int count() {
        return contacts.size();
    }

    // ---------- File handling ----------
    private void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (Contact c : contacts) {
                pw.println(c.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    private void load() {
        File f = new File(fileName);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                Contact c = Contact.fromFileString(line);
                if (c != null) contacts.add(c);
            }
        } catch (IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }
    }
}

public class ContactManagementSystem {
    private static final Scanner sc = new Scanner(System.in);
    private static final ContactManager manager = new ContactManager("contacts.txt");

    public static void main(String[] args) {
        int choice;
        do {
            showMenu();
            choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1: addContact();    break;
                case 2: viewContacts();  break;
                case 3: searchContact(); break;
                case 4: updateContact(); break;
                case 5: deleteContact(); break;
                case 6: System.out.println("Thank you for using Contact Management System!"); break;
                default: System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 6);
    }

    private static void showMenu() {
        System.out.println("\n===== CONTACT MANAGEMENT SYSTEM =====");
        System.out.println("1. Add Contact");
        System.out.println("2. View All Contacts");
        System.out.println("3. Search Contact by Name");
        System.out.println("4. Update Contact");
        System.out.println("5. Delete Contact");
        System.out.println("6. Exit");
        System.out.println("=====================================");
    }

    private static void addContact() {
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Phone (10 digits): ");
        String phone = sc.nextLine().trim();
        if (!ContactManager.isValidPhone(phone)) {
            System.out.println("Invalid phone number.");
            return;
        }

        System.out.print("Email (optional): ");
        String email = sc.nextLine().trim();
        if (!ContactManager.isValidEmail(email)) {
            System.out.println("Invalid email address.");
            return;
        }

        System.out.print("Address (optional): ");
        String address = sc.nextLine().trim();

        if (manager.addContact(new Contact(name, phone, email, address))) {
            System.out.println("Contact added successfully!");
        } else {
            System.out.println("A contact with this phone number already exists.");
        }
    }

    private static void viewContacts() {
        if (manager.count() == 0) {
            System.out.println("No contacts found.");
            return;
        }
        printHeader();
        for (Contact c : manager.getAllSorted()) {
            System.out.println(c);
        }
        System.out.println("Total contacts: " + manager.count());
    }

    private static void searchContact() {
        System.out.print("Enter name to search: ");
        String keyword = sc.nextLine().trim();
        List<Contact> results = manager.searchByName(keyword);
        if (results.isEmpty()) {
            System.out.println("No matching contacts found.");
            return;
        }
        printHeader();
        for (Contact c : results) {
            System.out.println(c);
        }
    }

    private static void updateContact() {
        System.out.print("Enter phone number of the contact to update: ");
        String phone = sc.nextLine().trim();
        if (manager.findByPhone(phone) == null) {
            System.out.println("Contact not found.");
            return;
        }

        System.out.println("(Leave a field blank to keep the old value)");
        System.out.print("New name: ");
        String name = sc.nextLine().trim();

        System.out.print("New phone: ");
        String newPhone = sc.nextLine().trim();
        if (!newPhone.isEmpty() && !ContactManager.isValidPhone(newPhone)) {
            System.out.println("Invalid phone number. Update cancelled.");
            return;
        }

        System.out.print("New email: ");
        String email = sc.nextLine().trim();
        if (!ContactManager.isValidEmail(email)) {
            System.out.println("Invalid email address. Update cancelled.");
            return;
        }

        System.out.print("New address: ");
        String address = sc.nextLine().trim();

        manager.updateContact(phone, name, newPhone, email, address);
        System.out.println("Contact updated successfully!");
    }

    private static void deleteContact() {
        System.out.print("Enter phone number of the contact to delete: ");
        String phone = sc.nextLine().trim();
        if (manager.deleteContact(phone)) {
            System.out.println("Contact deleted successfully!");
        } else {
            System.out.println("Contact not found.");
        }
    }

    // ---------- Helpers ----------
    private static void printHeader() {
        System.out.println(String.format("%-20s %-15s %-28s %s", "NAME", "PHONE", "EMAIL", "ADDRESS"));
        System.out.println("-".repeat(80));
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
