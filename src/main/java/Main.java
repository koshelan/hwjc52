import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.*;
import org.w3c.dom.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data1.csv";
        String xmlFileName = "data.xml";
        String jsonFileName = "data.json";
        String jsonFormZMLFileName = "data2.json";

//        List<Employee> list = parseCSV(columnMapping, fileName);
//        System.out.println(list);
//        String json = listToJson(list);
//        writeString(jsonFileName,json);

        List<Employee> list = parseXML(xmlFileName);
        System.out.println(list);
        String json = listToJson(list);
        writeString(jsonFormZMLFileName,json);

    }

    private static List<Employee> parseXML(String xmlFileName) {
        List<Employee> result = new ArrayList<>();
        long id=0;
        String firstName = null,
                lastName= null,
                country= null;
        int age=0;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFileName));
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element element = (Element) node;
                    NodeList childNodeList = element.getChildNodes();
                    for (int j = 0; j < childNodeList.getLength(); j++) {
                        Node childNode = childNodeList.item(j);
                        if (Node.ELEMENT_NODE == childNode.getNodeType()) {
                            switch (childNode.getNodeName()) {
                                case "id":
                                    id = Long.parseLong(childNode.getTextContent());
                                    break;
                                case "firstName":
                                    firstName = childNode.getTextContent();
                                    break;
                                case "lastName":
                                    lastName = childNode.getTextContent();
                                    break;
                                case "country":
                                    country = childNode.getTextContent();
                                    break;
                                case "age":
                                    age = Integer.parseInt(childNode.getTextContent());
                                    break;
                            }
                        }
                    }
                    Employee employee = new Employee(id,firstName,lastName,country,age);
                    result.add(employee);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }



    private static boolean writeString(String jsonFileName, String json) {
        try (FileWriter writer = new FileWriter(jsonFileName)) {
            writer.write(json);
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        File file = new File(fileName);
        List<Employee> result = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            result = csv.parse();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return result;
    }


}
