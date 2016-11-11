import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadie.la on 9/8/2016.
 */
public class Teacher {
    Integer teacherID;
    String name;
    List<Section> tsections = new ArrayList<Section>();

    public Teacher(Integer i, String n) {
        this.teacherID = i;
        this.name = n;
    }

    public void addTSection(Section s) {
        tsections.add(s);
    }

    public boolean isOccupied(int startTime, double length, TimePeriod.DayofWeek day) {
        for(int i = 0; i < tsections.size(); i++) {
            Section curSection = tsections.get(i);
            if (curSection.isoverlapping(startTime, length, day)) {
                curSection.isoverlapping(startTime, length, day);
                return true;
            }
        }
        return false;
    }

}
