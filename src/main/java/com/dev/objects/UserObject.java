package com.dev.objects;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    public int userId;

    @Column (name = "username")
    private String username;

    @Column (name = "password")
    private String password;

    @Column (name = "token")
    private String token;

    //one user can have access to many organizations
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Users_Organizations",
            joinColumns = { @JoinColumn(name = "userId") },
            inverseJoinColumns = { @JoinColumn(name = "organizationId") }
    )
    Set<OrganizationObject> organization = new HashSet<>();

    @Transient
    private List<OrganizationObject> organizations;

    //getters and setters:
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

    public List<OrganizationObject> organizations() {return organizations;}
    public void setOrganizations(List<OrganizationObject> organizations) {this.organizations = organizations;}

    public int getId() {return userId;}
    public void setId(int id) {this.userId = userId;}
    //end of getters and setters


}
