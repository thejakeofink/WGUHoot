package collier.mckennon.com.wguhoot.Model;

import com.orm.SugarRecord;

public class Mentor extends SugarRecord {
    private String name;
    private int phone;
    private String email;
    private long time;

    public Mentor() {

    }

    public Mentor(String name, int phone, String email, long time) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
