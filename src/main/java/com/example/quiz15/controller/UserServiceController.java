package com.example.quiz15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz15.constants.ResCodeMessage;
import com.example.quiz15.service.ifs.UserService;
import com.example.quiz15.vo.AddInfoReq;
import com.example.quiz15.vo.BasicRes;
import com.example.quiz15.vo.LoginReq;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 * @CrossOrigin</br>
 * 可提供跨域資源共享的請求，雖然前後端系統都在自己的同一台電腦，但前端呼叫後端提供的 API 會被
 * 認為是跨域請求
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
	public BasicRes login(@Valid @RequestBody LoginReq req, HttpSession session) {
		// 檢查 session 是否有存 email 的資訊
		String savedEmail = (String) session.getAttribute("email");
		// 若 savedEmail 得到的值不是 null，表示已經有驗證成功過了；
		// 反之，若是 null 表示 session 沒有記錄相關的資訊 ，也就是 login 沒有成功過
		
		// 若不是 null，表示有驗證成功了，直接回 成功的訊息
		if(savedEmail != null) {
			return new BasicRes(ResCodeMessage.SUCCESS.getCode(), //
					ResCodeMessage.SUCCESS.getMessage());
		}
		
		// 第一次登入: 驗證 email 和 password
		BasicRes res = userService.login(req);
		// 若 res 的 code 是 200，表示登入成功 --> 使用 session 記錄使用者相關訊息
		if(res.getCode() == 200) {
			session.setAttribute("email", req.getEmail());
		}
		return res;
	}
	
	@GetMapping("user/logout")
	public BasicRes logout(HttpSession session) {
		// 讓 session 失效 --> 下次再登入時，即使原本的 session 時效尚未過期，也會是新的 session
		session.invalidate();
		return new BasicRes(ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage());
	}
	
	/**
	 * 此方法主要是紀錄 session 常用的一些方法
	 */
	@Hidden // 讓此 API 不顯示在 Open API 的頁面
	@PostMapping("user/login_test")
	public BasicRes login_test(@Valid @RequestBody LoginReq req, HttpSession session) {
		// 取得 session_id
		System.out.println("email: " + req.getEmail() + "; session_id: " + session.getId());
		// 更改 session 的存活時間(單位是秒，預設是30分鐘)
		// 1. 每一個 session 在其存活時間內其 session_id 都會一樣；但每個 client 的 session_id 是不一樣的
		// 2. 注意的是從上一次有使用 http 協議後到下一次，只要不超過 session 的存活時間，其 session_id 都會相同，
		//    所以只要在存活時間內有持續透過 http 協議呼叫 API，其 session 的存活時間都會一直延續下去(重新計算存活時間)
		session.setMaxInactiveInterval(180);
		
		// 使用 session 記錄一些資訊: 字串 email 是 key； req.getEmail() 是 value
		session.setAttribute("email", req.getEmail());
		
		// 透過 key 從 session 取得紀錄的資訊
		// 因為 session 中的 value 資料型態是 Object，可以將其強制轉型為 String
		String mail = (String) session.getAttribute("email");
		
		return null;
	}

}
