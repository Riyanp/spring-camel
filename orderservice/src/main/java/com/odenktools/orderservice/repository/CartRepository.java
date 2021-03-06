package com.odenktools.orderservice.repository;

import com.odenktools.common.model.Cart;
import com.odenktools.common.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

	@Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.customerId = :customerId")
	Optional<Cart> findByAccountIdAndFetchCartItems(@Param("customerId") String customerId);

	Cart findByCustomerId(String customerId);
}
