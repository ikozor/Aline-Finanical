package com.aline.bankmicroservice.controller;

import com.aline.bankmicroservice.dto.request.CreateBranch;
import com.aline.bankmicroservice.dto.request.UpdateBranch;
import com.aline.bankmicroservice.service.BranchService;
import com.aline.core.model.Branch;
import com.aline.core.paginated.BranchPaginated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/branches")
@Slf4j(topic = "branch Controller")
@RequiredArgsConstructor
@Tag(name = "branches")
public class BranchController {

    private static final int DEFAULT_PAGE_SIZE = 15;
    private final BranchService branchService;
    @Value("${server.port}")
    public int PORT;

    @Operation(description = "Retrieve a page of branches")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieves a paginated branch dto")})
    @GetMapping("")
    public ResponseEntity<BranchPaginated> getBranches(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {

        BranchPaginated branches = branchService.getBranches(pageable);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(branches);
    }

    @Operation(description = "Create a Branch of the main bank")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully creates a branch")
    })
    @PostMapping()
    public ResponseEntity<Branch> createBranch(@RequestBody CreateBranch branchInfo) {
        Branch branch = branchService.postBranch(branchInfo);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/branches/{id}")
                .port(PORT)
                .buildAndExpand(branch.getId())
                .toUri();

        return ResponseEntity.created(location).contentType(MediaType.APPLICATION_JSON).body(branch);
    }

    @Operation(description = "Update a Branch of the main bank")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully updates a branch")
    })
    @PutMapping()
    public ResponseEntity<Void> updateBranch(@RequestBody UpdateBranch updateBranch) {
        branchService.updateBranch(updateBranch);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Update a Branch of the main bank")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deletes a branch")
    })
    @DeleteMapping()
    public ResponseEntity<Void> deleteBranchById(@RequestParam Long branchId) {
        branchService.deleteBranch(branchId);
        return ResponseEntity.noContent().build();
    }
}
