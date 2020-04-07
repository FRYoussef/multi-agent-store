package model;

public class ClothTransfer {
    private String imageUri;
    private String id;
    private String name;
    private String price;

    public ClothTransfer(String imageUri, String id, String name, String price) {
        this.imageUri = imageUri;
        this.id = id;
        this.name = name;
        this.price = price;
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
