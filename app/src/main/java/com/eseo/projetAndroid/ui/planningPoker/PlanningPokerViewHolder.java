package com.eseo.projetAndroid.ui.planningPoker;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eseo.projetAndroid.databinding.ItemNotesBinding;
import com.eseo.projetAndroid.models.Note;

public class PlanningPokerViewHolder extends RecyclerView.ViewHolder {

    private ItemNotesBinding binding;


    public PlanningPokerViewHolder(@NonNull View itemView) {
        super(itemView);

        binding = ItemNotesBinding.bind(itemView);

    }

    public void updateWithMessage(Note note){

        // Update message
        binding.messageTextView.setText(note.getValue());
        binding.textView3.setText(note.getAutor());

    }



}
