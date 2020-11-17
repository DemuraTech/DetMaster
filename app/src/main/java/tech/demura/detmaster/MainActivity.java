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

    // todo нужны ли здесь поля?
    Button btnBird, btnAnimal, btnTree;
    // todo почему статик?
    final String
            valueOfAnimals = "1",
            valueOfBirds = "2",
            group = "group";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBird = findViewById(R.id.btnBird);
        btnAnimal = findViewById(R.id.btnAnimal);
        btnTree = findViewById(R.id.btnTheory);

        btnBird.setOnClickListener(view -> {
            Intent intent = new Intent(this, DetMain.class);
            intent.putExtra(group, valueOfBirds);
            startActivity(intent);
        });

        btnAnimal.setOnClickListener(view -> {
            Intent intent = new Intent(this, DetMain.class);
            intent.putExtra(group, valueOfAnimals);
            startActivity(intent);
        });

        btnTree.setOnClickListener((view) -> {
            Toast.makeText(this, "Lambda function", Toast.LENGTH_SHORT).show();
        });



    }
}