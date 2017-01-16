package ZooTest.database;

import ZooTest.entity.Keeper;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ArslanovDamir on 19.12.2016.
 */
public class SearchDB {
    private static final String URL = "jdbc:mysql://localhost:3306/zoo?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private String getKeeperBySearch = "SELECT keeper.keeperID, keeper.name, keeper.surname FROM keeper WHERE keeper.name LIKE ?";

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

    public List<Keeper> getSearchKeepers(String name) {
        List<Keeper> keepers = new LinkedList<>();

        try(Connection dbConnection = getDBConnection();
            PreparedStatement pStatement = dbConnection.prepareStatement(getKeeperBySearch)) {

            pStatement.setString(1, (name + "%"));
            ResultSet resultSet = pStatement.executeQuery();

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

}
