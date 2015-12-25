import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class MPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private int width, height;
	private int currentDay, month, year;
	private int currentMonth, currentYear;
	private int tHeight, dHeight, nHeight;
	private JPanel monthPanel, titleP, daysP, numbersP;
	private static ResultSet data;
	private String partner;
	private int count = 0;
	private int startTCount = 0;
	private int totalCost;

	private String[] monthNames = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };

	private String[] dayNames = { "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su" };

	private String[] appNames = { "Date", "Start", "End", "Patient",
			"Treatment" };

	private String[] trtNames = { "Treatment Name", "Cost" };

	String[] treatmentForm = { "Name: ", "Cost: " };

	private ArrayList<String> dayNamesList = new ArrayList<String>();

	private ArrayList<String> apps = new ArrayList<String>();

	private ArrayList<String> currentPatient = new ArrayList<String>();
	private ArrayList<String> startTime = new ArrayList<String>();
	
	private ArrayList<String> appDays = new ArrayList<String>();

	//MPanel constructor
	public MPanel(int month, int year, double width, double height,
			String partner) {
		this.month = month;
		this.year = year;
		currentMonth = month;
		currentYear = year;
		this.width = (int) (width / 1.8);
		this.height = (int) (height / 1.5);
		this.partner = partner;
		this.add(createCalendar(this.month, this.year));

	}

	// Create a Calendar
	private JPanel createCalendar(int month, int year) {
		tHeight = height / 10;
		dHeight = (height - tHeight) / 20;
		nHeight = height - (tHeight + dHeight);
		dayNamesList.addAll(Arrays.asList(dayNames));
		monthPanel = new JPanel(true);
		monthPanel.setLayout(new BorderLayout());
		monthPanel.setBackground(Color.WHITE);
		monthPanel.setForeground(Color.BLACK);
		titleP = createMonth(month, year);
		monthPanel.add(titleP, BorderLayout.NORTH);
		daysP = createDayLabels();
		monthPanel.add(daysP, BorderLayout.CENTER);
		numbersP = createDays(month, year);
		monthPanel.add(numbersP, BorderLayout.SOUTH);
		// monthPanel.setBorder(null);
		return monthPanel;
	}

	// delete the old panel(month) and add a new one
	private void newMonth() {
		monthPanel.removeAll();
		titleP = createMonth(month, year);
		monthPanel.add(titleP, BorderLayout.NORTH);
		monthPanel.add(daysP, BorderLayout.CENTER);
		numbersP = createDays(month, year);
		monthPanel.add(numbersP, BorderLayout.SOUTH);
		monthPanel.revalidate();
	}

	// create the title of the calendar with two buttons
	private JPanel createMonth(int m, int y) {
		JPanel titlePanel = new JPanel(true);
		titlePanel.setLayout(new BorderLayout());
		JButton prev = creatButton("prev");
		prev.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				month--;
				if (month == -1) {
					month = 11;
					year--;
				}
				newMonth();
			}
		});
		titlePanel.add(prev, BorderLayout.WEST);
		titlePanel.setPreferredSize(new Dimension(width, tHeight));
		titlePanel.setBackground(Color.WHITE);
		JLabel label = new JLabel(monthNames[m] + " " + y);
		label.setFont(new java.awt.Font("Times New Roman", 1, 18));
		label.setBackground(Color.WHITE);
		label.setOpaque(true);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		titlePanel.add(label, BorderLayout.CENTER);
		JButton next = creatButton("next");
		next.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				month++;
				if (month == 12) {
					month = 0;
					year++;
				}
				newMonth();
			}
		});
		titlePanel.add(next, BorderLayout.EAST);

		return titlePanel;
	}

	// create a grid view with day names
	private JPanel createDayLabels() {
		JPanel dayLabels = new JPanel(true);
		dayLabels.setLayout(new GridLayout(0, dayNames.length));
		dayLabels.setPreferredSize(new Dimension(width, dHeight));
		for (int i = 0; i < dayNames.length; i++) {
			JPanel days = new JPanel(true);
			days.setLayout(new BorderLayout());
			days.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1,
					Color.BLACK));
			JLabel dayName = new JLabel(dayNames[i]);
			dayName.setHorizontalAlignment(JLabel.CENTER);
			dayName.setVerticalAlignment(JLabel.CENTER);
			days.add(dayName, BorderLayout.CENTER);
			dayLabels.add(days);
		}
		return dayLabels;
	}

	// get the number of days in a month
	private int getDaysInMonth(int year, int month, int day) {
		Calendar pCal = new GregorianCalendar(year, month, day);

		// Get the number of days in that month
		int daysInMonth = pCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return daysInMonth;
	}

	// create the grid view for all the days
	private JPanel createDays(int m, int y) {
		JPanel dayPanel = new JPanel(true);
		dayPanel.setLayout(new GridLayout(0, dayNames.length));
		dayPanel.setPreferredSize(new Dimension(width, nHeight));

		Calendar today = Calendar.getInstance();
		int iMonth = today.get(Calendar.MONTH);
		int iYear = today.get(Calendar.YEAR);
		int iDay = today.get(Calendar.DAY_OF_MONTH);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, m);
		calendar.set(Calendar.YEAR, y);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		Date firstDayOfMonth = calendar.getTime();

		DateFormat sdf = new SimpleDateFormat("EEEEEEEE");
		String fdom = sdf.format(firstDayOfMonth).substring(0, 2);
		int firstDay = dayNamesList.indexOf(fdom);

		// Get the number of days in that month
		int daysInPreviousMonth = getDaysInMonth(iYear, iMonth-1, iDay);

		int daysInMonth = getDaysInMonth(year, month, iDay);
		System.out.println(daysInMonth);

		List<String> days = new ArrayList<String>();
		for (int i = 0; i < firstDay; i++) {
			days.add(String.valueOf(daysInPreviousMonth - i));
		}
		for (int i = 1; i <= daysInMonth; i++) {
			days.add(String.valueOf(i));
		}

		int max = dayNames.length * 6;

		int daysToAdd = max - days.size();

		for (int i = 1; i <= daysToAdd; i++) {
			days.add(String.valueOf(i));
		}

		for (int i = 0; i < max; i++) {
			int day = Integer.parseInt(days.get(i));
			int x = i;
			JPanel dPanel = new JPanel(true);
			dPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			dPanel.setLayout(new BorderLayout());
			JLabel dayLabel = new JLabel();
			dayLabel.setText(days.get(i));
			dayLabel.setHorizontalAlignment(JLabel.CENTER);
			dayLabel.setVerticalAlignment(JLabel.CENTER);
			
			if (i < firstDay || i >= daysInMonth + firstDay)
				dPanel.setBackground(new Color(255, 255, 255, 0));
			else {
				int dayNumber = Integer.parseInt(days.get(i));
				if (dayNumber == iDay && i < 25 && month == currentMonth
						&& year == currentYear) {
					dPanel.setBackground(Color.ORANGE);
				}
				else {
					dPanel.setBackground(Color.WHITE);
				}
			}
			dPanel.add(dayLabel, BorderLayout.CENTER);
			dPanel.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent evt) {
					monthPanel.removeAll();
					if (x < firstDay) {
						if (m - 1 == -1) {
							month = 11;
							year = year - 1;
						} else {
							month = month - 1;
						}
					} else if (x >= daysInMonth + firstDay) {
						if (m + 1 == 12) {
							month = 0;
							year = year + 1;
						} else {
							month = month + 1;
						}
					}
					System.out.println("Current day:" + currentDay);
					int daysInMonth = getDaysInMonth(year, month, currentDay);
					titleP = createDayTitle(day, month, year, daysInMonth);
					JPanel flow = new JPanel(new BorderLayout());
					JPanel appNames = createAppLabels();
					flow.add(titleP, BorderLayout.NORTH);
					flow.add(appNames, BorderLayout.SOUTH);
					flow.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1,
							Color.BLACK));
					monthPanel.add(flow, BorderLayout.NORTH);
					JButton next = creatButton("Back");
					next.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mousePressed(java.awt.event.MouseEvent evt) {
							month = currentMonth;
							year = currentYear;
							newMonth();
						}
					});
					JPanel rowList = new JPanel(new BorderLayout());
					JPanel listView = new JPanel();
					listView.setLayout(new GridLayout(0, 1));
					JScrollPane scroller = new JScrollPane() {
						@Override
						public Dimension getPreferredSize() {
							return new Dimension(width, height
									- (50 + tHeight + dHeight));
						}
					};
					rowList.add(listView,BorderLayout.NORTH);
					scroller.setViewportView(rowList);
//					scroller.setViewportView(listView);
					// scroller.setPreferredSize(new Dimension(300, 200));
					data = Connection.getAppointments(year + "-" + (month + 1)
							+ "-" + currentDay, partner);
					try {
						if (!data.isBeforeFirst()) {
							JLabel label = new JLabel("No appointments!!!!!!!!");
//							label.setPreferredSize(new Dimension(width, height
//									- (50 + tHeight + dHeight)));
							label.setFont(new java.awt.Font("Times New Roman",
									1, 18));
							label.setBackground(Color.WHITE);
							label.setOpaque(true);
							label.setHorizontalAlignment(JLabel.CENTER);
							label.setVerticalAlignment(JLabel.CENTER);
							listView.add(label);
						} else {
							data.getMetaData();
							while (data.next()) {
								for (int i = 1; i <= 4; i++) {
									apps.add(data.getString(i));
									if (apps.size() == 4) {
										startTime.add(apps.get(1));
										System.out.println(apps.get(1));
										System.out.println("SIZEEEEEEEEEEEEEEEEEEEEEEEE:"+startTime.size());
										System.out.println(apps.get(3));
										ResultSet data = Connection
												.getPatientName(apps.get(3));
										while (data.next()) {
											if (!data.getString(1).isEmpty()) {
												currentPatient.add(data
														.getString(1));
												currentPatient.add(data
														.getString(2));
												currentPatient.add(data
														.getString(3));
											}
										}
										listView.add(createAppointments(apps));
										apps.clear();
									}
								}
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					Connection.closeConnection();
					monthPanel.add(scroller, BorderLayout.CENTER);
					monthPanel.add(next, BorderLayout.SOUTH);
					monthPanel.revalidate();
				}

				public void mouseReleased(java.awt.event.MouseEvent evt) {
					System.out.println("mouseReleased");
				}
			});

			dayPanel.add(dPanel);
		}
		appDays.clear();

		return dayPanel;
	}

	// remove and update with a new day title
	private void newDayTitle() {
		monthPanel.removeAll();
		int maxDays = getDaysInMonth(year, month, currentDay);
		titleP = createDayTitle(currentDay, month, year, maxDays);
		JPanel flow = new JPanel(new BorderLayout());
		JPanel appNames = createAppLabels();
		flow.add(titleP, BorderLayout.NORTH);
		flow.add(appNames, BorderLayout.SOUTH);
		flow.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK));
		monthPanel.add(flow, BorderLayout.NORTH);
		// monthPanel.add(titleP,BorderLayout.NORTH);
//		JPanel listView = new JPanel() {
//			public Dimension getPreferredSize() {
//				return monthPanel.getSize();
//			};
//		};
//		listView.setLayout(new BoxLayout(listView, BoxLayout.Y_AXIS));
		JPanel rowList = new JPanel(new BorderLayout());
		 JPanel listView = new JPanel();
		 listView.setLayout(new GridLayout(0, 1));
		JScrollPane scroller = new JScrollPane() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(width, height - (50 + tHeight + dHeight));
			}
		};
		rowList.add(listView,BorderLayout.NORTH);
		scroller.setViewportView(rowList);

		data = Connection.getAppointments(year + "-" + (month + 1) + "-"
				+ currentDay, partner);
		try {
			if (!data.isBeforeFirst()) {
				JLabel label = new JLabel("No appointments!!!!!!!!");
//				label.setPreferredSize(new Dimension(width,height
//									- (50 + tHeight + dHeight)));
				label.setFont(new java.awt.Font("Times New Roman", 1, 18));
				label.setBackground(Color.WHITE);
				label.setOpaque(true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				listView.add(label);
			} else {
				data.getMetaData();
				while (data.next()) {
					for (int i = 1; i <= 4; i++) {
						apps.add(data.getString(i));
						if (apps.size() == 4) {
							startTime.add(apps.get(1));
							System.out.println(apps.get(1));
							System.out.println("SIZEEEEEEEEEEEEEEEEEEEEEEEE:"+startTime.size());
							System.out.println(apps.get(3));
							ResultSet data = Connection
									.getPatientName(apps.get(3));
							while (data.next()) {
								if (!data.getString(1).isEmpty()) {
									currentPatient.add(data
											.getString(1));
									currentPatient.add(data
											.getString(2));
									currentPatient.add(data
											.getString(3));
								}
							}
							listView.add(createAppointments(apps));
							apps.clear();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Connection.closeConnection();
		monthPanel.add(scroller, BorderLayout.CENTER);
		JButton back = creatButton("Back");
		back.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				month = currentMonth;
				year = currentYear;
				newMonth();
			}
		});
		monthPanel.add(back, BorderLayout.SOUTH);
		monthPanel.revalidate();
	}

	//shows the treatments in an appointment
	private void printTreatments(String date,String start,String patient) {
		monthPanel.removeAll();
		JPanel first = new JPanel();
		first.setLayout(new BoxLayout(first, BoxLayout.Y_AXIS));
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setPreferredSize(new Dimension(width, tHeight));
		titlePanel.setBackground(Color.WHITE);
		JLabel label = new JLabel(patient+" "+currentDay+"-"+month+"-"+year);
		label.setFont(new java.awt.Font("Times New Roman", 1, 18));
		label.setBackground(Color.WHITE);
		label.setOpaque(true);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		titlePanel.add(label, BorderLayout.CENTER);
		first.add(titlePanel);
		JButton back = creatButton("Back");
		back.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				newDayTitle();
			}
		});
		buttonStyle(back);
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
		first.add(back);
		monthPanel.add(first);
		JPanel addressPanel = new JPanel();
		Border border = addressPanel.getBorder();
		Border margin = new EmptyBorder(10, 10, 10, 10);
		addressPanel.setBorder(new CompoundBorder(border, margin));

		JPanel gridL = new JPanel(new GridLayout(2, 2));
		gridL.setPreferredSize(new Dimension(width,50));
		JLabel nameL = new JLabel("name");
//		JPanel centrN = createCenteredLabel(nameL);
		JTextField name = new JTextField();
		JLabel costL = new JLabel("cost");
//		JPanel centrL = createCenteredLabel(costL);
		JTextField cost = new JTextField();
//		gridL.add(centrN);
		gridL.add(nameL);
		gridL.add(name);
//		gridL.add(centrL);
		gridL.add(costL);
		gridL.add(cost);
		JPanel flow = new JPanel();
		flow.setLayout(new BoxLayout(flow, BoxLayout.Y_AXIS));
		flow.add(gridL);
		JButton submit = creatButton("Submit");
		submit.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				String textFieldValue = name.getText();
				String textFieldValue2 = cost.getText();
				if (cost.getText().isEmpty())
					cost.setText("Insert value");
				else if (name.getText().isEmpty())
					name.setText("Insert name");
				else if(!cost.getText().isEmpty()&&!name.getText().isEmpty()&&isInt(cost.getText()))
					if((int)(Math.log10(currentDay)+1)==1)
						Connection.insertTreatment(name.getText(),Integer.parseInt(cost.getText()),
								year+"-"+(month+1)+"-"+"0"+currentDay,start,partner);
					else 
						Connection.insertTreatment(name.getText(),Integer.parseInt(cost.getText()),
								year+"-"+(month+1)+"-"+currentDay,start,partner);
					if((int)(Math.log10(currentDay)+1)==1)
						printTreatments(year+"-"+(month+1)+"-"+"0"+currentDay,start,patient);
					else 
						printTreatments(year+"-"+(month+1)+"-"+currentDay,start,patient);	
				Connection.closeConnection();
					
					
				System.out.println(textFieldValue);
				System.out.println(textFieldValue2);
			}
		});
		buttonStyle(submit);
		submit.setAlignmentX(Component.CENTER_ALIGNMENT);
		flow.add(submit);
		JPanel rowList = new JPanel(new BorderLayout());
		JPanel listViewTitle = createTrtLabels();
		JPanel listView = new JPanel();
		listView.setLayout(new GridLayout(0, 1));
		JScrollPane scroller = new JScrollPane() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(width, height - (50+50 + tHeight+100));
			}
		};

		rowList.add(listView,BorderLayout.NORTH);
		scroller.setViewportView(rowList);
//		scroller.setViewportView(listView);

		data = Connection.getTreatments(date,start,partner);

		try {
			if (!data.isBeforeFirst()) {
				JLabel label1 = new JLabel("No treatments!!!!!!!!");
				label1.setFont(new java.awt.Font("Times New Roman", 1, 18));
				label1.setBackground(Color.WHITE);
				label1.setOpaque(true);
				label1.setHorizontalAlignment(JLabel.CENTER);
				label1.setVerticalAlignment(JLabel.CENTER);
				listView.add(label1);
			} else {
				data.getMetaData();
				while (data.next()) {
					for (int i = 1; i <= 2; i++) {
						apps.add(data.getString(i));
						if (apps.size() == 2) {
							listView.add(createTreatments(apps));
							System.out.println(apps.get(1));
							totalCost = totalCost + Integer.parseInt(apps.get(1));
							apps.clear();
						}
					}
				}
				apps.add("Total Cost");
				apps.add(String.valueOf(totalCost));
				totalCost = 0;
				listView.add(createTreatments(apps));
				apps.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Connection.closeConnection();
		
		flow.add(listViewTitle);
		flow.add(scroller);
		monthPanel.add(flow, BorderLayout.SOUTH);

		monthPanel.revalidate();
	}

	// create a day title
	private JPanel createDayTitle(int d, int m, int y, int daysInMonth) {
		currentPatient.clear();
		startTime.clear();
		count = 0;
		startTCount = 0;
		currentDay = d;
		JPanel titlePanel = new JPanel(true);
		titlePanel.setLayout(new BorderLayout());
		JButton prev = creatButton("prev");
		prev.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				currentDay--;
				int maxDays = getDaysInMonth(year, month, currentDay);
				if (currentDay == 0) {
					month--;
					maxDays = getDaysInMonth(year, month, currentDay);
					currentDay = maxDays;
				}
				if (month == -1) {
					month = 11;
					year--;
					maxDays = getDaysInMonth(year, month, currentDay);
					currentDay = maxDays;
				}
				newDayTitle();
			}
		});
		titlePanel.add(prev, BorderLayout.WEST);
		titlePanel.setPreferredSize(new Dimension(width, tHeight));
		titlePanel.setBackground(Color.WHITE);

		JLabel label = new JLabel(d + " " + monthNames[m] + " " + y);
		label.setFont(new java.awt.Font("Times New Roman", 1, 18));
		label.setBackground(Color.WHITE);
		label.setOpaque(true);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		titlePanel.add(label, BorderLayout.CENTER);
		JButton next = creatButton("next");
		next.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				currentDay++;
				int maxDays = getDaysInMonth(year, month, currentDay);
				if (currentDay > maxDays) {
					month++;
					currentDay = 1;
				}
				if (month == 12) {
					month = 0;
					year++;
				}
				newDayTitle();
			}
		});
		titlePanel.add(next, BorderLayout.EAST);
		return titlePanel;
	}

	// create the appointment labels
	private JPanel createAppLabels() {
		JPanel appLabels = new JPanel(true);
		appLabels.setLayout(new GridLayout(1, appNames.length));
		appLabels.setPreferredSize(new Dimension(width, dHeight));
		for (int i = 0; i < appNames.length; i++) {
			JPanel apps = new JPanel(true);
			apps.setLayout(new BorderLayout());
			apps.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1,
					Color.BLACK));
			JLabel dayName = new JLabel(appNames[i]);
			dayName.setHorizontalAlignment(JLabel.CENTER);
			dayName.setVerticalAlignment(JLabel.CENTER);
			apps.add(dayName, BorderLayout.CENTER);
			appLabels.add(apps);
		}
		return appLabels;
	}

	//shows a list of appointments
	private JPanel createAppointments(ArrayList<String> apppointments) {
		int copyCounter = count;
		int copyCounter2 = startTCount;
		count = count + 3;
		startTCount ++;
		JPanel app = new JPanel(true);
		app.setLayout(new GridLayout(1, apppointments.size() + 1));
		app.setPreferredSize(new Dimension(width-10, 80));
		System.out.println(dHeight);
		for (int i = 0; i < apppointments.size() + 1; i++) {
			JPanel row = new JPanel(true);
			row.setLayout(new BorderLayout());
			row.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1,
					Color.BLACK));
			JLabel appCol;
			if (i < apppointments.size()) {
				appCol = new JLabel(apppointments.get(i));
				appCol.setHorizontalAlignment(JLabel.CENTER);
				appCol.setVerticalAlignment(JLabel.CENTER);
				row.add(appCol, BorderLayout.CENTER);
				if (i == 3 && copyCounter < currentPatient.size()) {
					appCol = new JLabel(currentPatient.get((copyCounter + 1))
							+ " " + currentPatient.get((copyCounter + 2)) + "("
							+ apppointments.get(i) + ")");
					appCol.setHorizontalAlignment(JLabel.CENTER);
					appCol.setVerticalAlignment(JLabel.CENTER);
					row.add(appCol, BorderLayout.CENTER);
				}
			} else {
				JButton btn = new JButton("Treatment");
				btn.setPreferredSize(new Dimension(100, 50));
//				btn.setBorder(BorderFactory.createMatteBorder(10, 0, 10, 0,new Color(255, 255, 255, 0)));
				btn.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						if (copyCounter < currentPatient.size()) {
							System.out.println(currentPatient.get(copyCounter));
							System.out.println(currentPatient
									.get(copyCounter + 1));
							System.out.println(currentPatient
									.get(copyCounter + 2));
							String ptName = currentPatient.get(copyCounter + 1)+currentPatient
									.get(copyCounter + 2);
							if((int)(Math.log10(currentDay)+1)==1){
								System.out.println("Counter: "+copyCounter2+" "+startTime.size());
								printTreatments(year+"-"+(month+1)+"-"+"0"+currentDay,startTime.get(copyCounter2),ptName);
							}
							else 
								printTreatments(year+"-"+(month+1)+"-"+currentDay,startTime.get(copyCounter2),ptName);	
							Connection.closeConnection();
						}

					}
				});
				JPanel btnPanel = createCenteredButton(btn);
//				btnPanel.setBorder(new ShadowBorder());
//				JPanel wrappingPanel = new JPanel(new BorderLayout());
//		        wrappingPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
//		        wrappingPanel.setOpaque(false);
//		        btn.setBorder(null);
//		        btnPanel.setBorder(null);
//		        wrappingPanel.add(btnPanel);
//				btnPanel.add( Box.createHorizontalStrut(100) );
				row.add(btnPanel);
			}
			app.add(row);
		}

		return app;
	}

	//prints the treatments
	private JPanel createTrtLabels() {
		JPanel trtLabels = new JPanel(true);
		trtLabels.setLayout(new GridLayout(1, trtNames.length));
		trtLabels.setPreferredSize(new Dimension(width, dHeight));
		for (int i = 0; i < trtNames.length; i++) {
			JPanel trts = new JPanel(true);
			trts.setLayout(new BorderLayout());
			trts.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1,
					Color.BLACK));
			JLabel dayName = new JLabel(trtNames[i]);
			dayName.setHorizontalAlignment(JLabel.CENTER);
			dayName.setVerticalAlignment(JLabel.CENTER);
			trts.add(dayName, BorderLayout.CENTER);
			trtLabels.add(trts);
		}
		return trtLabels;
	}

	//puts the treatments in a table
	private JPanel createTreatments(ArrayList<String> treatments) {
		JPanel trtList = new JPanel(true);
		trtList.setLayout(new GridLayout(1, treatments.size()));
		trtList.setPreferredSize(new Dimension(width, dHeight));
		for (int i = 0; i < treatments.size(); i++) {
			JPanel row = new JPanel(true);
			row.setLayout(new BorderLayout());
			row.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1,
					Color.BLACK));
			JLabel trtCol;
			trtCol = new JLabel(treatments.get(i));
			trtCol.setHorizontalAlignment(JLabel.CENTER);
			trtCol.setVerticalAlignment(JLabel.CENTER);
			row.add(trtCol, BorderLayout.CENTER);
			trtList.add(row);
		}

		return trtList;
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
	
	//creates a centered label
	private JPanel createCenteredLabel(JLabel btn) {
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.PAGE_AXIS));
		btnPanel.add(Box.createVerticalGlue());
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnPanel.add(btn);
		btnPanel.add(Box.createVerticalGlue());
		return btnPanel;
	}

	// create a button
	private JButton creatButton(String name) {
		JButton btn = new JButton(name);
		btn.setPreferredSize(new Dimension(100, 50));
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		return btn;
	}
	
	//a style for a button
	private void buttonStyle(JButton btn) {
		btn.setOpaque(true);
		btn.setContentAreaFilled(true);
		btn.setBorderPainted(true);
	}
	
	//checks if a string can be converted to a number
	private boolean isInt(String value){
        boolean status=true;
        if(value.length()<1)
            return false;
        for(int i = 0;i<value.length();i++){
            char c=value.charAt(i);
            if(Character.isDigit(c) || c=='.'){
                
            }else{
                status=false;
                break;
            }
        }
        return status;
    }

	// override the screen size
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

}
