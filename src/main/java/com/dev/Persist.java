package com.dev;

import com.dev.objects.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


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
        UserObject userObject = (UserObject) session.createQuery
                        ("FROM UserObject u WHERE u.username = :username AND u.password = :password")
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
        UserObject userObject = (UserObject) session.createQuery
                        ("FROM UserObject u WHERE u.username = :username")
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
        if (userObject.getId() > 0) {
            success = true;
        }
        return success;
    }

    public boolean isFirstSignIn(String token) {
        UserObject userObject = getUserByToken(token);
        if (userObject.getFirstLogIn() == 0) {
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

    //RELATED TO USER:
    //get user id by token:
    public Integer getUserIdByToken (String token) {
        Integer id = null;
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject u WHERE u.token = :token")
                .setParameter("token", token)
                .uniqueResult();
        session.close();
        if (userObject != null) {
            id = userObject.getId();
        }
        return id;

    }
    //get user object by token
    public UserObject getUserByToken (String token ){
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject u WHERE u.token = :token")
                .setParameter("token",token)
                .uniqueResult();
        session.close();
        return userObject;
    }

    //get user object by organization id:
    public List<UserObject> getUserByOrganizationId(int organizationId){
        Session session = sessionFactory.openSession();
        List<UserObject> userObjectList;
        userObjectList = session.createQuery
                        ("SELECT UserObject FROM UserOrganizations u WHERE u.organizations.id=:id")
                .setParameter("id",organizationId)
                .list();
        session.close();
        return userObjectList;
    }
    //get all users:
    public List<UserObject> getAllUsers (){
        Session session = sessionFactory.openSession();
        List<UserObject> users = (List<UserObject>)session.createQuery( "FROM UserObject").list();
        session.close();
        return users;
    }
    //end of USER.

    //related to ORGANIZATIONS:
    // get all organizations:
    public List<OrganizationObject> getAllOrganizations () {
        Session session = sessionFactory.openSession();
        List<OrganizationObject> OrganizationObjects =session.createQuery
                ( "FROM OrganizationObject").list();
        session.close();
        return OrganizationObjects;
    }

    //get organizations by user:
    public List<OrganizationObject> getOrganizationByUser (String token) {
        Session session =sessionFactory.openSession();
        List <OrganizationObject> organizations = session.createQuery
                        ("SELECT organizations FROM UserOrganizations u WHERE u.userObject.id=:id ")
                .setParameter("id",getUserByToken(token).getId())
                .list();
        session.close();
        return organizations;
    }
    //get organization object by organization id:
    private OrganizationObject getOrganizationByOrganizationId(int id) {
        Session session = sessionFactory.openSession();
        OrganizationObject organizationObject = (OrganizationObject) session.createQuery
                        ("FROM OrganizationObject o WHERE o.id = :id")
                .setParameter("id",id)
                .uniqueResult();
        session.close();
        return organizationObject;
    }

    //dose store belong to organization
    public boolean doseStoreBelongToOrganization (int storeId , int organizationId){
        Session session = sessionFactory.openSession();
        OrganizationObject organizations=(OrganizationObject)session
                .createQuery("SELECT organizations FROM OrganizationStore o WHERE o.store.id =:storeId AND o.organizations.id =:organizationId")
                .setParameter("storeId",storeId)
                .setParameter("organizationId",organizationId)
                .uniqueResult();
        session.close();
        return organizations != null;

    }
    //end of organizations.

    //RELATED TO STORE:
    // get all the Stores:
    public List<StoreObject> getAllStores (){
        List<StoreObject> stores;
        Session session = sessionFactory.openSession();
        stores = (List<StoreObject>)session.createQuery( "FROM StoreObject").list();
        session.close();
        return stores;
    }
    //get store by organization:
    public List<StoreObject> getStoresByOrganization(int id) {
        Session session = sessionFactory.openSession();
        List <StoreObject> stores = session.createQuery
                        ("SELECT store FROM OrganizationStore o WHERE o.organizations.id=:id")
                .setParameter("id",id)
                .list();
        session.close();
        return stores;
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
                        ("FROM StoreObject s WHERE s.id = :id")
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
        Session session = sessionFactory.openSession();
        List<SaleObject> sales = (List<SaleObject>)session.createQuery
                ( "FROM SaleObject").list();
        session.close();
        return sales;
    }
    // does the store has the sale:
    //does user deserve sale:
    public boolean doesUserDeserveSale(String token , int id) {
        Session session= sessionFactory.openSession();
        StoreObject store = (StoreObject) session.createQuery
                        ("SELECT store FROM SaleObject s WHERE s.id=:id")
                .setParameter("id",id)
                .uniqueResult();
        session.close();
        return doseStoreBelongToUser(token, store.getId());
    }

    //does user friend in organization that work with the store that has the sale:
    //does store belong to user:
    private boolean doseStoreBelongToUser(String token, int id) {
        Session session= sessionFactory.openSession();
        List<OrganizationObject> organization = (List<OrganizationObject>)session.createQuery
                         ("SELECT organizations FROM OrganizationStore o WHERE o.store.id=:id")
                .setParameter("id",id)
                .uniqueResult();
        session.close();
        for(OrganizationObject organizations:organization) {
            return doseUserInOrganization(token, organizations.getOrganizationId());
        }
        return false;
    }

    //get sale by store id:
    public List<SaleObject> getSalesByStoreId(int id) {
        List<SaleObject> saleObjects = null;
        Session session = sessionFactory.openSession();
        saleObjects = (List<SaleObject>)session.createQuery(
                        "FROM SaleObject s WHERE s.store.id = :id")
                .setParameter("id", id)
                .list();
        session.close();
        return saleObjects;
    }

    //get all sales related to user by user token:
    public List<SaleObject> getSalesByUser(String token) {
        Session session = sessionFactory.openSession();
        List<SaleObject> sales = new ArrayList<>();
        List<OrganizationObject> organizations = getOrganizationByUser(token);

        for (OrganizationObject organization : organizations ){
            List<StoreObject> stores = getStoresByOrganization(organization.getOrganizationId());
            for (StoreObject store : stores) {
                sales.addAll(getSalesByStoreId(store.getId()));
            }

        }
        session.close();
        return sales;
    }
    //get start date of sale:
    public List<SaleObject> getStartSales(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        String currentDate = formatter.format(date);
        Session session = sessionFactory.openSession();
        List <SaleObject> sales = session.createQuery("FROM SaleObject s WHERE s.startDate=:currentDate")
                .setParameter("currentDate",currentDate)
                .list();
        session.close();
        return sales;
    }
    //get end date for sale:
    public List<SaleObject> getEndSales(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        String currentDate = formatter.format(date);
        Session session = sessionFactory.openSession();
        List <SaleObject> sales = session.createQuery("FROM SaleObject s WHERE s.endDate=:currentDate ")
                .setParameter("currentDate",currentDate)
                .list();
        session.close();
        return sales;
    }

    //end of sale.

    //SETTINGS:
    //change settings:
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
        Session session = sessionFactory.openSession();
        List<OrganizationObject> organizations  = (List<OrganizationObject>)session.createQuery(
                "SELECT organizations FROM UserOrganizations u WHERE u.userObject.id =:userId " +
                        "AND u.organizations.id =:organizationId")
                .setParameter("userId",getUserByToken(token).getId())
                .setParameter("organizationId",organizationId)
                .uniqueResult();
        session.close();
        return organizations != null;
    }
    // remove users friendship:
    public void removeUserFromOrganization (String token, int organizationId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserOrganizations organizationUserToDelete = (UserOrganizations) session.createQuery
                        (" FROM UserOrganizations u where u.userObject.id =:userId AND u.organizations.id =:organizationId")
                .setParameter("userId",getUserByToken(token).getId())
                .setParameter("organizationId",organizationId)
                .uniqueResult();
        UserOrganizations userOrganization = (UserOrganizations) session.load(UserOrganizations.class,organizationUserToDelete.getId());
        session.delete(userOrganization);
        transaction.commit();
        session.close();
    }
    //add the organization to the userOrganization relationship:
    public void addUserToOrganization(String token, int organizationId){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserOrganizations userOrganization = new UserOrganizations
                (getOrganizationByOrganizationId(organizationId),getUserByToken(token));
        session.save(userOrganization);
        transaction.commit();
        session.close();

    }

    //Delete multiple users:
    public List<UserObject> removeIfMultipleUsers(List<UserObject> userObjectList){
        List<UserObject> newList = new ArrayList<>();
        for (UserObject userObject : userObjectList){
            if (!newList.contains(userObject))
                newList.add(userObject);
        }
        return newList;
    }

}
