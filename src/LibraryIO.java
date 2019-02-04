import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * <h1>Library</h1>
 * <p>
 * This application is designed as a demonstrative tool to fulfill the specifications of the "Assessed Coursework 2: Interactive System" assignment for CSC8001.
 * </p>
 * <p>
 * This task is to design and implement an object oriented program which is able to take an input of library data from a file of a specific format containing users and books, and allow a user (librarian) to return and issue books to those users.
 * It is specified the system must take user input before displaying that interactive system. This functionality has been extended to default directories for different operating systems. In the case those default directories cannot be detected a user is able to specify directories they are familiar with (forward error handling). Further details on how the input file is read is documented in the LibraryFileReader class.
 * It is specified the system must contain at least the commands f (finish running the program), b (to display on the screen the information about all the books in the library), u (to display on the screen the information about all the users), i (to update the stored data when a book is issued to a user), and r (to update the stored data when a user returns a book to the library).
 * It is specified to include validation procedures to validate (and in conjunction to this confirm) to the user that the book they wish to either issue or return to specific users is in fact the correct book or user.
 * It is specified that all User and Book objects are stored within a SortedArrayList object which extends the ArrayList object such that it is able to lexicographically order books and users of the array. This is achieved by implementing the Comparable interface in those objects and using the compareTo method to define an ascending sorting procedure (see SortedArrayList and User or Book objects for further details).
 * It is specified there should exist relevant procedures. To illustrate these include displaying an appropriate messages on the screen and outputting notices asking users to return books.
 * </p>
 * <p>
 * Please be advised that the program is designed to frequently clear the screen in the BlueJ terminal. As a result your console experience may be affected by this.
 * </p>
 * <p>
 * Please be advised this JavaDoc is thorough for the purposes of demonstrating understanding.
 * </p>
 * <p>
 * The LibraryIO class implements a user interface which contains the main method of the application. It is an abstract class, containing a number of methods which act as interfaces (or menus) to help guide the user throughout the application.
 * </p>
 * <p>
 * The class is responsible for aggressively validating user input to provide user feedback.
 * </p>
 * <p>
 * The class is also responsible for handling ID information for Books and Users. This information is not semantically representative of the internal structure of the application, this refers to userIndex and bookIndex which is equal to the userId and bookId incremented by 1.
 * </p>
 * <p>
 * The only method public in this class is the main method as the main method must always be used as the starting point for the execution of this interface: it is only possible for the user interface to be initialized before data has been read.
 * </p>
 * <p>
 * Special thanks to Steve Riddle, Marta Koutny and the demonstrators of CSC8001 who helped me understand the Java language and documentation. I hope you enjoy the application.
 * </p>
 */
public class LibraryIO {

    /**
     * The method main is responsible for the initial execution of the application, the 'initialization' of the application.
     * As such it instantiates the initial objects required to run the user interface.
     * At present the process to initialize the application is
     * (1) Read data of the known format from a file (defined in LibraryFileReader) and import that data into the Library's data structure i.e. userdata.
     * (2) Initialize the user interface so the user can work with the program interactively.
     * <p>
     * The program should not initialize the user interface until data of the known format has been detected.
     * This is made explicit by a switch case which uses a status code 'importStatusCode' such that it is only possible for this value to be equal to 1 if stage 1 of initialization procedure was successful (at which point begin stage 2).
     */
    public static void main(String[] args) {
        Scanner userInput;
        Library library = new Library();
        userInput = new Scanner(System.in);
        String pathInput;
        boolean fileInitialized = false;
        byte importStatusCode;
        String defaultPath = getDefaultPathByOperatingSystem() + "inputdata.txt";
        while (!fileInitialized) {
            System.out.println("Library Interactive System: Initialisation");
            System.out.println();
            System.out.println("Please enter the path to the file (with extension) containing the data you would like to import, or type 'f' to exit.");
            if (!defaultPath.equals("")) {
                System.out.println("Alternatively, pressing enter will use the file at the default path '" + defaultPath + "' for your operating system.");
            }
            System.out.println();
            System.out.print("> ");
            pathInput = userInput.nextLine();
            System.out.println();

            if (pathInput.equals(""))
                pathInput = defaultPath;
            try {
                importStatusCode = library.importDataFromFile(pathInput);
            } catch (IOException e) {
                System.out.println("The file at " + pathInput + " could not be read." +
                        "\nPlease validate the file exists and ensure your local system administrator has granted you permissions to read that file.");
                System.out.println("\nPlease press enter to continue.");
                userInput.nextLine();
                continue;
            }

            {
                if (importStatusCode == 1) {
                    System.out.println("Success!");

                    fileInitialized = true;
                } else if (importStatusCode == -1) {
                    System.out.println("An invalid value has been detected for the number of items stated at " + pathInput + "" +
                            "\nPlease check your data and try again. Please be advised the value for each import type (books, users .etc) cannot be below 0 or equal to a value over 2147483647.");
                    System.out.println("\nPlease press enter to continue.");
                    userInput.nextLine();
                } else if (importStatusCode == -2) {
                    System.out.println("The format of your data is incorrect.\nPlease check your data and try again.");
                    System.out.println("\nPlease press enter to continue.");
                    userInput.nextLine();
                } else {
                    System.out.println("An unknown error has occurred. The system will now terminate.");
                    break;
                }
            }

        }


        if (fileInitialized) {
            userInterface(library, userInput);
        } else {
            terminate(library, userInput, -1);
        }
    }

    /**
     * The userInterface method presents a switch case of commands which refer to the other methods in the LibraryIO class.
     * The userInterface allows a user to interactively work with the application. To help accessibility, commands for both advanced and novice users of the application have been implemented (i.e. i (issue) or i:id (issue by id).
     * It would be wasteful to create new object of the Scanner class whenever user input is required at every stage of the interface, so we pass the object as a parameter to the next class which requires it, starting with the user interface.
     * Throughout the application the style of creating menus is consistent with the style in this method (a while loop with a boolean required to exit). For this we choose boolean !exit instead of !menuExit as the variable name to make it clear we intend to close the application.
     *
     * @param library   refers to the library object to perform operations on used throughout the entire application.
     * @param userInput refers to the scanner object required for taking user input instantiated in the main method and used throughout the entire application.
     */
    private static void userInterface(Library library, Scanner userInput) {
        boolean exit = false;
        String userSelection;
        System.out.println("\nWelcome to the application!\n");
        Integer userId;
        Integer bookId;
        while (!exit) {
            try {
                System.out.print('\u000C');

                System.out.println("Library Interactive System: Main Menu" +
                        "\n" +
                        "\nPlease select a command. Type help for a list of commands.");
                System.out.println();
                System.out.print("> ");
                userSelection = userInput.nextLine();
                if (userSelection != null) {
                    switch (userSelection) {
                        case "b":
                            printAllBooks(library, userInput);
                            break;
                        case "f":
                            exit = true;
                            break;
                        case "help":
                            printCommands(userInput);
                            break;
                        case "i":
                            searchInterface(library, userInput, "issue");
                            break;
                        case "i:id":
                            System.out.println();
                            System.out.println("Please enter the User ID you would like to issue a book to. Otherwise press enter to return to the main menu.");
                            System.out.println();
                            System.out.print("> ");
                            userSelection = userInput.nextLine();
                            if (userSelection.equals("")) {
                                continue;
                            }
                            userId = Integer.valueOf(userSelection);
                            if (userId <= 0) {
                                throw new NumberFormatException();
                            }
                            System.out.println();
                            System.out.println("Please enter the Book ID you would like to issue.");
                            System.out.println();
                            System.out.print("> ");
                            userSelection = userInput.nextLine();
                            if (userSelection.equals("")) {
                                continue;
                            }
                            bookId = Integer.valueOf(userSelection);
                            if (userId <= 0) {
                                throw new NumberFormatException();
                            }
                            issueBooksInterface(library, userInput, bookId, userId);
                            break;
                        case "r":
                            searchInterface(library, userInput, "return");
                            break;
                        case "r:id":
                            System.out.println();
                            System.out.println("Please enter the User ID you would like to return a book from.");
                            System.out.println();
                            System.out.print("> ");
                            userSelection = userInput.nextLine();
                            if (userSelection.equals("")) {
                                continue;
                            }
                            userId = Integer.valueOf(userSelection);
                            if (userId <= 0) {
                                throw new NumberFormatException();
                            }
                            System.out.println();
                            System.out.println("Please enter the Book ID you would like to return.");
                            System.out.println();
                            System.out.print("> ");
                            userSelection = userInput.nextLine();
                            if (userSelection.equals("")) {
                                continue;
                            }
                            bookId = Integer.valueOf(userSelection);
                            if (userId <= 0) {
                                throw new NumberFormatException();
                            }
                            returnBooksInterface(library, userInput, bookId, userId);
                            break;
                        case "u":
                            printAllUsers(library, userInput);
                            break;
                        default:
                            break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println("To perform this operation you must enter a valid ID.");
                System.out.println("\nPlease press enter to return to the selection menu.");
                userInput.nextLine();

            }
        }
        terminate(library, userInput, 0);
    }

    /**
     * The getDefaultPathByOperatingSystem method contains a set of default paths for different operating systems (see switch case).
     * The operating system is identified by the first index of the string split of the System.getproperty("os.name") via the String.split method.
     * That implementation is a temporary implementation used to generify operating systems i.e. all modern Windows operating systems (at least for now) have a reference to the C:/ drive.
     * Future implementations should consider implementing nuances of the method's switch case.
     * If the method returns an empty string it should be assumed that an operating system cannot be detected.
     *
     * @return returns the default path for the operating system.
     */
    private static String getDefaultPathByOperatingSystem() {
        String os = (System.getProperty("os.name")).split("\\s+")[0];
        String path = "";
        switch (os) {
            case "Windows":
                path = "C:/librarydata/";
                break;
            case "Linux":
                path = System.getProperty("user.home") + "/librarydata/";
            default:
                break;
        }
        return path;
    }

    /**
     * The issueBooksInterface method defines the general interface which attempts to issue a book to a particular user.
     * To illustrate, a book and user may be identified by the search function but it is not the responsibility of the search function to issue books to that user.
     * The method is responsible for performing a final single validation check on ID information only, to check whether the user is a valid user and whether the book is a valid book, before attempting to issue a book to that user.
     * Appropriate messages are displayed in cases.
     * a) The book is not on loan and can be loaned by that user.
     * b) The book is not on loan and cannot be loaned by that user i.e. too many books.
     * c) The book is on loan and cannot be loaned by that user (is on loan), issues a return notice to the user who has loaned a book.
     * d) The system has attempted to issue a return notice but has failed, asks to check permissions.
     *
     * @param library   refers to the library object to perform operations on used throughout the entire application.
     * @param userInput refers to the scanner object required for taking user input instantiated in the main method and used throughout the entire application.
     * @param bookId    is the bookId to validate (and convert to a bookIndex used by the Library's internal architecture).
     * @param userId    is the userId to validate (and convert to a userIndex used by the Library's internal architecture).
     */
    private static void issueBooksInterface(Library library, Scanner userInput, int bookId, int userId) {
        String userSelection;
        int userIndex = userId - 1;
        int bookIndex = bookId - 1;
        try {
            if (bookIndex < 0 || userIndex < 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if ((library.getBookshelf().getNumberOfBooks() - 1) < bookIndex || (library.getUserdata().getNumberOfUsers() - 1) < userIndex) {
                throw new ArrayIndexOutOfBoundsException();
            }
            User user = library.getUserdata().getUser(userIndex);
            Book book = library.getBookshelf().getBook(bookIndex);
            System.out.println();
            System.out.println("The system will attempt to issue...");
            System.out.println();
            System.out.println("Book ID. Author Surname, Author Forename. Title. On Loan[?].");
            System.out.println();
            System.out.print(bookId + ". " + book.getAuthorSurname() + ", " + book.getAuthorForename() + ". " + book.getTitle() + ".");
            if (book.getOnLoanTo() == -1) {
                System.out.print(" No.");
            } else {
                System.out.print(" Yes.");
            }
            System.out.println();
            System.out.println();
            System.out.println("To user...");
            System.out.println();
            System.out.println("User ID. Surname, Forename. Books Loaned.");
            System.out.println();
            System.out.println(userId + ". " + user.getNameBySurname() + ". " + user.getBooksLoaned() + ".");
            System.out.println();
            System.out.println("Please type 'yes' or 'y' to confirm the above details are correct. Please input any other data to return back to the main menu.");
            System.out.println();
            System.out.print("> ");
            userSelection = userInput.nextLine();
            switch (userSelection) {
                case "yes":
                case ("y"):
                    if (library.issueBookToUser(bookIndex, userIndex) == 1) {
                        System.out.println();
                        System.out.println("The book has been successfully issued to the user." +
                                "\n" + library.getUserdata().getUser(userIndex).getNameBySurname() + " has now loaned " + library.getUserdata().getUser(userIndex).getBooksLoaned() + " items.");
                        System.out.println("Pressing enter will return you to the main menu.");
                        userInput.nextLine();
                    } else if (library.issueBookToUser(bookIndex, userIndex) == -1) {
                        System.out.println();
                        System.out.println("The book is already on loan");
                        String path = getDefaultPathByOperatingSystem();
                        if (path.equals("")) {
                            System.out.println("A default path to output a return notice could not be detected." +
                                    "\nPlease enter the path you would like to output the return notice to...");
                            System.out.println();
                            System.out.print("> ");
                            path = userInput.nextLine();
                        } else {
                            path = getDefaultPathByOperatingSystem() + "return notice.txt";
                        }
                        try {
                            if ((library.writeReturnNotice(bookIndex, (path)))) {
                                System.out.println();
                                System.out.println("Wrote return notice to " + path + ".");
                                System.out.println();
                                System.out.println("Pressing enter will return you to the main menu.");
                                userInput.nextLine();
                            }
                        } catch (IOException e) {
                            System.out.println("\nFailed to write data to " + path);
                            System.out.println("The operation has been cancelled. Please contact your system administrator and ensure you have read and write permissions to " + path + ".");
                            System.out.println();
                            System.out.println("Pressing enter will return you to the main menu.");
                            userInput.nextLine();
                        }
                    } else if (library.issueBookToUser(bookIndex, userIndex) == -2) {
                        System.out.println();
                        System.out.println("This user has already loaned out three books.");
                        System.out.println();
                        System.out.println("Pressing enter will return you to the selection screen.");
                        userInput.nextLine();
                    }
                    break;
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println();
            System.out.println("The application could not find information at id(s) specified.");
            System.out.println();
            System.out.println("Press enter to return to the main menu.");
            userInput.nextLine();
        }
    }

    /**
     * The printAllBooks method returns general information about all books and prints that information to the screen.
     *
     * @param library   refers to the library object to perform operations on used throughout the entire application.
     * @param userInput refers to the scanner object required for taking user input instantiated in the main method and used throughout the entire application.
     */
    private static void printAllBooks(Library library, Scanner userInput) {
        System.out.println();
        int bookIndex = 1;
        System.out.println("Book ID. Author Surname, Author Forename. Title. On Loan[?].");
        System.out.println();
        for (Book book : library.getBookshelf().getBooks()) {
            System.out.print(bookIndex + ". " + book.getAuthorSurname() + ", " + book.getAuthorForename() + ". " + book.getTitle() + ".");
            if (book.getOnLoanTo() == -1) {
                System.out.print(" No.");
            } else {
                System.out.print(" Yes.");
            }
            System.out.println();
            bookIndex++;
        }
        System.out.println("\nPlease press enter to continue.");
        userInput.nextLine();
    }

    /**
     * The printCommands method returns information about all commands accessible by the user interface (the userInterface method).
     *
     * @param userInput refers to the scanner object required for taking user input instantiated in the main method and used throughout the entire application.
     */
    private static void printCommands(Scanner userInput) {
        System.out.println("\n" +
                "The commands available from the main menu are...\n\n" +
                "b - to display on the screen the information about all the books in the library. " +
                "\nu - display on the screen the information about all the users." +
                "\ni - to update the stored data when a book is issued to a user." +
                "\ni:id - to update the stored data when a book is issued to a user by IDs only." +
                "\nr - to update the stored data when a user returns a book to the library." +
                "\nr:id - to update the stored data when a user returns a book to the library by IDs only." +
                "\nf - terminate the application.");
        System.out.println("\nPlease press enter to continue.\n");
        userInput.nextLine();
    }

    /**
     * The printAllUsers method returns general information about all users and prints that information to the screen.
     *
     * @param library   refers to the library object to perform operations on used throughout the entire application.
     * @param userInput refers to the scanner object required for taking user input instantiated in the main method and used throughout the entire application.
     */
    private static void printAllUsers(Library library, Scanner userInput) {
        System.out.println();
        int userIndex = 1;
        System.out.println("User ID. Surname, Forename. Books Loaned.");
        System.out.println();
        for (User user : library.getUserdata().getUsers()) {
            System.out.println(userIndex + ". " + user.getNameBySurname() + ". " + user.getBooksLoaned() + ".");
            userIndex++;
        }
        System.out.println("\nPlease press enter to continue.");
        userInput.nextLine();
    }

    /**
     * The returnBooksInterface method defines the general interface which attempts to return a book from a particular user.
     * To illustrate, a book and user may be identified by the search function but it is not the responsibility of the search function to return books from that user.
     * The method is responsible for performing a final single validation check on ID information only, to check whether the user is a valid user and whether the book is a valid book, before attempting to return a book from that user.
     * Appropriate messages are displayed in cases.
     * a) The book has successfully been returned by that user.
     * b) The book cannot be returned by that user (i.e. the book was not issued to that user).
     * c) The book was issued to that user but cannot be returned by that user (raises system failure).
     *
     * @param library   refers to the library object to perform operations on used throughout the entire application.
     * @param userInput refers to the scanner object required for taking user input instantiated in the main method and used throughout the entire application.
     * @param bookId    is the bookId to validate (and convert to a bookIndex used by the Library's internal architecture).
     * @param userId    is the userId to validate (and convert to a userIndex used by the Library's internal architecture).
     */
    private static void returnBooksInterface(Library library, Scanner userInput, int bookId, int userId) {
        String userSelection;
        int userIndex = userId - 1;
        int bookIndex = bookId - 1;
        try {
            if (bookId < 0 || userIndex < 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if ((library.getBookshelf().getNumberOfBooks() - 1) < bookIndex || (library.getUserdata().getNumberOfUsers() - 1) < userIndex) {
                throw new ArrayIndexOutOfBoundsException();
            }
            Book book = library.getBookshelf().getBook(bookIndex);
            User user = library.getUserdata().getUser(userIndex);
            System.out.println();
            System.out.println("The system will attempt to return...");
            System.out.println();
            System.out.println("Book ID. Author Surname, Author Forename. Title. On Loan[?]");
            System.out.println();
            System.out.print(bookId + ". " + book.getAuthorSurname() + ", " + book.getAuthorForename() + ". " + book.getTitle() + ".");
            if (book.getOnLoanTo() == -1) {
                System.out.print(" No.");
            } else {
                System.out.print(" Yes.");
            }
            System.out.println();
            System.out.println();
            System.out.println("From user...");
            System.out.println();
            System.out.println("User ID. Surname, Forename. Books Loaned.");
            System.out.println();
            System.out.println(userId + ". " + user.getNameBySurname() + ". " + user.getBooksLoaned() + ".");
            System.out.println();
            System.out.println("Please type 'yes' or 'y' to confirm the above details are correct. Please input any other data to return back to the main menu.");
            System.out.println();
            System.out.print("> ");
            userSelection = userInput.nextLine();
            switch (userSelection) {
                case "yes":
                case ("y"):
                    if (library.returnBookFromUser(bookIndex, userIndex)) {
                        System.out.println();
                        System.out.println("The book has been successfully returned." +
                                "\n" + library.getUserdata().getUser(userIndex).getNameBySurname() + " has now loaned " + library.getUserdata().getUser(userIndex).getBooksLoaned() + " items.\n");
                        System.out.println("Pressing enter will return you to the main menu.");
                        userInput.nextLine();
                    } else {
                        System.out.println();
                        System.out.println("The book is not on loan to this user.");
                        System.out.println("Pressing enter will return you to the main menu.");
                        userInput.nextLine();
                    }
                    break;
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("\nThe application could not find information at id(s) specified.");
            System.out.println();
            System.out.println("Press enter to return to the main menu.");
            userInput.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println();
            e.printStackTrace();
            System.out.println("\nA fatal system error has occurred, IllegalArgumentException at library.returnBookFromUser(). Please contact the software supplier with reproduction steps if possible. Please attach the log above.");
            System.out.println("The application will now terminate.");
            userInput.nextLine();
            terminate(library, userInput, -1);
        }
    }

    /**
     * The searchInterface method defines the general interface from which a user interacts with the search features of the program (as inspired by CSC8001).
     * To illustrate when issuing books searching for the user ensures the users validity. If the user is not a valid user in the system the relevant error handling procedures occur.
     * If the request is a valid request then the search interface needs to know where to pass information after validating user information.
     * This is defined by the searchMode parameter which informs the method what method to pass the validated information to (i.e. user or book).
     * This means of writing the application avoids code duplication: otherwise the same kind of validation procedures would take place in other methods i.e. the issueBooksInterface and returnBooksInterface methods.
     * The method allows users to search for a user and book by either User ID, Book ID, or details corresponding to either the User ID or Book ID (such as forename(s), surname(s), author(s) .etc.).
     * To illustrate it might be the case that the user knows the User ID of the person they would like to issue the book to, but not the Book ID (but the book name).
     * Search functions are performed by the relative search methods in the relevant instances of the library object (please see the Library Userdata or Library Bookdata methods for more information on this).
     * The task of the searchInterface is only to find ids, i.e. User ID's and Book ID's, and appropriate those ids to the relevant methods.
     *
     * @param library    refers to the library object to perform operations on used throughout the entire application.
     * @param userInput  is the scanner object required for taking user input instantiated in the main method and used throughout the entire application.
     * @param searchMode defines what function to pass information to once a search procedure has been completed.
     */
    private static void searchInterface(Library library, Scanner userInput, String searchMode) {
        ArrayList<User> userResults;
        ArrayList<Book> bookResults;
        boolean menuExit = false;
        String strInputUser = "";
        String strInputBook = "";
        String userSelection;
        int userId;
        int bookId;

        boolean searchByUserID = false;
        boolean searchByBookID = false;
        while (!menuExit) {
            // Present a menu to search details of users or books.
            System.out.print('\u000C');
            System.out.println();
            System.out.println("Library Interactive System: " + searchMode);
            System.out.println();
            System.out.println("Please select the details of the user, and book you would like to " + searchMode + " using the options below.");
            System.out.print("1. User Details: ");
            if (searchByUserID) {
                System.out.print("[User ID] " + strInputUser + "\n");
            } else System.out.print(strInputUser + "\n");
            System.out.print("2. Book Details: ");
            if (searchByBookID) {
                System.out.print("[Book ID] " + strInputBook + "\n");
            } else System.out.print((strInputBook + "\n"));
            System.out.println("3. Submit");
            System.out.println("4. Return to the previous menu.");
            System.out.println();
            System.out.print("> ");
            userSelection = userInput.nextLine();
            try {
                switch (userSelection) {
                    //If the user selection is for users present a submenu of ways to search.
                    case "1":
                        searchByUserID = false;
                        searchByBookID = false;
                        System.out.println();
                        System.out.println("Would you like to input the User ID or would you like to search by other details? Press enter to return to the previous selection screen.");
                        System.out.println("1. User ID.");
                        System.out.println("2. Other details (either user forename or user surname) This is a case-sensitive operation.");
                        System.out.println("u. Print a list of all users to the screen.");
                        System.out.println();
                        System.out.print("> ");
                        userSelection = userInput.nextLine();
                        switch (userSelection) {
                            case "1":
                                //Prevent user id's from being non-integers.
                                System.out.println();
                                System.out.println("Please enter the User ID you would like to search by...");
                                System.out.println();
                                System.out.print("> ");
                                userSelection = userInput.nextLine();
                                userId = Integer.valueOf(userSelection);
                                if (userId > 0) {
                                    strInputUser = userSelection;
                                    searchByUserID = true;
                                } else {
                                    System.out.println();
                                    System.out.println("A User ID must be an integer over 0.");
                                    System.out.println("Please press enter to return to the selection screen");
                                    userInput.nextLine();
                                    searchByUserID = false;
                                }
                                break;
                            case "2":
                                System.out.println();
                                System.out.println("Please enter additional details (either forename or surname) you would like to search by...");
                                System.out.println();
                                System.out.print("> ");
                                strInputUser = userInput.nextLine();
                                searchByUserID = false;
                                break;
                            case "u":
                                printAllUsers(library, userInput);

                            default:
                                break;
                        }
                        break;
                    //If the user's selection is for books then present a submenu of ways to search.
                    case "2":
                        searchByBookID = false;
                        System.out.println();
                        System.out.println("Would you like to input the Book ID or would you like to search by other details? Press enter to return to the previous selection screen.");
                        System.out.println("1. Book ID.");
                        System.out.println("2. Other details (either author forename, surname, or book title). This is a case-sensitive operation.");
                        System.out.println("b. Print a list of all books to the screen.");
                        System.out.println();
                        System.out.print("> ");
                        userSelection = userInput.nextLine();
                        switch (userSelection) {
                            case "1":
                                // Prevent book id's from being non-integers.
                                System.out.println();
                                System.out.println("Please enter the Book ID you would like to search by...");
                                System.out.println();
                                System.out.print("> ");
                                userSelection = userInput.nextLine();
                                bookId = Integer.valueOf(userSelection);
                                if (bookId > 0) {
                                    strInputBook = userSelection;
                                    searchByBookID = true;
                                } else {
                                    System.out.println();
                                    System.out.println("A Book ID must be an integer over 0.");
                                    System.out.println("Please press enter to return to the selection screen");
                                    userInput.nextLine();
                                    searchByBookID = false;
                                    break;
                                }
                                break;
                            case "2":
                                System.out.println();
                                System.out.println("Please enter additional details (Author, Title .etc.) you would like to search by...");
                                System.out.println();
                                System.out.print("> ");
                                strInputBook = userInput.nextLine();
                                searchByBookID = false;
                                break;
                            case "b":
                                printAllBooks(library, userInput);
                            default:
                                break;
                        }
                        break;
                    case "3":
                        //Once the user submits the relevant information begin a search procedure. This first searches for matches of users, then searches for matches of books.
                        //If either the input for the user or input for the book is blank, then return back to the selection screen.
                        if (strInputUser.equals("") | strInputBook.equals("")) {
                            System.out.println("\nPlease enter a selection.");
                            continue;
                        }

                        //Search users for matches.
                        try {
                            System.out.println("");
                            System.out.println("Searching users...");
                            //First using boolean true/false values identify if the user selected to search by a user's id. If this is true, then convert the value into an integer (it should not be passed as not convertible in the first place). If it is the system will throw a number format exception.
                            if (searchByUserID) {
                                userId = Integer.valueOf(strInputUser);
                                //If the user id refers to a value which is not in range of the number of users this may throw an IndexOutOfBounds exception when later iterating through the SortedArrayList since java collections allow for null elements. We can predict this and handle it early.
                                if (userId <= 0 | library.getUserdata().getNumberOfUsers() < userId) {
                                    throw new ArrayIndexOutOfBoundsException();
                                }
                                // IF the value is within range then attempt to return the userdata using that user id converted to a user index.
                                library.getUserdata().getUser(userId - 1);
                            } else {
                                //If we do not search by a user's id then use the searchUser's method to find the user.
                                userResults = library.getUserdata().searchUsers(strInputUser);
                                //If there is more than one result...
                                if (1 < userResults.size()) {
                                    System.out.println();
                                    System.out.println("Users have been found and are presented below...");
                                    System.out.println();
                                    System.out.println("User ID. Surname, Forename. Books Loaned.");
                                    System.out.println();
                                    // Print the users and the corresponding id determined by the number of times that user occurred in the database using the 'getUserIndexFromSearchOccurrence' method.
                                    int occurrence = 1;
                                    for (User user : userResults) {
                                        System.out.println(library.getUserdata().getUserIndexFromSearchOccurrence(strInputUser, occurrence) + 1 + ". " + user.getNameBySurname() + ". " + user.getBooksLoaned() + ".");
                                        occurrence++;
                                    }
                                    // Prompt the user to make a valid selection.
                                    System.out.println("\nPlease select a User ID in order to narrow the search. Pressing enter will return you to the selection screen.");
                                    System.out.println();
                                    System.out.print("> ");
                                    strInputUser = userInput.nextLine();
                                    userId = Integer.valueOf(strInputUser);
                                    //If the user id is an invalid selection (is not within the number of users in the system) throw an ArrayIndexOutOfBoundsException.
                                    if (userId <= 0 | library.getUserdata().getNumberOfUsers() < userId) {
                                        throw new ArrayIndexOutOfBoundsException();
                                    }
                                    //Otherwise if only one search result is returned then use that search result.
                                } else if (1 == userResults.size()) {
                                    userId = library.getUserdata().getUserIndexFromSearchOccurrence(strInputUser, 1) + 1;
                                    //This procedure exhausts the possible options we want a user to take so anything else is indicative that the search result is out of bounds.
                                } else {
                                    throw new ArrayIndexOutOfBoundsException();
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException | InputMismatchException | NumberFormatException e) {
                            //In the case an exception is raised, gracefully handle the exception and clear the relevant values used to search.
                            System.out.println("\nCould not find user " + strInputUser);
                            System.out.println("\nPlease press enter to return to the selection menu.");
                            strInputUser = "";
                            searchByUserID = false;
                            userInput.nextLine();
                            continue;
                        }
                        System.out.println();
                        System.out.println("Success.");

                        //Search books for matches [the procedure for this is the same as the procedure for users, but for books].
                        try {
                            System.out.println();
                            System.out.println("Searching books...");
                            if (searchByBookID) {
                                bookId = Integer.valueOf(strInputBook);
                                if (bookId <= 0 | library.getBookshelf().getNumberOfBooks() < bookId) {
                                    throw new ArrayIndexOutOfBoundsException();
                                }
                                library.getBookshelf().getBook(bookId - 1);
                            } else {
                                bookResults = library.getBookshelf().searchBooks(strInputBook);
                                if (1 < bookResults.size()) {
                                    System.out.println();
                                    System.out.println("Books have been found and are presented below...");
                                    System.out.println("Book ID. Author Surname, Author Forename(s). Title. On Loan[?].");
                                    System.out.println();
                                    int occurrence = 1;
                                    for (Book book : bookResults) {
                                        System.out.print(library.getBookshelf().getBookIndexFromSearchOccurrence(strInputBook, occurrence) + 1 + ". " + book.getAuthorSurname() + ", " + book.getAuthorForename() + "." + book.getTitle() + ".");
                                        if (book.getOnLoanTo() == -1) {
                                            System.out.print(" No.");
                                        } else {
                                            System.out.print(" Yes.");
                                        }
                                        System.out.println();
                                        occurrence++;
                                    }
                                    System.out.println("\nPlease select a Book ID in order to narrow the search. Pressing enter will return you to the selection screen.");
                                    System.out.println();
                                    System.out.print("> ");
                                    strInputBook = userInput.nextLine();
                                    bookId = Integer.valueOf(strInputBook);
                                    if (bookId <= 0 | library.getBookshelf().getNumberOfBooks() < bookId) {
                                        throw new ArrayIndexOutOfBoundsException();
                                    }
                                } else if (bookResults.size() == 1) {
                                    bookId = library.getBookshelf().getBookIndexFromSearchOccurrence(strInputBook, 1) + 1;
                                } else {
                                    throw new ArrayIndexOutOfBoundsException();
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException | InputMismatchException | NumberFormatException e) {
                            System.out.println("\nCould not find book " + strInputBook);
                            System.out.println("\nPlease press enter to return to the selection menu.");
                            strInputBook = "";
                            searchByBookID = false;
                            userInput.nextLine();
                            continue;
                        }

                        //If the operation is successful for each search procedure above, then the method will send the data to the method selected by the user in the user interface.
                        strInputUser = "";
                        strInputBook = "";
                        switch (searchMode) {
                            case "issue":
                                issueBooksInterface(library, userInput, bookId, userId);
                                menuExit = true;
                                break;
                            case "return":
                                returnBooksInterface(library, userInput, bookId, userId);
                                menuExit = true;
                                break;
                        }

                        break;
                    case "4":
                        menuExit = true;
                    default:
                        break;
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("\nPlease ensure you are entering a number and within the correct data range.");
                System.out.println("\nPlease press enter to return to the selection menu.");
                userInput.nextLine();
            }
        }
    }

    /**
     * The terminate method attempts to gracefully close the application by clearing arrays and other information.
     *
     * @param library   refers to the library object to attempt a graceful closing procedure with.
     * @param userInput refers to the scanner object required for taking user input instantiated in the main method and used throughout the entire application.
     * @param exitCode  is the exit code the application terminates by. A non-zero error code indicates an abnormal termination (for more details please visit https://docs.oracle.com/javase/7/docs/api/java/lang/System.html#exit(int)).
     */
    private static void terminate(Library library, Scanner userInput, int exitCode) {
        //Clear the objects and arrays before closing for a controlled and graceful termination of the application.
        library.clearBookshelf();
        library.clearUserdata();
        System.out.println("\n" +
                "Goodbye.");
        userInput.close();
        System.exit(exitCode);
    }


}

