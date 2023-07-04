package devandroid.jeff.movies.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import devandroid.jeff.movies.R;
import devandroid.jeff.movies.adapter.AdapterBreve;
import devandroid.jeff.movies.database.FirebaseHelper;
import devandroid.jeff.movies.model.Post;

public class BreveFragment extends Fragment {

    private List<Post> postList = new ArrayList<>();
    private AdapterBreve adapterBreve;
    private RecyclerView rv_posts;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_breve, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializaComponentes(view);
        configRecyclerView();
        recuperaPost();
    }

    private void inicializaComponentes(View view) {
        rv_posts = view.findViewById(R.id.rv_posts);
        progressBar = view.findViewById(R.id.progressBar);
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

                progressBar.setVisibility(View.GONE);
                adapterBreve.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRecyclerView() {
        rv_posts.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_posts.setHasFixedSize(true);
        adapterBreve = new AdapterBreve(postList);
        rv_posts.setAdapter(adapterBreve);
    }
}