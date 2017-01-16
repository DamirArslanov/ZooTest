package ZooTest.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

/**
 * Created by ArslanovDamir on 12.12.2016.
 */

public class Cage {
    private int cageID;
    private int number;


    public Cage() {
    }

    public int getCageID() {
        return cageID;
    }

    public void setCageID(int cageID) {
        this.cageID = cageID;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cage cage = (Cage) o;

        if (cageID != cage.cageID) return false;
        return number == cage.number;

    }

    @Override
    public int hashCode() {
        int result = cageID;
        result = 31 * result + number;
        return result;
    }

    @Override
    public String toString() {
        return "Cage{" +
                "cageID=" + cageID +
                ", number=" + number +
                '}';
    }
}
