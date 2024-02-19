package mysite.sbb.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mysite.sbb.answer.domain.dto.AnswerDTO;
import mysite.sbb.question.domain.dto.QuestionFormDTO;
import mysite.sbb.question.domain.entity.Question;
import mysite.sbb.question.service.QuestionService;
import mysite.sbb.user.domain.entity.SiteUser;
import mysite.sbb.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

	private final QuestionService questionService;
	private final UserService userService;

	@RequestMapping
	public String questionMain() {
		return "redirect:/question/list";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String questionForm(QuestionFormDTO questionFormDTO) {
		return "question_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String questionCreate(@ModelAttribute @Valid QuestionFormDTO questionFormDTO, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		SiteUser user = userService.getUser(principal.getName());
		questionService.saveQuestion(questionFormDTO.getSubject(), questionFormDTO.getContent(), user);
		return "redirect:/question/list";
	}

	@GetMapping("/list")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Question> questionList = questionService.getList(page, kw);
		model.addAttribute("paging", questionList);
		model.addAttribute("kw", kw);
		return "question_list";
	}

	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable Long id) {
		Question question = questionService.getQuestion(id);
		model.addAttribute("question", question);
		model.addAttribute("answerDTO", new AnswerDTO());
		return "question_detail";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionFormDTO questionFormDTO, @PathVariable Long id, Principal principal) {
		Question question = questionService.getQuestion(id);
		if (question.getAuthor().getUserId().equals(principal.getName())) {
			questionFormDTO.setSubject(question.getSubject());
			questionFormDTO.setContent(question.getContent());
			return "question_form";
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify(@Valid QuestionFormDTO questionFormDTO, BindingResult bindingResult, Principal principal, @PathVariable Long id) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}

		Question question = questionService.getQuestion(id);
		if (!question.getAuthor().getUserId().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		this.questionService.modify(question, questionFormDTO.getSubject(), questionFormDTO.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(Principal principal, @PathVariable("id") Long id) {
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUserId().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.questionService.delete(question);
		return "redirect:/";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/recommend/{id}/{isRecommend}")
	public String questionRecommend(Principal principal, @PathVariable Long id, @PathVariable Boolean isRecommend) {

		Question question = questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());

		if (isRecommend) {
			questionService.recommend(question, siteUser);
		} else {
			questionService.notRecommend(question, siteUser);
		}
		return String.format("redirect:/question/detail/%s", id);
	}


}
