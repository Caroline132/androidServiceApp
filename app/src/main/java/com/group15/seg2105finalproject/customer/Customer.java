package com.group15.seg2105finalproject.customer;

import com.group15.seg2105finalproject.branch.Branch;
import com.group15.seg2105finalproject.branch.Employee;
import com.group15.seg2105finalproject.branch.Rating;
import com.group15.seg2105finalproject.main.User;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import java.util.*;

// line 34 "model.ump"
// line 77 "model.ump"
public class Customer extends User
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Customer Attributes
    private List request;

    //Customer Associations
    private List<Employee> employees;
    private List<Rating> ratings;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Customer(String aUsername, String aPassword, String aFirstName, String aLastName, String aEmail, List aRequest)
    {
        super(aUsername, aPassword, aFirstName, aLastName, aEmail);
        request = aRequest;
        employees = new ArrayList<Employee>();
        ratings = new ArrayList<Rating>();
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setRequest(List aRequest)
    {
        boolean wasSet = false;
        request = aRequest;
        wasSet = true;
        return wasSet;
    }

    public List getRequest()
    {
        return request;
    }
    /* Code from template association_GetMany */
    public Employee getEmployee(int index)
    {
        Employee aEmployee = employees.get(index);
        return aEmployee;
    }

    public List<Employee> getEmployees()
    {
        List<Employee> newEmployees = Collections.unmodifiableList(employees);
        return newEmployees;
    }

    public int numberOfEmployees()
    {
        int number = employees.size();
        return number;
    }

    public boolean hasEmployees()
    {
        boolean has = employees.size() > 0;
        return has;
    }

    public int indexOfEmployee(Employee aEmployee)
    {
        int index = employees.indexOf(aEmployee);
        return index;
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
    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfEmployees()
    {
        return 0;
    }
    /* Code from template association_AddUnidirectionalMany */
    public boolean addEmployee(Employee aEmployee)
    {
        boolean wasAdded = false;
        if (employees.contains(aEmployee)) { return false; }
        employees.add(aEmployee);
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeEmployee(Employee aEmployee)
    {
        boolean wasRemoved = false;
        if (employees.contains(aEmployee))
        {
            employees.remove(aEmployee);
            wasRemoved = true;
        }
        return wasRemoved;
    }
    /* Code from template association_AddIndexControlFunctions */
    public boolean addEmployeeAt(Employee aEmployee, int index)
    {
        boolean wasAdded = false;
        if(addEmployee(aEmployee))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfEmployees()) { index = numberOfEmployees() - 1; }
            employees.remove(aEmployee);
            employees.add(index, aEmployee);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveEmployeeAt(Employee aEmployee, int index)
    {
        boolean wasAdded = false;
        if(employees.contains(aEmployee))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfEmployees()) { index = numberOfEmployees() - 1; }
            employees.remove(aEmployee);
            employees.add(index, aEmployee);
            wasAdded = true;
        }
        else
        {
            wasAdded = addEmployeeAt(aEmployee, index);
        }
        return wasAdded;
    }
    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfRatings()
    {
        return 0;
    }
    /* Code from template association_AddManyToOne */
    public Rating addRating(int aValue, String aComment, Branch aBranch)
    {
        return new Rating(aValue, aComment, this, aBranch);
    }

    public boolean addRating(Rating aRating)
    {
        boolean wasAdded = false;
        if (ratings.contains(aRating)) { return false; }
        Customer existingCustomer = aRating.getCustomer();
        boolean isNewCustomer = existingCustomer != null && !this.equals(existingCustomer);
        if (isNewCustomer)
        {
            aRating.setCustomer(this);
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
        //Unable to remove aRating, as it must always have a customer
        if (!this.equals(aRating.getCustomer()))
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

    public void delete()
    {
        employees.clear();
        for(int i=ratings.size(); i > 0; i--)
        {
            Rating aRating = ratings.get(i - 1);
            aRating.delete();
        }
        super.delete();
    }


    public String toString()
    {
        return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
                "  " + "request" + "=" + (getRequest() != null ? !getRequest().equals(this)  ? getRequest().toString().replaceAll("  ","    ") : "this" : "null");
    }
}




