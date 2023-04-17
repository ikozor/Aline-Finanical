package com.aline.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmUserRegistrationResponse {

    private String username;
    private LocalDateTime confirmedAt;
    private boolean enabled;

}
