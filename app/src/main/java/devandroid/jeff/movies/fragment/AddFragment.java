package devandroid.jeff.movies.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

import devandroid.jeff.movies.R;
import devandroid.jeff.movies.controller.PostController;
import devandroid.jeff.movies.database.FirebaseHelper;
import devandroid.jeff.movies.model.Post;

public class AddFragment extends Fragment {

    private final int SELECAO_GALERIA = 100;

    private String caminhoImagem = null;
    private ImageView imageView;
    private ImageView imageViewFake;
    private EditText et_titulo;
    private EditText et_genero;
    private EditText et_elenco;
    private EditText et_ano;
    private EditText et_duracao;
    private EditText et_sinopse;
    private Button btn_cadastrar;
    private ProgressBar progressBar;
    private PostController postController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postController = new PostController();
        inicializaComponentes(view);
        configClicks();
    }

    private void inicializaComponentes(View view) {
        imageView = view.findViewById(R.id.imageView);
        imageViewFake = view.findViewById(R.id.imageViewFake);
        et_titulo = view.findViewById(R.id.et_titulo);
        et_genero = view.findViewById(R.id.et_genero);
        et_elenco = view.findViewById(R.id.et_elenco);
        et_ano = view.findViewById(R.id.et_ano);
        et_duracao = view.findViewById(R.id.et_duracao);
        et_sinopse = view.findViewById(R.id.et_sinopse);
        btn_cadastrar = view.findViewById(R.id.btn_cadastrar);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void configClicks() {
        imageView.setOnClickListener(view -> {
            verificaPermissaoGaleria();
        });

        btn_cadastrar.setOnClickListener(view -> {
            validaDados();
        });
    }
    private void validaDados() {
        String titulo = et_titulo.getText().toString().trim();
        String genero = et_genero.getText().toString().trim();
        String elenco = et_elenco.getText().toString().trim();
        String ano = et_ano.getText().toString().trim();
        String duracao = et_duracao.getText().toString().trim();
        String sinopse = et_sinopse.getText().toString().trim();

        if(!titulo.isEmpty()) {
            if(!genero.isEmpty()) {
                if(!elenco.isEmpty()) {
                    if(!ano.isEmpty()) {
                        if(!duracao.isEmpty()) {
                            if(!sinopse.isEmpty()) {
                                if(caminhoImagem != null) {
                                    progressBar.setVisibility(View.VISIBLE);

                                    Post post = new Post();
                                    post.setTitulo(titulo);
                                    post.setGenero(genero);
                                    post.setElenco(elenco);
                                    post.setAno(ano);
                                    post.setDuracao(duracao);
                                    post.setSinopse(sinopse);

                                    boolean deuCerto = postController.salvarImagemStorage(post, caminhoImagem);

                                    if(deuCerto) {
                                        Toast.makeText(getActivity(), R.string.txt_cadastro_sucesso, Toast.LENGTH_SHORT).show();
                                        limparCampos();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), R.string.txt_cadastrar_erro, Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(getActivity(), R.string.txt_selecione_imagem, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                et_sinopse.requestFocus();
                                et_sinopse.setError(getString(R.string.txt_info_obrigatoria));
                            }
                        } else {
                            et_duracao.requestFocus();
                            et_duracao.setError(getString(R.string.txt_info_obrigatoria));
                        }
                    } else {
                        et_ano.requestFocus();
                        et_ano.setError(getString(R.string.txt_info_obrigatoria));
                    }
                } else {
                    et_elenco.requestFocus();
                    et_elenco.setError(getString(R.string.txt_info_obrigatoria));
                }
            } else {
                et_genero.requestFocus();
                et_genero.setError(getString(R.string.txt_info_obrigatoria));
            }
        } else {
            et_titulo.requestFocus();
            et_titulo.setError(getString(R.string.txt_info_obrigatoria));
        }
    }

    private void limparCampos() {
        imageView.setImageBitmap(null);
        imageViewFake.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        et_titulo.getText().clear();
        et_genero.getText().clear();
        et_elenco.getText().clear();
        et_ano.getText().clear();
        et_duracao.getText().clear();
        et_sinopse.getText().clear();
    }

    private void verificaPermissaoGaleria() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissões")
                .setDeniedMessage("Se você não aceitar a permissão, não poderá acessar a Galeria do dispositivo, deseja ativar a permissão?")
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECAO_GALERIA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == SELECAO_GALERIA) {
                Uri imagemSelecionada = data.getData();
                caminhoImagem = imagemSelecionada.toString();

                try {
                    Bitmap bitmap;

                    if(Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagemSelecionada);
                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imagemSelecionada);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }

                    imageViewFake.setVisibility(View.GONE);
                    imageView.setImageBitmap(bitmap);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
}