package com.eseo.projetAndroid.ui.createGroup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.eseo.projetAndroid.R;
import com.eseo.projetAndroid.databinding.ActivityCreateGroupBinding;
import com.eseo.projetAndroid.manager.ChatManager;
import com.eseo.projetAndroid.manager.GroupManager;
import com.eseo.projetAndroid.manager.UserManager;

import com.eseo.projetAndroid.models.Salon;
import com.eseo.projetAndroid.models.User;
import com.eseo.projetAndroid.ui.BaseActivity;
import com.eseo.projetAndroid.ui.groups.GroupsActivity;

import java.util.HashMap;
import java.util.Map;

public class CreateGroupActivity extends BaseActivity<ActivityCreateGroupBinding> {

    private UserManager userManager = UserManager.getInstance();
    private GroupManager groupManager = GroupManager.getInstance();
    private ChatManager chatManager = ChatManager.getInstance();



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
