package com.dev.objects;

import javax.persistence.*;

@Entity
@Table( name = "Users_Organizations")
public class OrganiztionsSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    public int id;

    @ManyToOne
    @JoinColumn(name = "organizations")
    private OrganizationObject organizations ;

    @ManyToOne
    @JoinColumn(name = "sale")
    private SaleObject sales ;

    //constructors:
    public OrganiztionsSales(int id, OrganizationObject organizations, SaleObject sales) {
        this.id = id;
        this.organizations = organizations;
        this.sales = sales;
    }

    public OrganiztionsSales() {}

    //getters and setters:
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public OrganizationObject getOrganizations() {return organizations;}
    public void setOrganizations(OrganizationObject organizations) {this.organizations = organizations;}

    public SaleObject getSales() {return sales;}
    public void setSales(SaleObject sales) {this.sales = sales;}
}
