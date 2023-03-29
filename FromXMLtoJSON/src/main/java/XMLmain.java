import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLmain {
    private static final String EMPLOYEE = "employee";
    private static final String ID = "id";
    private static final String FIRSTNAME = "firstName";
    private static final String LASTNAME = "lastName";
    private static final String COUNTRY = "county";
    private static final String AGE = "age";

    static String fileName = "data.xml";

    public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, SAXException {

        createXMLFile(fileName);

        List<Employee> list = parseXML("data.xml");

        String json = CSVmain.listToJson(list);

        CSVmain.writeString(json, "data2.json");

    }

    //-----------------------------------Создание и заполнение XML файла---------------------------------
    public static void createXMLFile(String fileName) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element staff = document.createElement("staff");
        document.appendChild(staff);

        Element employee1 = document.createElement(EMPLOYEE);
        staff.appendChild(employee1);
        Element employee2 = document.createElement(EMPLOYEE);
        staff.appendChild(employee2);

        Element id1 = document.createElement(ID);
        id1.appendChild((document.createTextNode("1")));
        Element id2 = document.createElement(ID);
        id2.appendChild((document.createTextNode("2")));
        employee1.appendChild(id1);
        employee2.appendChild(id2);

        Element firstName1 = document.createElement(FIRSTNAME);
        firstName1.appendChild((document.createTextNode("John")));
        Element firstName2 = document.createElement(FIRSTNAME);
        firstName2.appendChild((document.createTextNode("Ivan")));
        employee1.appendChild(firstName1);
        employee2.appendChild(firstName2);

        Element lastName1 = document.createElement(LASTNAME);
        lastName1.appendChild((document.createTextNode("Smith")));
        Element lastName2 = document.createElement(LASTNAME);
        lastName2.appendChild((document.createTextNode("Petrov")));
        employee1.appendChild(lastName1);
        employee2.appendChild(lastName2);

        Element country1 = document.createElement(COUNTRY);
        country1.appendChild((document.createTextNode("USA")));
        Element country2 = document.createElement(COUNTRY);
        country2.appendChild((document.createTextNode("RU")));
        employee1.appendChild(country1);
        employee2.appendChild(country2);

        Element age1 = document.createElement(AGE);
        age1.appendChild((document.createTextNode("25")));
        Element age2 = document.createElement(AGE);
        age2.appendChild((document.createTextNode("23")));
        employee1.appendChild(age1);
        employee2.appendChild(age2);

        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(fileName));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, streamResult);
    }

    //------------------------------Формируем список сотрудников из XML файла---------------------------
    public static List<Employee> parseXML(String filePath) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> employees = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(filePath);

        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node emp = nodeList.item(i);

            if (Node.ELEMENT_NODE == emp.getNodeType()) {
                Employee employee = parseEmployee(nodeList.item(i));
                employees.add(employee);
            }
        }
        return employees;
    }

    //-----------------------------------Отдельный метод заполнения данными-----------------------------
    public static Employee parseEmployee(Node elementNode) {
        long id = 0;
        String firstName = null;
        String lastName = null;
        String country = null;
        int age = 0;

        NodeList elementOfEmp = elementNode.getChildNodes();
        for (int j = 0; j < elementOfEmp.getLength(); j++) {

            switch (elementOfEmp.item(j).getNodeName()) {

                case ID: {
                    id = Long.valueOf(elementOfEmp.item(j).getTextContent());
                    break;
                }
                case FIRSTNAME: {
                    firstName = elementOfEmp.item(j).getTextContent();
                    break;
                }
                case LASTNAME: {
                    lastName = elementOfEmp.item(j).getTextContent();
                    break;
                }
                case COUNTRY: {
                    country = elementOfEmp.item(j).getTextContent();
                    break;
                }
                case AGE: {
                    age = Integer.valueOf(elementOfEmp.item(j).getTextContent());
                    break;
                }
            }
        }
        return new Employee(id, firstName, lastName, country, age);
    }

}
