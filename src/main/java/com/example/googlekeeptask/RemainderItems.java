package com.example.googlekeeptask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RemainderItems  {
    public int id;
    public String title;
    public String items;

    public static ArrayList<Items> convertItemsStringToArrayList(String items){
        ArrayList<Items> itemList = new ArrayList<>();
        try {
            JSONArray itemsArray = new JSONArray(items);
            for(int i = 0;i<itemsArray.length();i++){
                JSONObject itemObject = itemsArray.getJSONObject(i);
                Items newItem = new Items();
                newItem.itemId = itemObject.getInt(Items.CONST_ITEM_ID);
                newItem.itemName = itemObject.getString(Items.CONST_ITEM_NAME);
                newItem.isChecked = itemObject.optBoolean(Items.CONST_ITEM_COMPLETED);

                itemList.add(newItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemList;
    }

    public static String convertItemsListToString(ArrayList<Items> itemList){
        String itemArrayValue = "";
        JSONArray itemsArray = new JSONArray();
        for (Items items : itemList){
            try {
                JSONObject itemObject = new JSONObject();
                itemObject.put(Items.CONST_ITEM_ID,items.itemId);
                itemObject.put(Items.CONST_ITEM_NAME,items.itemName);
                itemObject.put(Items.CONST_ITEM_COMPLETED,items.isChecked);

                itemsArray.put(itemObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        itemArrayValue = itemsArray.toString();
        return itemArrayValue;
    }
}
