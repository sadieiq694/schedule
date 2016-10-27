import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadie.la on 10/24/2016.
 */
public class Room {
    Integer roomID;
    String roomNum;
    List<Section> sections = new ArrayList<Section>();

    public Room(Integer i, String n) {
        this.roomID = i;
        this.roomNum = n;
    }

    public void addRSection(Section s) {sections.add(s);}

}



