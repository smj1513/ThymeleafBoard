package mysite.sbb.question.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionFormDTO {
	@NotEmpty(message = "제목은 필수 사항입니다.")
	@Size(max=200)
	private String subject;

	@NotEmpty(message = "내용은 필수 사항입니다.")
	private String content;
}
