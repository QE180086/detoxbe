package com.parttime.job.Application.common.utils;

import com.parttime.job.Application.common.constant.SortMessageConstant;
import com.parttime.job.Application.common.request.PagingRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

public class PagingUtil {
    public static Sort createSort(PagingRequest pagingRequest) {
        if (pagingRequest.getSortRequest() != null && StringUtils.hasText(pagingRequest.getSortRequest().getField())) {
            return Sort.by(
                    SortMessageConstant.SORT_DESC.equalsIgnoreCase(pagingRequest.getSortRequest().getDirection()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC,
                    pagingRequest.getSortRequest().getField()
            );
        }
        return Sort.unsorted();
    }

    private PagingUtil() {
    }
}
