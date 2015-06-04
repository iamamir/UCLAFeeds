package core.logic.buzz;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BuzzOfvcardLogic
{

    public static  String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof org.w3c.dom.CharacterData) {
           org.w3c.dom.CharacterData cd = (org.w3c.dom.CharacterData) child;
           return cd.getData();
        }
        return "?";
      }
    
    
}
