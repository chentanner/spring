package com.sss.app.core.query.parsing;

import com.sss.app.core.query.expressions.QueryCriteria;
import com.sss.app.core.query.propertymapper.QueryPropertyMapper;
import com.sss.app.dinos.model.Dino;
import com.sss.app.dinos.repository.DinoRepository;
import com.sss.app.test.TransactionalSpringTestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

//@WithMockUser
class QueryAsTextParserTest extends TransactionalSpringTestCase {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private QueryPropertyMapper queryPropertyMapper;

    @Override
    public void setUp() {

    }

    @Test
    public void parseWithReservedWordsFrom() {

        Dino dino = new Dino();
        String dinoName = "NewDino";
        dino.getDetail().setName(dinoName);
        dino.save();

        QueryAsTextParser<Dino> parser = new QueryAsTextParser<>(Dino.class);
        QueryCriteria<Dino> queryCriteria = parser.parse("WHERE name = '" + dinoName + "'");
        logger.error(queryCriteria.toExternalString());

        DinoRepository dinoRepository = getBean(DinoRepository.class);
        List<Dino> dinos = dinoRepository.find(queryCriteria);
        assertFalse(dinos.isEmpty());
    }

//    @Test
//    public void parseWithJoins() {
//
//        QueryAsTextParser parser = new QueryAsTextParser(QueryEntityName.LOCATION.getDomainName());
//        QueryCriteria queryCriteria = parser.parse("FROM Location JOIN geographicRegion gr WHERE gr.code = 'home'");
//        logger.error(queryCriteria.toExternalString());
//    }
//
//    @Test
//    public void parseWithLinkedJoins() {
//
//        QueryAsTextParser parser = new QueryAsTextParser(QueryEntityName.FLOAT_INDEX.getDomainName());
//        QueryCriteria queryCriteria = parser.parse(
//                "JOIN location loc, loc.geographicRegion gr WHERE gr.code = 'WESTERN'");
//        logger.error(queryCriteria.generateQueryForIds());
//
//        QueryPageSelection selection = queryService.executeIdQuery(queryCriteria);
//        assertEquals(4, selection.getSelectedIds().size());
//
//    }
//
//    @Test
//    public void parseWithJoinsOrderBy() {
//
//        QueryAsTextParser parser = new QueryAsTextParser(QueryEntityName.LOCATION.getDomainName());
//        QueryCriteria queryCriteria = parser.parse("JOIN geographicRegion gr ORDER BY gr.code");
//        logger.error(queryCriteria.toExternalString());
//        QueryPageSelection selection = queryService.executeIdQuery(queryCriteria);
//    }
//
//    @Test
//    public void parseWithJoinsOrderByBadPropertyFail() {
//
//        QueryAsTextParser parser = new QueryAsTextParser(QueryEntityName.LOCATION.getDomainName());
//        try {
//            QueryCriteria queryCriteria = parser.parse("JOIN geographicRegion gr ORDER BY gr.c");
//            fail("should have thrown exception");
//        } catch (ApplicationRuntimeException e) {
//            assertEquals(CoreTransactionErrorCode.INVALID_QUERYTEXT_MISSING_PROPERTY.getCode(), e.getErrorCode());
//            return;
//        }
//        fail("should have thrown exception");
//    }
//
//
//    @Test
//    public void parseWithMultipleJoins() {
//
//        QueryAsTextParser parser = new QueryAsTextParser(QueryEntityName.POSITION_CONFIGURATION.getDomainName());
//        QueryCriteria queryCriteria = parser.parse(
//                "JOIN currency cu, commodity co WHERE cu.code = 'CAD' AND co.code = 'NATGAS'");
//        logger.error(queryCriteria.toExternalString());
//        logger.error(queryCriteria.generateQueryForIds());
//        queryService.executeIdQuery(queryCriteria);
//    }
//
//    @Test
//    public void parseWithJoinAndOuterJoin() {
//
//        QueryAsTextParser parser = new QueryAsTextParser(QueryEntityName.POSITION_CONFIGURATION.getDomainName());
//        QueryCriteria queryCriteria = parser.parse(
//                "OUTER JOIN commodity co JOIN currency cu WHERE cu.code = 'CAD' AND co.code = 'NATGAS'");
//        logger.error(queryCriteria.toExternalString());
//        logger.error(queryCriteria.generateQueryForIds());
//        queryService.executeIdQuery(queryCriteria);
//    }
//
//    @Test
//    public void parseWithOuterJoins() {
//
//        QueryAsTextParser parser = new QueryAsTextParser(QueryEntityName.LOCATION.getDomainName());
//        QueryCriteria queryCriteria = parser.parse("FROM Location OUTER JOIN geographicRegion gr WHERE gr.code = 'home'");
//        logger.error(queryCriteria.toExternalString());
//        logger.error(queryCriteria.generateQueryForIds());
//        queryService.executeIdQuery(queryCriteria);
//    }
//
//
//    @Test
//    public void testParseToExpressionGroupSimple() {
//
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE dealNo = 7");
//
//        assertEquals(1, queryCriteria.getWhereClause().getExpressions().size());
//
//        logger.debug(queryCriteria.toFormattedString());
//
//        WhereExpression whereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertTrue(whereExpression.getPropertyValue() instanceof Integer);
//        assertEquals(queryPropertyMapper.getQueryMap("DealView").get("dealNo"), whereExpression.getQueryProperty());
//        assertEquals(ExpressionOperator.EQUALS, whereExpression.getOperator());
//
//        String externalizedText = queryCriteria.toExternalString();
//        parser = new QueryAsTextParser("DealView");
//        QueryCriteria criteria2 = parser.parse(externalizedText);
//
//        whereExpression = criteria2.getWhereClause().getWhereExpressions().get(0);
//        assertTrue(whereExpression.getPropertyValue() instanceof Integer);
//        assertEquals(queryPropertyMapper.getQueryMap("DealView").get("dealNo"), whereExpression.getQueryProperty());
//        assertEquals(ExpressionOperator.EQUALS, whereExpression.getOperator());
//    }
//
//    @Test
//    public void testParseInvalidDateFail() {
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//        try {
//            QueryCriteria queryCriteria = parser.parse("WHERE startDate = 2020-12-01");
//            logger.error(queryCriteria.toExternalString());
//            queryService.executeIdQuery(queryCriteria);
//            fail("Should have thrown exception");
//        } catch (ApplicationRuntimeException e) {
//            logger.error(e.getMessage());
//            assertEquals(CoreTransactionErrorCode.INVALID_QUERYTEXT_INVALID_WHERE.getCode(), e.getErrorCode());
//        } catch (Throwable t) {
//            logger.error(t.getMessage());
//            fail("Should have thrown exception");
//        }
//
//    }
//
//    @Test
//    public void testParseSingleQuotedDateFail() {
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//        try {
//            QueryCriteria queryCriteria = parser.parse("WHERE startDate = '2020-12-01");
//            logger.error(queryCriteria.toExternalString());
//            queryService.executeIdQuery(queryCriteria);
//            fail("Should have thrown exception");
//        } catch (ApplicationRuntimeException e) {
//            logger.error(e.getMessage());
//            assertEquals(CoreTransactionErrorCode.INVALID_QUERYTEXT_BAD_SYNTAX.getCode(), e.getErrorCode());
//            return;
//        }
//        fail("Failed to throw correct exception");
//    }
//
//
//    @Test
//    public void testParseRelativeDate() {
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE startDate = '{BEGOFDAY}'");
//        logger.error(queryCriteria.toExternalString());
//        QueryPageSelection selection = queryService.executeIdQuery(queryCriteria);
//        assertEquals(0, selection.getSelectedIds().size());
//    }
//
//
//    @Test
//    public void testParseToExpressionGroupTwoExpression() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE dealNo = 7 AND buySellCode = 'B'");
//        logger.debug(queryCriteria.toString());
//
//        assertEquals(3, queryCriteria.getWhereClause().getExpressions().size());
//
//        WhereExpression whereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertTrue(whereExpression.getPropertyValue() instanceof Integer);
//        assertEquals(queryPropertyMapper.getQueryMap("DealView").get("dealNo"), whereExpression.getQueryProperty());
//        assertEquals(ExpressionOperator.EQUALS, whereExpression.getOperator());
//
//        assertEquals("AND", queryCriteria.getWhereClause().getExpressions().get(1).toString());
//
//        WhereExpression whereExpression2 = queryCriteria.getWhereClause().getWhereExpressions().get(1);
//        assertEquals("B", whereExpression2.getPropertyValue().toString());
//        assertEquals(queryPropertyMapper.getQueryMap("DealView").get("buySellCode"),
//                     whereExpression2.getQueryProperty());
//        assertEquals(ExpressionOperator.EQUALS, whereExpression2.getOperator());
//
//
//        String externalizedText = queryCriteria.toExternalString();
//        parser = new QueryAsTextParser("DealView");
//        QueryCriteria criteria2 = parser.parse(externalizedText);
//
//        assertEquals(3, criteria2.getWhereClause().getExpressions().size());
//
//        whereExpression = criteria2.getWhereClause().getWhereExpressions().get(0);
//        assertTrue(whereExpression.getPropertyValue() instanceof Integer);
//        assertEquals(queryPropertyMapper.getQueryMap("DealView").get("dealNo"), whereExpression.getQueryProperty());
//        assertEquals(ExpressionOperator.EQUALS, whereExpression.getOperator());
//
//
//        assertEquals("AND", criteria2.getWhereClause().getExpressions().get(1).toString());
//
//        whereExpression2 = criteria2.getWhereClause().getWhereExpressions().get(1);
//        assertEquals("B", whereExpression2.getPropertyValue().toString());
//        assertEquals(queryPropertyMapper.getQueryMap("DealView").get("buySellCode"),
//                     whereExpression2.getQueryProperty());
//        assertEquals(ExpressionOperator.EQUALS, whereExpression2.getOperator());
//
//    }
//
//    @Test
//    public void testParseWithEnumCode() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE buySellCode = B");
//
//        logger.debug(queryCriteria.toString());
//
//        assertEquals(1, queryCriteria.getWhereClause().getExpressions().size());
//
//        WhereExpression whereExpression2 = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertEquals("B", whereExpression2.getPropertyValue().toString());
//        assertEquals(queryPropertyMapper.getQueryMap("DealView").get("buySellCode"),
//                     whereExpression2.getQueryProperty());
//        assertEquals(ExpressionOperator.EQUALS, whereExpression2.getOperator());
//    }
//
//    @Test
//    public void testParseWithEnumCodeAgreementWithSingleQuotes() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("AgreementView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE confirmationMethodCode eq 'DONOTCONFIRM' ");
//
//        logger.debug(queryCriteria.toString());
//
//        assertEquals(1, queryCriteria.getWhereClause().getExpressions().size());
//
//        WhereExpression whereExpression2 = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertEquals(queryPropertyMapper.getQueryMap("AgreementView").get("confirmationMethodCode"),
//                     whereExpression2.getQueryProperty());
//        assertEquals(ExpressionOperator.EQUALS, whereExpression2.getOperator());
//        assertEquals("DONOTCONFIRM", whereExpression2.getPropertyValue());
//    }
//
//
//    @Test
//    public void testParseWithEnumCodeAgreementWithLabelHasSpaces() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("AgreementView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE confirmationMethodCode eq 'Do Not Confirm' ");
//
//        logger.debug(queryCriteria.toString());
//
//        assertEquals(1, queryCriteria.getWhereClause().getExpressions().size());
//
//        WhereExpression whereExpression2 = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertEquals(queryPropertyMapper.getQueryMap("AgreementView").get("confirmationMethodCode"),
//                     whereExpression2.getQueryProperty());
//        assertEquals(ExpressionOperator.EQUALS, whereExpression2.getOperator());
//        assertEquals("DONOTCONFIRM", whereExpression2.getPropertyValue());
//    }
//
//
//    @Test
//    public void testParseWithEnumCodeAgreement() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("AgreementView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE confirmationMethodCode eq DONOTCONFIRM ");
//
//        logger.debug(queryCriteria.toString());
//
//        assertEquals(1, queryCriteria.getWhereClause().getExpressions().size());
//
//        WhereExpression whereExpression2 = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertEquals(queryPropertyMapper.getQueryMap("AgreementView").get("confirmationMethodCode"),
//                     whereExpression2.getQueryProperty());
//        assertEquals(ExpressionOperator.EQUALS, whereExpression2.getOperator());
//        assertEquals("DONOTCONFIRM", whereExpression2.getPropertyValue());
//    }
//
//
//    @Test
//    public void testParseWithEnumName() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE buySellCode = Buy");
//
//        logger.debug(queryCriteria.toString());
//
//        assertEquals(1, queryCriteria.getWhereClause().getExpressions().size());
//
//        WhereExpression whereExpression2 = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertEquals("B", whereExpression2.getPropertyValue().toString());
//        assertEquals(queryPropertyMapper.getQueryMap("DealView").get("buySellCode"),
//                     whereExpression2.getQueryProperty());
//        assertEquals(ExpressionOperator.EQUALS, whereExpression2.getOperator());
//    }
//
//
//    @Test
//    public void testParseInOperatorWithEnumName() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE buySellCode in (Buy,Sell)");
//        logger.debug(queryCriteria.toString());
//
//        assertEquals(1, queryCriteria.getWhereClause().getExpressions().size());
//
//        WhereExpression whereExpression2 = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertTrue(whereExpression2.getPropertyValue() instanceof List);
//        List<String> values = (List<String>) whereExpression2.getPropertyValue();
//        assertEquals("B", values.get(0));
//        assertEquals(queryPropertyMapper.getQueryMap("DealView").get("buySellCode"),
//                     whereExpression2.getQueryProperty());
//        assertEquals(ExpressionOperator.IN, whereExpression2.getOperator());
//
//    }
//
//
//    @Test
//    public void testParseToExpressionGroupTwoExpressionWithExternalOperator() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE dealNo eq 7 AND buySellCode ne 'B'");
//        logger.error(queryCriteria.toString());
//        assertEquals(3, queryCriteria.getWhereClause().getExpressions().size());
//
//        WhereExpression whereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertTrue(whereExpression.getPropertyValue() instanceof Integer);
//        assertEquals(queryPropertyMapper.getQueryMap("DealView").get("dealNo"), whereExpression.getQueryProperty());
//        assertEquals(ExpressionOperator.EQUALS, whereExpression.getOperator());
//
//        assertEquals("AND", queryCriteria.getWhereClause().getExpressions().get(1).toString());
//
//        WhereExpression whereExpression2 = queryCriteria.getWhereClause().getWhereExpressions().get(1);
//        assertEquals("B", whereExpression2.getPropertyValue().toString());
//        assertEquals(queryPropertyMapper.getQueryMap("DealView").get("buySellCode"),
//                     whereExpression2.getQueryProperty());
//        assertEquals(ExpressionOperator.NOT_EQUALS, whereExpression2.getOperator());
//
//    }
//
//
//    @Test
//    public void testParseToQueryCriteriaSimpleWithFrom() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("FROM DealView WHERE dealNo = 7");
//        assertEquals(1, queryCriteria.getWhereClause().getWhereExpressions().size());
//        logger.debug(queryCriteria.toString());
//    }
//
//    @Test
//    public void testParseToQueryCriteriaWithJustWhere() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE ");
//        assertEquals(0, queryCriteria.getWhereClause().getWhereExpressions().size());
//        logger.debug(queryCriteria.toString());
//    }
//
//    @Test
//    public void testParseToQueryCriteriaSimpleWithFromOrderBy() {
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("FROM DealView WHERE dealNo = 7 ORDER BY dealNo,startDate");
//        assertEquals(1, queryCriteria.getWhereClause().getWhereExpressions().size());
//        assertEquals(2, queryCriteria.getOrderClause().getOrderExpressions().size());
//    }
//
//
//    @Test
//    public void testParseToQueryCriteriaSimpleWithFromOrderByComplex() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("FROM DealView WHERE dealNo = 7 ORDER BY dealNo ASC, startDate DESC");
//        assertEquals(1, queryCriteria.getWhereClause().getWhereExpressions().size());
//        assertEquals(2, queryCriteria.getOrderClause().getOrderExpressions().size());
//    }
//
//    @Test
//    public void testParseToQueryCriteriaTwoExpressions() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE dealNo = 7 AND buySellCode = 'B'");
//        logger.error(queryCriteria.toString());
//    }
//
//    @Test
//    public void testParseToQueryCriteriaWithQuoteMismatch() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        try {
//            QueryCriteria queryCriteria = parser.parse("WHERE startDate = '2020-10-10 AND buySellCode = 'B'");
//            fail("Should have thrown exception");
//        } catch (ApplicationRuntimeException f) {
//            logger.debug(f.toString());
//            return;
//        }
//        fail("Should have thrown exception");
//    }
//
//
//    @Test
//    public void testParseToQueryCriteriaTwoExpressionWithGroups() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE dealNo = 7 AND (buySellCode = 'B')");
//        assertEquals(5, queryCriteria.getWhereClause().getExpressions().size());
//    }
//
//
//    @Test
//    public void testParseToQueryCriteriaTwoExpressionWithThreeGroups() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse(
//                "WHERE (dealNo = 7) AND ((buySellCode = 'B') OR startDate > '2018-01-01')");
//        assertEquals(3, queryCriteria.getWhereClause().getWhereExpressions().size());
//        ExpressionElement element = queryCriteria.getWhereClause().getExpressions().get(5);
//        assertTrue(element instanceof BracketElement);
//        Bracket bracket = ((BracketElement) element).getBracket();
//        assertEquals(Bracket.OPEN, bracket);
//    }
//
//
//    @Test
//    public void testInOperator() {
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE dealNo in (1, 2, 3)");
//        WhereExpression whereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertEquals(ExpressionOperator.IN, whereExpression.getOperator());
//        assertTrue(whereExpression.getPropertyValue() instanceof List);
//
//        String externalText = queryCriteria.toExternalString();
//        queryCriteria = parser.parse(externalText);
//        whereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertEquals(ExpressionOperator.IN, whereExpression.getOperator());
//        assertTrue(whereExpression.getPropertyValue() instanceof List);
//    }
//
//
//    @Test
//    public void testLikeOperator() {
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE ticketNo like 'g%'");
//        WhereExpression whereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertEquals(ExpressionOperator.LIKE, whereExpression.getOperator());
//    }
//
//
//    @Test
//    public void testParseToQueryCriteriaNotEqual() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE dealNo != 7");
//        WhereExpression whereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertEquals(ExpressionOperator.NOT_EQUALS, whereExpression.getOperator());
//    }
//
//    @Test
//    public void testParseToQueryCriteriaIncompleteOperator() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        try {
//            QueryCriteria queryCriteria = parser.parse("WHERE dealNo e 7");
//            fail("Should have failed on an exception");
//        } catch (ApplicationRuntimeException s) {
//            assertEquals(CoreTransactionErrorCode.INVALID_QUERYTEXT_BAD_SYNTAX.getCode(), s.getErrorCode());
//            return;
//        }
//        fail("Should have failed on an exception");
//    }
//
//    @Test
//    public void testParseToQueryCriteriaCommodityEnumsAsLabel() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("Commodity");
//        QueryCriteria queryCriteria = parser.parse("WHERE lowestPositionFrequencyType = 'Monthly'");
//        WhereExpression whereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertEquals("M", whereExpression.getPropertyValue());
//    }
//
//    @Test
//    public void testParseToQueryCriteriaCommodityEnumsAsCode() {
//
//        QueryAsTextParser parser = new QueryAsTextParser("Commodity");
//        QueryCriteria queryCriteria = parser.parse("WHERE lowestPositionFrequencyType = 'M'");
//        WhereExpression whereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        assertEquals("M", whereExpression.getPropertyValue());
//    }
//
//
//    @Test
//    public void testWithinMthOperator() {
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE dealDate withinmth '2020-01-04'");
//
//        assertEquals(5, queryCriteria.getWhereClause().getExpressions().size());
//
//        WhereExpression whereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        Date valueDate = (Date) whereExpression.getPropertyValue();
//        assertDateEquals(DateUtils.createDate(2020, 1, 1), valueDate);
//
//        WhereExpression secondWhereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(1);
//        valueDate = (Date) secondWhereExpression.getPropertyValue();
//        assertDateEquals(DateUtils.createDate(2020, 1, 31), valueDate);
//
//
//        List<DealView> views = DealView.fetchDealViews(queryCriteria);
//
//    }
//
//
//    @Test
//    public void testWithinYearOperator() {
//        QueryAsTextParser parser = new QueryAsTextParser("DealView");
//
//        QueryCriteria queryCriteria = parser.parse("WHERE dealDate withinyear '2020-04-01'");
//
//        assertEquals(5, queryCriteria.getWhereClause().getExpressions().size());
//
//        WhereExpression whereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(0);
//        Date valueDate = (Date) whereExpression.getPropertyValue();
//        assertDateEquals(DateUtils.createDate(2020, 1, 1), valueDate);
//
//        WhereExpression secondWhereExpression = queryCriteria.getWhereClause().getWhereExpressions().get(1);
//        valueDate = (Date) secondWhereExpression.getPropertyValue();
//        assertDateEquals(DateUtils.createDate(2020, 12, 31), valueDate);
//
//
//        List<DealView> views = DealView.fetchDealViews(queryCriteria);
//
//    }


}
