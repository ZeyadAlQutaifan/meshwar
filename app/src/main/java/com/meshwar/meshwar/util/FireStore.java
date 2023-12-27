package com.meshwar.meshwar.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.meshwar.meshwar.models.Comment;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.models.User;

import java.util.List;

public class FireStore {
    public static FirebaseFirestore getFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    public static CollectionReference usersRef(){
        return getFirestoreInstance().collection("users");
    }
    public static CollectionReference placesRef(){
        return getFirestoreInstance().collection("places");
    }
    public static CollectionReference commentRef(){
        return getFirestoreInstance().collection("comments");
    }
    public static Query getPlaceComments(String placeId){
        return commentRef().whereEqualTo("placeId", placeId).orderBy("creationTime");
    }

    public static Task<DocumentReference> addComment(Comment comment){
        return commentRef().add(comment);
    }
    public static Task<DocumentSnapshot> getPLace(String placeId){
        return placesRef().document(placeId).get();
    }

    public static Task<DocumentReference> addPlace(List<String> imageUrls) {
        Place.getInstance().setImages(imageUrls);
        // Save the post to Firestore
        return placesRef().add(Place.getInstance());
    }
    public static Task<Void> writeUser(String userId , User newUser){
        return usersRef().document(userId).set(newUser);
    }

    public static Task<DocumentSnapshot> getUser(String ownerID) {
        return usersRef().document(ownerID).get();
    }
}
