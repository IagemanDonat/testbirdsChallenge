package com.testbirdsChallenge.entity;

public class VM {

    private String macAdress;

    private String name;

    private String resourcePath;

    private String state;



    public VM(String macAdress, String name, String resourcePath) {
        this.macAdress = macAdress;
        this.name = name;
        this.resourcePath = resourcePath;
        this.state = "initialised";
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public String getMacAdress() {
        return macAdress;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
