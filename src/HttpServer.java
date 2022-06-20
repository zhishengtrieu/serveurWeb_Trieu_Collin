import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;

public class HttpServer {
    private int port_number;
    private Config config;
    private ServerSocket socket;

    public HttpServer() throws IOException {
        this.config = new Config();
        this.port_number = this.config.getPort_number();
        this.socket = new ServerSocket(this.port_number);
    }


    public void start() throws IOException {
        //cette boucle permet de traiter les requetes en continu
        while (true) {
            //on met a jour la configuation a chaque iteration
            this.config = new Config();
            int newPort = this.config.getPort_number();
            if (newPort != this.port_number) {
                //si le port a change, on ferme le socket et on en cree un nouveau
                this.port_number = newPort;
                this.socket.close();
                this.socket = new ServerSocket(this.port_number);
            }

            //on recupere les flux d'entree et de sortie des sockets
            Socket socketRecu = this.socket.accept();
            OutputStream outputStream = socketRecu.getOutputStream();
            InputStream inputStream = socketRecu.getInputStream();

            //on essaye de recuperer une requete
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList<String> request = new ArrayList<String>();
            String received = bfReader.readLine();
            while ((received != null) && (!received.equals(""))) {
                request.add(received);
                received = bfReader.readLine();
            }

            //si on recupere une requete
            if (request.size() > 0) {
                //on verifie que l'ip du client n'est pas bannie
                IP gereIP = new IP(socketRecu.getInetAddress(), this.config);
                if (gereIP.accept()) {
                    //si elle n'est pas bannie, on traite la requete
                    //on recupere le nom du fichier que la requete demande
                    String[] tab = request.get(0).split(" ");
                    //on recupere le fichier demande
                    try {
                        byte[] response;
                        File file = new File("/Site/"+ tab[1]);
                        if (!file.isDirectory()) {
                            response = Files.readAllBytes(file.toPath());
                        } else {
                            response = IndexOf.indexOf(this.config, file);
                        }

                        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
                        outputStream.write(httpResponse.getBytes("UTF-8"));
                        outputStream.write(response);
                    } catch (IOException e) {
                        //si on ne trouve pas le fichier on renvoie une erreur 404
                        FileInputStream file = new FileInputStream("/Site/404.html");
                        byte[] response = file.readAllBytes();
                        String httpResponse = "HTTP/1.1 404 Not Found\r\n\r\n";
                        outputStream.write(httpResponse.getBytes("UTF-8"));
                        outputStream.write(response);
                    }
                } else {
                    //si l'ip est bannie on renvoie une erreur 403
                    String httpResponse = "HTTP/1.1 403 Forbidden\r\n\r\n";
                    outputStream.write(httpResponse.getBytes("UTF-8"));
                    outputStream.write("403 Forbidden".getBytes("UTF-8"));
                }
            }

            bfReader.close();
            socketRecu.close();
        }
    }

    public static void creerFichierPid(long pid) {
        try {
            File f = new File("/var/run/myweb.pid");
            PrintWriter fw = new PrintWriter(f);
            fw.println(pid);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {
            HttpServer server = new HttpServer();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long pid = ProcessHandle.current().pid();
        creerFichierPid(pid);
    }


}
