/*
 * #%L
 * Native ARchive plugin for Maven
 * %%
 * Copyright (C) 2002 - 2014 NAR Maven Plugin developers.
 * %%
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
 * #L%
 */
package com.github.maven_nar.cpptasks.gcc.cross;

import java.io.File;

import org.apache.tools.ant.BuildException;

import com.github.maven_nar.cpptasks.CCTask;
import com.github.maven_nar.cpptasks.LinkerParam;
import com.github.maven_nar.cpptasks.compiler.CommandLineLinkerConfiguration;
import com.github.maven_nar.cpptasks.compiler.LinkType;
import com.github.maven_nar.cpptasks.compiler.Linker;
import com.github.maven_nar.cpptasks.gcc.AbstractArLibrarian;

/**
 * Adapter for the 'ar' archiver
 *
 * @author Adam Murdoch
 */
public final class GccLibrarian extends AbstractArLibrarian {
  private static String[] objFileExtensions = new String[] {
    ".o"
  };
  private static GccLibrarian instance = new GccLibrarian("ar", objFileExtensions, false, new GccLibrarian("ar",
      objFileExtensions, true, null));

  public static GccLibrarian getInstance() {
    return instance;
  }

  private GccLibrarian(final String command, final String[] inputExtensions, final boolean isLibtool,
      final GccLibrarian libtoolLibrarian) {
    super(command, "V", inputExtensions, new String[0], "lib", ".a", isLibtool, libtoolLibrarian);
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    final GccLibrarian clone = (GccLibrarian) super.clone();
    return clone;
  }

  @Override
  public Linker getLinker(final LinkType type) {
    return GccLinker.getInstance().getLinker(type);
  }

  @Override
  public void link(final CCTask task, final File outputFile, final String[] sourceFiles,
      final CommandLineLinkerConfiguration config) throws BuildException {
    try {
      final GccLibrarian clone = (GccLibrarian) this.clone();
      final LinkerParam param = config.getParam("target");
      if (param != null) {
        clone.setCommand(param.getValue() + "-" + this.getCommand());
      }
      clone.superlink(task, outputFile, sourceFiles, config);
    } catch (final CloneNotSupportedException e) {
      superlink(task, outputFile, sourceFiles, config);
    }
  }

  private void superlink(final CCTask task, final File outputFile, final String[] sourceFiles,
      final CommandLineLinkerConfiguration config) throws BuildException {
    super.link(task, outputFile, sourceFiles, config);
  }
}
