package com.aline.underwritermicroservice.service;

import com.aline.core.exception.BadRequestException;
import com.aline.core.model.Applicant;
import com.aline.core.model.Application;
import com.aline.core.model.ApplicationStatus;
import com.aline.core.model.ApplicationType;
import com.aline.core.model.credit.CreditCardOffer;
import com.aline.core.model.credit.CreditLine;
import com.aline.core.model.credit.CreditLineStatus;
import com.aline.core.model.loan.Loan;
import com.aline.core.model.loan.LoanStatus;
import com.aline.underwritermicroservice.model.CreditScoreRating;
import com.aline.underwritermicroservice.service.function.UnderwriterConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Underwriter Service
 * <p>Used to approve or deny applications automatically.</p>
 */
@Service
@RequiredArgsConstructor
public class UnderwriterService {

    public static class DenyReasons {
        public static final String INSUFFICIENT_INCOME = "Income is insufficient.";
    }

    /**
     * This method is used to underwrite an application.
     * The logic is not variable but the result can be used
     * within one of the functions in any way that is fit.
     * When the application is approved, the {@link UnderwriterConsumer}
     * will provide an ApplicationStatus object as a parameter in
     * the function that can be used to either apply to the application
     * or for other logic.
     * @param application The application to approve or deny.
     * @param underwriterConsumer Function for approving or denying an application.
     */
    public void underwriteApplication(Application application, UnderwriterConsumer underwriterConsumer) {

        List<String> reasons = new ArrayList<>();

        checkIncome(application, reasons);

        if (reasons.isEmpty()) { // Income must be over or equal to $15,000.00 annually.
            underwriterConsumer.respond(ApplicationStatus.APPROVED, new String[]{"Application was approved"});
        } else {
            underwriterConsumer.respond(ApplicationStatus.DENIED, reasons.toArray(new String[0]));
        }
    }

    public Loan createLoan(Application application) {
        if (application.getApplicationType() != ApplicationType.LOAN)
            throw new BadRequestException("Unable to create a loan with a non-loan application.");

        if (application.getLoanType() == null)
            throw new BadRequestException("Loan type is required if application type is LOAN.");

        if (application.getApplicationAmount() == null)
            throw new BadRequestException("Application amount is required if application type is LOAN.");

        Applicant applicant = application.getPrimaryApplicant();

        return Loan.builder()
                .loanType(application.getLoanType())
                .amount(application.getApplicationAmount())
                .status(LoanStatus.PENDING)
                .apr(calculateApr(getCreditScore(applicant)))
                .term(calculateTerm(application))
                .startDate(LocalDate.now())
                .build();
    }

    public CreditLine createCreditLine(Application application) {

        CreditCardOffer cardOffer = application.getCardOffer();
        Applicant applicant = application.getPrimaryApplicant();

        int creditScore = getCreditScore(applicant);
        float apr = calculateApr(creditScore, cardOffer.getMinApr(), cardOffer.getMaxApr());

        return CreditLine.builder()
                .creditLineType(cardOffer.getCreditLineType())
                .minPayment(cardOffer.getMinPayment())
                .creditLimit(cardOffer.getAmount())
                .startDate(LocalDate.now())
                .apr(apr)
                .status(CreditLineStatus.PENDING)
                .build();

    }

    public int getCreditScore(Applicant applicant) {
        // This generates a fake credit score for testing purposes
        int age = Period.between(applicant.getDateOfBirth(), LocalDate.now()).getYears();
        int income = applicant.getIncome();
        int score = 0;

        score += age >= 18 ? 300 : 0;
        score += age >= 25 ? 200 : 0;
        score -= income <= 1500000 ? 300 : 0;
        score += income >= 3000000 ? 200 : 0;
        score += income >= 5500000 ? 150 : 0;

        return score;
    }

    public CreditScoreRating rateCreditScore(int creditScore) {
        CreditScoreRating rating = null;

        if (creditScore >= 300 && creditScore <= 579) {
            rating = CreditScoreRating.POOR;
        } else if (creditScore >= 580 && creditScore <= 669) {
            rating = CreditScoreRating.FAIR;
        } else if (creditScore >= 670 && creditScore <= 739) {
            rating  = CreditScoreRating.GOOD;
        } else if (creditScore >= 740 && creditScore <= 799) {
            rating = CreditScoreRating.VERY_GOOD;
        } else if (creditScore >= 800) {
            rating = CreditScoreRating.EXCELLENT;
        }

        return rating;
    }

    public float calculateApr(int creditScore) {
        CreditScoreRating rating = rateCreditScore(creditScore);
        switch (rating) {
            case POOR:
                return 24.99f;
            case FAIR:
                return 18.5f;
            case GOOD:
                return 11.5f;
            case VERY_GOOD:
                return 8.25f;
            case EXCELLENT:
                return 5.99f;
        }
        return 30.98f;
    }

    public float calculateApr(int creditScore, float minApr, float maxApr) {
        CreditScoreRating rating = rateCreditScore(creditScore);
        switch (rating) {
            case POOR:
            case FAIR:
                return maxApr;
            case GOOD:
                return (minApr + maxApr) / 2;
            case VERY_GOOD:
            case EXCELLENT:
                return minApr;
        }
        return maxApr;
    }

    public int calculateTerm(Application application) {
        Applicant applicant = application.getPrimaryApplicant();
        int income = applicant.getIncome();
        int idealIncome = 3000000;
        int applyAmount = application.getApplicationAmount();

        if (applyAmount < 200000) {
            return 12;
        }

        if (income - idealIncome <= 0) {
            if (applyAmount < 1000000) {
                return 24;
            }
            return 36;
        }

        if (income - idealIncome >= idealIncome) {
            if (applyAmount < 1000000) {
                return 6;
            }
            return 12;
        }

        if (applyAmount > 10000000) {
            return 60;
        }

        if (applyAmount > 5000000) {
            return 48;
        }

        return 24;
    }

    private void checkIncome(Application application, List<String> reasons) {
        Applicant primaryApplicant = application.getPrimaryApplicant();
        checkCondition(primaryApplicant.getIncome() < 1500000,
                        DenyReasons.INSUFFICIENT_INCOME,
                        reasons);
    }

    private void checkCondition(boolean condition, String reason, List<String> reasons) {
        if (condition) {
            reasons.add(reason);
        }
    }

}
