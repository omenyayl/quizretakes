package quizretakes.utils;

import java.time.*;

/**
 * This bean holds a single quiz retake appointment

 * @author Jeff Offutt
 */

public class ApptBean
{
   private int quizID;
   private int retakeID;
   private String name;

   // *** Constructor *** //
   public ApptBean(int retakeID, int quizID, String name)
   {
      this.retakeID = retakeID;
      this.quizID   = quizID;
      this.name     = name;
   }

   // *** Getters *** //
   public int getQuizID()
   {
      return quizID;
   }
   public int getRetakeID()
   {
      return retakeID;
   }
   public String getName()
   {
      return name;
   }

   public String toString()
   {
      return retakeID + ":" + quizID + ":" + name;
   }
}
