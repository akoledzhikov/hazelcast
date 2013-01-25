/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.query.impl;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class MultiResultSet extends AbstractSet<QueryableEntry> {
    private final List<ConcurrentMap<Object, QueryableEntry>> resultSets = new ArrayList<ConcurrentMap<Object, QueryableEntry>>();

    public MultiResultSet() {
    }

    public void addResultSet(ConcurrentMap<Object, QueryableEntry> resultSet) {
        resultSets.add(resultSet);
    }

    @Override
    public boolean contains(Object o) {
        QueryableEntry entry = (QueryableEntry) o;
        for (ConcurrentMap<Object, QueryableEntry> resultSet : resultSets) {
            if (resultSet.containsKey(entry.getKeyData())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<QueryableEntry> iterator() {
        return new It();
    }

    class It implements Iterator<QueryableEntry> {
        int currentIndex = 0;
        Iterator<QueryableEntry> currentIterator;

        public boolean hasNext() {
            if (resultSets.size() == 0) return false;
            if (currentIterator != null && currentIterator.hasNext()) {
                return true;
            }
            while (currentIndex < resultSets.size()) {
                currentIterator = resultSets.get(currentIndex++).values().iterator();
                if (currentIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        public QueryableEntry next() {
            if (resultSets.size() == 0) return null;
            return currentIterator.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean add(QueryableEntry obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        int size = 0;
        for (ConcurrentMap<Object, QueryableEntry> resultSet : resultSets) {
            size += resultSet.size();
        }
        return size;
    }
}