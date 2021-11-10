package com.eseo.projetAndroid.ui.groups;

import android.content.Intent;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.eseo.projetAndroid.R;
import com.eseo.projetAndroid.databinding.ItemGroupsBinding;
import com.eseo.projetAndroid.models.Salon;
import com.eseo.projetAndroid.ui.planningPoker.PlanningPokerActivity;


public class GroupsViewHolder extends RecyclerView.ViewHolder {

    private ItemGroupsBinding binding;

    private final int color;

    public GroupsViewHolder(@NonNull View itemView) {
        super(itemView);

        binding = ItemGroupsBinding.bind(itemView);

        // Setup default colors
        color = ContextCompat.getColor(itemView.getContext(), R.color.colorAccent);
    }

    public void updateWithMessage(Salon salon){

        // Update message
        binding.messageTextView.setText(salon.getNom());

    }

    public void openChat(Salon salon){
        itemView.setOnClickListener(view -> {
            Intent intent = new Intent(itemView.getContext(), PlanningPokerActivity.class);
            intent.putExtra("salon", salon.getUid());
            intent.putExtra("scrum", salon.getScrum());
            itemView.getContext().startActivity(intent);
        });
    }



}
