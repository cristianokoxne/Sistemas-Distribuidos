import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) throws IOException {
		
		
		Socket s = new Socket("127.0.0.1", 7000);

		
		DataInputStream in = new DataInputStream(s.getInputStream());
		DataOutputStream os = new DataOutputStream(s.getOutputStream());
		
		Scanner lerTecla = new Scanner(System.in);
		
		String str = "";
		do {
			str = lerTecla.nextLine();
			os.writeUTF(str);
			System.out.println(in.readUTF());
		} while(!str.equals("exit"));

		
		s.close();
	}
}
