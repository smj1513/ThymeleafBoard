package mysite.sbb.user.controller;

import mysite.sbb.user.domain.entity.SiteUser;
import mysite.sbb.user.repository.UserRepository;
import mysite.sbb.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.security.auth.login.LoginException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserControllerTest {

	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository repository;

	@Test
	void login() throws LoginException {
		//given
		String id = "minj21";
		String pw = "mjmj0221!";
		String encode = passwordEncoder.encode(pw);
		//when
		Optional<SiteUser> byUserId = repository.findByUserId(id);
		SiteUser siteUser = byUserId.get();
		//then
		assertThat(siteUser.getPassword()).isEqualTo(encode);
	}
}