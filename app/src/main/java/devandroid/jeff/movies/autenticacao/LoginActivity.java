package devandroid.jeff.movies.autenticacao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import devandroid.jeff.movies.R;
import devandroid.jeff.movies.activity.MainActivity;
import devandroid.jeff.movies.controller.UsuarioController;

public class LoginActivity extends AppCompatActivity {

     private UsuarioController usuarioController;
     private Button btn_cadastro;
     private Button btn_entrar;
     private EditText edit_email;
     private EditText edit_senha;
     private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioController = new UsuarioController();

        inicializaComponentes();
        configCliques();
    }

    private void inicializaComponentes() {
        btn_cadastro = findViewById(R.id.btn_cadastro);
        btn_entrar = findViewById(R.id.btn_entrar_login);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        progressBar = findViewById(R.id.progressBar);
    }

    private void configCliques() {
        btn_cadastro.setOnClickListener(v -> {
            startActivity(new Intent(this, CadastroActivity.class));

        });

        btn_entrar.setOnClickListener(v -> {
            logar();
        });
    }

    private void logar() {
        String email = edit_email.getText().toString().trim();
        String senha = edit_senha.getText().toString().trim();

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);

                boolean loginValido = usuarioController.validaLogin(email, senha, this);

                if(loginValido) {
                    finish();
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    progressBar.setProgress(View.GONE);
                }

            } else {
                edit_senha.requestFocus();
                edit_senha.setError(getString(R.string.txt_campo));
            }
        } else {
            edit_email.requestFocus();
            edit_email.setError(getString(R.string.txt_campo));
        }
    }


}