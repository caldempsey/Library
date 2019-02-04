import java.io.IOException;

/**
 * An instance of the Library class imagines there exists two loosely coupled classes, LibraryBookshelf (representative of the bookshelf of the library), and LibraryUserdata (representative of the userdata of the library).
 * <p>
 * The Library class is responsible for management of the bookshelf of the library and the userdata of the library.
 * The Library class should handle all index reference variables meaningful to the data structure of the application, i.e. userIndex and bookIndex, *not* userId and bookId (displayed to the front end of the user).
 * The Library class is thus responsible for managing operations between these two classes.
 * The Library class also owns a FileWriter (for writing documents) and a FileReader (for reading documents).
 * All objects prefixed with Library are imagined as objects which belong to the library: the bookshelf belongs to the library which specifies all books owned by the library (but the library doesn't own all things books, sometimes they are in the hands of users).
 * The Library evaluates what ought to be the case between operations, so makes a judgement whether to throw back an error.
 * Methods of the Library class are package private as they ought to only receive input or output from methods of the LibraryIO class (for Library input or output).
 */
class Library {
    private LibraryBookshelf bookshelf;
    private LibraryUserdata userdata;


    /**
     * The Library method is a constructor which instantiates an instance of the bookshelf and an instance of userdata (which are persistent objects used throughout the operation of the application).
     */
    Library() {
        bookshelf = new LibraryBookshelf();
        userdata = new LibraryUserdata();
    }


    /**
     * The clearBookshelf method clears the library's bookshelf of all data.
     */
    void clearBookshelf() {
        bookshelf.clearBooks();
    }

    /**
     * The clearUserdata method clears the library's userdata of all data.
     */
    void clearUserdata() {
        userdata.clearUsers();
    }


    /**
     * The getBookshelf method returns the bookshelf object currently in use.
     *
     * @return returns the library's bookshelf.
     */
    LibraryBookshelf getBookshelf() {
        return bookshelf;
    }

    /**
     * The getUserdata method returns the userdata object currently in use.
     *
     * @return returns the library's userdata.
     */
    LibraryUserdata getUserdata() {
        return userdata;
    }

    /**
     * The importDataFromFile method is responsible for passing the required information to return book and user data for the Library from a file.
     * This method is primarily responsible for creating a temporary instance of the LibraryFileReader class and passing the location data to it.
     *
     * @param filepath specifies the filepath of the file to import.
     * @return returns status code of the method fileReader.importDataFromFile (which performs the import operation and is more thorough than we can be here since it can identify different permutations of a syntax error as opposed to lumping them into one exception).
     * The exception to this is a general IOException which only has one permutation (must be indicative of a read error) and must be handled, so does not come with a status code.
     */
    byte importDataFromFile(String filepath) throws IOException {
        byte operationStatusCode;
        LibraryFileReader fileReader = new LibraryFileReader();
        operationStatusCode = fileReader.importDataFromFile(filepath, userdata, bookshelf);
        if (operationStatusCode != 1) {
            clearBookshelf();
            clearUserdata();
        }
        return operationStatusCode;
    }

    /**
     * The issueBookToUser method is responsible for attempting to loan the book from the bookshelf, and issue that book to the user.
     * Returns a different operation status based on the possible outcomes.
     *
     * @param bookIndex specifies the index of the book.
     * @param userIndex specifies the index of the user.
     * @return returns 1 if the operation was a success, returns -1 if the book was already on loan, returns -2 if the user has already loaned out three books.
     */
    byte issueBookToUser(int bookIndex, int userIndex) {

        if (bookshelf.loanBook(bookIndex, userIndex)) {
            if (userdata.issueBookToUser(userIndex)) {
                return 1;
            } else {
                bookshelf.returnBook(bookIndex);
                return -2;
            }
        } else {
            return -1;
        }
    }

    /**
     * The returnBookFromUser method is responsible for attempting safely to return a book from a user..
     * The method makes requisite checks on whether the book is loaned to that user, and if true attempts to return the book from that user.
     * If that operation is unsuccessful, it shouldn't be, then the system will throw an IllegalArgumentException which are interpreted as fatal.
     *
     * @param bookIndex specifies the index of the book.
     * @param userIndex specifies the index of the user.
     * @return returns true if the operation was success, else returns false (or throws exception).
     */
    boolean returnBookFromUser(int bookIndex, int userIndex) {
        if (bookshelf.isBookLoanedToUser(bookIndex, userIndex)) {
            if (!bookshelf.returnBook(bookIndex)) {
                throw new IllegalArgumentException();
            }
            if (userdata.returnBookFromUser(userIndex)) {
                return true;
            } else throw new IllegalArgumentException();
        } else {
            return false;
        }
    }


    /**
     * The writeReturnNotice method creates an instance of the LibraryFileWriter object to write a return notice with.
     * This method is primarily responsible for creating a temporary instance of the LibraryFileWriter class and passing required parameters.
     * Since interactions between objects of the two classes LibraryUserdata and LibraryBookshelf are managed by instances of this class, this method is also responsible for identifying who to write the return notice to given a specified bookIndex.
     *
     * @param bookIndex        specifies the index of the book.
     * @param returnNoticePath specifies the filepath to export the return notice to.
     * @return returns the result of the operation performed by the instance of the FileWriter class.
     * @throws IOException if an IOException occurs when writing to the file.
     */
    boolean writeReturnNotice(int bookIndex, String returnNoticePath) throws IOException {
        Book book = bookshelf.getBook(bookIndex);
        User userOnLoanTo = userdata.getUser(book.getOnLoanTo());
        LibraryFileWriter fileWriter = new LibraryFileWriter();
        return fileWriter.writeReturnNotice(userOnLoanTo.getForename(), book.getBookByAuthorSurname(), returnNoticePath);
    }
}
