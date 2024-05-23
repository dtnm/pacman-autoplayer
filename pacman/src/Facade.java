package src;

import src.matachi.mapeditor.editor.Controller;
import src.utility.GameCallback;
import src.utility.PropertiesLoader;

import java.io.*;
import java.util.*;

public class Facade {

    private MapBuilder builder;
    private GameChecker gameChecker;
    private ILevelChecking levelChecker;
//    private
    public Facade(String filePath) throws IOException {

        //      handle None version
        if (filePath.equals("")){
//                edit mode and visualization only to a blank map
            builder = new MapBuilder();
            builder.buildMap(filePath);
        }
//      fileMap
        else if(filePath.endsWith(".xml")){
//                edit mode and visualization only to the current map
            builder = new MapBuilder();
            builder.buildMap(filePath);
        }
//      folder
        else{
//            map = new Controller(filePath, "xml");
//            folder check + level check implement in this part
//            builder = new MapBuilder();
//            builder.buildMap(filePath);
            File folder = new File(filePath);
            try {
                FileWriter fileWriter = new FileWriter("ErrorLog.txt");
                fileWriter.write("");
                fileWriter.close();

//                System.out.println("File content cleared successfully.");
            } catch (IOException e) {
                System.err.println("An error occurred while trying to clear the file.");
                e.printStackTrace();
            }

//            return XML files only
            File[] listOfFiles = folder.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isFile() && Character.isDigit(file.getName().charAt(0)) &&file.getName().endsWith(".xml");
                }
            });

            ArrayList<File> fileList = new ArrayList<>(Arrays.asList(listOfFiles));

//           Game check ~ folder checking
            gameChecker = new GameChecker(fileList, filePath);
            boolean gameCheck = gameChecker.check(fileList);

//            game check fails,
            if (!gameCheck){
//                edit mode and visualization only
                builder = new MapBuilder();
                builder.buildMap("");
            }

//            valid folder
            else{
//              Level checking
                // filter valid files, starting with a number, checked with folder
                ArrayList<String> filteredList = new ArrayList<>();
                for (File file: fileList){
                    if (file.getName().isEmpty()){
                        continue;
                    }
                    char firstChar = file.getName().charAt(0);
                    if (Character.isDigit(firstChar)){
                        filteredList.add(file.getName());
                    }
                }

//                sort to play from map 1 to map n

                 Collections.sort(filteredList, new Comparator<String>() {
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

                PortalDict mainDict = new PortalDict();
                for (String file: filteredList){
                    IReadingFile fileAdapter = ReadingFileFactory.getInstance().getFileAdapter("xml");
                    String maze = fileAdapter.convertToStringMaze(filePath+"/"+file, mainDict);

                    levelChecker = LevelCheckingFactory.getInstance().getLevelChecker("torusverse", mainDict, file);
                    boolean testMode = levelChecker.check(maze);

//                    level checking fails
                    if (!testMode){
                        builder = new MapBuilder();
                        builder.buildMap(filePath+"/"+file);
//                        System.out.println("Stuck");
                        break;
                    }

                    else {
                        GameCallback gameCallback = new GameCallback();

                        //System.out.println(file);
                        Game game = new Game(gameCallback, file, mainDict);

//                        break;
                    }



                }
            }


        }
    }




}
