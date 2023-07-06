package devandroid.jeff.movies.model;

import com.google.firebase.database.DatabaseReference;

import devandroid.jeff.movies.database.FirebaseHelper;

public class Usuario {

    private String id;
    private String nome;
    private String data;
    private String email;
    private String senha;
    private String telefone;
    private String endereco;

    public Usuario() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference();
        this.setId(usuarioRef.push().getKey());
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
