package com.changhong.tvos.dtv.vo;

import com.changhong.tvos.model.ChOsType.EnumPanel3DType;
import android.os.Parcel;
import android.os.Parcelable;

public class InterPanelInfo implements Parcelable {

    public String mstrPanelName;
    public int miPanelWidth;
    public int miPanelHeight;
    public int miPanelSize;
    public EnumPanel3DType mePanel3DType;

    public static final Creator<InterPanelInfo> CREATOR = new Parcelable.Creator<InterPanelInfo>() {
        public InterPanelInfo createFromParcel(Parcel source) {
            return new InterPanelInfo(source);
        }

        public InterPanelInfo[] newArray(int size) {
            return new InterPanelInfo[size];
        }
    };

    public InterPanelInfo() {
    }

    public InterPanelInfo(int miPanelWidth, int miPanelHeight,
                          EnumPanel3DType d3d) {
        this.miPanelWidth = miPanelWidth;
        this.miPanelHeight = miPanelHeight;
        this.mePanel3DType = d3d;
    }

    private InterPanelInfo(Parcel source) {
        readFromParcel(source);
    }

    public void readFromParcel(Parcel in) {
        this.miPanelWidth = in.readInt();
        this.miPanelHeight = in.readInt();
        switch (in.readInt()) {
            case 0:
                this.mePanel3DType = EnumPanel3DType.PANEL_PDP_2D;
                break;
            case 1:
                this.mePanel3DType = EnumPanel3DType.PANEL_PDP_SG_HALF3D;
                break;
            case 2:
                this.mePanel3DType = EnumPanel3DType.PANEL_PDP_SG_FULL3D;
                break;
            case 3:
                this.mePanel3DType = EnumPanel3DType.PANEL_LCD_2D;
                break;
            case 4:
                this.mePanel3DType = EnumPanel3DType.PANEL_LCD_PR_3D;
                break;
            case 5:
                this.mePanel3DType = EnumPanel3DType.PANEL_LCD_SG_3D;
                break;

        }
        //this.mePanel3DType.PANEL_PDP_2D = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.miPanelWidth);
        dest.writeInt(this.miPanelHeight);
        switch (this.mePanel3DType) {

            case PANEL_PDP_2D:
                dest.writeInt(0);
                break;

            case PANEL_PDP_SG_HALF3D:
                dest.writeInt(1);
                break;

            case PANEL_PDP_SG_FULL3D:
                dest.writeInt(2);
                break;

            case PANEL_LCD_2D:
                dest.writeInt(3);
                break;

            case PANEL_LCD_PR_3D:
                dest.writeInt(4);
                break;

            case PANEL_LCD_SG_3D:
                dest.writeInt(5);
                break;
        }
        //	 dest.writeInt(this.mePanel3DType);
    }
}
