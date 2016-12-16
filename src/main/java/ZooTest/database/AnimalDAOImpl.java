package ZooTest.database;

import ZooTest.database.interfaces.AnimalDao;
import ZooTest.entity.Animal;
import ZooTest.entity.Cage;
import ZooTest.entity.Keeper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;

/**
 * Created by ArslanovDamir on 16.12.2016.
 */
public class AnimalDAOImpl implements AnimalDao {
    private static final String URL = "jdbc:mysql://localhost:3306/zoo?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private String InsertAnimal = "INSERT INTO animal (id, name, animalClass, age) " +
            "VALUES(?, ?, ?, ?)";
    private String getAnimalByID = "SELECT animal.id, animal.name, animal.animalClass, animal.age FROM animal WHERE animal.id = ?;";

    private String getKeeperByAnimalID = "SELECT keeper.keeperID, keeper.name, keeper.surname " +
            "FROM keeper JOIN keeper_animals ON keeper.keeperID = keeper_animals.keeperID " +
            "WHERE keeper_animals.animalID = ?";

    private String getCageByAnimalID = "SELECT cage.cageID, cage.number " +
            "FROM cage JOIN cage_animals ON cage.cageID = cage_animals.cageID " +
            "WHERE cage_animals.animalID = ?";

    private String setKeeper = "INSERT INTO keeper_animals (animalID, keeperID) VALUES (?, ?)";
    private String updateKeeper = "UPDATE keeper_animals SET keeperID = ? WHERE animalID = ?";

    private String setCage = "INSERT INTO cage_animals (animalID, cageID) VALUES (?, ?)";
    private String updateCage = "UPDATE cage_animals SET cageID = ? WHERE animalID = ?";



    private Connection getDBConnection() {
        Connection dbConnection = null;
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        try {
            dbConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

    @Override
    public void addAnimal(Animal animal) {

        try(Connection dbConnection = getDBConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet resultSetA = stmt.executeQuery("SELECT COUNT(*) FROM animal");
            PreparedStatement pStatement = dbConnection.prepareStatement(InsertAnimal);
            PreparedStatement kpStatement = dbConnection.prepareStatement(setKeeper);
            PreparedStatement cpStatement = dbConnection.prepareStatement(setCage)) {

            dbConnection.setAutoCommit(false);

            resultSetA.next();
            int lastAnimalsID = resultSetA.getInt(1);
            System.out.println("Сейчас максимальный Animal ID = "  + lastAnimalsID);

            pStatement.setInt(1, lastAnimalsID+1);
            pStatement.setString(2, animal.getName());
            pStatement.setString(3, animal.getAnimalClass());
            pStatement.setInt(4, animal.getAge());

            //(animalID, keeperID) VALUES (?, ?)
            kpStatement.setInt(1, animal.getId());
            kpStatement.setInt(2, animal.getKeeper().getId());

            //(animalID, cageID) VALUES (?, ?)
            cpStatement.setInt(1, animal.getId());
            cpStatement.setInt(2, animal.getCage().getCageID());

            pStatement.executeUpdate();
            kpStatement.executeUpdate();
            cpStatement.executeUpdate();

            dbConnection.commit();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateAnimal(Animal animal) {

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(InsertAnimal);
            PreparedStatement kpStatement = dbConnection.prepareStatement(updateKeeper);
            PreparedStatement cpStatement = dbConnection.prepareStatement(updateCage)) {

            dbConnection.setAutoCommit(false);

            pStatement.setInt(1, animal.getId());
            pStatement.setString(2, animal.getName());
            pStatement.setString(3, animal.getAnimalClass());
            pStatement.setInt(4, animal.getAge());

            //SET keeperID = ? WHERE animalID = ?
            kpStatement.setInt(1, animal.getKeeper().getId());
            kpStatement.setInt(2, animal.getId());

            //SET cageID = ? WHERE animalID = ?
            cpStatement.setInt(1, animal.getCage().getCageID());
            cpStatement.setInt(2, animal.getId());

            pStatement.executeUpdate();
            kpStatement.executeUpdate();
            cpStatement.executeUpdate();

            dbConnection.commit();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public Animal getAnimalByID(int id) {


        Animal animal = new Animal();
        Keeper keeper = new Keeper();
        Cage cage = new Cage();

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(getAnimalByID);
            PreparedStatement kpStatement = dbConnection.prepareStatement(getKeeperByAnimalID);
            PreparedStatement cpStatement = dbConnection.prepareStatement(getCageByAnimalID)) {


//            dbConnection.setAutoCommit(false);


            pStatement.setInt(1, id);
            kpStatement.setInt(1, id);
            cpStatement.setInt(1,id);

            //animal.id, animal.name, animal.animalClass, animal.age
            ResultSet animalResult = pStatement.executeQuery();
            while (animalResult.next()) {
                animal.setId(animalResult.getInt("id"));
                System.out.println(animal.getId());
                animal.setName(animalResult.getString("name"));
                System.out.println(animal.getName());
                animal.setAnimalClass(animalResult.getString("animalClass"));
                System.out.println(animal.getAnimalClass());
                animal.setAge(animalResult.getInt("age"));
                System.out.println(animal.getAge());
                System.out.println("-------------");
            }
            //keeper.keeperID, keeper.name, keeper.surname
            ResultSet keeperResult = kpStatement.executeQuery();
            while (keeperResult.next()) {
                keeper.setId(keeperResult.getInt("keeperID"));
                System.out.println(keeper.getId());
                keeper.setName(keeperResult.getString("name"));
                System.out.println(keeper.getName());
                keeper.setSurname(keeperResult.getString("surname"));
                System.out.println(keeper.getSurname());
                System.out.println("--------------");
            }
            animal.setKeeper(keeper);
            //cage.cageID, cage.number
            ResultSet cageResult = cpStatement.executeQuery();
            while (cageResult.next()) {
                cage.setCageID(cageResult.getInt("cageID"));
                System.out.println(cage.getCageID());
                cage.setNumber(cageResult.getInt("number"));
                System.out.println(cage.getNumber());
            }
            animal.setCage(cage);

//            dbConnection.commit();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return animal;
    }

    @Override
    public void deleteAnimal(int id) {

    }

    @Override
    public List<Animal> getAllAnimals() {
        return null;
    }
}
