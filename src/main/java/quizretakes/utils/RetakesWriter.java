package quizretakes.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

public class RetakesWriter {

    private String retakesXMLFile;

    public RetakesWriter(String retakesXMLFile) {
        this.retakesXMLFile = retakesXMLFile;
    }

    /**
     * Creates a DOM document containing the retake data
     * @param retakes a list of retakes
     * @return a Document containing the retake data
     * @throws ParserConfigurationException
     */
    private Document constructDocumentFromRetakes(Retakes retakes) throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        // root element
        Element root = document.createElement("retakes");
        document.appendChild(root);

        for (RetakeBean retake: retakes) {
            Element retakeElement = document.createElement("retake");

            Element id = document.createElement("id");
            id.appendChild(document.createTextNode(String.valueOf(retake.getID())));
            retakeElement.appendChild(id);

            Element location = document.createElement("location");
            location.appendChild(document.createTextNode(retake.getLocation()));
            retakeElement.appendChild(location);

            LocalDate retakeDate = retake.getDate();
            LocalTime retakeTime = retake.getTime();
            // Element that holds the date
            Element dateGiven = document.createElement("dateGiven");
            Element month = document.createElement("month");
            Element day = document.createElement("day");
            Element hour = document.createElement("hour");
            Element minute = document.createElement("minute");

            // set values of our date
            month.appendChild(document.createTextNode(
                    String.valueOf(retakeDate.getMonthValue())
            ));
            day.appendChild(document.createTextNode(
                    String.valueOf(retakeDate.getDayOfMonth())
            ));
            hour.appendChild(document.createTextNode(
                    String.valueOf(retakeTime.getHour())
            ));
            minute.appendChild(document.createTextNode(
                    String.valueOf(retakeTime.getMinute())
            ));
            dateGiven.appendChild(month);
            dateGiven.appendChild(day);
            dateGiven.appendChild(hour);
            dateGiven.appendChild(minute);

            // add the date given to our retake element
            retakeElement.appendChild(dateGiven);

            root.appendChild(retakeElement);
        }

        return document;

    }

    /**
     *
     * @param retakes The retakes list to write
     * @throws IllegalStateException when the retakes list is empty
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public void write(Retakes retakes)
            throws IllegalStateException, ParserConfigurationException, TransformerException {

        if (retakes.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Document retakesDocument = constructDocumentFromRetakes(retakes);

        // create the xml file
        //transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(retakesDocument);
        StreamResult streamResult = new StreamResult(new File(retakesXMLFile));

        transformer.transform(domSource, streamResult);

    }
}

