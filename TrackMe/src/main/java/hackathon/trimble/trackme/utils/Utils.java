package hackathon.trimble.trackme.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class Utils{
     public static String ENCODED_KEY = "4A11B37DF84BB62C1674E9B4EAAA4E60";
     public static String PUBLIC_KEY= "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJOYb/hCi53ASk/xSNMBINrNaBDJsWrAhRLi4Fa3Va8T2YZIr+azQ4gl1N6NF+dZ9xkh3v5+J7xne5icF0WO0RkCAwEAAQ==";
     public static String SPLIT_CHAR = "\\|";
     public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, secKey);
            byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
            return new String(bytePlainText);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static boolean validateSignature(String data, String sign) throws NoSuchAlgorithmException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException, CertificateException, IOException, SignatureException {
        try {
            Signature sig = Signature.getInstance("MD5WithRSA");
            byte[] signatureBytes =  Base64.decode(sign.getBytes(), Base64.DEFAULT);
            PublicKey publicKey=getKey(PUBLIC_KEY);
            if(publicKey!=null) {
                sig.initVerify(publicKey);
            }
            sig.update(data.getBytes("UTF8"));
            return sig.verify(signatureBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static PublicKey getKey(String key){
        try{
            byte[] byteKey = Base64.decode(key, Base64.DEFAULT);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey=kf.generatePublic(X509publicKey);
            return publicKey;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @SuppressWarnings("unused")
    public static byte[] base64ToByte(String data) throws Exception {
        try {
            return android.util.Base64.decode(data, android.util.Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public static String GetAppVersion(Context context){
        String version = "";
        try{
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pinfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static List<String> SplitStringGetContents(String data){
         try{

             List<String> items = new ArrayList<String>(Arrays.asList(data.split(SPLIT_CHAR)));
            return items;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  static HashMap<String , String> getMappingPojo(){
        try {
            HashMap fieldMap=new HashMap<>();
            fieldMap.put("1","id");
            fieldMap.put("2","name");
            fieldMap.put("3","fatherName");
            fieldMap.put("4","motherName");
            fieldMap.put("5","dateOfBirth");
            fieldMap.put("6","placeOfBirth");
            fieldMap.put("7","gender");
            fieldMap.put("8","bloodGroup");
            fieldMap.put("9","presentAddress");
            fieldMap.put("10","permanentAddress");
            fieldMap.put("11","registrationNo");
            fieldMap.put("12","dateOfIssue");
            fieldMap.put("13","registrar");
            fieldMap.put("14","block");
            fieldMap.put("15","district");
            fieldMap.put("16","state");
            fieldMap.put("17","remarks");
            fieldMap.put("18","profileImageUrl");
            fieldMap.put("19","createdAt");

            return  fieldMap;
        } catch (Exception e) {
            return    new HashMap<>();
        }
    }

    public  static String getImageUrl(String image){
        try {
            Log.e("image",AppProperties.getInstance().get("baseUrl")+image);
            return AppProperties.getInstance().get("baseUrl")+image;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static boolean isNetworkAvailable(final Context context) {
        try {
            final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
