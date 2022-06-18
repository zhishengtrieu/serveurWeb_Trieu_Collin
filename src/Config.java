import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Config {
    private int port_number;
    private String root;
    private boolean index;
    private String accept;
    private String reject;
    public final static String CONFIG_FILE = "config/config.xml";

    public Config() {
        //on initialise les variables avec les valeurs par defaut
        this.port_number = 80;
        this.root = "";
        this.index = false;
        this.accept = null;
        this.reject = null;
        //on parse le fichier xml
        try {
            File xmlFile = new File(CONFIG_FILE);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            this.port_number = Integer.parseInt(doc.getElementsByTagName("port").item(0).getTextContent());
            this.root = doc.getElementsByTagName("root").item(0).getTextContent();
            this.index = Boolean.parseBoolean(doc.getElementsByTagName("index").item(0).getTextContent());
            this.accept = doc.getElementsByTagName("accept").item(0).getTextContent();
            this.reject = doc.getElementsByTagName("reject").item(0).getTextContent();
        } catch (ParserConfigurationException | IOException | SAXException e) {
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
