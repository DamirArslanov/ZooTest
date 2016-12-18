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
        Integer id = Integer.parseInt(request.getParameter("id"));

        if (id != null) {
            Keeper keeper = keeperDAO.getKeeperByID(id);
            request.setAttribute("keeper", keeper);
        }

        List<Keeper> keepers = keeperDAO.getAllKeepers();

        request.setAttribute("keepers", keepers);

        getServletContext().getRequestDispatcher("/WEB-INF/keepers.jsp").forward(
                request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        request.setCharacterEncoding("UTF-8");
        Keeper keeper = new Keeper();
        String name = request.getParameter("name");

        keeper.setName(name);
        String surname = request.getParameter("surname");
        keeper.setSurname(surname);
        System.out.println(keeper.toString());

        String id = (request.getParameter("id"));
        System.out.println("ID: " + id);
        if (id == null) {
            keeperDAO.addKeeper(keeper);
        } else {
            keeper.setId(Integer.parseInt(id));
            keeperDAO.updateKeeper(keeper);
        }
        System.out.println(keeper.toString());
        response.sendRedirect(request.getContextPath() + "/keepers");
    }
}
