package com.aline.bankmicroservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberSearchCriteria {

    @Nullable
    private String searchName;
    @Nullable
    private Long searchId;
    @Nullable
    private String accountStatus;
    @Nullable
    private Integer floorAmount;
    @Nullable
    private Integer ceilingAmount;
    @Nullable
    private Boolean isPrimary;
    @Nullable
    private Boolean hasChecking;
    @Nullable
    private Boolean hasSavings;
}
