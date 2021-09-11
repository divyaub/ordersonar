package com.market.order.controller;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.market.order.dto.OrderDTO;
import com.market.order.dto.ProductsorderedDTO;
import com.market.order.exception.InfyMarketException;
import com.market.order.service.OrderService;
import com.market.order.service.ProductOrderService;

@RestController
@CrossOrigin
@RequestMapping
public class OrderController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	Environment environment;
	@Autowired
	private OrderService orderservice;
	@Autowired
	ProductOrderService proser;

	// Get orders by ID
	@RequestMapping(value = "/api/orders/{orderid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderDTO>> getSpecificOrder(@PathVariable String orderid) throws InfyMarketException {
		try {
			logger.info("Order details {}", orderid);
			List<OrderDTO> orders = orderservice.getSpecificOrder(orderid);
			return new ResponseEntity<>(orders, HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);
		}

	}

	// Get all orders
	@GetMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderDTO>> getAllOrder() throws InfyMarketException {
		try {
			logger.info("Fetching all products");
			List<OrderDTO> orderdto = orderservice.getAllOrder();
			return new ResponseEntity<>(orderdto, HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);
		}

	}

	// Placeorders->POST
	@RequestMapping(value = "/orders/placeorders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> searchOrder(@RequestBody OrderDTO orderDTO) throws InfyMarketException {
		try {
			String orderid = orderservice.saveOrder(orderDTO);
			String successMessage = environment.getProperty("API.ORDER_SUCCESS") + orderid;
			return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);
		}
	}

	// Reorder
	@PostMapping(value = "/orders/reorder/{orderid}/{orderid}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> reOrder(@RequestBody OrderDTO orderDTO) throws InfyMarketException {
		try {
			logger.info("Reordering the order {}", orderDTO.getOrderid());
			boolean order = orderservice.reOrder(orderDTO);
			String successMessage = environment.getProperty("API.ORDER_SUCCESS") + order;
			return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);
		}

	}

	// Get products ordered by ProductId
	@RequestMapping(value = "/api/productsorders/{prodid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductsorderedDTO>> getProductById(@PathVariable String prodid)
			throws InfyMarketException {
		try {
			logger.info("product details request for ordered product {}", prodid);
			List<ProductsorderedDTO> orders = proser.getProductById(prodid);
			return new ResponseEntity<>(orders, HttpStatus.CREATED);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);
		}
	}

	// Delete order-->DELETE
	@DeleteMapping(value = "/order/{orderid}")
	public ResponseEntity<String> deleteOrder(@PathVariable String orderid) throws InfyMarketException {
		try {
			orderservice.deleteOrder(orderid);
			String successMessage = environment.getProperty("API.DELETE_SUCCESS");
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);

		}

	}
}
