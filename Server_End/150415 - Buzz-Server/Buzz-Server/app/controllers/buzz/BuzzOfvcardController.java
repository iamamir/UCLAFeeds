package controllers.buzz;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import models.db.buzz.BuzzOfvcard;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import play.libs.Json;
import play.mvc.Result;
import util.BuzzJsonNode;
import controllers.BaseController;
import controllers.common.Authenticated;
import controllers.common.DBConnection;
import core.logic.buzz.BuzzOfvcardLogic;
import dao.buzz.BuzzOfvcardDao;

public class BuzzOfvcardController extends BaseController
{

    @Authenticated
    @DBConnection(write=false)
    public static Result getVcard(){


        /*BuzzJsonNode nfsJsonNode=new BuzzJsonNode(request().body().asJson());
        String userName= nfsJsonNode.findPathAsText("userName");
        BuzzOfvcard buzzOfvcard=BuzzOfvcardDao.getInstance().selectBuzzOfvcard(userName);

        String xmlString=buzzOfvcard.getvCard();

        ObjectNode objectNode=Json.newObject();


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;

        try {
            db = dbf.newDocumentBuilder();


            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlString));

            try {
                org.w3c.dom.Document doc = db.parse(is);
               NodeList nodes=doc.getElementsByTagName("vCard");


               for(int i=0;i<nodes.getLength();i++)
               {
                   Element element=(Element) nodes.item(i);

                  NodeList first = element.getElementsByTagName("VCARD_FIELD_EMAIL");
                  Element line=(Element) first.item(0);
                  objectNode.put("VCARD_FIELD_EMAIL", BuzzOfvcardLogic.getCharacterDataFromElement(line));

                  NodeList second = element.getElementsByTagName("VCARD_FIELD_JID");
                  line=(Element) second.item(0);
                  objectNode.put("VCARD_FIELD_JID", BuzzOfvcardLogic.getCharacterDataFromElement(line));

                  NodeList third = element.getElementsByTagName("JABBERID");
                  line=(Element) third.item(0);
                  objectNode.put("JABBERID", BuzzOfvcardLogic.getCharacterDataFromElement(line));

                  NodeList fourth = element.getElementsByTagName("VCARD_FIELD_PHONE");
                  line=(Element) fourth.item(0);
                  objectNode.put("VCARD_FIELD_PHONE", BuzzOfvcardLogic.getCharacterDataFromElement(line));

                  NodeList fifth = element.getElementsByTagName("VCARD_FIELD_STATUS");
                  line=(Element) fifth.item(0);
                  objectNode.put("VCARD_FIELD_STATUS", BuzzOfvcardLogic.getCharacterDataFromElement(line));

                  NodeList sixth = element.getElementsByTagName("VCARD_FIELD_NICKNAME");
                  line=(Element) sixth.item(0);
                  objectNode.put("VCARD_FIELD_NICKNAME", BuzzOfvcardLogic.getCharacterDataFromElement(line));

                  NodeList seventh = element.getElementsByTagName("TYPE");
                  line=(Element) seventh.item(0);
                  objectNode.put("TYPE", BuzzOfvcardLogic.getCharacterDataFromElement(line));


               }


            } catch (SAXException e) {
                System.out.println("SAX Exception");
            } catch (IOException e) {
                System.out.println("IO exception");
            }
        } catch (ParserConfigurationException e1) {
            System.out.println("parse configuration exception");
        }*/

    return ok("hello world");
    }

}
