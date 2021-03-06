package ZooTest.utils;

import ZooTest.database.AnimalDAOImpl;
import ZooTest.database.CageDAOImpl;
import ZooTest.database.KeeperDAOImpl;
import ZooTest.entity.Animal;
import ZooTest.entity.Animals;
import ZooTest.entity.Cage;
import ZooTest.entity.Keeper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ArslanovDamir on 09.01.2017.
 */
public class XMLtoDB {
    AnimalDAOImpl animalDAO = new AnimalDAOImpl();
    KeeperDAOImpl keeperDAO = new KeeperDAOImpl();
    CageDAOImpl cageDAO = new CageDAOImpl();



    public Set<Animal> insertDB(Animals animals, Boolean record) {
        List<Animal> animalList = new ArrayList<>();
        Set<Keeper> keeperSet = new LinkedHashSet<>();
        Set<Cage> cageSet = new LinkedHashSet<>();

        Set<Animal> faultInsert = new LinkedHashSet<>();
        System.out.println(animals.getAnimalList().size());

        for (Animal animal: animals.getAnimalList()) {
            System.out.println(animal.toString());
            keeperSet.add(animal.getKeeper());
            cageSet.add(animal.getCage());
        }
        System.out.println("Количество поступивших смотрителей: " + keeperSet.size());
        System.out.println("Количество поступивших клеток: " + cageSet.size());


        for (Animal animal : animals.getAnimalList()) {
            System.out.println("Animal FOR:" + animal);
            //Проверим существует ли питомец с таким именем
            Animal testAnimal = animalDAO.findAnimalByName(animal.getName());
            System.out.println("XMLtoDB: base: " + testAnimal.toString());

           //Если нет, то
            if (testAnimal != null && testAnimal.getId() == 0) {
                //Есть ли у питомца смотритель? Если есть:
                if (animal.getKeeper() != null && animal.getKeeper().getId() != 0) {
                    keeperDB(animal);
                }

                //Если у питомца прописана клетка, то
                if (animal.getCage() != null && animal.getCage().getCageID() != 0) {
                    cageDB(animal);
                }
                animalDAO.addAnimal(animal);

            } else {

                System.out.println("Попали в ELSE XMLtoDB");
                //Если стоит установка переписать всех питомцев в абзу из XML
                if (record) {
                    testAnimal.setName(animal.getName());
                    testAnimal.setAge(animal.getAge());
                    testAnimal.setAnimalClass(animal.getAnimalClass());

                    if (animal.getKeeper() != null && animal.getKeeper().getId() != 0) {
                        testAnimal.setKeeper(animal.getKeeper());
                        keeperDB(testAnimal);
                    } else testAnimal.setKeeper(null);

                    if (animal.getCage() != null && animal.getCage().getCageID() != 0) {
                        testAnimal.setCage(animal.getCage());
                        cageDB(testAnimal);
                    }else testAnimal.setCage(null);

                    System.out.println("XMLtoDB: testAnimal: " + testAnimal.toString());
                    animalDAO.updateAnimal(testAnimal);

                //Если нет установки перезаписать питомцев в базу,
                    // то просто в конце выведем список "пропущенных"
                } else {
                    faultInsert.add(animal);
                }
            }
        }
        return faultInsert;
    }

    //Пришел питомец. Есть смотритель? А в базе такой имеется?
    // Если нет смотрителя - смотрителя в базу, если есть в базе - пропишем того, кто есть в базе
    public void keeperDB(Animal animal) {
        Keeper keeper = keeperDAO.findKeeperByNameSurname(animal.getKeeper().getName(), animal.getKeeper().getSurname());
        if (keeper != null && keeper.getId() == 0) {
            keeperDAO.addKeeper(animal.getKeeper());
        } else {
            animal.setKeeper(keeper);
        }
    }

    //Тоже самое
    public void cageDB(Animal animal) {
        Cage cage = cageDAO.findCageByNumber(animal.getCage().getNumber());
        if (cage != null && cage.getCageID() == 0) {
            cageDAO.addCage(animal.getCage());
        } else {
            animal.setCage(cage);
        }
    }
}
