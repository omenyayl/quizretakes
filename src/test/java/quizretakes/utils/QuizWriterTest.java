package quizretakes.utils;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

    public class QuizWriterTest {

    private static final String testQuizXMLPath = "./testQuizzes.xml";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @After
    public void tearDown() {
        File quizXMLFile = new File(testQuizXMLPath);
        if (quizXMLFile.exists()) {
            quizXMLFile.delete();
        }
    }

    private void assertEqualQuizzesAndXML(Quizzes quizzes, String quizXMLFile) {
        try {
            assertEquals(quizzes.toString(),
                    new QuizReader().read(quizXMLFile).toString());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            fail("Failed to parse the written XML file.");
            e.printStackTrace();
        }
    }

    private void runQuizWriter(Quizzes quizzes, String quizXMLPath) {
        QuizWriter quizWriter = new QuizWriter(quizXMLPath);
        try {
            quizWriter.write(quizzes);
        } catch (TransformerException | ParserConfigurationException e) {
            fail("Failed to write the quizzes XML file.");
            e.printStackTrace();
        }
    }

    /**
     * Tests whether an XML file is created that can be parsed successfully by
     * QuizReader
     */
    @Test
    public void createQuizXMLTest() {
        Quizzes quizzes = new Quizzes();
        quizzes.addQuiz(new QuizBean(
                1, 3, 5, 10, 30
        ));
        runQuizWriter(quizzes, testQuizXMLPath);
        assertEqualQuizzesAndXML(quizzes, testQuizXMLPath);
    }


    /**
     * Tests whether the QuizWriter can write multiple quizzes into an
     * XML file
     */
    @Test
    public void createMultipleQuizXMLTest() {
        Quizzes quizzes = new Quizzes();
        quizzes.addQuiz(new QuizBean(
                1, 3, 2, 10, 30
        ));
        quizzes.addQuiz(new QuizBean(
                2, 3, 3, 9, 50
        ));
        quizzes.addQuiz(new QuizBean(
                3, 3, 1, 10, 30
        ));

        runQuizWriter(quizzes, testQuizXMLPath);
        assertEqualQuizzesAndXML(quizzes, testQuizXMLPath);
    }

    /**
     * Tests whether the QuizWriter throws an IllegalArgumentException
     * if there is an empty quizzes list
     */
    @Test
    public void createEmptyQuizXMLFile() {
        Quizzes quizzes = new Quizzes();

        expectedException.expect(IllegalArgumentException.class);

        QuizWriter quizWriter = new QuizWriter(testQuizXMLPath);
        try {
            quizWriter.write(quizzes);
        } catch (TransformerException | ParserConfigurationException e) {
            fail("Failed to write empty quizzes");
            e.printStackTrace();
        }
    }

}