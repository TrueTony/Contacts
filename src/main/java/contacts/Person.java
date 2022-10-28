package contacts;

import java.io.Serial;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class Person extends Contact {

    @Serial
    private static final long serialVersionUID = 1L;

    private String surname;
    private LocalDate birtDate;
    private Gender gender;

    public Person(String name, String surname, LocalDate birthDate, Gender gender, String phoneNumber) {
        super(name, phoneNumber);
        this.surname = surname;
        this.birtDate = birthDate;
        this.gender = gender;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirtDate() {
        if (birtDate == null) {
            return "[no data]";
        }
        return birtDate.toString();
    }

    public void setBirtDate(String birtDate) {
        if (Person.isValidBirthDate(birtDate)) {
            this.birtDate = LocalDate.parse(birtDate);
        } else {
            this.birtDate = null;
        }
    }

    public String getGender() {
        if (gender == null) {
            return "[no data]";
        }
        return gender.toString();
    }

    public void setGender(String gender) {
        if (Person.isValidGender(gender)) {
            this.gender = "M".equalsIgnoreCase(gender) ? Gender.MALE : Gender.FEMALE;
        } else {
            this.gender = null;
        }
    }

    public static boolean isValidBirthDate(String birthDate) {
        try {
            LocalDate.parse(birthDate);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    public static boolean isValidGender(String gender) {
        return "M".equalsIgnoreCase(gender) || "F".equalsIgnoreCase(gender);
    }

    @Override
    void showShortInfo() {
        System.out.printf("%s %s\n", getName(), getSurname());
    }

    @Override
    void showFullInfo() {
        System.out.printf("Name: %s\n", getName());
        System.out.printf("Surname: %s\n", getSurname());
        System.out.printf("Birth date: %s\n", getBirtDate());
        System.out.printf("Gender: %s\n", getGender());
        System.out.printf("Number: %s\n", getPhoneNumber());
        System.out.printf("Time created: %s\n", getCreatedTime());
        System.out.printf("Time last edit: %s\n\n", getLastEditTime());
    }

    @Override
    String[] getFieldToEdit() {
        return new String[]{"name", "surname", "birth", "gender", "number"};
    }

    @Override
    void editInfo(String field, String newValue) {
        switch (field.toLowerCase()) {
            case "name" -> setName(newValue);
            case "surname" -> setSurname(newValue);
            case "birth" -> setBirtDate(newValue);
            case "gender" -> setGender(newValue);
            case "number" -> setPhoneNumber(newValue);
        }
        setLastEditTime(LocalDateTime.now().withSecond(0).withNano(0));
    }

    @Override
    boolean found(String searchVal) {
        Pattern pattern = Pattern.compile(".*" + searchVal + ".*", Pattern.CASE_INSENSITIVE);
        return  pattern.matcher(getName()).matches() || pattern.matcher(getSurname()).matches()
                || pattern.matcher(getPhoneNumber()).matches();

    }
}
