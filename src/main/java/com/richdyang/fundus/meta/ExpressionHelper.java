package com.richdyang.fundus.meta;

import com.richdyang.fundus.base.Numerics;
import com.richdyang.fundus.base.text.Texts;

import static com.richdyang.fundus.base.Numerics.parseNumeric;
import static com.richdyang.fundus.base.text.Texts.FormatHelper;
import static com.richdyang.fundus.base.text.Texts.FormatHelper.unqualify;

/**
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 * @since fundus
 */
public class ExpressionHelper {

    public static Object[] split(String expression) {

        String name = unqualify(expression);
        int index = expression.lastIndexOf(name) - 1;
        if (index < 0) index = 0;
        String prefix = expression.substring(0, index);

        return new Object[]{prefix, tokenToSegment(name, true)};
    }

    public static Segment tokenToSegment(String token, boolean last) {

        Segment segment = new Segment();
        segment.last = last;

        int start = token.indexOf('[');
        int end = token.indexOf(']');
        if (start == -1 && end == -1) {
            segment.array = false;
            segment.name = token;
        } else if (start > 0 && start < end && end == token.length() - 1) {
            segment.array = true;
            segment.name = token.substring(0, start);
            if (end - start > 1) {
                segment.index = parseNumeric(token.substring(start + 1, end), int.class);
            } else {//允许没有指定index，即空[],但只能在最后一个，表示添加到List
                if (!segment.last) {
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
        for (int i = 0; i < len; ++i) {
            String token = tokens[i];

            Segment segment = tokenToSegment(token, i == len - 1);

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
