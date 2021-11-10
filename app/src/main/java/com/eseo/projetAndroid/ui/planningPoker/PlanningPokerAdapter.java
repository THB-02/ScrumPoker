package com.eseo.projetAndroid.ui.planningPoker;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.eseo.projetAndroid.R;
import com.eseo.projetAndroid.models.Note;
import com.eseo.projetAndroid.models.Salon;
import com.eseo.projetAndroid.ui.groups.GroupAdapter;
import com.eseo.projetAndroid.ui.groups.GroupsViewHolder;

public class PlanningPokerAdapter extends FirestoreRecyclerAdapter<Note, PlanningPokerViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    // VIEW TYPES

    private PlanningPokerAdapter.Listener callback;

    public PlanningPokerAdapter(@NonNull FirestoreRecyclerOptions<Note> options, PlanningPokerAdapter.Listener callback) {
        super(options);
        this.callback = callback;
    }

    @Override
    protected void onBindViewHolder(@NonNull PlanningPokerViewHolder holder, int position, @NonNull Note model) {
        holder.itemView.invalidate();
        holder.updateWithMessage(model);
    }

    @Override
    public PlanningPokerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlanningPokerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notes, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

}
