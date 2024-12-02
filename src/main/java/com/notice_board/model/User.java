package com.notice_board.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

/**
 * <pre>
 * User
 * </pre>
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2024-12-02 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("U")
@SQLDelete(sql = "update member set del_dt = now() where id = ?")
public class User extends Member {

}
