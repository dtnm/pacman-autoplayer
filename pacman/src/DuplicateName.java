package src;

import src.utility.GameCallback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DuplicateName implements  IGameCheckingStrategy{
    private String filePath;
    private String logFilePath = "ErrorLog.txt";
    private FileWriter fileWriter = null;
//    private GameCallback gameCallback;
    public DuplicateName(String filePath) {
//        gameCallback = new GameCallback();
        this.filePath = filePath;

    }

    @Override
    public boolean check(ArrayList<File> fileList) {
        if (fileList.size() == 0){
            return false;
        }

        ArrayList<String>fileStrings = new ArrayList<>();

        for (File file: fileList){
            fileStrings.add(file.getName());
        }



        ArrayList<String> filteredFileStrings = new ArrayList<>();
        for (String file: fileStrings){
            if (file.isEmpty()){
                continue;
            }
            char firstChar = file.charAt(0);
            if (Character.isDigit(firstChar)){
                filteredFileStrings.add(file);
            }
        }

        try {
            if (filteredFileStrings.isEmpty()) {
                return false;
            }
            Collections.sort(filteredFileStrings, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    String num1 = s1.split("\\D+")[0];
                    String num2 = s2.split("\\D+")[0];
                    try {
                        int n1 = Integer.parseInt(num1);
                        int n2 = Integer.parseInt(num2);
                        return Integer.compare(n1, n2);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }
            });
        } catch (IllegalArgumentException e) {
            return false;
        }


//        HashMap for level: {level: ArrayList}
//        {1: [1map.xml, 1sdf.xml], ...}
        HashMap<Integer, ArrayList<String>> levelDict = new HashMap<>();
        boolean noDuplicate = true;
        for (String file: filteredFileStrings){
            String fileNum = file.split("\\D+")[0];
            if (fileNum.equals("")){
                continue;
            }
            int num = Integer.parseInt(fileNum);
            if (!levelDict.containsKey(num)){
                levelDict.put(num, new ArrayList<String>());
                levelDict.get(num).add(file);
            }
            else{
                levelDict.get(num).add(file);
            }
        }

//        check duplicate files
        for (Integer index: levelDict.keySet()){
            if (levelDict.get(index).size() > 1){
                try {
                    fileWriter = new FileWriter(logFilePath, true);
                    fileWriter.write(this.filePath + " - multiple maps at same level: "+ this.convertToString(levelDict.get(index)));
                    fileWriter.write("\n");
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                noDuplicate = false;
            }
        }


        return noDuplicate;
    }

    public String convertToString(ArrayList<String> list){
        String sentence = "";
        for (String ele: list){
            sentence += ele;
            sentence += "; ";
        }
        return sentence.substring(0, sentence.length() - 2);
    }
}
