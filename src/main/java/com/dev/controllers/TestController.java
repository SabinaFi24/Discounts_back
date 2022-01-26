package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.OrganizationObject;
import com.dev.objects.SaleObject;
import com.dev.objects.StoreObject;
import com.dev.objects.UserObject;
import com.dev.utils.FileBrowserUtil;
import com.dev.utils.Utils;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


@RestController
public class TestController {


    @Autowired
    private Persist persist;

    @PostConstruct
    private void init () {

    }

    @RequestMapping("sign-in")
    public String signIn (String username, String password) {
        String token = persist.getTokenByUsernameAndPassword(username, password);
        return token;
    }
    @RequestMapping("first-sign-in")
    public boolean firstSignIn (String token){
        return persist.isFirstSignIn(token);
    }
    @RequestMapping(value = "after-first-sign-in",method = RequestMethod.POST )
    public void afterFirstSignIn (@RequestParam String token){
        persist.afterFirstSignIn(token);
    }


    @RequestMapping("create-account")
    public boolean createAccount (String username, String password) {
        boolean success = false;
        boolean alreadyExists = persist.getTokenByUsername(username) != null;
        if (!alreadyExists) {
            UserObject userObject = new UserObject();
            userObject.setUsername(username);
            userObject.setPassword(password);
            userObject.setFirstLogIn(0);
            String hash = Utils.createHash(username, password);
            userObject.setToken(hash);
            success = persist.addAccount(userObject);
        }

        return success;
    }

    //related to organization:
    @RequestMapping("get-all-organizations")
    public List<OrganizationObject> getAllOrganization() {return persist.getAllOrganizations();}

    @RequestMapping("get-organizations-by-user")
    public List<OrganizationObject> getOrganizationByUser (String token) {return persist.getOrganizationByUser(token);}

    //related to store:
    @RequestMapping("get-stores-by-organization")
    public List<StoreObject> getStoresByOrganization (int organizationId) {return persist.getStoresByOrganization(organizationId);}
    @RequestMapping("get-all-stores")
    public List<StoreObject> getAllStores () {return persist.getAllStores();}

    @RequestMapping(value = "get-store-name-by-store-id")
    public String getStoreNameByStoreId (int storeId){
        return persist.getStoreNameByStoreId(storeId);
    }

    //related to sale:
    @RequestMapping("get-sales-by-user")
    public boolean doesUserDeserveSale (String token , int saleId) {
        return persist.doesUserDeserveSale(token,saleId);
    }

    @RequestMapping("get-all-sales")
    public List<SaleObject> getAllSales () {
        return persist.getAllSales();
    }

    @RequestMapping("settings-change")
    public boolean settingsChange(String token,int organizationId){
        return persist.settingChange(token,organizationId);
    }
    @RequestMapping(value = "get-sales-by-store-id")
    public List<SaleObject> getSalesByStoreIdStoreId (int storeId){
        return persist.getSalesByStoreId(storeId);
    }





    /* irrelevant by now:

    @RequestMapping(value = "/add-post", headers = "content-type=multipart/*", method = RequestMethod.POST)
    public boolean addPost (@RequestParam(value = "file", required = false) MultipartFile multipartFile, String token, String content) {
        Integer postId = persist.addPost(token, content);
        if (multipartFile != null && postId != null) {
            try {
                File fileOnDisk = new File(FileBrowserUtil.getPathToPostImage(postId));
                multipartFile.transferTo(fileOnDisk);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return postId != null;
    }
    @RequestMapping("remove-post")
    public boolean removePost (String token, int postId) {
        return persist.removePost(token, postId);
    }


    @RequestMapping(value = "/get-post-image", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public void postImage (HttpServletResponse response, int postId) {
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        String path = FileBrowserUtil.getPathToPostImage(postId);
        if (new File(path).exists()) {
            serveImage(response, path);
        }

    }

    public static void serveImage (HttpServletResponse response, String path) {
        try {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            FileInputStream fileInputStream = new FileInputStream(new File(path));
            StreamUtils.copy(fileInputStream, response.getOutputStream());
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/get-followed", method = RequestMethod.GET)
    public List<UserObject> getFollowed (String token) {
       return persist.getFollowed(token);

    }*/








}
