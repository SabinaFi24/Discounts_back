package com.dev.objects;

import javax.persistence.*;


//one user can have access to many organizations
@Entity
@Table( name = "Users_Organizations")

public class UserOrganizations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    public int id;

    @ManyToOne
    @JoinColumn(name = "organizations")
    private OrganizationObject organizations ;

    @ManyToOne
    @JoinColumn(name = "user")
    private UserObject userObject ;

    //constructors:
    public UserOrganizations(int id, OrganizationObject organizations, UserObject userObject) {
        this.id = id;
        this.organizations = organizations;
        this.userObject = userObject;
    }
    public UserOrganizations( OrganizationObject organizations, UserObject userObject) {
        this.organizations = organizations;
        this.userObject = userObject;
    }
    public UserOrganizations() {}

    public UserOrganizations(Object organizationByOrganizationId, UserObject userByToken) {
        this.organizations = organizations;
        this.userObject = userObject;
    }

    //getters and setters:
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public OrganizationObject getOrganizations() {return organizations;}
    public void setOrganizations(OrganizationObject organizations) {this.organizations = organizations;}

    public UserObject getUserObject() {return userObject;}
    public void setUserObject(UserObject userObject) {this.userObject = userObject;}
}
