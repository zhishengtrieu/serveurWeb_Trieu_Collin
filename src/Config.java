import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class Config {
    private int port_number;
    private String root;
    private boolean index;
    private InetAddress accept;
    private InetAddress reject;

    public Config(String nomF) {
        this.port_number = 8080;
        this.root = null;
        this.index = false;
        this.accept = null;
        this.reject = null;
        try {
            File xmlFile = new File(nomF);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            this.port_number = Integer.parseInt(doc.getElementsByTagName("port").item(0).getTextContent());
            this.root = doc.getDocumentElement().getAttribute("root");
            this.index = Boolean.parseBoolean(doc.getDocumentElement().getAttribute("index"));
            this.accept = InetAddress.getByName(doc.getDocumentElement().getAttribute("accept"));
            this.reject = InetAddress.getByName(doc.getDocumentElement().getAttribute("reject"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    public InetAddress getReject() {
        return reject;
    }

    public int getPort_number() {
        return port_number;
    }

    public String getRoot() {
        return root;
    }

    public boolean isIndex() {
        return index;
    }

    public InetAddress getAccept() {
        return accept;
    }
}
