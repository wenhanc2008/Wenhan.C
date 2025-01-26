/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


import java.util.List; // 更正为这个
import java.util.ArrayList;

/**
 *
 * @author hanbao
 */
public class Product {
    
    private String name;
    private int price;
    private int id;

    private static int count = 0;
    private List<Feature> features;
    
    public Product() {
        count++;
        id = count;
        features = new ArrayList<>();
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
    
    
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    public List<Feature> getFeatures() {
        return features;
    }
    
    public Feature addNewFeature() {
        Feature newFeature = new Feature(this); // 传递当前 Product 实例作为 Feature 的 owner
        features.add(newFeature);
        return newFeature;
    }
    
    
    public void removeFeature(int index) {
        if (index >= 0 && index < features.size()) {
            features.remove(index);
        }
    }
       
    @Override
    public String toString() {
        return name;
    }
    
    
}
