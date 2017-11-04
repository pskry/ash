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

package org.ashlang.ash.type;

import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.Map;

import static org.ashlang.ash.type.Operator.*;
import static org.ashlang.ash.type.Type.I32;
import static org.ashlang.ash.type.Type.INVALID;

public class OperatorMap {

    private final Map<Triple<Type, Operator, Type>, Type> opMap;

    public OperatorMap() {
        opMap = new HashMap<>();
        operation(I32, ADD, I32).resultsIn(I32);
        operation(I32, SUB, I32).resultsIn(I32);
        operation(I32, MUL, I32).resultsIn(I32);
        operation(I32, DIV, I32).resultsIn(I32);
        operation(I32, MOD, I32).resultsIn(I32);
    }

    public Type getResultOf(Type left, Operator op, Type right) {
        Triple<Type, Operator, Type> key = Triple.of(left, op, right);
        return opMap.getOrDefault(key, INVALID);
    }

    private EntryBuilder operation(Type left, Operator op, Type right) {
        return new EntryBuilder(opMap, left, op, right);
    }

    private static class EntryBuilder {
        private final Map<Triple<Type, Operator, Type>, Type> opMap;
        private final Type left;
        private final Operator op;
        private final Type right;

        private EntryBuilder(Map<Triple<Type, Operator, Type>, Type> opMap,
                             Type left, Operator op, Type right) {
            this.opMap = opMap;
            this.left = left;
            this.op = op;
            this.right = right;
        }

        private void resultsIn(Type result) {
            opMap.put(Triple.of(left, op, right), result);
        }

    }

}
