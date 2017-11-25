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

package org.ashlang.ash.symbol;

import org.ashlang.ash.ast.ASTNode;
import org.ashlang.ash.ast.DeclarationNode;
import org.ashlang.ash.ast.FuncDeclarationNode;

import java.util.*;
import java.util.stream.Collectors;

public class SymbolTable {

    private final Deque<Scope> scopeStack;
    private final Map<ASTNode, SymbolTableSnapshot> snapshotMap;

    public SymbolTable() {
        scopeStack = new ArrayDeque<>();
        snapshotMap = new HashMap<>();
    }

    //region scope / memo

    public SymbolTableSnapshot recall(ASTNode declaringScope) {
        SymbolTableSnapshot snapshot = null;

        while (snapshot == null) {
            snapshot = snapshotMap.get(declaringScope);
            declaringScope = declaringScope.getParent();
        }

        return snapshot;
    }

    public void pushScope() {
        scopeStack.push(new Scope());
    }

    public void popScope(ASTNode declaringScope) {
        if (snapshotMap.containsKey(declaringScope)) {
            throw new IllegalArgumentException();
        }

        snapshotMap.put(declaringScope, new SymbolTableSnapshot(scopeStack));

        scopeStack.pop();
    }

    //endregion scope / memo

    //region symbol

    public Symbol declareSymbol(DeclarationNode declSite) {
        return currentScope().declareSymbol(declSite);
    }

    public Symbol getDeclaredSymbol(DeclarationNode declSite) {
        for (Scope scope : scopeStack) {
            Symbol declaredSymbol = scope.getDeclaredSymbol(declSite);
            if (declaredSymbol != null) {
                return declaredSymbol;
            }
        }

        return null;
    }

    public Symbol getDeclaredSymbol(String identifier) {
        for (Scope scope : scopeStack) {
            Symbol declaredSymbol = scope.getDeclaredSymbol(identifier);
            if (declaredSymbol != null) {
                return declaredSymbol;
            }
        }

        return null;
    }

    public Collection<Symbol> getDeclaredSymbolsInCurrentScope() {
        return currentScope().getDeclaredSymbols();
    }

    public Collection<Symbol> getDeclaredSymbols() {
        return scopeStack.stream()
            .flatMap(scope -> scope.getDeclaredSymbols().stream())
            .collect(Collectors.toList());
    }

    //endregion symbol

    //region function

    public Function declareFunction(FuncDeclarationNode declSite) {
        return currentScope().declareFunction(declSite);
    }

    public Function getDeclaredFunction(FuncDeclarationNode declSite) {
        for (Scope scope : scopeStack) {
            Function declaredFunction = scope.getDeclaredFunction(declSite);
            if (declaredFunction != null) {
                return declaredFunction;
            }
        }

        return null;
    }

    public Function getDeclaredFunction(String identifier) {
        for (Scope scope : scopeStack) {
            Function declaredFunction = scope.getDeclaredFunction(identifier);
            if (declaredFunction != null) {
                return declaredFunction;
            }
        }

        return null;
    }

    public Collection<Function> getDeclaredFunctions() {
        return scopeStack.stream()
            .flatMap(scope -> scope.getDeclaredFunctions().stream())
            .collect(Collectors.toList());
    }

    //endregion function

    private Scope currentScope() {
        return scopeStack.peek();
    }

}
