import java.util.ArrayList;

/**
 * The LibraryBookshelf class is responsible for management of all the books at the library.
 * Those books are appended to a SortedArrayList which extends the ArrayList class (see SortedArrayList class for further details).
 * The index of each book in that Sorted Array List 'books' +1 is equal to the bookId interpreted by the user interface.
 * All stored information in a LibraryBookshelf object should be referenced only as their unique index; similarly all new methods created in the LibraryBookshelf class must only make reference to an index in the 'books' SortedArrayList (and never the bookIndex expressed LibraryIO).
 * Methods of the LibraryBookshelf class have been made package-private as they are only intended to be used by instances of the Library class (the Library holds the LibraryBookshelf [the bookshelf of the library).
 */
class LibraryBookshelf {
    private SortedArrayList<Book> books;

    /**
     * LibraryBookshelf is the constructor method for instances of the LibraryBookshelf class.
     * The LibraryBookshelf class contains only a SortedArrayList of Book objects.
     */
    LibraryBookshelf() {
        books = new SortedArrayList<>();
    }


    /**
     * The addBook method adds a book to the SortedArrayList 'books'.
     *
     * @param book specifies the book object to be added to 'books'.
     */
    void addBook(Book book) {
        books.add(book);
    }


    /**
     * The clearBooks method clears the SortedArrayList of book objects.
     */
    void clearBooks() {
        books.clear();
    }

    /**
     * The getBook method returns the book object from the SortedArrayList 'books' given the specified index.
     *
     * @param bookIndex specifies the index of the book to return from 'books'.
     * @return returns the instance of the Book class within the SortedArrayList.
     */
    Book getBook(int bookIndex) {
        return books.get(bookIndex);
    }


    /**
     * The getBooks method returns all books as the SortedArrayList 'books'.
     *
     * @return returns the SortedArrayList 'books'.
     */
    SortedArrayList<Book> getBooks() {
        return books;
    }

    /**
     * The loanBook method takes the relevant precautions to loan a book to a user.
     * <p>
     * The method inspects whether the book is already on loan and if this is the case the method returns the operation cannot be completed.
     * If the book is not already on loan then the method continues with the operation.
     *
     * @param bookIndex specifies the bookIndex to loan.
     * @param userIndex specified the userIndex to be assigned as loaning the book at bookIndex.
     * @return returns true if the operation was successful, returns false if the operation was not successful.
     */
    boolean loanBook(int bookIndex, int userIndex) {
        if (books.get(bookIndex).isOnLoan()) {
            return false;
        } else {
            books.get(bookIndex).setOnLoanTo(userIndex);
            return true;
        }
    }

    /**
     * The returnBook method returns a book which was loaned from the library's bookshelf.
     *
     * @param bookIndex specifies the book to return.
     * @return returns true if the book was not on loan and the operation was successful, returns false if the book was already on loan so the operation could not be completed.
     */
    boolean returnBook(int bookIndex) {
        if (books.get(bookIndex).isOnLoan()) {
            books.get(bookIndex).setOnLoanTo(-1);
            return true;
        } else {
            return false;
        }
    }

    /**
     * The searchBooks method takes an input string and searches for the existence of that input string using the contains method.
     * The method constructs an ArrayList of results and returns that ArrayList.
     * In the future this may use a SortedArrayList, but for the time being this would be more expensive since a SortedArrayList would pass information through the comparator of the object.
     * Since the objects are already sorted this is not a requirement at this time.
     *
     * @param inputDetails specifies the input details to search by.
     * @return returns an ArrayList of the result Book objects.
     */
    ArrayList<Book> searchBooks(String inputDetails) {
        ArrayList<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthorSurname().contains(inputDetails)) {
                results.add(book);
            }
            if (book.getAuthorForename().contains(inputDetails)) {
                results.add(book);
            }
            if (book.getTitle().contains(inputDetails)) {
                results.add(book);
            }
        }
        return results;
    }

    /**
     * The getBookIndexFromSearchOccurrence allows one to delimit the number of times a search matches a given result before the result index is returned.
     * For example if it can be known that there are two books named 'Java' and I only want the first index of these books there is no point retrieving an ArrayList using the searchBooks method.
     * Alternatively this search halts on the defined number of occurrences and provides an index.
     *
     * @param inputDetails specifies the input string to search by.
     * @param occurrence   specifies the number of occurrences to pass before terminating the search.
     * @return returns the index of the object at the specified location.
     */
    int getBookIndexFromSearchOccurrence(String inputDetails, int occurrence) {
        int index = 0;
        for (Book book : books) {
            if (book.getAuthorSurname().contains(inputDetails)) {
                occurrence--;
                if (occurrence == 0) {
                    return index;
                }
            }
            if (book.getAuthorForename().contains(inputDetails)) {
                occurrence--;
                if (occurrence == 0) {
                    return index;
                }
            }
            if (book.getTitle().contains(inputDetails)) {
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
     * The getNumberOfBooks method returns the number of books held in the LibraryBookshelf, in the SortedArrayList books.
     *
     * @return returns the number of books held in 'books'.
     */
    int getNumberOfBooks() {
        return books.size();
    }

    /**
     * The isBookLoanedToUser method determines if a book is already loaned by a user.
     *
     * @param bookIndex specifies the bookIndex to search.
     * @param userIndex specifies the userIndex to search.
     * @return returns true if the book is loaned to the user specified, returns false if the book is not loaned to the user specified.
     */
    boolean isBookLoanedToUser(int bookIndex, int userIndex) {
        Book book = books.get(bookIndex);
        if (!books.get(bookIndex).isOnLoan())
            return false;
        else {
            if (book.getOnLoanTo() == userIndex) {
                return true;
            }
        }
        return false;
    }
}