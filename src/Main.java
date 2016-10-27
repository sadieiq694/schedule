import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 Created by sadie.la on 8/26/2016.
 class - list (identical of periods w no times set, just the number/length of slots)
 section - list periods w time and lengths
 courses file - specify number of blocks, how long they have to be

 **/
public class Main {
    public static void main(String[] args) {

        Scheduler schedule = new Scheduler();
        schedule.keyCourse();
        schedule.keyTimes();
        schedule.keyTeachers();
        schedule.keyRooms();
        schedule.loadCourseSections();

        int errors = 0;
        do {

            schedule.resetSchedule();
            errors = schedule.setSectionTimes();
            // schedule.testVisitor();
            schedule.viewSchedule();
            System.out.println(errors);
        } while (errors > 0);
        //System.out.println(errors);

    }

    public static  ArrayList<String> sectionCSVtoArrayList(String sectionCSV) {
        ArrayList<String> sectionResult = new ArrayList<String>();

        if(sectionCSV != null) {
            String[] splitData = sectionCSV.split("\\s*,\\s*");
            for(int i = 0; i < splitData.length; i++) {
                if(!(splitData[i] == null) || !(splitData[i].length() == 0)) {
                    sectionResult.add(splitData[i].trim());
                }
            }
        }
        return sectionResult;
    }
}
