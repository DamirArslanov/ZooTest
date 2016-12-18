package ZooTest.servlets;

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
@WebServlet("/cages")
public class Cages extends HttpServlet {
    @Inject
    private CageDAO cageDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cage> cages = cageDAO.getAllCages();
        request.setAttribute("cages", cages);
        request.getRequestDispatcher("WEB-INF/cages.jsp").forward(request, response);
    }
}
