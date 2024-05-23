package src;

import src.matachi.mapeditor.editor.Controller;

public interface IReadingFile {
    public abstract void loadFileMap(String fileMap, Controller controller);

    public String convertToStringMaze(String filePath, PortalDict protals);
}
