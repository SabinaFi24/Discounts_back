package com.dev.objects;

import javax.persistence.*;
import java.util.List;

@Entity
@Table( name = "store")
public class StoreObject {
    @Id//the id of the table (primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment
    @Column (name = "id")
    private int id;

    @Column (name = "name")
    private String name;

    @Transient//not saved in database
    private List<SaleObject> sales;

    public int getStoreId() {return id;}
    public void setStoreId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public List<SaleObject> getSales() {return sales;}
    public void setSales(List<SaleObject> sales) {this.sales = sales;}
}
