package ZooTest.database;

import ZooTest.database.interfaces.KeeperDAO;
import ZooTest.entity.Keeper;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ArslanovDamir on 16.12.2016.
 */
public class KeeperDAOImpl implements KeeperDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/zoo?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";


    private String addKeeper = "INSERT INTO keeper (name, surname) VALUES (?, ?)";
    private String updateKeeper = "UPDATE keeper SET keeper.name = ?, keeper.surname = ? WHERE keeper.keeperID = ?";
    private String getKeeperByID = "SELECT keeper.keeperID, keeper.name, keeper.surname FROM keeper WHERE keeper.keeperID = ?";
    private String getAllKeepers = "SELECT keeper.keeperID, keeper.name, keeper.surname FROM keeper";

    private String deleteKeeper = "DELETE FROM keeper WHERE keeper.keeperID = ?";
    private String deleteLinkAnimal = "DELETE FROM keeper_animals WHERE keeper_animals.keeperID = ?";

    private String searchKeeper = "SELECT keeper.keeperID, keeper.name, keeper.surname FROM keeper WHERE keeper.name = ? AND keeper.surname = ?";



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
    public void addKeeper(Keeper keeper) {

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(addKeeper, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pStatement.setString(1, keeper.getName());
            pStatement.setString(2, keeper.getSurname());

            pStatement.executeUpdate();
            try (ResultSet generatedKeys =  pStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    keeper.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating keeper failed, no ID obtained.");
                }
            }
        }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    @Override
    public void updateKeeper(Keeper keeper) {

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(updateKeeper)) {

            //SET keeper.name = ?, keeper.surname = ? WHERE keeper.keeperID = ?
            pStatement.setString(1, keeper.getName());
            pStatement.setString(2, keeper.getSurname());
            pStatement.setInt(3, keeper.getId());

            pStatement.executeUpdate();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Keeper getKeeperByID(int id) {
        Keeper keeper = new Keeper();

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(getKeeperByID)) {
            pStatement.setInt(1, id);

            //keeper.keeperID, keeper.name, keeper.surname
            ResultSet keeperResult = pStatement.executeQuery();
            while (keeperResult.next()) {
                keeper.setId(keeperResult.getInt("keeperID"));
                keeper.setName(keeperResult.getString("name"));
                keeper.setSurname(keeperResult.getString("surname"));
                keeper.setNameSurname(keeper.getFIO());
            }

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

            aStatement.setInt(1, id);
            pStatement.setInt(1, id);

            aStatement.executeUpdate();
            pStatement.executeUpdate();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Keeper> getAllKeepers() {

        List<Keeper> keepers = new LinkedList<>();

        try(Connection dbConnection = getDBConnection();
            PreparedStatement stmt = dbConnection.prepareStatement(getAllKeepers);
            ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Keeper keeper = new Keeper();
                keeper.setId(resultSet.getInt(1));
                keeper.setName(resultSet.getString(2));
                keeper.setSurname(resultSet.getString(3));
                keeper.setNameSurname(keeper.getFIO());
                keepers.add(keeper);
            }

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return keepers;
    }

    @Override
    public Keeper findKeeperByNameSurname(String name, String surname) {
        Keeper keeper = new Keeper();
        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(searchKeeper)) {
            System.out.println("Пришел запрос на поиск: Имя " + name + " Фамилия " + surname);

            pStatement.setString(1, name);
            pStatement.setString(2, surname);

            //keeper.keeperID, keeper.name, keeper.surname
            ResultSet keeperResult = pStatement.executeQuery();

            if(keeperResult.next()) {
                keeper.setId(keeperResult.getInt(1));
                keeper.setName(keeperResult.getString(2));
                keeper.setSurname(keeperResult.getString(3));
                keeper.setNameSurname(keeper.getFIO());
            }

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return keeper;
    }
}
