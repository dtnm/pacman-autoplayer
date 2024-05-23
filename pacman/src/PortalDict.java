package src;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * given the portal type ie PortalWhite, PortalGold,...
 * assign alphabet character to new type of portal
 */
public class PortalDict {
//    partially adopted from chatgpt
    private  static Map<String, Character> wordDefs;
    private Character nextVal;

    public PortalDict() {
        wordDefs = new HashMap<>();
        nextVal = 'a';
    }



//    map word to definition: String to Character
    public void addWord(String word, Character definition) {
        wordDefs.put(word, definition);
    }

    // definition ie the character
    public Character getDefinition(String portal) {
        return wordDefs.get(portal);
    }

    public ArrayList<Character> getAllCharacter(){
        ArrayList<Character> characterList = new ArrayList<>();
        for (Map.Entry<String, Character> entry : wordDefs.entrySet()) {
            characterList.add(entry.getValue());
        }
        return characterList;
    }

    public char assignValue(String portal) {
        // skip when the assigning of alphabet is i,g,x
        if (nextVal.equals('i') || nextVal.equals('g') || nextVal.equals('x')
        || nextVal.equals('p') || nextVal.equals('m')){
            nextVal++;
        }

        if (!wordDefs.containsKey(portal)) {
            wordDefs.put(portal, nextVal);
            nextVal++;
        }
        return wordDefs.get(portal);
    }

    public String convertToValue(Character letter) {
        for (Map.Entry<String, Character> entry : wordDefs.entrySet()) {
            if (entry.getValue().equals(letter)) {
                return entry.getKey();
            }
        }
        return "";
    }

    public  Map<String, Character> getWordDefs() {
        return wordDefs;
    }

    public boolean checkExistPortal(String portalName){
        // check if the portal name has already exist in the hashmap or not
        return wordDefs.containsKey(portalName);
    }
}