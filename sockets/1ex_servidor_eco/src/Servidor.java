
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerException;
import java.util.Date;

public class Servidor {
	private int porta = 7000;
	private ServerSocket serverSocket;
	
	public Servidor() throws ServerException, IOException {
		
		serverSocket = new ServerSocket(porta);
		
		System.out.println("Servidor iniciado na porta " + porta);

		while (true) {

			
			Socket s = serverSocket.accept();
			String ip = s.getInetAddress().getHostAddress();
			System.out.println("Conectado com " + ip);

			
			DataInputStream  in  = new DataInputStream(s.getInputStream());
			
			
			DataOutputStream out = new DataOutputStream(s.getOutputStream());

			
			

			String str = "";
			do{	
				str = in.readUTF();
				out.writeUTF("servidor:" + str);
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