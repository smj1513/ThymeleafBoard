package mysite.sbb.user.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String userId;

	@Column(unique = true)
	private String nickName;

	private String userName;

	private String password;

	private LocalDate birthDate;

	private String gender;

	@Column(unique = true)
	private String phoneNumber;

	@Column(unique = true)
	@Email
	private String email;
}
