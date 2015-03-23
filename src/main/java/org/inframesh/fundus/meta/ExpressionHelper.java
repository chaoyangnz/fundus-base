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
package org.inframesh.fundus.meta;

import org.inframesh.fundus.base.Numerics;
import org.inframesh.fundus.base.text.Texts;

/**
 * 
 * @since fundus
 * @version 
 *
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 */ 
public class ExpressionHelper {
	
	public static Object[] split(String expression) {
		
		String name = Texts.FormatHelper.unqualify(expression);
		int index = expression.lastIndexOf(name)-1;
		if(index < 0) index = 0;
		String prefix = expression.substring(0, index);
		
		return new Object[] {prefix, tokenToSegment(name, true)};
	}
	
	public static Segment tokenToSegment(String token, boolean last) {
		
		Segment segment = new Segment();
		segment.last = last;
		
		int start = token.indexOf('[');
		int end = token.indexOf(']');
		if(start == -1 && end == -1) {
			segment.array = false;
			segment.name = token;
		} else if(start > 0 && start < end && end == token.length()-1) {
			segment.array = true;
			segment.name = token.substring(0, start);
			if(end-start > 1) {
				segment.index = Numerics.parseNumeric(token.substring(start+1, end), int.class);
			} else {//允许没有指定index，即空[],但只能在最后一个，表示添加到List
				if(!segment.last) {
					throw new IllegalStateException("Illegal expression: '" + segment.name + "' should be read but not index");
				}
			}
		} else {
			throw new IllegalStateException("Illegal expression");
		}
		
		return segment;
	}
	
	public static Segment[] parse(String expression) {
		String[] tokens = expression.split("\\.");
		int len = tokens.length;
		Segment[] segments = new Segment[len];
		for(int i = 0; i < len; ++i) {
			String token = tokens[i];
			
			Segment segment = tokenToSegment(token, i == len-1);

			segments[i] = segment;
		}
		
		return segments;
	}
	
	public static class Segment {
		public boolean array = false;
		public int index = -1;
		public String name;
		public boolean last = false;
	}
	
//	public static void main(String[] args) {
//		String expression = "jk[]";
//		Segment[] segments = parse(expression);
//		
//		Object[] ss = split(expression);
//	}
}
