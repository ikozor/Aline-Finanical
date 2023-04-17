package com.aline.bankmicroservice.controller;

import com.aline.bankmicroservice.dto.request.MemberSearchCriteria;
import com.aline.bankmicroservice.dto.request.MemberUpdateRequest;
import com.aline.bankmicroservice.dto.response.MemberResponse;
import com.aline.bankmicroservice.service.MemberService;
import com.amazonaws.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@Slf4j(topic = "Member Controller")
@AllArgsConstructor
@Tag(name = "Member")
public class MemberController {
    private final MemberService memberService;

    @Operation(description = "Search Members by using the Member Search Criteria and Pageable Parameters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieves the search"),
            @ApiResponse(responseCode = "403", description = "Unauthorized attempt to access api. Must have employee or administrator authority")
    })
    @GetMapping("")
    public ResponseEntity<Page<MemberResponse>> getMembersBySearch(Pageable pageable,
                                                                   MemberSearchCriteria searchCriteria) {
        Page<MemberResponse> membersPage = memberService.searchMembers(searchCriteria, pageable);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(membersPage);
    }

    @Operation(description = "Retrieves Member by Membership Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieves member")
    })
    @GetMapping("/{membershipId}")
    public ResponseEntity<MemberResponse> getMemberByMembershipId(@PathVariable String membershipId){
        MemberResponse member = memberService.getMemberByMembershipId(membershipId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(member);
    }

    @Operation(description = "Update Member Application details")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully updates")
    })
    @PutMapping("")
    public ResponseEntity<Void> updateMember(@RequestBody MemberUpdateRequest update) {
        memberService.updateMember(update);
        return ResponseEntity.noContent().build();
    }
}
