package com.aline.core.model;

import com.aline.core.model.account.Account;
import com.aline.core.model.credit.CreditCardOffer;
import com.aline.core.model.loan.LoanType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * Application entity
 * <p>
 *     This class houses all the applicants that are applying for the same account.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class Application implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Application Type
     * @see ApplicationType
     */
    @NotNull(message = "Application type is required.")
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    /**
     * Application Status
     * @see ApplicationStatus
     */
    @NotNull(message = "Application status is required.")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @ManyToOne(optional = false)
    @NotNull(message = "Primary applicant is required.")
    private Applicant primaryApplicant;

    /**
     * Applicants that have applied under this application.
     * This information will be ignored by serialization of the
     * entity.
     */
    @ManyToMany
    @JoinTable(
            name = "application_applicant",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "applicant_id")
    )
    @ToString.Exclude
    @JsonManagedReference
    private Set<Applicant> applicants;

    /**
     * If type is LOAN then application amount is required
     */
    private Integer applicationAmount;

    /**
     * Loan type is required if application type is loan
     */
    private LoanType loanType;

    /**
     * The credit card offer that a credit card will be created from
     */
    @ManyToOne
    @JoinColumn(name = "card_offer_id")
    private CreditCardOffer cardOffer;

    /**
     * Account that a loan will be depositing into
     */
    @ManyToOne
    @JoinColumn(name = "deposit_account_id")
    private Account depositAccount;
}
