/* 
 *  Copyright (c)2009-2010 The Inframesh Software Foundation (ISF)
 *
 *  Licensed under the Inframesh Software License (the "License"), 
 *	Version 1.0 ; you may obtain a copy of the license at
 *
 *  	http://www.inframesh.org/licenses/LICENSE-1.0
 *
 *  Software distributed under the License is distributed  on an "AS IS" 
 *  BASIS but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the License 
 *  for more details.
 *  
 *  Inframesh Software Foundation is donated by Drowell Technology Limited.
 */
package org.inframesh.fundus.base.datastruct;

import java.util.EmptyStackException;

import org.inframesh.fundus.base.ArgumentAssert;


/**
 * 字符堆栈
 * 
 * @since fundus
 * @version $Revision: 1.0 $Date:2010-2-25 下午03:24:25 $
 * 
 * @author <a href="mailto:josh.yoah@gmail.com">杨超</a>
 */ 
public final class CharStack {
	private char[] buffer;
	private int size;

	/**
	 * 默认构造，默认缓冲区大小为10
	 * 
	 * @see #CharStack(int)
	 */
	public CharStack() {
		this(10);
	}

	/**
	 * 以指定缓冲区大小构造字符堆栈
	 * 
	 * @param capacity
	 *            堆栈容量
	 * @throws IllegalArgumentException
	 *             当容量小于0时，抛出此异常
	 * @see #CharStack()
	 */
	public CharStack(int capacity) throws IllegalArgumentException {
		ArgumentAssert.notNull(capacity, "stack capacity {0} lower than 0", capacity);
		this.size = 0;
		this.buffer = new char[capacity];
	}

	/**
	 * 将字符压入堆栈
	 * 
	 * @param c
	 *            字符
	 * @see #push(int)
	 */
	public void push(char c) {
		ensureCapacity();
		this.buffer[this.size++] = c;
	}

	/**
	 * 将以ASCII码表示的字符以字符形式压入堆栈
	 * 
	 * @param c
	 *            字符ASCII码
	 * @see #push(char)
	 */
	public void push(int c) {
		this.push((char) c);
	}

	/**
	 * 查看栈顶字符，并将此字符从堆栈里移除
	 * 
	 * @return 返回栈顶字符
	 * @throws EmptyStackException
	 *             当堆栈为空时，抛出此异常
	 * @see #peek()
	 */
	public char pop() throws EmptyStackException {
		char c = this.peek();
		this.buffer[this.size--] = 0;
		return c;
	}

	/**
	 * 查看栈顶字符，而不移除此字符
	 * 
	 * @return 返回栈顶字符
	 * @throws EmptyStackException
	 *             当堆栈为空时，抛出此异常
	 * @see #pop()
	 */
	public char peek() throws EmptyStackException {
		try {
			return this.buffer[this.size - 1];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new EmptyStackException();
		}
	}

	/**
	 * 返回堆栈大小
	 * 
	 * @return 返回堆栈的大小
	 */
	public int size() {
		return this.size;
	}

	/**
	 * 返回堆栈中指定索引位置的字符
	 * 
	 * @param index
	 *            索引
	 * @return 堆栈中指定索引位置的字符
	 * @throws IndexOutOfBoundsException
	 *             当index &gt;= size() || index &lt; 0时抛出此异常
	 */
	public char get(int index) throws IndexOutOfBoundsException {
		return this.buffer[index];
	}

	/**
	 * 检索指定字符在堆栈中的位置，索引从0开始
	 * 
	 * @param c
	 *            待检索字符
	 * @return 返回指定字符在堆栈中的索引号，如果不存在则返回-1
	 */
	public int indexOf(char c) {
		for (int i = 0; i < this.size; i++) {
			if (this.buffer[i] == c) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 检索指定字符在堆栈中的索引，索引从0开始
	 * 
	 * @param c
	 *            待检测字符的ASCII码
	 * @return 返回指定字符在堆栈中的索引号，如果不存在则返回-1
	 */
	public int indexOf(int c) {
		return this.indexOf((char) c);
	}

	/**
	 * 测试堆栈是否为空
	 * 
	 * @return 如果堆栈为空则返回true，否则返回false
	 */
	public boolean empty() {
		return this.size <= 0;
	}

	/**
	 * 清空堆栈
	 */
	public void clear() {
		this.size = 0;
	}

	/**
	 * 确定堆栈是否有足够的容量，如果容量不够，则分配和当前缓冲区一样大小的新的缓冲区
	 */
	private void ensureCapacity() {
		synchronized (this.buffer) {
			if (this.size >= this.buffer.length - 1) {
				char[] buffer = new char[this.buffer.length << 1];
				System.arraycopy(this.buffer, 0, buffer, 0, this.buffer.length);
				this.buffer = buffer;
			}
		}
	}

	/**
	 * 返回字符堆栈的字符串表示形式，此字符串形式如：['c<sub>1</sub>', 'c<sub>2</sub>', ... , 'c<sub>n</sub>']
	 * 
	 * @return 返回字符堆栈的字符串表示形式
	 */
	@Override
	public String toString() {
		/*
		 * StringBuffer buff = new StringBuffer(); buff.append('['); for (int i =
		 * 0; i < this.size; i++) { buff.append('\'');
		 * buff.append(Text.escape(this.buffer[i])); buff.append('\''); if (i <
		 * this.size - 1) buff.append(',').append(' '); } return
		 * buff.append(']').toString();
		 */
		return new String(this.buffer, 0, size);
	}
}
