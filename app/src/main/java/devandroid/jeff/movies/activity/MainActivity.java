package devandroid.jeff.movies.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import devandroid.jeff.movies.R;
import devandroid.jeff.movies.model.Categoria;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializaComponentes();

        //Codigo que controla o BottomNavigationView
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        //salvarCategorias();
    }

    private void inicializaComponentes() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void salvarCategorias() {
        new Categoria("Ação");
        new Categoria("Aventura");
        new Categoria("Animação");
        new Categoria("Comédia");
        new Categoria("Drama");
        new Categoria("Épico");
        new Categoria("Faroeste");
        new Categoria("Ficção");
        new Categoria("Guerra");
        new Categoria("Terror");

    }
}