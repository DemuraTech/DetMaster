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
        tvResult = findViewById(R.id.tvResult);
        ivResult = findViewById(R.id.ivResult);

        dataFromBase = new DataFromBase(this);
        Intent intent = getIntent();
        dataFromBase.setGroup1(Integer.parseInt(intent.getStringExtra("group")));

        btnYes = findViewById(R.id.btnYes);
        btnYes.setOnClickListener(view -> {
            onClickBtnYes();
        });

        btnNo = findViewById(R.id.btnNo);
        btnNo.setOnClickListener(view -> {
            onClickBtnNo();
        });

        btnResult = findViewById(R.id.btnResult);
        btnResult.setOnClickListener(view -> {
            super.onBackPressed();
        });

        tvPrev.setText(dataFromBase.previewText());
        registerForContextMenu(tvProgress);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // todo D in SOLID principles
        // опираемся на интерфейсы и абстракции вместо реализаций
        // List - абстракция, ArrayList - реализация
        // Если будет List то можно легко ArrayList заменить на LinkedList
        ArrayList<String> contextMenuData = dataFromBase.getStringList();

        for (int i = 0; i < contextMenuData.size(); i++) {
            // todo если все таки нужен индекс то лучше обычный for
            menu.add(0, i + 1, 0, contextMenuData.get(i));
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
