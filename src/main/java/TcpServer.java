import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class TcpServer {

	ServerSocket sserver1;
	ServerSocket sserver2;
	Socket s1;
	Socket s2;
	BufferedReader in1;
	BufferedReader in2;
	DataOutputStream out1;
	DataOutputStream out2;
	String messageIN = new String();
	String messageOUT = new String();
	boolean flagServer2Open = false;
	public void openSocket1() throws IOException {
		sserver1 = new ServerSocket(1111);
		s1 = sserver1.accept();
		in1 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
		out1 = new DataOutputStream(s1.getOutputStream());
	}

	public void openSocket2() throws IOException {
		sserver2 = new ServerSocket(2222);
		s2 = sserver2.accept();
		in2 = new BufferedReader(new InputStreamReader(s2.getInputStream()));
		out2 = new DataOutputStream(s2.getOutputStream());

	}
	void forwardStream() throws IOException {

		while(true){

			try {
				String rline = in1.readLine();
				out2.writeBytes(rline);
			} catch (IOException e) {
				out1.writeBytes("Connection with server 2 is down.");
				sserver2.close();
				s2.close();
				flagServer2Open = false;
			} catch (NullPointerException ne){
				if (!s1.isClosed()){
					out2.writeBytes("Connection with server 1 is down.");
					sserver1.close();
					s1.close();
					openSocket1();
				}
			}

			if (!flagServer2Open){
				System.out.println("A");
				openSocket2();
				flagServer2Open = true;
			}

		}

	}

	public static void main(String[] args) {
		TcpServer tcps = new TcpServer();

		try {
			tcps.flagServer2Open = true;
			tcps.openSocket1();
			tcps.openSocket2();
			tcps.forwardStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	  
}
