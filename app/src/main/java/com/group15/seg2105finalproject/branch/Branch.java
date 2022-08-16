package com.group15.seg2105finalproject.branch;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import com.group15.seg2105finalproject.customer.Customer;
import com.group15.seg2105finalproject.main.Service;

import java.util.*;

// line 16 "model.ump"
// line 103 "model.ump"
// line 111 "model.ump"
public class Branch
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Branch Attributes
    private String phone;
    private String address;
    private List request;
    private HashMap hours;

    //Branch Associations
    private Employee employee;
    private List<Rating> ratings;
    private List<Service> enabledServices;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Branch(String aPhone, String aAddress, List aRequest, HashMap aHours)
    {
        phone = aPhone;
        address = aAddress;
        request = aRequest;
        hours = aHours;
        ratings = new ArrayList<Rating>();
        enabledServices = new ArrayList<Service>();
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setPhone(String aPhone)
    {
        boolean wasSet = false;
        phone = aPhone;
        wasSet = true;
        return wasSet;
    }

    public boolean setAddress(String aAddress)
    {
        boolean wasSet = false;
        address = aAddress;
        wasSet = true;
        return wasSet;
    }

    public boolean setRequest(List aRequest)
    {
        boolean wasSet = false;
        request = aRequest;
        wasSet = true;
        return wasSet;
    }

    public boolean setHours(HashMap aHours)
    {
        boolean wasSet = false;
        hours = aHours;
        wasSet = true;
        return wasSet;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getAddress()
    {
        return address;
    }

    public List getRequest()
    {
        return request;
    }

    public HashMap getHours()
    {
        return hours;
    }
    /* Code from template association_GetOne */
    public Employee getEmployee()
    {
        return employee;
    }

    public boolean hasEmployee()
    {
        boolean has = employee != null;
        return has;
    }
    /* Code from template association_GetMany */
    public Rating getRating(int index)
    {
        Rating aRating = ratings.get(index);
        return aRating;
    }

    public List<Rating> getRatings()
    {
        List<Rating> newRatings = Collections.unmodifiableList(ratings);
        return newRatings;
    }

    public int numberOfRatings()
    {
        int number = ratings.size();
        return number;
    }

    public boolean hasRatings()
    {
        boolean has = ratings.size() > 0;
        return has;
    }

    public int indexOfRating(Rating aRating)
    {
        int index = ratings.indexOf(aRating);
        return index;
    }
    /* Code from template association_GetMany */
    public Service getEnabledService(int index)
    {
        Service aEnabledService = enabledServices.get(index);
        return aEnabledService;
    }

    public List<Service> getEnabledServices()
    {
        List<Service> newEnabledServices = Collections.unmodifiableList(enabledServices);
        return newEnabledServices;
    }

    public int numberOfEnabledServices()
    {
        int number = enabledServices.size();
        return number;
    }

    public boolean hasEnabledServices()
    {
        boolean has = enabledServices.size() > 0;
        return has;
    }

    public int indexOfEnabledService(Service aEnabledService)
    {
        int index = enabledServices.indexOf(aEnabledService);
        return index;
    }
    /* Code from template association_SetOptionalOneToOne */
    public boolean setEmployee(Employee aNewEmployee)
    {
        boolean wasSet = false;
        if (employee != null && !employee.equals(aNewEmployee) && equals(employee.getBranch()))
        {
            //Unable to setEmployee, as existing employee would become an orphan
            return wasSet;
        }

        employee = aNewEmployee;
        Branch anOldBranch = aNewEmployee != null ? aNewEmployee.getBranch() : null;

        if (!this.equals(anOldBranch))
        {
            if (anOldBranch != null)
            {
                anOldBranch.employee = null;
            }
            if (employee != null)
            {
                employee.setBranch(this);
            }
        }
        wasSet = true;
        return wasSet;
    }
    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfRatings()
    {
        return 0;
    }
    /* Code from template association_AddManyToOne */
    public Rating addRating(int aValue, String aComment, Customer aCustomer)
    {
        return new Rating(aValue, aComment, aCustomer, this);
    }

    public boolean addRating(Rating aRating)
    {
        boolean wasAdded = false;
        if (ratings.contains(aRating)) { return false; }
        Branch existingBranch = aRating.getBranch();
        boolean isNewBranch = existingBranch != null && !this.equals(existingBranch);
        if (isNewBranch)
        {
            aRating.setBranch(this);
        }
        else
        {
            ratings.add(aRating);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeRating(Rating aRating)
    {
        boolean wasRemoved = false;
        //Unable to remove aRating, as it must always have a branch
        if (!this.equals(aRating.getBranch()))
        {
            ratings.remove(aRating);
            wasRemoved = true;
        }
        return wasRemoved;
    }
    /* Code from template association_AddIndexControlFunctions */
    public boolean addRatingAt(Rating aRating, int index)
    {
        boolean wasAdded = false;
        if(addRating(aRating))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfRatings()) { index = numberOfRatings() - 1; }
            ratings.remove(aRating);
            ratings.add(index, aRating);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveRatingAt(Rating aRating, int index)
    {
        boolean wasAdded = false;
        if(ratings.contains(aRating))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfRatings()) { index = numberOfRatings() - 1; }
            ratings.remove(aRating);
            ratings.add(index, aRating);
            wasAdded = true;
        }
        else
        {
            wasAdded = addRatingAt(aRating, index);
        }
        return wasAdded;
    }
    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfEnabledServices()
    {
        return 0;
    }
    /* Code from template association_AddUnidirectionalMany */
    public boolean addEnabledService(Service aEnabledService)
    {
        boolean wasAdded = false;
        if (enabledServices.contains(aEnabledService)) { return false; }
        enabledServices.add(aEnabledService);
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeEnabledService(Service aEnabledService)
    {
        boolean wasRemoved = false;
        if (enabledServices.contains(aEnabledService))
        {
            enabledServices.remove(aEnabledService);
            wasRemoved = true;
        }
        return wasRemoved;
    }
    /* Code from template association_AddIndexControlFunctions */
    public boolean addEnabledServiceAt(Service aEnabledService, int index)
    {
        boolean wasAdded = false;
        if(addEnabledService(aEnabledService))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfEnabledServices()) { index = numberOfEnabledServices() - 1; }
            enabledServices.remove(aEnabledService);
            enabledServices.add(index, aEnabledService);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveEnabledServiceAt(Service aEnabledService, int index)
    {
        boolean wasAdded = false;
        if(enabledServices.contains(aEnabledService))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfEnabledServices()) { index = numberOfEnabledServices() - 1; }
            enabledServices.remove(aEnabledService);
            enabledServices.add(index, aEnabledService);
            wasAdded = true;
        }
        else
        {
            wasAdded = addEnabledServiceAt(aEnabledService, index);
        }
        return wasAdded;
    }

    public void delete()
    {
        Employee existingEmployee = employee;
        employee = null;
        if (existingEmployee != null)
        {
            existingEmployee.delete();
        }
        for(int i=ratings.size(); i > 0; i--)
        {
            Rating aRating = ratings.get(i - 1);
            aRating.delete();
        }
        enabledServices.clear();
    }

    // line 24 "model.ump"
    public void modifyAddress(){

    }

    // line 25 "model.ump"
    public void modifyPhone(){

    }


    public String toString()
    {
        return super.toString() + "["+
                "phone" + ":" + getPhone()+ "," +
                "address" + ":" + getAddress()+ "]" + System.getProperties().getProperty("line.separator") +
                "  " + "request" + "=" + (getRequest() != null ? !getRequest().equals(this)  ? getRequest().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
                "  " + "hours" + "=" + (getHours() != null ? !getHours().equals(this)  ? getHours().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
                "  " + "employee = "+(getEmployee()!=null?Integer.toHexString(System.identityHashCode(getEmployee())):"null");
    }
}



