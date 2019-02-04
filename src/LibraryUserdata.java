import java.util.ArrayList;

/**
 * The LibraryUserdata class is responsible for management of all the users at the library.
 * Those users are appended to a SortedArrayList which extends the ArrayList class (see SortedArrayList class for further details).
 * The index of each user in that Sorted Array List 'users' +1 is equal to the userId interpreted by the user interface.
 * All stored information in a LibraryUserdata object should be referenced only as their unique index; similarly all new methods created in the LibraryUserdata class must only make reference to an index in the 'users' SortedArrayList (and never the userId expressed in LibraryIO).
 * Methods of the LibraryUserdata class have been made package-private as they are only intended to be used by instances of the Library object (the Library holds the LibraryUserdata [the userdata of the library).
 */
class LibraryUserdata {
    private SortedArrayList<User> users;

    /**
     * LibraryUserdata is the constructor method for instances of the LibraryUserdata class.
     * The LibraryUserdata class contains only a SortedArrayList of User objects.
     */
    LibraryUserdata() {
        users = new SortedArrayList<>();
    }


    /**
     * The addUser method adds a book to the SortedArrayList 'users'.
     *
     * @param user specifies the book object to be added to 'users'.
     */
    boolean addUser(User user) {
        users.add(user);
        return true;
    }

    /**
     * The clearUsers method clears the SortedArrayList of user objects.
     */
    void clearUsers() {
        users.clear();
    }

    /**
     * The getUser method returns the user object from the SortedArrayList 'users' given the specified index.
     *
     * @param userIndex specifies the index of the users to return from 'users'.
     * @return returns the instance of the User class within the SortedArrayList.
     */
    User getUser(int userIndex) {
        return users.get(userIndex);
    }


    /**
     * The getUsers method returns all users as the SortedArrayList 'users'.
     *
     * @return returns the SortedArrayList 'users'.
     */
    SortedArrayList<User> getUsers() {
        return users;
    }

    /**
     * The getNumberOfUsers method the number of all users stored within the SortedArrayList 'users'.
     *
     * @return returns the size of SortedArrayList 'users'.
     */
    int getNumberOfUsers() {
        return users.size();
    }


    /**
     * The searchUsers method takes an input string and searches for the existence of that input string using the contains method.
     * The method constructs an ArrayList of results and returns that ArrayList.
     * In the future this may use a SortedArrayList, but for the time being this would be more expensive since a SortedArrayList would pass information through the comparator of the object.
     * Since the objects are already sorted this is not a requirement at this time.
     *
     * @param inputDetails specifies the input details to search by.
     * @return returns an ArrayList of the result User objects.
     */
    ArrayList<User> searchUsers(String inputDetails) {
        ArrayList<User> results = new ArrayList<>();
        for (User user : users) {
            if (user.getSurname().contains(inputDetails)) {
                results.add(user);
            }
            if (user.getForename().contains(inputDetails)) {
                results.add(user);
            }
        }
        return results;
    }

    /**
     * The getUserIndexFromSearchOccurrence allows one to delimit the number of times a search matches a given result before the result index is returned.
     * For example if it can be known that there are two users named 'Anna' and I only want the first index of these users there is no point retrieving an ArrayList using the searchUsers method.
     * Alternatively this search halts on the defined number of occurrences and provides an index.
     *
     * @param inputDetails specifies the input string to search by.
     * @param occurrence   specifies the number of occurrences to pass before terminating the search.
     * @return returns the index of the object at the specified location.
     */
    int getUserIndexFromSearchOccurrence(String inputDetails, int occurrence) {
        int index = 0;
        for (User user : users) {
            if (user.getSurname().contains(inputDetails)) {
                occurrence--;
                if (occurrence == 0) {
                    return index;
                }
            }
            if (user.getForename().contains(inputDetails)) {
                occurrence--;
                if (occurrence == 0) {
                    return index;
                }
            }
            index++;
        }
        return -1;
    }

    /**
     * The issueBookToUser is intended to be used when a book is issued to a user.
     * For the time being the maximum defined number of books for any user is 3.
     * The system checks the number of loaned books represented a byte since this is the maximum defined number of books.
     * The system returns false of the user has already loaned 3 books since this is the maximum defined number of books.
     * If the user has not exceeded this limitation then the number of loaned books is incremented by one to signify the user has taken out a new books.
     *
     * @param userId the userId of the book.
     * @return returns true if loaning the book to the user was successful, returns false if the operation was unsuccessful.
     */
    boolean issueBookToUser(int userId) {
        byte loanedBooks = users.get(userId).getBooksLoaned();
        if (loanedBooks == 3) {
            return false;
        } else {
            users.get(userId).incrementLoanedBooks();
            return true;
        }
    }

    /**
     * The issueBookToUser is intended to be used when a book is returned from a user.
     * The system returns false if the user has not loaned any books.
     * If the user has loaned books then the number of books the user has loaned is decremented by 1.
     *
     * @param userId the userId of the book.
     * @return returns true if returning the book from the user was successful, returns false if the operation was unsuccessful.
     */
    boolean returnBookFromUser(int userId) {
        byte loanedBooks = users.get(userId).getBooksLoaned();
        if (loanedBooks == 0) {
            //Cannot return when already = 0.
            return false;
        } else {
            users.get(userId).decrementLoanedBooks();
            return true;
        }
    }


}
