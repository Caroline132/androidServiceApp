package com.group15.seg2105finalproject.main;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/



// line 53 "model.ump"
// line 91 "model.ump"
public class Service
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Service Attributes
    private String serviceName;
    private double hourlyRate;
    private boolean isEnabled;
    private ArrayList<String> requirements;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Service(String aServiceName, double aHourlyRate, boolean aIsEnabled, ArrayList<String> aRequirements)
    {
        serviceName = aServiceName;
        hourlyRate = aHourlyRate;
        isEnabled = aIsEnabled;
        requirements = aRequirements;

        requirements.add("first name");
        requirements.add("last name");
        requirements.add("date of birth");
        requirements.add("address");
        requirements.add("email");
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setServiceName(String aServiceName)
    {
        boolean wasSet = false;
        serviceName = aServiceName;
        wasSet = true;
        return wasSet;
    }

    public boolean setHourlyRate(double aHourlyRate)
    {
        boolean wasSet = false;
        hourlyRate = aHourlyRate;
        wasSet = true;
        return wasSet;
    }

    public boolean setIsEnabled(boolean aIsEnabled)
    {
        boolean wasSet = false;
        isEnabled = aIsEnabled;
        wasSet = true;
        return wasSet;
    }

    public boolean setRequirements(ArrayList<String> aRequirements)
    {
        boolean wasSet = false;
        requirements = aRequirements;
        wasSet = true;
        return wasSet;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public double getHourlyRate()
    {
        return hourlyRate;
    }

    public boolean getIsEnabled()
    {
        return isEnabled;
    }

    public ArrayList<String> getRequirements()
    {
        return requirements;
    }

    public void delete()
    {}


    public String toString()
    {
        return super.toString() + "["+
                "serviceName" + ":" + getServiceName()+ "," +
                "hourlyRate" + ":" + getHourlyRate()+ "," +
                "isEnabled" + ":" + getIsEnabled()+ "]" + System.getProperties().getProperty("line.separator") +
                "  " + "requirements" + "=" + (getRequirements() != null ? !getRequirements().equals(this)  ? getRequirements().toString().replaceAll("  ","    ") : "this" : "null");
    }
}
