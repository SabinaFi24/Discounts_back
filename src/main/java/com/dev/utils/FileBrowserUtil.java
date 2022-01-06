package com.dev.utils;

public class FileBrowserUtil {
    public static String getPathToPostImage ( int postId){
        return  String.format("%s\\%d.jpg",System.getProperty("user.home"),postId);
    }
}
