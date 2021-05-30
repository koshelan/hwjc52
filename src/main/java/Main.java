import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Object RuntimeException;

    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String jsonFileName = "data.json";

        List<Employee> list = parseCSV(columnMapping, fileName);

        String json = listToJson(list);
        writeString(jsonFileName,json);
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
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(list,listType);
    }

    private static  List<Employee> parseCSV(String[] columnMapping, String fileName) {
        File file = new File (fileName);
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
