package dataAccess;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CsvHandler {
    public static final String CSV_SEPARATOR = ",";
    public static final String FIELD_SEPARATOR = ";";
    public static final String EMPTY_FIELD = ",,";
    public static final String FILL_EMPTY_FIELD = ",\"\",";

    public static ArrayList<ArrayList<String>> readCSV(String csvPath, int nFields){
        ArrayList<ArrayList<String>> fields = new ArrayList<>();
        String line = "";
        int cont = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(new File(csvPath)))) {
            // headers
            br.readLine();

            while ((line = br.readLine()) != null) {
                line.replace(EMPTY_FIELD, FILL_EMPTY_FIELD);
                String[] lineFields = line.split(CSV_SEPARATOR);
                fields.add(new ArrayList<>(Arrays.asList(lineFields)));

                // fill empty fields
                if(fields.get(cont).size() < nFields)
                    for (int i = fields.get(cont).size(); i <= nFields; i++)
                        fields.get(cont).add("");

                cont++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fields;
    }

    public static void writeCSV(String csvPath, ArrayList<String> lines){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(csvPath)))) {
            for (int i = 0; i < lines.size(); i++) {
                bw.write(lines.get(i));
                if(i < lines.size()-1)
                    bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] fieldSplitter(String field){
        return field.split(FIELD_SEPARATOR);
    }
}
