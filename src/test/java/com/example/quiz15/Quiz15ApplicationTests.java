package com.example.quiz15;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.quiz15.service.ifs.QuizService;
import com.example.quiz15.vo.QuestionVo;
import com.example.quiz15.vo.QuizCreateReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class Quiz15ApplicationTests {
	
	@Autowired
	private QuizService quizService;

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
		QuestionVo vo1 = new QuestionVo(1, "���\�Yԣ123", "���", true, List.of("�J��", "�~��", "�T���v", "���{"));
		QuestionVo vo2= new QuestionVo(1, "���\��ԣ123", "���", true, List.of("����", "����", "����", "�̼�"));
		QuizCreateReq req = new QuizCreateReq("���\�j�լd", "���\�j�լd", LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 31), 
				true, List.of(vo1, vo2));
		quizService.create(req);
	}

}
