package mysite.sbb;

import mysite.sbb.question.service.QuestionService;
import mysite.sbb.user.domain.entity.SiteUser;
import mysite.sbb.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Test
	void contextLoads() {
		String pw = passwordEncoder.encode("admin");
		SiteUser siteUser = SiteUser.builder().nickName("완장").userId("admin").userName("관리자").email("minj21@kumoh.ac.kr").phoneNumber("010-1234-5678").gender("male").birthDate(LocalDate.now()).password(pw).build();
		userRepository.save(siteUser);
		for (int i = 0; i < 300; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]",i);
			String content = "테스트";
			this.questionService.saveQuestion(subject,content, siteUser);
		}
	}

}
