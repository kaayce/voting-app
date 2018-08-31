/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pat;

import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.sql.ResultSet;

public class verificationClass {

    private DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();
    private DataClass dbObject;

    public verificationClass()
    {
        this.dbObject = DataClass.getInstance();
    }

    public int searchTemplate(DPFPFeatureSet features)
    {
        DPFPTemplate existingTemplate1 = null;
        DPFPTemplate existingTemplate2 = null;
        int id = 0;

        ResultSet listOfVoters = this.dbObject.fetchrows("select * from tbl_voters");

        try{
            while(listOfVoters.next())
            {
                id = listOfVoters.getInt(1);
                int numberOfFiles = 0;
                {

                    // Check quality of the existingTemplate and start verification if it's good
                    if (features != null)
                    {
                        try{
                            existingTemplate1 = this.dbObject.getTemplateFromDatabase(id,"finger_template1");
                        }
                        catch(Exception e){

                        }

                        try{
                            existingTemplate2 = this.dbObject.getTemplateFromDatabase(id,"finger_template2");
                        }
                        catch(Exception e){

                        }
                        if(existingTemplate1!=null){
                            // Compare the feature set with our template
                            DPFPVerificationResult result1 = verificator.verify(features, existingTemplate1);


                            result1.getFalseAcceptRate();

                            if (result1.isVerified())
                            {
                                return id;
                            }
                        }

                        if (existingTemplate2!=null){
                            // Compare the feature set with our template
                            DPFPVerificationResult result2 = verificator.verify(features, existingTemplate2);


                            result2.getFalseAcceptRate();

                            if (result2.isVerified())
                            {
                                return id;
                            }
                                    //makeReport("The fingerprint was VERIFIED.");
                            //else
                                        //makeReport("The fingerprint was NOT VERIFIED.");
                        }

                        

                        


                    }
                    else
                    {
                        return 0;
                    }

                }
            }
        }
        catch(Exception ex)
        {

        }
        
        return 0;
    }
}
