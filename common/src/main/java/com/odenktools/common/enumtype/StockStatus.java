package com.odenktools.common.enumtype;

public enum StockStatus {
	AVAILABLE("available"),
	OUT_OF_STOCK("outofstock"),
	PREORDER("preorder");

	private String stockStatus;
	StockStatus(String stockStatus) {
	}

	public String getStockStatus() {
		return stockStatus;
	}
}
