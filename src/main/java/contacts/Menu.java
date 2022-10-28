package contacts;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Menu {

    private PhoneBook phoneBook;
    private final String serializationFileName;
    private Scanner scanner = new Scanner(System.in);
    private Map<Integer, Integer> indexMap = new HashMap<>();

    public Menu(PhoneBook phoneBook, String filename) {
        this.phoneBook = phoneBook;
        serializationFileName = filename;
        if (serializationFileName.length() > 0) {
            readObjects();
        }
    }

    public void startMenu() {
        boolean proc = true;
        while (proc) {
            System.out.print("[menu] Enter action (add, list, search, count, exit): ");
            String command = scanner.nextLine();
            switch (command) {
                case "add" -> addCommand();
                case "list" -> listMenu();
                case "search" -> searchMenu();
                case "count" -> countCommand();
                case "exit" -> proc = false;
            }
        }
    }

    private void searchMenu() {
        searchCommand();
        while (true) {
            System.out.print("[search] Enter action ([number], back, again): ");
            String command = scanner.nextLine();
            if (command.equals("again")) {
                searchCommand();
            } else if (isIndexId(command, true)) {
                int id = Integer.parseInt(command);
                phoneBook.getContact(indexMap.get(id)).showFullInfo();
                recordMenu(id);
                break;
            } else if (command.equals("back")) {
                break;
            }
        }
        System.out.println();
    }

    private  void recordMenu(int id) {
        boolean proc = true;
        while (proc) {
            System.out.print("[record] Enter action (edit, delete, menu): ");
            String command = scanner.nextLine();
            switch (command) {
                case "edit" -> editCommand(id);
                case "delete" -> removeCommand();
                case "menu" -> proc = false;
            }
        }
    }

    private void listMenu() {
        listCommand();
        while (true) {
            System.out.print("[list] Enter action ([number], back): ");
            String command = scanner.nextLine();
            if ("back".equals(command)) {
                break;
            } else if (isIndexId(command, false)) {
                int id = Integer.parseInt(command) - 1;
                phoneBook.getContact(id).showFullInfo();
                recordMenu(id);
                break;
            }
        }
        System.out.println();
    }

    private boolean isIndexId(String command, boolean inIndexMap) {
        int id;
        Optional<Integer> max;
        Optional<Integer> min;
        try {
            id = Integer.parseInt(command);
        } catch (Exception e) {
            return false;
        }
        if (inIndexMap) {
            if (indexMap.size() == 0) {
                return false;
            }
            min = indexMap.keySet().stream().min(Integer::compareTo);
            max = indexMap.keySet().stream().max(Integer::compareTo);
        } else {
            if (phoneBook.getSize() == 0) {
                return false;
            }
            id--;
            min = Optional.of(0);
            max = Optional.of(phoneBook.getSize());

        }
        return id >= min.get() || id <= max.get();
    }

    private void searchCommand() {
        System.out.print("Enter search query: ");
        String searchVal = scanner.nextLine();
        System.out.printf("Found %d results:\n", countFoundResults(searchVal));
        listCommand(searchVal);
        System.out.println();
    }

    private void addCommand() {
        System.out.print("Enter the type (person, organization): ");
        String input = scanner.nextLine().toLowerCase();
        if ("person".equals(input)) {
            addPerson();
        } else if ("organization".equals(input)) {
            addOrganization();
        }
    }

    private void addPerson() {
        System.out.print("Enter the name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the surname: ");
        String surname = scanner.nextLine();
        System.out.print("Enter the birth date: ");
        String birthDate = scanner.nextLine();
        LocalDate birthDateLocalDate = null;
        if (Person.isValidBirthDate(birthDate)) {
            birthDateLocalDate = LocalDate.parse(birthDate);
        } else {
            System.out.println("Bad birth date!");
        }
        System.out.print("Enter the gender (M, F): ");
        String gender = scanner.nextLine();
        Gender genderEnum = null;
        if (Person.isValidGender(gender)) {
            genderEnum = "M".equalsIgnoreCase(gender) ? Gender.MALE : Gender.FEMALE;
        } else {
            System.out.println("Bad gender!");
        }
        System.out.print("Enter the number: " );
        String number = scanner.nextLine();
        phoneBook.addContact(new Person(name, surname, birthDateLocalDate, genderEnum, number));
        writeObjects();
        System.out.println("The record added.\n");
    }

    private void addOrganization() {
        System.out.print("Enter the organization name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the address: ");
        String address = scanner.nextLine();
        System.out.print("Enter the number: " );
        String number = scanner.nextLine();
        phoneBook.addContact(new Organization(name, address, number));
        writeObjects();
        System.out.println("The record added.\n");
    }

    private void removeCommand() {
        if (phoneBook.getSize() == 0) {
            System.out.println("No records to remove!");
        } else {
            listCommand();
            System.out.print("Select a record: ");
            int delNumber = Integer.parseInt(scanner.nextLine());
            phoneBook.delContact(delNumber - 1);
            System.out.println("The record removed!\n");
        }
    }

    private void editCommand(int editNumber) {
        if (phoneBook.getSize() == 0) {
            System.out.println("No records to edit!");
        } else {
            Contact contact = phoneBook.getContact(editNumber);
            System.out.printf("Select a field (%s):", Arrays.toString(contact.getFieldToEdit()));
            String field = scanner.nextLine();
            System.out.printf("Enter %s: ", field);
            String newValue = scanner.nextLine();
            contact.editInfo(field, newValue);
            writeObjects();
            System.out.println("Saved");
            contact.showFullInfo();
        }
    }

    private void countCommand() {
        System.out.printf("The Phone Book has %d records.\n\n", phoneBook.getSize());
    }

    private void listCommand() {
        int index = 0;
        for (Contact contact : phoneBook.getAllContacts()) {
            System.out.printf("%d. ", ++index);
            contact.showShortInfo();
        }
        System.out.println();
    }

    private void listCommand(String searchVal) {
        indexMap.clear();
        int index = 0;
        int origIndex = 0;
        for (Contact contact : phoneBook.getAllContacts()) {
            if (contact.found(searchVal)) {
                System.out.printf("%d. ", ++index);
                indexMap.put(index, origIndex);
                contact.showShortInfo();
            }
            origIndex++;
        }
    }

    private int countFoundResults(String searchVal) {
        int res = 0;
        for (Contact contact : phoneBook.getAllContacts()) {
            if (contact.found(searchVal)) {
                res++;
            }
        }
        return res;
    }

    private void writeObjects() {
        if (serializationFileName.length() == 0) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(serializationFileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(phoneBook);
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readObjects() {
        try {
            FileInputStream fis = new FileInputStream(serializationFileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream obs = new ObjectInputStream(bis);
            phoneBook = (PhoneBook) obs.readObject();
            obs.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
