package src;

public class LevelCheckingFactory {
    public static LevelCheckingFactory instance;

    private ILevelChecking levelChecker;

    public static LevelCheckingFactory getInstance(){
        if (instance == null){
            instance = new LevelCheckingFactory();
        }
        return instance;
    }

    public ILevelChecking getLevelChecker(String version, PortalDict protals, String filePath){
        if (version.equals("torusverse")){
            CompositeLevelChecking composite = new CompositeLevelChecking();
            StartingPointChecker startRule = new StartingPointChecker(filePath);
            GoldPillCountChecker goldPillRule = new GoldPillCountChecker(filePath);
            PortalChecker portalRule = new PortalChecker(protals, filePath);
            AccessibleLevelChecker accessRule = new AccessibleLevelChecker(filePath);

            composite.addRule(startRule);
            composite.addRule(goldPillRule);
            composite.addRule(portalRule);
            composite.addRule(accessRule);
            levelChecker = composite;
        }
        return levelChecker;
    }



}
