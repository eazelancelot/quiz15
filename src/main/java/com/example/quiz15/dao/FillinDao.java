package com.example.quiz15.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz15.entity.Fillin;
import com.example.quiz15.entity.FillinId;
import com.example.quiz15.vo.QuestionAnswerDto;
import com.example.quiz15.vo.UserVo;

@Repository
public interface FillinDao extends JpaRepository<Fillin, FillinId>{
	
	@Modifying
	@Transactional
	@Query(value = "insert into fillin (quiz_id, question_id, email, " //
			+ " answer, fillin_date) values (?1, ?2, ?3, ?4, ?5)", //
			nativeQuery = true)
	public void insert(int quizId, int questionId, String email, String answer, LocalDate now);

	@Query(value = "select count(email) from fillin where quiz_id = ?1 and email = ?2", //
			nativeQuery = true)
	public int selectCountByQuizIdAndEmail(int quizId, String email);
	
	/**
	 * 1. �� select �����L�k�u�ίS�w���@�i��(�άO�˸���ƪ� Entity) �Ӹ˸���ƮɡAnativeQuery �n�ܦ� false </br>
	 * 2. nativeQuery = false �ɡASQL�y�k�� <br>
	 * 2.0. �L�k�� * �Ӫ�ܩҦ������A�u��@�@���g�X
	 * 2.1. select �����W�ٷ|�ܦ��U�� Entity class �����ݩ��ܼƦW�� <br>
	 * 2.2. on �᭱�����W�٬O Entity class �����ݩ��ܼƦW��<br>
	 * 2.3. ���W�ٷ|�ܦ� Entity class �W�� <br>
	 * 2.4. select �᭱�����n�z�L new �غc��k���覡�Ӷ�ȡAUserVo ���]�n���������غc��k<br>
	 * 2.5. UserVo �n���w���㪺���|: com.example.quiz15.vo.UserVo<br>
	 * 3. select distinct: �N select �X���Ҧ����ȡA�R�����ƪ����G�A�u�O�d�@�����<br>
	 * 4. U.�ܼƦW��: �y�k��U. �᭱���ܼƦW�١A�O�n�� User �o�� class ���ܼƦW�١A�ӫD UserVo �����ܼƦW�١A
	 *    �]���O from User as U 
	 */
	@Query(value = "select distinct new com.example.quiz15.vo.UserVo("//
			+ " U.name, U.email, U.phone, U.age, F.fillinDate) " //
			+ " from User as U " //
			+ " join Fillin as F on U.email = F.email where F.quizId = ?1", //
			nativeQuery = false)
	public List<UserVo> selectUserVoList(int quizId);
	
	/**
	 * �n�T�O�k���ǰt�����(null)�Q�e�{�A�n�N�k���L�o���󲾦� on ����
	 */
	@Query(value = "select new com.example.quiz15.vo.QuestionAnswerDto(" //
			+ " Qu.questionId, Qu.question, Qu.type, Qu.required, F.answer)" //
			+ " from Question as Qu left join Fillin as F "//
			+ " on Qu.questionId = F.questionId and F.quizId = ?1 and F.email = ?2"//
			+ " where Qu.quizId = ?1 ", //
			nativeQuery = false)
	public List<QuestionAnswerDto> selectQuestionAnswerList(int quizId, String email);	

	@Query(value = "select new com.example.quiz15.vo.QuestionAnswerDto(" //
			+ " Qu.questionId, Qu.question, Qu.type, Qu.required, F.answer)" //
			+ " from Question as Qu join Fillin as F "//
			+ " on Qu.questionId = F.questionId and F.quizId = ?1 "//
			+ " where Qu.quizId = ?1 ", //
			nativeQuery = false)
	public List<QuestionAnswerDto> selectQuestionAnswerList(int quizId);
}
