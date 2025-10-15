package com.bistu.common.JNI.dto;

public class DK_BOX {
    public double X0;
    public double Y0;	// 左上角坐标
    public double X1;
    public double Y1;	// 右下角坐标

    public DK_BOX(){
	X0=0.0; Y0=0.0; X1=0.0; Y1=0.0;
    }

    public DK_BOX(double x0, double y0, double x1, double y1)
    {
        X0=x0; Y0=y0; X1=x1; Y1=y1;
    }

//    boolean operator==(const __DK_BOX& box) const
//    {
//        if (this == &box)
//            return DK_TRUE;
//        return DEQUAL(X0, box.X0) && DEQUAL(Y0, box.Y0) && DEQUAL(X1, box.X1) && DEQUAL(Y1, box.Y1);
//    }

//    boolean operator!=(const __DK_BOX& box) const
//    {
//        return !(*this == box);
//    }

    public double Width()
    {


        return Math.abs(X1 - X0);
    }

//    private double fabs(double v) {
//        return v;
//    }

    public double Height()
    {

        
        return Math.abs(Y1 - Y0);
    }

//    private double fabs(double v) {
//        return v;
//    }

    // 判断是否为空区域
    boolean IsEmpty()
    {
        return (X0 == X1 && Y0 == Y1);
    }

    // 判断点是否位于BOX内
    boolean PosInBox( DK_POS pos)
    {
        return (pos.X >= X0 && pos.X <= X1 && pos.Y >= Y0 && pos.Y <= Y1);
    }

    // 判断是否包含目标BOX
    boolean ContainsBox( DK_BOX box)
    {
        return (X0 <= box.X0 && X1 >= box.X1 && Y0 <= box.Y0 && Y1 >= box.Y1);
    }

    // 判断两个BOX是否有交集
    boolean IsIntersect( DK_BOX box)
    {
        return (X0 <= box.X1 && X1 >= box.X0 && Y0 <= box.Y1 && Y1 >= box.Y0);
    }

    // 偏移指定距离
    void Offset(double dx, double dy)
    { X0 += dx;  X1 += dx; Y0 += dy; Y1 += dy; }
}
