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
 //parameter for not two sections of one class on the same day

 **/
public class Main {
    public static void main(String[] args) {

        Scheduler schedule = new Scheduler();
        schedule.loadCourse();
        schedule.loadTimes();
        schedule.loadTeachers();
        schedule.loadRooms();
        schedule.loadCourseSections();

        int minErrors = 20;
        int errors = 0;
        do {

            schedule.resetSchedule();
            errors = schedule.setSectionTimes();
            // schedule.testVisitor();
            //System.out.println(errors);
            if (errors < minErrors) {
                minErrors = errors;
                System.out.println(errors);
            }
        } while (errors > 0);
        schedule.viewSchedule();
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
