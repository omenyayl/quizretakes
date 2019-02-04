package quizretakes.Components;

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

    public String toString() {
        return String.format("Opportunity #%d: Quiz %d from %s, %d/%d",
                retakeID,
                quizID,
                date.getDayOfWeek(),
                date.getMonthValue(),
                date.getDayOfMonth()
        );
    }

}
