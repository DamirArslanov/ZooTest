package ZooTest.servlets;

import ZooTest.database.interfaces.AnimalDAO;
import ZooTest.entity.Animal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


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
    @Inject
    private AnimalDAO animalDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/zoo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Animal> animals = new ArrayList<>();
        animals = animalDAO.getAllAnimals();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(animals));
        System.out.println(gson.toJson(animals));
    }
}
