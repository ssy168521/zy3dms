package com.web.dao;

/**
 * @author Administrator
 * @ClassName:Table
 * @Version 版本
 * @ModifiedBy 修改人
 * @date 2018年1月29日 下午10:01:16
 */
public class Table {
	// 手动添加
	private String fieldName;
	private String fieldTypeName;
	private int fieldid;
	private boolean nullValue;
	private boolean primaryKey;

	// 有参构造
	public Table(String fieldName) {
		this.setFieldName(fieldName);// 将局部变量的值传递给成员变量
	}

	public Table() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldTypeName() {
		return fieldTypeName;
	}

	public void setFieldTypeName(String fieldTypeName) {
		this.fieldTypeName = fieldTypeName;
	}

	public boolean isNullValue() {
		return nullValue;
	}

	public void setNullValue(boolean nullValue) {
		this.nullValue = nullValue;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public int getFieldid() {
		return fieldid;
	}

	public void setFieldid(int fieldid) {
		this.fieldid = fieldid;
	}
}
