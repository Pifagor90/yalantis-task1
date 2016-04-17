/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.entities;


import java.io.Serializable;

public class Company {

    private String mName;

    public Company(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
