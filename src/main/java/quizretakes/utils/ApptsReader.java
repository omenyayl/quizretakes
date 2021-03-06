// JO, 6-Jan-2019
// Readsappointments
// Stores in a ArrayList and returns

package quizretakes.utils;

import java.lang.*;
import java.io.IOException;


import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class ApptsReader
{

static private final String separator = ",";

public ArrayList<ApptBean> read (String filename) throws IOException
{

   // read appointments file
   ArrayList<ApptBean> appts = new ArrayList<ApptBean>();
   ApptBean a;
   File file = new File(filename);
   if (!file.exists())
   {
      throw new IOException ("No appointments to read.");
   }
   else
   {
      FileReader fw = new FileReader(file.getAbsoluteFile());
      BufferedReader bw = new BufferedReader(fw);

      String line;
      while ((line = bw.readLine()) != null)
      {
         String[] s = line.split(separator);
         a = new ApptBean(Integer.parseInt(s[0]), Integer.parseInt(s[1]), s[2]);
         appts.add(a);
      }
      bw.close();
   }

   return (appts);
} // end read

} // end class
