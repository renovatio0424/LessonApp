package com.example.kimjungwon.lessonapp;

/**
 * Created by kimjungwon on 2017-03-20.
 */

public class Area {
    Boolean checked = false;
    String areaname ;
    int cd ;
    int xpos ;
    int ypos ;


    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getAreaname() {
        return areaname;
    }

    public int getCd() {
        return cd;
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public Boolean getChecked() {
        return checked;
    }
}
