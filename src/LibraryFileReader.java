import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * The LibraryFileReader class is responsible for all operations which read a file to instances of a library object (insofar, instances of the LibraryUserData and LibraryBookshelf classes).
 * The LibraryFileReader class imports information of a given kind (defined in importModes), primarily to be processed by the importDataFromFile method.
 * The LibraryFileReader class should always close files once it is finished with them.
 * <p>
 * The LibraryFileReader class is made package private as it is intended for use only by instances of the Library Class (the file reader of the library belongs to the library).
 */
class LibraryFileReader {
    private final String[] importModes = {"Books", "Users"};

    /**
     * The importDataFromFile method is the primary file import procedure designed with developers in-mind: in such a way that it makes it easy to change the details of the kinds of items being imported to the Library.
     * <p>
     * If you would like to only add another object to be read by the text file then you simply need to add the name of the object to the end of the array 'importModes' and specify the rules you want that import to behave by within the switch case that iterates over those import modes,, then pass the parameters you need.
     * <p>
     * If you would like to understand the logic more deeply there are three things to keep in mind...
     * The outer for loop which ranges over the import modes declared in the array of strings 'import modes'.
     * The inner while loop which evaluates for an integer data-type within the text file that indicates the next set of lines.
     * The 'importModes' switch case which iterates over the different import modes defined in the array 'import modes' when the outer for loop has finished iteration.
     * <p>
     * To illustrate a sequence might process in this order...
     * The outer for loop which first reads for items of the kind 'books'.
     * The inner while loop which obtains the value for the number of lines to parse and continues until the next integer has been found.
     * While the next integer has not been found the while loop iterates through all of the lines.
     * For each iteration of the while loop the switch case of import modes determines which rules evaluate the lines (which obtains its value from the inner for loop, items of the kind 'books').
     * This process continues until the inner while loop has found the next integer or has evaluated all of the lines for each set of rules.
     * <p>
     * The sequence checks for consistency by ensuring the actual line count for each item of a kind is equal to the specified line count for each iteration of the while loop, that is if there are 5 books with 2 lines then only after processing the number of lines for books is it necessary to increment this value by 1.
     * The sequence closes the file and returns error codes
     * <p>
     * The benefit of this approach is all a developer needs to do in order to define an new item is specify what they want the application to do when iterating over items of a kind 'DVD's', instead of making changes to the logic they otherwise shouldn't have to make (see comment blocks in code).
     * The caveat of this approach is the complexity of the sequence.
     *
     * @param filepath  specifies the directory filepath.
     * @param userdata  specifies the userdata object required to insert users to.
     * @param bookshelf specifies the bookshelf object required to inset books to.
     * @return returns 1 if the file has been successfully imported returns -1 if there is an identifiable error with the number specified by the file (i.e. if the input file contains 0 books), returns -2 if there is a syntax error with the file.
     * @throws IOException is thrown for input output issues including those associated with a FileNotFound exception (interrupted file operations .etc.).
     *                     <p>
     *                     Methods of the LibraryFileReader class are made package private as they are only intended to be used by instances of the Library class.
     */
    byte importDataFromFile(String filepath, LibraryUserdata userdata, LibraryBookshelf bookshelf) throws IOException {

        //Variables required for importDataFromFile to operate//
        String line;
        String title;
        boolean fileProcessed = false;
        int statedLineCount = 0;
        double actualLineCount = 0;


        //Variables for evaluating lines of type Books.
        String authorForename;
        String authorSurname;

        //Variables for evaluating lines of type Users.
        String[] name;
        FileReader file = new FileReader(filepath);
        Scanner data = new Scanner(file);
        boolean readingLines = false;

        //Add variables you define here.


        //Try block for data processing.
        try {
            // For each importMode defined in the array importModes (top of class).
            for (String mode : importModes) {
                //While the data from the file has another line and the stated line count is equal to or greater than the actual line count.
                while (data.hasNext() && actualLineCount <= statedLineCount) {
                    //Process the lines, set file processed to true (indicating we have started to process the file).
                    fileProcessed = true;
                    //Read each line as a string 'line'.
                    line = data.nextLine();
                    if (line.equals("")) {
                        file.close();
                        return -2;
                    }
                    //If we are reading lines and the line is not a long, continue
                    if (!readingLines) {
                        if (isIntOver0(line)) {
                            statedLineCount = Integer.parseInt(line);
                            actualLineCount = 0;
                            readingLines = true;
                            // If we are not reading lines and the first line isn't a long, continue.
                            continue;
                        } else {
                            // If we are not reading the lines and we have reached a line such that it isn't an integer, close the file and return a syntax issue.
                            file.close();
                            return -2;
                        }
                    } else {
                        //If we are reading the lines and the line is an integer over 0, then check if the actual line count is equal to the stated line count. If we are reading lines and the line is not an integer over 0 then continue reading lines.
                        if (isIntOver0(line)) {
                            if (actualLineCount == statedLineCount) {
                                statedLineCount = Integer.parseInt(line);
                                actualLineCount = 0;
                                //If the actual line count is equal to the stated line count break the loop.
                                break;
                            } else {
                                //If the actual line count is not equal to the stated line count then terminate the operation.
                                file.close();
                                return -2;
                            }
                        }
                    }
                    switch (mode) {
                        case "Books":
                            //Logic for processing a single book.
                            title = line;
                            line = data.nextLine();
                            authorForename = line.substring(0, line.lastIndexOf(" "));
                            authorSurname = line.substring(line.lastIndexOf(" ") + 1);
                            if (authorSurname.equals("") | authorForename.equals("")) {
                                file.close();
                                return -2;
                            }
                            importBookObject(title, authorForename, authorSurname, bookshelf);
                            //Increment the number of books **added** by 1.
                            actualLineCount++;
                            break;

                        case "Users":
                            //Logic for processing a single user.
                            name = line.split("\\s+");
                            if (name.length != 2) {
                                file.close();
                                return -2;
                            } else {
                                importUserObject(name[0], name[1], userdata);
                                //Increment the number of users **added** by 1.
                                actualLineCount++;
                            }
                            break;

                        //Add your set of rules here i.e.
                        //
                        //case "DVD":
                        //Using the line 'line'.
                        //Perform evaluation for lines of type DVD.
                        //importDVDObject();
                        //actualLineCount++; [Required to indicate how many lines you have processed for each iteration of readMode.
                    }
                }
            }

            //If for the last iteration of read modes, if the actual line count is not equal to the line count stated, return that there is a syntax error.
            if (!(actualLineCount == statedLineCount)) {
                file.close();
                return -2;
            }
            //If the file has not been processed (the file was blank), return there is a syntax error.
            if (!fileProcessed) {
                file.close();
                return -2;
            }
            //Catch IO exceptions and return these gracefully.
        } catch (IndexOutOfBoundsException e) {
            file.close();
            return -2;

            //Catch a NumberFormatException, if the number of lines stated exceeds the maximum value of an integer (in which case a SortedArrayList will fail-over at some point since it extends the ArrayList class whose maximum number of entries is equal to the maximum value of an integer).
        } catch (NumberFormatException e) {
            file.close();
            return -1;
        }

        //If we get this far the operation was a success.
        return 1;
    }


    /**
     * Used to import a new Book object to the library's bookshelf during a file read procedure.
     *
     * @param title          specifies the title of the book.
     * @param authorForename specifies the author's forename of the book.
     * @param authorSurname  specifies the author's
     * @param bookshelf      the bookshelf object required to perform operations on books in the library.
     */
    private void importBookObject(String title, String authorForename, String authorSurname, LibraryBookshelf bookshelf) {
        Book book = new Book(title, authorForename, authorSurname);
        bookshelf.addBook(book);
    }


    /**
     * Used to import a new User object to the library's userdata during a file read procedure.
     *
     * @param forename specifies the forename of the user.
     * @param surname  specifies the surname of the user.
     * @param userdata the userdata object required to perform operations on users of the library.
     */
    private void importUserObject(String forename, String surname, LibraryUserdata userdata) {
        User user = new User(forename, surname);
        userdata.addUser(user);
    }

    /**
     * Used to validate if an integer object is over the value of 0.
     *
     * @param input the input string of the integer to check.
     */
    private boolean isIntOver0(String input) {
        try {
            final int l = Integer.parseInt(input);
            return (l > 0);
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
