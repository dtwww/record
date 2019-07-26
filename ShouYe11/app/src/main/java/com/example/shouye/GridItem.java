package com.example.shouye;

/**
 * Created by ASUS on 2017/12/25.
 */

public class GridItem {
    private int itemId;
    private String itemName;

    public GridItem(){
    }

    public GridItem(int itemId, String itemName){
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public int getItemId(){
        return itemId;
    }

    public String getItemName(){
        return itemName;
    }

    public void setItemId(int itemId){
        this.itemId = itemId;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }
}
