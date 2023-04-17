package com.aline.core.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class utilizes the {@link SecureRandom} to
 * generate random numbers.
 * <p>
 *     This class can also be injected into
 *     a service.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class RandomNumberGenerator {

    private final SecureRandom secureRandom;

    public String generateRandomNumberString(int length) {
        byte[] seed = secureRandom.generateSeed(16);
        secureRandom.setSeed(seed);

        return IntStream.range(0, length)
                .map(x -> secureRandom.nextInt(9))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

}
