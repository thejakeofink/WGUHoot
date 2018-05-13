package collier.mckennon.com.wguhoot.Model;

import com.orm.SugarRecord;

public class Assessment extends SugarRecord {
    String title;
    String startDate;
    String endDate;
    long time;

    public Assessment() {

    }

    public Assessment(String title, String startDate, String endDate, long time) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
