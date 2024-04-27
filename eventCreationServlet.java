import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.nio.file.Files;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.io.*;


@WebServlet("/eventCreationServlet")
@MultipartConfig
public class eventCreationServlet extends HttpServlet {
	
	protected static int registerEvent(Event event, byte[] fileData) {
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
			
			
			
			// INSERT EVENT
			
			ps = conn.prepareStatement("INSERT INTO events (name, organizer_id, event_date, event_time, city, state, total_available, price, description, img_file, artist) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, event.getName());
			ps.setInt(2, event.getOrganizer());
			ps.setString(3, event.getDate());
			ps.setString(4, event.getTime());
			ps.setString(5, event.getCity());
			ps.setString(6, event.getState());
			ps.setInt(7, event.getTotalAvail());
			ps.setDouble(8, event.getPrice());
			ps.setString(9, event.getDescription());
			ps.setBytes(10, fileData);
			ps.setString(11, event.getArtist());
			ps.executeUpdate();
			
			// get index of inserted event
			st = conn.createStatement();
			rs = st.executeQuery("SELECT LAST_INSERT_ID()");
			rs.next();
			eventID = rs.getInt(1);
			
		}catch(SQLException sqle) {
			System.out.println("SQLException in registerEvent. ");
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
        response.setContentType("multipart/form-data");
        
        
        // reading in image file
        Part filePart = request.getPart("file");
        // Get the filename from the Part
        String fileName = filePart.getSubmittedFileName();

        // Get the InputStream of the file data
        InputStream fileContent = filePart.getInputStream();

        // Read the file content into a byte array
        byte[] fileData = new byte[(int) filePart.getSize()];
        fileContent.read(fileData);
        fileContent.close();

		
		// getting the other info
		String eventName = request.getParameter("eventName");
        String eventArtist = request.getParameter("eventArtist");
        String eventOrganizer = request.getParameter("organizerID");
        String eventDate = request.getParameter("eventDate");
        String eventTime = request.getParameter("eventTime");
        String eventCity = request.getParameter("eventCity");
        String eventState = request.getParameter("eventState");
        String totalTickets = request.getParameter("totalTickets");
        String ticketPrice = request.getParameter("ticketPrice");
        String eventDescription = request.getParameter("eventDescription");
	    

        Event event = new Event(eventName, Integer.parseInt(eventOrganizer), eventDate, eventTime, eventCity, eventState, Integer.parseInt(totalTickets), Double.parseDouble(ticketPrice), eventDescription, fileName, eventArtist);
        
        
        
	    int eventID = registerEvent(event, fileData);
	    
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
		this.date = date;
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
	
	public void setImgFile(String imgFile) {
		this.imgFile = imgFile;
	}

	public String getArtist() {
		return artist;
	}	
}
