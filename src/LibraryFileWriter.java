import java.io.IOException;
import java.io.PrintWriter;

/**
 * The LibraryFileWriter class is responsible for all operations which write to a file i.e. return notices.
 * The LibraryFileWriter class should always close files once it is finished with them.
 * <p>
 * The LibraryFileWriter class is made package private as it is intended for use only by instances of the Library Class (the file writer of the library belongs to the library).
 */
class LibraryFileWriter {

    /**
     * The writeReturnNotice method is responsible for creating an instance of the Print Writer class to write a return notice to a file.
     *
     * @param userString the user to address the return notice to.
     * @param bookString the book to inform the user to return.
     * @param directory  the directory to output the file to.
     * @return returns true when the operation has been completed.
     * @throws IOException throws an IOException if a general input/output error occurs (possibly permissions issues).
     */
    boolean writeReturnNotice(String userString, String bookString, String directory) throws IOException {
        PrintWriter outFile = new PrintWriter(directory);
        outFile.println("Dear " + userString+",");
        outFile.println();
        outFile.println();
        outFile.println("Please return the following item as soon as possible as it has been requested by another user of the library...");
        outFile.println();
        outFile.println(bookString);
        outFile.println();
        outFile.println();
        outFile.println("Best wishes,");
        outFile.println("The Library");
        outFile.close();
        return true;
    }
}
