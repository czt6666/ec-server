package com.bistu.common.JNI.dto;

public class DK_POS {
    public double X;
    public double Y;

    public DK_POS() {
        X = 0.0;
        Y = 0.0;
    }

    public DK_POS(double x, double y) {
        X = x;
        Y = y;
    }


//    boolean operator==( DK_POS& pos)
//    {
//        return DEQUAL(X, pos.X) && DEQUAL(Y, pos.Y);
//    }

//    boolean operator!=( DK_POS& pos)
//    {
//        return !(*this == pos);
//    }

    public boolean IsEmpty() { return X == 0.0 && Y == 0.0; }

    // 偏移指定距离
    public void Offset(double dx, double dy) { X += dx; Y += dy; }
}
