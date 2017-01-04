package ZooTest.servlets.editEntity;

import ZooTest.database.interfaces.AnimalDAO;
import ZooTest.entity.Animal;
import ZooTest.entity.Cage;
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
 * Created by ArslanovDamir on 19.12.2016.
 */
@WebServlet("/editanimal")
public class EditAnimal extends HttpServlet {
    @Inject
    private AnimalDAO animalDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Вошли в Get /editanimal");
        Gson gson = new Gson();
        String id = (request.getParameter("id"));

        if (id != null || !id.equals("")) {
            Animal animal = animalDAO.getAnimalByID(Integer.parseInt(id));
            request.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(animal));
            System.out.println("[" + gson.toJson(animal) + "]");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Вошли в /editanimal POST");
        request.setCharacterEncoding("UTF-8");
        Animal animal = new Animal();

        String name = request.getParameter("name");
        animal.setName(name);
        String animalClass = request.getParameter("animalClass");
        animal.setAnimalClass(animalClass);
        String age = request.getParameter("age");
        animal.setAge(Integer.parseInt(age));
        String tempKeeper = request.getParameter("keeper");
        String[] FIO = tempKeeper.split(" ", 2);
        System.out.println("Вывод массива ФИО: " + FIO[0] + " " + FIO[1]);
        Keeper keeper = new Keeper();
        keeper.setName(FIO[0]);
        keeper.setSurname(FIO[1]);
        keeper.setNameSurname(keeper.getFIO());
        animal.setKeeper(keeper);

        String cageID = request.getParameter("cage");
        Cage cage = new Cage();
        cage.setCageID(Integer.parseInt(cageID));
        cage.setNumber(cage.getCageID());
        animal.setCage(cage);

        String id = (request.getParameter("id"));
        System.out.println("Полученный ID питомца : " + id);
        if (id == null || id.equals("")) {
            animalDAO.addAnimal(animal);
        } else {
            animal.setId(Integer.parseInt(id));
            animalDAO.updateAnimal(animal);
        }
        System.out.println("Питомец сохранен: ");
        System.out.println(animal.toString());
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(animal));
        System.out.println("Сгенерированный JSON: " + "[" + gson.toJson(animal) + "]");
//        response.sendRedirect(request.getContextPath() + "/management");
    }
}
