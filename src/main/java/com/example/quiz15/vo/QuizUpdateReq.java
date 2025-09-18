package com.example.quiz15.vo;

import java.time.LocalDate;
import java.util.List;

import com.example.quiz15.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

public class QuizUpdateReq extends QuizCreateReq {

	@Min(value = 1, message = ConstantsMessage.QUIZ_ID_ERROR)
	private int quizId;

	
	public QuizUpdateReq() {
		super();
	}

	public QuizUpdateReq(String name, String description, LocalDate startDate, LocalDate endDate, boolean published,
			List<QuestionVo> questionList) {
		super(name, description, startDate, endDate, published, questionList);
	}
	

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

}
