package com.example.graficonas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final int VERTICAL_LEVELS_NUM = 4;

    @BindView(R.id.vertical_levels_container)
    protected LinearLayout verticalLevelsContainer;
    @BindView(R.id.bars_container)
    protected LinearLayout barsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        createVerticalLevels();
        createBars();
    }

    private void createBars() {
            View view =
                    LayoutInflater.from(this).inflate(R.layout.bar, barsContainer, false);
            barsContainer.addView(view);
    }

    private void createVerticalLevels() {
        for (int i = VERTICAL_LEVELS_NUM; i >= 0; i--) {
           View view =
                    LayoutInflater.from(this).inflate(R.layout.horizontal_vertical_level, verticalLevelsContainer, false);
            ((TextView)view.findViewById(R.id.level_value)).setText(String.valueOf(i * 25));
            verticalLevelsContainer.addView(view);
        }
    }
}