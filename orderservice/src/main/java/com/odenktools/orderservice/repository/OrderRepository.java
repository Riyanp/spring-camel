package com.odenktools.orderservice.repository;

import com.odenktools.common.model.Order;
import com.odenktools.common.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

}
