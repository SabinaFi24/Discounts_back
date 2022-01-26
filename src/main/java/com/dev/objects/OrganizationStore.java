package com.dev.objects;
import javax.persistence.*;

    @Entity
    @Table( name = "organizations_store")
    public class OrganizationStore {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column (name="id")
        public int id;

        @ManyToOne
        @JoinColumn(name = "organizations")
        private OrganizationObject organizations ;

        @ManyToOne
        @JoinColumn(name = "store")
        private StoreObject store ;

        //constructors:
        public OrganizationStore(OrganizationObject organizations, StoreObject store) {
            this.organizations = organizations;
            this.store = store;
        }

        public OrganizationStore() {}

        //getters and setters:
        public int getId() {return id;}
        public void setId(int id) {this.id = id;}

        public OrganizationObject getOrganizations() {return organizations;}
        public void setOrganizations(OrganizationObject organizations) {this.organizations = organizations;}

        public StoreObject getStore() {return store;}
        public void setStore(StoreObject store) {this.store = store;}

    }


