package mysite.sbb.question.Repository;

import mysite.sbb.question.domain.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	Optional<Question> findBySubject(String subject);
	Optional<Question> findBySubjectAndContent(String subject, String content);

	List<Question> findByIdLessThan(Long id);

	List<Question> findBySubjectLike(String subject);
	Page<Question> findAll(Pageable pageable);
	Page<Question> findAll(Specification<Question> spec, Pageable pageable);

	@Query("""
		select distinct q
		from Question q left outer join SiteUser u1 on q.author=u1
			left outer join Answer a on a.question=q
			left outer join SiteUser u2 on a.author=u2
		where q.subject like %:kw%\s
			or q.content like %:kw%\s
			or u1.nickName like %:kw%\s
			or a.content like %:kw%\s
			or u2.nickName like %:kw%
		""")
	Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);


}
