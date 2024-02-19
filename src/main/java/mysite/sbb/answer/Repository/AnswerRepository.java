package mysite.sbb.answer.Repository;

import mysite.sbb.answer.domain.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

	List<Answer> findByQuestionId(Long questionId);
}
