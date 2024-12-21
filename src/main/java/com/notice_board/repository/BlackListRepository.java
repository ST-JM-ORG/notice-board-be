package com.notice_board.repository;

import com.notice_board.model.auth.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    Optional<BlackList> findByInvalidRefreshToken(String invalidRefreshToken);
}
