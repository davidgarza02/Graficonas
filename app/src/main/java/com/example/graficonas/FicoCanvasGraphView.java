package com.example.graficonas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class FicoCanvasGraphView extends View {
    public static final String[] MONTHS = new String[]{"Jul", "Aug", "Sep", "Oct"};
    public static final Float[] LEVELS = new Float[]{0.5f, 0.3f, 0.5f, 0.2f};
    public static final String[] LEVELS_NAME = new String[]{"500", "570", "640", "710", "780", "850"};
    private static final float LEFT_LEVELS_WIDTH_DP = 50;
    private static final float BOTTOM_LEVELS_HEIGHT_DP = 30;
    private static final float PADDING_TOP_DP = 25;
    private static final float PADDING_RIGHT_DP = 10;
    private static final float GRID_THICKNESS_PX = 2;
    private static final float LINE_THICKNESS_PX = 10;
    private static final float TEXT_SIZE_DP = 15;
    private static final float MARGIN_GRID_LINE_MONTHS_DP = 20;

    private Paint bottomLinePaint;
    private TextPaint textPaint;
    private Paint linePaint;
    private Paint whiteLinePaint;

    private Path linePath = new Path();

    private float horizontalLimit = 1;

    private int viewWidth;
    private int viewHeight;
    private float gridLeft;
    private float gridBottom;
    private float gridTop;
    private float gridRight;
    private float gridWidth;
    private float deviceDensity;

    public FicoCanvasGraphView(Context context) {
        super(context);
    }

    public FicoCanvasGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FicoCanvasGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = getHeight();
        viewWidth = getWidth();
        deviceDensity = getResources().getDisplayMetrics().density;

        gridLeft = LEFT_LEVELS_WIDTH_DP * deviceDensity;
        gridBottom = viewHeight - (BOTTOM_LEVELS_HEIGHT_DP * deviceDensity);
        gridTop = PADDING_TOP_DP * deviceDensity;
        gridRight = viewWidth - (PADDING_RIGHT_DP * deviceDensity);
        gridWidth = viewWidth - gridLeft;

        initPaints();
    }

    private void initPaints() {
        bottomLinePaint = new Paint();
        bottomLinePaint.setStyle(Paint.Style.FILL);
        bottomLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
        bottomLinePaint.setStrokeWidth(GRID_THICKNESS_PX);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(TEXT_SIZE_DP * getResources().getDisplayMetrics().density);
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.black));

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(LINE_THICKNESS_PX);
        linePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        whiteLinePaint = new Paint();
        whiteLinePaint.setStyle(Paint.Style.FILL);
        whiteLinePaint.setStrokeWidth(LINE_THICKNESS_PX * 2);
        whiteLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(gridLeft, gridBottom, gridRight, gridBottom, bottomLinePaint);

        float gridWidth = gridRight - gridLeft;
        float spaceBetweenBars = gridWidth / (MONTHS.length * 1.7f);
        float columnLeft = gridLeft + (PADDING_RIGHT_DP * deviceDensity);
        float columnRight = columnLeft + spaceBetweenBars;

        for (int i = 0; i < LEVELS_NAME.length; i++) {
            canvas.drawText(LEVELS_NAME[i], PADDING_RIGHT_DP * deviceDensity, gridBottom - (((float) (viewHeight - (PADDING_TOP_DP * deviceDensity) * 2) / LEVELS_NAME.length) * i), textPaint);
        }

        for (String month : MONTHS) {
            canvas.drawText(month, columnLeft, gridBottom + (MARGIN_GRID_LINE_MONTHS_DP * deviceDensity), textPaint);
            columnLeft = columnRight + spaceBetweenBars;
            columnRight = columnLeft + spaceBetweenBars;
        }

        spaceBetweenBars = gridWidth / MONTHS.length;
        columnLeft = gridLeft;
        columnRight = columnLeft + spaceBetweenBars;


        for (int i = 0; i < LEVELS.length; i++) {
            float limit = (gridWidth * horizontalLimit) + gridLeft;
            boolean limitMet = columnRight > limit;

            if (columnRight > gridRight) {
                columnRight = gridRight;
            }

            float startY = gridTop + (viewHeight * LEVELS[i]);
            float stopY = gridTop + (viewHeight * LEVELS[i < LEVELS.length - 1 ? i + 1 : i]);

            float distanceVerticalSegment = (stopY - startY) / 100;
            float distanceHorizontal = columnRight - columnLeft;
            float limitPercentageHorizontal = ((limit - columnLeft) * 100) / distanceHorizontal;
            float point = startY + (distanceVerticalSegment * limitPercentageHorizontal);

            canvas.drawLine(columnLeft, startY, Math.min(columnRight, limit), columnRight > limit ? point : stopY, linePaint);

            columnLeft = columnLeft + spaceBetweenBars;
            columnRight = columnRight + spaceBetweenBars;

            if (limitMet) {
                break;
            }
        }

    }

    public void updateHorizontalLimit(float limit) {
        this.horizontalLimit = limit;
        invalidate();
    }
}
