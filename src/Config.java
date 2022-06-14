import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;

public class Config {
    private int port_number;
    private String root;
    private boolean index;
    private String accept;
    private String reject;

    public Config(String nomF) {
        this.port_number = 8080;
        this.root = "";
        this.index = false;
        this.accept = null;
        this.reject = null;
        try {
            File xmlFile = new File(nomF);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            this.port_number = Integer.parseInt(doc.getElementsByTagName("port").item(0).getTextContent());
            this.root = doc.getElementsByTagName("root").item(0).getTextContent();
            this.index = Boolean.parseBoolean(doc.getElementsByTagName("index").item(0).getTextContent());
            this.accept = doc.getElementsByTagName("accept").item(0).getTextContent();
            String[] temp = this.accept.split("/");
            this.accept = temp[0];
            this.reject = doc.getElementsByTagName("reject").item(0).getTextContent();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    public String getReject() {
        return this.reject;
    }

    public int getPort_number() {
        return this.port_number;
    }

    public String getRoot() {
        return this.root;
    }

    public boolean isIndex() {
        return this.index;
    }

    public String getAccept() {
        return this.accept;
    }
}
