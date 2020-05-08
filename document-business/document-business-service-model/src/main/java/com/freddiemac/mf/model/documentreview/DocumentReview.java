package com.freddiemac.mf.model.documentreview;

import java.io.Serializable;

public class DocumentReview implements Serializable {
	private static final long serialVersionUID = -733581885299541823L;

	private String documentReviewId;
	private Loan loan;
	private Document document;

	public String getDocumentReviewId() {
		return documentReviewId;
	}

	public void setDocumentReviewId(String documentReviewId) {
		this.documentReviewId = documentReviewId;
	}

	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
