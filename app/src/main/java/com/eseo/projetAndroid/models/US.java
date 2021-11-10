package com.eseo.projetAndroid.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;

public class US {

    private String id;
    private String enonce;
    private Date dateCreated;
    private HashMap<String, String> notes = new HashMap<String, String>();
    private boolean finished;
    private String noteFinal;

    public US(){
        this.finished = false;
    }

    public US(String enonce){
        this.enonce = enonce;
    }
    public US(String enonce, String noteFinal){
        this.enonce= enonce;
        this.noteFinal = noteFinal;}


    public String getId() {
        return id;
    }
    @ServerTimestamp
    public Date getDateCreated() { return dateCreated; }
    public void setId(String id) {
        this.id = id;
    }
    public String getEnonce() {
        return enonce;
    }
    public boolean isFinished() { return finished; }

    public String getNoteFinal() { return noteFinal; }
    public void setNoteFinal(String noteFinal) { this.noteFinal = noteFinal; }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
    public HashMap<String, String> getNotes() {
        return notes;
    }
    public void setFinished(boolean finished) { this.finished = finished; }
    public void setNotes(HashMap<String, String> notes) {
        this.notes = notes;
    }
}
