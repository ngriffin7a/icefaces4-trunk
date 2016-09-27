/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.ace.model.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.io.Serializable;

public abstract class WrappedList<E> implements List<E>, Serializable {

	//                //
	// Internal logic //
	//                //

	private List<E> wrapped = null;

	public List<E> getWrapped() {
		if (wrapped == null) wrapped = new ArrayList<E>();
		return wrapped;
	}

	public void setWrapped(List<E> wrapped) {
		this.wrapped = wrapped;
	}

	//                                //
	// Wrapper java.util.List methods //
	//                                //

	public boolean add(E e) {
		return getWrapped().add(e);
	}

	public void add(int index, E element) {
		getWrapped().add(index, element);
	}

	public boolean addAll(Collection<? extends E> c) {
		return getWrapped().addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return getWrapped().addAll(index, c);
	}

	public void clear() {
		getWrapped().clear();
	}

	public boolean contains(Object o) {
		return getWrapped().contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return getWrapped().containsAll(c);
	}

	public boolean equals(Object o) {
		return getWrapped().equals(o);
	}

	public E get(int index) {
		return getWrapped().get(index);
	}

	public int hashCode() {
		return getWrapped().hashCode();
	}

	public int indexOf(Object o) {
		return getWrapped().indexOf(o);
	}

	public boolean isEmpty() {
		return getWrapped().isEmpty();
	}

	public Iterator<E> iterator() {
		return getWrapped().iterator();
	}

	public int lastIndexOf(Object o) {
		return getWrapped().lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return getWrapped().listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return getWrapped().listIterator(index);
	}

	public E remove(int index) {
		return getWrapped().remove(index);
	}

	public boolean remove(Object o) {
		return getWrapped().remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return getWrapped().removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return getWrapped().retainAll(c);
	}

	public E set(int index, E element) {
		return getWrapped().set(index, element);
	}

	public int size() {
		return getWrapped().size();
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return getWrapped().subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return getWrapped().toArray();
	}
	
	public <T> T[] toArray(T[] a) {
		return getWrapped().toArray(a);
	}
}