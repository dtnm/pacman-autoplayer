package src;

import src.utility.GameCallback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CorrectName implements IGameCheckingStrategy{
    private String filePath;
    private String logFilePath = "ErrorLog.txt";
    private FileWriter fileWriter = null;

    @Override
    public boolean check(ArrayList<File> fileList) {
        if (fileList.size() == 0){
            try {
                fileWriter = new FileWriter(logFilePath, true);
//                fileWriter.write("[Level " + filePath + " - more than one start for PacMan: " + this.convertToString(pacManList));
                fileWriter.write(this.filePath + " - no maps found"  );

                fileWriter.write("\n");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        boolean startWithNumber = false;
        for(File file: fileList){
                if (file.getName().matches("^\\d.*")) {
                    startWithNumber = true;
//                    break;
            }
        }

        if(!startWithNumber){
            try {
                fileWriter = new FileWriter(logFilePath, true);
//                fileWriter.write("[Level " + filePath + " - more than one start for PacMan: " + this.convertToString(pacManList));
                fileWriter.write(this.filePath + " - no maps found"  );

                fileWriter.write("\n");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return startWithNumber;
    }

    public CorrectName(String filePath){

        this.filePath = filePath;
    }
}
