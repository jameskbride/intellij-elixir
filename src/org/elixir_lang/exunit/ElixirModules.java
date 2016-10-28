package org.elixir_lang.exunit;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.ResourceUtil;
import com.intellij.util.io.URLUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;

public class ElixirModules {
  private ElixirModules() {
  }

  public static final String FORMATTER_FILE_NAME = "team_city_ex_unit_formatter.ex";
  public static final String MIX_TASK_FILE_NAME = "test_with_formatter.ex";

  private static File putFile(@NotNull String fileName, @NotNull File directory) throws IOException {
    URL moduleUrl = ResourceUtil.getResource(ElixirModules.class, "/exunit", fileName);
    if (moduleUrl == null) {
      throw new IOException("Failed to locate Elixir module " + fileName);
    }

    try (BufferedInputStream inputStream = new BufferedInputStream(URLUtil.openStream(moduleUrl))) {
      File file = new File(directory, fileName);
      try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
        FileUtil.copy(inputStream, outputStream);
        return file;
      }
    }
  }

  public static File putFormatterTo(@NotNull File directory) throws IOException {
    return putFile(FORMATTER_FILE_NAME, directory);
  }

  public static File putMixTaskTo(@NotNull File directory) throws IOException {
    return putFile(MIX_TASK_FILE_NAME, directory);
  }
}
