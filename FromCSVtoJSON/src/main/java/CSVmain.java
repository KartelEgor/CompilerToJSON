import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class CSVmain {
    static String[] employee1 = "1,John,Smith,USA,25".split(",");
    static String[] employee2 = "2,Ivan,Petrov,RU,23".split(",");
    static String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
    static String fileNameCSV = "data.csv";

    public static void main(String[] args) {

        createCSVFile(fileNameCSV);

        List<Employee> list = parseCSV(columnMapping, fileNameCSV);

        String json = listToJson(list);

        writeString(json, "data.json");

    }

    //-----------------------------Создание CSV файла-----------------------------------------------------
    public static void createCSVFile(String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeNext(employee1);
            writer.writeNext(employee2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //------------------------------Формируем список сотрудников из CSV файла---------------------------
    public static List<Employee> parseCSV(String[] mapping, String fileName) {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(fileName));

            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(mapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();

            List<Employee> staff = csv.parse();

            return staff;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //-----------------------------Преобразование в строку в формате JSON--------------------------
    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        return json;
    }

    //-----------------------------Запись данных в файл JSON--------------------------
    public static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
