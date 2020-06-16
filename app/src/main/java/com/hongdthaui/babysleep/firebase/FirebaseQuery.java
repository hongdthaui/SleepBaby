package com.hongdthaui.babysleep.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by hongdthaui on 6/14/2020.
 */
public class FirebaseQuery {
    public static String NORTH_SONG = "NorthSong";
    public static String CENTRAL_SONG = "CentralSong";
    public static String SOUTH_SONG = "SouthSong";
    public static String WORDLESS_SONG = "WordlessSong";

    public static void getNorthSongList(ValueEventListener valueEventListener){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(NORTH_SONG);
        databaseReference.addValueEventListener(valueEventListener);
    }

    public static void getSouthSongList(ValueEventListener valueEventListener){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(SOUTH_SONG);
        databaseReference.addValueEventListener(valueEventListener);
    }

    public static void getWordlessSongList(ValueEventListener valueEventListener){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(WORDLESS_SONG);
        databaseReference.addValueEventListener(valueEventListener);
    }

    public static void getCentralSongList(ValueEventListener valueEventListener) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(CENTRAL_SONG);
        databaseReference.addValueEventListener(valueEventListener);
    }
}
