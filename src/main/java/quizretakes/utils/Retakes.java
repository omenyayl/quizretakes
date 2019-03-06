package quizretakes.utils;

import java.util.*;

/**
 * This class holds a collection of Retakes

 * @author Jeff Offutt
 */

public class Retakes implements Iterable<RetakeBean>
{
   private ArrayList<RetakeBean> retakes;

   // ***** Constructors //
   public Retakes()
   {
      retakes = new ArrayList<RetakeBean>();
   }

   public Retakes(int ID, String location, int month, int day, int hour, int minute)
   {
      retakes = new ArrayList<RetakeBean>();
      RetakeBean qr = new RetakeBean(ID, location, month, day, hour, minute);
      retakes.add (qr);
   }

   public Retakes(RetakeBean qr)
   {
      retakes = new ArrayList<RetakeBean>();
      retakes.add (qr);
   }

   // ***** //
   public void sort ()
   {
      Collections.sort (retakes);
   }

   @Override
   public Iterator<RetakeBean> iterator()
   {
       return retakes.iterator();
   }

   // ***** adders & getters //
   public void addRetake (RetakeBean qr)
   {
      retakes.add (qr);
   }

   public String toString ()
   {
      return (Arrays.toString(retakes.toArray()));
   }

   public boolean isEmpty() {
      return retakes.size() == 0;
   }

}
