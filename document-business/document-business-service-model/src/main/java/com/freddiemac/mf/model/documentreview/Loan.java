package com.freddiemac.mf.model.documentreview;

import java.io.Serializable;

public class Loan implements Serializable {
	private static final long serialVersionUID = -1909058665731800977L;

	private String loanNumberId;
	private String loanType;

	public String getLoanNumberId() {
		return loanNumberId;
	}

	public void setLoanNumberId(String loanNumberId) {
		this.loanNumberId = loanNumberId;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
}
