package src;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AccessibleLevelChecker implements ILevelChecking {
    private String filePath;
    private String logFilePath = "ErrorLog.txt";
    private FileWriter fileWriter = null;

    public AccessibleLevelChecker(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean check(String maze) {
        ArrayList<Integer> list = new ArrayList<>();
        String[] lines = maze.split("\n");
        int row = 0;
        boolean checkLevel = true;

        HashMap<String, ArrayList<Point>> pillGoldUnaccessible = new HashMap<>();


//        System.out.println("Maximum rows is " + lines.length);
        for (String line : lines) {
//            System.out.println("Maximum cols is " + line.length());
            for (int col = 0; col < line.length(); col++) {
                char c = line.charAt(col);
                if (c != 'x') {
                    continue;
                }
                if (row == 0 && col == 0){
                    continue;
                }
//                System.out.println("Coordinates are: "+ row + " " + col);
//                --->
                for (int rectangleWidth = 3; rectangleWidth < line.length() - 3; rectangleWidth++) {
//                    System.out.println("Rectanglewidth of "+ rectangleWidth);
//                    [--->]T
                    for (int rectangleHeight = 3; rectangleHeight < lines.length - 3; rectangleHeight++) {
//                        System.out.println("RectangleHeight of "+ rectangleHeight);
                        if (row + rectangleHeight >= lines.length || col + rectangleWidth >= line.length()){
                            continue;
                        }

                        HashMap<String, ArrayList<Point>> unaccessibleItems = isRectangle(lines, row, col, rectangleWidth, rectangleHeight);
                        boolean isolated = unaccessibleItems.size() >= 1;
                        if (isolated){
//                            System.out.println("UNACCESSIBLE");
//                            System.out.println(unaccessibleItems);
                            for (String string: unaccessibleItems.keySet()){
                                if (!pillGoldUnaccessible.containsKey(string)){
                                    pillGoldUnaccessible.put(string, new ArrayList<>());
                                }
                                for (Point point: unaccessibleItems.get(string)){
                                    if (!pillGoldUnaccessible.get(string).contains(point)){
                                        pillGoldUnaccessible.get(string).add(point);
                                    }
                                }
                            }
                            checkLevel = false;
                        }
                    }
                }
            }
            row ++;
        }
//        System.out.println(list);

        if (pillGoldUnaccessible.size() == 0){
            return checkLevel;
        }

        for(String string: pillGoldUnaccessible.keySet()){
            try {
                fileWriter = new FileWriter(logFilePath, true);
//                    bufferedWriter = new BufferedWriter(fileWriter);
//                    fileWriter.write();
//                    System.out.println("Level "+ this.filePath+ " - portal "+ portal + " count is not 2: "+ this.convertToString(locations));
                fileWriter.write("Level "+ this.filePath+ " - "+ string + " not accessible: "+ this.convertToString(pillGoldUnaccessible.get(string)));
                fileWriter.write("\n");
//                    bufferedWriter.newLine();

//                    bufferedWriter.close();
                fileWriter.close();
//                System.out.println("PRINTOUT SUCCESSFULLY");
            } catch (IOException e) {
                e.printStackTrace();
            }
//            System.out.println();

        }
        return checkLevel;


    }

    public HashMap<String, ArrayList<Point>> isRectangle(String[] lines, int x, int y, int width, int height){
        int max_row = x + width;
        int max_col = y + height;
        ArrayList<Character> list = new ArrayList<>();
        boolean signal = true;
        boolean containPills = false;

        HashMap<String, ArrayList<Point>> pillGoldUnaccessible = new HashMap<>();
//      pill index is 0, gold is 1

//        System.out.println(" Change: "+ max_row + ", "+max_col);
        for (int row_ind = x; row_ind < x + height; row_ind ++){
            for (int col_ind = y; col_ind < y + width; col_ind ++){

//                System.out.println(lines[row_ind]);
//                if row_ind = x; append everything, all the symbols of the line
//                if row_ind > x and row_ind < x + height -1; just append the first and end elements
//                   in here check if any character either equals gold or pills, then change containpills = true
//                this case [row_ind][y; y+ width - 1]
//                else if row_ind = x + height - 1; append everything

                if (row_ind == x){
                    if(lines[row_ind].charAt(col_ind) != 'x'){
                        signal = false;
                    }
                    list.add(lines[row_ind].charAt(col_ind));
                }

                else if (row_ind > x && row_ind < x + height - 1){

                    if(col_ind == y || col_ind == y+width - 1){
                        if(lines[row_ind].charAt(col_ind) != 'x'){
                            signal = false;
//                        return false;
                        }
                        list.add(lines[row_ind].charAt(col_ind));
                    }

                    else{
                        if(lines[row_ind].charAt(col_ind) == '.' || lines[row_ind].charAt(col_ind) == 'g'  ){
                            if(lines[row_ind].charAt(col_ind) == '.'){
                                if(!pillGoldUnaccessible.containsKey("Pill")){
                                    pillGoldUnaccessible.put("Pill", new ArrayList<>());
                                    pillGoldUnaccessible.get("Pill").add(new Point(row_ind + 1, col_ind + 1));
                                }
                                else{
                                    pillGoldUnaccessible.get("Pill").add(new Point(row_ind + 1, col_ind + 1));
                                }
                            }

                            else if(lines[row_ind].charAt(col_ind) == 'g'){
                                if(!pillGoldUnaccessible.containsKey("Gold")){
                                    pillGoldUnaccessible.put("Gold", new ArrayList<>());
                                    pillGoldUnaccessible.get("Gold").add(new Point(row_ind + 1, col_ind+ 1));

                                }
                                else{
                                    pillGoldUnaccessible.get("Gold").add(new Point(row_ind+ 1, col_ind+ 1));
                                }
                            }
                            containPills = true;
                        }
                    }
                }

                else if (row_ind == x + height - 1){
                    if(lines[row_ind].charAt(col_ind) != 'x'){
                        signal = false;
                    }
                    list.add(lines[row_ind].charAt(col_ind));
                }
            }
        }

//        if signal and containPills:  return true; else return false
        if (signal && containPills){
            return pillGoldUnaccessible;
        }

        else{
            return new HashMap<String, ArrayList<Point>>();
        }
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
