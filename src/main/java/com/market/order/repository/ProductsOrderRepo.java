package com.market.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.market.order.entity.Productsorder;

public interface  ProductsOrderRepo extends JpaRepository<Productsorder, String>{
	
	List<Productsorder> findByProdid(String prodid);
	}

