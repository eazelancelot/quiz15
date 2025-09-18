package com.example.quiz15;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import com.example.quiz15.constants.ConstantsMessage;
import com.example.quiz15.constants.ResCodeMessage;
import com.example.quiz15.dao.UserDao;
import com.example.quiz15.entity.User;
import com.example.quiz15.service.ifs.UserService;
import com.example.quiz15.vo.AddInfoReq;
import com.example.quiz15.vo.BasicRes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @SpringBootTest: 有加上此註釋表示在執行測試方法之前，會先啟動整個專案，然後讓專案中
 *                  原本有被託管的物件建立起來，因此在測試方法時需要使用到被託管的物件時，可以正常被注入；
 *                  反之要求注入沒有被託管的物件，該物件就會是 null </br>
 *                  @TestInstance(TestInstance.Lifecycle.PER_CLASS):
 *                  測試類別中有使用到 @BeforeAll 或是
 * @AfterAll 時要加的註釋
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UserTest {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserService userService;

	// mockMvc 是基於 WebApplicationContext 所建立的物件，可用來編寫 web 應用的整合測試
	@Autowired
	private WebApplicationContext wac;

	// 實現對 http 請求的模擬，主要用來測試 controller
	private MockMvc mockMvc;

	// 指定交換資料的格式和編碼
	private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	// 建立 MockMvc
	@BeforeAll
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	private int res;

	private ObjectMapper mapper = new ObjectMapper();

	// @BeforeEach: 在執行每個 @Test 方法之 ""前"" 會執行有加上此註釋的方法
	@BeforeEach
	public void addTestInfo() {
		// 測試新增資訊
		res = userDao.addInfo("C01", "0922345678", "c01@CCC", 45, "AA123456");
	}

	// @AfterEach: 在執行每個 @Test 方法之 ""後"" 會執行有加上此註釋的方法
	@AfterEach
	public void deleteTestInfo() {
		userDao.deleteInfo("c01@CCC");
	}

	// @BeforeAll: 在整個測試執行中，只會執行一次，執行時間點是在所有測試方法執行之前
	@BeforeAll
	public void beforeAll() {
		System.out.println("Before All~~~~~");
	}

	// @AfterAll: 在整個測試執行中，只會執行一次，執行時間點是在所有測試方法結束之後
	@AfterAll
	public void afterAll() {
		System.out.println("After All~~~~");
	}

	@BeforeEach
	public void beforeEach() {
		System.out.println("Before Each !!!!!!");
	}

	@AfterEach
	public void afterEach() {
		System.out.println("After Each !!!!!!");
	}

	@Test
	public void addInfoDaoTest() {
		try {
			// 測試新增資訊
//			int res = userDao.addInfo("C01", "0922345678", "c01@CCC", 45, "AA123456");

			// 測試資料已在每個測試方法之前已新增
			// 確認 res 是否等於 1，後面的訊息表示前面的判斷式不成立時返回的訊息
			Assert.isTrue(res == 1, "addInfo failed!!");

			// 測試資料已在每個測試方法之後刪除

			// 最後會將新增的測試資料刪除
//			userDao.deleteInfo("c01@CCC");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test
	public void getCountByEmailDaoTest() {

		// 測試新增資訊: 這邊不需要用一個變數將結果接回來，因為前面已經有測試過 addInfo 是OK的
//		addTestInfo();
//		userDao.addInfo("C01", "0922345678", "c01@CCC", 45, "AA123456");

		int res = userDao.getCountByEmail("c01@CCC");
		// 確認 res 是否等於 1，後面的訊息表示前面的判斷式不成立時返回的訊息
		Assert.isTrue(res == 1, "getCountByEmail failed!!");

		// 最後會將新增的測試資料刪除
//		userDao.deleteInfo("c01@CCC");
	}

	@Test
	public void getByEmailDaoTest() {
		// 測試新增資訊:
//		userDao.addInfo("C01", "0922345678", "c01@CCC", 45, "AA123456");

		User res = userDao.getByEmail("c01@CCC");
		Assert.isTrue(res != null, "getByEmail failed!!");

		// 最後會將新增的測試資料刪除
//		userDao.deleteInfo("c01@CCC");
	}

	@Test
	public void addInfoServiceTest() {
		// 根據 addInfo 的 if 條件式一一檢查
		// 1. 檢查重複的 email 會得到 EMAIL_EXISTS 的錯誤訊息
		AddInfoReq req = new AddInfoReq();
		req.setName("C02");
		req.setPhone("0922345678");
		req.setEmail("c01@CCC");
		req.setAge(36);
		req.setPassword("AA123456");
		BasicRes response = userService.addInfo(req);
		Assert.isTrue(response.getMessage().equalsIgnoreCase( //
				ResCodeMessage.EMAIL_EXISTS.getMessage()), //
				"Add info error!!");
		// 2. 新增正確的資訊: 重新賦予 email 新的值
		req.setEmail("c02@CCC");
		response = userService.addInfo(req);
		Assert.isTrue(response.getMessage().equalsIgnoreCase( //
				ResCodeMessage.SUCCESS.getMessage()), //
				"Add info error!!");
		// 3. 因為是測試新增資料，所以要把新增的測試資料刪除
		userDao.deleteInfo("c02@CCC");
	}

	@Test
	public void addInfoServiceControllerTest() {
		// 建立 http headers
		HttpHeaders headers = new HttpHeaders();
		// 設定交互內容的格式和編碼
		headers.setContentType(MediaType.APPLICATION_JSON);
		try {
			// 檢查 request body 的 name
			callAPIAndCheckResponse("", "0922345678", "c02@CCC", 60, "AA123456", //
					ConstantsMessage.NAME_ERROR);
			// 檢查 request body 的 phone
			callAPIAndCheckResponse("C02", "", "c02@CCC", 60, "AA123456", //
					ConstantsMessage.PHONE_FORMAT_ERROR);
			// 檢查 request body 的 email
			callAPIAndCheckResponse("C02", "0922345678", "", 60, "AA123456", //
					ConstantsMessage.EMAIL_ERROR);
			// 檢查 request body 的 age
			callAPIAndCheckResponse("C02", "0922345678", "c02@CCC", -60, "AA123456", //
					ConstantsMessage.AGE_ERROR);
			// 檢查 request body 的 password
			callAPIAndCheckResponse("C02", "0922345678", "c02@CCC", 60, "", //
					ConstantsMessage.PASSWORD_ERROR);
			// 成功呼叫
			callAPIAndCheckResponse("C02", "0922345678", "c02@CCC", 60, "AA123456", //
					ResCodeMessage.SUCCESS.getMessage());
			// 刪除新增的資料
			userDao.deleteInfo("c02@CCC");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String setReqBody(String name, String phone, String email, int age, //
			String password) throws Exception {
		// Map 的 key-value 的資料型態，主要是根據 Request Body 的 JSON 來定義
		// Request Body 的 JSON 格式，key 的資料型態就會是 String，
		// value 則是會有多種資料型態，Java 中可以包含任意資料型態的就是 Object
		Map<String, Object> reqMap = new LinkedHashMap<>();
		reqMap.put("name", name);
		reqMap.put("phone", phone);
		reqMap.put("email", email);
		reqMap.put("age", age);
		reqMap.put("password", password);
		// 將 reqMap 轉成 String
		try {
			String reqStr = mapper.writeValueAsString(reqMap);
			return reqStr;
		} catch (Exception e) {
			throw e;
		}
	}

	// 為了檢查參數，所以把 request body 中要輸入的值參數化
	private void callAPIAndCheckResponse(String name, String phone, String email, //
			int age, String password, String errorMessage) {
		try {
			// 檢查 request body 的 參數
			String reqStr = setReqBody(name, phone, email, age, password);
			// 模擬 controller 請求
			ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/user/add_info")//
					.contentType(CONTENT_TYPE).content(reqStr));
			String responseStr = response.andReturn().getResponse().getContentAsString();
			
			Map<String, Object> resMap = mapper.readValue(responseStr, new TypeReference<>() {
			});
			
			// 原本取得的 message 對應的值之資料型態是 Object，要將其強制轉型為 String
			String message = (String) resMap.get("message");
			Assert.isTrue(message.equalsIgnoreCase(errorMessage), errorMessage);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
