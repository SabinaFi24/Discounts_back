package com.dev.objects;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "sales")
public class SaleObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @Column (name = "start_Date")
    private Date startDate;

    @Column (name = "end_Date")
    private Date endDate;

    @Column (name = "content")
    private String content;

    @Column (name = "is_For_All")
    private int isForAll;

    @ManyToOne
    @JoinColumn(name="store_Sale")
    private StoreObject store;

    //getters and setters:
    public int getSaleId() {return id;}
    public void setSaleId(int id) {id = id;}

    public Date getStartDate() {return startDate;}
    public void setStartDate(Date startDate) {this.startDate = startDate;}

    public Date getEndDate() {return endDate;}
    public void setEndDate(Date endDate) {this.endDate = endDate;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public int isForAll() {return isForAll;}
    public void setIsForAll(int isForAll) {isForAll = isForAll;}

    public StoreObject getStore() {return store;}
    public void setStore(StoreObject store) {this.store = store;}

    //end of getters and setters
}
