// PacGrid.java
package src;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import ch.aplu.jgamegrid.*;

public class PacManGameGrid
{
  private int nbHorzCells;
  private int nbVertCells;
  private int[][] mazeArray;
  // create unique number enumerate for each new file created to store properties type
  private int fileCounter = 1;

  private String filePath;

  public PacManGameGrid(int nbHorzCells, int nbVertCells)
  {
    this.nbHorzCells = nbHorzCells;
    this.nbVertCells = nbVertCells;
    mazeArray = new int[nbVertCells][nbHorzCells];
    String maze =
      "xxxxxxxxxxxxxxxxxxxx" + // 0
      "x....x....g...g....x" + // 1
      "xgxx.x.xxxxxx.x.xx.x" + // 2
      "x.x.......i.g....x.x" + // 3
      "x.x.xx.xx  xx.xx.x.x" + // 4
      "x......x    x......x" + // 5
      "x.x.xx.xxxxxx.xx.x.x" + // 6
      "x.x......gi......x.x" + // 7
      "xixx.x.xxxxxx.x.xx.x" + // 8
      "x...gx....g...x....x" + // 9
      "xxxxxxxxxxxxxxxxxxxx";// 10


    // Copy structure into integer array
    for (int i = 0; i < nbVertCells; i++)
    {
      for (int k = 0; k < nbHorzCells; k++) {
        int value = toInt(maze.charAt(nbHorzCells * i + k));
        mazeArray[i][k] = value;
      }
    }
  }

  // for reading new file loaded and concerting
//  map is just a single xml file: test3.xml instead of test/test3.xml
  public PacManGameGrid(String map, PortalDict protals){

    String maze = "";
    this.filePath = map;
//    map/filePath should have been xml files

//    adjust map to test/test3.xml
    map = "test" + File.separator + map;
//    properties/test3.properties
    String filePathProperties = PropertiesFileCheck();
//    System.out.println(filePathProperties);
    try {
      // Parse XML file
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      File xmlFile = new File(map);
      Document document = builder.parse(xmlFile);


      // Get the width and height
      NodeList sizeElements = document.getElementsByTagName("size");
      Element sizeElement = (Element) sizeElements.item(0);
      NodeList sizeChildNodes = sizeElement.getChildNodes();
      String width = "";
      String height = "";
      for (int i = 0; i < sizeChildNodes.getLength(); i++) {
        Node sizeChildNode = sizeChildNodes.item(i);
        if (sizeChildNode.getNodeType() == Node.ELEMENT_NODE) {
          Element element = (Element) sizeChildNode;
          if (element.getTagName().equals("width")) {
            width = element.getTextContent();
            this.nbHorzCells = Integer.parseInt(width);
          } else if (element.getTagName().equals("height")) {
            height = element.getTextContent();
            this.nbVertCells = Integer.parseInt(height);
          }
        }
      }
      mazeArray = new int[nbVertCells][nbHorzCells];

//      System.out.println("RIGHTHERE");


//      problem with file extractions


      // get tile
      NodeList rowList = document.getElementsByTagName("row");
      int row = 0;
      for (int i = 0; i < rowList.getLength(); i++) {
        //row ++;
        Element rowElement = (Element) rowList.item(i);
        NodeList cellList = rowElement.getElementsByTagName("cell");
        String rowStr = "";
        int column = 0;
        for (int j = 0; j < cellList.getLength(); j++) {
          //column++;
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
            rowStr+= " ";
            writePropertiesToFile(filePathProperties,"Troll", column, row);
          }else if (cell.equals("TX5Tile")) {
            rowStr += " ";
            writePropertiesToFile(filePathProperties, "TX5", column, row);
          }else if (cell.equals("PacTile")) {
            rowStr += ".";
            writePropertiesToFile(filePathProperties, "PacMan", column, row);
          }else if (cell.startsWith("Portal")){
            rowStr += protals.assignValue(cell);
          }
          column++;
        }
        maze += rowStr;
        row++;
      }

      writeSeedToFile(filePathProperties, 30006);
      // change the arguement passed in later
      boolean isAuto = writePacmanAutoToFile(filePathProperties,false);
      if (isAuto == true){
        addValueForPacManAuto(filePathProperties, 'R', 0,0,0,0);
      }


    } catch (Exception e) {
      e.printStackTrace();
    }

    //System.out.print("HELLO");


//    System.out.print(maze);
//    maze =
//            "xxxxxxxxxxxxxxxxxxxx" + // 0
//                    "x....x....g...g....x" + // 1
//                    "xgxx.x.xxxxxx.x.xx.x" + // 2
//                    "x.x.......i.g....x.x" + // 3
//                    "x.x.xx.xx  xx.xx.x.x" + // 4
//                    "x......x    x......x" + // 5
//                    "x.x.xx.xxxxxx.xx.x.x" + // 6
//                    "x.x......gi......x.x" + // 7
//                    "xixx.x.xxxxxx.x.xx.x" + // 8
//                    "x...gx....g...x....x" + // 9
//                    "xxxxxxxxxxxxxxxxxxxx";// 10
    // Copy structure into integer array
    for (int i = 0; i < nbVertCells; i++)
    {
      for (int k = 0; k < nbHorzCells; k++) {
        int value = toInt(maze.charAt(nbHorzCells * i + k), protals);
        mazeArray[i][k] = value;
      }
    }

    //this.fileCounter++;

  }

  public void writePropertiesToFile(String filePath, String cellType, int x, int y) {

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      writer.write(cellType + ".location=" + x + "," + y + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeSeedToFile(String filePath, int value){
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      writer.write("seed=" + value + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // return true if PacManisAuto is true
//  no longer always returns false
  public boolean writePacmanAutoToFile(String filePath, boolean isAuto){
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      if (isAuto == true) {
        writer.write("PacMan.isAuto=" + true + "\n");
        return true;
      }else{
        writer.write("PacMan.isAuto=" + true + "\n" +
                "PacMan.move=\n" +
                "Pills.location=\n" +
                "Gold.location=");
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return isAuto;
  }

  public void addValueForPacManAuto(String filePath, char pacMove, int pillX, int pillY, int goldX, int goldY){
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      writer.write("PacMan.move=" + pacMove + ",");
      writer.write("Pills.location=" + pillX + "," + pillY + ";");
      writer.write("Gold.location=" + goldX + "," + goldY + ";");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // check if the filename has alreaedy exist or not
  // if exist then increment, else publish the file
  // return string filepath
  public String PropertiesFileCheck(){
    // create file
    String folderPath = "properties";
    String fileName = "test" + filePath.substring(0, filePath.length() - 4) + ".properties";
    String filePath = folderPath + File.separator + fileName;

    File file = new File(filePath);
    while(file.exists()){
      try {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("");  // Delete the existing text by writing an empty string
        fileWriter.close();
        return filePath;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
//    while (file.exists()) {
//      // File already exists, delete the current text in the file and return the filepath
////      fileCounter++;
//      fileName = "test" + fileCounter + ".properties";
//      filePath = folderPath + File.separator + fileName;
//      file = new File(filePath);
//    }

    return filePath;
  }

  public int getCell(Location location)
  {
    return mazeArray[location.y][location.x];
  }
  private int toInt(char c)
  {
    if (c == 'x')
      return 0;
    if (c == '.')
      return 1;
    if (c == ' ')
      return 2;
    if (c == 'g')
      return 3;
    if (c == 'i')
      return 4;
    return -1;
  }

  private int toInt(char c, PortalDict portal)
  {
    if (c == 'x')
      return 0;
    if (c == '.')
      return 1;
    if (c == ' ')
      return 2;
    if (c == 'g')
      return 3;
    if (c == 'i')
      return 4;
    if (portal.checkExistPortal("PortalYellowTile") && c == portal.getDefinition("PortalYellowTile")){
      return 5;
    }
    if (portal.checkExistPortal("PortalWhiteTile") && c == portal.getDefinition("PortalWhiteTile")){
      return 6;
    }
    if (portal.checkExistPortal("PortalDarkGoldTile") && c == portal.getDefinition("PortalDarkGoldTile")){
      return 7;
    }
    if (portal.checkExistPortal("PortalDarkGrayTile") && c == portal.getDefinition("PortalDarkGrayTile")){
      return 8;
    }
    return -1;
  }


}
