//package com.digitalpersona.onetouch.ui.swing.sample.Enrollment;
package pat;
import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.processing.*;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class EnrollmentForm extends CaptureForm
{
	private DPFPEnrollment enroller = DPFPGlobal.getEnrollmentFactory().createEnrollment();
	private VotersRegistrationForm callerObj;
	EnrollmentForm(Frame owner) {
		super(owner);
                this.callerObj = (VotersRegistrationForm)owner;
	}
	
	@Override protected void init()
	{
		super.init();
		this.setTitle("Fingerprint Enrollment");
		updateStatus();
	}

	@Override protected void process(DPFPSample sample) {
		super.process(sample);
		// Process the sample and create a feature set for the enrollment purpose.
		DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);


		// Check quality of the sample and add to enroller if it's good
		if (features != null) try
		{
			makeReport("The fingerprint feature set was created.");
			enroller.addFeatures(features);		// Add feature set to template.
		}
		catch (DPFPImageQualityException ex) { }
		finally {
			updateStatus();

                        DPFPTemplate template = enroller.getTemplate();
                        this.callerObj.setFingerCapturedTemplate(template,this.picture);
                        DPFPTemplateStatus templateStatus = enroller.getTemplateStatus();
                        
			// Check if template has been created.
			switch(templateStatus)
			{
				case TEMPLATE_STATUS_READY:	// report success and stop capturing
					stop();
                                        this.setVisible(false);
					//((MainMenuForm)getOwner()).setTemplate(template);//enroller.getTemplate());
					///setPrompt("Click Close, and then click Fingerprint Verification.");
                                        //this.setVisible(false);
					break;

                                case TEMPLATE_STATUS_INSUFFICIENT:
                                    new Alert("Template insufficient, Please place same finger on sensor again.");
                                    break;
                                case TEMPLATE_STATUS_UNKNOWN:
                                    new Alert("Template unknown");
                                    break;

				case TEMPLATE_STATUS_FAILED:	// report failure and restart capturing
					enroller.clear();
					stop();
					updateStatus();
					((MainMenuForm)getOwner()).setTemplate(null);
					JOptionPane.showMessageDialog(EnrollmentForm.this, "The fingerprint template is not valid. Repeat fingerprint enrollment.", "Fingerprint Enrollment", JOptionPane.ERROR_MESSAGE);
					start();
					break;
			}
		}
	}
	
	private void updateStatus()
	{
		// Show number of samples needed.
		setStatus(String.format("Fingerprint samples needed: %1$s", enroller.getFeaturesNeeded()));
	}
	
}
