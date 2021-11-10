package com.eseo.projetAndroid.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.eseo.projetAndroid.manager.UserManager;
import com.eseo.projetAndroid.models.US;

import java.util.HashMap;
import java.util.Map;

public class PlanningPokerRepository {

    private static final String CHAT_COLLECTION = "salons";
    private static final String MESSAGE_COLLECTION = "messages";
    private static volatile PlanningPokerRepository instance;
    private UserManager userManager;

    private PlanningPokerRepository() { this.userManager = UserManager.getInstance(); }


    public static PlanningPokerRepository getInstance() {
        PlanningPokerRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(PlanningPokerRepository.class) {
            if (instance == null) {
                instance = new PlanningPokerRepository();
            }
            return instance;
        }
    }

    public void createUS(String enonce, String salon) {

        // Create the Message object
        US us = new US(enonce);

        // Store US to Firestore
        FirebaseFirestore.getInstance().collection("salons")
                .document(salon)
                .collection("ListeUS")
                .add(us);

    }
    public Query getLastUS(String salon){
        return FirebaseFirestore.getInstance().collection("salons")
                .document(salon)
                .collection("ListeUS")
                .orderBy("dateCreated", Query.Direction.DESCENDING)
                .limit(1);
    }


    public void addNote(String username, String salon, String idUS, String note){
        Map<String, Map<String, String>> data = new HashMap<>();
        Map<String,String> uneNote = new HashMap<>();
        uneNote.put(username,note);
        data.put("notes", uneNote);

        FirebaseFirestore.getInstance().collection("salons")
                .document(salon)
                .collection("ListeUS")
                .document(idUS)
                .set(data,SetOptions.merge());

        FirebaseFirestore.getInstance().collection("salons")
                .document(salon)
                .collection("ListeUS")
                .document(idUS)
                .collection("notes")
                .whereEqualTo("autor",username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size() == 0){
                            Map<String, String> data = new HashMap<>();
                            data.put("value",note);
                            data.put("autor",username);

                            FirebaseFirestore.getInstance().collection("salons")
                                    .document(salon)
                                    .collection("ListeUS")
                                    .document(idUS)
                                    .collection("notes")
                                    .add(data);
                        }
                        else{
                            String idNote= "";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                idNote = document.getId();
                            }
                            Map<String, String> data = new HashMap<>();
                            data.put("value",note);

                            FirebaseFirestore.getInstance().collection("salons")
                                    .document(salon)
                                    .collection("ListeUS")
                                    .document(idUS)
                                    .collection("notes")
                                    .document(idNote)
                                    .set(data,SetOptions.merge());
                        }

                    }
                });
    }
    public void addNoteFinal( String salon, String idUS, String note){
        Map<String,String> uneNote = new HashMap<>();
        uneNote.put("noteFinal",note);

        FirebaseFirestore.getInstance().collection("salons")
                .document(salon)
                .collection("ListeUS")
                .document(idUS)
                .set(uneNote,SetOptions.merge());


    }


    public void finishUS(String salon, String idUS, boolean finished){
        Map<String, Boolean> data = new HashMap<>();
        data.put("finished",finished);

        FirebaseFirestore.getInstance().collection("salons")
                .document(salon)
                .collection("ListeUS")
                .document(idUS)
                .set(data,SetOptions.merge());
   }


   public Query getAllNotes(String salon, String idUS){
        return FirebaseFirestore.getInstance().collection("salons")
               .document(salon)
               .collection("ListeUS")
               .document(idUS)
               .collection("notes");
   }

    public Query getAllUS(String salon){
        return FirebaseFirestore.getInstance().collection("salons")
                .document(salon)
                .collection("ListeUS").orderBy("dateCreated", Query.Direction.ASCENDING);
    }
}
