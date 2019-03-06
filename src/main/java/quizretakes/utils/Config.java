package quizretakes.utils;

public class Config {

    private static Config instance;

    private String courseID;

    private Config(){

    }

    synchronized public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public void setCourseID (String courseID) {
        this.courseID = courseID;
    }

    public String getCourseID() {
        return courseID;
    }

    public static final String separator = ",";
    public static final String courseBase   = "course";
    private static final String quizzesBase = "quiz-orig";
    private static final String retakesBase = "quiz-retakes";
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
