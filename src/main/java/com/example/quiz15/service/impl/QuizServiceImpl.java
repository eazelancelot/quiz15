package com.example.quiz15.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz15.constants.QuestionType;
import com.example.quiz15.constants.ResCodeMessage;
import com.example.quiz15.dao.QuestionDao;
import com.example.quiz15.dao.QuizDao;
import com.example.quiz15.entity.Question;
import com.example.quiz15.entity.Quiz;
import com.example.quiz15.service.ifs.QuizService;
import com.example.quiz15.vo.BasicRes;
import com.example.quiz15.vo.QuestionRes;
import com.example.quiz15.vo.QuestionVo;
import com.example.quiz15.vo.QuizCreateReq;
import com.example.quiz15.vo.QuizUpdateReq;
import com.example.quiz15.vo.SearchReq;
import com.example.quiz15.vo.SearchRes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuizServiceImpl implements QuizService {

	// ���� ���O(�� Json �榡)�P���󤧶����ഫ
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	/**
	 * @throws Exception
	 * @Transactional: �ư�</br>
	 *                 1. ��@�Ӥ�k������h�� Dao ��(���άO�P�@�i��g�h�����)�A�o�ǩҦ���������ӳ��n��P�@�����欰�A
	 *                 �ҥH�o�Ǹ�ƭn�������g�J���\�A���M�N�����g�J����</br>
	 *                 2. @Transactional ���Ħ^�Ҫ����`�w�]�O RunTimeException�A�Y�o�ͪ����`���O
	 *                 RunTimeException
	 *                 �Ψ�l���O�����`�����A��ƬҤ��|�^�ҡA�]���Q�n���u�n�o�ͥ���@�ز��`�ɸ�Ƴ��n�i�H�^�ҡA�i�H
	 *                 �N @Transactional �����Ľd��q RunTimeException ������ Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public BasicRes create(QuizCreateReq req) throws Exception {
		// �Ѽ��ˬd�w�z�L @Valid ���ҤF
		try {
			// �ˬd���: �ϥαư��k
			BasicRes checkRes = checkDate(req.getStartDate(), req.getEndDate());
			if (checkRes.getCode() != 200) { // ������ 200 ����ˬd�X�����~
				return checkRes;
			}
			// �s�W�ݨ�
			quizDao.insert(req.getName(), req.getDescription(), req.getStartDate(), //
					req.getEndDate(), req.isPublished());
			// �s�W���ݨ���A���o�ݨ��y����
			// ���M�]�� @Transactional �|���N��ƴ���(commit)�i��Ʈw�A����ڤWSQL�y�k�w�g���槹���A
			// �̵M�i�H���o��������
			int quizId = quizDao.getMaxQuizId();
			// �s�W���D
			// ���X�ݨ������Ҧ����D
			List<QuestionVo> questionVoList = req.getQuestionList();
			// �B�z�C�@�D���D
			for (QuestionVo vo : questionVoList) {
				// �ˬd�D�������P�ﶵ
				checkRes = checkQuestionType(vo);
				// �I�s��k checkQuestionType �o�쪺 res �Y�O null�A����ˬd���S���D�A
				// �]����k���ˬd��᳣̫�S���D�ɬO�^�� ���\
				if (checkRes.getCode() != 200) {
//					return checkRes;
					// �]���e���w�g����F quizDao.insert �F�A�ҥH�o��n�ߥX Exception
					// �~�|�� @Transactional �ͮ�
					throw new Exception(checkRes.getMessage());
				}
				// �]�� MySQL �S�� List ����Ʈ榡�A�ҥH�n�� options ��Ʈ榡 �q List<String> �ন String
				List<String> optionsList = vo.getOptions();
				String str = mapper.writeValueAsString(optionsList);
				// �n�O�o�]�w quizId
				questionDao.insert(quizId, vo.getQuestionId(), vo.getQuestion(), //
						vo.getType(), vo.isRequired(), str);
			}
			return new BasicRes(ResCodeMessage.SUCCESS.getCode(), //
					ResCodeMessage.SUCCESS.getMessage());
		} catch (Exception e) {
			// ���� return BasicRes �ӬO�n�N�o�ͪ����`�ߥX�h�A�o�� @Transaction �~�|�ͮ�
			throw e;
		}
	}

	private BasicRes checkQuestionType(QuestionVo vo) {
		// 1. �ˬd type �O�_�O�W�w������
		String type = vo.getType();
		// ���] �q vo ���X�� type ���ŦX�w�q��3���������䤤�@�ءA�N��^���~�T��
		if (!(type.equalsIgnoreCase(QuestionType.SINGLE.getType())//
				|| type.equalsIgnoreCase(QuestionType.MULTI.getType())//
				|| type.equalsIgnoreCase(QuestionType.TEXT.getType()))) {
			return new BasicRes(ResCodeMessage.QUESTION_TYPE_ERROR.getCode(), //
					ResCodeMessage.QUESTION_TYPE_ERROR.getMessage());
		}
		// 2. type �O���Φh�諸�ɭԡA�ﶵ(options)�ܤ֭n��2��
		// ���] type ������ TEXT --> �N��� type �O���Φh��
		if (!type.equalsIgnoreCase(QuestionType.TEXT.getType())) {
			// ���Φh��ɡA�ﶵ�ܤ֭n��2��
			if (vo.getOptions().size() < 2) {
				return new BasicRes(ResCodeMessage.OPTIONS_INSUFFICIENT.getCode(), //
						ResCodeMessage.OPTIONS_INSUFFICIENT.getMessage());
			}
		} else { // else --> type �O text --> �ﶵ���ӬO null �άO size = 0
			// ���] �ﶵ���O null �� �ﶵ�� List ������
			if (vo.getOptions() != null && vo.getOptions().size() > 0) {
				return new BasicRes(ResCodeMessage.TEXT_HAS_OPTIONS_ERROR.getCode(), //
						ResCodeMessage.TEXT_HAS_OPTIONS_ERROR.getMessage());
			}
		}
		return new BasicRes(ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage());
	}

	private BasicRes checkDate(LocalDate startDate, LocalDate endDate) {
		// 1. �}�l�������񵲧������ 2. �}�l���������e�Ыت������
		// �P�_��: ���] �}�l����񵲧������ �� �}�l������e����� --> �^���~�T��
		// LocalDate.now() --> ���o��e�����
		if (startDate.isAfter(endDate) //
				|| startDate.isBefore(LocalDate.now())) {
			return new BasicRes(ResCodeMessage.DATE_FORMAT_ERROR.getCode(), //
					ResCodeMessage.DATE_FORMAT_ERROR.getMessage());
		}
		return new BasicRes(ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BasicRes update(QuizUpdateReq req) throws Exception {
		// �Ѽ��ˬd�w�z�L @Valid ���ҤF

		// ��s�O��w�s�b�ݨ��i��ק�
		try {
			// 1. �ˬd quizId �O�_�s�b
			int quizId = req.getQuizId();
			int count = quizDao.getCountByQuizId(quizId);
			if (count != 1) {
				return new BasicRes(ResCodeMessage.NOT_FOUND.getCode(), //
						ResCodeMessage.NOT_FOUND.getMessage());
			}
			// 2. �ˬd���
			BasicRes checkRes = checkDate(req.getStartDate(), req.getEndDate());
			if (checkRes.getCode() != 200) { // ������ 200 ����ˬd�X�����~
				return checkRes;
			}
			// 3. ��s�ݨ�
			int updateRes = quizDao.update(quizId, req.getName(), req.getDescription(), //
					req.getStartDate(), req.getEndDate(), req.isPublished());
			if (updateRes != 1) { // ��ܸ�ƨS��s���\
				return new BasicRes(ResCodeMessage.QUIZ_UPDATE_FAILED.getCode(), //
						ResCodeMessage.QUIZ_UPDATE_FAILED.getMessage());
			}
			// 4. �R���P�@�i�ݨ����Ҧ����D
			questionDao.deleteByQuizId(quizId);
			// 5. �ˬd���D
			List<QuestionVo> questionVoList = req.getQuestionList();
			for (QuestionVo vo : questionVoList) {
				// �ˬd�D�������P�ﶵ
				checkRes = checkQuestionType(vo);
				// ��k���ˬd��᳣̫�S���D�ɬO�^�� ���\
				if (checkRes.getCode() != 200) {
					// �]���e���w�g����F quizDao.insert �F�A�ҥH�o��n�ߥX Exception
					// �~�|�� @Transactional �ͮ�
					throw new Exception(checkRes.getMessage());
				}
				// �]�� MySQL �S�� List ����Ʈ榡�A�ҥH�n�� options ��Ʈ榡 �q List<String> �ন String
				List<String> optionsList = vo.getOptions();
				String str = mapper.writeValueAsString(optionsList);
				// �n�O�o�]�w quizId
				questionDao.insert(quizId, vo.getQuestionId(), vo.getQuestion(), //
						vo.getType(), vo.isRequired(), str);
			}
		} catch (Exception e) {
			// ���� return BasicRes �ӬO�n�N�o�ͪ����`�ߥX�h�A�o�� @Transaction �~�|�ͮ�
			throw e;
		}
		return new BasicRes(ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage());
	}

	@Override
	public SearchRes getAllQuizs() {
		List<Quiz> list = quizDao.getAll();
		return new SearchRes(ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage(), list);
	}

	@Override
	public QuestionRes getQuizsByQuizId(int quizId) {
		if (quizId <= 0) {
			return new QuestionRes(ResCodeMessage.QUIZ_ID_ERROR.getCode(), //
					ResCodeMessage.QUIZ_ID_ERROR.getMessage());
		}
		List<QuestionVo> questionVoList = new ArrayList<>();
		List<Question> list = questionDao.getQuestionsByQuizId(quizId);
		// ��C�D�ﶵ����ƫ��A�q String �ഫ�� List<String>
		for (Question item : list) {
			String str = item.getOptions();
			try {
				List<String> optionList = mapper.readValue(str, new TypeReference<>() {
				});
				// �N�qDB���o���C�@�����(Question item) ���C�����ȩ�� QuestionVo ���A�H�K��^���ϥΪ�
				// Question �M QuestionVo ���t�O�b�� �ﶵ ����ƫ��A
				QuestionVo vo = new QuestionVo(item.getQuizId(), item.getQuestionId(), //
						item.getQuestion(), item.getType(), item.isRequired(), optionList);
				// ��C�� vo ��� questionVoList ��
				questionVoList.add(vo);
			} catch (Exception e) {
				// �o�䤣�g throw e �O�]������k���S���ϥ� @Transactional�A���v�T��^���G
				return new QuestionRes(ResCodeMessage.OPTIONS_TRANSFER_ERROR.getCode(), //
						ResCodeMessage.OPTIONS_TRANSFER_ERROR.getMessage());
			}
		}
		return new QuestionRes(ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage(), questionVoList);
	}

	@Override
	public SearchRes search(SearchReq req) {
		// �ഫ req ����
		// �Y quizName �O null�A�ন�Ŧr��
		String quizName = req.getQuizName();
		if (quizName == null) {
			quizName = "";
		} else { // �h�l���A���ݭn�g�A�����F�z�ѤU����3���B��l�Ӽg
			quizName = quizName;
		}
		// 3���B��l
		// �榡: �ܼƦW�� = ����P�_�� ? �P�_�����G�� true �ɭn�ᤩ���� : �P�_�����G�� false �ɭn�ᤩ����;
		quizName = quizName == null ? "" : quizName;
		// �W�����{���X�i�H�u�ΤU���@��Ө��o��
		String quizName1 = req.getQuizName() == null ? "" : req.getQuizName();
		// =========================================
		// �ഫ �}�l��� --> �Y�S�����}�l��� --> ���w�@�ӫܦ����ɶ�
		LocalDate startDate = req.getStartDate() == null ? LocalDate.of(1970, 1, 1) //
				: req.getStartDate();

		LocalDate endDate = req.getEndDate() == null ? LocalDate.of(2999, 12, 31) //
				: req.getEndDate();
		List<Quiz> list = new ArrayList<>();
		if(req.isPublished()) {
			list = quizDao.getAllPublished(quizName, startDate, endDate);
		} else {
			list = quizDao.getAll(quizName, startDate, endDate);
		}
		return new SearchRes(ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage(), list);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BasicRes delete(int quizId) throws Exception {
		if (quizId <= 0) {
			return new BasicRes(ResCodeMessage.QUIZ_ID_ERROR.getCode(), //
					ResCodeMessage.QUIZ_ID_ERROR.getMessage());
		}
		try {
			quizDao.deleteById(quizId);
			questionDao.deleteByQuizId(quizId);
		} catch (Exception e) {
			// ���� return BasicRes �ӬO�n�N�o�ͪ����`�ߥX�h�A�o�� @Transaction �~�|�ͮ�
			throw e;
		}
		return null;
	}

}
