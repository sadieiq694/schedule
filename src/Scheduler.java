import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by sadie.la on 9/1/2016.
 */
public class Scheduler {
    List<Section> courseSections = new ArrayList<Section>();
    Map<Integer, TimePeriod> timesDictionary = new HashMap<Integer, TimePeriod>();
    Map<Integer, Teacher> teachersDictionary = new HashMap<Integer, Teacher>();
    Map<Integer, Course> coursesDictionary = new HashMap<Integer, Course>();
    Map<Integer, Room> roomsDictionary = new HashMap<Integer, Room>();
    Map<String, TreeMap<Integer, List<String>>> organizer = new HashMap<String, TreeMap<Integer, List<String>>>();
    String[] weekDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
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
                    TimePeriod period = new TimePeriod(Integer.parseInt(lineArray.get(1)), lineArray.get(2), Double.parseDouble(lineArray.get(3)));
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

    public List<TimePeriod> findAvailableTimes(Section sec, double len) {
        List<TimePeriod> li = new ArrayList<TimePeriod>();
        for(TimePeriod t: timesDictionary.values()) {
            li.add(t);
        }
        for(int i = li.size()-1; i >= 0; i--) {
            if (shouldRemove(sec, li.get(i).startTime, len, li.get(i).day)) {
                li.remove(i);
            } else if (li.get(i).length < len) {
                li.remove(i);
            }
        }
        return li;
    }

    public boolean shouldRemove(Section curSec, int strttime, double len, String dayofweek) {
        if (curSec.isoverlapping(strttime, len, dayofweek)) {
            return true;
        } else if (curSec.teacher.isOccupied(strttime, len, dayofweek)) {
            return true;
        } else if(roomInUse(curSec, strttime, len, dayofweek)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean roomInUse(Section s, int strtTime, double len, String d) {
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

    public int setSectionTimes() {
        int errorNum = 0;
        for(int i = 0; i < courseSections.size(); i++) {
            Section currentSection = courseSections.get(i);
            for(int j = 0; j < courseSections.get(i).course.timeReqs.size(); j++) {
                List<TimePeriod> availableTimes = findAvailableTimes(currentSection, currentSection.course.timeReqs.get(j).length);
                int size = availableTimes.size();
                if (size==0){
                    errorNum++;
                    System.out.println(currentSection.course.name + currentSection.course.timeReqs.get(j).length);
                } else {
                    int randInt = rnd.nextInt(size);
                    TimePeriod t = availableTimes.get(randInt);
                    double len = currentSection.course.timeReqs.get(j).length;
                    TimePeriod timeP = new TimePeriod(t.startTime, t.day, len);
                    currentSection.periods.add(timeP);
                }
            }
        }
        return errorNum;
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


    public void viewSchedule() { //this function is really ugly rn I'm just checking if it works ahhhh
        //dictionary w/in a dictionary first key = day, keys to a second dictionary whose key is time

        for(String day: weekDays) {
            organizer.put(day, new TreeMap<Integer, List<String>>());
        }
        for(int i = 0; i < courseSections.size(); i++) {
            int numPer = courseSections.get(i).course.timeReqs.size();
            for(int j = 0; j < numPer; j++) {
                Section curSection = courseSections.get(i);
                String curName = curSection.course.name;
                String curDay = curSection.periods.get(j).day;
                Integer curTime = curSection.periods.get(j).startTime;
                //double curLength = curSection.periods.get(j).length;
                if (!organizer.get(curDay).containsKey(curTime)) {
                    organizer.get(curDay).put(curTime, new ArrayList<String>());
                }
                organizer.get(curDay).get(curTime).add(curName);
            }
        }

        for(String day: weekDays) {
            Map<Integer, List<String>> dayMap = organizer.get(day);
            System.out.println(day);
            for(Map.Entry<Integer, List<String>> entry: dayMap.entrySet()) {
                if(entry.getKey() > 1230) {
                    System.out.print(entry.getKey()-1200);
                } else {
                    System.out.print(entry.getKey());
                }
                System.out.println(entry.getValue());
            }
        }
    }
}
