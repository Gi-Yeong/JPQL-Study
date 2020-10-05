package jpql;

import javax.persistence.*;
import java.util.Collection;
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
            member.setUsername("관리자");
            member.setAge(10);
            member.setTeam(team);
            member.setType(MemberType.ADMIN);

            em.persist(member);

            em.flush();
            em.clear();

//            String query = "select m.username From Member m";
//            String query = "select t.members From Team t";
            String query = "select m.username From Team t join t.members m";
            List<Integer> resultList = em.createQuery(query, Integer.class)
                    .getResultList();

            for (Integer integer : resultList) {
                System.out.println("integer = " + integer);
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
