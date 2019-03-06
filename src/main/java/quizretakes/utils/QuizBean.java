package quizretakes.utils;

import java.time.*;

/**
 * This bean holds information about a quiz

 * @author Jeff Offutt
 */
/* *****************************************
<Quizzes>
  <quiz>
    <id>1</id> --integer > 0
    <dateGiven>
      <month>1</month> --01..12
      <day>10</day> --1..31
      <hour>15</hour> --0..23
      <minute>30</minute> --> 0-59
    </dateGiven>
  </quiz>
  <quiz>
    <id>2</id>
...
</Quizzes>
***************************************** */

public class QuizBean implements Comparable<QuizBean>
{
   private int ID;
   private LocalDate dateGiven;
   private LocalTime timeGiven;

   // *** Constructor *** //
   public QuizBean(int quizID, int month, int day, int hour, int minute)
   {
      ID = quizID;
      int year = Year.now().getValue();
      dateGiven = LocalDate.of (year, month, day);
      timeGiven = LocalTime.of (hour, minute);
   }

   @Override
   public int compareTo (QuizBean quizB)
   {
      return this.ID - quizB.ID;
   }

   // *** Getters *** //
   public LocalDate getDate()
   {
      return dateGiven;
   }
   public int getID()
   {
      return ID;
   }
   public String toString()
   {
      return ID + ": " +
             dateGiven.toString() + ": " +
             dateGiven.getDayOfWeek() + ": " +
             timeGiven.toString();
   }

   // Date methods
   public Month getMonth()
   {
      return dateGiven.getMonth();
   }
   public int getMonthNum()
   {
      return dateGiven.getMonthValue();
   }
   public DayOfWeek getDayOfWeek()
   {
      return dateGiven.getDayOfWeek();
   }
   public String dateAsString ()
   {
      return dateGiven.toString();
   }

   // Time methods
   public String timeAsString ()
   {
      return timeGiven.toString();
   }

/*
   public String getQuizId() {
      return quizId;
   }
*/

}
