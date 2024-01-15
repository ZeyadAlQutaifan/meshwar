package com.meshwar.meshwar.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.meshwar.meshwar.models.Comment;
import com.meshwar.meshwar.models.Favorite;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.models.User;

import java.util.List;

public class FireStore {
    public static FirebaseFirestore getFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    public static CollectionReference usersRef() {
        return getFirestoreInstance().collection("users");
    }

    public static CollectionReference placesRef() {
        return getFirestoreInstance().collection("places");
    }

    public static CollectionReference commentRef() {
        return getFirestoreInstance().collection("comments");
    }

    public static CollectionReference favRef() {
        return getFirestoreInstance().collection("favorites");
    }

    public static Task<Void> updateUser(String userId , User user) {
        return usersRef().document(userId).set(user);
    }
    public static Task<DocumentReference> addPlaceToFav(String placeId) {
        Favorite favorite = new Favorite();
        favorite.setCreationDate(System.currentTimeMillis());
        favorite.setPlaceId(placeId);
        favorite.setUserId(FireAuth.authInstance.getUid());
        return favRef().add(favorite);
    }

    public static Task<QuerySnapshot> getIfFavorite(String placeId) {

        return favRef().whereEqualTo("placeId", placeId)
                .whereEqualTo("userId", FireAuth.authInstance.getUid())
                .get() ;
    }

    public static Task<Void> removePlaceFromFav(String placeId) {
        // Get the reference to the document to be deleted
        return favRef()
                .whereEqualTo("placeId", placeId)
                .whereEqualTo("userId", FireAuth.authInstance.getUid())
                .get()
                .continueWithTask(querySnapshotTask -> {
                    // Check if there is any matching document
                    if (!querySnapshotTask.getResult().isEmpty()) {
                        // Delete the document
                        DocumentReference documentReference = querySnapshotTask.getResult().getDocuments().get(0).getReference();
                        return documentReference.delete();
                    } else {
                        // No matching document found
                        return null;
                    }
                });
    }
    public static Task<Void> removePlace(String placeId) {
        // Get the reference to the document to be deleted
       return placesRef().document(placeId).delete();


    }


    public static Query getPlaceComments(String placeId) {
        return commentRef().whereEqualTo("placeId", placeId).orderBy("creationTime");
    }

    public static Task<DocumentReference> addComment(Comment comment) {
        return commentRef().add(comment);
    }

    public static Task<DocumentSnapshot> getPLace(String placeId) {
        return placesRef().document(placeId).get();
    }

    public static Task<DocumentReference> addPlace(List<String> imageUrls) {
        Place.getInstance().setImages(imageUrls);
        // Save the post to Firestore
        return placesRef().add(Place.getInstance());
    }

    public static Task<Void> writeUser(String userId) {
        return usersRef().document(userId).set(User.getInstance());
    }

    public static Task<DocumentSnapshot> getUser(String ownerID) {
        return usersRef().document(ownerID).get();
    }
}
