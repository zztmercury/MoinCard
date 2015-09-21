package com.lovemoin.card.app.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.utils.DisplayUtil;

public class PlusStarDrawView extends View {
    private static final int starWidth = 40;
    private static final int starHeight = 40;
    private int count = 3;
    private Bitmap star;
    private Paint paint;

    public PlusStarDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        star = BitmapFactory.decodeResource(getResources(), R.drawable.star, options);
        star = Bitmap.createScaledBitmap(star, DisplayUtil.dp2Px(context, starWidth), DisplayUtil.dp2Px(context, 40), true);
        paint = new Paint();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        //先根据宽度进行缩小
        while (width / inSampleSize > reqWidth) {
            inSampleSize++;
        }
        //然后根据高度进行缩小
        while (height / inSampleSize > reqHeight) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (count != -1) {

            int dx = DisplayUtil.dp2Px(getContext(), starWidth + 10);
            int dy = DisplayUtil.dp2Px(getContext(), starHeight);

            int width = getWidth();
            int height = getHeight();

            for (int i = 0; i < count; i++) {
                int x = (i % 5) * dx;
                int y = (i / 5) * dy;
                canvas.drawBitmap(star, x, y, paint);
            }


        } else {
            setVisibility(GONE);
        }
        super.onDraw(canvas);
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        if (count >= 5) {
            width = DisplayUtil.dp2Px(getContext(), (5 * starWidth + 4 * 10));
        } else {
            width = DisplayUtil.dp2Px(getContext(), (count * starWidth + (count - 1) * 10));
        }
        height = DisplayUtil.dp2Px(getContext(), (float) (starHeight * (Math.ceil(count / 5.0))));
        setMeasuredDimension(width < 0 ? 0 : width, height < 0 ? 0 : height);
    }
}
