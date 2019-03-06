package quizretakes.components;

import quizretakes.utils.Config;

import java.time.LocalDate;

public class QuizListItem {
    private int retakeID;
    private int quizID;
    private LocalDate date;

    public QuizListItem(int retakeID, int quizID, LocalDate date) {
        this.retakeID = retakeID;
        this.quizID = quizID;
        this.date = date;
    }

    public int getRetakeID() {
        return retakeID;
    }

    public int getQuizID() {
        return quizID;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getIDString() {
        return this.retakeID + Config.separator + quizID;
    }

    public String toString() {
        return String.format("Session #%d: Quiz %d from %s, %d/%d",
                retakeID,
                quizID,
                date.getDayOfWeek(),
                date.getMonthValue(),
                date.getDayOfMonth()
        );
    }

}
