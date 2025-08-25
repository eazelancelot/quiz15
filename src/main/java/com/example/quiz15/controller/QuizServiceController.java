package com.example.quiz15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz15.service.ifs.QuizService;
import com.example.quiz15.vo.BasicRes;
import com.example.quiz15.vo.QuestionRes;
import com.example.quiz15.vo.QuizCreateReq;
import com.example.quiz15.vo.QuizUpdateReq;
import com.example.quiz15.vo.SearchReq;
import com.example.quiz15.vo.SearchRes;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class QuizServiceController {
	
	@Autowired
	private QuizService quizService;
	
	@PostMapping("quiz/create")
	public BasicRes create(@Valid @RequestBody QuizCreateReq req) throws Exception {
		return quizService.create(req);
	}
	
	@PostMapping("quiz/update")
	public BasicRes update(@Valid @RequestBody QuizUpdateReq req) throws Exception {
		return quizService.update(req);
	}
	
	@GetMapping("quiz/getAll")
	public SearchRes getAllQuizs() {
		return quizService.getAllQuizs();
	}
	
	// API �����|: http://localhost:8080/quiz/get_questions?quizId=1
	// ?�᭱�� quizId �����n�M @RequestParam �A�������r��@��
	@PostMapping("quiz/get_questions")
	public QuestionRes getQuizsByQuizId(@RequestParam("quizId") int quizId) {
		return quizService.getQuizsByQuizId(quizId);
	}
	
	@PostMapping("quiz/search")
	public SearchRes search(@RequestBody SearchReq req) {
		return quizService.search(req);
	}
	
	// API �����|: http://localhost:8080/quiz/get_questions?quizId=1
	// ?�᭱�� quizId �����n�M @RequestParam �A�������r��@��
	@PostMapping("quiz/delete")
	public BasicRes delete(@RequestParam("quizId") int quizId) throws Exception {
		return quizService.delete(quizId);
	}

}
