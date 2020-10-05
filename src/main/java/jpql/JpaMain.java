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

//            String query = "select m from Member m";
            String query = "select m from Member m join fetch m.team";
            List<Member> resultList = em.createQuery(query, Member.class).getResultList();

            for (Member m : resultList) {
                System.out.println("member3 = " + m.getUsername() + ", " + m.getTeam().getName());
                // 회원1, 팀A(SQL)
                // 회원2, 팀A(1 차 캐시)
                // 회원3, 팀B(SQL)

                // 회원 100명 -> 쿼리가 100 번 나감: N + 1
                
                // fetch 조인으로 변경 하면 select 쿼리 한번에 모든 정보를 다 가져올 수 있다
                // 지연 로딩 없이 그냥 한번에 다 가져온다
            }

            query = "select distinct t From Team t join fetch t.members m";
            List<Team> resultList1 = em.createQuery(query, Team.class)
                    .getResultList();

            for (Team team : resultList1) {
                System.out.println("team = " + team.getName() + " , " + team.getMembers().size());
                for(Member mem : team.getMembers()) {
                    System.out.println("mem = " + mem);
                }
                //team = 팀A , 2
                //team = 팀A , 2 -> 왜 2개지? 일 대 다 관계에서는 데이터가 늘어 날 수 밖에 없다
                //team = 팀B , 1
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
