
/**
 * Instances of the User class are user objects held within the LibraryUserdata and used throughout the application.
 * An object of the user class will thus hold information pertaining to the users. This includes the name of the user and details about the number of books they have loaned.
 * The User class implements a comparator such that case-sensitive information is ignored when evaluated by the compareTo method.
 * Methods of the User class have been made package-private as they are only intended to be used by instances of the LibraryUserdata class (the LibraryUserdata holds the Users in the Library).
 */
class User implements Comparable<User> {
    private String forename;
    private String surname;
    private byte booksLoaned;

    /**
     * The User method is a constructor method used when constructing a new User object.
     *
     * @param forename specifies the forename of the user.
     * @param surname  specifies the surname of the user.
     */
    User(String forename, String surname) {
        this.forename = forename;
        this.surname = surname;
        booksLoaned = 0;
    }


    /**
     * The method compareTo overrides a method of the Comparable interface implemented.
     * By returning "compareToIgnoreCase", this method declares if the surname of this object is less than the surname of the object specified by the parameter is equal to a value less the 0, then the surname of this object is lexicographically sorted before the object specified in the parameter.
     * In this case if the surname is an equal string then the 'forename' field decides this order.
     *
     * @param o refers to the object to compare to.
     * @return returns negative if the value of the author surname of this object is lexicographically sorted before the author surname of the object 'o'.
     */
    @Override
    public int compareTo(User o) {
        if (o.surname.equals(surname)) {
            return forename.compareToIgnoreCase(o.forename);
        }
        return surname.compareToIgnoreCase(o.surname);
    }

    /**
     * The getForename method returns the forename of the user.
     *
     * @return returns the forename of the user.
     */
    String getForename() {
        return forename;
    }

    /**
     * The getSurname method returns the surname of the user.
     *
     * @return returns the surname of the user.
     */
    String getSurname() {
        return surname;
    }

    /**
     * The getNameBySurname method returns the surname followed by the name as a formatted string format...
     *
     * @return returns the 'surname' and 'forename' fields as formatted "Surname, Forename".
     */
    String getNameBySurname() {
        return surname + ", " + forename;
    }

    /**
     * The getBooksLoaned method returns the number of books loaned by the user.
     *
     * @return returns 'booksLoaned'.
     */
    byte getBooksLoaned() {
        return booksLoaned;
    }

    /**
     * The decrementLoanedBooks method decrements the value of booksLoaned by 1. This operation should only be performed by instances of the LibraryUserdata class.
     */
    void decrementLoanedBooks() {
        booksLoaned--;
    }

    /**
     * The incrementLoanedBooks method increments the value of booksLoaned by 1.  This operation should only be performed by instances of the LibraryUserdata class.
     */
    void incrementLoanedBooks() {
        booksLoaned++;
    }
}
