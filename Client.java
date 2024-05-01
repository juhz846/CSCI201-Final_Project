import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import com.google.gson.Gson;

public class Client {

    private static final String SERVER_URL = "http://localhost:8080/UserLoginServlet";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        User user = new User("username", "password", null, 0.0); 
        try {
            String json = gson.toJson(user);
            HttpRequest request = HttpRequest.newBuilder()
                                             .uri(URI.create(SERVER_URL))
                                             .header("Content-Type", "application/json")
                                             .POST(BodyPublishers.ofString(json))
                                             .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response status: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
