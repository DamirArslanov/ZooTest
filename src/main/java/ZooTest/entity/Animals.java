package ZooTest.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Created by ArslanovDamir on 12.12.2016.
 */

@XmlRootElement(name = "Animals")
public class Animals {

    private List<Animal> animalList;

    @XmlElement(name = "Animal")
    public List<Animal> getAnimalList() {
        return animalList;
    }

    public void setAnimalList(List<Animal> animalList) {
        this.animalList = animalList;
    }
}
