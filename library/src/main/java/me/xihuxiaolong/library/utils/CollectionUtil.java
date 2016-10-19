package me.xihuxiaolong.library.utils;

import java.util.Collection;

/**
 * Created by yangxiaolong on 15/10/30.
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    public static int getSize(Collection coll) {
        return coll == null ? 0 : coll.size();
    }
}
