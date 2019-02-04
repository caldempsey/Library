import java.util.ArrayList;

/**
 * The SortedArrayList class extends the ArrayList class such that it sorts objects such that they implement the Comparable interface.
 * A SortedArrayList object therefore is able to use any of the methods of the ArrayList class (and overrides at least one of these methods to perform sorting).
 * The SortedArrayList performs sorting operations using the compareTo method on objects from the Comparable interface.
 * As a result, those objects must implement the Comparable interface in order to be contained by a SortedArrayList.
 *
 * @param <E> Param E refers to the elements of a kind such that it can be contained within an ArrayList object.
 *            Similarly Param E also refers to objects of a kind such that it can be extended from a Comparable object. All objects of this kind must implement the Comparable interface.
 */
class SortedArrayList<E extends Comparable<E>> extends ArrayList<E> {

    /**
     * The add method overrides the add method for ArrayList objects and adds them in an order using the following insertion sort algorithm.
     * <p>
     * The method takes in an element of a kind E (such that it implements the Comparable interface) and checks if the SortedArrayList object is empty using methods inherited from the ArrayList class.
     * If the SortedArrayList is empty then it performs an add operation defined by the ArrayList class (since this is what we override we cannot use the method of the SortedArrayList class without causing recursive behaviour).
     * If it is not the case that the SortedArrayList is empty, then for each item in the SortedArrayList, the compareTo method is called comparing the element to be added to the SortedArrayList with each element contained within the ArrayList.
     * If the value of the compareTo method is smaller than zero (semantically means 'is smaller'), then add that string to that position (shifts the index of all other items up by 1).
     * If the value of the compareTo method is bigger than zero (semantically means 'is bigger'), then perform an add method such that an item is added to the end of the ArrayList.
     * If the value of the compareTo method is equal, then the order between those two elements is not necessarily relevant (since the comparison between the two elements is semantically equal), the operation should add the element after those elements.
     *
     * @param e the object of an element of a kind E to add to the ArrayList.
     * @return returns true if the operation was successful.
     */
    @Override
    public boolean add(E e) {

        if (this.isEmpty()) {
            super.add(e);
            return true;
        }
        for (int i = 0; i < this.size(); i++) {
            if (e.compareTo(this.get(i)) < 0) {
                super.add(i, e);
                return true;
            }
        }
        super.add(e);
        return true;
    }
}
