package com.aline.cardmicroservice.service;

import com.aline.cardmicroservice.repository.CardRepository;
import com.aline.core.repository.AccountRepository;
import com.aline.core.util.CardUtility;
import com.aline.core.util.RandomNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class CardServiceTest {

    @Mock
    CardRepository repository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    CardIssuerService cardIssuerService;
    @Mock
    RandomNumberGenerator randomNumberGenerator;
    CardUtility cardUtility;
    CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardUtility = new CardUtility(randomNumberGenerator);
        cardService = new CardService(repository, accountRepository, cardIssuerService, randomNumberGenerator, cardUtility);
        when(randomNumberGenerator.generateRandomNumberString(11)).thenReturn("12345678912");
    }

    @Test
    void test_createDebitCard() {}

    @Test
    void test_validateCardNumber() {
        assertTrue(cardService.validateCardNumber("4539343576281337"));
        assertTrue(cardService.validateCardNumber("12345674"));
        assertFalse(cardService.validateCardNumber("12345675"));
        assertTrue(cardService.validateCardNumber("5432178944"));
        assertFalse(cardService.validateCardNumber("5432178948"));
    }

    @Test
    void test_generateCardNumber() {
        String cardNumber = cardService.generateCardNumber("1234", 16);
        assertEquals("1234", cardNumber.substring(0, 4));
        assertEquals(16, cardNumber.length());
        assertEquals(8, Character.getNumericValue(cardNumber.charAt(15)));
        assertTrue(cardService.validateCardNumber(cardNumber));
    }

}
