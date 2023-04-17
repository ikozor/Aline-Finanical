package com.aline.transactionmicroservice.util;

import com.aline.core.model.Member_;
import com.aline.core.model.account.Account_;
import com.aline.transactionmicroservice.model.Merchant;
import com.aline.transactionmicroservice.model.Merchant_;
import com.aline.transactionmicroservice.model.Transaction;
import com.aline.transactionmicroservice.model.Transaction_;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class TransactionSpecification implements Specification<Transaction> {

    private final TransactionCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> cq, CriteriaBuilder criteriaBuilder) {
        String[] searchTerms = criteria.getSearchTerms();

        switch (criteria.getMode()) {
            case ACCOUNT:
                long accountId = criteria.getAccountId();
                return searchByAccount(accountId, searchTerms, root, criteriaBuilder);
            case MEMBER:
                long memberId = criteria.getMemberId();
                return searchByMemberId(memberId, searchTerms, root, criteriaBuilder);
            default:
                return null;
        }
    }

    public Predicate searchByAccount(long accountId, String[] searchTerms, Root<Transaction> root, CriteriaBuilder cb) {
        final Predicate byAccount = cb.equal(root.get(Transaction_.account).get(Account_.ID), accountId);
        return getSearchTermsPredicate(searchTerms, root, cb, byAccount);
    }

    public Predicate searchByMemberId(long memberId, String[] searchTerms, Root<Transaction> root, CriteriaBuilder cb) {

        val members = root.join(Transaction_.account).join(Account_.members);
        val byMember = cb.equal(members.get(Member_.id), memberId);
        return getSearchTermsPredicate(searchTerms, root, cb, byMember);
    }

    private Predicate getSearchTermsPredicate(String[] searchTerms, Root<Transaction> root, CriteriaBuilder cb, Predicate byPredicate) {
        if (searchTerms != null) {
            if (searchTerms.length > 0) {
                log.info("Searching with search terms: {}", Arrays.toString(searchTerms));
                Predicate bySearchTerms = cb.or(Arrays.stream(searchTerms)
                        .map(term -> searchBySearchTerm(term, root, cb))
                        .toArray(Predicate[]::new));
                return cb.and(byPredicate, bySearchTerms);
            }
        }
        return byPredicate;
    }

    public Predicate searchBySearchTerm(String searchTerm, Root<Transaction> root, CriteriaBuilder cb) {

        Predicate[] searchTerms = Arrays.stream(searchTerm.split("[\\s,]+"))
                .map(term -> {
                    String searchPattern = "%" + term.toLowerCase(Locale.ROOT) + "%";
                    log.info("Search Pattern: {}", searchPattern);
                    return cb.like(cb.lower(root.get(Transaction_.description)), searchPattern);
                }).toArray(Predicate[]::new);
        List<Predicate> merchantInfo = searchMerchantInfoBySearchTerm(searchTerm, root, cb);
        return cb.or(cb.and(searchTerms), cb.or(merchantInfo.toArray(new Predicate[0])));
    }

    public List<Predicate> searchMerchantInfoBySearchTerm(String searchTerm, Root<Transaction> root, CriteriaBuilder cb) {
        Path<Merchant> merchantPath = root.get(Transaction_.merchant);
        List<Path<String>> merchantProperties = Arrays.asList(
                merchantPath.get(Merchant_.name),
                merchantPath.get(Merchant_.code),
                merchantPath.get(Merchant_.address),
                merchantPath.get(Merchant_.city),
                merchantPath.get(Merchant_.state)
        );
        String searchPattern = "%" + searchTerm.toLowerCase(Locale.ROOT) + "%";

        return merchantProperties.stream()
                .map(property -> cb.like(cb.lower(property), searchPattern))
                .collect(Collectors.toList());

    }

}
