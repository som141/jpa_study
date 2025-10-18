package start;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class JpaMain {
    public static void main(String[] args) {


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("start");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            truncateAllTables(em, "jstudy");
            logic(em);
            Member member = em.find(Member.class, "id1");//먼저 컨텍스트를 뒤져보고 그 다음 없으면 db를 뒤져서 찾음 그리고 영속성에 저장하고 반환.
            System.out.println(member.getAge());
            Member a = em.find(Member.class,"id1");
            Member b = em.find(Member.class,"id1");
            System.out.println(a==b);//영속성 컨텍스트에 캐싱해서 참조값이 같으므로 동등성을 보장 할 수 있다.
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.out.println("cutcutcut");
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void logic(EntityManager em){
        String id ="id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("영주");
        member.setAge(2);
        //등록
        em.persist(member);//일단 컨텍스트에 저장되고 이걸 커밋할때 db로 내림.//저장만.
        //수정
        member.setAge(22);

        List<Member> list = em.createQuery(
                "select m from Member m where m.age >= :age order by m.username",
                Member.class
        ).setParameter("age", 20).getResultList();
        System.out.println(list);
    }
    private static void truncateAllTables(EntityManager em, String schema) {
        // 1) 스키마의 테이블 목록 조회
        List<String> tables = em.createNativeQuery(
                "SELECT table_name FROM information_schema.tables " +
                        "WHERE table_schema = :schema AND table_type='BASE TABLE'"
        ).setParameter("schema", schema).getResultList();

        // 2) FK 끄고 TRUNCATE
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        for (Object t : tables) {
            String table = (String) t;
            em.createNativeQuery("TRUNCATE TABLE `" + table + "`").executeUpdate();
        }
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}