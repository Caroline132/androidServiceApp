package com.group15.seg2105finalproject.main;

import com.group15.seg2105finalproject.main.Registration;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/



// line 2 "model.ump"
// line 64 "model.ump"
public class User
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //User Attributes
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public User(String aUsername, String aPassword, String aFirstName, String aLastName, String aEmail)
    {
        username = aUsername;
        password = aPassword;
        firstName = aFirstName;
        lastName = aLastName;
        email = aEmail;
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setUsername(String aUsername)
    {
        boolean wasSet = false;
        username = aUsername;
        wasSet = true;
        return wasSet;
    }

    public boolean setPassword(String aPassword)
    {
        boolean wasSet = false;
        password = aPassword;
        wasSet = true;
        return wasSet;
    }

    public boolean setFirstName(String aFirstName)
    {
        boolean wasSet = false;
        firstName = aFirstName;
        wasSet = true;
        return wasSet;
    }

    public boolean setLastName(String aLastName)
    {
        boolean wasSet = false;
        lastName = aLastName;
        wasSet = true;
        return wasSet;
    }

    public boolean setEmail(String aEmail)
    {
        boolean wasSet = false;
        email = aEmail;
        wasSet = true;
        return wasSet;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void delete()
    {}

    /**
     * Return true if username string is valid.
     * @param username
     * @return true if username string is valid.
     */
    public static boolean isUsernameValid(String username) {
        if (username.isEmpty()) {
            return false;
        }

        if (Registration.containsChars(username, ".#$[]")) {
            return false;
        }

        return true;
    }

    /**
     * Return true if password string is valid.
     * @param password
     * @return true if password string is valid.
     */
    public static boolean isPasswordValid(String password) {
        if (password.isEmpty()) {
            return false;
        }
        return true;
    }


    public String toString()
    {
        return super.toString() + "["+
                "username" + ":" + getUsername()+ "," +
                "password" + ":" + getPassword()+ "," +
                "firstName" + ":" + getFirstName()+ "," +
                "lastName" + ":" + getLastName()+ "," +
                "email" + ":" + getEmail()+ "]";
    }
}
