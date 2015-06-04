package quicksign;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import com.ning.http.util.Base64;


public class SOAPClient {
	
	
	//Data Preparation 
	private static String headerData = "5FJN59UJ4Z6";
	private static String password = "5tjGUQbBbmOSDpwc/rfljg==";
	private static byte[] iv = { (byte) 0xc9, (byte) 0x36, (byte) 0xea, (byte) 0x78,
			(byte) 0xd9, (byte) 0x36, (byte) 0x99, (byte) 0x3e,
			(byte) 0x36, (byte) 0x78, (byte) 0x52, (byte) 0x78,
			(byte) 0x3e, (byte) 0xea, (byte) 0x3e, (byte) 0xf2 };
	
	private static Cipher cipher = null;

	static {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
	
	
	private static String ReadPDF(){
		try {
			File file = new File("public/demo.pdf");
			InputStream inputStream = new FileInputStream(file); 
			byte[] bytes = readFully(inputStream);//new byte[(int) file.length()];
			return Base64.encode(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private static String PreparedRequestData(){
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><quicksign><donnees_transaction><idDistributeur>NETSOL</idDistributeur><idSignataire>NETSOL</idSignataire><codeProduit>NETSOL1</codeProduit>"
				+ "<numDossier>1234567890</numDossier><delaiRetract>45</delaiRetract><conjoint>N</conjoint></donnees_transaction><donnees_clients><id>12345678901234</id><civilite>MR</civilite>"
				+ "<nom>DUPONT</nom><prenom>JEAN</prenom><dateNaissance>31121980</dateNaissance></donnees_clients>"
				+ "<donnees_contact><email1>Sheheryar.Aamir@netsoltech.com</email1>"
				+ "<telPortable_1>00923018407414</telPortable_1>"
				+ "<adresse1_1>21RUEDELABANQUE</adresse1_1><adresse1_2>BATIMENTC</adresse1_2><codePostal1>75002</codePostal1><ville1>PARIS</ville1></donnees_contact>"
				+ "<donnees_pdf><contratPDF1_xml>" +ReadPDF()+ "</contratPDF1_xml></donnees_pdf>"
				+ "<options><URLRetourForm>http://www.siteclient.fr/formulaire</URLRetourForm>"
				+ "<URLRetourFin>http://www.siteclient.fr/fin</URLRetourFin></options><metier><donneesMetier1></donneesMetier1>"
				+ "<donneesMetier2></donneesMetier2><donneesMetier3></donneesMetier3></metier></quicksign>";
				
		
		byte[] encryptResult = encrypt(content, password);
		String encryptData = Base64.encode(encryptResult);		 
		System.out.println("Base64 format:" + encryptData);
		return headerData+encryptData;
	}
	
	private static byte[] encrypt(String message, String passWord) {
		try {
			SecretKey secretKey = new SecretKeySpec(
					Base64.decode(passWord), "AES");			
			
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            
			cipher.init(Cipher.ENCRYPT_MODE, secretKey,ivspec);
			byte[] resultBytes = cipher.doFinal(message.getBytes("UTF-8"));
			return resultBytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//----------------------------Data Preparation 
	
	
	
	
	public static byte[] readFully(InputStream stream) throws IOException
	 {
	     byte[] buffer = new byte[8192];
	     ByteArrayOutputStream baos = new ByteArrayOutputStream();

	     int bytesRead;
	     while ((bytesRead = stream.read(buffer)) != -1)
	     {
	         baos.write(buffer, 0, bytesRead);
	     }
	     return baos.toByteArray();
	 }
	
	
	
	
	public static String CallService() {
	    String result = "";
	    try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            String url = "https://recetteqs.quicksign.fr/BUSINESS_PROCESS_WS/QUICKSIGN_SOAPService?wsdl";
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);

            // Process the SOAP Response
            result = printSOAPResponse(soapResponse);

            soapConnection.close();
        } catch (Exception e) {
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }
	    
	    return result;
    }
	
	
	private static String printSOAPResponse(SOAPMessage soapResponse) throws Exception {
	    StringWriter writer = new StringWriter();
	    
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.print("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(writer);
        transformer.transform(sourceContent, result);
        return writer.toString();
    }
	
	
	
	private static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://recetteqs.quicksign.fr/BUSINESS_PROCESS_WS/QUICKSIGN_SOAPService";

        // SOAP Envelope
        
      /*  xmlns:q0="http://xml.netbeans.org/schema/soap_parameters"  
        		  xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"  
        		  xmlns:xsd="http://www.w3.org/2001/XMLSchema"  
        		  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">  */

        
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("q0", "http://xml.netbeans.org/schema/soap_parameters");

        /*
        Constructed SOAP Request Message:
        <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:example="http://ws.cdyne.com/">
            <SOAP-ENV:Header/>
            <SOAP-ENV:Body>
                <example:VerifyEmail>
                    <example:email>mutantninja@gmail.com</example:email>
                    <example:LicenseKey>123</example:LicenseKey>
                </example:VerifyEmail>
            </SOAP-ENV:Body>
        </SOAP-ENV:Envelope>
         */

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("input", "q0");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("data", "q0");
        soapBodyElem1.addTextNode(PreparedRequestData());
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI  + "input");
        soapMessage.saveChanges();
        /* Print the request message */
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }
	
	
}
