package dataAccess;

import logic.transfer.Clothing;

import java.util.ArrayList;
import java.util.HashSet;

public class ClothingDao implements IDao<Clothing>{
    private static final int POS_ID = 0;
    private static final int POS_NAME = 1;
    private static final int POS_VIEWS = 2;
    private static final int POS_PRICE = 3;
    private static final int POS_SALES = 4;
    private static final int POS_SIZES = 5;
    private static final int POS_TAGS = 6;
    private static final int POS_IMAGE_URI = 7;
    private static final String CSV_URI = "../DB/ClothingDB.csv";

    @Override
    public Clothing get(int id) {
        HashSet<Clothing> clothings = getAll();
        Clothing clothing = new Clothing();

        for(Clothing clth : clothings){
            if(clth.getId() == id){
                clothing = clth;
                break;
            }
        }

        return clothing;
    }

    @Override
    public HashSet<Clothing> getAll() {
        HashSet<Clothing> clothings = new HashSet<>();
        ArrayList<ArrayList<String>> clothFields = CsvHandler.readCSV(CSV_URI);

        // let's transform strings into objects
        for(ArrayList<String> al : clothFields){
            Clothing clothing = new Clothing(
                    Integer.parseInt(al.get(POS_ID)),
                    al.get(POS_NAME),
                    al.get(POS_PRICE),
                    al.get(POS_IMAGE_URI),
                    Integer.parseInt(al.get(POS_VIEWS)),
                    Integer.parseInt(al.get(POS_SALES)),
                    CsvHandler.fieldSplitter(al.get(POS_SIZES)),
                    CsvHandler.fieldSplitter(al.get(POS_TAGS))
            );
            clothings.add(clothing);
        }

        return clothings;
    }

    @Override
    public void write(Clothing clothing) {
        HashSet<Clothing> clothings = getAll();

        // if exists remove it in order to add it again (modification)
        clothings.remove(clothing);
        clothings.add(clothing);

        writeAll(clothings);
    }

    @Override
    public void writeAll(HashSet<Clothing> clothings) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(getCsvHeader());

        for(Clothing clothing : clothings)
            lines.add(clothing.toCsvFormat());

        CsvHandler.writeCSV(CSV_URI, lines);
        lines = null;
    }

    public String getCsvHeader(){
        return "Id" + CsvHandler.CSV_SEPARATOR +
                "Name" + CsvHandler.CSV_SEPARATOR +
                "Views" + CsvHandler.CSV_SEPARATOR +
                "Price" + CsvHandler.CSV_SEPARATOR +
                "Sales" + CsvHandler.CSV_SEPARATOR +
                "Size" + CsvHandler.CSV_SEPARATOR +
                "Tags" + CsvHandler.CSV_SEPARATOR +
                "ImageUrl";
    }
}
