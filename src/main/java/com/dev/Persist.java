package com.dev;

import com.dev.objects.OrganizationObject;
import com.dev.objects.SaleObject;
import com.dev.objects.StoreObject;
import com.dev.objects.UserObject;
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
    public List<OrganizationObject> getAllOrganizations (String token) {
        List<OrganizationObject> OrganizationObjects = null;
        Session session = sessionFactory.openSession();
        OrganizationObjects = (List<OrganizationObject>)session.createQuery( "FROM OrganizationObject").list();
        session.close();
        return OrganizationObjects;
    }

    //all the organizations that the user is friend in:
    public List<OrganizationObject> getOrganizationByUser (String token) {
        List<OrganizationObject> OrganizationObjects = null;
        Session session = sessionFactory.openSession();
        OrganizationObjects = (List<OrganizationObject>)session.createQuery(
                "FROM OrganizationObject " +
                        "WHERE UserObject.token = :token " +
                        "ORDER BY id DESC ")
                .setParameter("token", token)
                .list();
        session.close();
        return OrganizationObjects;
    }
    //end of organizations.

    //related to STORE:
    //all the Stores that exist
    public List<StoreObject> getAllStores (String token) {
        List<StoreObject> storeObjects = null;
        Session session = sessionFactory.openSession();
        storeObjects = (List<StoreObject>)session.createQuery(
                        "FROM StoreObject " +
                                "WHERE UserObject.token = :token " +
                                "ORDER BY id DESC ")
                .setParameter("token", token)
                .list();
        session.close();
        return storeObjects;
    }

    public List<StoreObject> getStoresByOrganization(int organizationId) {
        List<StoreObject> storesObjects = null;
        Session session = sessionFactory.openSession();
        storesObjects = (List<StoreObject>)session.createQuery(
                        "FROM StoreObject " +
                                "WHERE OrganizationObject.OrganizationId = :organizationId " +
                                "ORDER BY id DESC ")
                .setParameter("organizationId", organizationId)
                .list();
        session.close();
        return storesObjects;
    }
    //end of store.

    //related to SALE:
    public List<SaleObject> getSalesByUser(String token) {
        List<SaleObject> saleObjects = null;
        Session session = sessionFactory.openSession();
        saleObjects = (List<SaleObject>)session.createQuery(
                        "FROM SaleObject " +
                                "WHERE UserObject.token = :token " +
                                "ORDER BY id DESC ")
                .setParameter("token", token)
                .list();
        session.close();
        return saleObjects;
    }
    //check if it's make sense:
    public List<SaleObject> getSaleByStore(int storeId) {
        List<SaleObject> saleObjects = null;
        Session session = sessionFactory.openSession();
        saleObjects = (List<SaleObject>)session.createQuery(
                        "FROM SaleObject " +
                                "WHERE SaleObject.store.storeId = :storeId " +
                                "ORDER BY id DESC ")
                .setParameter("storeId", storeId)
                .list();
        session.close();
        return saleObjects;
    }


    //end of sale.



/*
    public boolean removePost (String token, int postId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("DELETE FROM PostObject WHERE id = :id AND userObject.token = :token")
                .setParameter("id", postId)
                .setParameter("token", token)
                .executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }
    public List<UserObject> getFollowed(String token){
        Session session = sessionFactory.openSession();
        List<UserObject> userObjects = (List<UserObject>) session.createQuery(
                "SELECT f.followed FROM FollowsRelations f WHERE f.following.token= :token")
                .setParameter("token",token).list();
        session.close();
        return userObjects;
    }
    public Integer addPost (String token, String content) {
        Integer id=null;
        boolean success = false;
        Integer userId = getUserIdByToken(token);
        if (userId != null) {
            PostObject postObject = new PostObject();
            UserObject userObject = new UserObject();

            userObject.setId(userId);
            postObject.setUserObject(userObject);
            postObject.setContent(content);

            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(postObject);
            transaction.commit();
            session.close();
            id=postObject.getId();
        }
        return id;
    }*/
}
