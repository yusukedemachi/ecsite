package com.example.ecsite.model.from;

import java.io.Serializable;
import java.util.List;

public class CartForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private long userId;
	private List<Cart> cartList;
	
	public long getUserId() {
		return userId;
	}
	
	public void setuserId(long userId) {
		this.userId = userId;
	}
	
	public List<Cart> getCartList(){
		return cartList;
	}
	
	public void setcartList(List<Cart> cartList) {
		this.cartList = cartList;
	}
}
