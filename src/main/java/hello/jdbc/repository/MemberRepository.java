package hello.jdbc.repository;

import hello.jdbc.domain.Member;

import java.sql.SQLException;
import java.util.List;

public interface MemberRepository {
    Member save(Member member);
    Member findById(String memberId);
    void update(String memberId, int money);
    void delete(String memberId);
    void deleteAll();

    List<Member> findAll();
}
