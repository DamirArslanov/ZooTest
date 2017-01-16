package ZooTest.database;

import ZooTest.database.interfaces.CageDAO;
import ZooTest.entity.Cage;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ArslanovDamir on 18.12.2016.
 */
public class CageDAOImpl implements CageDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/zoo?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";


    private String addCage = "INSERT INTO cage (number) VALUES (?)";
    private String updateCage = "UPDATE cage SET cage.number = ? WHERE cage.cageID = ?";
    private String getCageByID = "SELECT cage.cageID, cage.number FROM cage WHERE cage.cageID = ?";
    private String getAllCages = "SELECT cage.cageID, cage.number  FROM cage";

    private String deleteCage = "DELETE FROM cage WHERE cage.cageID = ?";
    private String deleteLinkAnimal = "DELETE FROM cage_animals WHERE cage_animals.cageID = ?";

    private String searchCage = "SELECT cage.cageID, cage.number  FROM cage WHERE cage.number = ?";


    @Override
    public void addCage(Cage cage) {

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(addCage, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pStatement.setInt(1, cage.getNumber());
            pStatement.executeUpdate();

            try (ResultSet generatedKeys =  pStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cage.setCageID(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating cage failed, no ID obtained.");
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateCage(Cage cage) {

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(updateCage)) {

            //UPDATE cage SET cage.number = ? WHERE cage.cageID = ?
            pStatement.setInt(1, cage.getNumber());
            pStatement.setInt(2, cage.getCageID());
            pStatement.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Cage getCageByID(int id) {
        Cage cage = new Cage();

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(getCageByID)) {
            pStatement.setInt(1, id);

            //SELECT cage.cageID, cage.number FROM cage WHERE cage.cageID = ?
            ResultSet cageResult = pStatement.executeQuery();
            while (cageResult.next()) {
                cage.setCageID(cageResult.getInt(1));
                cage.setNumber(cageResult.getInt(2));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cage;
    }

    @Override
    public void deleteCage(int id) {
        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(deleteCage);
            PreparedStatement aStatement = dbConnection.prepareStatement(deleteLinkAnimal)) {


            //DELETE FROM cage_animals WHERE cage_animals.cageID = ?
            //DELETE FROM cage WHERE cage.cageID = ?
            aStatement.setInt(1, id);
            pStatement.setInt(1, id);

            aStatement.executeUpdate();
            pStatement.executeUpdate();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Cage> getAllCages() {
        List<Cage> cages = new LinkedList<>();

        try(Connection dbConnection = getDBConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet resultSet = stmt.executeQuery(getAllCages)) {

            while (resultSet.next()) {
                Cage cage = new Cage();

                cage.setCageID(resultSet.getInt(1));
                cage.setNumber(resultSet.getInt(2));
                cages.add(cage);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cages;
    }

    @Override
    public Cage findCageByNumber(int number) {
        Cage cage = new Cage();

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(searchCage)) {

            pStatement.setInt(1, number);
            ResultSet cageResult = pStatement.executeQuery();
            while (cageResult.next()) {
                cage.setCageID(cageResult.getInt(1));
                cage.setNumber(cageResult.getInt(2));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cage;
    }


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
}
