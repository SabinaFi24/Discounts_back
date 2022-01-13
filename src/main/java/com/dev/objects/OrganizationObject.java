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
        @Column (name = "organizationId")
        private int OrganizationId;

        @Column (name = "name")
        private String name;


        @ManyToMany
        @JoinTable (name = "Users_Organizations", joinColumns = {@JoinColumn(name="organizationId")},
                inverseJoinColumns = {@JoinColumn(name = "userId")})
        Set<UserObject> users = new HashSet<>();

         @ManyToMany
         @JoinTable (name = "Organizations_Sales", joinColumns = {@JoinColumn(name="organizationId")},
            inverseJoinColumns = {@JoinColumn(name = "saleId")})
         Set<SaleObject> sales = new HashSet<>();


        //getters and setters:
        public int getOrganizationId() {return OrganizationId;}
        public void setOrganizationId(int organizationId) {OrganizationId = organizationId;}

        public String getName() {return name;}
        public void setName(String name) {this.name = name;}

    public Set<UserObject> getUsers() {
        return users;
    }

    public void setUsers(Set<UserObject> users) {
        this.users = users;
    }

        //end of getters and setters
}


