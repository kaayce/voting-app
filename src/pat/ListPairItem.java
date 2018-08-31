/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pat;

/**
 *
 * @author Patrick
 */
//class to handle the pair values for list items and spinners
public class ListPairItem <T>{

	//set the class variables to String and Item
    private String text;
    private T item;
    //constructor
    public ListPairItem(String text, T item) {
            this.text = text;
            this.item = item;
    }
    //to get the text
    public String getText() {
        return text;
    }
    //to get the item
    public T getItem() {
        return item;
    }

    @Override
    public String toString() {
        return getText();
    }
}
