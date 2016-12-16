package ZooTest.database.interfaces;

import ZooTest.entity.Animal;

import java.util.List;

/**
 * Created by ArslanovDamir on 16.12.2016.
 */
public interface AnimalDao {

    public void addAnimal(Animal animal);

    public void updateAnimal(Animal animal);

    public Animal getAnimalByID(int id);

    public void deleteAnimal(int id);

    public List<Animal> getAllAnimals();


}
