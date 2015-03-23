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
package org.inframesh.fundus.util;


/**
 * 本工具类用于获取运行时方法调用栈call stack
 * 
 * @since fundus
 * @version $Revision: 1.0 $Date:2010-2-8 上午10:17:24 $
 * 
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 */ 
public final class StackTracer {
	
	/**
	 * 打印调用栈, 按被调用顺序LOG
	 * <pre>
	 * method1
	 *   /|\  call
	 * method2
	 *   /|\  call
	 * method2
	 * </pre>
	 * 
	 */
	public static void printCallerTrace() {
		StringBuffer sbf = new StringBuffer();
        StackTraceElement[] trace = new Throwable().getStackTrace();
        sbf.append("Call trace:\n");;
        for (int i=1; i < trace.length; i++) {
        	for(int j=1; j < i; ++j) {
        		sbf.append("  ");
        	}
        	sbf.append("|- ").append(trace[i]).append("\n");
        }
        System.out.println(sbf);
	}

	/**
	 * 打印调用栈，按调用顺序LOG
	 * <pre>
	 * method1
	 *   \|/  call
	 * method2
	 *   \|/  call
	 * method2
	 * </pre>
	 */
	public static void printCalleeTrace() {
		StringBuffer sbf = new StringBuffer();
        StackTraceElement[] trace = new Throwable().getStackTrace();
        sbf.append("Call trace:\n");
        for (int i=0; i < trace.length - 1; i++) {
        	for(int j = trace.length-1; j > i+1; --j) {
        		sbf.append("  ");
        	}
        	sbf.append("|- " ).append(trace[trace.length - i -1]).append("\n");
        } 	
        System.out.println(sbf);
    }
	
	/**
	 * 获取调用getCallee()方法的当前方法
	 * @return
	 */
	public static String getCallee(){  
        return getCaller(1);  
    }
	
	/**
	 * 获取调用getCallee()方法的当前方法的直接caller
	 * @return
	 */
	public static String getCaller(){  
          
        return getCaller(2);  
    }
	
	/**
	 * 获取调用getCaller()方法的当前方法的任意深度caller<p>
	 * depth = 0, 返回调用getCaller(0)的方法，同{@link #getCallee()}<br/>
	 * depth = 1, 返回调用getCaller(1)的直接caller, 同{@link #getCaller()}<br/>
	 * depth > 1, 以此类推
	 * @param depth specified depth for caller
	 * @return the caller info if not reach the maximum depth
	 */
	public static String getCaller(int depth){  
        StringBuffer sb = new StringBuffer();   
          
        StackTraceElement[] trace = new Throwable().getStackTrace();  
        int length = trace.length; 
        if(length > 1 + depth) {
        	sb.append(trace[depth + 1]); 
        } else {
        	//sb.append("specified depth > maximum depth or reach entry point");
        }
          
        return sb.toString();  
    }
	
	/**
	 * 获取调用getCallerElement()方法的当前方法的任意深度caller<p>
	 * depth = 0, 返回调用getCaller(0)的方法，同{@link #getCalleeElement()}<br/>
	 * depth = 1, 返回调用getCaller(1)的直接caller, 同{@link #getCallerElement()}<br/>
	 * depth > 1, 以此类推
	 * @param depth specified depth for caller
	 * @return the caller <code>StackTraceElement</code> if not reach the maximum depth
	 */
	public static StackTraceElement getCallerElement(int depth){  
		StackTraceElement[] trace = new Throwable().getStackTrace();  
        int length = trace.length; 
        if(length > 1 + depth) {
        	return trace[depth + 1]; 
        } 
        
        return null; 
	}
	
	public static StackTraceElement getCallerElement(){  
		return getCallerElement(2); 
	}
	
	public static StackTraceElement getCalleeElement(){  
		return getCallerElement(1); 
	}
}
