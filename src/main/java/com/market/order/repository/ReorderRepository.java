package com.market.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.market.order.entity.Order;

public interface ReorderRepository extends JpaRepository<Order, String>{
	Order findByOrderid(String orderid);
}
