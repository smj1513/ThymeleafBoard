package mysite.sbb.question.service;

import lombok.RequiredArgsConstructor;
import mysite.sbb.answer.Repository.AnswerRepository;
import mysite.sbb.answer.domain.entity.Answer;
import mysite.sbb.exception.DataNotFoundException;
import mysite.sbb.question.Repository.QuestionRepository;
import mysite.sbb.question.domain.entity.Question;
import mysite.sbb.user.domain.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;

	public Page<Question> getList(int page, String keyword) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return questionRepository.findAllByKeyword(keyword,pageable);
	}

	public Question getQuestion(Long id) {
		Optional<Question> question = questionRepository.findById(id);
		if (question.isPresent()) {
			return question.get();
		} else {
			throw new DataNotFoundException("question not fount");
		}
	}

	public void modify(Question question,  String subject, String content){
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		questionRepository.save(question);
	}

	public List<Answer> getAnswerList(Long questionId){
		return answerRepository.findByQuestionId(questionId);
	}

	public Question saveQuestion(String subject, String content, SiteUser siteUser) {
		Question question = Question.builder().subject(subject).content(content).author(siteUser).createDate(LocalDateTime.now()).build();
		return questionRepository.save(question);
	}

	public void delete(Question question){
		questionRepository.delete(question);
	}

	public void recommend(Question question, SiteUser recommender){
		question.getRecommender().add(recommender);
		questionRepository.save(question);
	}

	public void notRecommend(Question question, SiteUser notRecommender){
		question.getNotRecommender().add(notRecommender);
		questionRepository.save(question);
	}
}
