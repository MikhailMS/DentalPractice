import java.sql.* ;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Connection {
	
	private static String connectionString = "jdbc:mysql://stusql.dcs.shef.ac.uk/team011?user=team011&password=d29f3255";
	protected static java.sql.Connection connection;
	private static ResultSet data;
	private static PreparedStatement stmt = null;
	
	//functionality for registering a patient
	//first checks if the address exists - if no, add the address and then the patient
	//									   if yes, just add the patient
	//returns a boolean if the Plan for the patient is valid
	public static boolean insertPatient(String id,String title,String forename,String surname, String bdate,String phone,String plan,String hn,String str,String dst,String city,String p){
		boolean planValid = false;
		try{
			// this is how you connect
			connection = DriverManager.getConnection(connectionString);
			// in here you create a statement to execute different queries 
			if(plan.isEmpty()||plan == "null"){
				stmt = connection.prepareStatement("SELECT * FROM Address WHERE House_number= ? AND Postcode= ?;");
				stmt.setString(1, hn);
				stmt.setString(2, p);
				data = stmt.executeQuery();
				if(!data.isBeforeFirst()){
					stmt = connection.prepareStatement("INSERT INTO Address VALUES(?,?,?,?,?);");
					stmt.setString(1, hn);
					stmt.setString(2, str);
					stmt.setString(3, dst);
					stmt.setString(4, city);
					stmt.setString(5, p);
					stmt.executeUpdate();
					
					stmt = connection.prepareStatement("INSERT INTO Patient VALUES(?,?,?,?,?,?,?,?,?);");
					stmt.setString(1, id);
					stmt.setString(2, title);
					stmt.setString(3, forename);
					stmt.setString(4, surname);
					stmt.setString(5, bdate);
					stmt.setString(6, phone);
					stmt.setString(7, "null");
					stmt.setString(8, hn);
					stmt.setString(9, p);
					stmt.executeUpdate();
				}
				else{
					stmt = connection.prepareStatement("INSERT INTO Patient VALUES(?,?,?,?,?,?,?,?,?);");
					stmt.setString(1, id);
					stmt.setString(2, title);
					stmt.setString(3, forename);
					stmt.setString(4, surname);
					stmt.setString(5, bdate);
					stmt.setString(6, phone);
					stmt.setString(7, "null");
					stmt.setString(8, hn);
					stmt.setString(9, p);
					stmt.executeUpdate();
				}
				planValid = true;
			}
			else {
				planValid = true;
				stmt = connection.prepareStatement("SELECT * FROM Plan WHERE name=?;");
				stmt.setString(1, plan);
				data = stmt.executeQuery();
				if(!data.isBeforeFirst()){
					planValid = false;
				}
				else{
					stmt = connection.prepareStatement( "SELECT * FROM Address WHERE House_number= ? AND Postcode= ? ;");
					stmt.setString(1, String.valueOf(hn));
					stmt.setString(2, String.valueOf(p));
					data = stmt.executeQuery();
					if(!data.isBeforeFirst()){
						stmt = connection.prepareStatement("INSERT INTO Address VALUES(?,?,?,?,?);");
						stmt.setString(1, hn);
						stmt.setString(2, str);
						stmt.setString(3, dst);
						stmt.setString(4, city);
						stmt.setString(5, p);
						stmt.executeUpdate();
						
						stmt = connection.prepareStatement("INSERT INTO Patient VALUES(?,?,?,?,?,?,?,?,?);");
						stmt.setString(1, id);
						stmt.setString(2, title);
						stmt.setString(3, forename);
						stmt.setString(4, surname);
						stmt.setString(5, bdate);
						stmt.setString(6, phone);
						stmt.setString(7, plan);
						stmt.setString(8, hn);
						stmt.setString(9, p);
						stmt.executeUpdate();
					}
					else{
						stmt = connection.prepareStatement("INSERT INTO Patient VALUES(?,?,?,?,?,?,?,?,?);");
						stmt.setString(1, id);
						stmt.setString(2, title);
						stmt.setString(3, forename);
						stmt.setString(4, surname);
						stmt.setString(5, bdate);
						stmt.setString(6, phone);
						stmt.setString(7, plan);
						stmt.setString(8, hn);
						stmt.setString(9, p);
						stmt.executeUpdate();
					}
				}
			}
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		return planValid;
	}
	
	//modifies the address of a patient
	//if the address exists, update the patient
	//if it doesn't, insert the address and the patient
	public static void modifyAddress(String hn,String str, String dst,String city,String p,String id,String ohn,String opc){
		try{
			// this is how you connect
			connection = DriverManager.getConnection(connectionString);
			// in here you create a statement to execute different queries 
			stmt = connection.prepareStatement("SELECT * FROM Address WHERE House_number='"+hn+"'AND Postcode='"+p+"';");
			stmt.setString(1, String.valueOf(hn));
			stmt.setString(2, String.valueOf(p));
			data = stmt.executeQuery();
			if(hn == ohn && p == opc){
				stmt = connection.prepareStatement("UPDATE Address SET Street = ? , District= ? ,City= ? , WHERE House_number= ? AND Postcode= ? ;");
				stmt.setString(1, str);
				stmt.setString(2, dst);
				stmt.setString(3, city);
				stmt.setString(4, hn);
				stmt.setString(5, p);
				stmt.executeUpdate();
			}
			
			if(!data.isBeforeFirst()){
				stmt = connection.prepareStatement("INSERT INTO Address VALUES(?,?,?,?,?);");
				stmt.setString(1, hn);
				stmt.setString(2, str);
				stmt.setString(3, dst);
				stmt.setString(4, city);
				stmt.setString(5, p);
				stmt.executeUpdate();
				
				stmt = connection.prepareStatement("UPDATE Patient SET House_number = ? , Postcode= ?  WHERE ID= ? ;");
				stmt.setString(1, hn);
				stmt.setString(2, p);
				stmt.setString(3, id);
				stmt.executeUpdate();
			}
			else{
				stmt = connection.prepareStatement("UPDATE Patient SET House_number = ? , Postcode= ?  WHERE ID= ? ;");
				stmt.setString(1, hn);
				stmt.setString(2, p);
				stmt.setString(3, id);
				stmt.executeUpdate();
			}
			stmt = connection.prepareStatement("SELECT * FROM Patient WHERE House_number= ? AND Postcode= ? ;");
			stmt.setString(1, ohn);
			stmt.setString(2, opc);
			data = stmt.executeQuery();
			if(!data.isBeforeFirst()){
				stmt = connection.prepareStatement("DELETE FROM Address WHERE House_number= ? AND Postcode= ? ");
				stmt.setString(1, ohn);
				stmt.setString(2, opc);
				stmt.executeUpdate();
			}
			
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
	}

	//returns a list of appointments given a date and a partner
	public static ResultSet getAppointments(String currentDate,String partner){
		try{
			// this is how you connect
			connection = DriverManager.getConnection(connectionString);
			stmt = connection.prepareStatement("SELECT * FROM Appointment WHERE Date = ? AND Partner = ? ORDER BY Start ASC;");
			stmt.setString(1, currentDate);
			stmt.setString(2, partner);
			data = stmt.executeQuery();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		return data;
	}
	
	//given a patient id returns the name associated to it
	public static ResultSet getPatientName(String patientId){
		try{
			// this is how you connect
			connection = DriverManager.getConnection(connectionString);
			
			stmt = connection.prepareStatement("SELECT ID,Forename,Surname FROM Patient WHERE ID =?;");
			stmt.setString(1, patientId);
			data = stmt.executeQuery();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		return data;
	}
	
	//returns a list of appointments given date, start and end times
	public static ResultSet getTreatments(String day,String start,String partner){
		try{
			connection = DriverManager.getConnection(connectionString);
			stmt = connection.prepareStatement("SELECT Name,Cost FROM Treatment WHERE Date = ? AND Start = ? AND Partner = ?;");
			stmt.setString(1, day);
			stmt.setString(2, start);
			stmt.setString(3, partner);
			data = stmt.executeQuery();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		return data;
	}
	
	//returns a list of appointments given date,start and partner
	public static ResultSet getAppointments(String day,String start,String partner){
		try{
			connection = DriverManager.getConnection(connectionString);
			stmt = connection.prepareStatement("SELECT Start,End FROM Appointment WHERE Date = ?;");
			stmt.setString(1, day);
			data = stmt.executeQuery();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		return data;
	}

	//a function for inserting a treatment in an appointment
	public static void insertTreatment(String trtName,int cost,String date,String start,String partner){
		try{
			// this is how you connect
			connection = DriverManager.getConnection(connectionString);
			stmt = connection.prepareStatement("INSERT INTO Treatment VALUES(?,?,?,?,?);");
			stmt.setString(1, trtName);
			stmt.setString(2, String.valueOf(cost));
			stmt.setString(3, date);
			stmt.setString(4, start);
			stmt.setString(5, partner);
			stmt.executeUpdate();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
	}
	
	//modifies the personal information of the patient without the address
	public static void modifyPatient(String id,String title, String fname,String sname,String bd,String ph,String pl){
		try{
			// this is how you connect
			connection = DriverManager.getConnection(connectionString);
			stmt = connection.prepareStatement("UPDATE Patient SET Title =?, Forename=?,Surname=?,Birthdate=?,Phone=?, Plan=? WHERE ID=?;");
			stmt.setString(1, title);
			stmt.setString(2, fname);
			stmt.setString(3, sname);
			stmt.setString(4, bd);
			stmt.setString(5, ph);
			stmt.setString(5, pl);
			stmt.setString(5, id);
			stmt.executeUpdate();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
	}
	
	//returns the row of the patient given their id
	public static ResultSet searchByID(int id) {
		try{
			connection = DriverManager.getConnection(connectionString);
			stmt = connection.prepareStatement("SELECT * FROM Patient WHERE ID =?;");
			stmt.setString(1, String.valueOf(id));
			data = stmt.executeQuery();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		return data;
	}
	
	//returns a list of people given a surname
	public static ResultSet searchBySurname(String name) {
		try{
			connection = DriverManager.getConnection(connectionString);
			stmt = connection.prepareStatement("SELECT * FROM Patient WHERE Surname =?;");
			stmt.setString(1, name);
			data = stmt.executeQuery();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		return data;
	}
	
	//returns the row of the patient given their id
	public static ResultSet getPatientInfo(int id) {
		try{
			connection = DriverManager.getConnection(connectionString);
			stmt = connection.prepareStatement("SELECT * FROM Patient WHERE ID =?;");
			stmt.setString(1, String.valueOf(id));
			data = stmt.executeQuery();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		return data;
	}
	
	//returns the full address of the patient given house number and postcode
	public static ResultSet getPatientAddress(int house,String postcode) {
		try{
			connection = DriverManager.getConnection(connectionString);
			stmt = connection.prepareStatement("SELECT * FROM Address WHERE House_number = ? AND Postcode = ? ");
			stmt.setString(1, String.valueOf(house));
			stmt.setString(2, postcode);
			data = stmt.executeQuery();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		return data;
	}
	
	//closes the connection with the server
	public static void closeConnection(){
		if(connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
