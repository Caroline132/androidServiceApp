package com.group15.seg2105finalproject.admin;

import com.group15.seg2105finalproject.main.Service;
import com.group15.seg2105finalproject.main.User;

import java.util.List;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import java.util.*;

// line 40 "model.ump"
// line 83 "model.ump"
public class Administrator extends User
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Administrator Associations
    private List<Service> adminServices;
    private List<User> users;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Administrator(String aUsername, String aPassword, String aFirstName, String aLastName, String aEmail)
    {
        super(aUsername, aPassword, aFirstName, aLastName, aEmail);
        adminServices = new ArrayList<Service>();
        users = new ArrayList<User>();
    }

    //------------------------
    // INTERFACE
    //------------------------
    /* Code from template association_GetMany */
    public Service getAdminService(int index)
    {
        Service aAdminService = adminServices.get(index);
        return aAdminService;
    }

    public List<Service> getAdminServices()
    {
        List<Service> newAdminServices = Collections.unmodifiableList(adminServices);
        return newAdminServices;
    }

    public int numberOfAdminServices()
    {
        int number = adminServices.size();
        return number;
    }

    public boolean hasAdminServices()
    {
        boolean has = adminServices.size() > 0;
        return has;
    }

    public int indexOfAdminService(Service aAdminService)
    {
        int index = adminServices.indexOf(aAdminService);
        return index;
    }
    /* Code from template association_GetMany */
    public User getUser(int index)
    {
        User aUser = users.get(index);
        return aUser;
    }

    public List<User> getUsers()
    {
        List<User> newUsers = Collections.unmodifiableList(users);
        return newUsers;
    }

    public int numberOfUsers()
    {
        int number = users.size();
        return number;
    }

    public boolean hasUsers()
    {
        boolean has = users.size() > 0;
        return has;
    }

    public int indexOfUser(User aUser)
    {
        int index = users.indexOf(aUser);
        return index;
    }
    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfAdminServices()
    {
        return 0;
    }
    /* Code from template association_AddUnidirectionalMany */
    public boolean addAdminService(Service aAdminService)
    {
        boolean wasAdded = false;
        if (adminServices.contains(aAdminService)) { return false; }
        adminServices.add(aAdminService);
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeAdminService(Service aAdminService)
    {
        boolean wasRemoved = false;
        if (adminServices.contains(aAdminService))
        {
            adminServices.remove(aAdminService);
            wasRemoved = true;
        }
        return wasRemoved;
    }
    /* Code from template association_AddIndexControlFunctions */
    public boolean addAdminServiceAt(Service aAdminService, int index)
    {
        boolean wasAdded = false;
        if(addAdminService(aAdminService))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfAdminServices()) { index = numberOfAdminServices() - 1; }
            adminServices.remove(aAdminService);
            adminServices.add(index, aAdminService);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveAdminServiceAt(Service aAdminService, int index)
    {
        boolean wasAdded = false;
        if(adminServices.contains(aAdminService))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfAdminServices()) { index = numberOfAdminServices() - 1; }
            adminServices.remove(aAdminService);
            adminServices.add(index, aAdminService);
            wasAdded = true;
        }
        else
        {
            wasAdded = addAdminServiceAt(aAdminService, index);
        }
        return wasAdded;
    }
    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfUsers()
    {
        return 0;
    }
    /* Code from template association_AddUnidirectionalMany */
    public boolean addUser(User aUser)
    {
        boolean wasAdded = false;
        if (users.contains(aUser)) { return false; }
        users.add(aUser);
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeUser(User aUser)
    {
        boolean wasRemoved = false;
        if (users.contains(aUser))
        {
            users.remove(aUser);
            wasRemoved = true;
        }
        return wasRemoved;
    }
    /* Code from template association_AddIndexControlFunctions */
    public boolean addUserAt(User aUser, int index)
    {
        boolean wasAdded = false;
        if(addUser(aUser))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
            users.remove(aUser);
            users.add(index, aUser);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveUserAt(User aUser, int index)
    {
        boolean wasAdded = false;
        if(users.contains(aUser))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
            users.remove(aUser);
            users.add(index, aUser);
            wasAdded = true;
        }
        else
        {
            wasAdded = addUserAt(aUser, index);
        }
        return wasAdded;
    }

    public void delete()
    {
        adminServices.clear();
        users.clear();
        super.delete();
    }

    // line 44 "model.ump"
    public void deleteUser(){

    }

    // line 45 "model.ump"
    public void modifyService(){

    }

    // line 46 "model.ump"
    public void addRequirement(){

    }

    // line 47 "model.ump"
    public void deleteRequirement(){

    }

    // line 48 "model.ump"
    public void addService(){

    }

    // line 49 "model.ump"
    public void deleteService(){

    }

}



