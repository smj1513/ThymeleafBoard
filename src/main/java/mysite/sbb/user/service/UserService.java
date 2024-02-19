package mysite.sbb.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mysite.sbb.exception.DataNotFoundException;
import mysite.sbb.user.domain.dto.UserDTO;
import mysite.sbb.user.domain.entity.SiteUser;
import mysite.sbb.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
@Log4j2
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public SiteUser join(UserDTO userDTO) {
		SiteUser siteUser = SiteUser.builder()
				.userId(userDTO.getUserId())
				.nickName(userDTO.getNickName())
				.userName(userDTO.getUserName())
				.email(userDTO.getEmail())
				.phoneNumber(userDTO.getPhoneNumber())
				.birthDate(userDTO.getBirthDate())
				.gender(userDTO.getGender())
				.password(passwordEncoder.encode(userDTO.getPassword1()))
				.build();
		return userRepository.save(siteUser);
	}

	public SiteUser getUser(String userId){
		Optional<SiteUser> siteUser = userRepository.findByUserId(userId);
		if(siteUser.isPresent()){
			return siteUser.get();
		}
		else{
			throw new DataNotFoundException("siteuser not found");
		}
	}

}
