package com.aline.core.paginated;

import com.aline.core.model.Branch;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class BranchPaginated extends PageImpl<Branch> {
    @JsonCreator
    public BranchPaginated(@JsonProperty("content") List<Branch> content,
                           @JsonProperty("number") int number,
                           @JsonProperty("size") int size,
                           @JsonProperty("pageable") JsonNode pageable,
                           @JsonProperty("totalElements") Long totalElements,
                           @JsonProperty("last") boolean last,
                           @JsonProperty("totalPages") int totalPages,
                           @JsonProperty("first") boolean first,
                           @JsonProperty("sort") JsonNode sort,
                           @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public BranchPaginated(List<Branch> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public BranchPaginated(List<Branch> content, int number, int size, Sort sort, long total) {
        super(content, PageRequest.of(number, size, sort), total);
    }

    public BranchPaginated(List<Branch> content) {
        super(content);
    }

    public BranchPaginated() {
        super(new ArrayList<Branch>());
    }
}
