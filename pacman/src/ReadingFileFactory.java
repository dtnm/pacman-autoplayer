package src;

public class ReadingFileFactory {
    public static ReadingFileFactory instance;

    private IReadingFile fileAdapter;

    public static ReadingFileFactory getInstance(){
        if (instance == null){
            instance = new ReadingFileFactory();
        }
        return instance;
    }

    public IReadingFile getFileAdapter(String fileType){
        if (fileType.equals("xml")){
            fileAdapter = new XMLAdapter();
        }
        return fileAdapter;
    }



}
