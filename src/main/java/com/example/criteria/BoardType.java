package com.example.criteria;

public enum BoardType {
	  ��������("��������"),
	  ��û("�����Խ���");

	
	 private final String value;
	 
	 BoardType(String value){
	    this.value = value;
	  }

	  public String getValue(){
	    return value;
	  }
	}