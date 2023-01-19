
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Servidor {
	private int port = 7000;
	private ServerSocket serverSocket;
	
	List<String> fortunes;
	Random random;
	
	public Servidor() throws ServerException, IOException {
		
		serverSocket = new ServerSocket(port);
	    fortunes = new ArrayList<String>();
		random = new Random();
	    
		while (true) {
		
			Socket s = serverSocket.accept();
		
			DataInputStream  in  = new DataInputStream(s.getInputStream());
			
			DataOutputStream out = new DataOutputStream(s.getOutputStream());

			String str = "";
			String cleanStr = "";
			int index = 0;
			String fortune;
			
			do{	
				str = in.readUTF();
				
				if(str.equals("GET-FORTUNE")) {
					index = random.nextInt(fortunes.size() - 1);
					fortune = fortunes.get(index);
					out.writeUTF(fortune);
				}
				
				else if(str.contains("SET-FORTUNE")) {
					cleanStr = str.replace("SET-FORTUNE", "").replace("\n", "");
					fortunes.add(cleanStr);
				}
				
			}while( !str.equals("exit") ); 

			s.close();
		}

		
	}

	public static void main(String[] args) {
		
		try {
			new Servidor();
		} catch (ServerException e) {
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}