
package util;


import org.codehaus.jackson.JsonNode;


public class BuzzJsonNode
{

    JsonNode jsonNode;

    public BuzzJsonNode(JsonNode jsonNode)
    {

        this.jsonNode = jsonNode;
    }

    public <G> Object getValue(String key)
    {
        return null;
    }

    public JsonNode findPath(String path)
    {
        return this.jsonNode.findPath(path);
    }

    public long findPathAsLong(String path)
    {
        return this.jsonNode.findPath(path).asLong();
    }

    public String findPathAsText(String path)
    {
        return this.jsonNode.findPath(path).asText();
    }

    public double findPathAsDouble(String path)
    {
        return this.jsonNode.findPath(path).asDouble();
    }

    public boolean findPathAsBoolean(String path)
    {
        return this.jsonNode.findPath(path).asBoolean();
    }

    public JsonNode toJsonNode()
    {
        return this.jsonNode;
    }

}
