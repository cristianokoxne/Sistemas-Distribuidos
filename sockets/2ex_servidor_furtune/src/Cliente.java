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

		os.writeUTF("SET-FORTUNE\nTodas as coisas são difíceis antes de se tornarem fáceis.\n");
		os.writeUTF("SET-FORTUNE\nSe você se sente só é porque construiu muros ao invés de pontes.\n");
		os.writeUTF("SET-FORTUNE\nVencer é 90 por cento suor e 10 por cento de engenho.\n");
		os.writeUTF("SET-FORTUNE\nVocê é do tamanho do seu sonho.\n");
		os.writeUTF("SET-FORTUNE\nPare de procurar eternamente; a felicidade está mesmo aqui ao seu lado.\n");
		os.writeUTF("SET-FORTUNE\nO conhecimento é a única virtude e a ignorância é o único vício.\n");
		os.writeUTF("SET-FORTUNE\nO nosso primeiro e último amor é… o amor-próprio.\n");
		os.writeUTF("SET-FORTUNE\nDeixe de lado as preocupações e seja feliz.\n");
		os.writeUTF("SET-FORTUNE\nNós somos o que pensamos.\n");
		os.writeUTF("SET-FORTUNE\nA maior barreira para o sucesso é o medo do fracasso.\n");
		os.writeUTF("SET-FORTUNE\nO pessimista vê a dificuldade em cada oportunidade; O otimista vê a oportunidade em cada dificuldade.\n");
		os.writeUTF("SET-FORTUNE\nO insucesso é apenas uma oportunidade para recomeçar de novo com mais experiência.\n");
		os.writeUTF("SET-FORTUNE\nCoragem é a resistência ao medo, domínio do medo, e não a ausência do medo.\n");
		os.writeUTF("SET-FORTUNE\nO verdadeiro homem mede a sua força, quando se defronta com o obstáculo.\n");
		os.writeUTF("SET-FORTUNE\nQuem quer vencer um obstáculo deve armar-se da força do leão e da prudência da serpente\n");
		os.writeUTF("SET-FORTUNE\nA adversidade desperta em nós capacidades que, em circunstâncias favoráveis, teriam ficado adormecidas.\n");
		os.writeUTF("SET-FORTUNE\nMotivação não é sinónimo de transformação, mas um passo em sua direção.\n");

		os.writeUTF("GET-FORTUNE");
		String fortune = in.readUTF();
		
		System.out.println("\nSua mensagem é: \n");
		System.out.println(fortune);
		System.out.println("\n  \n");
		
		os.writeUTF("exit");
		s.close();
	}
}
