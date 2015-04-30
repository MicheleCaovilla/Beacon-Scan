package it.unipd.mich.beaconscan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mich on 26/04/15.
 */
public class MapView extends View {
    static Paint brushCircleDist;
    static Paint brushBeaconPos;
    static Paint brushGrid;
    static Paint brushText;
    static Paint brushResult;
    static Paint brushPhone;


    MyBeacon[] beacons;
    MyBeacon bPhone, bNlat;


    int num_col, num_row;
    int w;
    int h;

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);

        brushCircleDist = new Paint();
        brushCircleDist.setColor(Color.CYAN);
        brushCircleDist.setAntiAlias(true);
        brushCircleDist.setStyle(Paint.Style.STROKE); //only line

        brushBeaconPos = new Paint();
        brushBeaconPos.setColor(Color.RED);
        brushBeaconPos.setAntiAlias(true);

        brushResult = new Paint();
        brushResult.setColor(Color.YELLOW);
        brushResult.setAntiAlias(true);

        brushPhone = new Paint();
        brushPhone.setColor(Color.GREEN);
        brushPhone.setAntiAlias(true);

        brushGrid = new Paint();
        brushGrid.setColor(Color.GRAY);
        brushGrid.setAntiAlias(true);

        brushText = new Paint();
        brushText.setColor(Color.BLACK);
        brushText.setAntiAlias(true);
        brushText.setTextSize(32);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //calcolo della dimensione del quadretto (prendo minore tra spessore riga e spessore colonna
        int width_square;

        if (num_col < num_row) {
            width_square = h / num_row;
            num_col = w / width_square;
        } else {
            width_square = w / num_col;
            num_row = h / width_square;
        }


        //array for vertical lines
        float[] linesVert = new float[4 * (num_col + 1)];

        for (int i = 0; i < 4 * (num_col + 1); i = i + 4) {
            linesVert[i] = linesVert[i + 2] = width_square * (i / 4);
            linesVert[i + 1] = 0;
            linesVert[i + 3] = h;
        }

        //array for horizontal lines
        float[] linesHoriz = new float[4 * (num_row + 1)];

        for (int i = 0; i < 4 * (num_row + 1); i = i + 4) {
            linesHoriz[i + 1] = linesHoriz[i + 3] = width_square * (i / 4);
            linesHoriz[i] = 0;
            linesHoriz[i + 2] = w;
        }

        canvas.drawLines(linesVert, brushGrid);
        canvas.drawLines(linesHoriz, brushGrid);


        for (MyBeacon b : beacons) {
            drawBeacon(canvas, width_square, b, brushBeaconPos);
        }

        drawBeacon(canvas, width_square, bPhone, brushPhone);
        drawBeacon(canvas, width_square, bNlat, brushResult);
        invalidate();
    }


    public void drawBeacon(Canvas c, int width_square, MyBeacon b, Paint brush) {
        float x = (float) ((b.getX() + 2) * width_square);
        float y = (float) ((b.getY() + 2) * width_square);
        float d;
        if (b.getDistance() == "nil") {   //don't show distance
            d = 0;
        } else {
            d = (float) (b.getDistanceNum() * b.getAlpha() * width_square);
        }

        c.drawCircle(x, y, 23, brush);
        c.drawCircle(x, y, d, brushCircleDist);
        c.drawText(b.getBeac_numb(), x - 18, y + 10, brushText);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {    //get view dimension
        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(w, h);
    }
}