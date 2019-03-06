package quizretakes.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class RetakesWriterTest {

    private static final String retakesXMLPath = "./out/test-retakes.xml";

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        File retakesXMLFile = new File(retakesXMLPath);
        if (retakesXMLFile.exists()) {
            retakesXMLFile.delete();
        }
    }


    /**
     * Compares the retakes object with the parsed contents of the retakesXMLFile
     * @param retakes the retakes object
     * @param retakesXMLFile the path to the retakes XML file
     */
    private void assertSameRetakesFromWrittenXML(Retakes retakes, String retakesXMLFile) {
        Retakes readRetakes = null;
        try {
            readRetakes = new RetakesReader().read(retakesXMLFile);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            fail("Failed to read the written retakes XML.");
        }

        assertEquals(
                "Retakes have not been written correctly.",
                retakes.toString(),
                readRetakes.toString());
    }

    /**
     * Created a test that instantiates a RetakesWriter with a Retakes list,
     * and calls its write function with the path to the output test xml file.
     * Then, it uses RetakesReader to read that written XML file and tests
     * whether the retakes strings are the same.
     */
    @Test
    public void testWriteSingleRetake() {
        Retakes retakes = new Retakes(
                1,
                "ENGR",
                3,
                5,
                10,
                30
        );
        RetakesWriter retakesWriter = new RetakesWriter(retakesXMLPath);
        try {
            retakesWriter.write(retakes);
        } catch (ParserConfigurationException | TransformerException e) {
            fail("Failed to write a new retakes XML file");
        }

        assertSameRetakesFromWrittenXML(retakes, retakesXMLPath);

    }


    /**
     * Instantiates a list of multiple retakes, and tests whether
     * retakes.toString is equal to the parsed written retakes XML
     */
    @Test
    public void testWriteMultipleRetakes() {
        Retakes retakes = new Retakes();
        retakes.addRetake(
                new RetakeBean(
                        1,
                        "ENGR 1102",
                        3,
                        5,
                        10,
                        30
                )
        );

        retakes.addRetake(
                new RetakeBean(
                        2,
                        "ENGR 1102",
                        3,
                        10,
                        9,
                        30
                )
        );

        retakes.addRetake(
                new RetakeBean(
                        3,
                        "ENGR 1105",
                        3,
                        10,
                        11,
                        30
                )
        );

        RetakesWriter retakesWriter = new RetakesWriter(retakesXMLPath);
        try {
            retakesWriter.write(retakes);
        } catch (ParserConfigurationException | TransformerException e) {
            fail("Failed to write a new retakes XML file");
        }

        assertSameRetakesFromWrittenXML(retakes, retakesXMLPath);
    }

    /**
     * Tests whether an IllegalArgumentException is thrown when attempting
     * to write the XML with an empty retakes list.
     */
    @Test
    public void testWriteEmptyRetakes() {
        Retakes retakes = new Retakes();

        RetakesWriter retakesWriter = new RetakesWriter(retakesXMLPath);
        exceptionRule.expect(IllegalArgumentException.class);
        try {
            retakesWriter.write(retakes);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

}