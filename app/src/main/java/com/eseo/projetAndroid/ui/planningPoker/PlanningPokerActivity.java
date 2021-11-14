package com.eseo.projetAndroid.ui.planningPoker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eseo.projetAndroid.ui.chat.MentorChatActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.eseo.projetAndroid.R;
import com.eseo.projetAndroid.databinding.ActivityPlanningPokerBinding;
import com.eseo.projetAndroid.manager.GroupManager;
import com.eseo.projetAndroid.manager.PlanningPokerManager;
import com.eseo.projetAndroid.manager.UserManager;
import com.eseo.projetAndroid.models.Note;
import com.eseo.projetAndroid.models.Salon;
import com.eseo.projetAndroid.models.US;
import com.eseo.projetAndroid.models.User;
import com.eseo.projetAndroid.ui.BaseActivity;
import com.eseo.projetAndroid.ui.addGroup.AddGroupActivity;
import com.eseo.projetAndroid.ui.groups.GroupAdapter;
import com.eseo.projetAndroid.ui.usSummary.UsSummaryActivity;

import java.util.HashMap;
import java.util.Map;


public class PlanningPokerActivity extends BaseActivity<ActivityPlanningPokerBinding> implements PlanningPokerAdapter.Listener{

    private String salon;
    private String scrum;
    private PlanningPokerAdapter planningPokerAdapter;
    private String currentChatName;
    private String noteFinal;
    private UserManager userManager = UserManager.getInstance();
    private PlanningPokerManager planningPokerManager = PlanningPokerManager.getInstance();
    private GroupManager groupManager = GroupManager.getInstance();

    @Override
    protected ActivityPlanningPokerBinding getViewBinding() {
        return ActivityPlanningPokerBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                salon= null;
                scrum= null;
            } else {
                salon= extras.getString("salon");
                scrum= extras.getString("scrum");
            }
        } else {
            salon= (String) savedInstanceState.getSerializable("salon");
            scrum= (String) savedInstanceState.getSerializable("scrum");
        }
        setupListeners();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_us,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        if(item.getItemId()==R.id.copy_menu){
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("idRoom",salon);
                    clipboardManager.setPrimaryClip(clip);
                    Toast.makeText(PlanningPokerActivity.this, "Clé du salon copié !", Toast.LENGTH_SHORT).show();


            return true;
        }
        if(item.getItemId()==R.id.list_us){
            UsSummaryActivity();

            return true;
        }
        if(item.getItemId()==R.id.chat){
            startMentorChatActivity();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListeners(){

        String userId = userManager.getCurrentUser().getUid();
        String username = userManager.getCurrentUser().getDisplayName();
        planningPokerManager.getLastUS(salon).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    if (value.size() > 0) {
                        for (QueryDocumentSnapshot document : value) {
                            US us = document.toObject(US.class);
                            String idUS = document.getId();
                            configureRecyclerView(salon, idUS);
                            binding.textUs.setText(us.getEnonce());
                            if(us.getNotes().containsKey(username)){

                                int count = 0;
                                String userNote = us.getNotes().get(username);
                                for (Map.Entry<String,String> note : us.getNotes().entrySet()){
                                    if(note.getValue().equals(userNote)){
                                        count++;
                                    }
                                }
                                Log.e("notes identiques",String.valueOf(count));
                                Log.e("notes identiques",String.valueOf(us.getNotes().size()==count));
                                if (scrum.equals(userId)) {

                                    if(us.getNotes().size()==count){
                                        binding.btnUsSuivante.setVisibility(View.VISIBLE);

                                    }
                                    else{
                                        binding.btnUsSuivante.setVisibility(View.GONE);
                                    }
                                }

                            }

                            if (us.getNotes().containsKey(username)) {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                            } else {
                                binding.btnModifReponse.setVisibility(View.GONE);
                                binding.btnNotes.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.GONE);
                                binding.btnUsSuivante.setVisibility(View.GONE);
                            }

                            binding.btnModifReponse.setOnClickListener(view -> {
                                binding.btnModifReponse.setVisibility(View.GONE);
                                binding.btnNotes.setVisibility(View.VISIBLE);
                                binding.btnUsSuivante.setVisibility(View.GONE);
                                binding.resultNotes.setVisibility(View.GONE);
                            });

                            binding.button0.setOnClickListener(view -> {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                                noteFinal= "0";
                                planningPokerManager.addNote(username, salon, idUS, "0");
                            });
                            binding.button05.setOnClickListener(view -> {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                                noteFinal= "0.5";
                                planningPokerManager.addNote(username, salon, idUS, "0.5");
                            });
                            binding.button1.setOnClickListener(view -> {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                                noteFinal= "1";
                                planningPokerManager.addNote(username, salon, idUS, "1");

                            });
                            binding.button2.setOnClickListener(view -> {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                                noteFinal= "2";
                                planningPokerManager.addNote(username, salon, idUS, "2");

                            });
                            binding.button3.setOnClickListener(view -> {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                                noteFinal= "3";
                                planningPokerManager.addNote(username, salon, idUS, "3");

                            });
                            binding.button5.setOnClickListener(view -> {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                                noteFinal= "5";
                                planningPokerManager.addNote(username, salon, idUS, "5");

                            });
                            binding.button8.setOnClickListener(view -> {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                                noteFinal= "8";
                                planningPokerManager.addNote(username, salon, idUS, "8");

                            });
                            binding.button13.setOnClickListener(view -> {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                                noteFinal= "13";
                                planningPokerManager.addNote(username, salon, idUS, "13");

                            });
                            binding.button20.setOnClickListener(view -> {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                                noteFinal= "20";
                                planningPokerManager.addNote(username, salon, idUS, "20");

                            });
                            binding.button40.setOnClickListener(view -> {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                                noteFinal= "40";
                                planningPokerManager.addNote(username, salon, idUS, "40");
                            });
                            binding.button100.setOnClickListener(view -> {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                                noteFinal= "100";
                                planningPokerManager.addNote(username, salon, idUS, "100");

                            });
                            binding.buttonInterrogation.setOnClickListener(view -> {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.VISIBLE);
                                binding.resultNotes.setVisibility(View.VISIBLE);
                                noteFinal= "?";
                                planningPokerManager.addNote(username, salon, idUS, "?");
                            });

                            binding.btnUsSuivante.setOnClickListener(view -> {
                                planningPokerManager.finishUS(salon, idUS, true);
                                binding.messageContainer.setVisibility(View.VISIBLE);
                                planningPokerManager.addNoteFinal(salon, idUS, noteFinal);
                            });

                            if (us.isFinished()) {
                                binding.btnNotes.setVisibility(View.GONE);
                                binding.btnModifReponse.setVisibility(View.GONE);
                                binding.btnUsSuivante.setVisibility(View.GONE);
                                binding.resultNotes.setVisibility(View.GONE);
                                binding.textUs.setText("Le scrum master a fermé les votes.\nEn attente d'une nouvelle US..");
                            } else {
                                binding.messageContainer.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        binding.btnNotes.setVisibility(View.GONE);
                        binding.btnModifReponse.setVisibility(View.GONE);
                        binding.btnUsSuivante.setVisibility(View.GONE);
                    }
                }
                else{
                binding.btnNotes.setVisibility(View.GONE);
                binding.btnModifReponse.setVisibility(View.GONE);
                binding.btnUsSuivante.setVisibility(View.GONE);
            }
            }
        });

        if(scrum.equals(userId)){
            binding.messageContainer.setVisibility(View.VISIBLE);
        }
        else{
            binding.btnUsSuivante.setVisibility(View.GONE);
            binding.messageContainer.setVisibility(View.GONE);
        }

        if(binding.textUs.getText().equals(R.string.nom_us)){
            Log.e("string",String.valueOf(binding.textUs.getText().equals("En attente d'US du scrum master..")));
            binding.btnModifReponse.setVisibility(View.GONE);
            binding.btnNotes.setVisibility(View.GONE);
            binding.btnVoirNotes.setVisibility(View.GONE);
            binding.btnUsSuivante.setVisibility(View.GONE);
            binding.resultNotes.setVisibility(View.GONE);
        }

        // Send button
        binding.sendButton.setOnClickListener(view -> {
            sendUS();
        });


    }

    // Configure RecyclerView
    private void configureRecyclerView(String chatName, String idUS) {
        //Track current chat name
        this.currentChatName = chatName;
        //Configure Adapter & RecyclerView
        this.planningPokerAdapter = new PlanningPokerAdapter(
                generateOptionsForAdapter(planningPokerManager.getAllNotes(salon,idUS)),
                this);

        binding.resultNotes.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
        binding.resultNotes.setAdapter(this.planningPokerAdapter);
    }

    // Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Note> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .setLifecycleOwner(this)
                .build();
    }

    private void sendUS(){
        // Check if user can send a message (Text not null + user logged)
        boolean canSendMessage = !TextUtils.isEmpty(binding.chatEditText.getText()) && userManager.isCurrentUserLogged();

        if (canSendMessage){
            // Create a new message for the chat
            planningPokerManager.createUS(binding.chatEditText.getText().toString(), this.salon);
            // Reset text field
            binding.chatEditText.setText("");
        }
    }


    @Override
    public void onDataChanged() {

    }
    private void UsSummaryActivity() {
        Intent intent = new Intent(this, UsSummaryActivity.class);
        intent.putExtra("salon",salon);
        startActivity(intent);
    }

    private void startMentorChatActivity(){
        Intent intent = new Intent(this, MentorChatActivity.class);
        intent.putExtra("salon",salon);
        startActivity(intent);

    }
}
