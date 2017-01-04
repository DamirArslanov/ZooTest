package ZooTest.database;

import ZooTest.database.interfaces.AnimalDAO;
import ZooTest.entity.Animal;
import ZooTest.entity.Cage;
import ZooTest.entity.Keeper;

import javax.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ArslanovDamir on 16.12.2016.
 */



public class AnimalDAOImpl implements AnimalDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/zoo?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private String InsertAnimal = "INSERT INTO animal (name, animalClass, age) " +
            "VALUES(?, ?, ?)";

    private String updateAnimal = "UPDATE animal SET animal.name = ?, animal.animalClass = ?, animal.age = ? WHERE animal.id = ?";

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

    private String deleteAnimal = "DELETE FROM animal WHERE animal.id = ?";
    private String setNullInKeeper = "DELETE FROM keeper_animals WHERE animalID = ?";
    private String setNullInCage = "DELETE FROM cage_animals WHERE animalID  = ?";

    private String getFullAnimal = "SELECT animal.id, animal.name, animal.animalClass, animal.age, " +
                                            "keeper.keeperID, keeper.name, keeper.surname, " +
                                            "cage.cageID, cage.number FROM animal " +
            "LEFT JOIN keeper_animals ON animal.id = keeper_animals.animalID " +
            "LEFT JOIN keeper ON keeper_animals.keeperID = keeper.keeperID " +
            "LEFT JOIN cage_animals ON animal.id = cage_animals.animalID " +
            "LEFT JOIN cage ON cage_animals.cageID = cage.cageID " +
            "WHERE animal.id = ?";

    private String getFullALLAnimal = "SELECT animal.id, animal.name, animal.animalClass, animal.age, " +
            "keeper.keeperID, keeper.name, keeper.surname, " +
            "cage.cageID, cage.number FROM animal " +
            "LEFT JOIN keeper_animals ON animal.id = keeper_animals.animalID " +
            "LEFT JOIN keeper ON keeper_animals.keeperID = keeper.keeperID " +
            "LEFT JOIN cage_animals ON animal.id = cage_animals.animalID " +
            "LEFT JOIN cage ON cage_animals.cageID = cage.cageID";


    private String searchKeeper = "SELECT keeper.keeperID FROM keeper WHERE keeper.name = ? AND keeper.surname = ?";




    private Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            dbConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

    //?????List<Integer>?????
    @Override
    public List<Integer> addAnimal(Animal animal) {
        Integer addedAnimal = null, addedKeeper = null, addedCage = null;
        List<Integer> added = new LinkedList<>();

        try(Connection dbConnection = getDBConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet resultSetA = stmt.executeQuery("SELECT COUNT(*) FROM animal");
            PreparedStatement pStatement = dbConnection.prepareStatement(InsertAnimal, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement getKeeperStmt = dbConnection.prepareStatement(searchKeeper);
            PreparedStatement kpStatement = dbConnection.prepareStatement(setKeeper);
            PreparedStatement cpStatement = dbConnection.prepareStatement(setCage)) {

//            dbConnection.setAutoCommit(false);

//            resultSetA.next();
//            int lastAnimalsID = resultSetA.getInt(1);
//            System.out.println("Сейчас максимальный Animal ID = "  + (lastAnimalsID+1));



//            keeper.name = ? AND keeper.surname = ?
            getKeeperStmt.setString(1, animal.getKeeper().getName());
            getKeeperStmt.setString(2, animal.getKeeper().getSurname());
            ResultSet getKeeperResult = getKeeperStmt.executeQuery();
            getKeeperResult.next();
            int keeperID = getKeeperResult.getInt(1);


//            animal (name, animalClass, age)
            pStatement.setString(1, animal.getName());
            pStatement.setString(2, animal.getAnimalClass());
            pStatement.setInt(3, animal.getAge());
            addedAnimal = pStatement.executeUpdate();

            try (ResultSet generatedKeys =  pStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    animal.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }


            //(animalID, keeperID) VALUES (?, ?)
            kpStatement.setInt(1, animal.getId());
            kpStatement.setInt(2, keeperID);
            addedKeeper = kpStatement.executeUpdate();


            //(animalID, cageID) VALUES (?, ?)
            cpStatement.setInt(1, animal.getId());

            cpStatement.setInt(2, animal.getCage().getCageID());
            addedCage = cpStatement.executeUpdate();


            added.add(addedAnimal);
            added.add(addedKeeper);
            added.add(addedCage);

//            dbConnection.commit();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return added;
    }

    @Override
    public void updateAnimal(Animal animal) {

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(updateAnimal);
            PreparedStatement getKeeperStmt = dbConnection.prepareStatement(searchKeeper);
            PreparedStatement kpStatement = dbConnection.prepareStatement(updateKeeper);
            PreparedStatement cpStatement = dbConnection.prepareStatement(updateCage)) {


            getKeeperStmt.setString(1, animal.getKeeper().getName());
            getKeeperStmt.setString(2, animal.getKeeper().getSurname());
            ResultSet getKeeperResult = getKeeperStmt.executeQuery();
            getKeeperResult.next();
            int keeperID = getKeeperResult.getInt(1);

//            dbConnection.setAutoCommit(false);

//            animal.name = ?, animal.animalClass = ?, animal.age = ? WHERE animal.id = ?
            pStatement.setString(1, animal.getName());
            pStatement.setString(2, animal.getAnimalClass());
            pStatement.setInt(3, animal.getAge());
            pStatement.setInt(4, animal.getId());

            //SET keeperID = ? WHERE animalID = ?
            kpStatement.setInt(1, keeperID);
            kpStatement.setInt(2, animal.getId());

            //SET cageID = ? WHERE animalID = ?
            cpStatement.setInt(1, animal.getCage().getCageID());
            cpStatement.setInt(2, animal.getId());


            kpStatement.executeUpdate();
            cpStatement.executeUpdate();
            pStatement.executeUpdate();
//            dbConnection.commit();

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

            dbConnection.setAutoCommit(false);

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
                keeper.setNameSurname(keeper.getFIO());
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

            dbConnection.commit();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return animal;
    }

    @Override
    public void deleteAnimal(int id) {
        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(deleteAnimal);
            PreparedStatement kpStatement = dbConnection.prepareStatement(setNullInKeeper);
            PreparedStatement cpStatement = dbConnection.prepareStatement(setNullInCage)) {
//
//            dbConnection.setAutoCommit(false);


            kpStatement.setInt(1, id);
            cpStatement.setInt(1,id);
            pStatement.setInt(1, id);


            kpStatement.executeUpdate();
            cpStatement.executeUpdate();
            pStatement.executeUpdate();

//            dbConnection.commit();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    @Override
    public List<Animal> getAllAnimals() {
        List<Animal> animals = new LinkedList<>();

        try(Connection dbConnection = getDBConnection();
//            Statement stmt = dbConnection.createStatement();
            PreparedStatement stmt = dbConnection.prepareStatement(getFullALLAnimal);
            ResultSet resultSetA = stmt.executeQuery()) {


//            dbConnection.setAutoCommit(false);
            while (resultSetA.next()) {

                Animal animal = new Animal();
                Keeper keeper = new Keeper();
                Cage cage = new Cage();

                animal.setId(resultSetA.getInt(1));
                System.out.println("AnimalID: " + animal.getId());
                animal.setName(resultSetA.getString(2));
                System.out.println("AnimalNAME: " + animal.getName());
                animal.setAnimalClass(resultSetA.getString(3));
                System.out.println("AnimalCLASS: " + animal.getAnimalClass());
                animal.setAge(resultSetA.getInt(4));
                System.out.println("AnimalAGE: " + animal.getAge());
                System.out.println("-------------");

                keeper.setId(resultSetA.getInt(5));
                if (keeper.getId() != 0) {
                    System.out.println("KeeperID: " + keeper.getId());
                    keeper.setName(resultSetA.getString(6));
                    System.out.println("KeeperNAME: " + keeper.getName());
                    keeper.setSurname(resultSetA.getString(7));
                    System.out.println("KeeperSURNAME: " + keeper.getSurname());
                    keeper.setNameSurname(keeper.getFIO());
                    System.out.println("KeeperNameAndSurname: " + keeper.getNameSurname());
                    System.out.println("--------------");
                    animal.setKeeper(keeper);
                } else animal.setKeeper(null);

                cage.setCageID(resultSetA.getInt(8));
                if (cage.getCageID() != 0) {
                    System.out.println("CageID " + cage.getCageID());
                    cage.setNumber(resultSetA.getInt(9));
                    System.out.println("CageNumber" + cage.getNumber());
                    animal.setCage(cage);
                    System.out.println("--------------");
                    System.out.println("--------_____________--------");
                }
                animals.add(animal);
            }
//            dbConnection.commit();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return animals;
    }
}
