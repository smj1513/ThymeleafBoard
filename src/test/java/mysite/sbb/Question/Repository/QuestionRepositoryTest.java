package mysite.sbb.Question.Repository;

import lombok.extern.log4j.Log4j2;
import mysite.sbb.question.Repository.QuestionRepository;
import mysite.sbb.question.domain.entity.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@SpringBootTest
class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository repository;

	@AfterEach
	void repositoryClear() {
		repository.deleteAll();
	}

	@Test
	void testJpa() {
		//given
		Question q1 = Question.builder().
				subject("sbb가 무엇인가요?").
				content("sbb에 대해서 알고 싶습니다.").
				createDate(LocalDateTime.now()).build();
		repository.save(q1);

		Question q2 = Question.builder().
				subject("스프링부트 모델 질문 입니다.").
				content("id는 자동으로 생성되나요?").
				createDate(LocalDateTime.now()).build();
		repository.save(q2);

		//when
		List<Question> all = repository.findAll();

		//then
		assertThat(all.size()).isEqualTo(2);
	}

	@Test
	void findById() {
		//given
		Question q1 = Question.builder().
				subject("jpa").
				content("content").
				createDate(LocalDateTime.now()).
				build();
		repository.save(q1);

		//when
		Optional<Question> testq1 = repository.findById(q1.getId());

		//then
		assertThat(testq1).isPresent().map(Question::getId).hasValue(q1.getId());
	}

	@Test
	void findBySubject() {
		//given
		Question q = Question.builder().subject("test").content("jpatest").createDate(LocalDateTime.now()).build();
		repository.save(q);
		//when
		Optional<Question> test = repository.findBySubject("test");

		//then
		assertThat(test.orElseThrow().getSubject()).isEqualTo("test");
	}

	@Test
	void findBySubjectAndContent() {
		//given
		Question question = Question.builder().subject("test").content("testJpa").createDate(LocalDateTime.now()).build();
		repository.save(question);
		//when
		Optional<Question> bySubjectAndContent = repository.findBySubjectAndContent("test", "testJpa");
		Question result = null;
		if (bySubjectAndContent.isPresent()) {
			result = bySubjectAndContent.get();
		}

		//then
		assertThat(result.getId()).isEqualTo(question.getId());


	}

	@Test
	void findByIdLessThan() {
		//given
		Question question1 = Question.builder().subject("test1").content("test1").createDate(LocalDateTime.now()).build();
		Question question2 = Question.builder().subject("test2").content("test2").createDate(LocalDateTime.now()).build();
		repository.save(question1);
		repository.save(question2);

		//when
		List<Question> byIdLessThan = repository.findByIdLessThan(Long.MAX_VALUE);
		byIdLessThan.forEach(question -> System.out.println("question.getId() = " + question.getId()));
		//then
		assertThat(byIdLessThan.size()).isEqualTo(2);

	}

	@Test
	void findBySubjectLike() {
		//given
		Question question1 = Question.builder().content("test1").subject("test1").createDate(LocalDateTime.now()).build();
		Question question2 = Question.builder().content("test2").subject("test2").createDate(LocalDateTime.now()).build();

		repository.save(question1);
		repository.save(question2);
		//when
		List<Question> questionList = repository.findBySubjectLike("%test%");

		questionList.forEach(question -> System.out.println("question = " + question));
		//then
		questionList.forEach(question -> System.out.println("question = " + question.getId()));
		assertThat(questionList.size()).isEqualTo(2);
	}

	@Test
	void updateQuestion() {
		//given
		Question question1 = Question.builder().content("test1").subject("test1").createDate(LocalDateTime.now()).build();
		repository.save(question1);
		Optional<Question> question = repository.findById(1L);
		//when
		question.ifPresent(q -> {
			q.setSubject("수정된 제목.");
			repository.save(q);
		});

		//then
		Optional<Question> getQ = repository.findById(1L);
		Question temp= null;
		if(getQ.isPresent()){
			temp = getQ.get();
			System.out.println("temp = " + temp.getSubject());
		}

		assertThat(temp.getSubject()).isEqualTo("수정된 제목.");
	}
}