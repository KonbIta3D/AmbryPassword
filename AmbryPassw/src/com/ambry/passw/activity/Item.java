package com.ambry.passw.activity;

/**
 * Created with IntelliJ IDEA.
 * User: stalker
 * Date: 15.08.13
 * Time: 9:41
 * To change this template use File | Settings | File Templates.
 */
public class Item {
    private CharSequence login;
    private CharSequence password;
    private CharSequence comment;
    private long id;

    public CharSequence getLogin(){
        return login;
    }
    public CharSequence getPassword(){
        return password;
    }
    public CharSequence getComment(){
        return comment;
    }
    public long getId(){
        return id;
    }

    public Item(String login, String password, String comment, long id){
        this.login=login;
        this.password=password;
        this.comment=comment;
        this.id=id;
    }
}
