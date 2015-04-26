import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {

	public static ArrayList<User> ParseUsers(String is)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document;
		ArrayList<User> users = new ArrayList<User>();
		document = builder.parse(is);

		NodeList nodeList = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				String ID = node.getAttributes().getNamedItem("ID")
						.getNodeValue();
				String username = elem.getElementsByTagName("UserName").item(0)
						.getChildNodes().item(0).getNodeValue();
				String password = elem.getElementsByTagName("Password").item(0)
						.getChildNodes().item(0).getNodeValue();
				users.add(new User(ID, username, password));
			}
		}
		return users;
	}
}