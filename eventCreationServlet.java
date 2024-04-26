import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/eventCreationServlet")
public class eventCreationServlet extends HttpServlet {
	
	protected static int registerEvent(Event event) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Connection conn = null;
		PreparedStatement ps = null;
		Statement st = null;
		ResultSet rs = null;
		
		int eventID = -1;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ticketmaster?user=root&password=allent2004");
			
			System.out.println("HERE!!!!");
			ps = conn.prepareStatement("INSERT INTO events (name, organizer_id, event_date, event_time, city, state, total_available, price, description, img_file, artist) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
			ps.setString(1, event.getName());
			ps.setInt(2, event.getOrganizer());
			ps.setString(3, event.getDate());
			ps.setString(4, event.getTime());
			ps.setString(5, event.getCity());
			ps.setString(6, event.getState());
			ps.setInt(7, event.getTotalAvail());
			ps.setDouble(8, event.getPrice());
			ps.setString(9, event.getDescription());
			ps.setString(10, event.getImgFile());
			ps.setString(11, event.getArtist());
			ps.execute();
			
			// get index of inserted event
			rs = st.executeQuery("SELECT LAST_INSERT_ID()");
			rs.next();
			eventID = rs.getInt(1);
			
		}catch(SQLException sqle) {
			System.out.println("SQLException in registerUser. ");
			sqle.printStackTrace();
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(st != null) {
					st.close();
				}
				if(conn != null) {
					conn.close();
				}
			}catch(SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		
		return eventID;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        
        // Read the JSON data from the request body
	    BufferedReader reader = request.getReader();
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        sb.append(line);
	    }
	    String jsonString = sb.toString();
	    
	    // Print the JSON data received from the client
	    System.out.println("Received JSON: " + jsonString);
	    
	    Event event = new Gson().fromJson(jsonString, Event.class);
	    
	    int eventID = registerEvent(event);
	    
	    Gson gson = new Gson();
	    
	    if(eventID == -1) { // this means error occurred
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Event could not be added";
			pw.write(gson.toJson(error));
			pw.flush();
	    }else {
			response.setStatus(HttpServletResponse.SC_OK);
			pw.write(gson.toJson(eventID));
			pw.flush();
	    }
	}
}

class Event {
	private String name;
	private int organizer_id;
	private String date;
	private String time;
	private String city;
	private String state;
	private int totalAvail;
	private double price;
	private String description;
	private String imgFile;
	private String artist;
	
	public Event(String name, int organizer_id, String date, String time, String city, String state, int totalAvail, double price, String description, String imgFile, String artist) {
		this.name = name;
		this.organizer_id = organizer_id;
		this.time = time;
		this.city = city;
		this.state = state;
		this.totalAvail = totalAvail;
		this.price = price;
		this.description = description;
		this.imgFile = imgFile;
		this.artist = artist;
	}

	public String getName() {
		return name;
	}


	public int getOrganizer() {
		return organizer_id;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public int getTotalAvail() {
		return totalAvail;
	}


	public double getPrice() {
		return price;
	}


	public String getDescription() {
		return description;
	}


	public String getImgFile() {
		return imgFile;
	}

	public String getArtist() {
		return artist;
	}	
}
