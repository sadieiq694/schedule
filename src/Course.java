import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadie.la on 9/8/2016.
 */
public class Course {
    String name;
    //String room;
    List<Section> sections = new ArrayList<Section>();
    List<TimePeriod> timeReqs = new ArrayList<TimePeriod>();

    public Course(String s) {
        this.name = s;
    }

    public void addPeriod(double perLeng) {
        TimePeriod time = new TimePeriod(perLeng);
        timeReqs.add(time);
    }

    public void addSection(Section s) {sections.add(s);}
}
