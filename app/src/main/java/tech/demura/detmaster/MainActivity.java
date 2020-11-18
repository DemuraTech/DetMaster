package tech.demura.detmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

// todo советую в своих методах использовать аннотации @Nullable @NonNull для параметров
//  и выходных значений. Это позволит контролировать более менее null значения в коде

public class MainActivity extends AppCompatActivity {

    final String valueOfAnimals = "1";
    final String valueOfBirds = "2";
    final String group = "group";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnBird = findViewById(R.id.btnBird);
        btnBird.setOnClickListener(view -> {
            Intent intent = new Intent(this, DetMain.class);
            intent.putExtra(group, valueOfBirds);
            startActivity(intent);
        });

        Button btnAnimal = findViewById(R.id.btnAnimal);
        btnAnimal.setOnClickListener(view -> {
            Intent intent = new Intent(this, DetMain.class);
            intent.putExtra(group, valueOfAnimals);
            startActivity(intent);
        });

        Button btnTheory = findViewById(R.id.btnTheory);
        btnTheory.setOnClickListener((view) -> {
            Toast.makeText(this, "Lambda function", Toast.LENGTH_SHORT).show();
        });
    }
}
