package com.example.quiz15.vo;

import java.util.List;

import com.example.quiz15.constants.ConstantsMessage;

import jakarta.validation.constraints.NotEmpty;

public class DeleteReq {

	// 就只有確認 List size 不為空
	@NotEmpty(message = ConstantsMessage.QUIZ_ID_ERROR)
	private List<Integer> quizIdList;

	public List<Integer> getQuizIdList() {
		return quizIdList;
	}

	public void setQuizIdList(List<Integer> quizIdList) {
		this.quizIdList = quizIdList;
	}

}
