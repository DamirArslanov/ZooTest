package ZooTest.utils;

import ZooTest.entity.AnimalsList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * Created by ArslanovDamir on 15.12.2016.
 */

public class XmlFactory {

    public void marshallFile(AnimalsList animalsList) {
        try {
            JAXBContext context = JAXBContext.newInstance(animalsList.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            //Должны тут переписать под возвращение файла
            marshaller.marshal(animalsList, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public AnimalsList unmarshallFile(InputStream inputStream) {
        AnimalsList animalsList = null;
        try {
            JAXBContext context = JAXBContext.newInstance(AnimalsList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            animalsList = (AnimalsList) unmarshaller.unmarshal(inputStream);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return animalsList;
    }
}
