package com.ptitB22CN539.QuizRemake.Utils;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {
    public static Pageable getPageable(Integer page, Integer limit) {
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 10;
        }
        return PageRequest.of(page - 1, limit);
    }
    public static Pageable getPageable(Integer page, Integer limit, @NotNull Sort sort) {
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 10;
        }
        return PageRequest.of(page - 1, limit, sort);
    }
}
