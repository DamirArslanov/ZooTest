package ZooTest.servlets.editEntity;

import ZooTest.database.interfaces.AnimalDAO;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ArslanovDamir on 02.01.2017.
 */
@WebServlet("/deleteanimal")
public class DeleteAnimal extends HttpServlet{
    @Inject
    private AnimalDAO animalDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = (request.getParameter("id"));
        if (id != null || !id.equals("")) {
            animalDAO.deleteAnimal(Integer.parseInt(id));
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Питомец удален!");
        }
        else {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Питомец не удален! Попробуйте снова.");
        }
    }
}
