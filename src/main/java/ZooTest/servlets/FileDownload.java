package ZooTest.servlets;

import ZooTest.database.AnimalDAOImpl;
import ZooTest.entity.Animals;
import ZooTest.utils.XmlFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by ArslanovDamir on 11.01.2017.
 */
@WebServlet("/xml")
public class FileDownload  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        XmlFactory xmlFactory = new XmlFactory();
        AnimalDAOImpl animalDAO = new AnimalDAOImpl();
        Animals animals = new Animals();
        animals.setAnimalList(animalDAO.getAllAnimals());


        File file = xmlFactory.marshallFile(animals);

        response.setContentType("application/xml");
        response.setContentLength((int) file.length());
        response.setHeader( "Content-Disposition",
                String.format("attachment; filename=\"%s\"", file.getName()));

        OutputStream out = response.getOutputStream();

        try (FileInputStream in = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        XmlFactory xmlFactory = new XmlFactory();
//
//
//        OutputStream out = response.getOutputStream();
//        FileInputStream in = new FileInputStream("D:\\myGalactic2.xml");
//        byte[] buffer = new byte[4096];
//        int length;
//        while ((length = in.read(buffer)) > 0){
//            out.write(buffer, 0, length);
//        }
//        in.close();
//        out.flush();

    }
}
