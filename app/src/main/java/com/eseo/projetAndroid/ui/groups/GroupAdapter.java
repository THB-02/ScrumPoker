package com.eseo.projetAndroid.ui.groups;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.eseo.projetAndroid.R;
import com.eseo.projetAndroid.models.Salon;


public class GroupAdapter extends FirestoreRecyclerAdapter<Salon, GroupsViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    // VIEW TYPES

    private GroupAdapter.Listener callback;

    public GroupAdapter(@NonNull FirestoreRecyclerOptions<Salon> options, GroupAdapter.Listener callback) {
        super(options);
        this.callback = callback;
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupsViewHolder holder, int position, @NonNull Salon model) {
        holder.itemView.invalidate();
        holder.updateWithMessage(model);
        holder.openChat(model);
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_groups, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

}
