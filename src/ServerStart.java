import com.chen.server.impl.GameServer;


public class ServerStart {

	public static void main(String[] args) {
		new Thread(GameServer.getInstance()).start();
	}
}
