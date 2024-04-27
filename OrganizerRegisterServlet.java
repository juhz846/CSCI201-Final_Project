package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


@WebServlet("/OrganizerRegisterServlet")
public class OrganizerRegisterServlet {
	
	protected static int registerOrganizer(String email, String password, String username) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		int organizerID = -1;
		
		try {
	        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ticketmaster?user=root&password=allent2004");
			
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM organizers WHERE email='" + email + "'");
			if (!rs.next()) {
				st = conn.createStatement();
				rs = st.executeQuery("SELECT * FROM organizers WHERE username='" + username + "'");
				if (!rs.next()) {
					rs.close();
					st.execute("INSERT INTO organizers (email, username, password, balance) VALUES ('" + email + "', '" + username + "', '" + password + "', 0)");
					rs = st.executeQuery("SELECT LAST_INSERT_ID()");
					rs.next();
					organizerID = rs.getInt(1);
				}
				else {
					organizerID = -2;
				}
			}
		} catch(SQLException sqle) {
			System.out.println("SQLException in registerUser. ");
			sqle.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		
		return organizerID;
	}
	
	
	private static final long serialVersionUID = 1L;
	
	public OrganizerRegisterServlet() {
	      super();
	  }
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Organizer user = new Gson().fromJson(request.getReader(), Organizer.class);
		
		String email = user.getEmail();
		String password = user.getPassword();
		String username = user.getUsername();
		double balance = user.getBalance();
		
		Gson gson = new Gson();
		
		if (username == null || username.isBlank() 
				|| password == null || password.isBlank()
				|| email == null || email.isBlank()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "User info missing";
			pw.write(gson.toJson(error));
			pw.flush();
		}
		
		int userID = registerOrganizer(email, password, username);
		
		if (userID == -1) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Username is taken";
			pw.write(gson.toJson(error));
			pw.flush();
		}
		else if (userID == -2) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Email is already registered";
			pw.write(gson.toJson(error));
			pw.flush();
		}
		else {
			response.setStatus(HttpServletResponse.SC_OK);
			pw.write(gson.toJson(userID));
			pw.flush();
		}
	}
	
	class Organizer {
	    private int organizerId;
	    private String username;
	    private String password; // Note: Storing passwords as plain text is not secure.
	    private String email;
	    private double balance;
	
	    // Constructor
	    public Organizer(String username, String password, String email, double balance) {
	        this.username = username;
	        this.password = password;
	        this.email = email;
	        this.balance = balance;
	    }
	
	    // Getters
	    public int getUserId() {
	        return organizerId;
	    }
	
	    public String getUsername() {
	        return username;
	    }
	
	    public String getPassword() {
	        return password;
	    }
	
	    public String getEmail() {
	        return email;
	    }
	
	    public double getBalance() {
	        return balance;
	    }
	
	    // Setters
	    public void setUserId(int organizerId) {
	        this.organizerId = organizerId;
	    }
	
	    public void setUsername(String username) {
	        this.username = username;
	    }
	
	    public void setPassword(String password) {
	        this.password = password;
	    }
	
	    public void setEmail(String email) {
	        this.email = email;
	    }
	
	    public void setBalance(double balance) {
	        this.balance = balance;
	    }
	}  
}
