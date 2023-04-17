package com.aline.core.model;

import com.aline.core.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 * One-Time Passcode for password reset.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class OneTimePasscode {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otp_generator")
    @SequenceGenerator(name = "otp_generator", sequenceName = "otp_sequence", allocationSize = 1)
    private int id;

    /**
     * The one-time passcode
     */
    private String otp;

    /**
     * Make sure that the passcode has been checked
     */
    private boolean checked;

    /**
     * The user the request is resetting the password for
     */
    @OneToOne(optional = false)
    private User user;

}
