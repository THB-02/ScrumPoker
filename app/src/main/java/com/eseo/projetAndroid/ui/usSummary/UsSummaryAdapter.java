package com.eseo.projetAndroid.ui.usSummary;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.eseo.projetAndroid.R;
import com.eseo.projetAndroid.models.Note;
import com.eseo.projetAndroid.models.US;


public class UsSummaryAdapter extends FirestoreRecyclerAdapter<US, UsSummaryViewHolder>  {

    public interface Listener {
        void onDataChanged();
    }
    // VIEW TYPES

    private UsSummaryAdapter.Listener callback;

    public UsSummaryAdapter(@NonNull FirestoreRecyclerOptions<US> options, UsSummaryAdapter.Listener callback) {
        super(options);
        this.callback = callback;
    }

    @Override
    protected void onBindViewHolder(@NonNull UsSummaryViewHolder holder, int position, @NonNull US model) {
        holder.itemView.invalidate();
        holder.updateWithMessage(model);
    }

    @Override
    public UsSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UsSummaryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_us, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }



}
