/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.ide.projectView.impl.nodes;

import com.intellij.ide.util.treeView.TreeAnchorizer;
import com.intellij.psi.PsiAnchor;
import com.intellij.psi.PsiElement;

/**
 * @author peter
 */
public class PsiTreeAnchorizer extends TreeAnchorizer {

  @Override
  public Object createAnchor(Object element) {
    if (element instanceof PsiElement) {
      return PsiAnchor.create((PsiElement)element);
    }

    return super.createAnchor(element);
  }

  @Override
  public Object retrieveElement(Object pointer) {
    if (pointer instanceof PsiAnchor) {
      return ((PsiAnchor)pointer).retrieve();
    }

    return super.retrieveElement(pointer);
  }
}
