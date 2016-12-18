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


    private String addCage = "INSERT INTO cage (cageID, number) VALUES (?, ?)";
    private String updateCage = "UPDATE cage SET cage.number = ? WHERE cage.cageID = ?";
    private String getCageByID = "SELECT cage.cageID, cage.number FROM cage WHERE cage.cageID = ?";
    private String getAllCages = "SELECT cage.cageID, cage.number  FROM cage";

    private String deleteCage = "DELETE FROM cage WHERE cage.cageID = ?";
    private String deleteLinkAnimal = "DELETE FROM cage_animals WHERE cage_animals.cageID = ?";


    @Override
    public Integer addCage(Cage cage) {
        Integer add = null;
        try(Connection dbConnection = getDBConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet resultSetA = stmt.executeQuery("SELECT COUNT(*) FROM cage");
            PreparedStatement pStatement = dbConnection.prepareStatement(addCage)) {

            dbConnection.setAutoCommit(false);

            resultSetA.next();
            int lastCageID = resultSetA.getInt(1);
            System.out.println("Сейчас максимальный Cage ID = " + lastCageID);

            pStatement.setInt(1, (lastCageID+1));
            pStatement.setInt(2, cage.getNumber());

            add = pStatement.executeUpdate();
            dbConnection.commit();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return add;
    }

    @Override
    public void updateCage(Cage cage) {

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(updateCage)) {

            dbConnection.setAutoCommit(false);

            //UPDATE cage SET cage.number = ? WHERE cage.cageID = ?
            pStatement.setInt(1, cage.getNumber());
            pStatement.setInt(2, cage.getCageID());

            pStatement.executeUpdate();
            dbConnection.commit();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Cage getCageByID(int id) {
        Cage cage = new Cage();

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(getCageByID)) {

            dbConnection.setAutoCommit(false);
            pStatement.setInt(1, id);

            //SELECT cage.cageID, cage.number FROM cage WHERE cage.cageID = ?
            ResultSet cageResult = pStatement.executeQuery();
            while (cageResult.next()) {

                cage.setCageID(cageResult.getInt(1));
                System.out.println("CageID : " + cage.getCageID());
                cage.setNumber(cageResult.getInt(2));
                System.out.println("CageNumber: " + cage.getNumber());
                System.out.println("-------------");
            }
            dbConnection.commit();
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

            dbConnection.setAutoCommit(false);

            //DELETE FROM cage WHERE cage.cageID = ?
            //DELETE FROM cage_animals WHERE cage_animals.cageID = ?
            pStatement.setInt(1, id);
            aStatement.setInt(1, id);

            pStatement.executeUpdate();
            aStatement.executeUpdate();

            dbConnection.commit();

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

            dbConnection.setAutoCommit(false);

            while (resultSet.next()) {
                Cage cage = new Cage();

                cage.setCageID(resultSet.getInt(1));
                System.out.println("CageID: " + cage.getCageID());
                cage.setNumber(resultSet.getInt(2));
                System.out.println("CageNumber: " + cage.getNumber());
                System.out.println("--------------");
                cages.add(cage);
            }
            dbConnection.commit();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cages;
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
