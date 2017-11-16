package com.orrin.scab.product.model;

import com.orrin.sca.component.jpa.model.AbstractAuditingEntity;

/**
 * @author orrin.zhang on 2017/8/2.
 */
public class ProductDetail extends AbstractAuditingEntity {

	private static final long serialVersionUID = 1L;

	private String productId;

	private String productName;

	private String productDescId;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescId() {
		return productDescId;
	}

	public void setProductDescId(String productDescId) {
		this.productDescId = productDescId;
	}
}
