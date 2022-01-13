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
    private int SaleId;

    @Column (name = "start_Date")
    private Date startDate;

    @Column (name = "end_Date")
    private Date endDate;

    @Column (name = "content")
    private String content;

    @Column (name = "is_For_All")
    private boolean isForAll;

    //various sales can be linked to several organizations
    //in one store there can be different sales to different stores
    @ManyToMany
    @JoinTable (name = "Organizations_Sales", joinColumns = {@JoinColumn(name="saleId")},
            inverseJoinColumns = {@JoinColumn(name = "organizationId")})
    Set<OrganizationObject> Organizations = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="store_Sale")
    private StoreObject store;

    //getters and setters:
    public int getSaleId() {return SaleId;}
    public void setSaleId(int saleId) {SaleId = saleId;}

    public Set<OrganizationObject> getOrganizations() {
        return Organizations;
    }

    public void setOrganizations(Set<OrganizationObject> organizations) {
        Organizations = organizations;
    }

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
