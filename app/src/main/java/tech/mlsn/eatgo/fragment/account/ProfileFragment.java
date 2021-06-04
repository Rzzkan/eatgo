package tech.mlsn.eatgo.fragment.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.activity.LoginActivity;
import tech.mlsn.eatgo.activity.MainActivity;
import tech.mlsn.eatgo.fragment.dashboard.AdminDashboardFragment;
import tech.mlsn.eatgo.fragment.dashboard.RestoDasboardFragment;
import tech.mlsn.eatgo.fragment.dashboard.UserDashboardFragment;
import tech.mlsn.eatgo.fragment.menus.AllMenusFragment;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.Tools;

public class ProfileFragment extends Fragment {
    Button btnChangeProfile, btnChangeRestaurant, btnChangePassword, btnMenu, btnLogout;
    SPManager spManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initialization(view);
        return view;
    }

    private void initialization(View view){
        btnChangeProfile = view.findViewById(R.id.btnChangeProfile);
        btnChangeRestaurant = view.findViewById(R.id.btnChangeRestaurant);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnMenu = view.findViewById(R.id.btnMenu);
        btnLogout = view.findViewById(R.id.btnLogout);
        spManager = new SPManager(getContext());

        if (spManager.getSpRole().equalsIgnoreCase("resto")){
            btnChangeRestaurant.setVisibility(View.VISIBLE);
            btnMenu.setVisibility(View.VISIBLE);
        }else {
            btnChangeRestaurant.setVisibility(View.GONE);
            btnMenu.setVisibility(View.GONE);
        }

        btnChangeProfile.setOnClickListener(new clickListener(btnChangeProfile));
        btnChangeRestaurant.setOnClickListener(new clickListener(btnChangeRestaurant));
        btnChangePassword.setOnClickListener(new clickListener(btnChangePassword));
        btnMenu.setOnClickListener(new clickListener(btnMenu));
        btnLogout.setOnClickListener(new clickListener(btnLogout));
    }

    private class clickListener implements View.OnClickListener{
        Button btn;

        public clickListener(Button btn) {
            this.btn = btn;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnChangeProfile:
                    Tools.addFragment(getActivity(), new UpdateProfileFragment(),null,"update-profile");
                    break;
                case R.id.btnChangeRestaurant:
                    Tools.addFragment(getActivity(), new UpdateRestaurantFragment(),null,"update-restaurant");
                    break;
                case R.id.btnChangePassword:
                    Tools.addFragment(getActivity(), new UpdatePasswordFragment(),null,"update-password");
                    break;
                case R.id.btnMenu:
                    Tools.addFragment(getActivity(), new AllMenusFragment(), null, "all-menu");
                    break;
                case R.id.btnLogout:
                    logoutDialog();
                    break;
            }
        }
    }

    public void logoutDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle    ("Logout");
        builder.setMessage("Are you sure to Logout ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                spManager.saveSPBoolean(spManager.SP_IS_SIGNED, false);
                spManager.removeSP();
                startActivity(new Intent(getActivity(), LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}