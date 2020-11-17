package tech.demura.detmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//todo нейминг
public class DetMain extends AppCompatActivity {

    //todo поля лучше группировать по типу: вьюхи, бд, еще что-то
    Button btnYes, btnNo, btnResult;
    TextView tvPrev, tvProgress, tvResult;
    ImageView ivResult;

    DataFromBase dataFromBase;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_main);

        tvPrev = findViewById(R.id.tvPrev);
        tvProgress = findViewById(R.id.tvProgress);
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        ivResult = findViewById(R.id.ivResult);
        tvResult = findViewById(R.id.tvResult);
        btnResult = findViewById(R.id.btnResult);
        dataFromBase = new DataFromBase(this);
        Intent intent = getIntent();

        dataFromBase.setGroup1(Integer.parseInt(intent.getStringExtra("group")));

        btnYes.setOnClickListener(view -> {
            onClickBtnYes();
        });
        btnNo.setOnClickListener(view -> {
            onClickBtnNo();
        });
        btnResult.setOnClickListener(view -> {
            super.onBackPressed();
        });

        // todo превышение 100 строк, у меня на экране строка не помещается

        tvPrev.setText(dataFromBase.previewText());
        registerForContextMenu(tvProgress);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ArrayList<String> list = dataFromBase.getStringList();
        for (String i : list) {
            menu.add(0, list.indexOf(i)+1, 0, i);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        dataFromBase.Assignment(item.getItemId());
        onClickBtnYes();
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void onClickBtnNo() {
        dataFromBase.clickedNo();
        tvPrev.setText(dataFromBase.previewText());
    }

    public void onClickBtnYes() {

        dataFromBase.clickedYes();
        tvProgress.setText(dataFromBase.progressText());
        if (dataFromBase.getGroupId() > 0) {
            tvPrev.setText(dataFromBase.previewText());
        } else {

            tvResult.setText(dataFromBase.progressText());
            ivResult.setImageResource(dataFromBase.getImageId());

            tvProgress.setVisibility(View.INVISIBLE);
            tvPrev.setVisibility(View.INVISIBLE);
            btnNo.setVisibility(View.INVISIBLE);
            btnYes.setVisibility(View.INVISIBLE);
            ivResult.setVisibility(View.VISIBLE);
            tvResult.setVisibility(View.VISIBLE);
            btnResult.setVisibility(View.VISIBLE);
        }
    }
}
