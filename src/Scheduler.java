import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Predicate;


/**
 * Created by sadie.la on 9/1/2016.
 */
public class Scheduler {
    List<Section> courseSections = new ArrayList<Section>();
    Map<Integer, TimePeriod> timesDictionary = new HashMap<Integer, TimePeriod>();
    Map<Integer, Teacher> teachersDictionary = new HashMap<Integer, Teacher>();
    Map<Integer, Course> coursesDictionary = new HashMap<Integer, Course>();
    Map<Integer, Room> roomsDictionary = new HashMap<Integer, Room>();
    Map<TimePeriod.DayofWeek, TreeMap<Integer, List<String>>> organizer = new HashMap<TimePeriod.DayofWeek, TreeMap<Integer, List<String>>>();
    Random rnd = new Random();

    public void keyTimes() {
        //assigns time periods (time and day) to integer keys in a hashmap
        BufferedReader timeBuffer = null;
        try {
            String timeLine;
            timeBuffer = new BufferedReader(new FileReader("data/times.csv"));
            while ((timeLine = timeBuffer.readLine()) != null) {
                ArrayList<String> lineArray = Main.sectionCSVtoArrayList(timeLine);
                if(lineArray.size() >= 3) {
                    TimePeriod period = new TimePeriod(Integer.parseInt(lineArray.get(1)), TimePeriod.DayofWeek.valueOf(lineArray.get(2)), Double.parseDouble(lineArray.get(3)));
                    timesDictionary.put(Integer.parseInt(lineArray.get(0)), period);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(timeBuffer != null) timeBuffer.close();
            } catch (IOException timeException) {
                timeException.printStackTrace();
            }
        }
    }

    public void keyCourse() {
        //assigns Courses to integer keys in a hashmap
        BufferedReader classBuffer = null;
        try {
            String classLine;
            classBuffer = new BufferedReader(new FileReader("data/courseData.csv"));

            while ((classLine = classBuffer.readLine()) != null) {
                ArrayList<String> lineArray = Main.sectionCSVtoArrayList(classLine);
                //System.out.println("Class data: " + lineArray);
                if (lineArray.size() >= 4) {
                    Course nCourse = new Course(lineArray.get(1));
                    for (int i = 3; i < lineArray.size(); i++) {
                        nCourse.addPeriod(Double.parseDouble(lineArray.get(i)));
                    }
                    coursesDictionary.put(Integer.parseInt(lineArray.get(0)), nCourse);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (classBuffer != null) classBuffer.close();
            } catch (IOException courseException) {
                courseException.printStackTrace();
            }
        }
    }

    public void keyTeachers() {
        BufferedReader teacherBuffer = null;

        try {
            String teacherLine;
            teacherBuffer = new BufferedReader(new FileReader("data/teacherData.csv"));

            while ((teacherLine = teacherBuffer.readLine()) != null) {
                ArrayList<String> lineArray = Main.sectionCSVtoArrayList(teacherLine);
                //System.out.println("Teacher data: " + lineArray);
                if (lineArray.size() == 2) {
                    Teacher nTeacher = new Teacher(Integer.parseInt(lineArray.get(0)), lineArray.get(1));
                    teachersDictionary.put(nTeacher.teacherID, nTeacher);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (teacherBuffer != null) teacherBuffer.close();
            } catch (IOException sectionException) {
                sectionException.printStackTrace();
            }
        }
    }

    public void keyRooms() {
        BufferedReader roomBuffer = null;

        try {
            String roomLine;
            roomBuffer = new BufferedReader(new FileReader("data/room.csv"));

            while ((roomLine = roomBuffer.readLine()) != null) {
                ArrayList<String> lineArray = Main.sectionCSVtoArrayList(roomLine);
                //System.out.println("Teacher data: " + lineArray);
                if (lineArray.size() == 2) {
                    Room nRoom = new Room(Integer.parseInt(lineArray.get(0)), lineArray.get(1));
                    roomsDictionary.put(nRoom.roomID, nRoom);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (roomBuffer != null) roomBuffer.close();
            } catch (IOException sectionException) {
                sectionException.printStackTrace();
            }
        }
    }

    public void loadCourseSections() {

        BufferedReader sectionBuffer = null;

        try {
            String sectionLine;
            sectionBuffer = new BufferedReader(new FileReader("data/sectionData.csv"));

            while ((sectionLine = sectionBuffer.readLine()) != null) {
                //System.out.println("Raw data: " + sectionLine);
                ArrayList<String> lineArray = Main.sectionCSVtoArrayList(sectionLine);
                //System.out.println("ArrayList data: " + lineArray);
                //String courseNum = lineArray.get(0);
                if (lineArray.size() == 4) {
                    Integer i = Integer.parseInt(lineArray.get(3));
                    Room r = roomsDictionary.get(Integer.parseInt(lineArray.get(3)));
                    Section courseSec = new Section(Integer.parseInt(lineArray.get(0)), coursesDictionary.get(Integer.parseInt(lineArray.get(1))), teachersDictionary.get(Integer.parseInt(lineArray.get(2))), roomsDictionary.get(Integer.parseInt(lineArray.get(3))));
                    courseSections.add(courseSec);
                    //System.out.println(courseSec.course.name + courseSec.teacher.name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sectionBuffer != null) sectionBuffer.close();
            } catch (IOException sectionException) {
                sectionException.printStackTrace();
            }
        }
    }

    public interface PeriodVisitor {
        void visit(Course c, Section s, TimePeriod p);
    }

    public void accept(PeriodVisitor visitor) {
        for(int i = 0; i < courseSections.size(); i++) {
            Section currentSection = courseSections.get(i);
            for(int j = 0; j < currentSection.periods.size(); j++) {
                visitor.visit(currentSection.course, currentSection, currentSection.periods.get(j));
            }
        }
    }

    public void testVisitor() {
        accept(new PeriodVisitor() {
            @Override
            public void visit(Course c, Section s, TimePeriod p) {
                System.out.println(c.name + " " + s.sectionID + " " + p.day + ", " + p.startTime + " ");

            }
        });
    }

    public List<Section> shuffle(List<Section> list) {
        List<Section> newList = new ArrayList<Section>();
        int index = rnd.nextInt(list.size());
        while(list.size()>0) {
            index = rnd.nextInt(list.size());
            Section newElement = list.get(index);
            newList.add(newElement);
            list.remove(index);
        }
        return newList;
    }

    public List<TimePeriod> findAvailableTimes(Section sec, double length) {
        List<TimePeriod> li = new ArrayList<TimePeriod>();
        for(TimePeriod t: timesDictionary.values()) {
            li.add(t);
        }
        for(int i = li.size()-1; i >= 0; i--) {
            if (shouldRemove(sec, li.get(i).startTime, length, li.get(i).day)) {
                li.remove(i);
            } else if (li.get(i).length < length) {
                li.remove(i);
            }
        }
        return li;
    }

    public boolean shouldRemove(Section curSec, int strttime, double len, TimePeriod.DayofWeek dayofweek) {
        if (curSec.isoverlapping(strttime, len, dayofweek)) {
            return true;
        } else if (curSec.teacher.isOccupied(strttime, len, dayofweek)) {
            return true;
        } else if (roomInUse(curSec, strttime, len, dayofweek)) {
            return true;
        } else if (curSec.sameDay(dayofweek)) { //FIXXXXX!!!!
            return true;
        } else{
            return false;
        }
    }

    public boolean roomInUse(Section s, int strtTime, double len, TimePeriod.DayofWeek d) {
        for (int i = 0; i < s.room.sections.size(); i++) {
            Section curSec = s.room.sections.get(0);
            for (int j = 0; j < curSec.periods.size(); j++) {
                if (curSec.isoverlapping(strtTime, len, d)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*public List<TimePeriod> dailyAvailableTimes(String day, Section s, int index) {
        List<TimePeriod> li = findAvailableTimes(s, index);
        List<TimePeriod> dayTimes = new ArrayList<TimePeriod>();
        for(TimePeriod t: li) {
            if (t.day.equals(day)) {
                dayTimes.add(t);
            }
        }
        return dayTimes;
    }*/

    public List<TimePeriod> filterTimes(Predicate<TimePeriod> pred, Section sec, double length) {
        List<TimePeriod> li = findAvailableTimes(sec, length);
        List<TimePeriod> dayTimes = new ArrayList<>();
        for(TimePeriod t: li) {
            if(pred.test(t)) {
                dayTimes.add(t);
            }
        }
        return dayTimes;
    }

    public TimePeriod assignFirstPeriod(Section s, List<TimePeriod> availT) {
        int randInt = rnd.nextInt(availT.size());
        TimePeriod t = availT.get(randInt);
        TimePeriod timeP = new TimePeriod(t.startTime, t.day, s.course.timeReqs.get(0).length); //golden retriever of toxic waste
        s.periods.add(timeP);
        return timeP;
    }

    public int assignRestOfPeriods(Section s, TimePeriod tp) {
        int errorNum = 0;
        List<TimePeriod> reqs = s.course.timeReqs;
        TimePeriod.DayofWeek mustBeAfter = tp.day;
        for (int i = 1; i < reqs.size(); i++) {
            List<TimePeriod> filtered = filterTimes(t -> t.day.ordinal() > mustBeAfter.ordinal() && t.isoverlapping(tp.startTime, tp.length), s, reqs.get(i).length);
            if (filtered.size() != 0) {
                int randInt = rnd.nextInt(filtered.size());
                TimePeriod t = filtered.get(randInt);
                double len = reqs.get(i).length;
                TimePeriod timeP = new TimePeriod(t.startTime, t.day, len);
                s.periods.add(timeP);
            } else {
                errorNum++;
            }
        }
        return errorNum;
    }

    public int setSectionTimes() {
        int errorNum = 0;
        List<Section> shuffledSections= new ArrayList<Section>();
        shuffledSections = shuffle(courseSections);
        courseSections = shuffledSections;
        for(int i = 0; i < courseSections.size(); i++) {
            Section currentSection = courseSections.get(i);
            int numPeriods = currentSection.course.timeReqs.size();
            if (numPeriods == 5) {
                List<TimePeriod> availT = filterTimes(t -> t.day == TimePeriod.DayofWeek.Monday, currentSection, currentSection.course.timeReqs.get(0).length);
                TimePeriod classT = assignFirstPeriod(currentSection, availT);
                errorNum += assignRestOfPeriods(currentSection, classT);
            } else if(numPeriods == 4) {
                List<TimePeriod> availT = filterTimes(t -> t.day.ordinal() <= TimePeriod.DayofWeek.Tuesday.ordinal(), currentSection, currentSection.course.timeReqs.get(0).length);
                TimePeriod classT = assignFirstPeriod(currentSection, availT);
                errorNum += assignRestOfPeriods(currentSection, classT);
            } else if (numPeriods == 3 || numPeriods == 2) {
                List<TimePeriod> availT = filterTimes(t -> t.day.ordinal() <= TimePeriod.DayofWeek.Wednesday.ordinal(), currentSection, currentSection.course.timeReqs.get(0).length);
                TimePeriod classT = assignFirstPeriod(currentSection, availT);
                errorNum += assignRestOfPeriods(currentSection, classT);
            }
        }
        return errorNum;
        /*All periods (except 11:30) are listed as two hour blocks to tell the program it can put a lab block there; this is
        messing up the filter times function because it is saying periods overlap that actually don't*/
    }

    public void resetSchedule()
    {
        for(int i = 0; i < courseSections.size(); i++) {
            Section curSec = courseSections.get(i);
            for(int j = curSec.periods.size()-1; j >=0; j--) {
                curSec.periods.remove(j);
            }
        }
    }


    public void viewSchedule() {
        //dictionary w/in a dictionary first key = day, keys to a second dictionary whose key is time

        for(TimePeriod.DayofWeek day: TimePeriod.DayofWeek.values()) {
            organizer.put(day, new TreeMap<Integer, List<String>>());
        }
        for(int i = 0; i < courseSections.size(); i++) {
            int numPer = courseSections.get(i).periods.size();
            for(int j = 0; j < numPer; j++) {
                Section curSection = courseSections.get(i);
                TimePeriod.DayofWeek curDay = curSection.periods.get(j).day;
                Integer curTime = curSection.periods.get(j).startTime;
                String courseInfo;
                if(curSection.periods.get(j).length == 1) {
                    courseInfo = curSection.sectionID.toString() + " " + curSection.course.name + " " + curSection.teacher.name + " " + curSection.room.roomNum;
                } else {
                    courseInfo = curSection.sectionID.toString() + " " + curSection.course.name + " " + curSection.teacher.name + " " + curSection.room.roomNum + " " + "lab";
                }
                //double curLength = curSection.periods.get(j).length;
                if (!organizer.get(curDay).containsKey(curTime)) {
                    organizer.get(curDay).put(curTime, new ArrayList<String>());
                }
                organizer.get(curDay).get(curTime).add(courseInfo);
            }
        }

        try{
            PrintWriter writer = new PrintWriter("schedule.html", "UTF-8");

            writer.println("<table>");
            writer.println("<tr> <th>Day</th> <th>8:30</th> <th>9:30</th> <th>10:30</th> <th>11:30</th> <th>1:30</th> <th>2:30</th> <th>3:30</th> <th>4:30</th> <th>6:00</th> </tr>");

            for(TimePeriod.DayofWeek day: TimePeriod.DayofWeek.values()) {
                Map<Integer, List<String>> dayMap = organizer.get(day);
                writer.println("<tr> <td>" + day.name() + "</td>");
                for(Map.Entry<Integer, List<String>> entry: dayMap.entrySet()) {
                    writer.println("<td>" + entry.getValue() + "</td>");
                }
                writer.println("</tr>");
            }

            writer.println("</table>");

            writer.close();
        } catch (IOException e) {
            // do something
        }
    }
}
