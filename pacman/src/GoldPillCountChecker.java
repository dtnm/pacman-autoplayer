package src;

import src.utility.GameCallback;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GoldPillCountChecker implements ILevelChecking{
    private String filePath;
    private String logFilePath = "ErrorLog.txt";
    private FileWriter fileWriter = null;

    public GoldPillCountChecker(String filePath){
        this.filePath = filePath;
        try {
            fileWriter = new FileWriter(new File(logFilePath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public boolean check(String maze) {
        int count = 0;

        String[] lines = maze.split("\n");
        int row = 1;
        for (String line: lines){
            for (int col = 0; col < line.length(); col ++){
                char c = line.charAt(col);
                if (c == '.' || c == 'g'){
                    count ++;
                }
            }
            row ++;
        }


        if (count < 2){
             try {
                fileWriter = new FileWriter(logFilePath, true);
                fileWriter.write("Level " + this.filePath+ " - less than 2 Gold and Pill");
                fileWriter.write("\n");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;

    }
}
