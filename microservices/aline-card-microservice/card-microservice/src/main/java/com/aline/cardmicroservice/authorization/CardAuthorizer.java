package com.aline.cardmicroservice.authorization;

import com.aline.core.dto.request.CreateDebitCardRequest;
import com.aline.core.model.Member;
import com.aline.core.model.account.Account;
import com.aline.core.model.card.Card;
import com.aline.core.model.user.MemberUser;
import com.aline.core.model.user.UserRole;
import com.aline.core.security.service.AbstractAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component("authService")
@RequiredArgsConstructor
public class CardAuthorizer extends AbstractAuthorizationService<Card> {

    @Override
    public boolean canAccess(Card card) {
        if (getRole() == UserRole.MEMBER) {
            MemberUser user = (MemberUser) getUser();
            return user.getMember().getMembershipId().equals(card.getCardHolder().getMembershipId());
        }
        return roleIsManagement();
    }

    public boolean canAccessByMemberId(Long memberId) {
        if (memberId == null)
            return false;

        if (getRole() == UserRole.MEMBER) {
            MemberUser user = (MemberUser) getUser();
            return user.getMember().getId().equals(memberId);
        }

        return roleIsManagement();
    }

    public boolean canAccessByCreateDebitCardRequest(CreateDebitCardRequest cardRequest) {
        String accountNumber = cardRequest.getAccountNumber();
        String membershipId = cardRequest.getMembershipId();

        if (getRole() == UserRole.MEMBER) {
            MemberUser user = (MemberUser) getUser();
            Member member = user.getMember();
            Set<Account> accounts = member.getAccounts();
            return member.getMembershipId().equals(membershipId) &&
                    accounts.stream()
                            .map(Account::getAccountNumber)
                            .collect(Collectors.toSet())
                            .contains(accountNumber);
        }

        return roleIsManagement();
    }

}
