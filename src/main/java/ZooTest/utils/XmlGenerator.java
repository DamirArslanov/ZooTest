package ZooTest.utils;

import ZooTest.entity.Animal;
import ZooTest.entity.AnimalsList;
import ZooTest.entity.Cage;
import ZooTest.entity.Keeper;
import com.sun.jndi.toolkit.url.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by ArslanovDamir on 16.12.2016.
 */
public class XmlGenerator {


    public AnimalsList getAnimals () throws FileNotFoundException {
        long start = System.nanoTime();
        Scanner scanner;
        List<Animal> animals = new ArrayList<>();

        List<String> femaleFirstName = new LinkedList<>();
        scanner = new Scanner(new File("D:\\dictionary\\femaleFirst.txt"));
        while (scanner.hasNext()) {
            femaleFirstName.add(scanner.nextLine());
        }


        System.out.println(femaleFirstName.size());
        List<String> animalClass = new ArrayList<>();
        scanner = new Scanner(new File("D:\\dictionary\\animalClass.txt"));
        while (scanner.hasNext()) {
            animalClass.add(scanner.nextLine());
        }

        List<String> maleFirst = new ArrayList<>();
        scanner = new Scanner(new File("D:\\dictionary\\maleFirst.txt"));
        while (scanner.hasNext()) {
            maleFirst.add(scanner.nextLine());
        }

        List<String> lastName = new ArrayList<>();
        scanner = new Scanner(new File("D:\\dictionary\\lastName.txt"));
        while (scanner.hasNext()) {
            lastName.add(scanner.nextLine());
        }
        scanner.close();



        Random random = new Random();
        int cageCount = 1;
        int keepercount = 1;

        for (int i = 0; i < femaleFirstName.size(); i++) {
            Cage cage = new Cage();
            cage.setCageID(cageCount);
            cage.setNumber(cageCount);


            Keeper keeper = new Keeper();
            keeper.setId(keepercount);
            keeper.setName(maleFirst.get(random.nextInt(maleFirst.size())));
            keeper.setSurname(lastName.get(random.nextInt(lastName.size())));
            List<Integer> kAnimal = new ArrayList<>();
            List<Integer> animalID = new LinkedList<>();
            keeper.setKeepAnimals(animalID);
            for (int k = 0; k < 10; k++) {
                Animal animal = new Animal();

                if (i < femaleFirstName.size()) {
                    animal.setName(femaleFirstName.get(i));
                    i++;
                    animal.setId(i);
                } else break;


                animal.setAge(random.nextInt(10));
                animal.setCage(cage);
                animal.setAnimalClass(animalClass.get(random.nextInt(animalClass.size())));

                keeper.getKeepAnimals().add(animal.getId());
                animal.setKeeper(keeper);
                kAnimal.add(i);
                animals.add(animal);
            }


            keepercount++;
            //КИПЕР НЕ БУДЕТ ОБНОВЛЕН - УДАЛИТЬ И ПОЧИСТИТЬ КОД


            kAnimal.clear();
            i--;
            cageCount++;
        }
        AnimalsList animalsList = new AnimalsList();
        animalsList.setAllAnimals(animals);


        long end = System.nanoTime();
        long traceTime = end-start;
        System.out.println("Время " + traceTime);

        return animalsList;
    }
}
