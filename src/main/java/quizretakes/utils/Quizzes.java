package quizretakes.utils;

// import java.io.Serializable; ?? Needed?
import java.util.*;

/**
 * This class holds a collection of Quizzes

 * @author Jeff Offutt
 */

public class Quizzes implements Iterable<QuizBean>
{
   private ArrayList<QuizBean> quizzes;

   // ***** Constructors //
   public Quizzes()
   {
      quizzes = new ArrayList<QuizBean>();
   }

   public Quizzes(int quizID, int month, int day, int hour, int minute)
   {  // Adds one quiz to a new list
      quizzes = new ArrayList<QuizBean>();
      QuizBean qb = new QuizBean(quizID, month, day, hour, minute);
      quizzes.add (qb);
   }

   public Quizzes(QuizBean qb)
   {
      quizzes = new ArrayList<QuizBean>();
      quizzes.add (qb);
   }

   // *** sorting and iterating *** //
   public void sort ()
   {
      Collections.sort (quizzes);
   }

   @Override
   public Iterator<QuizBean> iterator()
   {
       return quizzes.iterator();
   }

   // ***** setters & getters //
   public void addQuiz (QuizBean qb)
   {
      quizzes.add (qb);
   }

   public String toString ()
   {
      return (Arrays.toString(quizzes.toArray()));
   }

   public boolean isEmpty() {
      return this.quizzes.size() == 0;
   }

}
