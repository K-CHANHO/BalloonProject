package org.zerock.balloon.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.balloon.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

//    @Query
//    String getNicknameById(String id);

    //id로 회원 정보 불러오기
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.sns = :sns and m.id = :id")
    Optional<Member> findById(String id, boolean sns);

    //email로 회원 정보 불러오기
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.sns = :sns and m.email = :email")
    Optional<Member> findByEmail(String email, boolean sns);

    //중복체크
    boolean existsById(String id);
    List<Member> findAllByEmail(String email);
    List<Member> findByNickname(String nickname);

    /**
     * 회원정보 변경
     */

//    @Modifying
//    @Query("update Member m set m.pw = :pw where m.id = :id")
//    List<Member> updatePasswordById(String id, String pw);



}
