import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class Appointment extends JPanel {
	private int width, height;
	private JPanel book;
	
	
	//Constructor for Appointment
	public Appointment(double width, double height) {
		this.width = (int) (width / 1.8);
		this.height = (int) (height / 1.5);
		this.add(createBook());
	}
	
	
	//Form for booking an appointment
	private JPanel createBook() {
		book = new JPanel();
		JLabel statusLabel = new JLabel("Book info:", JLabel.CENTER);
		JPanel labelPanel = createCentered(statusLabel);
		JPanel gridL = new JPanel(new GridLayout(5, 2));
		gridL.setPreferredSize(new Dimension(width, 160));
		JLabel date = new JLabel("Date");
		JTextField dateV = new JTextField();
		JLabel start = new JLabel("Start Time");
		JTextField startV = new JTextField();
		JLabel end = new JLabel("End Time");
		JTextField endV = new JTextField();
		JLabel partner = new JLabel("Partner");
		JTextField partnerV = new JTextField();
		JLabel patient = new JLabel("Patient ID");
		JTextField patientV = new JTextField();
		gridL.add(date);
		gridL.add(dateV);
		gridL.add(start);
		gridL.add(startV);
		gridL.add(end);
		gridL.add(endV);
		gridL.add(partner);
		gridL.add(partnerV);
		gridL.add(patient);
		gridL.add(patientV);
		JPanel flow = new JPanel();
		flow.setLayout(new BoxLayout(flow, BoxLayout.Y_AXIS));
		flow.add(labelPanel);
		flow.add(gridL);
		JButton btn = new JButton("Submit");
		btn.setPreferredSize(new Dimension(100, 50));
		btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
//				Connection.modifyPatient(ID,titleV.getText(), forenameV.getText(), surnameV.getText(), bdateV.getText(),
//						phoneV.getText(), planV.getText(), hnV.getText(), pc.getText());
				System.out.println("click");
			}
		});
		JPanel btnPanel = createCentered(btn);
		flow.add(btnPanel);
		JLabel holiday = new JLabel("Empty appointment:", JLabel.CENTER);
		JPanel holidayPanel = createCentered(holiday);
		flow.add(holidayPanel);
		JPanel gridE = new JPanel(new GridLayout(2, 2));
		gridE.setPreferredSize(new Dimension(width, 64));
		JLabel dateE = new JLabel("Date");
		JTextField dateEV = new JTextField();
		JLabel partnerE = new JLabel("Partner");
		JTextField partnerEV = new JTextField();
		gridE.add(dateE);
		gridE.add(dateEV);
		gridE.add(partnerE);
		gridE.add(partnerEV);
		flow.add(gridE);
		JButton btn2 = new JButton("Submit Empty");
		btn.setPreferredSize(new Dimension(100, 50));
		btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
//				Connection.modifyPatient(ID,titleV.getText(), forenameV.getText(), surnameV.getText(), bdateV.getText(),
//						phoneV.getText(), planV.getText(), hnV.getText(), pc.getText());
				System.out.println("click");
			}
		});
		JPanel btnPanel2 = createCentered(btn2);
		flow.add(btnPanel2);
		book.add(flow);
		return book;
	}
	
	//creating a centered JLabel
	static JPanel createCentered(JLabel btn) {
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.PAGE_AXIS));
		btnPanel.add(Box.createVerticalGlue());
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnPanel.add(btn);
		btnPanel.add(Box.createVerticalGlue());
		return btnPanel;
	}
	
	//creating a centered button
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
