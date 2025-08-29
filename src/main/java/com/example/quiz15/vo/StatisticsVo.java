package com.example.quiz15.vo;

import java.util.List;

// 一個 StatisticsVo 表示 一題的所有選項統計
public class StatisticsVo {

	private int questionId;

	private String question;

	private String type;

	private boolean required;

	private List<OptionCountVo> optionCountVoList;

	public StatisticsVo() {
		super();
	}

	public StatisticsVo(int questionId, String question, String type, boolean required,
			List<OptionCountVo> optionCountVoList) {
		super();
		this.questionId = questionId;
		this.question = question;
		this.type = type;
		this.required = required;
		this.optionCountVoList = optionCountVoList;
	}

	public int getQuestionId() {
		return questionId;
	}

	public String getQuestion() {
		return question;
	}

	public String getType() {
		return type;
	}

	public boolean isRequired() {
		return required;
	}

	public List<OptionCountVo> getOptionCountVoList() {
		return optionCountVoList;
	}

}
