import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HttpServer {
    private int port_number;
    private Config config;
    public final ServerSocket socket;

    public HttpServer() throws IOException {
        this.config = new Config("config/config.xml");
        this.port_number = this.config.getPort_number();
        this.socket = new ServerSocket(this.port_number);
    }


    public void start() throws IOException {
        while (true) {
            //on recupere les flux d'entree et de sortie des sockets
            Socket socketRecu = this.socket.accept();
            String name = socketRecu.getInetAddress().getHostName();
            InetAddress ip = InetAddress.getByName(name);
            String[] address = ip.toString().split("/");
            String add = address[1];
            System.out.println(add);
            OutputStream outputStream = socketRecu.getOutputStream();

            InputStream inputStream = socketRecu.getInputStream();
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList<String> request = new ArrayList<String>();
            //on essaye de recuperer la requete
            String received = bfReader.readLine();
            while ((received != null) && (!received.equals(""))) {
                System.out.println(received);
                request.add(received);
                received = bfReader.readLine();
            }

            //si on recupere une requete
            if (request.size() > 0) {
                String[] tab = request.get(0).split(" ");
                //on recupere le fichier demande
                try {
                    FileInputStream file = new FileInputStream(this.config.getRoot() + tab[1]);
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
