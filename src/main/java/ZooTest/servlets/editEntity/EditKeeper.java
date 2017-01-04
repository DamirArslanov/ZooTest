package ZooTest.servlets.editEntity;

import ZooTest.database.interfaces.KeeperDAO;
import ZooTest.entity.Keeper;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by ArslanovDamir on 18.12.2016.
 */
@WebServlet("/editkeeper")
public class EditKeeper extends HttpServlet {
    @Inject
    private KeeperDAO keeperDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("Вошли в Get /editkeeper");
        Gson gson = new Gson();
        String id = (request.getParameter("id"));

        if (id != null || !id.equals("")) {
            Keeper keeper = keeperDAO.getKeeperByID(Integer.parseInt(id));
            request.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(keeper));
            System.out.println(gson.toJson(keeper));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        Keeper keeper = new Keeper();

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        keeper.setName(name);

        String surname = request.getParameter("surname");
        keeper.setSurname(surname);
        System.out.println(keeper.toString());

        String id = (request.getParameter("id"));
        System.out.println("Полученный ID смотрителя : " + id);
        if (id == null || id.equals("")) {
            keeperDAO.addKeeper(keeper);
        } else {
            keeper.setId(Integer.parseInt(id));
            keeperDAO.updateKeeper(keeper);
        }
        System.out.println("Смотритель сохранен: ");
        System.out.println(keeper.toString());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(keeper));
        System.out.println("Сгенерированный JSON: ");
        System.out.println(gson.toJson(keeper));
    }
}
