package com.eseo.projetAndroid.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.eseo.projetAndroid.manager.UserManager;
import com.eseo.projetAndroid.models.Salon;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GroupRepository {

private static final String SALONS_COLLECTION = "salons";

private static volatile GroupRepository instance;
private UserManager userManager;
private Salon salon;

private GroupRepository() { this.userManager = UserManager.getInstance(); }


public static GroupRepository getInstance() {
    GroupRepository result = instance;
    if (result != null) {
        return result;
    }
    synchronized(GroupRepository.class) {
        if (instance == null) {
            instance = new GroupRepository();
        }
        return instance;
    }
}

public CollectionReference getGroupCollection(){
    return FirebaseFirestore.getInstance().collection(SALONS_COLLECTION);
}

public Query getAllRooms(){
    return  FirebaseFirestore.getInstance().collection("salons").whereEqualTo("members."+userManager.getCurrentUser().getUid(), userManager.getCurrentUser().getUid()).orderBy("dateCreated", Query.Direction.DESCENDING);
}


// Create salon in Firestore
public void createGroup(String nom) {
    userManager.getUserData().addOnSuccessListener(user -> {
        // Create the Message object
        Map<String, String> members = new HashMap<>();
        members.put(user.getUid().toString(), user.getUid().toString());
        Salon salon = new Salon(nom,user.getUid());
        salon.setMembers(members);
        Date date = new Date();
        long time = date.getTime();
        salon.setTime(time);

        this.getGroupCollection().add(salon);

    });

    }

    public Query getLastSalon(){
        return FirebaseFirestore.getInstance().collection("salons")
                .orderBy("dateCreated", Query.Direction.DESCENDING)
                .limit(1);
    }

    public void addScrum (String uidSalon, String uidUSer){
        userManager.getUserData().addOnSuccessListener(user -> {

            FirebaseFirestore.getInstance().collection("salons")
                    .document(uidSalon).collection("members").document(user.getUid()).set(user);


        });
    }




}
