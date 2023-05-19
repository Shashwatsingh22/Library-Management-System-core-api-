package com.lms.commons.web

import com.lms.commons.annotations.AuthorizedLibrarian
import com.lms.commons.annotations.SkipAuthentication
import java.util.function.Predicate

/**
 * @author Shashwat Singh
 *
 */

enum class ResourceType(val pathPrefixes: List<String>? = null) {
    SWAGGER(listOf("swagger-ui", "webjars", "api-doc")),
    OPEN,
    USER_AUTHENTICATED,
    AUTHORIZED_LIBRARIAN,
    ACTUATOR(listOf("actuator"));

    companion object {

        /**
         * @param url without context-path: ex /ping instead of /v1/ping
         * @param methodAnnotationChecker Predicate to validate if method contains the annotation
         * @param classAnnotationChecker Predicate to validate if the class contains the annotation
         */
        fun getResourceType(
            url: String,
            methodAnnotationChecker: Predicate<Class<out Annotation>>,
            classAnnotationChecker: Predicate<Class<out Annotation>>
        ): ResourceType {
            if (SWAGGER.pathPrefixes?.any { url.startsWith("/$it") } == true) {
                return SWAGGER
            }

            if (ACTUATOR.pathPrefixes?.any { url.startsWith("/$it") } == true) {
                return ACTUATOR
            }

            if (classAnnotationChecker.test(SkipAuthentication::class.java) || methodAnnotationChecker.test(
                    SkipAuthentication::class.java)) {
                return OPEN
            }

            if (classAnnotationChecker.test(AuthorizedLibrarian::class.java) || methodAnnotationChecker.test(
                    AuthorizedLibrarian::class.java)) {
                return AUTHORIZED_LIBRARIAN
            }

            return USER_AUTHENTICATED
        }
    }
}