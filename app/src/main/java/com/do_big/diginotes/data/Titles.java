package com.do_big.diginotes.data;

/**
 * Created by dell on 28-11-2017.
 */

public class Titles {
    private String title;
    private String dates;


    public Titles(String title, String dates) {
        this.title = title;
        this.dates = dates;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }
}
