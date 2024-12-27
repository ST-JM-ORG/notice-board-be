package com.notice_board.common.component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <pre>
 * 	Pagination 결과
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

    @Schema(description = "결과 Data 목록")
    private List<T> data;

    @Schema(description = "총 Data 수")
    private Long total;

    @Schema(description = "페이지 크기")
    private Integer size;

    @Schema(description = "페이지 번호")
    private Integer page;
}
