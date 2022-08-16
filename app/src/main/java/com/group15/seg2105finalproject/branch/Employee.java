package com.group15.seg2105finalproject.branch;

import com.group15.seg2105finalproject.main.User;

import java.util.HashMap;
import java.util.List;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/



// line 12 "model.ump"
// line 69 "model.ump"
public class Employee extends User
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Employee Associations
    private Branch branch;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Employee(String aUsername, String aPassword, String aFirstName, String aLastName, String aEmail, Branch aBranch)
    {
        super(aUsername, aPassword, aFirstName, aLastName, aEmail);
        boolean didAddBranch = setBranch(aBranch);
        if (!didAddBranch)
        {
            throw new RuntimeException("Unable to create employee due to branch. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    //------------------------
    // INTERFACE
    //------------------------
    /* Code from template association_GetOne */
    public Branch getBranch()
    {
        return branch;
    }
    /* Code from template association_SetOneToOptionalOne */
    public boolean setBranch(Branch aNewBranch)
    {
        boolean wasSet = false;
        if (aNewBranch == null)
        {
            //Unable to setBranch to null, as employee must always be associated to a branch
            return wasSet;
        }

        Employee existingEmployee = aNewBranch.getEmployee();
        if (existingEmployee != null && !equals(existingEmployee))
        {
            //Unable to setBranch, the current branch already has a employee, which would be orphaned if it were re-assigned
            return wasSet;
        }

        Branch anOldBranch = branch;
        branch = aNewBranch;
        branch.setEmployee(this);

        if (anOldBranch != null)
        {
            anOldBranch.setEmployee(null);
        }
        wasSet = true;
        return wasSet;
    }

    public void delete()
    {
        Branch existingBranch = branch;
        branch = null;
        if (existingBranch != null)
        {
            existingBranch.setEmployee(null);
        }
        super.delete();
    }

}