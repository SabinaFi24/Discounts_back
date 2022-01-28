package com.dev.objects;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table( name = "organization")
public class OrganizationObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @Column (name = "name")
    private String name;

    //getters and setters:
    public int getOrganizationId() {return id;}
    public void setOrganizationId(int id) {id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    //end of getters and setters
}


