package com.eseo.projetAndroid.models;

public class Note {
    private String value;
    private String autor;

    public Note(){

    }

    public Note(String value, String autor){
        this.value = value;
        this.autor = autor;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
