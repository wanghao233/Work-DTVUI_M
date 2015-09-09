/**
 * @filename DTV Ƶ����Ϣ����
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;


import android.os.Parcelable;

/** **/
public abstract class CarrierInfo implements Parcelable {
    /** **/
    public int miIndex;

    /** {@link DTVCnst.ConstDemodeType})*/
    public int miDemodType;

    /** **/
    public int miTsID;

    /** **/
    public int miOrgNetId;

    /** **/
    public int miFrequencyK;

    /** {@link DTVConstant.SpectrumMode} **/
    public byte mcSpectrum;
}
