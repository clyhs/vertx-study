/**
 * 
 */
package com.abvert.web.vo;

/**
 * @date Feb 3, 2022 11:17:21 PM
 *
 * @author 大鱼
 *
 */
public class Response {
	
	private String msg = "";
	private String code;
	
	Response () {}
	
	public Response(String code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	

}
