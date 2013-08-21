package com.ambry.passw.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by pcuser on 08.08.13.
 */
public class S_md5_Class {
    public S_md5_Class(){
     super();

    }


    public String md5(String in){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuffer sb = new StringBuffer(len<<1);
            for(int i = 0; i < len; i++){
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return  sb.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

}
