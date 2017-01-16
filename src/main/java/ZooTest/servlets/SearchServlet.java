package ZooTest.servlets;

import ZooTest.database.SearchDB;
import ZooTest.entity.Keeper;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ArslanovDamir on 19.12.2016.
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    @Inject
    private SearchDB searchDB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String search= request.getParameter("term");
        System.out.println("Пришел запрос на поиск: " + search );

        List<Keeper> keepers = searchDB.getSearchKeepers(search);

        for (Keeper keeper : keepers) {
            System.out.println(keeper.toString());
        }
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(keepers));

    }
}
