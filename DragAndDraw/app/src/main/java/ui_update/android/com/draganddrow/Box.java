package ui_update.android.com.draganddrow;


import android.graphics.PointF;

public class Box {
    private PointF mOrigin, mCurrent;

    public Box(PointF origin) {
        mOrigin = mCurrent = origin;
    }

    public PointF getOrigin() {
        return mOrigin;
    }


    public void setOrigin(PointF mOrigin) {
        this.mOrigin = mOrigin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }


    public void setCurrent(PointF mCurrent) {
        this.mCurrent = mCurrent;
    }
}
