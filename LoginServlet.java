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
	
	protected static int loginUser(String username, String password) {
			
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
		        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ticketmaster?user=root&password=root");
		        st = conn.createStatement();
		        
		        String sql = "SELECT id FROM users WHERE username='" + username + "' AND password='" + password + "'";
		        
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
			
			String username = user.getUsername();
			String password = user.getPassword();
						
			Gson gson = new Gson();
	    	
			if (username == null || password == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "Please fill out all required information";
				pw.write(gson.toJson(error));
				pw.flush();
				return;
			}
	        
	        int userID = loginUser(username, password);
	        
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