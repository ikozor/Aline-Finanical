package com.aline.core.listener;

import com.aline.core.model.Member;
import com.aline.core.util.RandomNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;

/**
 * The CreateMemberLister class introduces
 * side effects to the entity using the
 * JPA Entity life-cycle annotations.
 */
@Component
@Slf4j(topic = "Create Member Listener")
public class CreateMemberListener {

    private RandomNumberGenerator randomNumberGenerator;

    @Autowired
    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @PrePersist
    public void prePersist(Member member) {
        member.setMembershipId(generateMembershipId());
    }

    /**
     * Generate 8 digit random number.
     * @return 8 digit random number.
     */
    private String generateMembershipId() {
        return randomNumberGenerator.generateRandomNumberString(8);
    }

}
