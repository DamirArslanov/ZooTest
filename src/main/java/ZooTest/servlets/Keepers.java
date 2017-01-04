package ZooTest.servlets;

import ZooTest.database.KeeperDAOImpl;
import ZooTest.database.interfaces.KeeperDAO;
import ZooTest.entity.Keeper;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ArslanovDamir on 18.12.2016.
 */
@WebServlet("/keepers")
public class Keepers extends HttpServlet {
    @Inject
    private KeeperDAO keeperDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/keepers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Keeper> keepers;
        keepers = keeperDAO.getAllKeepers();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(keepers));
        System.out.println(gson.toJson(keepers));
    }
}
