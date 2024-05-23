package src;

import src.utility.GameCallback;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PortalChecker implements ILevelChecking{

    private Map<String, Integer> dictionary = new HashMap<>();
    private Map<String, ArrayList<Point>> portalLocations = new HashMap<>();

    private PortalDict protals;
    private String filePath;
    private String logFilePath = "ErrorLog.txt";
    private FileWriter fileWriter = null;
    private BufferedWriter bufferedWriter = null;

    public PortalChecker(PortalDict protals, String filePath){
        this.protals = protals;
        this.filePath = filePath;
//        try {
//            fileWriter = new FileWriter(logFilePath);
//            bufferedWriter = new BufferedWriter(fileWriter);
//        } catch (IOException ex) {
//            ex.printStackTrace();
        }
//    }


    @Override
    public boolean check(String maze) {
//        System.out.println("Portaldict");
//        System.out.println(protals.getWordDefs().keySet());

//        for (String portal: protals.getWordDefs().keySet()){
//            System.out.println(portal);
//            System.out.println(protals.getWordDefs().get(portal));
//        }

//        System.out.println(protals.convertToValue('a'));
        String[] lines = maze.split("\n");
        int row = 1;
        for (String line: lines){
            for (int col = 0; col < line.length(); col ++){
                char c = line.charAt(col);
                String definition = protals.convertToValue(c);
//                System.out.println(definition);
                if (definition != ""){
                    if (!dictionary.containsKey(definition)){
                        dictionary.put(definition, 1);
                        portalLocations.put(definition, new ArrayList<Point>());
                        Point current = new Point(row, col + 1);
                        portalLocations.get(definition).add(current);
//                        portalLocations.put(definition, new Point(row, col + 1));

                    }
                    else{
                        dictionary.put(definition, dictionary.get(definition) + 1);
                        Point point = new Point(row, col + 1);
                        portalLocations.get(definition).add(point);
                    }
                }
            }
            row ++;
        }

        boolean evenCount = true;
        for(String portal: dictionary.keySet()){
            Integer countPortal = dictionary.get(portal);
            if (countPortal != 2 && countPortal != 0){
                evenCount = false;
            }
        }

//        System.out.println(portalLocations);
//        System.out.println(evenCount);
        if (evenCount){
            return evenCount;
        }

        for (String portal: portalLocations.keySet()){
            ArrayList<Point> locations = portalLocations.get(portal);
//            System.out.println(locations);
            if (locations.size() != 2 && locations.size() != 0){
//                System.out.println(locations);
                try {
                    fileWriter = new FileWriter(logFilePath, true);
//                    bufferedWriter = new BufferedWriter(fileWriter);
//                    fileWriter.write();
//                    System.out.println("Level "+ this.filePath+ " - portal "+ portal + " count is not 2: "+ this.convertToString(locations));
                    fileWriter.write("Level "+ this.filePath+ " - portal "+ portal.substring(6, portal.length()-4) + " count is not 2: "+ this.convertToString(locations));
                    fileWriter.write("\n");
//                    bufferedWriter.newLine();

//                    bufferedWriter.close();
                    fileWriter.close();
//                    System.out.println("PRINTOUT SUCCESSFULLY");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //System.out.println("Level "+ this.filePath+ " - portal "+ portal + " count is not 2: "+ this.convertToString(locations));
            }
        }

        return false;

    }

    public String convertToString(ArrayList<Point> list){
        String sentence = "";
        for (Point ele: list){
            sentence += "(";
            sentence += (int) ele.getY();
            sentence += ",";
            sentence += (int) ele.getX();
            sentence += ")";
            sentence += "; ";
        }
        return sentence.substring(0, sentence.length() - 2);
    }
}
