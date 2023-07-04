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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import devandroid.jeff.movies.R;
import devandroid.jeff.movies.adapter.AdapterDownload;
import devandroid.jeff.movies.database.FirebaseHelper;
import devandroid.jeff.movies.model.Download;
import devandroid.jeff.movies.model.Post;

public class DownloadFragment extends Fragment implements AdapterDownload.OnClickListener {

    private AdapterDownload adapterDownload;
    private final List<String> downloadList = new ArrayList<>();
    private final List<Post> postList = new ArrayList<>();
    private RecyclerView rv_posts;
    private ProgressBar progressBar;
    private TextView tv_info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializaComponentes(view);
        recuperaDownload();
        configRecyclerView();
    }

    private void inicializaComponentes(View view) {
        rv_posts = view.findViewById(R.id.rv_posts);
        progressBar = view.findViewById(R.id.progressBar);
        tv_info = view.findViewById(R.id.tv_info);
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

                recuperaPost(downloadList);

                adapterDownload.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRecyclerView() {
        rv_posts.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_posts.setHasFixedSize(true);
        adapterDownload = new AdapterDownload(postList, this);
        rv_posts.setAdapter(adapterDownload);
    }

    private void recuperaPost(List<String> downloadList) {
        DatabaseReference postRef = FirebaseHelper.getDatabaseReference()
                .child("posts");

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);

                    if(downloadList.contains(post.getId())){
                        postList.add(post);
                    }
                }

                listIsEmpty();

                progressBar.setVisibility(View.GONE);
                Collections.reverse(postList);
                adapterDownload.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClickListener(Post post) {
        postList.remove(post);
        adapterDownload.notifyDataSetChanged();
        downloadList.remove(post.getId());
        Download.salvar(downloadList);
        listIsEmpty();
    }

    private void listIsEmpty() {
        if(!downloadList.isEmpty()) {
            tv_info.setText("");
        } else {
            tv_info.setText(R.string.txt_nenhum_download);
        }
    }
}