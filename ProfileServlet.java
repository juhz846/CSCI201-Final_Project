

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class EventDisplayServlet
 */
@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		
		BufferedReader reader = request.getReader();
		String line;
		StringBuilder requestBody = new StringBuilder();
		while ((line = reader.readLine()) != null) {
		    requestBody.append(line);
		}
		
		
		TypeUser tu = new Gson().fromJson(requestBody.toString(), TypeUser.class);
		
		String type = tu.type;

		int ID = tu.ID;
		String json = "";
		if (type == "user") {
			System.out.println("BBBBBBBBB");
			json = JDBC.getUserEventData(ID);
		}
		else {
			json = JDBC.getOrganizerEventData(ID);
		}
		
		pw.write(json);
		pw.flush();		
	}

}

class TypeUser{
	public int ID;
	public String type;	
}
