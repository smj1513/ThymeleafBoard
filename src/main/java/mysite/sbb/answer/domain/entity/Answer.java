package mysite.sbb.answer.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import mysite.sbb.question.domain.entity.Question;
import mysite.sbb.user.domain.entity.SiteUser;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String content;

	private LocalDateTime createDate;

	@ManyToOne
	private SiteUser author;

	@ManyToOne(optional = false) // Answer to Question 질문하나에 여러개의 답변.
	private Question question;

	@ManyToMany
	private Set<SiteUser> recommender;

	@ManyToMany
	private Set<SiteUser> notRecommender;

	private LocalDateTime modifyDate;

}
