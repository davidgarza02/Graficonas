package com.example.graficonas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class CreditScoreLevelsGraphView extends View {
    public static final int LEVEL = 0;

    private static final float PADDING_LEFT_DP = 9;
    private static final float PADDING_RIGHT_DP = 9;
    private static final float PADDING_TOP_DP = 2;
    private static final float PADDING_BOTTOM_DP = 2;
    private static final float SPACE_BETWEEN_LEVELS = 10;
    private static final float OFFSET_HORIZONTAL = 10;

    private Paint emptyLevelPaint;
    private Paint coloredLevelPaint;

    private float gridLeft;
    private float gridBottom;
    private float gridTop;
    private float gridWidth;
    private float deviceDensity;

    Path[] levelsPath = new Path[4];

    public CreditScoreLevelsGraphView(Context context) {
        super(context);
    }

    public CreditScoreLevelsGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CreditScoreLevelsGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int viewHeight = getHeight();
        int viewWidth = getWidth();
        deviceDensity = getResources().getDisplayMetrics().density;

        gridLeft = PADDING_LEFT_DP * deviceDensity;
        gridBottom = viewHeight - (PADDING_BOTTOM_DP * deviceDensity);
        gridTop = PADDING_TOP_DP * deviceDensity;
        gridWidth = viewWidth - gridLeft - OFFSET_HORIZONTAL - (PADDING_RIGHT_DP * deviceDensity);

        initPaints();
        initLevels();
    }

    private void initLevels() {
        for (int i = 0; i < levelsPath.length ; i++){
            levelsPath[i] = new Path();
        }
    }

    private void initPaints() {
        emptyLevelPaint = new Paint();
        emptyLevelPaint.setStyle(Paint.Style.FILL);
        emptyLevelPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));

        coloredLevelPaint = new Paint();
        coloredLevelPaint.setStyle(Paint.Style.FILL);
        coloredLevelPaint.setColor(ContextCompat.getColor(getContext(), R.color.bbva_blue));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw bars
        float spaceBetweenLevels = (gridWidth - (OFFSET_HORIZONTAL * levelsPath.length)) / (levelsPath.length);
        float columnLeft = gridLeft;
        float columnRight = columnLeft + spaceBetweenLevels;

        for (Path levelPath: levelsPath){
            levelPath.moveTo(columnLeft, gridBottom);
            levelPath.lineTo(columnRight, gridBottom);
            levelPath.lineTo(columnRight + (OFFSET_HORIZONTAL * deviceDensity), gridTop);
            levelPath.lineTo(columnLeft + (OFFSET_HORIZONTAL * deviceDensity), gridTop);
            levelPath.lineTo(columnLeft, gridBottom);
            levelPath.close();
            canvas.drawPath(levelPath, coloredLevelPaint);

            columnLeft = columnRight + SPACE_BETWEEN_LEVELS;
            columnRight = columnLeft + spaceBetweenLevels;
        }

    }
}
