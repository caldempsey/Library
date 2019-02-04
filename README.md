# Books
Exercise coding a book application

This task is to design and implement an object oriented program which is able to take an input of library data from a file of a specific format containing users and books, and allow a user (librarian) to return and issue books to those users.

It is specified the system must take user input before displaying that interactive system. This functionality has been extended to default directories for different operating systems. In the case those default directories cannot be detected a user is able to specify directories they are familiar with (forward error handling). Further details on how the input file is read is documented in the LibraryFileReader class.

It is specified the system must contain at least the commands f (finish running the program), b (to display on the screen the information about all the books in the library), u (to display on the screen the information about all the users), i (to update the stored data when a book is issued to a user), and r (to update the stored data when a user returns a book to the library).

It is specified to include validation procedures to validate (and in conjunction to this confirm) to the user that the book they wish to either issue or return to specific users is in fact the correct book or user.

It is specified that all User and Book objects are stored within a SortedArrayList object which extends the ArrayList object such that it is able to lexicographically order books and users of the array. This is achieved by implementing the Comparable interface in those objects and using the compareTo method to define an ascending sorting procedure (see SortedArrayList and User or Book objects for further details).

It is specified there should exist relevant procedures. To illustrate these include displaying an appropriate messages on the screen and outputting notices asking users to return books.
