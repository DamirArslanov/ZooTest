package ZooTest.servlets.editEntity;

import ZooTest.database.interfaces.KeeperDAO;
import ZooTest.entity.Keeper;

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
@WebServlet("/deletekeeper")
public class DeleteKeeper extends HttpServlet {
    @Inject
    private KeeperDAO keeperDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = (request.getParameter("id"));
        if (id != null) {
            keeperDAO.deleteKeeper(Integer.parseInt(id));
        }
        response.sendRedirect(request.getContextPath() + "/keepers");
    }
}
