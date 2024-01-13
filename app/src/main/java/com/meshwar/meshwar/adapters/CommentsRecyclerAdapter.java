package com.meshwar.meshwar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.meshwar.meshwar.R;
import com.meshwar.meshwar.models.Comment;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.models.User;
import com.meshwar.meshwar.util.FireStore;
import com.meshwar.meshwar.util.Global;

public class CommentsRecyclerAdapter  extends FirestoreRecyclerAdapter<Comment, CommentsRecyclerAdapter.CommentsViewHolder> {

    public CommentsRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Comment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentsViewHolder holder, int position, @NonNull Comment model) {

        // Set username
        FireStore.getUser(model.getOwnerID()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        User user = documentSnapshot.toObject(User.class);
                        holder.txtCommenter.setText(user.getFullName());
                        Glide.with(holder.itemView.getContext())
                                .load(user.getImageUrl())
                                .into(holder.imgprofile);
                    }

                }
            }
        });

        holder.txtCommentBody.setText(model.getBody());
        String date = Global.formatDateFromTimestamp(model.getCreationTime());
        holder.txtCreationDate.setText(date);
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentsRecyclerAdapter.CommentsViewHolder(view);
    }

    public class CommentsViewHolder  extends RecyclerView.ViewHolder{
        TextView txtCommentBody ;
        TextView txtCommenter ;
        TextView txtCreationDate ;

        ImageView imgprofile;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCommentBody = itemView.findViewById(R.id.textViewComment);
            txtCommenter = itemView.findViewById(R.id.textViewUsername);
            txtCreationDate = itemView.findViewById(R.id.textViewCreationDate);
            imgprofile = itemView.findViewById(R.id.imgUserComment);
        }
    }
}
