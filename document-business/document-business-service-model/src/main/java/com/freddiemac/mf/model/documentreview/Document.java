package com.freddiemac.mf.model.documentreview;

import java.io.Serializable;

public class Document implements Serializable {
	private static final long serialVersionUID = 1355546089800525011L;

	private String documentId;
	private String documentType;
	private String documentTitle;

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentTitle() {
		return documentTitle;
	}

	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
