package com.meshwar.meshwar.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FireAuth {
    public static FirebaseAuth authInstance =FirebaseAuth.getInstance();
    //public static String getUserId = authInstance.getCurrentUser().getUid();

    public static  Task<AuthResult> createUser(String email , String password){
        return authInstance.createUserWithEmailAndPassword(email , password) ;
    }

    public static Task<AuthResult> login(String email, String password) {
        return authInstance.signInWithEmailAndPassword(email , password);
    }
}
