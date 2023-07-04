package devandroid.jeff.movies.controller;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import devandroid.jeff.movies.activity.MainActivity;
import devandroid.jeff.movies.database.FirebaseHelper;
import devandroid.jeff.movies.model.Usuario;

public class UsuarioController {


    public boolean validaLogin(String email, String senha, Context context){

        final AtomicBoolean loginValido= new AtomicBoolean(true);

        FirebaseHelper.getAuth().signInWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                loginValido.set(false);
            }else {
                String error = Objects.requireNonNull(task.getException()).getMessage();
                Toast.makeText(context.getApplicationContext(), FirebaseHelper.validaErros(error), Toast.LENGTH_SHORT).show();
            }
        });

        return loginValido.get();
    }
    public boolean salvarCadastroUsuario(Usuario usuario){

        final AtomicBoolean salvouComSucesso = new AtomicBoolean(true);

        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String id = task.getResult().getUser().getUid();
                usuario.setId(id);
                salvouComSucesso.set(false);

            }
        });

        return salvouComSucesso.get();
    }
}
