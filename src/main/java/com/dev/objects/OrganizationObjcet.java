package com.dev.objects;


import javax.persistence.*;
import java.util.List;

@Entity
@Table ( name = "organization")
public class OrganizationObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int OrganizationId;

    @Column (name = "name")
    private String name;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private UserObject userObject;

    @ManyToMany
    @JoinColumn (name = "user_id")
    private List<UserObject> userObjects;

    @Transient
    private List<StoreObject> stores;



    //getters and setters:
    public int getOrganizationId() {return OrganizationId;}
    public void setOrganizationId(int organizationId) {OrganizationId = organizationId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public List<StoreObject> getStores() {return stores;}
    public void setStores(List<StoreObject> stores) {this.stores = stores;}

    public UserObject getUserObject() {return userObject;}
    public void setUserObject(UserObject userObject) {this.userObject = userObject;}

    public List<UserObject> getUserObjects() {return userObjects;}
    public void setUserObjects(List<UserObject> userObjects) {this.userObjects = userObjects;}

    //end of getters and setters
}
