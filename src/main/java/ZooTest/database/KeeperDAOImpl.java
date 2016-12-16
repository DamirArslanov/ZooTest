package ZooTest.database;

import ZooTest.database.interfaces.KeeperDAO;
import ZooTest.entity.Keeper;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Cheshire on 16.12.2016.
 */
public class KeeperDAOImpl implements KeeperDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/zoo?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";




    private String addKeeper = "INSERT INTO keeper (keeperID, name, surname) VALUES (?, ?, ?)";
    private String updateKeeper = "UPDATE keeper SET keeper.name = ?, keeper.surname = ? WHERE keeper.keeperID = ?";
    private String getKeeperByID = "SELECT keeper.keeperID, keeper.name, keeper.surname FROM keeper WHERE keeper.keeperID = ?";
    private String getAllKeepers = "SELECT keeper.keeperID, keeper.name, keeper.surname FROM keeper";

    private String deleteKeeper = "DELETE FROM keeper WHERE keeper.keeperID = ?";
    private String deleteLinkAnimal = "DELETE FROM keeper_animals WHERE keeper_animals.keeperID = ?";



    @Override
    public void addKeeper(Keeper keeper) {

        try(Connection dbConnection = getDBConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet resultSetA = stmt.executeQuery("SELECT COUNT(*) FROM keeper");
            PreparedStatement pStatement = dbConnection.prepareStatement(addKeeper)) {

            dbConnection.setAutoCommit(false);

            resultSetA.next();
            int lastKeeperID = resultSetA.getInt(1);
            System.out.println("Сейчас максимальный Keeper ID = " + lastKeeperID);

            pStatement.setInt(1, lastKeeperID+1);
            pStatement.setString(2, keeper.getName());
            pStatement.setString(3, keeper.getSurname());

            pStatement.executeUpdate();
            dbConnection.commit();
        }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    @Override
    public void updateKeeper(Keeper keeper) {

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(updateKeeper)) {

            dbConnection.setAutoCommit(false);

            //SET keeper.name = ?, keeper.surname = ? WHERE keeper.keeperID = ?
            pStatement.setString(1, keeper.getName());
            pStatement.setString(2, keeper.getSurname());
            pStatement.setInt(3, keeper.getId());

            pStatement.executeUpdate();
            dbConnection.commit();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Keeper getKeeperByID(int id) {
        Keeper keeper = new Keeper();

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(getKeeperByID)) {

            dbConnection.setAutoCommit(false);
            pStatement.setInt(1, id);

            //keeper.keeperID, keeper.name, keeper.surname
            ResultSet keeperResult = pStatement.executeQuery();
            while (keeperResult.next()) {
                keeper.setId(keeperResult.getInt("keeperID"));
                System.out.println(keeper.getId());
                keeper.setName(keeperResult.getString("name"));
                System.out.println(keeper.getName());
                keeper.setSurname(keeperResult.getString("surname"));
                System.out.println(keeper.getSurname());
                System.out.println("-------------");
            }
            dbConnection.commit();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return keeper;
    }

    @Override
    public void deleteKeeper(int id) {
        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(deleteKeeper);
            PreparedStatement aStatement = dbConnection.prepareStatement(deleteLinkAnimal)) {

            dbConnection.setAutoCommit(false);

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
    public List<Keeper> getAllKeepers() {
        List<Keeper> keepers = new LinkedList<>();

        try(Connection dbConnection = getDBConnection();
            Statement stmt = dbConnection.createStatement();
            ResultSet resultSet = stmt.executeQuery(getAllKeepers)) {

            dbConnection.setAutoCommit(false);

            while (resultSet.next()) {
                Keeper keeper = new Keeper();

                keeper.setId(resultSet.getInt(1));
                System.out.println("KeeperID: " + keeper.getId());
                keeper.setName(resultSet.getString(2));
                System.out.println("KeeperNAME: " + keeper.getName());
                keeper.setSurname(resultSet.getString(3));
                System.out.println("KeeperSURNAME: " + keeper.getSurname());
                System.out.println("--------------");
                keepers.add(keeper);
            }

            dbConnection.commit();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return keepers;
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
