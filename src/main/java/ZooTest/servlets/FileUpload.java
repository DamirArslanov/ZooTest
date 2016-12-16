package ZooTest.servlets;

import ZooTest.entity.Animal;
import ZooTest.entity.AnimalsList;
import ZooTest.utils.XmlFactory;
import jdk.internal.util.xml.impl.ReaderUTF8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;

/**
 * Created by Cheshire on 14.12.2016.
 */
@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
        maxFileSize=1024*1024*100,      // 10MB
        maxRequestSize=1024*1024*100)   // 100MB
public class FileUpload extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(FileUpload.class);

    /**
     * Name of the directory where uploaded files will be saved, relative to
     * the web application directory.
     */
    private static final String SAVE_DIR = "D:\\";

    /**
     * handles file upload
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
//        // gets absolute path of the web application
//        String appPath = request.getServletContext().getRealPath("");
//        // constructs path of the directory to save uploaded file
//        String savePath = File.separator + SAVE_DIR;
//
//        // creates the save directory if it does not exists
//        File fileSaveDir = new File(savePath);
//        if (!fileSaveDir.exists()) {
//            fileSaveDir.mkdir();
//        }

//        for (Part part : request.getParts()) {
//            String fileName = extractFileName(part);
//            // refines the fileName in case it is an absolute path
//            fileName = new File(fileName).getName();
//            part.write(savePath + File.separator + fileName);
//        }

        Part filePart = request.getPart("file");


        InputStream fileContent = filePart.getInputStream();
        XmlFactory xmlFactory = new XmlFactory();
        AnimalsList animalsList = xmlFactory.unmarshallFile(fileContent);

//        byte[] buffer = new byte[fileContent.available()];
//        fileContent.read(buffer);

        PrintWriter printWriter = response.getWriter();
        for (Animal animal : animalsList.getAllAnimals()) {
            printWriter.print(animal.getName());
            printWriter.print(animal.getAge());
            printWriter.print(animal.getKeeper().getName());
        }
//        printWriter.print(fileContent.read());
//        Reader reader = new ReaderUTF8(fileContent);
//        BufferedReader bufferedReader = new BufferedReader(reader);
//        System.out.println(bufferedReader.readLine());



//        request.setAttribute("message", "Upload has been done successfully!");
//        request.getRequestDispatcher("/message.jsp").forward(
//                request, response);
    }
    /**
     * Extracts file name from HTTP header content-disposition
     */
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
