package com.blood.donate.musicplayer;

public class SongsDetails {
    private String Title;
    private String Description;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public SongsDetails(String title, String description, String location) {
        this.location = location;
        Title = title;
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

}
