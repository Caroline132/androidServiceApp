package com.group15.seg2105finalproject.branch;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import com.group15.seg2105finalproject.customer.Customer;

// line 28 "model.ump"
// line 96 "model.ump"
public class Rating
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Rating Attributes
    private int value;
    private String comment;

    //Rating Associations
    private Customer customer;
    private Branch branch;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Rating(int aValue, String aComment, Customer aCustomer, Branch aBranch)
    {
        value = aValue;
        comment = aComment;
        boolean didAddCustomer = setCustomer(aCustomer);
        if (!didAddCustomer)
        {
            throw new RuntimeException("Unable to create rating due to customer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
        boolean didAddBranch = setBranch(aBranch);
        if (!didAddBranch)
        {
            throw new RuntimeException("Unable to create rating due to branch. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setValue(int aValue)
    {
        boolean wasSet = false;
        value = aValue;
        wasSet = true;
        return wasSet;
    }

    public boolean setComment(String aComment)
    {
        boolean wasSet = false;
        comment = aComment;
        wasSet = true;
        return wasSet;
    }

    public int getValue()
    {
        return value;
    }

    public String getComment()
    {
        return comment;
    }
    /* Code from template association_GetOne */
    public Customer getCustomer()
    {
        return customer;
    }
    /* Code from template association_GetOne */
    public Branch getBranch()
    {
        return branch;
    }
    /* Code from template association_SetOneToMany */
    public boolean setCustomer(Customer aCustomer)
    {
        boolean wasSet = false;
        if (aCustomer == null)
        {
            return wasSet;
        }

        Customer existingCustomer = customer;
        customer = aCustomer;
        if (existingCustomer != null && !existingCustomer.equals(aCustomer))
        {
            existingCustomer.removeRating(this);
        }
        customer.addRating(this);
        wasSet = true;
        return wasSet;
    }
    /* Code from template association_SetOneToMany */
    public boolean setBranch(Branch aBranch)
    {
        boolean wasSet = false;
        if (aBranch == null)
        {
            return wasSet;
        }

        Branch existingBranch = branch;
        branch = aBranch;
        if (existingBranch != null && !existingBranch.equals(aBranch))
        {
            existingBranch.removeRating(this);
        }
        branch.addRating(this);
        wasSet = true;
        return wasSet;
    }

    public void delete()
    {
        Customer placeholderCustomer = customer;
        this.customer = null;
        if(placeholderCustomer != null)
        {
            placeholderCustomer.removeRating(this);
        }
        Branch placeholderBranch = branch;
        this.branch = null;
        if(placeholderBranch != null)
        {
            placeholderBranch.removeRating(this);
        }
    }


    public String toString()
    {
        return super.toString() + "["+
                "value" + ":" + getValue()+ "," +
                "comment" + ":" + getComment()+ "]" + System.getProperties().getProperty("line.separator") +
                "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null") + System.getProperties().getProperty("line.separator") +
                "  " + "branch = "+(getBranch()!=null?Integer.toHexString(System.identityHashCode(getBranch())):"null");
    }
}




