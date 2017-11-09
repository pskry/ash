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

package org.ashlang.ash.pass;

import org.ashlang.ash.ast.ASTBaseVisitor;
import org.ashlang.ash.ast.VarDeclarationNode;
import org.ashlang.ash.err.ErrorHandler;

import static org.ashlang.ash.type.Types.INVALID;

class TypeCheckVisitor extends ASTBaseVisitor<Void, Void> {

    private final ErrorHandler errorHandler;

    TypeCheckVisitor(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public Void
    visitVarDeclarationNode(VarDeclarationNode node, Void argument) {
        if (INVALID.equals(node.getType())) {
            errorHandler.emitInvalidType(node.getTypeToken());
        }
        return null;
    }

}
