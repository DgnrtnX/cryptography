package com.algorithms.cryptography.util;

import java.util.function.LongUnaryOperator;

import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HashUtil {
	private static final int iterationCount = 10000;
	private static final int keyLength = 256;

	public byte[] generatePBKDF2Key(String password, String salt) {
		PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator();
		generator.init(password.getBytes(), salt.getBytes(), iterationCount);
		KeyParameter keyParameter = (KeyParameter) generator.generateDerivedMacParameters(keyLength);

		return keyParameter.getKey();
	}

	public String cipherNumber(long number) {
		LongUnaryOperator encryptOperation = n -> (n + 65537) % 10;
		return applyOperationToDigits(number, encryptOperation);
	}

	private String applyOperationToDigits(long number, LongUnaryOperator operation) {
		long result = 0;
		long multiplier = 1;

		while (number > 0) {
			long digit = number % 10;
			long encryptedDigit = operation.applyAsLong(digit);
			result += encryptedDigit * multiplier;
			multiplier *= 10;
			number /= 10;
		}

		log.info("Long value is {}", result);
		return String.valueOf(result);
	}
}
