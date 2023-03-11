package com.sss.app.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test2Test {


    @DisplayName("Test Spring @Autowired Integration")
    @Test
    void testGet() {

        assertEquals("abc", new StringBuilder().append("abc").toString());
    }
}
