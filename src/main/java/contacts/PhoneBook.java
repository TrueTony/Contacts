package contacts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhoneBook  implements Serializable {

    private List<Contact> contacts = new ArrayList<>();

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void delContact(int index) {
        contacts.remove(index);
    }

    public Contact getContact(int index) {
        return contacts.get(index);
    }

    public List<Contact> getAllContacts() {
        return contacts;
    }

    public int getSize() {
        return contacts.size();
    }

}
