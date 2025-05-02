package io.devchw.example.support.restdocs.enums

import io.devchw.example.support.apidocs.ApiDocumentEnum

interface EnumPopupLinkGenerator {
    fun generateEnumPopupLink(documentEnumClass: Class<out Enum<*>?>): String {
        return String.format(
            "link:docs/enums/%s.html[%s,role=\"popup\"]",
            documentEnumClass.simpleName,
            documentEnumClass.getAnnotation(
                ApiDocumentEnum::class.java
            ).description
        )
    }
}