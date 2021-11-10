package com.eseo.projetAndroid.ui.createGroup;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import com.eseo.projetAndroid.databinding.ActivityCreateGroupBinding;
import com.eseo.projetAndroid.manager.GroupManager;
import com.eseo.projetAndroid.manager.UserManager;

import com.eseo.projetAndroid.ui.BaseActivity;

public class CreateGroupActivity extends BaseActivity<ActivityCreateGroupBinding> {

    private UserManager userManager = UserManager.getInstance();
    private GroupManager groupManager = GroupManager.getInstance();



    @Override
    protected ActivityCreateGroupBinding getViewBinding() {
        return ActivityCreateGroupBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListeners();
    }


    private void setupListeners() {
        binding.createButton.setOnClickListener(view -> {
            createSalon();
            this.finish();
        });


    }

    public void createSalon() {

        boolean canCreate = !TextUtils.isEmpty(binding.TextCreate.getText()) && userManager.isCurrentUserLogged();

        if (canCreate){
            // Create a new message for the chat
            groupManager.createGroup(binding.TextCreate.getText().toString());


            // Reset text field
            binding.TextCreate.setText("");
            showSnackBar("Le salon a été crée !");
        }
        else{
            showSnackBar("Vous devez d'abord entrer un nomde groupe");
        }
    }


    // Show Snack Bar with a message
    private void showSnackBar( String message){
        Snackbar.make(binding.createLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}
