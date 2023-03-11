package com.sss.app.core.query.parsing;

import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;

public class BaseTextParser {
    private static final char QUOTE = '\'';

    protected void validateBracketCount(String text) {
        int countLeftBracket = 0;
        int countRightBracket = 0;
        boolean isWithinQuote = false;

        for (int i = 0; i < text.length(); i++) {
            // First check for a quote and toggle the isWithinQuote flag
            if (text.charAt(i) == QUOTE) {
                isWithinQuote = !isWithinQuote;
                continue;
            }

            // Ignore brackets within quotes
            if (isWithinQuote)
                continue;

            if (text.charAt(i) == '(')
                countLeftBracket++;

            if (text.charAt(i) == ')')
                countRightBracket++;

        }
        if (countLeftBracket != countRightBracket)
            throw new ApplicationRuntimeException(
                    TransactionErrorCode.INVALID_QUERY_TEXT_UNMATCHED_BRACKETS,
                    "left:" + countLeftBracket + " " + "right:" + countRightBracket);
    }
}
