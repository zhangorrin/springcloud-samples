package com.orrin.scab.product.service.impl;

import com.orrin.scab.product.model.ProductDetail;
import com.orrin.scab.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author orrin.zhang on 2017/8/2.
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	RestTemplate restTemplate;

	@Override
	public ProductDetail findProductDetailByProductId(String productId) {
		ProductDetail productDetail = new ProductDetail();
		productDetail.setProductId("1");
		productDetail.setProductName("产品一号");
		productDetail.setProductDescId("1");
		return productDetail;
	}

}
