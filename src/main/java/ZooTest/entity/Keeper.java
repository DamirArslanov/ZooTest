package ZooTest.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 * Created by ArslanovDamir on 12.12.2016.
 */
public class Keeper {

    private int id;
    private String name;
    private String surname;
    @XmlTransient
    private String nameSurname = getFIO();

    public Keeper() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Keeper keeper = (Keeper) o;

        if (id != keeper.id) return false;
        if (!name.equals(keeper.name)) return false;
        return surname.equals(keeper.surname);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + surname.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Keeper{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }

    public String getFIO() {
        return this.name + " " + this.surname;
    }
}
