import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThread implements Runnable{

    private static final ExecutorService executor = Executors.newFixedThreadPool(50); // Pool with 4 threads

    public static void runTask(Runnable task) {
        executor.submit(task);
    }

    public static void shutdown() {
        executor.shutdown();
    }
	public void run() {
		// TODO Auto-generated method stub
		
	}
}