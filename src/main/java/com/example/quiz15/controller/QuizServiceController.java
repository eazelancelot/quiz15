package com.example.quiz15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz15.constants.ResCodeMessage;
import com.example.quiz15.service.ifs.QuizService;
import com.example.quiz15.vo.BasicRes;
import com.example.quiz15.vo.DeleteReq;
import com.example.quiz15.vo.FeedbackRes;
import com.example.quiz15.vo.FeedbackUserRes;
import com.example.quiz15.vo.FillinReq;
import com.example.quiz15.vo.QuestionRes;
import com.example.quiz15.vo.QuizCreateReq;
import com.example.quiz15.vo.QuizUpdateReq;
import com.example.quiz15.vo.SearchReq;
import com.example.quiz15.vo.SearchRes;
import com.example.quiz15.vo.StatisticsRes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class QuizServiceController {

	@Autowired
	private QuizService quizService;

	// 假設只有登入成功之後才能使用 quizService.create
	@PostMapping("quiz/create")
	public BasicRes create(@Valid @RequestBody QuizCreateReq req, HttpSession session) throws Exception {
		BasicRes checkRes = checkLogin(session);
		// 若 checkRes != null，表示檢查不通過，直接將錯誤訊息返回
		if(checkRes != null) {
			return checkRes;
		}
		return quizService.create(req);
	}

	private BasicRes checkLogin(HttpSession session) {
		// 檢查 session 是否有存 email 的資訊
		String savedEmail = (String) session.getAttribute("email");
		// 若 savedEmail 是 null，返回請先登入的訊息
		if (savedEmail == null) {
			return new BasicRes(ResCodeMessage.PLEASE_LOGIN_FIRST.getCode(), //
					ResCodeMessage.PLEASE_LOGIN_FIRST.getMessage());
		}
		// 若 savedEmail 不是 null，表示已登入成功，回傳 null
		return null;
	}

	// 假設只有登入成功之後才能使用 quizService.update
	@PostMapping("quiz/update")
	public BasicRes update(@Valid @RequestBody QuizUpdateReq req, HttpSession session) throws Exception {
		BasicRes checkRes = checkLogin(session);
		// 若 checkRes != null，表示檢查不通過，直接將錯誤訊息返回
		if(checkRes != null) {
			return checkRes;
		}
		return quizService.update(req);
	}

	@GetMapping("quiz/getAll")
	public SearchRes getAllQuizs() {
		return quizService.getAllQuizs();
	}

	// API 的路徑: http://localhost:8080/quiz/get_questions?quizId=1
	// ?後面的 quizId 必須要和 @RequestParam 括號中的字串一樣
	@PostMapping("quiz/get_questions")
	public QuestionRes getQuizsByQuizId(@RequestParam("quizId") int quizId) {
		return quizService.getQuizsByQuizId(quizId);
	}

	@PostMapping("quiz/search")
	public SearchRes search(@RequestBody SearchReq req) {
		return quizService.search(req);
	}

	/**
	 * 1. API 的路徑: http://localhost:8080/quiz/get_questions?quizId=1 </br>
	 * 1.1. ?後面的 quizId 必須要和 @RequestParam 括號中的字串一樣</br>
	 * 2. 當方法中沒有使用 @RequestBody 時，http method 可以使用 @GetMapping，當然也可以使用 @PostMapping
	 */
	@GetMapping("quiz/delete")
	public BasicRes delete(@RequestParam("quizId") int quizId) throws Exception {
		return quizService.delete(quizId);
	}

	/**
	 * 1. 有2個相同的 http method 以及 API URL 路徑，專案無法啟動(Ambiguous mapping)</br>
	 * 1.1. 例如: 有2個 API 具有相同的 http method (@PostMapping) 和 URL 路徑("quiz/delete")
	 * 
	 */
	@PostMapping("quiz/delete")
	public BasicRes delete(@Valid @RequestBody DeleteReq req) throws Exception {
		return quizService.delete(req);
	}

	@PostMapping("quiz/fillin")
	public BasicRes fillin(@Valid @RequestBody FillinReq req) throws Exception {
		return quizService.fillin(req);
	}

	@PostMapping("quiz/get_feedback_user_list")
	public FeedbackUserRes feedbackUserList(@RequestParam("quizId") int quizId) {
		return quizService.feedbackUserList(quizId);
	}

	@PostMapping("quiz/feedback")
	public FeedbackRes feedback(@RequestParam("quizId") int quizId, //
			@RequestParam("email") String email) {
		return quizService.feedback(quizId, email);
	}

	@PostMapping("quiz/statistics")
	public StatisticsRes statistics(@RequestParam("quizId") int quizId) {
		return quizService.statistics(quizId);
	}

}
