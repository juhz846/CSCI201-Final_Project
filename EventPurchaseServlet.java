

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
 * Servlet implementation class EventPurchaseServlet
 */
@WebServlet("/EventPurchaseServlet")
public class EventPurchaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventPurchaseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		BufferedReader reader = request.getReader();
		String line;
		StringBuilder requestBody = new StringBuilder();
		while ((line = reader.readLine()) != null) {
		    requestBody.append(line);
		}
		
		System.out.println(requestBody.toString());
		
		Gson gson = new Gson();
		Ticket ticket = new Gson().fromJson(requestBody.toString(), Ticket.class);
		
		int purchase_success = JDBC.buyTicket(ticket.name, ticket.userID);
		if (purchase_success > 0) {
			response.setStatus(HttpServletResponse.SC_OK);
			String json = "{\"a\": \"Successfully purchased ticket for " + ticket.name+"\"";
			pw.write(gson.toJson(json));
			pw.flush();	
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Unable to purchase ticket";
			pw.write(gson.toJson(error));
			pw.flush();	
		}
		
				
	}
	
	class Ticket {
		public String name;
		public int userID;
	}

}
