package com.example.quiz15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz15.service.ifs.UserService;
import com.example.quiz15.vo.AddInfoReq;
import com.example.quiz15.vo.BasicRes;
import com.example.quiz15.vo.LoginReq;

import jakarta.validation.Valid;

/**
 * @CrossOrigin</br>
 * �i���Ѹ��귽�@�ɪ��ШD�A���M�e��ݨt�γ��b�ۤv���P�@�x�q���A���e�ݩI�s��ݴ��Ѫ� API �|�Q
 * �{���O���ШD
 */
@CrossOrigin 
@RestController
public class UserServiceController {

	@Autowired
	private UserService userService;

	@PostMapping("user/add_info")
	public BasicRes addInfo(@Valid @RequestBody AddInfoReq req) {
		return userService.addInfo(req);
	}

	@PostMapping("user/login")
	public BasicRes login(@Valid @RequestBody LoginReq req) {
		return userService.login(req);
	}

}
