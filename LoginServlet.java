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
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;



@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	
	protected static int loginUser(String email, String password) {
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			}
			catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		    Connection conn = null;
		    Statement st = null;
		    ResultSet rs = null;
	
		    int userID = -1;
	
		    try {
		        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ticketmaster?user=root&password=allent2004");
		        st = conn.createStatement();
		        
		        String sql = "SELECT id FROM users WHERE email='" + email.replaceAll("'", "''") + "' AND password='" + password.replaceAll("'", "''") + "'";
		        
		        rs = st.executeQuery(sql);
		        if (rs.next()) {
		            userID = rs.getInt("id");
		        } 
		    } catch (SQLException sqle) {
		        sqle.printStackTrace();
		    } finally {
		        try {
		            if (rs != null) rs.close();
		            if (st != null) st.close();
		            if (conn != null) conn.close();
		        } catch (SQLException sqle) {
		            sqle.printStackTrace();
		        }
		    }
	
		    return userID;
		}


	
	private static final long serialVersionUID = 1L;
	    
	    public LoginServlet() {
	        super();
	    }
	
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        
	    	
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			
			User user = new Gson().fromJson(request.getReader(), User.class);
			
			String email = user.getEmail();
			
			String password = user.getPassword();
			
			Gson gson = new Gson();
	    	
			if (email == null || password == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "Please fill out all required information";
				pw.write(gson.toJson(error));
				pw.flush();
			}
	        
	        int userID = loginUser(email, password);
	
	        
	        if (userID > 0) {
	            response.setStatus(HttpServletResponse.SC_OK);
	            pw.write(gson.toJson(userID));
	            pw.flush();
	        } else {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            String error = "Invalid username or password";
	            pw.write(gson.toJson(error));
	            pw.flush();
	        }
	        
	        pw.close();
	    }

}




class User {
    private int userId;
    private String username;
    private String password; // Note: Storing passwords as plain text is not secure.
    private String email;
    private double balance;

    // Constructor
    public User(String username, String password, String email, double balance) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.balance = balance;
    }

    // Getters
    public int getUserId() {
        return userId;
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
    public void setUserId(int userId) {
        this.userId = userId;
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
