package com.lms.commons.annotations

/**
 * @author Shashwat Singh
 * This used on those controller methods where only Librarian allowed to Access it.
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class AuthorizedLibrarian
