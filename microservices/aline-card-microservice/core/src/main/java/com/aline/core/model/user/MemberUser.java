package com.aline.core.model.user;

import com.aline.core.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue(UserRole.Roles.MEMBER)
public class MemberUser extends User {

    @NotNull
    @OneToOne
    private Member member;

}
