package com.notice_board.repository.specification;

import com.notice_board.api.admin.dto.UserSearchReqDto;
import com.notice_board.model.auth.Member;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminUserSpecification {

    // SearchType과 관련된 필드 이름을 매핑하는 Map
    private static final Map<UserSearchReqDto.SearchType, String> SEARCH_FIELD_MAP = Map.of(
            UserSearchReqDto.SearchType.ALL, "name,email,contact",
            UserSearchReqDto.SearchType.NAME, "name",
            UserSearchReqDto.SearchType.EMAIL, "email",
            UserSearchReqDto.SearchType.CONTACT, "contact"
    );

    public static Specification<Member> buildSearchSpecification(UserSearchReqDto reqDto, Member.UserType userType) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // SUPER_ADMIN 제외 조건
            predicates.add(criteriaBuilder.notEqual(root.get("userType"), Member.UserType.SUPER_ADMIN));

            // ADMIN 인 경우 USER 만 출력
            if (userType == Member.UserType.ADMIN) {
                predicates.add(criteriaBuilder.equal(root.get("userType"), Member.UserType.USER));
            }

            String keyword = reqDto.getKeyword();

            // 검색 키워드가 있을 경우
            if (StringUtils.isNotEmpty(keyword)) {
                String likePattern = "%" + keyword.toLowerCase() + "%";  // 검색어를 소문자로 변환
                UserSearchReqDto.SearchType searchType = reqDto.getSearchType();

                // 검색할 필드를 가져옴
                String searchFields = SEARCH_FIELD_MAP.get(searchType);

                if (searchFields != null) {
                    String[] fields = searchFields.split(",");
                    List<Predicate> fieldPredicates = new ArrayList<>();
                    for (String field : fields) {
                        fieldPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), likePattern));
                    }
                    predicates.add(criteriaBuilder.or(fieldPredicates.toArray(new Predicate[0])));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

