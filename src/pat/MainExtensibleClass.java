/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pat;

import java.sql.*;

public class MainExtensibleClass extends javax.swing.JFrame
{

    public DataClass dbConnection;
    public MainMenuForm parent;

    //method to load the party groups
    protected ResultSet loadPartyGroups()
    {
        ResultSet res = this.dbConnection.fetchrows("select * from tbl_group where status='1'");
        return res;
    }

    protected void getConnection()
    {
        this.dbConnection = DataClass.getInstance();
    }

    public String getGroupName(String group_id)
    {
        try{
            ResultSet res = this.dbConnection.fetchrows("select * from tbl_group where ID="+group_id);
            if(res.next())
            {
                return res.getString("group_name").toString();
            }
            else
            {
                return null;
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public String getCategoryName(String category_id)
    {
        try{
            ResultSet res = this.dbConnection.fetchrows("select * from tbl_category where ID="+category_id);
            if(res.next())
            {
                return res.getString("category_name").toString();
            }
            else
            {
                return null;
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public String getCandidateName(String candidate_id)
    {
        try{
            ResultSet res = this.dbConnection.fetchrows("select * from tbl_candidate where ID="+candidate_id);
            if(res.next())
            {
                return res.getString("lname").toString()+" "+res.getString("fname").toString()+" ("+res.getString("title").toString()+") "+getGroupName(res.getString("group_id").toString());
            }
            else
            {
                return null;
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }


    
    public String getStatusName(String status_id)
    {
        if(status_id.equals("1"))
        {
           return "Active" ;
        }
         else
        {
             return "In-Active" ;
         }
    }
}
