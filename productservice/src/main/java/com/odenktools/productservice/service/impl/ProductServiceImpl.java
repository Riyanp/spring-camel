package com.odenktools.productservice.service.impl;

import com.github.slugify.Slugify;
import com.mysema.query.jpa.impl.JPAQuery;
import com.odenktools.common.dto.ProductDto;
import com.odenktools.common.filter.QueryFilter;
import com.odenktools.common.model.Product;
import com.odenktools.common.model.QProduct;
import com.odenktools.common.response.RestResponsePage;
import com.odenktools.productservice.repository.ProductRepository;
import com.odenktools.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl extends QueryFilter<Product> implements ProductService {

	private final ProductRepository productRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public Boolean existsByProductName(String productName) {
		return productRepository.existsByProductName(productName);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Product> findAllBy(String productName, Sort sort, Pageable pageable) {

		QProduct qProduct = QProduct.product;
		JPAQuery query = new JPAQuery(this.entityManager).from(qProduct);

		//-- Pre-filtering
		query.where(qProduct.isActive.eq(1));
		query.where(qProduct.isDeleted.eq(0));

		if (productName != null && !productName.isEmpty()) {
			query.where(qProduct.productName.containsIgnoreCase(productName));
		}

		long total = query.count();
		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());
		for (Sort.Order order : sort) {
			query.orderBy(this.toOrderSpecifier(Product.class, qProduct.getMetadata(), order));
		}
		return new RestResponsePage<>(query.list(qProduct), pageable, total);
	}

	@Override
	public Boolean isProductWithIdExist(String productId) {
		Optional<Product> productOptional = this.productRepository.findById(productId);
		return productOptional.isPresent();
	}

	@Override
	public ProductDto createProduct(ProductDto request) {

		Product product = Product.builder()
				.productName(request.getProductName())
				.sku(new Slugify().slugify(request.getProductName()))
				.shortDescription(request.getShortDescription())
				.longDescription(request.getLongDescription())
				.quantity(request.getQuantity())
				.stockStatus(request.getStockStatus())
				.thumbnailImageUrl(request.getThumbnailImageUrl())
				.price(request.getPrice())
				.isActive(1)
				.isDeleted(0)
				.build();

		// -- save
		request.setId(this.productRepository.save(product).getId());

		return request;
	}
}
