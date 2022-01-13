package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.OrganizationObject;
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

    @RequestMapping("create-account")
    public boolean createAccount (String username, String password) {
        boolean success = false;
        boolean alreadyExists = persist.getTokenByUsernameAndPassword(username, password) != null;
        if (!alreadyExists) {
            UserObject userObject = new UserObject();
            userObject.setUsername(username);
            userObject.setPassword(password);
            String hash = Utils.createHash(username, password);
            userObject.setToken(hash);
            success = persist.addAccount(userObject);
        }

        return success;
    }


    /*@RequestMapping(value = "/add-post", headers = "content-type=multipart/*", method = RequestMethod.POST)
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
    }*/


    @RequestMapping("get-stores")
    public List<StoreObject> getStore (String token) {
        return persist.getStoresByUser(token);
    }

    @RequestMapping("get-stores")
    public List<OrganizationObject> getOrganization (String token) {
        return persist.getOrganizationByUser(token);
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

    }








}
