package com.dev;

import com.dev.objects.PostObject;
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
        if (userObject.getId() > 0) {
            success = true;
        }
        return success;
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
    }

    public Integer getUserIdByToken (String token) {
        Integer id = null;
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject WHERE token = :token")
                .setParameter("token", token)
                .uniqueResult();
        session.close();
        if (userObject != null) {
            id = userObject.getId();
        }
        return id;

    }

    public List<PostObject> getPostsByUser (String token) {
        List<PostObject> postObjects = null;
        Session session = sessionFactory.openSession();
        postObjects = (List<PostObject>)session.createQuery(
                "FROM PostObject " +
                        "WHERE userObject.token = :token " +
                        "ORDER BY id DESC ")
                .setParameter("token", token)
                .list();
        session.close();
        return postObjects;
    }

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
}
