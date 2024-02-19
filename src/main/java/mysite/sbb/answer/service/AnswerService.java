package mysite.sbb.answer.service;

import lombok.RequiredArgsConstructor;
import mysite.sbb.answer.Repository.AnswerRepository;
import mysite.sbb.answer.domain.entity.Answer;
import mysite.sbb.exception.DataNotFoundException;
import mysite.sbb.question.domain.entity.Question;
import mysite.sbb.user.domain.entity.SiteUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {
	private final AnswerRepository answerRepository;

	public Answer getAnswer(Long id) {
		Optional<Answer> ans = answerRepository.findById(id);
		if (ans.isPresent()) {
			return ans.get();
		}else{
			throw new DataNotFoundException("answer not found");
		}
	}

	public Answer save(Answer answer) {
		return answerRepository.save(answer);
	}

	public Answer create(Question question, String content, SiteUser siteUser) {
		Answer answer = Answer.builder().
				createDate(LocalDateTime.now()).
				question(question).
				author(siteUser).
				content(content).build();
		return answerRepository.save(answer);
	}

	public Answer modify(Answer answer, String content){
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		return this.answerRepository.save(answer);
	}

	public void delete(Answer answer){
		this.answerRepository.delete(answer);
	}

	public void recommend(Answer answer, SiteUser recommender){
		answer.getRecommender().add(recommender);
		answerRepository.save(answer);
	}

	public void notRecommend(Answer answer, SiteUser notRecommender){
		answer.getNotRecommender().add(notRecommender);
		answerRepository.save(answer);
	}
}
