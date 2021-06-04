package tech.mlsn.eatgo.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class SPManager {
    public static final String SP_LOGIN_APP = "spLoginApp";
    public static final String SP_ID = "spId";
    public static final String SP_ID_RESTO = "spId_Resto";
    public static final String SP_NAME = "spName";
    public static final String SP_NAME_RESTO = "spNameResto";
    public static final String SP_USERNAME = "spUsername";
    public static final String SP_PHONE = "spPhone";
    public static final String SP_PHONE_RESTO = "spPhoneResto";
    public static final String SP_ADDRESS = "spAddress";
    public static final String SP_ADDRESS_RESTO = "spAddressResto";
    public static final String SP_PASSWORD = "spPassword";
    public static final String SP_ROLE = "spRole";
    public static final String SP_IMG = "spImg";
    public static final String SP_IMG_RESTO = "spImgResto";
    public static final String SP_LINK_RESTO = "spLinkResto";
    public static final String SP_IS_SIGNED = "spSignedIn";
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SPManager(Context context){
        sp = context.getSharedPreferences(SP_LOGIN_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.apply();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.apply();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.apply();
    }

    public void removeSP(){
        spEditor.remove(SP_ID);
        spEditor.remove(SP_ID_RESTO);
        spEditor.remove(SP_NAME);
        spEditor.remove(SP_NAME_RESTO);
        spEditor.remove(SP_USERNAME);
        spEditor.remove(SP_PASSWORD);
        spEditor.remove(SP_PHONE);
        spEditor.remove(SP_ADDRESS);
        spEditor.remove(SP_ROLE);
        spEditor.remove(SP_IMG);
        spEditor.remove(SP_IMG_RESTO);
        spEditor.remove(SP_LINK_RESTO);
        spEditor.remove(SP_PHONE_RESTO);
        spEditor.apply();
    }

    public String getSpName(){
        return sp.getString(SP_NAME, "EatGo");
    }

    public String getSpUsername(){
        return sp.getString(SP_USERNAME, "");
    }

    public String getSpPassword(){
        return sp.getString(SP_PASSWORD, "");
    }

    public String getSpPhone(){
        return sp.getString(SP_PHONE, "");
    }
    public String getSpPhoneResto(){
        return sp.getString(SP_PHONE_RESTO, "");
    }

    public String getSpRole(){
        return sp.getString(SP_ROLE, "");
    }

    public String getSpId(){
        return sp.getString(SP_ID, "");
    }

    public String getSpImg(){
        return sp.getString(SP_IMG, "null");
    }

    public String getSpAddress(){
        return sp.getString(SP_ADDRESS, "");
    }


    public String getSpIdResto(){
        return sp.getString(SP_ID_RESTO, "");
    }

    public String getSpLinkResto(){
        return sp.getString(SP_LINK_RESTO, "");
    }

    public String getSpNameResto(){
        return sp.getString(SP_NAME_RESTO, "");
    }

    public String getSpImgResto(){
        return sp.getString(SP_IMG_RESTO, "null");
    }

    public String getSpAddressResto(){
        return sp.getString(SP_ADDRESS_RESTO, "");
    }

    public Boolean getSpIsSigned(){
        return sp.getBoolean(SP_IS_SIGNED, false);
    }


}
