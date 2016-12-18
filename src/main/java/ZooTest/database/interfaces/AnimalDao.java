package ZooTest.database.interfaces;

import ZooTest.entity.Animal;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ArslanovDamir on 16.12.2016.
 */

//Maybe <T>?
public interface AnimalDAO {

    public List<Integer> addAnimal(Animal animal);

    public void updateAnimal(Animal animal);

    public Animal getAnimalByID(int id);

    public void deleteAnimal(int id);

    public List<Animal> getAllAnimals();


}
