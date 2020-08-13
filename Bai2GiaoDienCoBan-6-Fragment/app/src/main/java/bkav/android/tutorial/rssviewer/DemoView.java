package bkav.android.tutorial.rssviewer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/** <p>Bkav QuangLH</p>
 * 
 * <p>Doan 1</p>
 * 
 * <p>Doan 2
 * <ul>
 * <li>Dong 1
 * <li>Dong 2
 * <li>Dong 3
 * </ul>
 * </p> */
public class DemoView extends View {

    public DemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        
    }
}
