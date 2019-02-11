package com.odenktools.orderservice.repository;

import com.odenktools.common.model.Cart;
import com.odenktools.common.model.CartItem;
import com.odenktools.common.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

}
