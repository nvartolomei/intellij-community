// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.codeInsight.completion.ml

import com.intellij.internal.ml.catboost.CatBoostJarCompletionModelProvider
import com.intellij.java.JavaBundle
import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage

class JavaMLRankingProvider : CatBoostJarCompletionModelProvider(JavaBundle.message("settings.completion.ml.java.display.name"),
                                                                 "java_features2", "java_model2") {

  override fun isLanguageSupported(language: Language): Boolean = JavaLanguage.INSTANCE == language

  override fun isEnabledByDefault(): Boolean {
    return true
  }
}