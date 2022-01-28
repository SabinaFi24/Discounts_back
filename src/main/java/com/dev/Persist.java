package com.dev;

import com.dev.objects.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class Persist {
    private Connection connection;

    private final SessionFactory sessionFactory;

    @Autowired
    public Persist (SessionFactory sf) {
        this.sessionFactory = sf;
    }


    @PostConstruct
    public void createConnectionToDatabase () {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ashCollege", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getTokenByUsernameAndPassword(String username, String password) {
        String token = null;
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject WHERE username = :username AND password = :password")
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
        session.close();
        if (userObject != null) {
            token = userObject.getToken();
        }
        return token;
    }
    public String getTokenByUsername(String username) {
        String token = null;
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject WHERE username = :username")
                .setParameter("username", username)
                .uniqueResult();
        session.close();
        if (userObject != null) {
            token = userObject.getToken();
        }
        return token;
    }

    public boolean addAccount (UserObject userObject) {
        boolean success = false;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(userObject);
        transaction.commit();
        session.close();
        if (userObject.getUserId() > 0) {
            success = true;
        }
        return success;
    }

    public boolean isFirstSignIn(String token) {
        UserObject userObject = getUserByToken(token);
        if (userObject.getFirstLogIn() == 0) {;
            return true;
        } else {
            return false;
        }
    }
    public void afterFirstSignIn(String token) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserObject userObject = getUserByToken(token);
        int firstLogin = userObject.getFirstLogIn()+1;
        userObject.setFirstLogIn(firstLogin);
        session.saveOrUpdate(userObject);
        transaction.commit();
        session.close();
    }

    public Integer getUserIdByToken (String token) {
        Integer id = null;
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject WHERE token = :token")
                .setParameter("token", token)
                .uniqueResult();
        session.close();
        if (userObject != null) {
            id = userObject.getUserId();
        }
        return id;

    }
    public UserObject getUserByToken (String token ){
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject u WHERE u.token = :token")
                .setParameter("token",token)
                .uniqueResult();
        session.close();
        return userObject;
    }

    //related to ORGANIZATIONS:
    // get all organizations:
    public List<OrganizationObject> getAllOrganizations () {
        List<OrganizationObject> OrganizationObjects = null;
        Session session = sessionFactory.openSession();
        OrganizationObjects = (List<OrganizationObject>)session.createQuery( "FROM OrganizationObject").list();
        session.close();
        return OrganizationObjects;
    }

    //get organizations by user:
    public List<OrganizationObject> getOrganizationByUser (String token) {
        List<OrganizationObject> OrganizationObjects = null;
        Session session = sessionFactory.openSession();
        OrganizationObjects = session.createQuery("SELECT organizations FROM UserOrganizations WHERE UserObject.id = :id")
                .setParameter("id",getUserByToken(token).getUserId())
                .list();
        session.close();
        return OrganizationObjects;
    }
    private Object getOrganizationByOrganizationId(int id) {
        Session session = sessionFactory.openSession();
        OrganizationObject organizationObject = (OrganizationObject) session.createQuery("FROM OrganizationObject o WHERE o.id = :id")
                .setParameter("id",id)
                .uniqueResult();
        session.close();
        return organizationObject;
    }

    //end of organizations.

    //related to STORE:

    // get all the Stores that exist:
    public List<StoreObject> getAllStores (){
        List<StoreObject> stores = null;
        Session session = sessionFactory.openSession();
        stores = (List<StoreObject>)session.createQuery( "FROM StoreObject").list();
        session.close();
        return stores;
    }
    //get store by organization:
    public List<StoreObject> getStoresByOrganization(int id) {
        List<StoreObject> storesObjects = null;
        Session session = sessionFactory.openSession();
        storesObjects = (List<StoreObject>)session.createQuery(
                        "FROM StoreObject WHERE OrganizationObject.id = :id")
                .setParameter("id", id)
                .list();
        session.close();
        return storesObjects;
    }
    //get store by id:
    public StoreObject getStoreByStoreId(int id){
        Session session = sessionFactory.openSession();
        StoreObject store = (StoreObject) session.createQuery("FROM StoreObject  WHERE id =:id")
                .setParameter("id",id)
                .uniqueResult();
        session.close();
        return store;
    }
    //get store name by store id:
    public String getStoreNameByStoreId(int id){
        String name = null;
        Session session = sessionFactory.openSession();
        StoreObject storeObject = (StoreObject) session.createQuery
                        ("FROM StoreObject WHERE StoreObject.id = :id")
                .setParameter("id", id)
                .uniqueResult();
        session.close();
        if (storeObject != null) {
            name = storeObject.getName();
        }
        return name;
    }

    //end of store.

    //related to SALE:
    //get all sales:
    public List<SaleObject> getAllSales (){
        List<SaleObject> sales = null;
        Session session = sessionFactory.openSession();
        sales = (List<SaleObject>)session.createQuery
                ( "FROM SaleObject").list();
        session.close();
        return sales;
    }
    // does the store has the sale:
    public boolean doesUserDeserveSale(String token , int id) {
        Session session= sessionFactory.openSession();
        StoreObject store = (StoreObject) session.createQuery("SELECT store FROM SaleObject s WHERE s.id=:id")
                .setParameter("id",id)
                .uniqueResult();
        session.close();
        return doseStoreBelongToUser(token, store.getStoreId());
    }
    //does user friend in organization that work with the store that has the sale:
    private boolean doseStoreBelongToUser(String token, int id) {
        List<OrganizationObject> organization = null;
        Session session= sessionFactory.openSession();
         organization = (List<OrganizationObject>)session.createQuery("SELECT organizations FROM OrganizationStore o WHERE o.store.id=:id")
                .setParameter("id",id)
                .uniqueResult();
        session.close();
        if (organization!=null){
            return true;
        }else{
            return true;
        }
    }

    //get sale by store:
    public List<SaleObject> getSalesByStoreId(int id) {
        List<SaleObject> saleObjects = null;
        Session session = sessionFactory.openSession();
        saleObjects = (List<SaleObject>)session.createQuery(
                        "FROM SaleObject " +
                                "WHERE SaleObject.store.id = :id")
                .setParameter("id", id)
                .list();
        session.close();
        return saleObjects;
    }
    //get all sales related to user:
    public List<SaleObject> getSalesByUser(String token) {
        Session session = sessionFactory.openSession();
        List<SaleObject> sales = new ArrayList<>();
        List<OrganizationObject> organizations = getOrganizationByUser(token);

        for (OrganizationObject organization : organizations ){
            List<StoreObject> stores = getStoresByOrganization(organization.getOrganizationId());
            for (StoreObject store : stores) {
                sales.addAll(getSalesByStoreId(store.getStoreId()));
            }

        }
        session.close();
        return sales;
    }
    //end of sale.

    //settings:
    public boolean settingChange(String token, int organizationId){
        //there is such user:
        if (getUserByToken(token) != null) {
            //user is already in the organization:
            if (doseUserInOrganization(token, organizationId)) {
                //so the user unmarked his friendship in organization:
                removeUserFromOrganization(token, organizationId);
                    return false;
            } else {
                addUserToOrganization(token, organizationId);
                    return true;
            }
        }
        return false;
    }
    //does the user is friend in organization:
    public boolean doseUserInOrganization(String token, int organizationId){
        List<OrganizationObject> organizations = null;
        Session session = sessionFactory.openSession();
        organizations = (List<OrganizationObject>)session.createQuery(
                "SELECT organizations FROM UserOrganizations u WHERE u.userObject.id =:userId " +
                        "AND u.organizations.id =:organizationId")
                .setParameter("userId",getUserByToken(token).getUserId())
                .setParameter("organizationId",organizationId)
                .uniqueResult();
        session.close();
        if (organizations!=null) {
            return true;
        }else {
            return false;
        }
    }
    // remove users friendship:
    public void removeUserFromOrganization (String token, int organizationId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("DELETE FROM UserOrganizations u WHERE u.userObject.id = :userId AND u.organizations.id =:organizationId")
                .setParameter("userId",getUserByToken(token).getUserId())
                .setParameter("organizationId",organizationId)
                .executeUpdate();
        transaction.commit();
        session.close();
    }
    //add the organization to the userOrganization relationship:
    public void addUserToOrganization(String token, int organizationId){
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UserOrganizations userOrganizations = new UserOrganizations
                    (getOrganizationByOrganizationId(organizationId),getUserByToken(token));
            session.save(userOrganizations);
            transaction.commit();
            session.close();

    }



}
