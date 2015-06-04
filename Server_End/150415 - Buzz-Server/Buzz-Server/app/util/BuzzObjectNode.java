
package util;


import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;


public class BuzzObjectNode
{

    ObjectNode objectNode;

    public BuzzObjectNode()
    {
        super();
        objectNode = Json.newObject();
    }

    public void put(String key, String value)
    {
        objectNode.put(key, value);
    }

    public void put(String key, Double value)
    {
        objectNode.put(key, value);
    }

    public void put(String key, Long value)
    {
        objectNode.put(key, value);
    }

    public void put(String key, Boolean value)
    {
        objectNode.put(key, value);
    }

    public ObjectNode toObjectNode()
    {
        return this.objectNode;
    }
}
