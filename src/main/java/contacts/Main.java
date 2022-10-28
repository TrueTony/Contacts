package contacts;

public class Main {

    public static void main(String[] args) {

        String filename = "";
        if (args.length >= 2) {
            filename = args[0];
        }

        PhoneBook phoneBook = new PhoneBook();
        Menu menu = new Menu(phoneBook, filename);
        menu.startMenu();
    }
}
