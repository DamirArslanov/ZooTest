package ZooTest.servlets;

import ZooTest.entity.Animal;
import ZooTest.entity.Animals;
import ZooTest.utils.XMLtoDB;
import ZooTest.utils.XmlFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.List;
import java.util.Set;

/**
 * Created by ArslanovDamir on 15.12.2016.
 */
@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
        maxFileSize=1024*1024*100,      // 100MB
        maxRequestSize=1024*1024*100)   // 100MB

public class FileUpload extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Попали в GET UploadServlet");
        request.getRequestDispatcher("/upload.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Попали в POST UploadServlet");


        Boolean record = Boolean.valueOf(request.getParameter("record"));
        Part filePart = request.getPart("file");

        InputStream fileContent = filePart.getInputStream();
        XmlFactory xmlFactory = new XmlFactory();
        Animals animalsList = xmlFactory.unmarshallFile(fileContent);
        XMLtoDB xmLtoDB = new XMLtoDB();
        Set<Animal> fault = xmLtoDB.insertDB(animalsList, record);


        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        printWriter.write(gson.toJson(fault));
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }

}
