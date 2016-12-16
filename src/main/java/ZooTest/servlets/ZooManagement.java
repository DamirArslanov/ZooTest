package ZooTest.servlets;

import ZooTest.database.AnimalToDB;
import ZooTest.database.DbInto;
import ZooTest.database.DbToEntity;
import ZooTest.entity.Animal;
import ZooTest.utils.XmlFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ArslanovDamir on 16.12.2016.
 */
@WebServlet("/management")
public class ZooManagement extends HttpServlet {

//    @Inject
//    XmlFactory xmlFactory;
//    @Inject
//    AnimalToDB animalToDB;
//    @Inject
//    DbInto dbInto;



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DbToEntity dbToEntity = new DbToEntity();
        List<Animal> animals = new ArrayList<>();

        try {
            animals = dbToEntity.getAllAnimalsInDB();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        req.setAttribute("animals", animals);
        req.getRequestDispatcher("/zoo.jsp").forward(req, resp);
    }
}
