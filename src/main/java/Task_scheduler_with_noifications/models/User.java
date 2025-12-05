package Task_scheduler_with_noifications.models;

import Task_scheduler_with_noifications.enums.Gender;

import java.awt.*;

public class User {
    private String userName;
    private String email;
    private String profilePic;
    private String phone;
    private String gender;

    public User(String userName, String email, String profilePic, String phone, String gender) {
        this.userName = userName;
        this.email = email;
        this.profilePic = profilePic;
        this.phone = phone;
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
