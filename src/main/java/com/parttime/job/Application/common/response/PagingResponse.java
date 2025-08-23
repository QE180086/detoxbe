package com.parttime.job.Application.common.response;

import com.parttime.job.Application.common.request.PagingRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse<T> {
    private List<T> content;
    private PagingRequest request;
    private long totalElement;
}
