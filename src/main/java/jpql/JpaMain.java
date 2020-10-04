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

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            List<Member> resultList = query1.getResultList(); // 값이 여러개 일 때

            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            String singleResult = query2.getSingleResult();
            // 값이 하나 일 때 -> 없거나, 두개 이상이면 Exception 이 뜬다
            // Spring Data JPA -> 에서는 Null, 이나 Optional 로 반환

            Query query3 = em.createQuery("select m.username, m.age from Member m");

            Member singleResult1 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult1 = " + singleResult1.getUsername());


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
