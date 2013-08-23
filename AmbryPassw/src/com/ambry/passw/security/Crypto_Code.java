package com.ambry.passw.security;

/**
 * Created by pcuser on 14.08.13.
 */
public class Crypto_Code {


    public String Crypto_Code_String(String stringToCode){

        String retVal = "";
        int position =0;
        int lehgth = 0;
        int cryptoChar=255;

        lehgth = stringToCode.length();
        for (position = 0;position<lehgth;position++){
            retVal+=Crypto_XOR_Char(stringToCode.charAt(position),(char)cryptoChar);
            if (cryptoChar==0)cryptoChar=255;
        }
        return retVal;
    }

    public char Crypto_XOR_Char(char charToCode, char keyChar){
        return (char)(charToCode^keyChar);
    }

    public String encrypt(String text, String keyword)
    {
        byte[] arr = text.getBytes();
        byte[] keyarr =keyword.getBytes();
        byte[] result = new byte[arr.length];

        for (int i = 0;i<arr.length; i++){
            result[i]=(byte) (arr[i]^keyarr[i %keyarr.length]);
        }
        return new String(result);
    }

    public String decrypt(byte[] text, String keyword){
        byte[] result = new byte[text.length];
        byte[] keyarr = keyword.getBytes();
        for (int i=0; i<text.length; i++){
            result[i]=(byte)(text[i]^keyarr[i%keyarr.length]);
                 }
        return new String(result);
    }
}
