package com.example.quiz15;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.quiz15.dao.FillinDao;
import com.example.quiz15.dao.QuizDao;
import com.example.quiz15.entity.Quiz;
import com.example.quiz15.service.ifs.QuizService;
import com.example.quiz15.vo.QuestionAnswerDto;
import com.example.quiz15.vo.QuestionVo;
import com.example.quiz15.vo.QuizCreateReq;
import com.example.quiz15.vo.QuizUpdateReq;
import com.example.quiz15.vo.UserVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
class Quiz15ApplicationTests {

	@Autowired
	private QuizService quizService;

	@Autowired
	private FillinDao fillinDao;
	
	@Autowired
	private QuizDao quizDao;
	
	@Test
	public void findTest() {
		List<Quiz> list = quizDao.findByName("早餐大調查");
		System.out.println(list.size());
	}

	@Test
	void contextLoads() {
		ObjectMapper mapper = new ObjectMapper();
		List<String> list = List.of("A", "B", "C");
		try {
			String mapperStr = mapper.writeValueAsString(list);
			System.out.println(mapperStr);
			String toStr = list.toString();
			System.out.println(toStr);
			System.out.println("=================");
			List<String> oList = mapper.readValue(mapperStr, new TypeReference<>() {
			});
			System.out.println(oList);
//			List<String> oList2 = mapper.readValue(toStr, new TypeReference<>() {
//			});
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void quizCreateTest() throws Exception {
		QuestionVo vo1 = new QuestionVo(1, "早餐吃啥123", "單選", true, List.of("蛋餅", "漢堡", "三明治", "飯糰"));
		QuestionVo vo2 = new QuestionVo(1, "早餐喝啥123", "單選", true, List.of("奶茶", "紅茶", "豆漿", "米漿"));
		QuizCreateReq req = new QuizCreateReq("早餐大調查", "早餐大調查", LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 31),
				true, List.of(vo1, vo2));
		quizService.create(req);
	}

	@Test
	public void test() {
		List<String> list = List.of("蛋餅", "漢堡", "三明治", "飯糰");
		ObjectMapper mapper = new ObjectMapper();
		String listStr;
		try {
			listStr = mapper.writeValueAsString(list);
			String strr = "[\"蛋餅\",\"漢堡\",\"三明治\",\"飯糰\"]";
			System.out.println(listStr);
			System.out.println(strr);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String str = "[\"蛋餅\",\"漢堡\",\"三明治\",\"飯糰\"]";
		System.out.println(str.contains("三明治"));
		System.out.println(str.contains("蛋餅漢堡"));
	}

	@Test
	public void test1() {
		List<UserVo> list = fillinDao.selectUserVoList(1);
		System.out.println(list.size());
	}

	@Test
	public void test2() {
		List<QuestionAnswerDto> list = fillinDao.selectQuestionAnswerList(1, "A01@aa");
		System.out.println(list.size());
		list = fillinDao.selectQuestionAnswerList(1, "B01@bb");
		System.out.println(list.size());
	}

	@Test
	public void test3() {
		List<QuestionAnswerDto> list = fillinDao.selectQuestionAnswerList(1);
		System.out.println(list.size());
		list = fillinDao.selectQuestionAnswerList(2);
		System.out.println(list.size());
	}

	@Test
	public void test4() {
		List<String> answerList = new ArrayList<>(List.of("蛋餅", "漢堡", "三明治", "飯糰", //
				"蛋餅", "漢堡", "三明治", "飯糰", "漢堡", "三明治","蛋餅"));
		int size = answerList.size();
		System.out.println("size: " + size);
		answerList.removeAll(List.of("蛋餅"));
		int newSize = answerList.size();
		System.out.println("newSize: " + newSize);
		System.out.println("count: " + (size - newSize));
		
	}
	
	@Test
	public void quizUpdateTest() {
		QuestionVo vo1 = new QuestionVo(1, "早餐吃啥456", "Single", true, List.of("蛋餅", "漢堡", "三明治", "飯糰"));
		QuizUpdateReq req = new QuizUpdateReq("早餐大調查", "早餐大調查", LocalDate.of(2025, 9, 1), LocalDate.of(2025, 10, 31),
				true, List.of(vo1));
		req.setQuizId(9);
		try {
			quizService.update(req);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getQuizIdInTest() {
		List<Integer> idList = new ArrayList<>(List.of(1, 2, 3, 4, 7, 8));
		List<Quiz> list = quizDao.getAllByIdIn(idList);
		System.out.println(list.size());
	}

}
