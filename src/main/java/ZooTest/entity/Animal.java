package ZooTest.entity;

import javax.xml.bind.annotation.*;

/**
 * Created by ArslanovDamir on 12.12.2016.
 */
@XmlType(propOrder = {"id", "name", "animalClass", "age", "cage", "keeper"})
public class Animal {
    private int id;
    private String name;
    private String animalClass;
    private int age;

    private Cage cage;

    private Keeper keeper;

    public Animal() {
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

    public String getAnimalClass() {
        return animalClass;
    }

    public void setAnimalClass(String animalClass) {
        this.animalClass = animalClass;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Cage getCage() {
        return cage;
    }

    public void setCage(Cage cage) {
        this.cage = cage;
    }

    public Keeper getKeeper() {
        return keeper;
    }

    public void setKeeper(Keeper keeper) {
        this.keeper = keeper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Animal animal = (Animal) o;

        if (id != animal.id) return false;
        if (age != animal.age) return false;
        if (!name.equals(animal.name)) return false;
        if (!animalClass.equals(animal.animalClass)) return false;
        if (cage != null ? !cage.equals(animal.cage) : animal.cage != null) return false;
        return keeper != null ? keeper.equals(animal.keeper) : animal.keeper == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + animalClass.hashCode();
        result = 31 * result + age;
        result = 31 * result + (cage != null ? cage.hashCode() : 0);
        result = 31 * result + (keeper != null ? keeper.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", animalClass='" + animalClass + '\'' +
                ", age=" + age +
                ", cage=" + cage +
                ", keeper=" + keeper +
                '}';
    }
}

