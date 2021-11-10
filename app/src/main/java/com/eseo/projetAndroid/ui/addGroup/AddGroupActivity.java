package com.eseo.projetAndroid.ui.addGroup;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.Nullable;
import com.eseo.projetAndroid.databinding.ActivityAddGroupBinding;
import com.eseo.projetAndroid.manager.GroupManager;
import com.eseo.projetAndroid.manager.UserManager;
import com.eseo.projetAndroid.models.Salon;
import com.eseo.projetAndroid.models.User;
import com.eseo.projetAndroid.ui.BaseActivity;

import java.util.Date;


public class AddGroupActivity extends BaseActivity<ActivityAddGroupBinding> {

        private UserManager userManager = UserManager.getInstance();
        private GroupManager groupManager = GroupManager.getInstance();
        private Salon salon = new Salon();


        @Override
        protected ActivityAddGroupBinding getViewBinding() {
            return ActivityAddGroupBinding.inflate(getLayoutInflater());
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setupListeners();
        }


        private void setupListeners() {
                binding.addButton.setOnClickListener(view -> {
                        addSalon();
                        this.finish();

                });
        }


        public void addSalon() {

                boolean canAdd = !TextUtils.isEmpty(binding.textInputEditText.getText()) && userManager.isCurrentUserLogged();

                if (canAdd){
                        String cleSalon = binding.textInputEditText.getText().toString();
                        User user = new User(userManager.getCurrentUser().getUid(), userManager.getCurrentUser().getDisplayName(), null);

                        salon.addMembers(user, cleSalon);
                        // Reset text field
                        binding.textInputEditText.setText("");
                        showSnackBar("Le salon a été ajouté !");
                }
                else{
                        showSnackBar("Vous devez rentrer une clé de salon valide !");
                }
        }

        private void showSnackBar( String message){
                Snackbar.make(binding.forSnackBar, message, Snackbar.LENGTH_SHORT).show();
        }


}
