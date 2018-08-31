/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pat;

import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import java.lang.*;
import java.sql.*;
import java.util.*;
import java.io.*;


public class DataClass {

    private static final DataClass instance = new DataClass();
    private String db_name = "bev_db.mdb";
    private String db_username = "root";
    private String db_password = "";
    private Connection db_connection;
    protected String dbpath;
    protected Statement globalQ;
    protected ResultSet globalRs;
    protected String dbPath;
    protected String AppPath;
    protected boolean dbstat=false;

    //connect to the database
    public void establishConnection()
    {
        try{
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            db_connection = DriverManager.getConnection(dbPath, "","");
            dbstat=true;
        }
        catch(Exception ex)
        {

        }
        String admin = "";
        try
        {
            String qry = "select * from tbl_users where username='admin'";
            ResultSet res=this.fetchrows(qry);
            //int test = res.getFetchSize();
            boolean test = res.next();
            //admin=res.getString("ID");

            if(!test)
            {
                this.executeQueryNoDataReturn("insert into tbl_users (username,password,status,created_date,last_login) values ('admin','password',1,'10/02/2014 17:55','')");
                new Alert(" Take Note of This Information of " +
                        " Your Admin account details. " +
                        " Your User Name is 'Admin' and " +
                        " Your Password is 'password'");
            }
        }
        catch(Exception ex)
        {
            
        }
    }

    public void closeDbConnection()
    {
        try{
            //disconnect from database
            dbstat=false;
            db_connection.close();
        }
        catch(Exception ex)
        {
            
        }

    }

    //constructor
    private DataClass() {

        this.dbpath="";
        //get application directory
        this.AppPath=System.getProperty("user.dir");

    	String newpath="";
        this.dbPath= "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";

        //reconstruct application path
        for(int i=0;i<this.AppPath.length();i++){
            char c = this.AppPath.charAt( i );
            int j = (int) c;
            if (j==92){
                newpath+="/";
            }else{
                newpath+=c;
            }
        }

        //get database path
        this.AppPath=newpath;
        this.dbpath=this.AppPath+"/"+this.db_name;
        //get database driver
        this.dbPath+= dbpath.trim() + ";DriverID=22;READONLY=true}"; // add on to the end
    }

    //function to perform insert or update table
    public boolean insertupdate(ArrayList arrdata,String tablename,String column) throws Exception{
            try{
                    insertData(arrdata,tablename);//insert record
                    return true;
            }
            catch(Exception ex){
                //die($ex);
                try{
                        updatetable(arrdata,tablename,column);
                        return true;
                }catch(Exception ex2){
                        throw new Exception("Cannot Perform operation: "+ ex2.toString());
                }
            }
    }

    //function to perform insert or update table
    public boolean insertignore(ArrayList arrdata,String tablename,String column) throws Exception{
        try{
            String qry = "select * from "+tablename+" where "+column;
            ResultSet res = this.fetchrows(qry);//check if row exist
            if (res==null){
                    
            }
            else if (res.next())
            {
                return false;
            }
            insertData(arrdata,tablename);//insert record
            return true;
        }
        catch(Exception ex){
                //return 'Perform operation '. $ex2;
            throw new Exception("Perform operation "+ ex.toString());
        }
    }

    //function to get the last inserted ID
    public int get_Last_Inserted_ID(String tablename) throws Exception{
            String qry = "SELECT MAX(ID) FROM "+tablename;
            ResultSet id = fetchrows(qry);

            if(id.getFetchSize()>0)
            {
                id.next();
                return id.getInt(1);
            }
            return 0;
    }

	//function to insert a fresh data into a specified table
    public boolean insertData(ArrayList arrdata,String tablename) throws Exception{
            //this.db_connection;
            String columns = "";
            String values = "";
            //lets check if there is something to insert
            if(arrdata.size()<1)
            {
                return false;
            }

            for(int i = 0;i<arrdata.size();i++){
                ListPairItem temp = (ListPairItem<String>)arrdata.get(i);
                 columns+=" "+temp.getText()+", ";
                 if(temp.getItem().getClass()==temp.getClass())
                 {
                    values+="'"+((ListPairItem) temp.getItem()).getItem()+"', ";
                 }
                else{
                    values+="'"+temp.getItem()+"', ";
                }
            }

            values+="END";
            columns+="END";
            values = values.replace(", END"," ");
            columns = columns.replace(", END"," ");

            String qry = "INSERT INTO "+tablename+" ("+columns+") VALUES ("+values+")";
            return this.executeQueryNoDataReturn(qry);
    }

    //function to update table in the database
    public boolean updatetable(ArrayList arrdata,String tablename,String column) throws Exception{
            String middle = "";
            String columns = "";
            String values = "";

            //lets check if there is something to insert
            if(arrdata.size()<1)
            {
                return false;
            }

            for(int i = 0;i<arrdata.size();i++){
                ListPairItem<String> temp = (ListPairItem<String>)arrdata.get(i);
                 columns=" "+temp.getText()+" ";
                 values=temp.getItem().toString();
                 middle+=columns+" ='"+values+"', ";
            }

            middle+="END";
            middle = middle.replace(", END"," ");

            String qry = "UPDATE "+tablename+" SET "+middle+" where ("+column+")";// die($qry);
            return this.executeQueryNoDataReturn(qry);
    }

    //function to update table in the database
    public boolean deletedata(String tablename,String column) throws Exception{
        try{
            String qry = "delete FROM "+ tablename+" where ("+column+")";
            boolean return_val= this.executeQueryNoDataReturn(qry);
            return return_val;
        }
        catch(Exception ex){
            throw new Exception("Cannot delete data: "+ex.toString());
        }
    }

    //function to fetch records from the database
    public ResultSet fetchrows(String qry)
    {
            //this.db_connection;
            ResultSet return_val = null;

            //ResultSet fetched_values=null;
        try{
            globalQ = this.db_connection.createStatement();
            globalQ.executeQuery(qry);
            return_val=globalQ.getResultSet();
            /*fetched_values.next();
            ListPairItem <Object> temp;
            fetched_values.getCursorName();
            while(fetched_values.next()){
                //execute query
                ListPairItem <Object> temp;
                fetched_values.getCursorName();
            }*/
            return return_val;
        }catch(Exception e){
            System.out.println(e.toString());
            return return_val;
        }
    }

    public DPFPTemplate getTemplateFromDatabase(int id, String fingerPrintColumnName) throws Exception
    {
        //lets prepare the finger template
        //PreparedStatement updateStmt = this.db_connection.prepareStatement("UPDATE tbl_voters "
        //        + " set "+fingerPrintColumnName+" = (?) where (id='"+id+"')");
        DPFPTemplate template = null;
        //int tempLength = temp.length;

        //byte [] tempR = rightTemplate.serialize();
        //long tempRLength = tempR.length;

        //lets update the column with finger template
        //updateStmt.setBinaryStream(1,new ByteArrayInputStream(temp),tempLength);

        //updateStmt.setBinaryStream(1,new ByteArrayInputStream(tempL),tempLLength);
        //updateStmt.setBinaryStream(2,new ByteArrayInputStream(tempR),tempRLength);

        //updateStmt.executeUpdate();

        //temporary solution to get the template from a file storage
        File file = new File(this.AppPath+"/TemplateStorage/"+fingerPrintColumnName+id+".tpt");

        byte[] data = this.readFile(file);

        template = DPFPGlobal.getTemplateFactory().createTemplate(data);
                //.getEnrollmentFactory().createEnrollment().getTemplate();

        //template.deserialize(data);

        return template;
    }

    public byte[] readFile(File file) throws Exception
    {
        RandomAccessFile f = new RandomAccessFile(file, "r");
        byte[] data;
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength) throw new IOException("File size >= 2 GB");

            // Read file and return data
            data = new byte[length];
            f.readFully(data);
        }
        finally {
            f.close();
        }
        return data;
    }

    public boolean addTemplateToDatabase(int id, DPFPTemplate template,String fingerPrintColumnName) throws Exception
    {
        //lets prepare the finger template
        PreparedStatement updateStmt = this.db_connection.prepareStatement("UPDATE tbl_voters "
                + " set "+fingerPrintColumnName+" = (?) where (id='"+id+"')");

        byte [] temp = template.serialize();
        int tempLength = temp.length;

        //byte [] tempR = rightTemplate.serialize();
        //long tempRLength = tempR.length;
        
        //lets update the column with finger template
        //updateStmt.setBinaryStream(1,new ByteArrayInputStream(temp),tempLength);

        //updateStmt.setBinaryStream(1,new ByteArrayInputStream(tempL),tempLLength);
        //updateStmt.setBinaryStream(2,new ByteArrayInputStream(tempR),tempRLength);

        //updateStmt.executeUpdate();

        //temporary solution to add the template to a file storage

        File tempFile = new File(this.AppPath+"/TemplateStorage/");
        tempFile.mkdirs();
        
        File file = new File(this.AppPath+"/TemplateStorage/"+fingerPrintColumnName+id+".tpt");
        
        FileOutputStream fos = new FileOutputStream(file);
        //ByteArrayOutputStream out = new ByteArrayOutputStream(fos);
        fos.write(temp, 0, tempLength);

        return true;
    }

    public boolean executeQueryNoDataReturn(String qry) throws Exception
    {
        this.globalQ = this.db_connection.createStatement();
        this.globalQ.execute(qry);
        this.globalQ.close();
        return true;
    }

    //get the instance for the class
    public static DataClass getInstance() {
        return instance;
    }

}
