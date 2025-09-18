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
 * @SpringBootTest: ���[�W��������ܦb������դ�k���e�A�|���Ұʾ�ӱM�סA�M�����M�פ�
 *                  �쥻���Q�U�ު�����إ߰_�ӡA�]���b���դ�k�ɻݭn�ϥΨ�Q�U�ު�����ɡA�i�H���`�Q�`�J�F
 *                  �Ϥ��n�D�`�J�S���Q�U�ު�����A�Ӫ���N�|�O null </br>
 *                  @TestInstance(TestInstance.Lifecycle.PER_CLASS):
 *                  �������O�����ϥΨ� @BeforeAll �άO
 * @AfterAll �ɭn�[������
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UserTest {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserService userService;

	// mockMvc �O��� WebApplicationContext �ҫإߪ�����A�i�Ψӽs�g web ���Ϊ���X����
	@Autowired
	private WebApplicationContext wac;

	// ��{�� http �ШD�������A�D�n�ΨӴ��� controller
	private MockMvc mockMvc;

	// ���w�洫��ƪ��榡�M�s�X
	private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	// �إ� MockMvc
	@BeforeAll
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	private int res;

	private ObjectMapper mapper = new ObjectMapper();

	// @BeforeEach: �b����C�� @Test ��k�� ""�e"" �|���榳�[�W����������k
	@BeforeEach
	public void addTestInfo() {
		// ���շs�W��T
		res = userDao.addInfo("C01", "0922345678", "c01@CCC", 45, "AA123456");
	}

	// @AfterEach: �b����C�� @Test ��k�� ""��"" �|���榳�[�W����������k
	@AfterEach
	public void deleteTestInfo() {
		userDao.deleteInfo("c01@CCC");
	}

	// @BeforeAll: �b��Ӵ��հ��椤�A�u�|����@���A����ɶ��I�O�b�Ҧ����դ�k���椧�e
	@BeforeAll
	public void beforeAll() {
		System.out.println("Before All~~~~~");
	}

	// @AfterAll: �b��Ӵ��հ��椤�A�u�|����@���A����ɶ��I�O�b�Ҧ����դ�k��������
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
			// ���շs�W��T
//			int res = userDao.addInfo("C01", "0922345678", "c01@CCC", 45, "AA123456");

			// ���ո�Ƥw�b�C�Ӵ��դ�k���e�w�s�W
			// �T�{ res �O�_���� 1�A�᭱���T����ܫe�����P�_�������߮ɪ�^���T��
			Assert.isTrue(res == 1, "addInfo failed!!");

			// ���ո�Ƥw�b�C�Ӵ��դ�k����R��

			// �̫�|�N�s�W�����ո�ƧR��
//			userDao.deleteInfo("c01@CCC");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test
	public void getCountByEmailDaoTest() {

		// ���շs�W��T: �o�䤣�ݭn�Τ@���ܼƱN���G���^�ӡA�]���e���w�g�����չL addInfo �OOK��
//		addTestInfo();
//		userDao.addInfo("C01", "0922345678", "c01@CCC", 45, "AA123456");

		int res = userDao.getCountByEmail("c01@CCC");
		// �T�{ res �O�_���� 1�A�᭱���T����ܫe�����P�_�������߮ɪ�^���T��
		Assert.isTrue(res == 1, "getCountByEmail failed!!");

		// �̫�|�N�s�W�����ո�ƧR��
//		userDao.deleteInfo("c01@CCC");
	}

	@Test
	public void getByEmailDaoTest() {
		// ���շs�W��T:
//		userDao.addInfo("C01", "0922345678", "c01@CCC", 45, "AA123456");

		User res = userDao.getByEmail("c01@CCC");
		Assert.isTrue(res != null, "getByEmail failed!!");

		// �̫�|�N�s�W�����ո�ƧR��
//		userDao.deleteInfo("c01@CCC");
	}

	@Test
	public void addInfoServiceTest() {
		// �ھ� addInfo �� if ���󦡤@�@�ˬd
		// 1. �ˬd���ƪ� email �|�o�� EMAIL_EXISTS �����~�T��
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
		// 2. �s�W���T����T: ���s�ᤩ email �s����
		req.setEmail("c02@CCC");
		response = userService.addInfo(req);
		Assert.isTrue(response.getMessage().equalsIgnoreCase( //
				ResCodeMessage.SUCCESS.getMessage()), //
				"Add info error!!");
		// 3. �]���O���շs�W��ơA�ҥH�n��s�W�����ո�ƧR��
		userDao.deleteInfo("c02@CCC");
	}

	@Test
	public void addInfoServiceControllerTest() {
		// �إ� http headers
		HttpHeaders headers = new HttpHeaders();
		// �]�w�椬���e���榡�M�s�X
		headers.setContentType(MediaType.APPLICATION_JSON);
		try {
			// �ˬd request body �� name
			callAPIAndCheckResponse("", "0922345678", "c02@CCC", 60, "AA123456", //
					ConstantsMessage.NAME_ERROR);
			// �ˬd request body �� phone
			callAPIAndCheckResponse("C02", "", "c02@CCC", 60, "AA123456", //
					ConstantsMessage.PHONE_FORMAT_ERROR);
			// �ˬd request body �� email
			callAPIAndCheckResponse("C02", "0922345678", "", 60, "AA123456", //
					ConstantsMessage.EMAIL_ERROR);
			// �ˬd request body �� age
			callAPIAndCheckResponse("C02", "0922345678", "c02@CCC", -60, "AA123456", //
					ConstantsMessage.AGE_ERROR);
			// �ˬd request body �� password
			callAPIAndCheckResponse("C02", "0922345678", "c02@CCC", 60, "", //
					ConstantsMessage.PASSWORD_ERROR);
			// ���\�I�s
			callAPIAndCheckResponse("C02", "0922345678", "c02@CCC", 60, "AA123456", //
					ResCodeMessage.SUCCESS.getMessage());
			// �R���s�W�����
			userDao.deleteInfo("c02@CCC");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String setReqBody(String name, String phone, String email, int age, //
			String password) throws Exception {
		// Map �� key-value ����ƫ��A�A�D�n�O�ھ� Request Body �� JSON �өw�q
		// Request Body �� JSON �榡�Akey ����ƫ��A�N�|�O String�A
		// value �h�O�|���h�ظ�ƫ��A�AJava ���i�H�]�t���N��ƫ��A���N�O Object
		Map<String, Object> reqMap = new LinkedHashMap<>();
		reqMap.put("name", name);
		reqMap.put("phone", phone);
		reqMap.put("email", email);
		reqMap.put("age", age);
		reqMap.put("password", password);
		// �N reqMap �ন String
		try {
			String reqStr = mapper.writeValueAsString(reqMap);
			return reqStr;
		} catch (Exception e) {
			throw e;
		}
	}

	// ���F�ˬd�ѼơA�ҥH�� request body ���n��J���ȰѼƤ�
	private void callAPIAndCheckResponse(String name, String phone, String email, //
			int age, String password, String errorMessage) {
		try {
			// �ˬd request body �� �Ѽ�
			String reqStr = setReqBody(name, phone, email, age, password);
			// ���� controller �ШD
			ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/user/add_info")//
					.contentType(CONTENT_TYPE).content(reqStr));
			String responseStr = response.andReturn().getResponse().getContentAsString();
			
			Map<String, Object> resMap = mapper.readValue(responseStr, new TypeReference<>() {
			});
			
			// �쥻���o�� message �������Ȥ���ƫ��A�O Object�A�n�N��j���૬�� String
			String message = (String) resMap.get("message");
			Assert.isTrue(message.equalsIgnoreCase(errorMessage), errorMessage);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
