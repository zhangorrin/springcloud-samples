package com.orrin.scab.product.service;

import com.orrin.scab.product.model.ProductDetail;

/**
 * @author orrin.zhang on 2017/8/2.
 */
public interface ProductService {
	ProductDetail findProductDetailByProductId(String productId);
}
