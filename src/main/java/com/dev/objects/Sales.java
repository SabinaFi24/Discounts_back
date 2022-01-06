package com.dev.objects;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name = "sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int SaleId;

    @ManyToOne
    @JoinColumn (name = "organization_id")
    private Organization organization;

    @Column (name = "start-date")
    private Date startDate;

    @Column (name = "end-date")
    private Date endDate;

    @Column (name = "content")
    private String content;

    @Column (name = "is-forall")
    private boolean isForAll;

    //getters and setters:
    public int getSaleId() {return SaleId;}
    public void setSaleId(int saleId) {SaleId = saleId;}

    public Organization getOrganization() {return organization;}
    public void setOrganization(Organization organization) {this.organization = organization;}

    public Date getStartDate() {return startDate;}
    public void setStartDate(Date startDate) {this.startDate = startDate;}

    public Date getEndDate() {return endDate;}
    public void setEndDate(Date endDate) {this.endDate = endDate;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public boolean isForAll() {return isForAll;}
    public void setForAll(boolean forAll) {isForAll = forAll;}
    //end of getters and setters
}
