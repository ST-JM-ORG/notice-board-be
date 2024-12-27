package com.notice_board.repository;

import com.notice_board.model.auth.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByIdAndUserType(Long id, Member.UserType userType);

    Optional<Member> findByIdAndUserTypeNot(Long id, Member.UserType userType);
}
