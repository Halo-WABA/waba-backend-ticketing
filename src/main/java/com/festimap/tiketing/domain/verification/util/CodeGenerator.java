package com.festimap.tiketing.domain.verification.util;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CodeGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    private CodeGenerator() {
    }

    public static String generateCode() {
        return IntStream.range(0,6)
                .map(i->secureRandom.nextInt(10))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}
