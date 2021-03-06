package com.sasmac.service;

import javax.xml.ws.WebFault;

/**
 * This class was generated by Apache CXF 3.1.11 2017-04-28T15:30:45.302+08:00
 * Generated source version: 3.1.11
 */

@WebFault(name = "FileNotFoundException", targetNamespace = "http://webservices.sasmac.com/")
public class FileNotFoundException_Exception extends Exception {

	private com.sasmac.service.FileNotFoundException fileNotFoundException;

	public FileNotFoundException_Exception() {
		super();
	}

	public FileNotFoundException_Exception(String message) {
		super();
	}

	public FileNotFoundException_Exception(String message, Throwable cause) {
		super();
	}

	public FileNotFoundException_Exception(String message,
			com.sasmac.service.FileNotFoundException fileNotFoundException) {
		super();
		this.fileNotFoundException = fileNotFoundException;
	}

	public FileNotFoundException_Exception(String message,
			com.sasmac.service.FileNotFoundException fileNotFoundException, Throwable cause) {
		super();
		this.fileNotFoundException = fileNotFoundException;
	}

	public com.sasmac.service.FileNotFoundException getFaultInfo() {
		return this.fileNotFoundException;
	}
}
