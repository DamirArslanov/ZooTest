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

    private String getAnimalByID = "SELECT animal.id, animal.name, animal.animalClass, animal.age FROM animal WHERE animal.id = ?";

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
    private String searchCage = "SELECT cage.cageID FROM cage WHERE cage.number = ?";

    private String findAnimalByName = "SELECT animal.id, animal.name, animal.animalClass, animal.age, " +
            "keeper.keeperID, keeper.name, keeper.surname, " +
            "cage.cageID, cage.number FROM animal " +
            "LEFT JOIN keeper_animals ON animal.id = keeper_animals.animalID " +
            "LEFT JOIN keeper ON keeper_animals.keeperID = keeper.keeperID " +
            "LEFT JOIN cage_animals ON animal.id = cage_animals.animalID " +
            "LEFT JOIN cage ON cage_animals.cageID = cage.cageID " +
            "WHERE animal.name = ?";




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

    
    @Override
    public void addAnimal(Animal animal) {

        // Т.е. при любой работе создается новый connection и НОВЫЙ PreparedStatement - 
        //PreparedStatements привязаны к одному соединению
        //переписать или добавить метод.    ...потом
        try(Connection dbConnection = getDBConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet resultSetA = stmt.executeQuery("SELECT COUNT(*) FROM animal");
            PreparedStatement pStatement = dbConnection.prepareStatement(InsertAnimal, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement getKeeperStmt = dbConnection.prepareStatement(searchKeeper);
            PreparedStatement getCageStmt = dbConnection.prepareStatement(searchCage);
            PreparedStatement kpStatement = dbConnection.prepareStatement(setKeeper);
            PreparedStatement cpStatement = dbConnection.prepareStatement(setCage)) {

//            keeper.name = ? AND keeper.surname = ?
            getKeeperStmt.setString(1, animal.getKeeper().getName());
            getKeeperStmt.setString(2, animal.getKeeper().getSurname());
            ResultSet getKeeperResult = getKeeperStmt.executeQuery();
            getKeeperResult.next();
            int keeperID = getKeeperResult.getInt(1);
            animal.getKeeper().setId(keeperID);


            getCageStmt.setInt(1, animal.getCage().getNumber());
            ResultSet getCageResult = getCageStmt.executeQuery();
            getCageResult.next();
            int cageID = getCageResult.getInt(1);
            animal.getCage().setCageID(cageID);



//            animal (name, animalClass, age)
            pStatement.setString(1, animal.getName());
            pStatement.setString(2, animal.getAnimalClass());
            pStatement.setInt(3, animal.getAge());
            pStatement.executeUpdate();

            try (ResultSet generatedKeys =  pStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    animal.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating animal failed, no ID obtained.");
                }
            }

            //(animalID, keeperID) VALUES (?, ?)
            kpStatement.setInt(1, animal.getId());
            kpStatement.setInt(2, keeperID);
            kpStatement.executeUpdate();


            //(animalID, cageID) VALUES (?, ?)
            cpStatement.setInt(1, animal.getId());
            cpStatement.setInt(2, cageID);
            cpStatement.executeUpdate();



        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateAnimal(Animal animal) {
        System.out.println("updateAnimal " + animal.toString());

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(updateAnimal);
            PreparedStatement getKeeperStmt = dbConnection.prepareStatement(searchKeeper);
            PreparedStatement getCageStmt = dbConnection.prepareStatement(searchCage);
            PreparedStatement kpStatement = dbConnection.prepareStatement(updateKeeper);
            PreparedStatement cpStatement = dbConnection.prepareStatement(updateCage)) {

            dbConnection.setAutoCommit(false);


            getKeeperStmt.setString(1, animal.getKeeper().getName());
            getKeeperStmt.setString(2, animal.getKeeper().getSurname());
            System.out.println("updateAnimal keeper " + animal.getKeeper().getName() + " " + animal.getKeeper().getSurname());
            ResultSet getKeeperResult = getKeeperStmt.executeQuery();
            getKeeperResult.next();
            int keeperID = getKeeperResult.getInt(1);
            System.out.println("updateAnimal keeperID " + keeperID);
            animal.getKeeper().setId(keeperID);


            getCageStmt.setInt(1, animal.getCage().getNumber());
            ResultSet getCageResult = getCageStmt.executeQuery();
            getCageResult.next();
            int cageID = getCageResult.getInt(1);
            System.out.println("updateAnimal cageID " + cageID);
            animal.getCage().setCageID(cageID);

//            animal.name = ?, animal.animalClass = ?, animal.age = ? WHERE animal.id = ?
            pStatement.setString(1, animal.getName());
            pStatement.setString(2, animal.getAnimalClass());
            pStatement.setInt(3, animal.getAge());
            pStatement.setInt(4, animal.getId());

            //SET keeperID = ? WHERE animalID = ?
            kpStatement.setInt(1, keeperID);
            kpStatement.setInt(2, animal.getId());

            //SET cageID = ? WHERE animalID = ?
            cpStatement.setInt(1, cageID);
            cpStatement.setInt(2, animal.getId());


            kpStatement.executeUpdate();
            cpStatement.executeUpdate();
            pStatement.executeUpdate();

            dbConnection.commit();

            System.out.println("updateAnimal before " + animal.toString());
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

            pStatement.setInt(1, id);
            kpStatement.setInt(1, id);
            cpStatement.setInt(1,id);

            //animal.id, animal.name, animal.animalClass, animal.age
            ResultSet animalResult = pStatement.executeQuery();
            while (animalResult.next()) {
                animal.setId(animalResult.getInt("id"));
                animal.setName(animalResult.getString("name"));
                animal.setAnimalClass(animalResult.getString("animalClass"));
                animal.setAge(animalResult.getInt("age"));
            }
            //keeper.keeperID, keeper.name, keeper.surname
            ResultSet keeperResult = kpStatement.executeQuery();
            while (keeperResult.next()) {
                keeper.setId(keeperResult.getInt("keeperID"));
                keeper.setName(keeperResult.getString("name"));
                keeper.setSurname(keeperResult.getString("surname"));
                keeper.setNameSurname(keeper.getFIO());
            }
            animal.setKeeper(keeper);
            //cage.cageID, cage.number
            ResultSet cageResult = cpStatement.executeQuery();
            while (cageResult.next()) {
                cage.setCageID(cageResult.getInt("cageID"));
                cage.setNumber(cageResult.getInt("number"));
            }
            animal.setCage(cage);
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

            kpStatement.setInt(1, id);
            cpStatement.setInt(1,id);
            pStatement.setInt(1, id);


            kpStatement.executeUpdate();
            cpStatement.executeUpdate();
            pStatement.executeUpdate();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public List<Animal> getAllAnimals() {
        List<Animal> animals = new LinkedList<>();

        try(Connection dbConnection = getDBConnection();
            PreparedStatement stmt = dbConnection.prepareStatement(getFullALLAnimal);
            ResultSet resultSetA = stmt.executeQuery()) {

            while (resultSetA.next()) {
                Animal animal = new Animal();
                Keeper keeper = new Keeper();
                Cage cage = new Cage();

                animal.setId(resultSetA.getInt(1));
                animal.setName(resultSetA.getString(2));
                animal.setAnimalClass(resultSetA.getString(3));
                animal.setAge(resultSetA.getInt(4));

                keeper.setId(resultSetA.getInt(5));
                if (keeper.getId() != 0) {
                    keeper.setName(resultSetA.getString(6));
                    keeper.setSurname(resultSetA.getString(7));
                    keeper.setNameSurname(keeper.getFIO());
                    animal.setKeeper(keeper);
                } else animal.setKeeper(null);

                cage.setCageID(resultSetA.getInt(8));
                if (cage.getCageID() != 0) {
                    cage.setNumber(resultSetA.getInt(9));
                    animal.setCage(cage);
                }
                animals.add(animal);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return animals;
    }

    @Override
    public Animal findAnimalByName(String name) {
        Animal animal = new Animal();
        Keeper keeper = new Keeper();
        Cage cage = new Cage();

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(findAnimalByName)) {

            pStatement.setString(1, name);
            ResultSet resultSetA = pStatement.executeQuery();
            if (resultSetA.next()) {
                animal.setId(resultSetA.getInt(1));
                animal.setName(resultSetA.getString(2));
                animal.setAnimalClass(resultSetA.getString(3));
                animal.setAge(resultSetA.getInt(4));

                keeper.setId(resultSetA.getInt(5));
                if (keeper.getId() != 0) {
                    keeper.setName(resultSetA.getString(6));
                    keeper.setSurname(resultSetA.getString(7));
                    keeper.setNameSurname(keeper.getFIO());
                    animal.setKeeper(keeper);
                } else animal.setKeeper(null);

                cage.setCageID(resultSetA.getInt(8));
                if (cage.getCageID() != 0) {
                    cage.setNumber(resultSetA.getInt(9));
                    animal.setCage(cage);
                } else animal.setCage(null);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return animal;
    }
}
