import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Register extends JPanel {
	private int width, height;
	private JPanel register;
	
	//register constructor
	public Register(double width, double height) {
		this.width = (int) (width / 1.8);
		this.height = (int) (height / 1.5);
		this.add(createRegister());
	}
	
	//the form for registering a patient
	private JPanel createRegister() {
		register = new JPanel();
		JLabel statusLabel = new JLabel("Book info:", JLabel.CENTER);
		JPanel labelPanel = createCentered(statusLabel);
		JPanel gridL = new JPanel(new GridLayout(11, 2));
		gridL.setPreferredSize(new Dimension(width, 352));
		JLabel title = new JLabel("Title");
		JTextField titleV = new JTextField();
		JLabel surname = new JLabel("Surname");
		JTextField surnameV = new JTextField();
		JLabel forename = new JLabel("Forename");
		JTextField forenameV = new JTextField();
		JLabel bdate = new JLabel("Birthdate(dddd-mm-yy)");
		JTextField bdateV = new JTextField();
		JLabel phone = new JLabel("Phone");
		JTextField phoneV = new JTextField();
		JLabel plan = new JLabel("Plan");
		JTextField planV = new JTextField();
		JLabel hn = new JLabel("House number");
		JTextField hnV = new JTextField();
		JLabel str = new JLabel("Street");
		JTextField strV = new JTextField();
		JLabel dist = new JLabel("District");
		JTextField distV = new JTextField();
		JLabel city = new JLabel("City");
		JTextField cityV = new JTextField();
		JLabel pc = new JLabel("Post code");
		JTextField pcV = new JTextField();
		gridL.add(title);
		gridL.add(titleV);
		gridL.add(forename);
		gridL.add(forenameV);
		gridL.add(surname);
		gridL.add(surnameV);
		gridL.add(bdate);
		gridL.add(bdateV);
		gridL.add(phone);
		gridL.add(phoneV);
		gridL.add(plan);
		gridL.add(planV);
		gridL.add(hn);
		gridL.add(hnV);
		gridL.add(str);
		gridL.add(strV);
		gridL.add(dist);
		gridL.add(distV);
		gridL.add(city);
		gridL.add(cityV);
		gridL.add(pc);
		gridL.add(pcV);
		JPanel flow = new JPanel();
		flow.setLayout(new BoxLayout(flow, BoxLayout.Y_AXIS));
		flow.add(labelPanel);
		flow.add(gridL);
		JButton btn = new JButton("Submit");
		btn.setPreferredSize(new Dimension(100, 50));
		btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean ok1 = true;
				boolean ok2 = true;
				boolean ok3 = true;
				boolean ok4 = true;
				boolean ok5 = true;
				boolean ok6 = true;
				boolean ok7 = true;
				boolean ok8 = true;
				boolean ok9 = true;
				boolean ok10 = true;
				if(titleV.getText().isEmpty()){
					titleV.setText("Insert value");
					ok1 = false;
				}
				else ok1 = true;
				if(forenameV.getText().isEmpty()){
					forenameV.setText("Insert value");
					ok2 = false;
				}
				else ok2 = true;
				if(surnameV.getText().isEmpty()){
					surnameV.setText("Insert value");
					ok3 = false;
				}
				else ok3 = true;
				if(bdateV.getText().isEmpty()){
					bdateV.setText("Insert value");
					ok4 = false;
				}
				else{
					 String datePattern = "\\d{4}-\\d{2}-\\d{2}";
				     boolean isDate = bdateV.getText().matches(datePattern);
				     if(isDate)
				    	 ok4 = true;
				     else{
				    	 bdateV.setText("Insert a valid date format");
				    	 ok4 = false;
				     }
				}
				if(phoneV.getText().isEmpty()){
					phoneV.setText("Insert value");
					ok5 = false;
				}
				else ok5 = true;
				if(hnV.getText().isEmpty()){
					hnV.setText("Insert value");
					ok6 = false;
				}
				else { 
					if(Search.isInteger(hnV.getText()))
						ok6 = true;
					else{
						hnV.setText("Insert a valid integer");
						ok6 = false;
					}
				}
				if(strV.getText().isEmpty()){
					strV.setText("Insert value");
					ok7 = false;
				}
				else ok7 = true;
				if(distV.getText().isEmpty()){
					distV.setText("Insert value");
					ok8 = false;
				}
				else ok8 = true;
				if(cityV.getText().isEmpty()){
					ok9 = false;
					cityV.setText("Insert value");
				}
				else ok9 = true;
				if(pcV.getText().isEmpty()){
					ok10 = false;
					pcV.setText("Insert value");
				}
				else ok10 = true;
				if(ok1&&ok2&&ok3&&ok4&&ok5&&ok6&&ok7&&ok8&&ok9&&ok10){
					Boolean planValid = Connection.insertPatient("0",titleV.getText(), forenameV.getText(), surnameV.getText(), bdateV.getText(),
							phoneV.getText(), planV.getText(),hnV.getText(), strV.getText(), distV.getText(), cityV.getText(), pcV.getText());
					if(!planValid)
						planV.setText("Not a valid plan");
					else{
				        JLabel hi = new JLabel("Patient inserted!");
				        JPanel success = Appointment.createCentered(hi);
				        register.removeAll();
				        register.add(success);
				        register.revalidate();
					}
				}
				System.out.println("click");
			}
		});
		JPanel btnPanel = createCentered(btn);
		flow.add(btnPanel);
		register.add(flow);
		return register;
	}
	
	//creates a centered label
	private JPanel createCentered(JLabel btn) {

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.PAGE_AXIS));
		btnPanel.add(Box.createVerticalGlue());
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnPanel.add(btn);
		btnPanel.add(Box.createVerticalGlue());
		return btnPanel;
	}
	
	//creates a centered button
	private JPanel createCentered(JButton btn) {

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.PAGE_AXIS));
		btnPanel.add(Box.createVerticalGlue());
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnPanel.add(btn);
		btnPanel.add(Box.createVerticalGlue());
		return btnPanel;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	

}

