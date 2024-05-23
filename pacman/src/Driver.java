package src;

import src.utility.GameCallback;
import src.utility.PropertiesLoader;
import src.matachi.mapeditor.editor.Controller;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Driver {
    /**
     * Starting point
     * @param args the command line arguments
     */

    public static void main(String args[]) throws IOException {
        if (args.length > 0) {
            String propertiesPath = args[0];
        }

        String fileMap = "test";
        new Facade(fileMap);
//        new Controller();
    }

}
