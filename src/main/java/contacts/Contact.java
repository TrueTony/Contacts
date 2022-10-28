package contacts;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Contact implements Serializable {

    private String name;
    private String phoneNumber;
    private final LocalDateTime createdTime;
    private LocalDateTime lastEditTime;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Contact(String name, String phoneNumber) {
        this.name = name;
        setPhoneNumber(phoneNumber);
        this.createdTime = LocalDateTime.now().withSecond(0).withNano(0);
        setLastEditTime(LocalDateTime.now().withSecond(0).withNano(0));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (checkNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            System.out.println("Wrong number format!");
            this.phoneNumber = "[no number]";
        }
    }

    public boolean hasNumber() {
        return !phoneNumber.equals("[no number]");
    }

    private boolean checkNumber(String number) {
        Pattern pattern = Pattern.compile("^\\+?(\\(\\w+\\)|\\w+[ -]\\(\\w{2,}\\)|\\w+)([ -]\\w{2,})*",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(LocalDateTime lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    abstract void showShortInfo();

    abstract void showFullInfo();

    abstract String[] getFieldToEdit();

    abstract void editInfo(String field, String newValue);

    abstract boolean found(String searchVal);

}
