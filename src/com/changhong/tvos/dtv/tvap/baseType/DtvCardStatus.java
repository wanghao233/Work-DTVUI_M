package com.changhong.tvos.dtv.tvap.baseType;

import com.changhong.tvos.dtv.vo.DTVCardStatus;
import com.changhong.tvos.dtv.vo.DTVCardStatus.CardStatus;
import com.changhong.tvos.dtv.vo.DTVCardStatus.CardType;

public class DtvCardStatus{

	private int mCardType =CardType.CARD_TYPE_NO_CARD;
	private int mCardState =CardStatus.CARD_STATUS_INVALID;
	
	public DtvCardStatus(DTVCardStatus dtvCardStatus){
		this.mCardType = dtvCardStatus.miCardType;
		this.mCardState = dtvCardStatus.miCardStatus;
	}
	public DtvCardStatus(int cardType,int cardState){
		this.mCardType = cardType;
		this.mCardState = cardState;
	}
	public int getCardType(){
		return mCardType;
	}
	public int getCardStatus(){
		return mCardState;
	}
}