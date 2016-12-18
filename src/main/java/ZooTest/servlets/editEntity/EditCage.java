package ZooTest.servlets.editEntity;

import ZooTest.database.interfaces.CageDAO;
import ZooTest.entity.Cage;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by ArslanovDamir on 19.12.2016.
 */
@WebServlet("/editcage")
public class EditCage extends HttpServlet {
    @Inject
    private CageDAO cageDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = (request.getParameter("id"));
        if (id != null) {
            Cage cage = cageDAO.getCageByID(Integer.parseInt(id));
            request.setAttribute("cage", cage);
        }
        List<Cage> cages = cageDAO.getAllCages();
        request.setAttribute("cages", cages);
        getServletContext().getRequestDispatcher("/WEB-INF/cages.jsp").forward(
                request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        Cage cage = new Cage();
        String name = request.getParameter("number");
        cage.setNumber(Integer.parseInt(name));

        String id = (request.getParameter("id"));
        System.out.println("ID: " + id);
        if (id == null) {
            cageDAO.addCage(cage);
        } else {
            cage.setCageID(Integer.parseInt(id));
            cageDAO.updateCage(cage);
        }
        System.out.println(cage.toString());
        response.sendRedirect(request.getContextPath() + "/cages");
    }
}
