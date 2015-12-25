import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Search extends JPanel {
	private static ResultSet data;
	private static int id, width, height, count;
	private static JPanel result, patientInfo, patientAddress;
	private static String forname, surname;
	private static JLabel statusLabel, typeInt;
	private static String patient, searchBy, userID, houseNumber, postcode;
	private static JTextField search;
	private static JPanel panel;
	private static String ID;
	private ArrayList<String> apps = new ArrayList<String>();
	private ArrayList<String> currentPatient = new ArrayList<String>();
	private ArrayList<String> ptInfo = new ArrayList<String>();
	private String oldHouse,oldPC;

	//search constructor
	public Search(double width, double height) {
		this.width = (int) (width / 1.8);
		this.height = (int) (height / 1.5);
		this.add(createSearch());
	}

	//creates the search form
	private JPanel createSearch() {
		searchBy = "id";
		panel = new JPanel(); // panel
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel searchLayout = new JPanel(new FlowLayout());
		JLabel label = new JLabel("Find patient:");
		search = new JTextField(20);

		JRadioButton byID = new JRadioButton("ID");
		byID.setSelected(true);
		JRadioButton bySurname = new JRadioButton("Surname");
		ButtonGroup groupRadio = new ButtonGroup();
		groupRadio.add(byID);
		groupRadio.add(bySurname);

		statusLabel = new JLabel("Cannot be empty!", JLabel.CENTER);
		JButton findButton = new JButton("Find");
		JPanel labelPanel = createCenteredButton(statusLabel);
		typeInt = new JLabel("Type an int");
		result = new JPanel();
		statusLabel.setVisible(false);

		findButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				panel.remove(result);
				statusLabel.setVisible(false);
				patient = search.getText();
				panel.add(labelPanel);
				if (searchBy == "id") {
					if (patient.isEmpty()) {
						statusLabel.setText("Cannot be empty");
						statusLabel.setVisible(true);
					} else if (!isInteger(patient)) {
						System.out.println("Does it go here?");
						statusLabel.setText("Type an int");
						statusLabel.setVisible(true);
					} else {
						data = Connection.searchByID(Integer.parseInt(patient));
						result = patientID(Integer.parseInt(patient));
					}
				}

				if (searchBy == "surname") {
					data = Connection.searchBySurname(patient);
					result = patientName(patient);
				}
				try {
					if (data != null) {
						data.getMetaData();
						while (data.next()) {
							for (int i = 1; i <= data.getMetaData()
									.getColumnCount(); i++) {
								System.out.println(data.getMetaData()
										.getColumnName(i)
										+ ": "
										+ data.getString(i));
							}
						}
						panel.add(result);
					}
				}

				catch (SQLException e) {
					e.printStackTrace();
				}
				panel.revalidate();
			}

		});

		byID.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				searchBy = "id";
				System.out.println(searchBy);
			}
		});

		bySurname.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				searchBy = "surname";
				System.out.println(searchBy);
			}
		});

		searchLayout.add(label);
		searchLayout.add(search);
		searchLayout.add(byID);
		searchLayout.add(bySurname);
		searchLayout.add(findButton);
		panel.add(searchLayout);
		return panel;
	}

	//creates a centered label
	private JPanel createCenteredButton(JLabel btn) {

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.PAGE_AXIS));
		btnPanel.add(Box.createVerticalGlue());
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnPanel.add(btn);
		btnPanel.add(Box.createVerticalGlue());
		return btnPanel;
	}

	//returns a list of patients (actually just one patient as the ID is unique)
	private JPanel patientID(int id) {
		JPanel patientID = new JPanel();
		JPanel listView = new JPanel();
		JPanel rowList = new JPanel(new BorderLayout());
		listView.setLayout(new GridLayout(0, 1));
		JScrollPane scroller = new JScrollPane() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(width, height - 120);
			}
		};

		rowList.add(listView, BorderLayout.NORTH);
		scroller.setViewportView(rowList);
		// scroller.setPreferredSize(new Dimension(300, 200));
		data = Connection.searchByID(id);
		try {
			if (!data.isBeforeFirst()) {
				JLabel label = new JLabel("Patient not found!");
				label.setFont(new java.awt.Font("Times New Roman", 1, 18));
				label.setBackground(Color.WHITE);
				label.setOpaque(true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				listView.add(label);
			} else {
				data.getMetaData();
				while (data.next()) {
					for (int i = 1; i <= data.getMetaData().getColumnCount(); i++) {
						apps.add(data.getString(i));
						if (apps.size() == 9) {
							listView.add(createPatients(apps));
							apps.clear();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		patientID.add(scroller);
		return patientID;
	}

	//checks if a string is an integer
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	//returns a list of patients matching the criteria
	private JPanel patientName(String id) {
		JPanel patientID = new JPanel();
		JPanel rowList = new JPanel(new BorderLayout());
		JPanel listView = new JPanel();
		listView.setLayout(new GridLayout(0, 1));
		JScrollPane scroller = new JScrollPane() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(width, height - 150);
			}
		};
		rowList.add(listView, BorderLayout.NORTH);
		scroller.setViewportView(rowList);
		// scroller.setPreferredSize(new Dimension(300, 200));
		data = Connection.searchBySurname(id);
		try {
			if (!data.isBeforeFirst()) {
				JLabel label = new JLabel("Patient not found!");
				label.setFont(new java.awt.Font("Times New Roman", 1, 18));
				label.setBackground(Color.WHITE);
				label.setOpaque(true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				listView.add(label);
			} else {
				data.getMetaData();
				while (data.next()) {
					for (int i = 1; i <= data.getMetaData().getColumnCount(); i++) {
						apps.add(data.getString(i));
						System.out.println(apps.size());
						if (apps.size() == 9) {
							listView.add(createPatients(apps));
							apps.clear();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		patientID.add(scroller);
		return patientID;
	}

	//constructs the table with the patients
	private JPanel createPatients(ArrayList<String> patients) {
		int copyCounter = count;
		count = count + 3;
		userID = "0";
		houseNumber = "0";
		postcode = "tctc";
		JPanel app = new JPanel(true);
		app.setLayout(new GridLayout(1, patients.size() + 1));
		app.setPreferredSize(new Dimension(width, 80));
		for (int i = 0; i < patients.size() + 1; i++) {

			JPanel row = new JPanel(true);
			row.setLayout(new BorderLayout());
			row.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1,
					Color.BLACK));
			JLabel appCol;
			if (i < patients.size()) {
				if (i == 0) {
					userID = patients.get(i);
					System.out.println(userID);
				}
				if (i == 7) {
					houseNumber = patients.get(i);
					System.out.println(houseNumber);
				}
				if (i == 8) {
					postcode = patients.get(i);
					System.out.println(postcode);
				}
				appCol = new JLabel(patients.get(i));
				appCol.setHorizontalAlignment(JLabel.CENTER);
				appCol.setVerticalAlignment(JLabel.CENTER);
				row.add(appCol, BorderLayout.CENTER);
				if (i == 3 && copyCounter < currentPatient.size()) {
					appCol = new JLabel(currentPatient.get((copyCounter + 1))
							+ " " + currentPatient.get((copyCounter + 2)) + "("
							+ patients.get(i) + ")");
					appCol.setHorizontalAlignment(JLabel.CENTER);
					appCol.setVerticalAlignment(JLabel.CENTER);
					row.add(appCol, BorderLayout.CENTER);
				}
			} else {
				JButton btn = new JButton("More");
				btn.setPreferredSize(new Dimension(100, 50));
				btn.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						patientInfo = createPatientInfo(Integer
								.parseInt(userID));
						patientAddress = createPatientAddress(
								Integer.parseInt(houseNumber), postcode);
						panel.removeAll();
						panel.add(patientInfo);
						panel.add(patientAddress);
						JButton btn = new JButton("Back");
						btn.setPreferredSize(new Dimension(100, 50));
						btn.addActionListener(new ActionListener() {

							public void actionPerformed(ActionEvent e) {
								panel.removeAll();
								panel.add(createSearch());
								panel.revalidate();
							}
						});
						JPanel btnPanel = createCenteredButton(btn);
						panel.add(btnPanel);
						panel.revalidate();
					}
				});
				JPanel btnPanel = createCenteredButton(btn);
				row.add(btnPanel);
			}
			app.add(row);
		}

		return app;
	}

	//creates a centered button
	private JPanel createCenteredButton(JButton btn) {
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.PAGE_AXIS));
		btnPanel.add(Box.createVerticalGlue());
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnPanel.add(btn);
		btnPanel.add(Box.createVerticalGlue());
		return btnPanel;
	}

	//the form containing all the information about a patient
	private JPanel createPatientInfo(int id) {
		JPanel personal = new JPanel();
		JLabel statusLabel = new JLabel("Patient info:", JLabel.CENTER);
		JPanel labelPanel = createCenteredButton(statusLabel);
		JPanel gridL = new JPanel(new GridLayout(6, 2));
		gridL.setPreferredSize(new Dimension(width, 256));
		JLabel title = new JLabel("Title");
		JTextField titleV = new JTextField();
		JLabel surname = new JLabel("Surname");
		JTextField surnameV = new JTextField();
		JLabel forename = new JLabel("Forename");
		JTextField forenameV = new JTextField();
		JLabel bdate = new JLabel("Birthdate(yyyy-mm-dd)");
		JTextField bdateV = new JTextField();
		JLabel phone = new JLabel("Phone");
		JTextField phoneV = new JTextField();
		JLabel plan = new JLabel("Plan");
		JTextField planV = new JTextField();
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
		JPanel flow = new JPanel();
		flow.setLayout(new BoxLayout(flow, BoxLayout.Y_AXIS));
		flow.add(labelPanel);
		flow.add(gridL);
		data = Connection.getPatientInfo(id);
		try {
			if (data != null) {
				data.getMetaData();
				while (data.next()) {
					for (int i = 1; i <= data.getMetaData().getColumnCount(); i++) {
						System.out.println(data.getMetaData().getColumnName(i)
								+ ": " + data.getString(i));
						ptInfo.add(data.getString(i));
					}
				}
				ID = ptInfo.get(0);
				titleV.setText(ptInfo.get(1));
				forenameV.setText(ptInfo.get(2));
				surnameV.setText(ptInfo.get(3));
				bdateV.setText(ptInfo.get(4));
				phoneV.setText(ptInfo.get(5));
				planV.setText(ptInfo.get(6));
				ptInfo.clear();
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		JButton btn = new JButton("Submit");
		btn.setPreferredSize(new Dimension(100, 50));
		btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean ok1 = true;
				boolean ok2 = true;
				boolean ok3 = true;
				boolean ok4 = true;
				boolean ok5 = true;
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
				if(ok1&&ok2&&ok3&&ok4&&ok5)
				Connection.modifyPatient(ID,titleV.getText(), forenameV.getText(), surnameV.getText(), bdateV.getText(),
						phoneV.getText(), planV.getText());
				Connection.closeConnection();
			}
		});
		JPanel btnPanel = createCenteredButton(btn);
		flow.add(btnPanel);
		personal.add(flow);
		return personal;
	}

	//the address associated to the patient
	private JPanel createPatientAddress(int house, String postcode) {
		JPanel personal = new JPanel();
		JLabel statusLabel = new JLabel("Address info:", JLabel.CENTER);
		JPanel labelPanel = createCenteredButton(statusLabel);
		JPanel gridL = new JPanel(new GridLayout(5, 2));
		gridL.setPreferredSize(new Dimension(width, 160));
//		JTextField idV = new JTextField();
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
		data = Connection.getPatientAddress(house, postcode);
		try {
			if (data != null) {
				data.getMetaData();
				while (data.next()) {
					for (int i = 1; i <= data.getMetaData().getColumnCount(); i++) {
						System.out.println(data.getMetaData().getColumnName(i)
								+ ": " + data.getString(i));
						ptInfo.add(data.getString(i));
					}
				}
				hnV.setText(ptInfo.get(0));
				strV.setText(ptInfo.get(1));
				distV.setText(ptInfo.get(2));
				cityV.setText(ptInfo.get(3));
				pcV.setText(ptInfo.get(4));
				oldHouse = ptInfo.get(0);
				oldPC = ptInfo.get(4);
				ptInfo.clear();
				// panel.add(result);
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		Connection.closeConnection();
		JButton btn = new JButton("Submit");
		btn.setPreferredSize(new Dimension(100, 50));
		btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean ok1 = true;
				boolean ok2 = true;
				boolean ok3 = true;
				boolean ok4 = true;
				boolean ok5 = true;
				if(hnV.getText().isEmpty()){
					hnV.setText("Insert value");
					ok1 = false;
				}
				else { 
					if(isInteger(hnV.getText()))
						ok1 = true;
					else{
						hnV.setText("Insert a valid integer");
						ok1 = false;
					}
				}
				if(strV.getText().isEmpty()){
					strV.setText("Insert value");
					ok2 = false;
				}
				else ok2 = true;
				if(distV.getText().isEmpty()){
					distV.setText("Insert value");
					ok3 = false;
				}
				else ok3 = true;
				if(cityV.getText().isEmpty()){
					ok4 = false;
					cityV.setText("Insert value");
				}
				else ok4 = true;
				if(pcV.getText().isEmpty()){
					ok5 = false;
					pcV.setText("Insert value");
				}
				else ok5 = true;
				if(ok1&&ok2&&ok3&&ok4&&ok5){
				Connection.modifyAddress(hnV.getText(), strV.getText(), distV.getText(), cityV.getText(), pcV.getText(),ID,oldHouse,oldPC);
				System.out.println("click");
				Connection.closeConnection();
				patientInfo = createPatientInfo(Integer
						.parseInt(userID));
				patientAddress = createPatientAddress(
						Integer.parseInt(hnV.getText()), pcV.getText());
				panel.removeAll();
				panel.add(patientInfo);
				panel.add(patientAddress);
				JButton btn = new JButton("Back");
				btn.setPreferredSize(new Dimension(100, 50));
				btn.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						panel.removeAll();
						panel.add(createSearch());
						panel.revalidate();
					}
				});
				JPanel btnPanel = createCenteredButton(btn);
				panel.add(btnPanel);
				panel.revalidate();
			}
			}
		});
		JPanel btnPanel = createCenteredButton(btn);
		flow.add(btnPanel);
		personal.add(flow);
		return personal;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
}
