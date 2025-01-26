/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author hanbao
 */
public class SupplierDirectory {
    private ArrayList<Supplier> supplierList;
    
    public SupplierDirectory() {
        supplierList = new ArrayList<Supplier>();
    }

    public ArrayList<Supplier> getSupplierList() {
        return supplierList;
    }
    
    public Supplier addSupplier() {
        Supplier s = new Supplier();
        supplierList.add(s);
        return s;
    }
    
    public void removeSupplier(Supplier s) {
        supplierList.remove(s);
    }
    
    public Supplier searchSupplier(String keyWord) {
        for(Supplier s : supplierList) {
            if(keyWord.equals(s.getSupplyName())) {
                return s;
            }
        }
        return null;
    }
    
}
