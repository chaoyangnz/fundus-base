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
package com.richdyang.fundus.base.text;


/**
 * Determines a true or false value for a given input. For example, a
 * {@code RegexPredicate} might implement {@code Predicate<String>}, and return
 * {@code true} for any string that matches its given regular expression.
 *
 * <p>Implementations which may cause side effects upon evaluation are strongly
 * encouraged to state this fact clearly in their API documentation.
 *
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>ince fundus
 */
public interface Matcher<T> {

  /*
   * This interface does not extend Function<T, Boolean> because doing so would
   * let predicates return null.
   */

  /**
   * Applies this predicate to the given object.
   *
   * @param input the input that the predicate should act on
   * @return the value of this predicate when applied to the input {@code t}
   */
  boolean matches(T input);

  /**
   * Indicates whether some other object is equal to this {@code Predicate}.
   * This method can return {@code true} <i>only</i> if the specified object is
   * also a {@code Predicate} and, for every input object {@code input}, it
   * returns exactly the same value. Thus, {@code predicate1.equals(predicate2)}
   * implies that either {@code predicate1.apply(input)} and
   * {@code predicate2.apply(input)} are both {@code true} or both
   * {@code false}.
   *
   * <p>Note that it is always safe <i>not</i> to override
   * {@link Object#equals}.
   */
  boolean equals(Object obj);
}