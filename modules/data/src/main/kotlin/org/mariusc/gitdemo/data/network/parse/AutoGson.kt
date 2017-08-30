package org.mariusc.gitdemo.data.network.parse

import com.google.auto.value.AutoValue


/**
 * Marks an [@AutoValue][AutoValue]-annotated type for proper Gson serialization.
 *
 *
 * This annotation is needed because the [retention][Retention] of `@AutoValue`
 * does not allow reflection at runtime.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoGson