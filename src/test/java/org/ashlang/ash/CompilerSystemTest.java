/*
 * The Ash Project
 * Copyright (C) 2017  Peter Skrypalle
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License only.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.ashlang.ash;

import org.ashlang.ash.ast.ASTNode;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class CompilerSystemTest {

    @DataProvider(parallel = true)
    public Object[][] provideAshSourceAndExpectedResultString() {
        return new Object[][]{
            {"0", "0"},
            {"1", "1"},
            {"2147483647", "2147483647"},
        };
    }

    @Test(dataProvider = "provideAshSourceAndExpectedResultString")
    public void java8_target(String input, String expected) {
        IOUtil.executeInTempDir(tmpDir -> {
            // Act
            ASTNode rootNode = AshMain.buildAST(input);
            String mainClassName = AshMain.compileToJVM(rootNode, tmpDir);

            // Assert
            ExecResult java = IOUtil.exec(
                "java", "-classpath", tmpDir, mainClassName);

            assertThat(java.getErr()).isEmpty();
            assertThat(java.getOut()).isEqualTo(expected);
            assertThat(java.getExitCode()).isZero();
        });
    }

    @Test(dataProvider = "provideAshSourceAndExpectedResultString")
    public void c11_target(String input, String expected) {
        IOUtil.executeInTempDir(tmpDir -> {
            // Act
            ASTNode rootNode = AshMain.buildAST(input);
            Path outFile = tmpDir.resolve("out");
            AshMain.compileToNative(rootNode, outFile);

            // Assert
            ExecResult run = IOUtil.exec(outFile);

            assertThat(run.getErr()).isEmpty();
            assertThat(run.getOut()).isEqualTo(expected);
            assertThat(run.getExitCode()).isZero();
        });
    }

}