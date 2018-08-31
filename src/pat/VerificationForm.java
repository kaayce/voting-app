//package com.digitalpersona.onetouch.ui.swing.sample.Enrollment;
package pat;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.verification.*;
import java.awt.*;
import java.sql.ResultSet;

public class VerificationForm extends CaptureForm
{
	private DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();
        private VotingForm callerObj;
        private DataClass dbObject;
	
	VerificationForm(Frame owner) {
		super(owner);
               this.callerObj =  (VotingForm)owner;
               dbObject = DataClass.getInstance();
	}
	
	@Override protected void init()
	{
		super.init();
		this.setTitle("Fingerprint Verification Form");
		updateStatus(0);
	}

	@Override protected void process(DPFPSample sample) {
		super.process(sample);

		// Process the sample and create a feature set for the enrollment purpose.
		DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

                verificationClass VeriFtest = new verificationClass();
                int id = VeriFtest.searchTemplate(features);
                String voter_name = null;
                try{
                    ResultSet rsV = this.dbObject.fetchrows("select * from tbl_voters where ID = "+id+"");
                    rsV.next();
                    voter_name = rsV.getString("title")+
                                " "+rsV.getString("lname")+" "+rsV.getString("fname");
                }catch(Exception ex){}
                
                //lets check if candidate has registered before
                if(id>0)
                {
                    //user has registered before
                    boolean votedBefore = false;
                    //lets check if user has voted at all
                    
                    try{
                        ResultSet rs = this.dbObject.fetchrows("select count(*), voter_id from tbl_voting where ("
                                + "category_id = 1 or category_id = 2 or category_id = 3 or category_id = 4 or "
                                + "category_id = 5) group by voter_id");
                        if(rs.next())
                        {
                            String voter_id = rs.getString("voter_id");
                            if(voter_id.equals(id+"") && rs.getInt(1)>4)
                            {
                                votedBefore = true;
                            }
                        }
                    }
                    catch(Exception ex)
                    {

                    }
                    
                    if(votedBefore)
                    {
                        this.callerObj.setEnableVote(false);
                        this.callerObj.setVoterDetail(0);
                        //lets notify the user for casting his vote
                        new Alert("You have already casted your votes, Please wait for the results");
                    }
                    else
                    {
                        this.setVisible(false);
                        this.callerObj.setVoterDetail(id,voter_name);
                        this.callerObj.setEnableVote(true);
                        this.callerObj.show();
                        new Alert(((voter_name==null)?"":voter_name)+" You have been identified, Please proceed to cast your vote.");
                    }
                }
                else
                {
                    this.callerObj.setVoterDetail(0);
                    this.callerObj.setEnableVote(false);
                    //lets notify the user for registration
                    new Alert("You have not registered to be elligible to vote, Please consult the administrator for registeration details");

                }

                if(true)
                {
                    return;
                }

		// Check quality of the sample and start verification if it's good
		if (features != null)
		{
			// Compare the feature set with our template
			DPFPVerificationResult result = 
				verificator.verify(features, ((MainMenuForm)getOwner()).getTemplate());
			updateStatus(result.getFalseAcceptRate());
			if (result.isVerified())
				makeReport("The fingerprint was VERIFIED.");
			else
				makeReport("The fingerprint was NOT VERIFIED.");
		}
	}
	
	private void updateStatus(int FAR)
	{
		// Show "False accept rate" value
		setStatus(String.format("False Accept Rate (FAR) = %1$s", FAR));
	}

}
