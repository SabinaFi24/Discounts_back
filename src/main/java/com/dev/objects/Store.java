package com.dev.objects;

import javax.persistence.*;
import java.util.List;

@Entity
@Table( name = "store")
public class Store {
    @Id//the id of the table (primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment
    @Column (name = "id")
    private int storeId;

    @Column (name = "name")
    private String name;

    @Transient//not saved in database
    private List<Sales> sales;
}
