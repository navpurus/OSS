/*
 * Copyright (C) 2007 The Guava Authors
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

package org.glassfish.jersey.internal.guava;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.glassfish.jersey.internal.guava.Preconditions.checkNotNull;

/**
 * Implementation of {@code Multimap} whose keys and values are ordered by
 * their natural ordering or by supplied comparators. In all cases, this
 * implementation uses {@link Comparable#compareTo} or {@link
 * Comparator#compare} instead of {@link Object#equals} to determine
 * equivalence of instances.
 * <p>
 * <p><b>Warning:</b> The comparators or comparables used must be <i>consistent
 * with equals</i> as explained by the {@link Comparable} class specification.
 * Otherwise, the resulting multiset will violate the general contract of {@link
 * SetMultimap}, which it is specified in terms of {@link Object#equals}.
 * <p>
 * <p>The collections returned by {@code keySet} and {@code asMap} iterate
 * through the keys according to the key comparator ordering or the natural
 * ordering of the keys. Similarly, {@code get}, {@code removeAll}, and {@code
 * replaceValues} return collections that iterate through the values according
 * to the value comparator ordering or the natural ordering of the values. The
 * collections generated by {@code entries}, {@code keys}, and {@code values}
 * iterate across the keys according to the above key ordering, and for each
 * key they iterate across the values according to the value ordering.
 * <p>
 * <p>The multimap does not store duplicate key-value pairs. Adding a new
 * key-value pair equal to an existing key-value pair has no effect.
 * <p>
 * <p>Null keys and values are permitted (provided, of course, that the
 * respective comparators support them). All optional multimap methods are
 * supported, and all returned views are modifiable.
 * <p>
 * <p>This class is not threadsafe when any concurrent operations update the
 * multimap. Concurrent read operations will work correctly. To allow concurrent
 * update operations, wrap your multimap with a call to {@link
 * Multimaps#synchronizedSortedSetMultimap}.
 * <p>
 * <p>See the Guava User Guide article on <a href=
 * "http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Multimap">
 * {@code Multimap}</a>.
 *
 * @author Jared Levy
 * @author Louis Wasserman
 * @since 2.0 (imported from Google Collections Library)
 */
public class TreeMultimap<K, V> extends AbstractSortedKeySortedSetMultimap<K, V> {
    private static final long serialVersionUID = 0;
    private transient Comparator<? super K> keyComparator;
    private transient Comparator<? super V> valueComparator;

    private TreeMultimap(Comparator<? super K> keyComparator,
                         Comparator<? super V> valueComparator) {
        super(new TreeMap<K, Collection<V>>(keyComparator));
        this.keyComparator = keyComparator;
        this.valueComparator = valueComparator;
    }

    /**
     * Creates an empty {@code TreeMultimap} ordered by the natural ordering of
     * its keys and values.
     */
    public static <K extends Comparable, V extends Comparable>
    TreeMultimap<K, V> create() {
        return new TreeMultimap<K, V>(Ordering.natural(), Ordering.natural());
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>Creates an empty {@code TreeSet} for a collection of values for one key.
     *
     * @return a new {@code TreeSet} containing a collection of values for one
     * key
     */
    @Override
    SortedSet<V> createCollection() {
        return new TreeSet<V>(valueComparator);
    }

    @Override
    Collection<V> createCollection(K key) {
        if (key == null) {
            keyComparator().compare(key, key);
        }
        return super.createCollection(key);
    }

    /**
     * Returns the comparator that orders the multimap keys.
     */
    private Comparator<? super K> keyComparator() {
        return keyComparator;
    }

  /*
   * The following @GwtIncompatible methods override the methods in
   * AbstractSortedKeySortedSetMultimap, so GWT will fall back to the ASKSSM implementations,
   * which return SortedSets and SortedMaps.
   */

    @Override
    public Comparator<? super V> valueComparator() {
        return valueComparator;
    }

    @Override
    NavigableMap<K, Collection<V>> backingMap() {
        return (NavigableMap<K, Collection<V>>) super.backingMap();
    }

    /**
     * @since 14.0 (present with return type {@code SortedSet} since 2.0)
     */
    @Override
    public NavigableSet<V> get(K key) {
        return (NavigableSet<V>) super.get(key);
    }

    @Override
    Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
        return Sets.unmodifiableNavigableSet((NavigableSet<V>) collection);
    }

    @Override
    Collection<V> wrapCollection(K key, Collection<V> collection) {
        return new WrappedNavigableSet(key, (NavigableSet<V>) collection, null);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>Because a {@code TreeMultimap} has unique sorted keys, this method
     * returns a {@link NavigableSet}, instead of the {@link java.util.Set} specified
     * in the {@link Multimap} interface.
     *
     * @since 14.0 (present with return type {@code SortedSet} since 2.0)
     */
    @Override
    public NavigableSet<K> keySet() {
        return (NavigableSet<K>) super.keySet();
    }

    @Override
    NavigableSet<K> createKeySet() {
        return new NavigableKeySet(backingMap());
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>Because a {@code TreeMultimap} has unique sorted keys, this method
     * returns a {@link NavigableMap}, instead of the {@link java.util.Map} specified
     * in the {@link Multimap} interface.
     *
     * @since 14.0 (present with return type {@code SortedMap} since 2.0)
     */
    @Override
    public NavigableMap<K, Collection<V>> asMap() {
        return (NavigableMap<K, Collection<V>>) super.asMap();
    }

    @Override
    NavigableMap<K, Collection<V>> createAsMap() {
        return new NavigableAsMap(backingMap());
    }

    /**
     * @serialData key comparator, value comparator, number of distinct keys, and
     * then for each distinct key: the key, number of values for that key, and
     * key values
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(keyComparator());
        stream.writeObject(valueComparator());
        Serialization.writeMultimap(this, stream);
    }

    @SuppressWarnings("unchecked") // reading data stored by writeObject
    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        keyComparator = checkNotNull((Comparator<? super K>) stream.readObject());
        valueComparator = checkNotNull((Comparator<? super V>) stream.readObject());
        setMap(new TreeMap<K, Collection<V>>(keyComparator));
        Serialization.populateMultimap(this, stream);
    }
}
