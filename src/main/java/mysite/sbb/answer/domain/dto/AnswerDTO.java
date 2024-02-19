package mysite.sbb.answer.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDTO {
	@NotEmpty(message = "내용은 필수 항목입니다.")
	private String content;
}
