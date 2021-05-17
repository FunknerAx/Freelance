package kz.ilyas.ambulancecall.data.model;

import java.io.Serializable;
import java.util.HashMap;

public class ClientProfile implements Serializable {
    private String uid;
    private String surname;
    private String name;
    private String patronymic;
    private String sex;
    private String birthDate;
    private String email;
    private String displayName;
    private String invalid;
    private String weight;

    public ClientProfile() {
    }

    public ClientProfile(HashMap<String, String> in) {
        this.uid = in.get("uid");
        this.surname = in.get("surname");
        this.name = in.get("name");
        this.patronymic = in.get("patronymic");
        this.sex = in.get("sex");
        this.birthDate = in.get("birthDate");
        this.email = in.get("email");
        this.displayName = in.get("displayName");
        this.weight = in.get("weight");
    }

    ;

    public ClientProfile(String uid, String surname, String name, String patronymic, String sex, String birthDate, String email, String invalid, String weight) {
        this.uid = uid;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.sex = sex;
        this.birthDate = birthDate;
        this.email = email;
        this.displayName = surname + " " + name.charAt(0) + ". " + patronymic.charAt(0) + ".";
        if (!(invalid.length() < 2)) {
            this.invalid = invalid;
        } else {
            this.invalid = "отсутствует";
        }
        this.weight = weight;
    }

    @Override
    public String toString() {
        return
                "\nФИО: " + surname + " " + name + " " + patronymic +
                        "\nПол: " + sex +
                        "\nВес: " + weight +
                        "\nДата рождения: " + birthDate+
                        "\nИнвалидность: " + (invalid == null? "Отсутствует":invalid)
                        ;
    }

    public String getUid() {
        return uid;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getSex() {
        return sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getInvalid() {
        return invalid;
    }

    public String getWeight() {
        return weight;
    }
}
