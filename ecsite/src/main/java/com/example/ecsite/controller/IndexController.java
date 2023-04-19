package com.example.ecsite.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ecsite.model.dao.GoodsRepository;
import com.example.ecsite.model.dao.PurchaseRepository;
import com.example.ecsite.model.dao.UserRepository;
import com.example.ecsite.model.entity.Goods;
import com.example.ecsite.model.entity.Purchase;
import com.example.ecsite.model.entity.User;
import com.example.ecsite.model.from.CartForm;
import com.example.ecsite.model.from.HistoryForm;
import com.example.ecsite.model.from.LoginForm;
import com.example.ecsite.model.dto.HistoryDto;

import com.example.ecsite.model.dto.LoginDto;
import com.google.gson.Gson;

@Controller
@RequestMapping("/ecsite")
public class IndexController {
	
	@Autowired
	private UserRepository userRepos;
	
	@Autowired
	private GoodsRepository goodsRepos;
	
	@Autowired
	private PurchaseRepository purchaseRepos;
	
	private Gson gson = new Gson();
	
	@RequestMapping("/")
	public String index(Model m) {
		List<Goods> goods = goodsRepos.findAll();
		m.addAttribute("goods", goods);
		
		return "index";	
	}

@ResponseBody
@PostMapping("/api/login")
public String loginApi(@RequestBody LoginForm form) {
	List<User> users = userRepos.findByUserNameAndPassword(form.getUserName(),form.getPassword());
	
	LoginDto dto = new LoginDto(0, null, null, "ゲスト");
	if(users.size() > 0) {
		dto = new LoginDto(users.get(0));
	}
	return gson.toJson(dto);
	}


@ResponseBody
@PostMapping("/api/purchase")
public String purchaseApi(@RequestBody CartForm f) {
	
	f.getCartList().forEach((c) -> {
		long total = c.getPrice() * c.getCount();
		purchaseRepos.persist(f.getUserId(), c.getId(), c.getGoodsName(), c.getCount(), total);
	});
	return String.valueOf(f.getCartList().size());
}

@ResponseBody
@PostMapping("/api/history")
public String historyApi(@RequestBody HistoryForm form) {
	String userId = form.getUserId();
	List<Purchase> history = purchaseRepos.findHistory(Long.parseLong(userId));
	List<HistoryDto> historyDtoList = new ArrayList<>();
	history.forEach((v) -> {
		HistoryDto dto = new HistoryDto(v);
		historyDtoList.add(dto);
	});
	return gson.toJson(historyDtoList);
}
}