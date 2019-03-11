package com.iscas.common.tools.list;

import java.util.List;

public class ListUtils {
    public static boolean sqlIsNotEmpty(final List coll) {
        return !(coll == null || coll.isEmpty()|| coll.get(0)==null);
    }
}
