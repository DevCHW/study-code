package com.example.restdocs.support;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

public class RestDocsUtils {

    public static OperationRequestPreprocessor requestPreprocessor() {
        return Preprocessors.preprocessRequest(
                Preprocessors.modifyUris()
                        .scheme("https")
                        .host("example.restdocs.com")
                        .removePort(),
                Preprocessors.prettyPrint());
    }

    public static OperationResponsePreprocessor responsePreprocessor() {
        return Preprocessors.preprocessResponse(Preprocessors.prettyPrint());
    }

}
