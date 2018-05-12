package collier.mckennon.com.wguhoot.Model;

import com.orm.SugarRecord;

public class Mentor extends SugarRecord {
    String name;
    String phone;
    String email;
    long time;

    public Mentor() {

    }

    public Mentor(String name, String phone, String email, long time) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
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
