package com.eseo.projetAndroid.ui.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.eseo.projetAndroid.databinding.ActivityMentorChatBinding;
import com.eseo.projetAndroid.manager.ChatManager;
import com.eseo.projetAndroid.manager.UserManager;
import com.eseo.projetAndroid.models.Message;
import com.eseo.projetAndroid.ui.BaseActivity;

public class MentorChatActivity extends BaseActivity<ActivityMentorChatBinding> implements MentorChatAdapter.Listener {

    private MentorChatAdapter mentorChatAdapter;
    private String salon;

    private UserManager userManager = UserManager.getInstance();
    private ChatManager chatManager = ChatManager.getInstance();

    @Override
    protected ActivityMentorChatBinding getViewBinding() {
        return ActivityMentorChatBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                salon = null;
            } else {
                salon = extras.getString("salon");
            }
        } else {
            salon = (String) savedInstanceState.getSerializable("salon");
        }
        configureRecyclerView(salon);
        setupListeners();
    }

    private void setupListeners(){

        // Send button
        binding.sendButton.setOnClickListener(view -> { sendMessage(); });
    }

    // Configure RecyclerView
    private void configureRecyclerView(String chatName){
        //Track current chat name
        this.salon = chatName;
        //Configure Adapter & RecyclerView
        this.mentorChatAdapter = new MentorChatAdapter(
                generateOptionsForAdapter(chatManager.getAllMessageForChat(this.salon)),
                Glide.with(this), this);

        mentorChatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                binding.chatRecyclerView.smoothScrollToPosition(mentorChatAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });

        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRecyclerView.setAdapter(this.mentorChatAdapter);
    }

    // Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();
    }

    public void onDataChanged() {
        // Show TextView in case RecyclerView is empty
        binding.emptyRecyclerView.setVisibility(this.mentorChatAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void sendMessage(){
        // Check if user can send a message (Text not null + user logged)
        boolean canSendMessage = !TextUtils.isEmpty(binding.chatEditText.getText()) && userManager.isCurrentUserLogged();

        if (canSendMessage){
            // Create a new message for the chat
            chatManager.createMessageForChat(binding.chatEditText.getText().toString(), this.salon);
            // Reset text field
            binding.chatEditText.setText("");
        }
    }
}
