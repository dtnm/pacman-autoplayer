package src;

import src.matachi.mapeditor.editor.Controller;

public class MapBuilder {
    private Controller map;

    public void buildMap(String filePath){
//      handle None version
        if (filePath.equals("")){
            map = new Controller();
        }
//      fileMap
        else if(filePath.endsWith(".xml")){
            map = new Controller(filePath, "xml");
        }
//      folder
        else{
            map = new Controller(filePath, "xml");
        }

    }
}
