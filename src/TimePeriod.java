/**
 * Created by sadie.la on 9/13/2016.
 */
public class TimePeriod {
    int timeID;
    int startTime;
    double length;
    String day;

    public TimePeriod(int start, String dayofweek) {this.startTime = start; this.day = dayofweek;}
    public TimePeriod(int start, String dayOfWeek, double leng) {this.startTime = start; this.day = dayOfWeek; this.length = leng;}
    public TimePeriod(int id, int start, String dayOfWeek, double leng) {this.timeID = id; this.startTime = start; this.day = dayOfWeek; this.length = leng;}

    public TimePeriod (double len) {
        this.length = len;
    }

    public static double milTime(int oTime) {
        int hour = oTime/100;
        double minutes = (oTime -(100*hour))/60.0;
        return hour + minutes;
    }

    public boolean isoverlapping(int strt, double len, String d) {
        double dubStart = milTime(startTime);
        double dubEnd = dubStart + length;
        double testStrt = milTime(strt);
        double testEnd = testStrt + len;
        if(dubEnd > testStrt && dubStart < testEnd && d.equals(day)) {
            return true;
        } else {
            return false;
        }

        //return (dubEnd > testStrt && dubStart < testEnd);
    }
}
