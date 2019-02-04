package quizretakes.utils;

public class QuizXMLFile {
    public static final String separator = ",";
    public static final String courseBase   = "course";
    private static final String quizzesBase = "quiz-orig";
    private static final String retakesBase = "quiz-Retakes";
    private static final String apptsBase   = "quiz-appts";

    public static String getQuizzesFilename(String courseID) {
        return quizzesBase +
                '-' +
                courseID +
                ".xml";

    }

    public static String getRetakesFilename(String courseID) {
        return retakesBase +
                '-' +
                courseID +
                ".xml";
    }

    public static String getApptsFilename(String courseID) {
        return apptsBase +
                '-' +
                courseID +
                ".txt";
    }

    public static String getCourseFilename(String courseID) {
        return courseBase +
                '-' +
                courseID +
                ".xml";
    }

}
