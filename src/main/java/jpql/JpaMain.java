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

            em.flush();
            em.clear();

            // 프로젝션이 되면 영속성 컨텍스트에서 모두 관리 된다, 즉 이 코드에서는 Update 쿼리가 나감
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            Member member1 = result.get(0);
            member1.setAge(20);

            // select m.team from Member m 으로 해도 되지만, 아래 처럼 join 문법으로 해야 JPQL 에서도
            // 아 JOIN 이 들어가는 구나 하고 명확하게 알 수 있다
            List<Team> result1 = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();

            List<Address> result2 = em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();


            // DTO 를 만들어서 new 를 써서 패키지 명을 포함한 전체 클래스 명을 입력해서 만든다, 생성자 필요
            List<MemberDTO> resultList = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = resultList.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getAge());
            
            

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
