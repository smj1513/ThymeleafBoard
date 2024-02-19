package mysite.sbb.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mysite.sbb.user.domain.Enum.UserRole;
import mysite.sbb.user.domain.entity.SiteUser;
import mysite.sbb.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserSecurityService implements UserDetailsService { //스프링 시큐리티에서 로그인시 사용하는 클래스

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Optional<SiteUser> _siteUser = userRepository.findByUserId(userId);
		if (_siteUser.isPresent()) {
			SiteUser siteUser = _siteUser.get();
			List<GrantedAuthority> authorities = new ArrayList<>();
			if ("admin".equals(userId)) {
				authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
			} else {
				authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
			}
			return new User(siteUser.getUserId(), siteUser.getPassword(), authorities);
		} else {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
	}
}
