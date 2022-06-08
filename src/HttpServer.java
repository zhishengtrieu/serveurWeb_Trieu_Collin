import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HttpServer {
    private int port_number;
    public final ServerSocket socket;

    public HttpServer() throws IOException {
        this.port_number = 8080;
        this.socket = new ServerSocket(this.port_number);
    }


    public void start() throws IOException {
        while (true) {
            //on recupere les flux d'entree et de sortie des sockets
            Socket socketRecu = this.socket.accept();
            OutputStream outputStream = socketRecu.getOutputStream();

            InputStream inputStream = socketRecu.getInputStream();
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList<String> request = new ArrayList<String>();
            //on recupere la requete
            String received = bfReader.readLine();
            while ((received != null) && (!received.equals(""))) {
                System.out.println(received);
                request.add(received);
                received = bfReader.readLine();
            }

            if (request.size() > 0) {
                String[] tab = request.get(0).split(" ");

                //on recupere le fichier demande
                try {
                    FileInputStream file = new FileInputStream("site" + tab[1]);
                    byte[] response = file.readAllBytes();

                    String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
                    outputStream.write(httpResponse.getBytes("UTF-8"));

                    outputStream.write(response);
                } catch (FileNotFoundException e) {

                }
            }

            bfReader.close();
            socketRecu.close();
        }
    }


    public static void main(String[] args) {
        try {
            HttpServer server = new HttpServer();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
