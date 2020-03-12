package com.example.criteria;

public enum BoardType {
	  공지사항("공지사항"),
	  요청("자유게시판");

	
	 private final String value;
	 
	 BoardType(String value){
	    this.value = value;
	  }

	  public String getValue(){
	    return value;
	  }
	}