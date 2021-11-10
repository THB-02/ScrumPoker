package com.eseo.projetAndroid.manager;

import com.google.firebase.firestore.Query;
import com.eseo.projetAndroid.repository.GroupRepository;

public class GroupManager {

    private static volatile GroupManager instance;
    private GroupRepository groupRepository;

    private GroupManager() {
        groupRepository = GroupRepository.getInstance();
    }

    public static GroupManager getInstance() {
        GroupManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(GroupManager.class) {
            if (instance == null) {
                instance = new GroupManager();
            }
            return instance;
        }
    }

    public Query getAllRooms(){
        return groupRepository.getAllRooms();
    }


    public void createGroup(String nom){
        groupRepository.createGroup(nom);
    }
    public Query getLastSalon(){
        return groupRepository.getLastSalon();

    }
    public void addScrum (String uidSalon, String uidUSer){
        groupRepository.addScrum(uidSalon,  uidUSer);
    }

}
