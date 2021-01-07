// This is a generated file. Not intended for manual editing.
package org.elixir_lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.ericsson.otp.erlang.OtpErlangObject;

public interface ElixirInterpolatedWordsHeredocLine extends HeredocLine {

  @Nullable
  ElixirEscapedEOL getEscapedEOL();

  @NotNull
  ElixirHeredocLinePrefix getHeredocLinePrefix();

  @NotNull
  ElixirInterpolatedWordsHeredocLineBody getInterpolatedWordsHeredocLineBody();

  Body getBody();

  @NotNull
  OtpErlangObject quote(@NotNull Heredoc heredoc, int prefixLength);

}
