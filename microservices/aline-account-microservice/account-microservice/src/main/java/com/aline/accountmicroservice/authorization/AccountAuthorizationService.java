package com.aline.accountmicroservice.authorization;

import com.aline.core.model.Member;
import com.aline.core.model.account.Account;
import com.aline.core.model.user.MemberUser;
import com.aline.core.model.user.UserRole;
import com.aline.core.security.service.AbstractAuthorizationService;
import org.springframework.stereotype.Component;

@Component("accountAuth")
public class AccountAuthorizationService extends AbstractAuthorizationService<Account> {

    @Override
    public boolean canAccess(Account returnObject) {

        if (getRole() == UserRole.MEMBER) {
            MemberUser user = (MemberUser) getUser();
            Member member = user.getMember();
            return member.getAccounts().contains(returnObject);
        }

        return roleIsManagement();
    }

    /**
     * Check if member ID can access the account resource
     * @param memberId The member ID to check against
     * @return Can access true or false
     */
    public boolean memberCanAccess(long memberId) {

        if (getRole() == UserRole.MEMBER) {
            MemberUser user = (MemberUser) getUser();

            Member member = user.getMember();

            return member.getId() == memberId;
        }

        return roleIsManagement();
    }
}
