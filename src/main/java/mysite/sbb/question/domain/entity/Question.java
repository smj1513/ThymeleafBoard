package mysite.sbb.question.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import mysite.sbb.answer.domain.entity.Answer;
import mysite.sbb.user.domain.entity.SiteUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Builder
@Setter
@Getter
@AllArgsConstructor
@Entity
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 200)
	private String subject;

	@Column(columnDefinition = "TEXT")
	private String content;

	private LocalDateTime createDate;

	@ManyToOne // 한명의 사용자가 여러 개의 질문을 작성할 수 있음
	private SiteUser author;

	@ManyToMany
	private Set<SiteUser> recommender;

	@ManyToMany
	private Set<SiteUser> notRecommender;

	private LocalDateTime modifyDate;

	@Builder.Default
	@OneToMany(mappedBy = "question" , cascade = CascadeType.REMOVE)
	private List<Answer> answerList = new ArrayList<>();

}
