package com.sss.app.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.sss.app.core.codes.service.EnumCodeService;
import com.sss.app.core.codes.snapshot.CodeItem;
import com.sss.app.core.entity.managers.ApplicationContextFactory;
import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.IOException;

@Configurable
public class CodeItemSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private static final Logger logger = LogManager.getLogger(CodeItemSerializer.class);

    private String codeFamily = null;
    private String injectedPropertyName = null;

    public final static String MISSING_ANNOTATION =
            "Consider adding an @InjectCodeItem annotation where you have used" +
                    " the \"@JsonSerialize(using = CodeItemSerializer.class)\" annotation.  " +
                    "The two work in conjunction.";

    @Override
    public JsonSerializer<String> createContextual(
            SerializerProvider serializerProvider,
            BeanProperty property
    ) {
        if (property != null) {
            InjectCodeItem annotation = property.getAnnotation(InjectCodeItem.class);
            if (annotation != null) {
                codeFamily = annotation.codeFamily();
                injectedPropertyName = annotation.injectedCodeItemPropertyName();
            }
        }

        return this;
    }

    @Override
    public void serialize(
            String code,
            JsonGenerator jsonGenerator,
            SerializerProvider provider
    ) throws IOException {
        jsonGenerator.writeString(code);

        if (codeFamily == null
                || codeFamily.trim().isEmpty()
                || injectedPropertyName == null
                || injectedPropertyName.trim().isEmpty()) {
            throw new ApplicationRuntimeException(
                    TransactionErrorCode.JSON_CODE_ITEM_SERIALIZATION,
                    MISSING_ANNOTATION
            );
        }

        CodeItem codeItem = getLookupCodeService().fetchCodeItem(codeFamily, code);

        if (codeItem == null) {
            logger.warn("Code '" + code + "' was not found in code family '" + codeFamily + "'.");
            codeItem = new CodeItem(
                    code,
                    code,
                    createLabel(code, codeFamily)
            );
        }

        jsonGenerator.writeObjectField(injectedPropertyName, codeItem);
    }

    private static EnumCodeService getLookupCodeService() {
        return ApplicationContextFactory.getBean(EnumCodeService.class);
    }

    public static String createLabel(
            String code,
            String codeFamily
    ) {
        return "<<'" + code + "' IS MISSING FROM '" + codeFamily + "'>>";
    }

}