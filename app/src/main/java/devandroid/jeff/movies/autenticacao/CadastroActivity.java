package devandroid.jeff.movies.autenticacao;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import devandroid.jeff.movies.R;
import devandroid.jeff.movies.activity.MainActivity;
import devandroid.jeff.movies.controller.UsuarioController;
import devandroid.jeff.movies.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private UsuarioController usuarioController;
    private EditText edit_nome;
    private EditText edit_data;
    private EditText edit_email;
    private EditText edit_senha;
    private EditText edit_telefone;
    private EditText edit_endereco;
    private ProgressBar progressBar;
    private Button btn_continuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        usuarioController = new UsuarioController();

        inicializaComponentes();
        configCliques();
    }

    private void inicializaComponentes() {
        edit_nome = findViewById(R.id.edit_nome);
        edit_data = findViewById(R.id.edit_data);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        edit_telefone = findViewById(R.id.edit_telefone);
        edit_endereco = findViewById(R.id.edit_endereco);
        progressBar = findViewById(R.id.progressBar);
        btn_continuar = findViewById(R.id.btn_continuar);
    }

    private void configCliques() {
        findViewById(R.id.imgView_voltar).setOnClickListener(view -> finish());
        findViewById(R.id.btn_entrar).setOnClickListener(view -> finish());
        btn_continuar.setOnClickListener(v -> validaDados());
    }

    private void validaDados() {
        String nome = edit_nome.getText().toString().trim();
        String data = edit_data.getText().toString().trim();
        String email = edit_email.getText().toString().trim();
        String senha = edit_senha.getText().toString().trim();
        String telefone = edit_telefone.getText().toString().trim();
        String endereco = edit_endereco.getText().toString().trim();

        if(!nome.isEmpty()) {
            if(!data.isEmpty()) {
                if(!email.isEmpty()) {
                    if(!senha.isEmpty()) {
                        if(!telefone.isEmpty()) {
                            if(!endereco.isEmpty()) {

                                Usuario usuario = new Usuario();
                                usuario.setNome(nome);
                                usuario.setData(data);
                                usuario.setEmail(email);
                                usuario.setSenha(senha);
                                usuario.setTelefone(telefone);
                                usuario.setEndereco(endereco);

                                boolean cadastro = usuarioController.salvarCadastroUsuario(usuario);

                                if (cadastro) {
                                    Toast.makeText(this, R.string.txt_cadastro_sucesso, Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(this, MainActivity.class));
                                } else {
                                    Toast.makeText(this, R.string.txt_cadastro_falha, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                edit_endereco.requestFocus();
                                edit_endereco.setError(getString(R.string.txt_campo));
                            }

                        } else {
                            edit_telefone.requestFocus();
                            edit_telefone.setError(getString(R.string.txt_campo));
                        }

                    } else {
                        edit_senha.requestFocus();
                        edit_senha.setError(getString(R.string.txt_campo));
                    }

                } else {
                    edit_email.requestFocus();
                    edit_email.setError(getString(R.string.txt_campo));
                }

            } else {
                edit_data.requestFocus();
                edit_data.setError(getString(R.string.txt_campo));
            }

        } else {
            edit_nome.requestFocus();
            edit_nome.setError(getString(R.string.txt_campo));
        }
    }
    
}