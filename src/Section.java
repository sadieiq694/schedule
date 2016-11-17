import java.util.ArrayList;
import java.util.List;

//course contain list of sections, global list of sections

/**
 * Created by sadie.la on 8/26/2016.
 */
public class Section {
    Integer sectionID;
    Course course;
    Teacher teacher;
    List<TimePeriod> periods = new ArrayList<TimePeriod>();
    Room room;

    //String type;
    //String specType;
    //List<String> prereqs;
    //String roomNumber;

    // public get

    public Section(Integer i, Course c, Teacher t, Room r) {
        this.sectionID = i;
        this.course = c;
        this.teacher = t;
        this.room = r;
        c.addSection(this);
        t.addTSection(this);
        r.addRSection(this);
    }

    public boolean isoverlapping(int strtTime, double len, TimePeriod.DayofWeek d) {
        for(int i = 0; i < periods.size(); i++) {
            if (periods.get(i).isoverlapping(strtTime, len, d)) {
                return true;
            }
        }
        return false;
    }

    public boolean isoverlapping(int strtTime, double len) {
        for(int i = 0; i < periods.size(); i++) {
            if (periods.get(i).isoverlapping(strtTime, len)) {
                return true;
            }
        }
        return false;
    }

    public boolean sameDay(TimePeriod.DayofWeek day) {
        for(int i = 0; i < periods.size(); i++) {
            if(periods.get(i).sameDay(day)) {
                return true;
            }
        }
        return false;
    }



    public boolean isoverlapping(TimePeriod p) {
        return isoverlapping(p.startTime, p.length, p.day);
    }


}
