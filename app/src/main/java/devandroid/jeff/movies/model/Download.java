package devandroid.jeff.movies.model;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import devandroid.jeff.movies.database.FirebaseHelper;

public class Download {

    public static void salvar(List<String> downloadList) {
        DatabaseReference downloadRef = FirebaseHelper.getDatabaseReference()
                .child("downloads");
        downloadRef.setValue(downloadList);
    }
}
