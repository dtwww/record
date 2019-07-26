package com.example.shouye.accounting;

import android.content.Context;

import com.example.shouye.util.Persistable;
import com.example.shouye.util.Persister;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by ThinkPad on 2017/12/8.
 */
//一条
public class Amount extends Persistable implements Serializable{
    private String id;
    private int type;
    private String content;
    private int amount;
    private double price;
    private Date time;
    public Amount(){
        super();
    }
    public Amount(int type, String content, int amount, double price){
        this.type = type;
        this.content = content;
        this.amount = amount;
        this.price = price;
    }

    public String getId(){
        return id;
    }
    public int getType(){
        return type;
    }
    public String getContent(){
        return content;
    }
    public int getAmount(){
        return amount;
    }
    public double getPrice(){return price;}
    public Date getTime(){return time;}

    public void setId(String id){
        this.id = id;
    }
    public void setType(int type){
        this.type = type;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setAmount(int amount){
        this.amount = amount;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public void setTime(Date time){
        this.time = time;
    }

    public static List<Amount> getAllAmounts(Context context) {
        Persister persister = Persister.getInstance(context);
        return persister.list(Amount.class);
    }
}
