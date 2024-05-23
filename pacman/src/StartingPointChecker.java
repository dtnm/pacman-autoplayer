package src;

import src.utility.GameCallback;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// only 1 valid starting point for Pacman
public class StartingPointChecker implements ILevelChecking{

    private String filePath;
    private String logFilePath = "ErrorLog.txt";
    private FileWriter fileWriter = null;

    public StartingPointChecker(String filePath){

        this.filePath = filePath;
    }
    @Override
    public boolean check(String maze) {
        ArrayList<Point> pacManList = new ArrayList<>();

        String[] lines = maze.split("\n");
        int row = 1;
        for (String line: lines){
//            System.out.println(line);
//            System.out.println(line.charAt(12));
            for (int col = 0; col < line.length(); col ++){
                char c = line.charAt(col);
                if (c == 'p'){
//                    System.out.println("EREKA");
                    pacManList.add(new Point(row,col+1));
                }
            }
            row ++;
        }

//        System.out.println(pacManList);
//        System.out.println(pacManList.size());

        if (pacManList.size() == 0){
            try {
                fileWriter = new FileWriter(logFilePath, true);
                fileWriter.write("Level "+ filePath+" - no start for PacMan");
                fileWriter.write("\n");
                fileWriter.close();


            } catch (IOException e) {
                e.printStackTrace();
            }


            //System.out.println("[Level "+ filePath+" - no start for PacMan");
            return false;
        }

        if (pacManList.size() >= 2){
            try {
                fileWriter = new FileWriter(logFilePath, true);
                fileWriter.write("Level " + filePath + " - more than one start for PacMan: " + this.convertToString(pacManList));
                fileWriter.write("\n");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("[Level "+ filePath+" - more than one start for PacMan: "+ this.convertToString(pacManList));
            return false;
        }

        return true;
    }

    public String convertToString(ArrayList<Point> list){
        String sentence = "";
        for (Point ele: list){
            sentence += "(";
            sentence += (int)ele.getY();
            sentence += ",";
            sentence += (int)ele.getX();
            sentence += ")";
            sentence += "; ";
        }
        return sentence.substring(0, sentence.length() - 2);
    }
}
