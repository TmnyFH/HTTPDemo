package com.example.httpdemo;

import org.json.JSONException;
import org.json.JSONObject;

public class TelegramMessage {

    private String text;
    private String fromFirstName;
    private String date;

    public TelegramMessage(String text, String fromFirstName, String date) {
        this.text = text;
        this.fromFirstName = fromFirstName;
        this.date = date;
    }

    public TelegramMessage(JSONObject result) {

        try{
            JSONObject message = result.getJSONObject("message");
            this.text = message.getString("text");
            this.date = String.valueOf(message.getLong("date"));

            JSONObject from = message.getJSONObject("from");
            this.fromFirstName = from.getString("first_name");
        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    public String getMessageData(){
        return fromFirstName + " wrote:\n" + text + "(" + date + ")\n\n";
    }
}
