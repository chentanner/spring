package com.sss.app.core.query.parsing;

import com.sss.app.core.query.parsing.operator.AssignmentOperator;
import com.sss.app.core.query.parsing.operator.ComparisonOperator;
import com.sss.app.core.query.parsing.operator.Operator;
import com.sss.app.core.query.parsing.token.CurlyBracket;
import com.sss.app.core.query.parsing.token.SquareBracket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasicTextReader {

    private static final Logger logger = LogManager.getLogger();
    public static final int IS_WHITESPACE = 0;
    public static final int IS_OPERATOR = 1;
    public static final int IS_SCALER = 3;
    public static final int IS_NAME = 4;
    public static final int IS_BRACKET = 5;
    public static final int IS_SQ_BRACKET = 6;
    public static final int IS_LETTER = 7;
    public static final int IS_QUOTE = 8;
    public static final int IS_SEPARATOR = 9;
    public static final int IS_CURLY_BRACKET = 10;
    public static final int IS_ASSIGNMENT_OPERATOR = 11;
    public static final int IS_COMPARISON_OPERATOR = 12;
    public static final int IS_BANG = 13;
    public static final int IS_ENCODED_FUNCTION = 15;
    public static final int IS_STRING_PARM = 20;
    public static final int IS_SCALER_PARM = 21;
    public static final int IS_PRICE_PROPERTY = 30;
    public static final int IS_QUANTITY_PROPERTY = 31;
    public static final int IS_COMMENT = 40;
    public static final int IS_UNKNOWN = 99;

    private static final char QUOTE = '\'';

    private final StringBuffer buffer;
    private int currentPos = 0;
    private final String text;

    public BasicTextReader(String text) {
        buffer = new StringBuffer(text);
        this.text = text;
    }

    public boolean hasNext() {
        return currentPos < text.length();
    }

    public char peek() {
        return buffer.charAt(currentPos);
    }

    public int getNextType() {
        if (peekIsSpace())
            return IS_WHITESPACE;
        if (peekIsSeparator())
            return IS_SEPARATOR;
        if (peekIsOperator())
            return IS_OPERATOR;
        if (peekIsAssignmentOperator())
            return IS_ASSIGNMENT_OPERATOR;
        if (peekIsComparisonOperator())
            return IS_COMPARISON_OPERATOR;
        if (peekIsBang())
            return IS_BANG;
        if (peekIsDigit())
            return IS_SCALER;
        if (peekIsEncodedStoredFunction())
            return IS_ENCODED_FUNCTION;
        if (peekIsLetter())
            return IS_LETTER;
        if (peekIsOpenSquareBracket())
            return IS_SQ_BRACKET;
        if (peekIsBracket())
            return IS_BRACKET;
        if (peekIsCurlyBracket())
            return IS_CURLY_BRACKET;
        if (peekIsQuote())
            return IS_QUOTE;
        if (peekIsStringParameter())
            return IS_STRING_PARM;
        if (peekIsDoubleParameter())
            return IS_SCALER_PARM;
        if (peekIsDealPriceParameter())
            return IS_PRICE_PROPERTY;
        if (peekIsDealQuantityParameter())
            return IS_QUANTITY_PROPERTY;
        if (peekIsComment())
            return IS_COMMENT;
        return IS_UNKNOWN;
    }

    private boolean peekIsDoubleParameter() {
        return buffer.charAt(currentPos) == '#';
    }

    private boolean peekIsStringParameter() {
        return buffer.charAt(currentPos) == '@';
    }

    private boolean peekIsDealPriceParameter() {
        return buffer.charAt(currentPos) == '$';
    }

    private boolean peekIsDollarSignParameter() {
        return buffer.charAt(currentPos) == '$';
    }

    private boolean peekIsComment() {
        return buffer.charAt(currentPos) == '^';
    }

    private boolean peekIsDealQuantityParameter() {
        return buffer.charAt(currentPos) == '~';
    }

    private boolean peekIsFloatFreqSeparator() {
        return buffer.charAt(currentPos) == ':';
    }

    private boolean peekIsOrdinalSeparator() {
        return buffer.charAt(currentPos) == ':';
    }

    private boolean peekIsComma() {
        return buffer.charAt(currentPos) == ',';
    }

    public char read() {
        return buffer.charAt(currentPos++);
    }

    public void skipWhiteSpaces() {
        while (peekIsSpace())
            read();
    }

    public void skip(int round) {
        for (int i = 0; i < round; i++) {
            read();
        }
    }

    public boolean peekIsSpace() {
        return Character.isWhitespace(buffer.charAt(currentPos));
    }

    public boolean peekIsQuote() {
        return QUOTE == (buffer.charAt(currentPos));
    }

    public static boolean firstCharIsOrdinal(String tokenIn) {
        if (tokenIn == null || tokenIn.length() == 0)
            return false;
        return Character.isDigit(tokenIn.charAt(0));
    }

    public boolean peekIsDigit() {
        return Character.isDigit(buffer.charAt(currentPos)) || peekIsPeriod();
    }

    public boolean peekIsEncodedStoredFunction() {
        return buffer.charAt(currentPos) == '_';
    }

    public boolean peekIsLetter() {
        return Character.isLetter(buffer.charAt(currentPos));
    }

    public boolean peekIsPeriod() {
        return (peek() == '.');
    }

    public boolean peekIsColon() {
        return (peek() == '.');
    }

    public boolean peekIsLegalNameSeparator() {
        char c = buffer.charAt(currentPos);
        return c == '/' || c == '-' || c == '.' || c == '_' || c == ' ';
    }

    public boolean peekIsUnderscoreSeparator() {
        char c = buffer.charAt(currentPos);
        return c == '_';
    }

    public boolean peekIsDateSeparator() {
        char c = buffer.charAt(currentPos);
        return c == '-';
    }

    public boolean peekIsTimeStampSeparator() {
        char c = buffer.charAt(currentPos);
        return c == ':';
    }

    public boolean peekIsOperator() {
        return (Operator.lookup(buffer.charAt(currentPos)) != null);
    }

    public boolean peekIsAssignmentOperator() {
        return AssignmentOperator.lookup(buffer.charAt(currentPos)) != null;
    }

    public boolean peekIsComparisonOperator() {
        return ComparisonOperator.lookup(buffer.charAt(currentPos)) != null;
    }

    public boolean peekIsBracket() {
        return (Bracket.lookup(buffer.charAt(currentPos)) != null);
    }

    public boolean peekIsOpenSquareBracket() {
        return (SquareBracket.isOpenBracket(buffer.charAt(currentPos)));
    }

    public boolean peekIsCurlyBracket() {
        return (CurlyBracket.isBracket(buffer.charAt(currentPos)));
    }

    public boolean peekIsSquareBracket() {
        return (SquareBracket.isBracket(buffer.charAt(currentPos)));
    }

    public boolean peekIsClosedSquareBracket() {
        return (SquareBracket.isCloseBracket(buffer.charAt(currentPos)));
    }

    public boolean peekIsOpenBracket() {
        Bracket bracket = Bracket.lookup(buffer.charAt(currentPos));
        if (bracket == null)
            return false;
        return bracket.equals(Bracket.OPEN);
    }

    public boolean peekIsCloseBracket() {
        Bracket bracket = Bracket.lookup(buffer.charAt(currentPos));
        if (bracket == null)
            return false;
        return bracket.equals(Bracket.CLOSE);
    }

    public char readSeparator() {
        return read();
    }

    public boolean peekIsSeparator() {
        return (',' == buffer.charAt(currentPos));
    }

    public boolean peekIsBang() {
        return ('!' == buffer.charAt(currentPos));
    }

    public String readStringToken() {
        StringBuilder fnName = new StringBuilder();

        while (hasNext()) {
            if (peekIsLetter()
                    || buffer.charAt(currentPos) == '_'
                    || Character.isDigit(buffer.charAt(currentPos))
                    || peekIsPeriod())
                fnName.append(read());
            else
                break;
        }
        return fnName.toString();
    }

    public String readDollarStringToken() {
        StringBuilder fnName = new StringBuilder();

        while (hasNext()) {
            if (peekIsDollarSignParameter()
                    || peekIsLetter()
                    || buffer.charAt(currentPos) == '_'
                    || Character.isDigit(
                    buffer.charAt(currentPos)))
                fnName.append(read());
            else
                break;
        }
        return fnName.toString();
    }

    public String readMultiValueToken() {
        StringBuilder fnName = new StringBuilder();

        while (hasNext()) {
            if (peekIsLetter()
                    || peekIsDigit()
                    || buffer.charAt(currentPos) == '_'
                    || peekIsLegalNameSeparator()
                    || peekIsColon()
            )
                fnName.append(read());
            else
                break;
        }
        return fnName.toString();
    }

    public String readComment() {
        StringBuilder comment = new StringBuilder();
        read();

        while (hasNext()) {
            comment.append(read());
        }
        return comment.toString();
    }

    public String readEncodedFunctionToken() {
        read(); // throw away "_"
        if (hasNext())
            read(); // throw away "f"
        StringBuilder fnName = new StringBuilder();

        while (hasNext()) {
            if (Character.isDigit(buffer.charAt(currentPos)))
                fnName.append(read());
            else
                break;
        }
        return fnName.toString();
    }

    /**
     * Read a date in the format of 'yyyy-mm-dd'
     */
    public String readDate() {
        StringBuilder name = new StringBuilder();
        while (hasNext()) {
            if (peekIsDigit() || peekIsDateSeparator())
                name.append(read());
            else
                break;
            if (peekIsQuote()) {
                read();
                break;
            }
        }
        return name.toString();
    }

    /**
     * Read a surrogate key encoded token in the form of [<type>:sk]
     */
    public String readSKEncodedToken() {
        StringBuilder token = new StringBuilder();
        read();
        while (hasNext()) {
            token.append(read());
            if (peekIsClosedSquareBracket()) {
                read();
                break;
            }
        }
        return token.toString();
    }

    /**
     * Assumes name is in the form quote [a-zA-Z0-9] followed by a quote
     */
    public String readQuotedToken() {
        if (peekIsQuote())
            read();

        StringBuilder name = new StringBuilder();
        while (hasNext()) {
            if (peekIsLetter()
                    || peekIsDigit()
                    || peekIsLegalNameSeparator()
                    || peekIsFloatFreqSeparator()
                    || peekIsSquareBracket())
                name.append(read());
            else
                break;
            if (peekIsQuote()) {
                read();
                break;
            }

        }
        return name.toString();
    }

    /**
     * Assumes name is in the form quote [a-zA-Z0-9] followed by a quote.
     * It also accepts any character in between the quotes including special
     * characters such as '"!@#$%^&*().`~|{}[]+-_/\?,<>;:'.
     * <p>
     * It also checks for escaped single quotes inside a single quoted string
     * and treats them as a character within the quote to handle quoted token
     * that contain single quotes (ex. 'o'mally').  The SAS standard for escaping
     * single quotes is 2 single quotes ('');
     */
    public String readExtendedQuotedToken() {
        if (peekIsQuote()) {
            read();
        }

        StringBuilder name = new StringBuilder();
        while (hasNext()) {
            //Read everything after the opening quote
            //until you encounter another quote
            if (peekIsQuote()) {
                read();
                if (!hasNext()) {
                    return name.toString();
                } else {
                    //If we run into another quote, check
                    //if its escaped (followed by another single quote)
                    //and if it is, ignore it.
                    if (peekIsQuote()) {
                        name.append(read());
                    } else {
                        break;
                    }
                }
            } else
                name.append(read());
        }

        return name.toString();
    }

    /**
     * Assumes that the number is in the form [0-9]+.[0-9]+ or simply [0-9]+
     */
    public String readDigits() {
        StringBuilder digits = new StringBuilder();
        while (hasNext()) {
            if (peekIsDigit() || peekIsPeriod()) {
                digits.append(read());
            } else {
                break;
            }
        }
        return digits.toString();
    }
}