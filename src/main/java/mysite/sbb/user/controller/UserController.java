package mysite.sbb.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mysite.sbb.user.domain.dto.UserDTO;
import mysite.sbb.user.service.UserSecurityService;
import mysite.sbb.user.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@Log4j2
@RequestMapping("/user")
public class UserController {
	private final UserService userService;
	private final UserSecurityService securityService;

	@GetMapping("/signup")
	public String signup(@ModelAttribute UserDTO userDTO) {
		return "signup_form";
	}

	@PostMapping("/signup")
	public String signup(@ModelAttribute @Valid UserDTO userDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup_form";
		}

		if (!userDTO.getPassword1().equals(userDTO.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 비밀번호 일치하지 않습니다.");
			return "signup_form";
		}

		try {
			userService.join(userDTO);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
			return "signup_form";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "signup_form";
		}
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "login_form";
	}

}
