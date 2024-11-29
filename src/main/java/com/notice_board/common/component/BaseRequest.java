package com.notice_board.common.component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Delegate;

import java.io.Serializable;

/**
 * <pre>
 *  요청 BASE DTO
 * </pre>
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2024-11-30 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */
@Data
public class BaseRequest implements Serializable {

    @Delegate(types = DelegateInclude.class)
    private final PageDTO page;

    private String searchType;

    private String searchValue;

    public BaseRequest() {
        this.page = new PageDTO();
    }

    private interface DelegateInclude {
        void setPageIndex(int pageIndex);
        void setPageUnit(int pageUnit);
        void setPageSize(int pageSize);
        void setTotalCount(int totalCount);

        void initPageInfo();
    }

    @Getter @Setter @ToString
    public class PageDTO implements Serializable {

        private int pageIndex;
        private int pageUnit;
        private int pageSize;
        private int totalCount;
        private int totalPageCount;
        private int firstPageNoOnPageList;
        private int lastPageNoOnPageList;
        private int firstIndex;
        private int lastIndex;

        private int offset;

        public PageDTO() {
            this.pageIndex = 1;
            this.pageUnit = 10;
            this.pageSize = 10;
        }


        public void initPageInfo() {
            totalPageCount = (totalCount - 1) / pageUnit + 1;
            firstPageNoOnPageList = ((pageIndex - 1) / pageSize) * pageSize + 1;
            lastPageNoOnPageList = firstPageNoOnPageList + pageSize - 1;
            if (lastPageNoOnPageList > totalPageCount) {
                lastPageNoOnPageList = totalPageCount;
            }
            firstIndex = (pageIndex - 1) * pageUnit;
            lastIndex = pageIndex * pageUnit - 1;
            if (lastIndex >= totalCount) {
                lastIndex = totalCount - 1;
            }

            offset = (pageIndex - 1) * pageUnit;
        }
    }
}
