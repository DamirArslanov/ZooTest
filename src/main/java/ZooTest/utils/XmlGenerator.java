package ZooTest.utils;

import ZooTest.entity.Animal;
import ZooTest.entity.Animals;
import ZooTest.entity.Cage;
import ZooTest.entity.Keeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by ArslanovDamir on 16.12.2016.
 */
public class XmlGenerator {


    public Animals getAnimals () throws FileNotFoundException {
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

            //Создадим клетку с id и номером клетки
            Cage cage = new Cage();
            cage.setCageID(cageCount);
            cage.setNumber(cageCount);


            //Создадим смотрителя
            Keeper keeper = new Keeper();
            keeper.setId(keepercount);
            //Возьмем случайное имя и фамилию
            keeper.setName(maleFirst.get(random.nextInt(maleFirst.size())));
            keeper.setSurname(lastName.get(random.nextInt(lastName.size())));
            keeper.setNameSurname(keeper.getFIO());

            //Пусть у каждого смотрителя будет 10 питомцев, для начала.
            for (int k = 0; k < 10; k++) {
                Animal animal = new Animal();

                //Пока не превысили лимит
                if (i < femaleFirstName.size()) {
                    //Возьмем имя из i - той строки
                    animal.setName(femaleFirstName.get(i));
                    i++;
                    animal.setId(i);
                } else break;


                animal.setAge(random.nextInt(10));
                animal.setCage(cage);
                animal.setAnimalClass(animalClass.get(random.nextInt(animalClass.size())));

                //Питомцу установим смотрителя
                animal.setKeeper(keeper);
                animals.add(animal);
            }


            //
            keepercount++;
            i--;
            cageCount++;
        }
        Animals animalsList = new Animals();
        animalsList.setAnimalList(animals);

        return animalsList;
    }
}
