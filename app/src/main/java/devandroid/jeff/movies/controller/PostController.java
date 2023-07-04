package devandroid.jeff.movies.controller;

import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.atomic.AtomicBoolean;

import devandroid.jeff.movies.database.FirebaseHelper;
import devandroid.jeff.movies.fragment.AddFragment;
import devandroid.jeff.movies.model.Post;

public class PostController {


    public boolean salvarImagemStorage(Post post, String caminhoImagem) {
        final AtomicBoolean salvoComSucesso = new AtomicBoolean(true);

        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("posts")
                .child(post.getId() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            post.setImagem(task.getResult().toString());
            salvar(post);

            salvoComSucesso.set(false);

        })).addOnFailureListener(e -> {

            salvoComSucesso.set(true);
        });
        return salvoComSucesso.get();
    }

    public void salvar(Post post) {
        DatabaseReference postRef = FirebaseHelper.getDatabaseReference()
                .child("posts")
                .child(post.getId());
        postRef.setValue(post);
    }
}
