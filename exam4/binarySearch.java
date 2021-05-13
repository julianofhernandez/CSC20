    /** Sort phone book by last using selection sort algorithm. 
    * @param name the name to search for
    * @return true if the contact is found, false otherwise
    */
    public boolean binarySearch(String name){
        this.selectionSort();
        // Search contact by last name using binary search. 
        // return true if contact is found else false
        int lowIndex = 0;
        int highIndex = this.contacts.size()-1;
        int middleIndex = (lowIndex + highIndex)/2;

        while (lowIndex <= highIndex) {
            middleIndex = (lowIndex + highIndex)/2;
            if(this.contacts.get(middleIndex).getLastName().equals(name)) {
                return true;
            // if middle is less than name
            } else if (this.contacts.get(middleIndex).getLastName().compareTo(name) < 0) {
                lowIndex = middleIndex + 1;
            // if middle is greater than name
            } else if (this.contacts.get(middleIndex).getLastName().compareTo(name) > 0) {
                highIndex = middleIndex - 1;
            }
        }
        // We reach here when element is not present
        // in array
        return false;
    }