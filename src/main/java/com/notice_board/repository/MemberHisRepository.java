package com.notice_board.repository;

import com.notice_board.model.auth.MemberHis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberHisRepository extends JpaRepository<MemberHis, Long> {
}
