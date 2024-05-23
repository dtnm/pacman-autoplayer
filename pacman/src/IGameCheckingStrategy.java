package src;

import java.io.File;
import java.util.ArrayList;

public interface IGameCheckingStrategy {
    public abstract boolean check(ArrayList<File> fileList);
}
