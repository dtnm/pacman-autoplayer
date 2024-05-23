package src;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameChecker {

    private CompositeChecking checker;
    private ArrayList<File> fileList;
    private String filePath;

//    filePath is the directory to the test folder
    public GameChecker(ArrayList<File> fileList, String filePath){
        this.fileList = fileList;
        this.filePath = filePath;
    }

    public boolean check(ArrayList<File> fileList){
//        Define set of rules
        checker = new CompositeChecking();
        IGameCheckingStrategy correctName = new CorrectName(filePath);
        IGameCheckingStrategy duplicateName = new DuplicateName(filePath);

        checker.addRule(correctName);
        checker.addRule(duplicateName);

//        Check for breaking rules
        boolean condition = checker.check(fileList);
//        System.out.println(condition);
        return condition;
    }


}
