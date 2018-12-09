package com.xiaohai.constant.enums;

public enum TestType {

	Test1(1, "枚举类型一"),
	Test2(2, "枚举类型一"),
	Test3(3, "枚举类型一"),
	
	;
    
    private int value;
    private String remark;
	private TestType(int val, String remark) {
	     this.value = val;
	     this.remark = remark;
	}
	
	private static TestType[] values = values();
	public static TestType getEnum(int val) {
		for (TestType a : values) {
			if (a.getValue() == val) 
				return a;
		}
		
		return null;
	}
	
	public int getValue() {
	     return this.value;
	}
	
	public String getRemark() {
		return this.remark;
	}
    
}
