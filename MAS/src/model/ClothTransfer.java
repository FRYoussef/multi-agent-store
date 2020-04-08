package model;

public class ClothTransfer {
    private String imageUri;
    private String id;
    private String name;
    private String price;
    private String[] sizes;

    public ClothTransfer(String imageUri, String id, String name, String price, String[] sizes) {
        this.imageUri = imageUri;
        this.id = id;
        this.name = name;
        this.price = price;
        this.sizes = sizes;
    }

    public String[] getSizes() {
        return sizes;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
