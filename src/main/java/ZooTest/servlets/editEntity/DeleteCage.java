package ZooTest.servlets.editEntity;

import ZooTest.database.interfaces.CageDAO;

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
@WebServlet("/deletecage")
public class DeleteCage extends HttpServlet {
    @Inject
    private CageDAO cageDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = (request.getParameter("id"));
        if (id != null || !id.equals("")) {
            cageDAO.deleteCage(Integer.parseInt(id));
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Клетка удалена!");
        }
        else {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Питомец не удален! Попробуйте снова.");
        }
    }
}
