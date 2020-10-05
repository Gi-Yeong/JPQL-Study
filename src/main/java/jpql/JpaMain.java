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


            em.flush();
            em.clear();

            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();

            for (Member m : resultList) {
                System.out.println("m = " + m);
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
