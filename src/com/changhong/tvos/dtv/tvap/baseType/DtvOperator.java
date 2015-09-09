package com.changhong.tvos.dtv.tvap.baseType;

import com.changhong.tvos.dtv.vo.Operator;

public class DtvOperator implements Comparable<DtvOperator>{
	private String mOperatorName;
	private int operatorCode;
	private String province;
	private String city;

	public DtvOperator(Operator operator) {
		mOperatorName = operator.mstrOperatorName;
		operatorCode =  operator.miOperatorCode;
		province = operator.province;
		city = operator.city;
	}

	public String getOperatorName() {
		return mOperatorName;
	};

	public int getOperatorCode() {
		return operatorCode;
	}

	public String getProvince() {
		return province;
	}


	public String getCity() {
		return city;
	}

	@Override
	public int compareTo(DtvOperator arg0) {
		// TODO Auto-generated method stub
		if(null != arg0){
			
			return operatorCode - arg0.operatorCode;
		}else{
			return 1;
		}
	};
}