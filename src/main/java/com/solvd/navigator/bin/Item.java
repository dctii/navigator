package com.solvd.navigator.bin;

public class Item {
    private int itemId;
    private String itemCode;
    private String itemName;
    private String itemDescription;
    public Item() {

    }

    public Item(int itemId, String itemCode, String itemName, String itemDescription) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    @Override
    public String toString() {
        return "Items{" +
               "itemId=" + itemId +
               ", itemCode='" + itemCode + '\'' +
               ", itemName='" + itemName + '\'' +
               ", itemDescription='" + itemDescription + '\'' +
               '}';
    }
}
