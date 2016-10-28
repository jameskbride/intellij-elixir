package org.elixir_lang.mix.runner.exunit;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.elixir_lang.psi.ElixirFile;
import org.jetbrains.annotations.NotNull;

public class MixExUnitRunConfigurationProducer extends RunConfigurationProducer<MixExUnitRunConfiguration> {
  public MixExUnitRunConfigurationProducer() {
    super(MixExUnitRunConfigurationType.getInstance());
  }

  @Override
  protected final boolean setupConfigurationFromContext(MixExUnitRunConfiguration runConfig, ConfigurationContext context, Ref<PsiElement> ref) {
    PsiElement location = ref.get();
    return location != null && location.isValid() &&
      setupConfigurationFromContextImpl(runConfig, context, location);
  }

  protected boolean setupConfigurationFromContextImpl(@NotNull MixExUnitRunConfiguration configuration,
                                                      @NotNull ConfigurationContext context,
                                                      @NotNull PsiElement psiElement) {
    if (psiElement instanceof PsiDirectory) {
      PsiDirectory dir = (PsiDirectory) psiElement;
      configuration.setName(configurationName(dir));
      configuration.setMixTestArgs(dir.getVirtualFile().getPath());
      return true;
    } else {
      PsiFile containingFile = psiElement.getContainingFile();
      if (!(containingFile instanceof ElixirFile || containingFile instanceof PsiDirectory)) return false;

      int lineNumber = lineNumber(psiElement);
      configuration.setName(configurationName(containingFile, lineNumber));
      configuration.setMixTestArgs(mixTestArgs(containingFile, lineNumber));

      return true;
    }
  }

  @Override
  public final boolean isConfigurationFromContext(MixExUnitRunConfiguration runConfig, ConfigurationContext context) {
    PsiElement location = context.getPsiLocation();
    return location != null && location.isValid() &&
      isConfigurationFromContextImpl(runConfig, context, location);
  }

  public boolean isConfigurationFromContextImpl(@NotNull MixExUnitRunConfiguration configuration,
                                                @NotNull ConfigurationContext context,
                                                @NotNull PsiElement psiElement) {
    PsiFile containingFile = psiElement.getContainingFile();
    VirtualFile vFile = containingFile != null ? containingFile.getVirtualFile() : null;
    if (vFile == null) return false;

    int lineNumber = lineNumber(psiElement);
    return StringUtil.equals(configuration.getName(), configurationName(containingFile, lineNumber)) &&
        StringUtil.equals(configuration.getMixTestArgs(), mixTestArgs(containingFile, lineNumber));
  }

  private int lineNumber(@NotNull PsiElement psiElement) {
    PsiFile containingFile = psiElement.getContainingFile();
    Project project = containingFile.getProject();
    PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
    Document document = psiDocumentManager.getDocument(containingFile);
    int textOffset = psiElement.getTextOffset();
    int lineNumber = document.getLineNumber(textOffset);
    if (lineNumber == 0) {
      return -1;
    } else {
      return lineNumber + 1;
    }
  }

  private String configurationName(PsiFileSystemItem file) {
    return "Mix ExUnit " + file.getName();
  }

  private String configurationName(PsiFileSystemItem file, int lineNumber) {
    if (lineNumber == -1) {
      return configurationName(file);
    } else {
      return configurationName(file) + ":" + lineNumber;
    }
  }

  private String mixTestArgs(PsiFileSystemItem file, int lineNumber) {
    String path = file.getVirtualFile().getPath();
    if (lineNumber == -1) {
      return path;
    } else {
      return path + ":" + lineNumber;
    }
  }

}
