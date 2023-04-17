package com.aline.core.model;

import com.aline.core.listener.CreateMemberListener;
import com.aline.core.model.account.Account;
import com.aline.core.model.card.Card;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EntityListeners(CreateMemberListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Branch branch;

    @Column(unique = true)
    private String membershipId;

    @OneToOne(optional = false)
    private Applicant applicant;

    @ManyToMany
    @JoinTable(
            name = "account_holder",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    @JsonManagedReference
    @ToString.Exclude
    private Set<Account> accounts;

    @OneToMany
    @JoinTable(
            name = "card_holder",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    @JsonManagedReference
    @ToString.Exclude
    private Set<Card> cards;

}
