package jpql;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("TeamA");
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            // inner 생략 가능
//            String query = "select m from Member m inner join m.team t";

            // outer 생략 가능
//            String query = "select m from Member m left join m.team t";

            // 세타 조인
//            String query = "select m from Member m, Team t where m.username = t.name";

            // 조인 대상 필터링
//            String query = "select m from Member m left join m.team t on t.name = 'teamA'";

            // 연관 관계 없는 외부 조인
            String query = "select m from Member m left join Team t on m.username = t.name";
            List<Member> resultList = em.createQuery(query, Member.class)
                    .getResultList();

            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
