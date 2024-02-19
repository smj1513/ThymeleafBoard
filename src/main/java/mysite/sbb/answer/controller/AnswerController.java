package mysite.sbb.answer.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mysite.sbb.answer.domain.dto.AnswerDTO;
import mysite.sbb.answer.domain.entity.Answer;
import mysite.sbb.answer.service.AnswerService;
import mysite.sbb.question.domain.entity.Question;
import mysite.sbb.question.service.QuestionService;
import mysite.sbb.user.domain.entity.SiteUser;
import mysite.sbb.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/answer")
@Controller
public class AnswerController {

	private final AnswerService answerService;
	private final QuestionService questionService;
	private final UserService userService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Long id, @ModelAttribute @Valid AnswerDTO answerDTO, BindingResult bindingResult, Principal principal) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		if (bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";
		}
		Answer answer = this.answerService.create(question, answerDTO.getContent(), siteUser);
		return String.format("redirect:/question/detail/%s#answer_%s", id, answer.getId());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String modify(@ModelAttribute AnswerDTO answerDTO, @PathVariable Long id, Principal principal) {
		Answer answer = answerService.getAnswer(id);
		if (!answer.getAuthor().getUserId().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		answerDTO.setContent(answer.getContent());
		return "answer_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify(@Valid AnswerDTO answerDTO, BindingResult bindingResult, @PathVariable Long id, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "answer_form";
		}
		Answer answer = answerService.getAnswer(id);
		if (!answer.getAuthor().getUserId().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		this.answerService.modify(answer, answerDTO.getContent());
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String delete(Principal principal, @PathVariable Long id) {
		Answer answer = answerService.getAnswer(id);
		if (!answer.getAuthor().getUserId().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		answerService.delete(answer);
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/recommend/{id}/{isRecommend}")
	public String answerRecommend(Principal principal, @PathVariable Long id, @PathVariable Boolean isRecommend) {
		Answer answer = answerService.getAnswer(id);
		SiteUser user = userService.getUser(principal.getName());
		if (isRecommend) {
			answerService.recommend(answer, user);
		} else {
			answerService.notRecommend(answer, user);
		}
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}

}

