package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);


            Member member = new Member();
            member.setUsername("회원1");
            member.setAge(10);
            member.setTeam(teamA);

            em.persist(member);

            Member member1 = new Member();
            member1.setUsername("회원2");
            member1.setAge(10);
            member1.setTeam(teamA);

            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원3");
            member2.setAge(10);
            member2.setTeam(teamB);

            em.persist(member2);

            // FLUSH 자동 호출
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("resultCount = " + resultCount);

            em.clear();
            Member find_member2 = em.find(Member.class, member2.getId());
            // DB 에는 age 가 20 이지만, 영속성 컨텍스트에는 10이다.
            // 그래서 벌크 연산 후, 영속성 컨텍스트를 초기화 해야 한다
            System.out.println("age: " + find_member2.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
