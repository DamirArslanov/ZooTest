package ZooTest.servlets;

import ZooTest.database.AnimalDAOImpl;
import ZooTest.database.SearchDB;
import ZooTest.entity.Animal;
import ZooTest.entity.Keeper;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ArslanovDamir on 19.12.2016.
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    @Inject
    private SearchDB searchDB;
    @Inject
    private AnimalDAOImpl animalDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        System.out.println("SearchServlet GET");
        String searchAnimal = request.getParameter("name");
        System.out.println("Пришел запрос на поиск питомца: " + searchAnimal);

        Animal animal = animalDAO.findAnimalByName(searchAnimal);
        System.out.println(animal.toString());
        if (animal != null && animal.getId() != 0) {
            response.getWriter().write("true");
        } else {
            response.getWriter().write("false");
        }




    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("SearchServlet POST");

        String searchKeeper= request.getParameter("term");
        System.out.println("Пришел запрос на поиск смотрителя: " + searchKeeper );

        List<Keeper> keepers = searchDB.getSearchKeepers(searchKeeper);

        for (Keeper keeper : keepers) {
            System.out.println(keeper.toString());
        }
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(keepers));

    }
}
