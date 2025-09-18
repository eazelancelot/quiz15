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
	public BasicRes login(@Valid @RequestBody LoginReq req, HttpSession session) {
		// �ˬd session �O�_���s email ����T
		String savedEmail = (String) session.getAttribute("email");
		// �Y savedEmail �o�쪺�Ȥ��O null�A��ܤw�g�����Ҧ��\�L�F�F
		// �Ϥ��A�Y�O null ��� session �S���O����������T �A�]�N�O login �S�����\�L
		
		// �Y���O null�A��ܦ����Ҧ��\�F�A�����^ ���\���T��
		if(savedEmail != null) {
			return new BasicRes(ResCodeMessage.SUCCESS.getCode(), //
					ResCodeMessage.SUCCESS.getMessage());
		}
		
		// �Ĥ@���n�J: ���� email �M password
		BasicRes res = userService.login(req);
		// �Y res �� code �O 200�A��ܵn�J���\ --> �ϥ� session �O���ϥΪ̬����T��
		if(res.getCode() == 200) {
			session.setAttribute("email", req.getEmail());
		}
		return res;
	}
	
	@GetMapping("user/logout")
	public BasicRes logout(HttpSession session) {
		// �� session ���� --> �U���A�n�J�ɡA�Y�ϭ쥻�� session �ɮĩ|���L���A�]�|�O�s�� session
		session.invalidate();
		return new BasicRes(ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage());
	}
	
	/**
	 * ����k�D�n�O���� session �`�Ϊ��@�Ǥ�k
	 */
	@Hidden // ���� API ����ܦb Open API ������
	@PostMapping("user/login_test")
	public BasicRes login_test(@Valid @RequestBody LoginReq req, HttpSession session) {
		// ���o session_id
		System.out.println("email: " + req.getEmail() + "; session_id: " + session.getId());
		// ��� session ���s���ɶ�(���O��A�w�]�O30����)
		// 1. �C�@�� session �b��s���ɶ����� session_id ���|�@�ˡF���C�� client �� session_id �O���@�˪�
		// 2. �`�N���O�q�W�@�����ϥ� http ��ĳ���U�@���A�u�n���W�L session ���s���ɶ��A�� session_id ���|�ۦP�A
		//    �ҥH�u�n�b�s���ɶ���������z�L http ��ĳ�I�s API�A�� session ���s���ɶ����|�@������U�h(���s�p��s���ɶ�)
		session.setMaxInactiveInterval(180);
		
		// �ϥ� session �O���@�Ǹ�T: �r�� email �O key�F req.getEmail() �O value
		session.setAttribute("email", req.getEmail());
		
		// �z�L key �q session ���o��������T
		// �]�� session ���� value ��ƫ��A�O Object�A�i�H�N��j���૬�� String
		String mail = (String) session.getAttribute("email");
		
		return null;
	}

}
