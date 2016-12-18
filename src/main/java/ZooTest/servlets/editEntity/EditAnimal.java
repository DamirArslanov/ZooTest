package ZooTest.servlets.editEntity;

import ZooTest.database.interfaces.AnimalDAO;
import ZooTest.entity.Animal;
import ZooTest.entity.Cage;
import ZooTest.entity.Keeper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by ArslanovDamir on 19.12.2016.
 */
public class EditAnimal extends HttpServlet {
    @Inject
    private AnimalDAO animalDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        if (id != null) {
            Animal animal = animalDAO.getAnimalByID(Integer.parseInt(id));
            request.setAttribute("animal", animal);
        }
//        List<Animal> animals = animalDAO.getAllAnimals();
//        request.setAttribute("animals", animals);
        getServletContext().getRequestDispatcher("/WEB-INF/animals.jsp").forward(
                request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Animal animal = new Animal();

        String name = request.getParameter("name");
        animal.setName(name);
        String animalClass = request.getParameter("animalClass");
        animal.setAnimalClass(animalClass);
        String age = request.getParameter("age");
        animal.setAge(Integer.parseInt(age));
        String cageID = request.getParameter("cage");
        Cage cage = new Cage();
        cage.setCageID(Integer.parseInt(cageID));
        animal.setCage(cage);

        String id = (request.getParameter("id"));
        System.out.println("ID: " + id);
        if (id == null) {
            animalDAO.addAnimal(animal);
        } else {
            animal.setId(Integer.parseInt(id));
            animalDAO.updateAnimal(animal);
        }
        System.out.println(animal.toString());
        response.sendRedirect(request.getContextPath() + "/management");
    }
}
