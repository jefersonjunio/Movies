package devandroid.jeff.movies.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.List;

import devandroid.jeff.movies.database.FirebaseHelper;

public class Categoria {

    private String id;
    private String nome;
    private List<Post> posts;

    public Categoria() {

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

    @Exclude
    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
