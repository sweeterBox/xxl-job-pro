package com.xxl.job.model;

import com.xxl.job.utils.JsonUtils;

import java.io.Serializable;

/**
 * common return
 * @author xuxueli 2015-12-4 16:32:31
 * @param <T>
 */
public class R<T> implements Serializable {

	public static final long serialVersionUID = 42L;

	public static final int SUCCESS_CODE = 200;

	public static final int FAIL_CODE = 500;

	public static final R<String> SUCCESS = new R<>(null);

	public static final R<String> FAIL = new R<>(FAIL_CODE, null);

	private int code;

	private String msg;

	private T content;

	public R(){

	}
	public R(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public R(T content) {
		this.code = SUCCESS_CODE;
		this.content = content;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return JsonUtils.obj2Json(this);
	}

}
