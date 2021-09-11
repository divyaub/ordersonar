package com.market.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.market.order.dto.OrderDTO;
import com.market.order.entity.Order;
import com.market.order.exception.InfyMarketException;
import com.market.order.repository.OrderRepository;
import com.market.order.repository.ReorderRepository;

@Service
@Transactional
public class OrderService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OrderRepository orderrepo;

	@Autowired
	ReorderRepository reorderRepo;

	// Get specific order by id
	public List<OrderDTO> getSpecificOrder(String orderid) throws InfyMarketException {

		logger.info("Order details of Id {}", orderid);

		Iterable<Order> order = orderrepo.findByOrderid(orderid);
		List<OrderDTO> orderDTO = new ArrayList<OrderDTO>();

		order.forEach(ord -> {
			orderDTO.add(OrderDTO.valueOf(ord));
		});
		if (orderDTO.isEmpty())
			throw new InfyMarketException("Service.ORDERS_NOT_FOUND");
		logger.info("{}", orderDTO);
		return orderDTO;
	}

	// Get all order details
	public List<OrderDTO> getAllOrder() throws InfyMarketException {

		Iterable<Order> orders = orderrepo.findAll();
		List<OrderDTO> orderDTOs = new ArrayList<>();

		orders.forEach(order -> {
			OrderDTO orderDTO = OrderDTO.valueOf(order);
			orderDTOs.add(orderDTO);
		});
		if (orderDTOs.isEmpty())
			throw new InfyMarketException("Service.ORDERS_NOT_FOUND");
		logger.info("Order Details : {}", orderDTOs);
		return orderDTOs;
	}

//	public String addOrder(OrderDTO order) throws InfyMarketException {
//		Order order1 = new Order();
//		order1.setAddress(order.getAddress());
//		order1.setBuyerid(order.getBuyerid());
//		Order ord = orderrepo.save(order1);
//		return ord.getBuyerid();
//	}

	// Place order
	public String saveOrder(OrderDTO orderDTO) throws InfyMarketException {

		Order order = orderrepo.getOrderByBuyerIdAndAddress(orderDTO.getBuyerid(), orderDTO.getAddress());
		if (order != null) {
			return order.getOrderid();
		} else {
			throw new InfyMarketException("Services.ORDER_NOT_PLACED");
		}

	}

	// Reorder
	public boolean reOrder(OrderDTO orderDTO) throws InfyMarketException {
		logger.info("Reordering the order{}", orderDTO.getOrderid());
		Order ord = reorderRepo.findByOrderid(orderDTO.getOrderid());
		if (ord != null && ord.getOrderid().equals(orderDTO.getOrderid())) {
			return true;
		} else {
			throw new InfyMarketException("Services.ORDER_NOT_PLACED");
		}
	}

	// Delete order
	public void deleteOrder(String orderid) throws InfyMarketException {
		Optional<Order> ord = orderrepo.findById(orderid);
		ord.orElseThrow(() -> new InfyMarketException("Service.ORDERS_NOT_FOUND"));
		orderrepo.deleteById(orderid);
	}
}