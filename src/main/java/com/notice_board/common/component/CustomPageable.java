package com.notice_board.common.component;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * <pre>
 * 	CustomPageable
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2024-12-28 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */
@Data
public class CustomPageable {
    @Schema(description = "페이지 번호 (1부터 시작)", defaultValue = "1")
    private int page = 1;

    @Schema(description = "페이지 크기", defaultValue = "10")
    private int size = 10;

    @Hidden
    private String sort = "id";

    @Hidden
    private Sort.Direction direction = Sort.Direction.DESC;

    public PageRequest getPageable() {
        return createPageRequest(sort, direction);
    }

    public PageRequest getPageable(String sort, Sort.Direction direction) {
        String resolvedSort = (StringUtils.isNotEmpty(sort)) ? sort : this.sort;
        Sort.Direction resolvedDirection = (direction != null) ? direction : this.direction;

        return createPageRequest(resolvedSort, resolvedDirection);
    }

    private PageRequest createPageRequest(String sort, Sort.Direction direction) {
        return PageRequest.of(page - 1, size, direction, sort.split(","));
    }
}
