package com.aline.underwritermicroservice.authorization;

import com.aline.core.model.Applicant;
import com.aline.core.model.Application;
import com.aline.core.model.user.MemberUser;
import com.aline.core.model.user.UserRole;
import com.aline.core.security.service.AbstractAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("applicationAuth")
@RequiredArgsConstructor
public class ApplicationAuthorizationService extends AbstractAuthorizationService<Application> {


    @Override
    public boolean canAccess(Application application) {

        if (getUser().getUserRole() == UserRole.MEMBER) {
            MemberUser user = (MemberUser) getUser();
            Applicant applicant = user.getMember().getApplicant();
            return application.getApplicants().contains(applicant);
        }

        return roleIsManagement();
    }
}
