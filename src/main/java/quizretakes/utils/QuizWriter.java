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

public class QuizWriter {

    private String quizzesXMLPath;

    /**
     * Class that handles writing quizzes to an XML file
     * @param quizzesXMLPath the path to the out XML file
     */
    public QuizWriter(String quizzesXMLPath) {
        this.quizzesXMLPath = quizzesXMLPath;
    }

    private Document constructDocumentFromQuizzes(Quizzes quizzes) throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        // root element
        Element root = document.createElement("quizzes");
        document.appendChild(root);

        for (QuizBean quiz: quizzes) {
            // root element for each quiz
            Element quizElement = document.createElement("quiz");

            Element idElement = document.createElement("id");
            idElement.appendChild(document.createTextNode(String.valueOf(quiz.getID())));
            quizElement.appendChild(idElement);

            Element dateGivenElement = document.createElement("dateGiven");
            Element month = document.createElement("month");
            Element day = document.createElement("day");
            Element hour = document.createElement("hour");
            Element minute = document.createElement("minute");

            LocalDate quizDate = quiz.getDate();
            LocalTime quiztime = quiz.getTime();
            // set values of our date
            month.appendChild(document.createTextNode(
                    String.valueOf(quizDate.getMonthValue())
            ));
            day.appendChild(document.createTextNode(
                    String.valueOf(quizDate.getDayOfMonth())
            ));
            hour.appendChild(document.createTextNode(
                    String.valueOf(quiztime.getHour())
            ));
            minute.appendChild(document.createTextNode(
                    String.valueOf(quiztime.getMinute())
            ));
            dateGivenElement.appendChild(month);
            dateGivenElement.appendChild(day);
            dateGivenElement.appendChild(hour);
            dateGivenElement.appendChild(minute);

            quizElement.appendChild(dateGivenElement);

            root.appendChild(quizElement);
        }

        return document;

    }

    /**
     * Writes an XML file containing quizzes
     * @param quizzes the quizzes to write
     */
    public void write(Quizzes quizzes) throws TransformerException, ParserConfigurationException {

        if (quizzes.isEmpty()) {
            throw new IllegalArgumentException("Quizzes cannot be empty");
        }

        Document retakesDocument = constructDocumentFromQuizzes(quizzes);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(retakesDocument);
        StreamResult streamResult = new StreamResult(new File(quizzesXMLPath));

        transformer.transform(domSource, streamResult);

    }
}
