package com.example.csci3130groupproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import android.util.Base64;

import com.example.csci3130groupproject.core.ApplicationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30, application = TestApplication.class)

public class ApplicationSubmissionUnitTest {
    private ApplicationRepository repository;

    @Before
    public void setup() {
        repository = new ApplicationRepository();
    }

    // Verifies encodeResume returns a non-null result for valid input
    @Test
    public void testEncodeResume_returnsNonNull() {
        byte[] fakePdf = "fake pdf content".getBytes();
        String result = repository.encodeResume(fakePdf);
        assertNotNull("Encoded resume should not be null", result);
    }

    // Verifies encodeResume returns a non-empty string for valid input
    @Test
    public void testEncodeResume_returnsNonEmptyString() {
        byte[] fakePdf = "fake pdf content".getBytes();
        String result = repository.encodeResume(fakePdf);
        assertFalse("Encoded resume should not be empty", result.isEmpty());
    }

    // Verifies encodeResume produces a valid Base64 string that decodes back to original
    @Test
    public void testEncodeResume_isDecodable() {
        byte[] original = "fake pdf content".getBytes();
        String encoded = repository.encodeResume(original);
        byte[] decoded = Base64.decode(encoded, Base64.DEFAULT);
        assertEquals("Decoded bytes should match original",
                new String(original), new String(decoded));
    }

    // Verifies encodeResume handles an empty byte array without crashing
    @Test
    public void testEncodeResume_emptyBytes_doesNotCrash() {
        byte[] empty = new byte[0];
        String result = repository.encodeResume(empty);
        assertNotNull("Result should not be null for empty input", result);
    }

    // Verifies different inputs produce different encoded outputs
    @Test
    public void testEncodeResume_differentInputs_produceDifferentOutputs() {
        byte[] input1 = "resume one".getBytes();
        byte[] input2 = "resume two".getBytes();
        String encoded1 = repository.encodeResume(input1);
        String encoded2 = repository.encodeResume(input2);
        assertFalse("Different inputs should produce different encoded strings",
                encoded1.equals(encoded2));
    }

    // Verifies the same input always produces the same encoded output
    @Test
    public void testEncodeResume_sameInput_producesSameOutput() {
        byte[] input = "consistent resume".getBytes();
        String encoded1 = repository.encodeResume(input);
        String encoded2 = repository.encodeResume(input);
        assertEquals("Same input should always produce the same encoded string",
                encoded1, encoded2);
    }
}