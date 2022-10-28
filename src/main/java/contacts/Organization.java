package contacts;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class Organization extends Contact {

    @Serial
    private static final long serialVersionUID = 1L;

    private String address;

    public Organization (String name, String address, String phoneNumber) {
        super(name, phoneNumber);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    void showShortInfo() {
        System.out.printf("%s\n", getName());
    }

    @Override
    void showFullInfo() {
        System.out.printf("Organization name: %s\n", getName());
        System.out.printf("Address: %s\n", getAddress());
        System.out.printf("Number: %s\n", getPhoneNumber());
        System.out.printf("Time created: %s\n", getCreatedTime());
        System.out.printf("Time last edit: %s\n\n", getLastEditTime());
    }

    @Override
    String[] getFieldToEdit() {
        return new String[]{"address", "number"};
    }

    @Override
    void editInfo(String field, String newValue) {
        switch (field.toLowerCase()) {
            case "address" -> setAddress(newValue);
            case "number" -> setPhoneNumber(newValue);
        }
        setLastEditTime(LocalDateTime.now().withSecond(0).withNano(0));
    }

    @Override
    boolean found(String searchVal) {
        Pattern pattern = Pattern.compile(".*" + searchVal + ".*", Pattern.CASE_INSENSITIVE);
        return  pattern.matcher(getName()).matches() || pattern.matcher(getPhoneNumber()).matches();

    }
}
