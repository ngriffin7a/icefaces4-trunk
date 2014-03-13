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

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2004-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.icefaces.impl.fastinfoset.com.sun.xml.fastinfoset.util;
import org.icefaces.impl.fastinfoset.com.sun.xml.fastinfoset.CommonResourceBundle;

public class ContiguousCharArrayArray extends ValueArray {
    public static final int INITIAL_CHARACTER_SIZE = 512;
    public static final int MAXIMUM_CHARACTER_SIZE = Integer.MAX_VALUE;
    
    protected int _maximumCharacterSize;
    
    public int[] _offset;
    public int[] _length;
    
    public char[] _array;
    public int _arrayIndex;
    public int _readOnlyArrayIndex;
    
    private String[] _cachedStrings;
    
    public int _cachedIndex;
    
    private ContiguousCharArrayArray _readOnlyArray;
    
    public ContiguousCharArrayArray(int initialCapacity, int maximumCapacity,
            int initialCharacterSize, int maximumCharacterSize) {
        _offset = new int[initialCapacity];
        _length = new int[initialCapacity];
        _array = new char[initialCharacterSize];
        _maximumCapacity = maximumCapacity;
        _maximumCharacterSize = maximumCharacterSize;
    }
    
    public ContiguousCharArrayArray() {
        this(DEFAULT_CAPACITY, MAXIMUM_CAPACITY,
                INITIAL_CHARACTER_SIZE, MAXIMUM_CHARACTER_SIZE);
    }
    
    public final void clear() {
        _arrayIndex = _readOnlyArrayIndex;
        _size = _readOnlyArraySize;
        
        if (_cachedStrings != null) {
            for (int i = _readOnlyArraySize; i < _cachedStrings.length; i++) {
                _cachedStrings[i] = null;
            }
        }
    }
    
    public final int getArrayIndex() {
        return _arrayIndex;
    }
    
    public final void setReadOnlyArray(ValueArray readOnlyArray, boolean clear) {
        if (!(readOnlyArray instanceof ContiguousCharArrayArray)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyArray}));
        }
        
        setReadOnlyArray((ContiguousCharArrayArray)readOnlyArray, clear);
    }
    
    public final void setReadOnlyArray(ContiguousCharArrayArray readOnlyArray, boolean clear) {
        if (readOnlyArray != null) {
            _readOnlyArray = readOnlyArray;
            _readOnlyArraySize = readOnlyArray.getSize();
            _readOnlyArrayIndex = readOnlyArray.getArrayIndex();
            
            if (clear) {
                clear();
            }
            
            _array = getCompleteCharArray();
            _offset = getCompleteOffsetArray();
            _length = getCompleteLengthArray();
            _size = _readOnlyArraySize;
            _arrayIndex = _readOnlyArrayIndex;
        }
    }
    
    public final char[] getCompleteCharArray() {
        if (_readOnlyArray == null) {
            return _array;
        } else {
            final char[] ra = _readOnlyArray.getCompleteCharArray();
            final char[] a = new char[_readOnlyArrayIndex + _array.length];
            System.arraycopy(ra, 0, a, 0, _readOnlyArrayIndex);
            return a;
        }
    }
    
    public final int[] getCompleteOffsetArray() {
        if (_readOnlyArray == null) {
            return _offset;
        } else {
            final int[] ra = _readOnlyArray.getCompleteOffsetArray();
            final int[] a = new int[_readOnlyArraySize + _offset.length];
            System.arraycopy(ra, 0, a, 0, _readOnlyArraySize);
            return a;
        }
    }
    
    public final int[] getCompleteLengthArray() {
        if (_readOnlyArray == null) {
            return _length;
        } else {
            final int[] ra = _readOnlyArray.getCompleteLengthArray();
            final int[] a = new int[_readOnlyArraySize + _length.length];
            System.arraycopy(ra, 0, a, 0, _readOnlyArraySize);
            return a;
        }
    }
        
    public final String getString(int i) {
        if (_cachedStrings != null && i < _cachedStrings.length) {
            final String s = _cachedStrings[i];
            return (s != null) ? s : (_cachedStrings[i] = new String(_array, _offset[i], _length[i]));
        }
        
        final String[] newCachedStrings = new String[_offset.length];
        if (_cachedStrings != null && i >= _cachedStrings.length) {
            System.arraycopy(_cachedStrings, 0, newCachedStrings, 0, _cachedStrings.length);
        }
        _cachedStrings = newCachedStrings;
        
        return _cachedStrings[i] = new String(_array, _offset[i], _length[i]);
    }
    
    public final void ensureSize(int l) {
        if (_arrayIndex + l >= _array.length) {
            resizeArray(_arrayIndex + l);
        }        
    }
    
    public final void add(int l) {
        if (_size == _offset.length) {
            resize();
        }
        
        _cachedIndex = _size;
        _offset[_size] = _arrayIndex;
        _length[_size++] = l;
                
        _arrayIndex += l;
    }
    
    public final int add(char[] c, int l) {
        if (_size == _offset.length) {
            resize();
        }
        
        final int oldArrayIndex = _arrayIndex;
        final int arrayIndex = oldArrayIndex + l;
        
        _cachedIndex = _size;
        _offset[_size] = oldArrayIndex;
        _length[_size++] = l;
        
        if (arrayIndex >= _array.length) {
            resizeArray(arrayIndex);
        }
        
        System.arraycopy(c, 0, _array, oldArrayIndex, l);
        
        _arrayIndex = arrayIndex;
        return oldArrayIndex;
    }
    
    protected final void resize() {
        if (_size == _maximumCapacity) {
            throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.arrayMaxCapacity"));
        }
        
        int newSize = _size * 3 / 2 + 1;
        if (newSize > _maximumCapacity) {
            newSize = _maximumCapacity;
        }
        
        final int[] offset = new int[newSize];
        System.arraycopy(_offset, 0, offset, 0, _size);
        _offset = offset;
        
        final int[] length = new int[newSize];
        System.arraycopy(_length, 0, length, 0, _size);
        _length = length;
    }
    
    protected final void resizeArray(int requestedSize) {
        if (_arrayIndex == _maximumCharacterSize) {
            throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.maxNumberOfCharacters"));
        }
        
        int newSize = requestedSize * 3 / 2 + 1;
        if (newSize > _maximumCharacterSize) {
            newSize = _maximumCharacterSize;
        }
        
        final char[] array = new char[newSize];
        System.arraycopy(_array, 0, array, 0, _arrayIndex);
        _array = array;
    }
}
