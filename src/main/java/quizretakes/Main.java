package quizretakes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage pStage;
    public static String pCourseID;

    @Override
    public void start(Stage primaryStage) throws Exception{
        pStage = primaryStage;
        pStage.setTitle("Quiz Retakes");
        switchScene(Layouts.LOGIN, getClass(), 300, 300);
    }

    public static Stage getStage(){
        return pStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void switchScene(Layouts resourcePath, Class<?> context, int width, int height) throws IOException {
        Parent root = FXMLLoader.load(context.getResource(resourcePath.toString()));
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(context.getResource("/bootstrap3.css").toExternalForm());

        pStage.setScene(scene);
        pStage.show();
    }

}

/*

User story:
As an instructor, I want to define scheduled quizzes and retakes using the UI so that I don't have to
hand-edit the XML files by hand.

Acceptance Test:

- Simulate the user adding a quiz and verify that it is added in the lists of quizzes in the XML.
- Simulate the user adding a retake and verify that it is added in the lists of retakes in the XML.
- Simulate the user deleting a retake and verify that the lists of retakes in the XML no longer has that retake.
- Simulate the user deleting a quiz and verify that the lists of quizzes in the XML no longer has that retake.
- Verify that the program displays an error when the user attempts to add a quiz or a retake with empty information.
- Verify that an error is shown when the user attempts to create a quiz with no id or no date.
- Verify that an error is shown when the user attempts to create a retake with no id or no location or no date.


Tests:

RetakesWriterTest.testWriteSingleRetake:
Created a test that instantiates a RetakesWriter with a Retakes list,
and calls its write function with the path to the output test xml file.
Then, it uses RetakesReader to read that written XML file and tests
whether the retakes strings are the same.

Refactoring:
- Created a stub constructor that takes in a Retakes list
- Created a stub write method that takes in a file path
- Added a getter for the timeOffered field in RetakeBean.java
- Implemented RetakesWriter.write(), and test now passes
- Refactored RetakesWriter and delegated the work of constructing 
  the DOM document to a new method. 
- Refactored RetakesWriterTest to delegate the comparison of a retakes
  object with the read XML file into a new method assertSameRetakesFromWrittenXML()

RetakesWriterTest.testWriteMultipleRetakes:
Instantiates a list of multiple retakes, and tests whether 
retakes.toString is equal to the parsed written retakes XML

Refactoring:
- Added a for loop in constructDocumentFromRetakes() to go through the retakes list
  and append the retakes into the XML document

RetakesWriterTest.testWriteEmptyRetakes:
Tests whether an IllegalArgumentException is thrown when attempting
to write the XML with an empty retakes list.                    

Refactoring:
- Added an isEmpty() method in Retakes.java
- Now the RetakesWriter does not have a field for the retakes. 
  Instead, the Retakes object is now a parameter in RetakesWrite.write()
- Refactored all of the test calls to write()
- Now checking if the retakes list is empty. If so, throw an IllegalArgumentException.
- the path to the output XML file is now a field in RetakesWriter


QuizWriterTest.createQuizXMLTest:
Tests whether an XML file is created that can be parsed successfully by
QuizReader                                                             

Refactoring:
- Created constructDocumentFromQuizzes that creates a DOM document
- Added a getTime() method in QuizBean in order to get the time of the quiz
- implemented write()

QuizWriterTest.createMultipleQuizXMLTest:
Tests whether the QuizWriter can write multiple quizzes into an
XML file     

Refactoring:
- Refactored tests by creating a new method dedicated to 
  comparing the Quizzes object to a parsed written XML file
- Refactored tests by creating a new method dedicated to 
  instantiating the QuizWriter and calling its write function.
- Added a tearDown() method that deletes the written test XML file
  if it exists.
- Added a for loop in constructDocumentFromQuizzes that goes through
  the quizzes and adds a new node for each quiz.

QuizWriterTest.createEmptyQuizXMLFile:
Tests whether the QuizWriter throws an IllegalArgumentException
if there is an empty quizzes list                              

Refactoring:
- Added a method isEmpty() in Quizzes.java
- 

 */