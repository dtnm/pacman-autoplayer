package src;

import java.util.ArrayList;

public class CompositeLevelChecking implements ILevelChecking{
    private ArrayList<ILevelChecking> rules = new ArrayList<>();
    public void addRule(ILevelChecking rule){
        rules.add(rule);
    }

    @Override
    public boolean check(String maze) {
        boolean levelCheck = true;
        for (ILevelChecking rule: rules){
            if (rule.check(maze) == false){
//                System.out.println(rule);
                levelCheck = false;
            }
        }
        return levelCheck;
    }
}
