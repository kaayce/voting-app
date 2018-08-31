//package com.digitalpersona.onetouch.ui.swing.sample.Enrollment;
package pat;

import java.io.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.digitalpersona.onetouch.*;

public class MainMenuForm extends JFrame
{
	public static String TEMPLATE_PROPERTY = "template";
	private DPFPTemplate template;
        public static DataClass DataObj;

	public class TemplateFileFilter extends javax.swing.filechooser.FileFilter {
		@Override public boolean accept(File f) {
			return f.getName().endsWith(".fpt");
		}
		@Override public String getDescription() {
			return "Fingerprint Template File (*.fpt)";
		}
	}
        
	MainMenuForm() {
            this.DataObj = DataClass.getInstance();
            this.DataObj.establishConnection();
        setState(Frame.NORMAL);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Biometric Electronic Voting System");
		setResizable(false);
                
                final JButton registerCandidate = new JButton("Register Candidate");
        registerCandidate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onRegisterCandidate(); }});

		final JButton votersRegister = new JButton("Register Voters");
        votersRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onVotersRegistration(); }});
		
                final JButton registerUsers = new JButton("Register Admin Users");
        registerUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onRegisterUsers(); }});

		//final JButton vote = new JButton("Fingerprint Verification");
                final JButton vote = new JButton("Voting Page");
        vote.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onVote(); }});

		//final JButton save = new JButton("Save Fingerprint Template");
                /*final JButton save = new JButton("Save Fingerprint Template");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onSave(); }});
                 *
                 */
                
                final JButton btnSetupVote = new JButton("Setup Voting");
        btnSetupVote.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onSetupVote(); }});

		final JButton btnLogout = new JButton("Log Out");
        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onLogout(); }});

                final JButton btnScoreReport = new JButton("Voting Scores");
        btnScoreReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onScoreReport(); }});

		final JButton quit = new JButton("Close");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { DataClass.getInstance().closeDbConnection(); System.exit(0); }});
		
		this.addPropertyChangeListener(TEMPLATE_PROPERTY, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				//vote.setEnabled(template != null);
				//save.setEnabled(template != null);
				if (evt.getNewValue() == evt.getOldValue()) return;
				if (template != null)
					JOptionPane.showMessageDialog(MainMenuForm.this, "The fingerprint template is ready for fingerprint verification.", "Fingerprint Enrollment", JOptionPane.INFORMATION_MESSAGE);
			}
		});
			
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(4, 1, 0, 5));
		center.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));
                
                center.add(registerCandidate);
		center.add(votersRegister);
                center.add(registerUsers);
                center.add(btnSetupVote);
		center.add(vote);
		//center.add(save);
		center.add(btnLogout);
		center.add(btnScoreReport);

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		bottom.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		bottom.add(quit);

		setLayout(new BorderLayout());
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.PAGE_END);
		
		pack();
		setSize((int)(getSize().width*1.6), getSize().height);
        setLocationRelativeTo(null);
		setTemplate(null);
		setVisible(true);
                onLogout();
	}

	private void onRegisterCandidate() {
		CandidateRegistrationForm form = new CandidateRegistrationForm(this);
		form.setVisible(true);
                //form.show();
                //this.hide();
	}

	private void onVotersRegistration() {
                VotersRegistrationForm form = new VotersRegistrationForm(this);
                form.setVisible(true);
		//EnrollmentForm form = new EnrollmentForm(this);
		//form.setVisible(true);
	}

        private void onSetupVote() {
            VoteSetupForm form = new VoteSetupForm(this);
            form.setVisible(true);
        }

        private void onLogout() {
		LoginForm form = new LoginForm(this);
		form.setVisible(true);
                this.hide();
	}

        private void onRegisterUsers()
        {
            UsersForm form = new UsersForm(this);
            form.setVisible(true);
        }

	private void onVote() {
            VotingForm form = new VotingForm(this);
            form.setVisible(true);
	}

        private void onScoreReport() {
            ReportForm form = new ReportForm(this);
            form.setVisible(true);
        }

	public DPFPTemplate getTemplate() {
		return template;
	}
	public void setTemplate(DPFPTemplate template) {
		DPFPTemplate old = this.template;
		this.template = template;
		firePropertyChange(TEMPLATE_PROPERTY, old, template);
	}
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainMenuForm();
            }
        });
    }

}
