package com.java.tims;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseXml {

    public static void main(String[] args) {
    	startParser();
    }
    
    public static void startParser(){
        String filePath = "C:/Users/pyogaraj/Desktop/test1.xml";
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("Enter the id please");
            Scanner s = new Scanner(System.in);
            String id = s.nextLine();		
            
            //update Id
            updateId(doc,id);
            
            //update value
            
            updateValue(doc,id);
            
            //update attribute value
//            updateAttributeValue(doc,id);
            
            //update Element value
//            updateElementValue(doc);
            
            //delete element
//            deleteElement(doc);
            
            //add new element
//            addElement(doc);
            
            //write the updated document to file or console
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("C:/Users/pyogaraj/Desktop/parse_updated.xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            System.out.println("XML file updated successfully");
            
        } catch (SAXException | ParserConfigurationException | IOException | TransformerException e1) {
            e1.printStackTrace();
        }
    	
    }

    private static void addElement(Document doc) {
        NodeList employees = doc.getElementsByTagName("Employee");
        Element emp = null;
        
        //loop for each employee
        for(int i=0; i<employees.getLength();i++){
            emp = (Element) employees.item(i);
            Element salaryElement = doc.createElement("salary");
            salaryElement.appendChild(doc.createTextNode("10000"));
            emp.appendChild(salaryElement);
        }
    }

    private static void deleteElement(Document doc) {
        NodeList employees = doc.getElementsByTagName("Employee");
        Element emp = null;
        //loop for each employee
        for(int i=0; i<employees.getLength();i++){
            emp = (Element) employees.item(i);
            Node genderNode = emp.getElementsByTagName("gender").item(0);
            emp.removeChild(genderNode);
        }
        
    }

    private static void updateElementValue(Document doc) {
        NodeList employees = doc.getElementsByTagName("Employee");
        Element emp = null;
        //loop for each employee
        for(int i=0; i<employees.getLength();i++){
            emp = (Element) employees.item(i);
            Node name = emp.getElementsByTagName("name").item(0).getFirstChild();
            System.out.println("name is"+name);
            name.setNodeValue(name.getNodeValue().toUpperCase());
          
//            name.setNodeValue(name.getNodeValue().replaceAll(".*", "R"));
        }
     
       }
    
    public static void updateValue(Document doc,String timsid){
    	NodeList tagid = doc.getElementsByTagName("ID");
    	NodeList logicalid = doc.getElementsByTagName("LogicalID");
    	Element tag = null;
    	
    	for(int i=0;i<tagid.getLength();i++){
    		tag = (Element) tagid.item(i);
    		tag.setTextContent(timsid);
    	}
    	for(int j=0;j<logicalid.getLength();j++){
    		tag = (Element) logicalid.item(j);
    		tag.setTextContent(timsid);
    	}
    	
    	
    }
    private static void updateId(Document doc,String timsid){
    	NodeList tagid = doc.getElementsByTagName("ID");
    	NodeList tagresult = doc.getElementsByTagName("Result");
    	Element em = null;
    	
    	for(int i=0;i<tagid.getLength();i++){
    		em = (Element) tagid.item(i);
//    		Node idtag = em.getElementsByTagName("ID").item(0);
    		em.setAttribute("xlink:href", "");
    		em.setAttribute("xlink:href", "http://tims.cisco.com/xml/"+timsid+"/entity.svc"+em.getAttribute("xlink:href"));
    	}
    	for(int j=0;j<tagresult.getLength();j++){
    		em = (Element) tagresult.item(j);
    		em.setAttribute("xlink:href", "");
    		em.setAttribute("xlink:href", "http://tims.cisco.com/xml/"+timsid+"/entity.svc"+em.getAttribute("xlink:href"));
    	}
    	
    }
    private static void updateAttributeValue(Document doc,String timsid) {
        NodeList employees = doc.getElementsByTagName("ID");
        Element emp = null;
        //loop for each employee
        for(int i=0; i<employees.getLength();i++){
            emp = (Element) employees.item(i);
            
//            System.out.println("emp is"+employees.item(i));
//            String gender = emp.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
//            Node name = emp.getElementsByTagName("ID").item(0).getFirstChild();
//            Node name1 = emp.getElementsByTagName("ID").item(0);
//            System.out.println("gender is"+name1);
            emp.setAttribute("xlink:href", "");
            emp.setAttribute("xlink:href", "http://tims.cisco.com/xml/"+timsid+"/entity.svc"+emp.getAttribute("xlink:href"));
//            if(gender.equalsIgnoreCase("male")){
//                //prefix id attribute with M
//            	System.out.println("id is"+emp.getAttribute("id"));
//                emp.setAttribute("id", "M"+emp.getAttribute("id"));
//                emp.setAttribute("xlink:href", "");
//                emp.setAttribute("xlink:href", "http://tims.cisco.com/xml/"+timsid+"/entity.svc"+emp.getAttribute("xlink:href"));
//            }else{
//                //prefix id attribute with F
//                emp.setAttribute("id", "F"+emp.getAttribute("id"));
//            }
        }
    }

}



