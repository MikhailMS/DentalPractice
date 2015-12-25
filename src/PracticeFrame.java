import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class PracticeFrame implements Runnable {

	private Dimension dim;
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu calendar, search, register, book;
	private JPanel home;

	//constructs the window
	@Override
	public void run() {
		// get the screem size
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println(dim.getHeight());
		// create a new frame
		frame = new JFrame();
		frame.setPreferredSize(new Dimension((int) (dim.getWidth() / 1.8),
				(int) (dim.getHeight() / 1.5)));
		// create a menuBar
		menuBar = new JMenuBar();
		
		// build the File menu
		calendar = new JMenu("Calendar");
		calendar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				MPanel doctor = new MPanel(11, 2015, dim.getWidth(), dim
						.getHeight(), "Dentist");
				MPanel hygienist = new MPanel(11, 2015, dim.getWidth(), dim
						.getHeight(), "Hygienist");

				JTabbedPane jtp = new JTabbedPane();
				frame.setTitle("Calendar");
				jtp.addTab("Doctor", doctor);
				jtp.addTab("Hygienist", hygienist);
				frame.getContentPane().removeAll();
				frame.getContentPane().add(jtp, BorderLayout.CENTER);
				frame.getContentPane().doLayout();
				frame.update(frame.getGraphics());
				// frame.revalidate();
				frame.pack();
			}
		});

		search = new JMenu("Search");
		search.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				frame.setTitle("Search");
				Search searchField = new Search(dim.getWidth(), dim.getHeight());
				frame.getContentPane().removeAll();
				frame.getContentPane().add(searchField, BorderLayout.CENTER);
				frame.getContentPane().doLayout();
				frame.update(frame.getGraphics());
				// frame.revalidate();
				frame.pack();
			}
		});
		register = new JMenu("Register");
		register.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				frame.setTitle("Register");
				Register register = new Register(dim.getWidth(), dim
						.getHeight());
				frame.getContentPane().removeAll();
				frame.getContentPane().add(register, BorderLayout.CENTER);
				frame.getContentPane().doLayout();
				frame.update(frame.getGraphics());
				// frame.revalidate();
				frame.pack();
			}
		});
		book = new JMenu("Appointment");
		book.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				frame.setTitle("Appointment");
				Appointment book = new Appointment(dim.getWidth(), dim.getHeight());
				frame.getContentPane().removeAll();
				frame.getContentPane().add(book, BorderLayout.CENTER);
				frame.getContentPane().doLayout();
				frame.update(frame.getGraphics());
				// frame.revalidate();
				frame.pack();
			}
		});

		JPanel blay = new JPanel(new BorderLayout());
		JButton partner = new JButton("Partner");
		partner.setPreferredSize(new Dimension(100, 50));
		partner.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				menuBar.add(calendar);
				frame.setJMenuBar(menuBar);
				JLabel part = new JLabel("Partner interface");
				home = createCentred(part);
				frame.add(home);
				frame.getContentPane().doLayout();
				frame.update(frame.getGraphics());
				frame.pack();
			}
		});
		blay.add(partner, BorderLayout.WEST);
		JButton secretary = new JButton("Secretary");
		secretary.setPreferredSize(new Dimension(100, 50));
		secretary.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				menuBar.add(calendar);
				menuBar.add(search);
				menuBar.add(register);
				menuBar.add(book);
				frame.setJMenuBar(menuBar);
				JLabel sec = new JLabel("Secretary interface");
				home = Appointment.createCentered(sec);
				frame.add(home);
				frame.getContentPane().doLayout();
				frame.update(frame.getGraphics());
				frame.pack();
			}
		});
		blay.add(secretary, BorderLayout.EAST);
		blay.setPreferredSize(new Dimension((int) (dim.getWidth() / 1.8),
				(int) (dim.getHeight() / 1.5)));
		frame.add(blay);

		// set the title
		frame.setTitle("Practice");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				exitProcedure();
			}
		});
		frame.setLayout(new GridLayout());

		frame.pack();
		// frame.setResizable(false);

		// Center the frame

		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height
				/ 2 - frame.getSize().height / 2);
		// frame.setLocation(0, 0);
		frame.setVisible(true);
		System.out.println("Frame size = " + frame.getSize());
	}

	// close the frame
	public void exitProcedure() {
		frame.dispose();
		System.exit(0);
	}

	//creates a centered label
	private JPanel createCentred(JLabel lbl){
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.PAGE_AXIS));
		btnPanel.add(Box.createVerticalGlue());
		lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnPanel.add(lbl);
		btnPanel.add(Box.createVerticalGlue());
		return btnPanel;	
	}

}