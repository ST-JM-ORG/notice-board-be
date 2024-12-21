package com.notice_board.common.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <pre>
 *	Pagination 결과
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2024-11-30 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaginationResDto<T> {
  private String error;
  private List<T> data;
  private Long total;
  private Integer size;
  private Integer page;
}
