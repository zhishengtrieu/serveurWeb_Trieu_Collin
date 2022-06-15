import java.io.File;

public class IndexOf {

    public static byte[] indexOf(Config config, File f) {
        String root = config.getRoot();
        String html = "<html><head><title>Index of "+root+"/</title></head><body><h1>Index of "+root+"/</h1><br><table>";

        File[] files = f.listFiles();

        for (File file : files) {
            if (file.isDirectory() && config.isIndex()) {
                html+="<tr><td><img src=\"/img/folder.gif\" alt=\"[DIR]\"></td><td><a href=\"/"+file.getName()+"/\">"+file.getName()+"/</a></td></tr>";
            } else {
                html+="<tr><td><img src=\"/img/file.gif\" alt=\"[FILE]\"></td><td><a href=\""+file.getName()+"\">"+file.getName()+"</a></td></tr>";
            }
        }

        html+= "</table></body></html>";
        return html.getBytes();
    }


}
