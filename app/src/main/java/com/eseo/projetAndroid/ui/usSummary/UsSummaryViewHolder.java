package com.eseo.projetAndroid.ui.usSummary;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eseo.projetAndroid.databinding.ItemNotesBinding;
import com.eseo.projetAndroid.databinding.ItemUsBinding;
import com.eseo.projetAndroid.models.US;

public class UsSummaryViewHolder extends RecyclerView.ViewHolder{

    private ItemUsBinding binding;


    public UsSummaryViewHolder(@NonNull View itemView) {
        super(itemView);

        binding = ItemUsBinding.bind(itemView);

    }

    public void updateWithMessage(US us){

        // Update message
        binding.messageTextView.setText(us.getEnonce());
        binding.Usnote.setText(us.getNoteFinal());

    }



}
