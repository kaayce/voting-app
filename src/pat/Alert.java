/*
 * @author 
 */

//package name
package pat;

//imported libraries
import javax.swing.*;

//message box class
public class Alert {
    //class constructor
    Alert(String message){
        //set message to display and show message box
        JOptionPane.showMessageDialog(null, message, "Biometric Electronic Voting",1);
    }
    //confirm message constructor
    int conf(String message,int a){
        //set message to display and show message box
        return JOptionPane.showConfirmDialog(null, message,"Biometric Electronic Voting",1);
    }
}
