package com.changhong.tvos.dtv.tvap;

import com.changhong.tvos.dtv.tvap.baseType.DtvCardStatus;

public class DtvCicaManager{
	private static DtvInterface mDtvInterface =DtvInterface.getInstance(); 
	
	public static DtvCardStatus getCardStatus(){
		DtvCardStatus mCardStatus =null;
		mCardStatus =mDtvInterface.getCicaCardStatus();
		return mCardStatus;
	}
	
	public static void cicaFlushMemory(){
		mDtvInterface.cicaFlushMemory();
	}
	
	public static void cicaQueryControl(int type, int msgID, int menuID, int operand, int opcode, int defOpcode, int inputItems, String[] inputList){
		mDtvInterface.cicaQueryControl(type, msgID, menuID, operand, opcode, defOpcode, inputItems, inputList);
	}
}