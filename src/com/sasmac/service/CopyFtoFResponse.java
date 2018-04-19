
package com.sasmac.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * CopyFtoFResponse complex type�� Java �ࡣ
 * 
 * <p>
 * ����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="CopyFtoFResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="return" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CopyFtoFResponse", propOrder = { "_return" })
public class CopyFtoFResponse {

	@XmlElement(name = "return")
	protected int _return;

	/**
	 * ��ȡreturn���Ե�ֵ��
	 * 
	 */
	public int getReturn() {
		return _return;
	}

	/**
	 * ����return���Ե�ֵ��
	 * 
	 */
	public void setReturn(int value) {
		this._return = value;
	}

}
