/**
 * Instances of the Book class are book objects held within the LibraryBookshelf and used throughout the application.
 * An object of the book class will thus hold information pertaining to the books. This includes the title of the book, and name of the author (forename and surname).
 * The onLoanto field represents the index of the SortedArrayList (integer) the book is loaned out to. This is -1 only if the book is not on loan; so should not be accessed without reference to the LibraryBookshelf object (responsible for managing books).
 * Methods of the Book class have been made package-private where they are only intended to be accessible from instances of the LibraryBookshelf class within the application.
 * The Book class implements a comparator such that case-sensitive information is ignored when evaluated by the compareTo method.
 * Methods of the Book class have been made package-private as they are only intended to be used by instances of the LibraryBookshelf class (the LibraryBookshelf holds the Books in the Library).
 */
class Book implements Comparable<Book> {
    private final String title;
    private final String authorForename;
    private final String authorSurname;
    private int onLoanTo;


    /**
     * The method Book is a constructor method responsible for creating an instance of the Book class with the designated parameters.
     *
     * @param title          a String which represents the new title of the book.
     * @param authorForename a String which represents the new author forename of the book.
     * @param authorSurname  a String which represents the new author surname of the book.
     *                       By default the  book is initialized as not on loan.
     */
    Book(String title, String authorForename, String authorSurname) {
        this.title = title;
        this.authorForename = authorForename;
        this.authorSurname = authorSurname;
        onLoanTo = -1;
    }

    /**
     * The method compareTo overrides a method of the Comparable interface implemented.
     * By returning "compareToIgnoreCase", this method declares if the author surname of this object is less than the surname of the object specified by the parameter is equal to a value less the 0, then the author surname of this object is lexicographically sorted before the object specified in the parameter.
     *
     * @param o refers to the object to compare to.
     * @return returns negative if the value of the author surname of this object is lexicographically sorted before the author surname of the object 'o'.
     */
    @Override
    public int compareTo(Book o) {
        return getAuthorSurname().compareToIgnoreCase(o.getAuthorSurname());
    }

    /**
     * The method getAuthorForename returns the author forename of the book.
     *
     * @return returns the value of the author forename.
     */
    String getAuthorForename() {
        return authorForename;
    }

    /**
     * The method getAuthorSurname returns the author forename of the book.
     *
     * @return returns the value of the author surname.
     */
    String getAuthorSurname() {
        return authorSurname;
    }

    /**
     * The method getBookByAuthorSurname returns the authorSurname, authorForename, and title of the book in a standard format.
     *
     * @return returns the author surname, author forename, and title of the book in the format "Author Surname, Author Forename. Book Title.".
     */
    String getBookByAuthorSurname() {
        return authorSurname + ", " + authorForename + ". " + title + ".";
    }

    /**
     * The method isOnLoan evaluates whether the book is on loan.
     *
     * @return returns true if the book is on loan.".
     */
    boolean isOnLoan() {
        return onLoanTo != -1;
    }

    /**
     * The method isOnLoanTo returns the index of the SortedArrayList the book is on loan to in the LibraryUserdata object.
     *
     * @return returns the index of the User object of the library's userdata that the book is on loan to.".
     */
    int getOnLoanTo() {
        return onLoanTo;
    }

    /**
     * The method setOnLoanTo sets the value of onLoanTo.  This operation should only be performed by instances of the LibraryBookshelf class.
     */
    void setOnLoanTo(int userId) {
        onLoanTo = userId;
    }

    /**
     * The method getTitle returns the title of the book.
     *
     * @return returns the title of the book.
     */
    String getTitle() {
        return title;
    }
}
