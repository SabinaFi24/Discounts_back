package com.dev.objects;


import javax.persistence.*;
import java.util.List;

@Entity
@Table ( name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int OrganizationId;

    @Column (name = "name")
    private String name;

    /*@ManyToOne
    @JoinColumn (name = "user_id")
    private UserObject userObject;*/

    @ManyToMany
    @JoinColumn (name = "user_id")
    private List<UserObject> userObjects;

    @Transient
    private List<Store> stores;



    //getters and setters:
    public int getOrganizationId() {return OrganizationId;}
    public void setOrganizationId(int organizationId) {OrganizationId = organizationId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public List<Store> getStores() {return stores;}
    public void setStores(List<Store> stores) {this.stores = stores;}
    //end of getters and setters
}
