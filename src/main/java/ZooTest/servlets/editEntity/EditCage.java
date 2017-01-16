package ZooTest.servlets.editEntity;

import ZooTest.database.interfaces.CageDAO;
import ZooTest.entity.Cage;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ArslanovDamir on 19.12.2016.
 */
@WebServlet("/editcage")
public class EditCage extends HttpServlet {
    @Inject
    private CageDAO cageDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        String id = (request.getParameter("id"));
        if (id != null || !id.equals("")) {
            Cage cage = cageDAO.getCageByID(Integer.parseInt(id));
            request.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(cage));
            System.out.println(gson.toJson(cage));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        Cage cage = new Cage();

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("number");
        cage.setNumber(Integer.parseInt(name));

        String id = (request.getParameter("id"));
        System.out.println("ID: " + id);
        if (id == null || id.equals("")) {
            cageDAO.addCage(cage);
        } else {
            cage.setCageID(Integer.parseInt(id));
            cageDAO.updateCage(cage);
        }
        System.out.println("Клетка: ");
        System.out.println(cage.toString());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(cage));
        System.out.println("Сгенерированный JSON: ");
        System.out.println(gson.toJson(cage));
    }
}
