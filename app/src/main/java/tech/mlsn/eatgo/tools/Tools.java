package tech.mlsn.eatgo.tools;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import tech.mlsn.eatgo.R;


/**
 * Created by Rzzkan on 10/05/2021.
 */
public class Tools {
    public static void addFragment(FragmentActivity activity, Fragment fragment, Bundle bundle , String tag) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.addToBackStack(tag);
        ft.replace(R.id.main_content, fragment, tag);
        ft.commitAllowingStateLoss();
        fragment.setArguments(bundle);
    }

    public static void removeAllFragment(FragmentActivity activity,Fragment replaceFragment, String tag) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        manager.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.replace(R.id.main_content, replaceFragment);
        ft.commitAllowingStateLoss();
    }

    public static String distanceFormat(String number){
        double x = Double.valueOf(number);
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(x).toString();
    }

    public static String currency (String value){
        String formattedNumber = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(Double.valueOf(value));
        return formattedNumber;
    }

}
