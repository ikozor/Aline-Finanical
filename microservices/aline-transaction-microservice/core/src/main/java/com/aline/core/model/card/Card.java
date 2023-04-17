package com.aline.core.model.card;

import com.aline.core.model.Member;
import com.aline.core.model.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

    @ManyToOne(optional = false)
    private Member cardHolder;

    @ManyToOne(optional = false)
    private Account account;

    @NotNull
    @Column(unique = true)
    private String cardNumber;

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    @Length(min = 3, max = 3)
    @Pattern(regexp = "\\d{3}")
    private String securityCode;

    @ManyToOne
    @JoinColumn(name = "card_issuer_name")
    private CardIssuer cardIssuer;

}
