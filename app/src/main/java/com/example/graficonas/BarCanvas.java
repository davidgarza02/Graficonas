package com.example.graficonas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class BarCanvas extends View {
    public static final String[] MONTHS = new String[]{"E", "F", "M", "A", "M", "J"};
    public static final String[] LEVELS = new String[]{"0", "25", "50", "75", "100"};
    public static final float[] DATA = new float[]{1, .4f, .5f, .4f, .6f, .9f};
    private static final float PADDING = 20;
    private static final float GRID_THICKNESS_PX = 10;
    private static final float GUIDELINE_THICKNESS_PX = 3;
    private static final float SPACE_BETWEEN_BARS = 110;
    private static final float SPACE_BETWEEN_DASH_AVERAGE_LINE = 10f;
    private static final float TEXT_SIZE_DP = 16;

    float averageLinePosition  = .3f;

    Paint barPaint;
    Paint blueBarPaint;
    Paint gridPaint;
    Paint averageLinePaint;
    Path averageLinePath;
    TextPaint textPaint;

    int height;
    int width;
    float gridLeft;
    float gridBottom;
    float gridTop;
    float gridRight;

    public BarCanvas(Context context) {
        super(context);
    }

    public BarCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BarCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = getHeight();
        width = getWidth();

        gridLeft = PADDING;
        gridBottom = height - PADDING - (30 * getResources().getDisplayMetrics().density) ;
        gridTop = PADDING;
        gridRight = width - PADDING;

        initPaints();
    }

    private void initPaints() {
        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setColor(ContextCompat.getColor(getContext(), R.color.bar_color));

        gridPaint = new Paint();
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
        gridPaint.setStrokeWidth(GRID_THICKNESS_PX);

        averageLinePaint = new Paint();
        averageLinePaint.setStyle(Paint.Style.STROKE);
        averageLinePaint.setPathEffect(new DashPathEffect(new float[] {SPACE_BETWEEN_DASH_AVERAGE_LINE,
                SPACE_BETWEEN_DASH_AVERAGE_LINE}, 0));
        averageLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        averageLinePaint.setStrokeWidth(GUIDELINE_THICKNESS_PX);

        averageLinePath = new Path();
        float y = gridTop + height * (1f - averageLinePosition);
        averageLinePath.moveTo(gridLeft,y);
        averageLinePath.quadTo(gridLeft,y,gridRight,y);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(TEXT_SIZE_DP * getResources().getDisplayMetrics().density);
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));

        blueBarPaint = new Paint();
        blueBarPaint.setStyle(Paint.Style.FILL);
        blueBarPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    Path newBarPath = new Path();
    Path blueBarPath = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw grid
        canvas.drawLine(gridLeft, gridBottom, gridRight, gridBottom, gridPaint);

        //Draw bars
        float totalColumnSpacing = SPACE_BETWEEN_BARS * (MONTHS.length + 1);
        float columnWidth  = (gridRight - gridLeft - totalColumnSpacing) / MONTHS.length;
        float columnLeft = gridLeft + SPACE_BETWEEN_BARS;
        float columnRight = columnLeft + columnWidth;

        final float VERTEX_OFFSET = -30f;
        final float BLUE_QUANTITY = 0.4f;

        for (int i = 0 ; i < LEVELS.length; i++) {
            canvas.drawText(LEVELS[i], 0, gridBottom - (((float) (height - PADDING * 2)/LEVELS.length) * i)  , textPaint);
        }

        for (int i = 0 ; i < MONTHS.length; i++){
            float top = gridTop + height * (1f - DATA[i]);
            float blueTop = gridTop - height * (1f - (1 - BLUE_QUANTITY));

            newBarPath.moveTo(columnLeft, top - VERTEX_OFFSET);
            newBarPath.lineTo(columnLeft, gridBottom - VERTEX_OFFSET);
            newBarPath.lineTo(columnRight, gridBottom );
            newBarPath.lineTo(columnRight, top );
            newBarPath.lineTo(columnLeft,top - VERTEX_OFFSET);
            newBarPath.close();
            canvas.drawPath(newBarPath, barPaint);

            if(i == 3) {
                blueBarPath.moveTo(columnLeft, (top + blueTop) - VERTEX_OFFSET);
                blueBarPath.lineTo(columnLeft, top - VERTEX_OFFSET);
                blueBarPath.lineTo(columnRight, top);
                blueBarPath.lineTo(columnRight, top + blueTop);
                blueBarPath.lineTo(columnLeft, (top + blueTop) - VERTEX_OFFSET);
                blueBarPath.close();
                canvas.drawPath(blueBarPath, blueBarPaint);
            }

            //draw gridline

//            canvas.drawRect(columnLeft, top, columnRight, gridBottom, barPaint);
            canvas.drawText(MONTHS[i], columnLeft, gridBottom + (30 * getResources().getDisplayMetrics().density), textPaint);

            columnLeft = columnRight + SPACE_BETWEEN_BARS;
            columnRight = columnLeft + columnWidth;
        }

        canvas.drawPath(averageLinePath, averageLinePaint);
    }

}
