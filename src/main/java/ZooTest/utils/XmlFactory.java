package ZooTest.utils;

import ZooTest.entity.Animals;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by ArslanovDamir on 15.12.2016.
 */

public class XmlFactory {

    public File marshallFile(Animals animals) {
        try {
            File file = new File("test.xml");
            JAXBContext context = JAXBContext.newInstance(animals.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(animals, file);
            return file;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Animals unmarshallFile(InputStream inputStream) {
        Animals animals = null;
        try {
            JAXBContext context = JAXBContext.newInstance(Animals.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            animals = (Animals) unmarshaller.unmarshal(inputStream);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return animals;
    }
}
