package devandroid.jeff.movies.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import devandroid.jeff.movies.R;
import devandroid.jeff.movies.adapter.AdapterBusca;
import devandroid.jeff.movies.database.FirebaseHelper;
import devandroid.jeff.movies.model.Post;

public class BuscaFragment extends Fragment {

    private AdapterBusca adapterBusca;
    private List<Post> postList = new ArrayList<>();
    private SearchView searchView;
    private RecyclerView rv_posts;
    private ProgressBar progressBar;
    private TextView tv_info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_busca, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializaComponentes(view);
        configRecyclerView(postList);
        recuperaPost();
        configSearchView();
    }

    private void configRecyclerView(List<Post> postList) {
        rv_posts.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_posts.setHasFixedSize(true);
        adapterBusca = new AdapterBusca(postList, getContext());
        rv_posts.setAdapter(adapterBusca);
        adapterBusca.notifyDataSetChanged();

    }

    private void recuperaPost() {

        DatabaseReference postRef = FirebaseHelper.getDatabaseReference()
                .child("posts");

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    postList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Post post = dataSnapshot.getValue(Post.class);
                        postList.add(post);
                    }
                    tv_info.setText("");
                } else {
                    tv_info.setText(R.string.txt_nenhum_resultado);
                }

                progressBar.setVisibility(View.GONE);
                configRecyclerView(postList);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void configSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(query.length() >= 3) {

                    buscarPosts(query);
                } else {
                    ocultarTeclado();
                    Toast.makeText(getContext(), R.string.txt_min_caracteres, Toast.LENGTH_SHORT).show();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    private void buscarPosts(String pesquisa) {
        List<Post> postListBusca = new ArrayList<>();

        ocultarTeclado();

        for (Post post : postList) {
            if (post.getTitulo().toUpperCase(Locale.ROOT).contains(pesquisa.toUpperCase(Locale.ROOT))) {
                postListBusca.add(post);
            }
        }

        if (!postListBusca.isEmpty()) {
            configRecyclerView(postListBusca);
        } else {
            configRecyclerView(new ArrayList<>());
            tv_info.setText(R.string.txt_nenhum_post);
        }
    }

    private void ocultarTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private void inicializaComponentes(View view) {
        searchView = view.findViewById(R.id.searchView);
        rv_posts = view.findViewById(R.id.rv_posts);
        progressBar = view.findViewById(R.id.progressBar);
        tv_info = view.findViewById(R.id.tv_info);
    }
}