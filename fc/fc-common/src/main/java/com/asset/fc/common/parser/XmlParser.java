package com.asset.fc.common.parser;

import com.asset.fc.common.model.Job;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.asset.fc.common.annotation.Parser;

@Parser(type = "xml")
public class XmlParser implements Parse {

    private final DocumentBuilder dBuilder;
    private final DocumentBuilderFactory dbFactory;
    Job jobObj = null;

    public XmlParser() throws Exception {
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
    }

    @Override
    public Job Prase(File file) throws Exception {
        jobObj = new Job();

        Document document = dBuilder.parse(file);

        document.getDocumentElement().normalize(); // normalize the data in the xml

        System.out.println("----------------------------");
        System.out.println("Root element :" + document.getDocumentElement().getNodeName());

        NodeList nodeList = document.getElementsByTagName("root");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node nNode = nodeList.item(i);// returns the index item in a collection

            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) { // to get the type of the object

                Element eElement = (Element) nNode;

                jobObj.setSourceFile(eElement.getElementsByTagName("sourceFile").item(0).getTextContent());
                jobObj.setDestniationFile(eElement.getElementsByTagName("destniationFile").item(0).getTextContent());
                jobObj.setMaxSpeed(Integer.parseInt(eElement.getElementsByTagName("maxSpeed").item(0).getTextContent()));

                System.out.println("Source File : " + eElement.getElementsByTagName("sourceFile").item(0).getTextContent());
                System.out.println("Destniation File : " + eElement.getElementsByTagName("destniationFile").item(0).getTextContent());
                System.out.println("Max Speed: " + eElement.getElementsByTagName("maxSpeed").item(0).getTextContent());
                System.out.println("----------------------------");

                return jobObj;
            }
        }

        return jobObj;
    }

    @Override
    public Job Prase(String body, String ext) throws Exception {
        return jobObj;
    }
}
