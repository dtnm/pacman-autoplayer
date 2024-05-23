package src;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import src.matachi.mapeditor.editor.Controller;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class XMLAdapter implements IReadingFile{
    @Override
    public void loadFileMap(String fileMap, Controller controller) {

        SAXBuilder builder = new SAXBuilder();
        try {

            File selectedFile;
            BufferedReader in;
            FileReader reader = null;

            Document document;
            selectedFile = new File(fileMap);
//            System.out.println(selectedFile);
            if (selectedFile.canRead() && selectedFile.exists()) {
                document = (Document) builder.build(selectedFile);

                Element rootNode = document.getRootElement();

                List sizeList = rootNode.getChildren("size");
                Element sizeElem = (Element) sizeList.get(0);
                int height = Integer.parseInt(sizeElem
                        .getChildText("height"));
                int width = Integer
                        .parseInt(sizeElem.getChildText("width"));
                controller.updateGrid(width, height);

                List rows = rootNode.getChildren("row");
                for (int y = 0; y < rows.size(); y++) {
                    Element cellsElem = (Element) rows.get(y);
                    List cells = cellsElem.getChildren("cell");

                    for (int x = 0; x < cells.size(); x++) {
                        Element cell = (Element) cells.get(x);
                        String cellValue = cell.getText();

                        char tileNr = 'a';
                        if (cellValue.equals("PathTile"))
                            tileNr = 'a';
                        else if (cellValue.equals("WallTile"))
                            tileNr = 'b';
                        else if (cellValue.equals("PillTile"))
                            tileNr = 'c';
                        else if (cellValue.equals("GoldTile"))
                            tileNr = 'd';
                        else if (cellValue.equals("IceTile"))
                            tileNr = 'e';
                        else if (cellValue.equals("PacTile"))
                            tileNr = 'f';
                        else if (cellValue.equals("TrollTile"))
                            tileNr = 'g';
                        else if (cellValue.equals("TX5Tile"))
                            tileNr = 'h';
                        else if (cellValue.equals("PortalWhiteTile"))
                            tileNr = 'i';
                        else if (cellValue.equals("PortalYellowTile"))
                            tileNr = 'j';
                        else if (cellValue.equals("PortalDarkGoldTile"))
                            tileNr = 'k';
                        else if (cellValue.equals("PortalDarkGrayTile"))
                            tileNr = 'l';
                        else
                            tileNr = '0';

                        controller.getModel().setTile(x, y, tileNr);
                    }

                    String mapString = controller.getModel().getMapAsString();
                    controller.getGrid().redrawGrid();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String convertToStringMaze(String filePath, PortalDict protals){
        String maze = "";
//        PortalDict protals = new PortalDict();
//        String filepath = PropertiesFileCheck();
        try {
            // Parse XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File xmlFile = new File(filePath);
            org.w3c.dom.Document document = builder.parse(xmlFile);

            // Get the width and height
            NodeList sizeElements = document.getElementsByTagName("size");
            org.w3c.dom.Element sizeElement = (org.w3c.dom.Element) sizeElements.item(0);
            NodeList sizeChildNodes = sizeElement.getChildNodes();
            String width = "";
            String height = "";
            for (int i = 0; i < sizeChildNodes.getLength(); i++) {
                Node sizeChildNode = sizeChildNodes.item(i);
                if (sizeChildNode.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) sizeChildNode;
                    if (element.getTagName().equals("width")) {
                        width = element.getTextContent();
//                        this.nbHorzCells = Integer.parseInt(width);
                    } else if (element.getTagName().equals("height")) {
                        height = element.getTextContent();
//                        this.nbVertCells = Integer.parseInt(height);
                    }
                }
            }
//            mazeArray = new int[nbVertCells][nbHorzCells];

            // get tile
            NodeList rowList = document.getElementsByTagName("row");
            int row = 0;
            for (int i = 0; i < rowList.getLength(); i++) {
                row ++;
                org.w3c.dom.Element rowElement = (org.w3c.dom.Element) rowList.item(i);
                NodeList cellList = rowElement.getElementsByTagName("cell");
                String rowStr = "";
                int column = 0;
                for (int j = 0; j < cellList.getLength(); j++) {
                    column++;
                    String cell = cellList.item(j).getTextContent();
                    if (cell.equals("WallTile")){
                        rowStr += "x";
                    }else if (cell.equals("PathTile")){
                        rowStr += " ";
                    }else if(cell.equals("PillTile")){
                        rowStr += ".";
                    }else if (cell.equals("GoldTile")){
                        rowStr += "g";
                    }else if (cell.equals("IceTile")){
                        rowStr += "i";
                    }else if (cell.equals("TrollTile")){
                        rowStr += "m";
//                        System.out.println("Troll:" + row + ","+ column);
//                        writePropertiesToFile(filepath,"Troll", row, column);
                    }else if (cell.equals("TX5Tile")) {
                        rowStr += "m";
//                        System.out.println("Tx5:" + row + ","+ column);
//                        writePropertiesToFile(filepath, "Tx5", row, column);
                    }else if (cell.equals("PacTile")) {
                        rowStr += "p";
//                        System.out.println("Pacman:" + row + ","+ column);
//                        writePropertiesToFile(filepath, "Pacman", row, column);
                    }else if (cell.startsWith("Portal")){
                        rowStr += protals.assignValue(cell);
                    }
                }
                maze += rowStr;
                maze += "\n";
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return maze;
    }


}
