package devandroid.jeff.movies.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import devandroid.jeff.movies.R;
import devandroid.jeff.movies.adapter.AdapterCategoria;
import devandroid.jeff.movies.autenticacao.LoginActivity;
import devandroid.jeff.movies.database.FirebaseHelper;
import devandroid.jeff.movies.model.Categoria;
import devandroid.jeff.movies.model.Post;

public class InicioFragment extends Fragment {

    private ImageButton imgBtn_ver_mais;
    private SwitchCompat switch_mode;
    private boolean modoNoite;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AdapterCategoria adapterCategoria;
    private final List<Categoria> categoriaList = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();
    private RecyclerView rv_categorias;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializaComponentes(view);
        configSwitchmode();
        configCliques();
        configRecyclerView();
        recuperaCategorias();
    }

    private void inicializaComponentes(View view) {
        imgBtn_ver_mais = view.findViewById(R.id.imgBtn_ver_mais);
        rv_categorias = view.findViewById(R.id.rv_categorias);
        progressBar = view.findViewById(R.id.progressBar);
        switch_mode = view.findViewById(R.id.switch_mode);
    }


    private void configCliques() {
        imgBtn_ver_mais.setOnClickListener(view -> {

            PopupMenu popupMenu = new PopupMenu(getContext(), imgBtn_ver_mais);
            popupMenu.getMenuInflater().inflate(R.menu.menu_toolbar_inicio, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.menu_sobre) {
                    Toast.makeText(getContext(), "Sobre", Toast.LENGTH_SHORT).show();
                }else if (menuItem.getItemId() == R.id.menu_sair) {
                    FirebaseHelper.getAuth().signOut();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }

                return true;
            });

            popupMenu.show();
        });


    }

    private void configRecyclerView() {
        rv_categorias.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_categorias.setHasFixedSize(true);
        categoriaList.clear();
        postList.clear();
        adapterCategoria = new AdapterCategoria(categoriaList, getContext());
        rv_categorias.setAdapter(adapterCategoria);
    }

    private void configSwitchmode() {
        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        modoNoite = sharedPreferences.getBoolean("modoNoite", false);

        if (modoNoite) {
            switch_mode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switch_mode.setOnClickListener(v -> {
            if (modoNoite) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor = sharedPreferences.edit();
                editor.putBoolean("modoNoite", false);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor = sharedPreferences.edit();
                editor.putBoolean("modoNoite", true);
            }
            editor.apply();
        });
    }

    private void recuperaCategorias() {
        DatabaseReference categoriaRef = FirebaseHelper.getDatabaseReference()
                .child("categorias");

        categoriaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    categoriaList.add(dataSnapshot.getValue(Categoria.class));
                }

                recuperaPost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaPost() {
        DatabaseReference postRef = FirebaseHelper.getDatabaseReference()
                .child("posts");

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    postList.add(dataSnapshot.getValue(Post.class));
                }

                for (Categoria categoria : categoriaList) {
                    categoria.setPosts(postList);
                }

                progressBar.setVisibility(View.GONE);
                adapterCategoria.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}