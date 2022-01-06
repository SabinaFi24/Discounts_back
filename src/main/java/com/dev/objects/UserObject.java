package com.dev.objects;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class UserObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    public int id;

    @Column (name = "username")
    private String username;

    @Column (name = "password")
    private String password;

    @Column (name = "token")
    private String token;

    @Transient
    private List<Organization> organizations;

    //getters and setters:
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }


    public List<Organization> organizations() {
        return organizations;
    }
    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    //end of getters and setters


}
