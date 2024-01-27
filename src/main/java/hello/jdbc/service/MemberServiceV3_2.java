package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

/***
 * 트랜잭션 - 트랜잭션 템플릿
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_2 {

//    private final PlatformTransactionManager transactionManager;
    private final TransactionTemplate tsTemplate;
    private final MemberRepositoryV3 repository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 repository) {
        this.tsTemplate = new TransactionTemplate(transactionManager);
        this.repository = repository;
    }

    public void accountTransfer(String fromId, String toId, int money) {

        tsTemplate.executeWithoutResult((status) -> {
            //비즈니스 로직
            try {
                bizLogic(fromId, toId, money);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = repository.findById(fromId);
        Member toMember = repository.findById(toId);

        repository.update(fromId, fromMember.getMoney() - money);
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
        repository.update(toId, toMember.getMoney() + money);
    }
}
