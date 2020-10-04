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

            // de 가 어느 위치에 있니?, 4번째 그래서 4를 반환 함
            String query = "select locate('de', 'abcdefg') from Member m";
            List<Integer> resultList = em.createQuery(query, Integer.class)
                    .getResultList();

            for (Integer integer : resultList) {
                System.out.println("integer = " + integer);
            }

            // Team 엔티티 안에 members 는 List 이다. size 함수는 해당 List 의 size 를 반환 한다.
            query = "select size(t.members) from Team t";
            em.createQuery(query);

            query = "select function('group_concat', m.username) from Member m";
            List<String> resultList1 = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList1) {
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
