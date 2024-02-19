package mysite.sbb.user.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {
	@Size(min = 3, max = 25)
	@NotEmpty(message = "사용자 ID는 필수 항목입니다.")
	private String userId;

	@NotEmpty(message = "비밀번호는 필수 항목입니다.")
	private String password1;

	@NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
	private String password2;

	@NotEmpty(message = "사용자 이름은 필수 항목입니다.")
	private String userName;

	@NotEmpty(message = "닉네임은 필수 항목 입니다.")
	private String nickName;

	@NotNull(message = "생년 월일은 필수항목입니다.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	@NotEmpty(message = "성별은 필수 항목입니다.")
	private String gender;

	@NotEmpty(message = "전화번호는 필수 항목입니다.")
	@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "전화번호 형태가 올바르지않습니다.")
	private String phoneNumber;

	@NotEmpty(message = "이메일은 필수 항목입니다.")
	@Email
	private String email;
}
