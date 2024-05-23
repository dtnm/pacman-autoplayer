package src;

import java.io.File;
import java.util.ArrayList;

public class CompositeChecking implements IGameCheckingStrategy{
    private ArrayList<IGameCheckingStrategy> rules;
    @Override
    public boolean check(ArrayList<File> fileList) {
        for (IGameCheckingStrategy rule: rules){
            if (rule.check(fileList) == false){
                return false;
            }
        }
        return true;
    }

    public CompositeChecking(){
        rules = new ArrayList<>();
    }

    public void addRule(IGameCheckingStrategy rule){
        rules.add(rule);
    }
}
