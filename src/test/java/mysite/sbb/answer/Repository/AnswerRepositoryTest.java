package mysite.sbb.answer.Repository;

import mysite.sbb.answer.domain.entity.Answer;
import mysite.sbb.question.Repository.QuestionRepository;
import mysite.sbb.question.domain.entity.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AnswerRepositoryTest {
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private QuestionRepository questionRepository;

	@AfterEach
	@BeforeEach
	void clear(){
		answerRepository.deleteAll();
		questionRepository.deleteAll();
	}


	@Transactional //
	@Test
	void findById() {
		//given
		Question question = Question.builder().subject("subject1").content("답변은 자동으로 생성되나요?").createDate(LocalDateTime.now()).build();
		questionRepository.save(question);
		Optional<Question> q1 = questionRepository.findById(1L);
		Answer answer = Answer.builder().content("네. 자동으로 생성됩니다.").question(question).createDate(LocalDateTime.now()).build();
		answerRepository.save(answer);

		//when
		Optional<Answer> ans = answerRepository.findById(1L);
		assertTrue(ans.isPresent());
		Answer a = ans.get();

		//then
		System.out.println("question.getContent() = " + question.getContent());
		System.out.println("a = " + a.getContent());
		assertThat(a.getQuestion().getId()).isEqualTo(question.getId());
	}

	//	@Transactional// 테스트코드에서 메서드 단위로 DB세션을 유지시킨다.
	// 대표적으로 LazyInitializeException이 발생하는 코드. 안티 패턴인 fetch = FetchType.EAGER으로 설정하여 해결했음.
	@Test
	void findAnsList() {
		//given
		Question question1 = Question.builder().subject("Q1").content("q1").createDate(LocalDateTime.now()).build();
		Question question2 = Question.builder().subject("Q2").content("q2").createDate(LocalDateTime.now()).build();
		Question question3 = Question.builder().subject("Q3").content("q3").createDate(LocalDateTime.now()).build();
		questionRepository.save(question1);
		questionRepository.save(question2);
		questionRepository.save(question3);
		questionRepository.flush();

		Answer answer1 = Answer.builder().content("q1-a1").createDate(LocalDateTime.now()).question(question1).build();
		Answer answer2 = Answer.builder().content("q1-a2").createDate(LocalDateTime.now()).question(question1).build();
		Answer answer3 = Answer.builder().content("q2-a1").createDate(LocalDateTime.now()).question(question2).build();
		answerRepository.save(answer1);
		answerRepository.save(answer2);
		answerRepository.save(answer3);
		answerRepository.flush();

		//when
		Optional<Question> q1 = questionRepository.findById(1L); //이 시점에서 세션 종료
		Optional<Question> q2 = questionRepository.findById(2L);
		Optional<Question> q3 = questionRepository.findById(3L);
		assertTrue(q1.isPresent());
		assertTrue(q2.isPresent());
		assertTrue(q3.isPresent());

		Question qu1 = q1.get();
		Question qu2 = q2.get();
		Question qu3 = q3.get();
		List<Answer> answerList = qu1.getAnswerList();
		List<Answer> answerList1 = qu2.getAnswerList();
		List<Answer> answerList2 = qu3.getAnswerList();

		//then
		assertThat(answerList.size()).isEqualTo(2);
		assertThat(answerList1.size()).isEqualTo(1);
		assertThat(answerList2.size()).isEqualTo(0);
	}


}