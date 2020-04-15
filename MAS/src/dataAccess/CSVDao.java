package dataAccess;

import model.ClothTransfer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVDao {
    private static final String CSV_URI = "DB.csv";
    private static final String CSV_SEPARATOR = ",";
    private static final String FIELD_SEPARATOR = ";";

    public ArrayList<ClothTransfer> getCloths(){
        ArrayList<ClothTransfer> alCloths = new ArrayList<>();
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_URI))) {
            // headers
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(CSV_SEPARATOR);
                alCloths.add(new ClothTransfer(
                        fields[0],
                        fields[1],
                        fields[2],
                        fields[3],
                        fields[4].split(FIELD_SEPARATOR)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return alCloths;
    }
}
