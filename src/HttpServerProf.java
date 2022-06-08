import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;
import java.util.Scanner;

/**
 * https://javarevisited.blogspot.com/2015/06/how-to-create-http-server-in-java-serversocket-example.html
 * 
 * @author wiart1
 *
 */
public class HttpServerProf {

	public static void main(String[] args) throws IOException {
		int port = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Application :");
		System.out.println("1 - Lecture requ�te http");
		System.out.println("2 - Envoyer une r�ponse");
		System.out.println("3 - page HTML");

		int choix = sc.nextInt();
		switch (choix) {
		case 1:
			lireRequete(port);
			break;
		case 2:
			envoyerReponse(port);
			break;
		case 3:
			requeteHtml(port);
			break;
		default:
			System.out.println("pas un choix disponible");
			return;

		}

	}

	public static void lireRequete(int port) throws IOException {
		final ServerSocket server = new ServerSocket(port);
		System.out.println("Listening for connection on port  " + port + " ....");
		final Socket clientSocket = server.accept();
		while (true) {
			
			InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			String line = reader.readLine();
			while (!line.isEmpty()) {
				System.out.println(line);
				line = reader.readLine();
			}
		}

	}
	
	private static void envoyerReponse(int port) throws IOException {
		ServerSocket server = new ServerSocket(port);
		System.out.println("Listening for connection on port  " + port + "  ....");
		while (true) {
			try (Socket socket = server.accept()) {
				Date today = new Date();
				String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
				socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
				socket.close();
			}
		}

	}

	private static void requeteHtml(int port) throws IOException {
		final ServerSocket server = new ServerSocket(port);
		System.out.println("Listening for connection on port " + port + " ....");
		while (true) {
			try (Socket socket = server.accept()) {
				InputStreamReader isr = new InputStreamReader(socket.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				String line = reader.readLine();

				String text = null;
				String nomFichier = "";
				if (line.contains("GET")) {
					text = line.split(" ")[1];

					for (int i = 0; i < text.length() - 1; i++) {
						nomFichier += text.charAt(i + 1);
					}

					System.out.println(text);
				}
				if (!nomFichier.equals("favicon.ico")) {
					System.out.println(nomFichier);
					FileInputStream f = new FileInputStream("site/"+nomFichier);
					byte[] b = f.readAllBytes();

					String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
					socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
					socket.getOutputStream().write(b);
				}
			}
		}

	}



}
