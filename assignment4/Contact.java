/**
* Holds the logic for each contact in the phone book
* 
* @author Julian Hernandez
* @version v1.0
*/
public class  Contact{
    /** first Name */
    public String firstName;
    /** last Name */
    public String lastName;
    /** home Number */
    public long homeNumber;
    /** office Number */
    public long officeNumber;
    /** email Address */
    public String emailAddress;

    /** Constructor for a contact
    @param firstName first name
    @param lastName last name
    @param homeNumber home number
    @param officeNumber office number
    @param emailAddress the email address */
    public Contact(String firstName, String lastName, long homeNumber, long officeNumber, String emailAddress){
        this.firstName = firstName;
        this.lastName = lastName;
        this.homeNumber = homeNumber;
        this.officeNumber = officeNumber;
        this.emailAddress = emailAddress;
    }

    /** Getter
    @return the first name */
    public String getFirstName() {
        return this.firstName;
    }

    /** Getter
    @return the last name */
    public String getLastName() {
        return this.lastName;
    }

    /** Getter
    @return the home number */
    public long getHomeNumber() {
        return this.homeNumber;
    }

    /** Getter
    @return the office number */
    public long getOfficeNumber() {
        return this.officeNumber;
    }

    /** Getter
    @return the email address */
    public String getEmailAddress() {
        return this.emailAddress;
    }


    /**Setter
    @param firstName first Name*/
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**Setter
    @param lastName last Name*/
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**Setter
    @param homeNumber home Number*/
    public void setHomeNumber(long homeNumber) {
        this.homeNumber = homeNumber;
    }

    /**Setter
    @param officeNumber office Number*/
    public void setOfficeNumber(long officeNumber) {
        this.officeNumber = officeNumber;
    }

    /**Setter
    @param emailAddress email Address*/
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /** Returns this contact as a string */
    public String toString() {
        String home;
        String work;
        if (this.homeNumber == 0) {
            home = "-";
        } else {
            home = Long.toString(this.homeNumber);
        }
        if (this.officeNumber == 0) {
            work = "-";
        } else {
            work = Long.toString(this.officeNumber);
        }
        return String.format("%-10s%-15s%-15s%-20s%s\n", 
                            this.firstName, this.lastName, home, work,this.emailAddress
        );
    }
}
