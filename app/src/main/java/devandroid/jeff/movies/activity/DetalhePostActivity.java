package devandroid.jeff.movies.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import devandroid.jeff.movies.R;
import devandroid.jeff.movies.adapter.AdapterPost;
import devandroid.jeff.movies.database.FirebaseHelper;
import devandroid.jeff.movies.model.Download;
import devandroid.jeff.movies.model.Post;

public class DetalhePostActivity extends AppCompatActivity {

    private TextView tv_titulo;
    private TextView tv_ano;
    private TextView tv_duracao;
    private TextView tv_elenco;
    private TextView tv_sinopse;
    private ImageView imgBtn_post;
    private ConstraintLayout btn_assistir;
    private ConstraintLayout btn_baixar;
    private RecyclerView rvPosts;
    private ImageButton imgBtn_voltar;
    private final List<Post> postList = new ArrayList<>();
    private final List<String> downloadList = new ArrayList<>();
    AdapterPost adapterPost;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_post);

        inicializaComponentes();

        configRecyclerView();

        configDados();

        configCliques();

        recuperaPost();

        recuperaDownload();
    }

    private void inicializaComponentes() {
        tv_titulo = findViewById(R.id.tv_titulo);
        tv_ano = findViewById(R.id.tv_ano);
        tv_duracao = findViewById(R.id.tv_duracao);
        tv_elenco = findViewById(R.id.tv_elenco);
        tv_sinopse = findViewById(R.id.tv_sinopse);
        imgBtn_post = findViewById(R.id.imgBtn_post);
        imgBtn_voltar = findViewById(R.id.imgBtn_voltar);
        btn_assistir = findViewById(R.id.btn_assistir);
        btn_baixar = findViewById(R.id.btn_baixar);
        rvPosts = findViewById(R.id.rv_posts);
    }

    private void configRecyclerView() {
        rvPosts.setLayoutManager(new GridLayoutManager(this, 3));
        rvPosts.setHasFixedSize(true);
        adapterPost = new AdapterPost(postList, this);
        rvPosts.setAdapter(adapterPost);
    }

    private void configDados() {
        post = (Post) getIntent().getSerializableExtra("postSelecionado");

        tv_titulo.setText(post.getTitulo());
        tv_ano.setText(post.getAno());
        tv_elenco.setText(post.getElenco());
        tv_sinopse.setText(post.getSinopse());
        tv_duracao.setText(getString(R.string.text_duracao, post.getDuracao()));

        Picasso.get().load(post.getImagem()).into(imgBtn_post);
    }
    private void configCliques() {
        imgBtn_voltar.setOnClickListener(view -> {
            finish();
        });

        btn_baixar.setOnClickListener(view -> {
            efetuarDownload();
        });

    }

    private void recuperaPost() {
        DatabaseReference postRef = FirebaseHelper.getDatabaseReference()
                .child("posts");

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    postList.add(dataSnapshot.getValue(Post.class));
                }

                adapterPost.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaDownload() {
        DatabaseReference downloadRef = FirebaseHelper.getDatabaseReference()
                .child("downloads");

        downloadRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    downloadList.add(dataSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void efetuarDownload() {
        if (!downloadList.contains(post.getId())) {
            downloadList.add(post.getId());
            Download.salvar(downloadList);

            Toast.makeText(this, "Download efetuado com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Download efetuado anteriormente!", Toast.LENGTH_SHORT).show();
        }
    }

}