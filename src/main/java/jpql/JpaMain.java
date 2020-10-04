package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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

            String query = "select " +
                        " case when m.age <= 10 then '학생요금' " +
                        "      when m.age >= 60then '경로요금' " +
                        "      else '일반요금' " +
                        " end " +
                        " from Member m ";
            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();
            for (String s : resultList) {
                System.out.println("s = " + s);
            }

            query = "select coalesce(m.username, '이름없는 회원') from Member m";
            List<String> resultList1 = em.createQuery(query, String.class)
                    .getResultList();
            for (String s : resultList1) {
                System.out.println("s = " + s);
            }

            query = "select nullif(m.username, '관리자') from Member m";
            List<String> resultList2 = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList2) {
                System.out.println("s = " + s);
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
