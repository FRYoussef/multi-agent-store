package model;

import dataAccess.CSVHandler;

public class Clothing {
    private int id;
    private String name;
    private String price;
    private String imageUri;
    private int views;
    private int sales;
    private String[] sizes;
    private String[] tags;

    public Clothing(int id, String name, String price, String imageUri, int views, int sales, String[] sizes, String[] tags) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUri = imageUri;
        this.views = views;
        this.sales = sales;
        this.sizes = sizes;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String[] getSizes() {
        return sizes;
    }

    public void setSizes(String[] sizes) {
        this.sizes = sizes;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String toCsvFormat(){
        StringBuilder sb = new StringBuilder(id + CSVHandler.CSV_SEPARATOR)
                .append(name).append(CSVHandler.CSV_SEPARATOR)
                .append(views).append(CSVHandler.CSV_SEPARATOR)
                .append(price).append(CSVHandler.CSV_SEPARATOR)
                .append(sales).append(CSVHandler.CSV_SEPARATOR);

        for(String s : sizes)
            sb.append(s).append(CSVHandler.FIELD_SEPARATOR);

        sb.append(CSVHandler.CSV_SEPARATOR);

        for(String t : tags)
            sb.append(t).append(CSVHandler.FIELD_SEPARATOR);

        sb.append(CSVHandler.CSV_SEPARATOR)
            .append(imageUri);

        return sb.toString();
    }

    /**
     * Use this instance instead a null value
     * @return Clothing
     */
    public static Clothing getDefault(){
        Clothing clothing = new Clothing(
                -1,
                "None",
                "",
                "",
                0,
                0,
                new String[]{""},
                new String[]{""}
        );
        return clothing;
    }
}
