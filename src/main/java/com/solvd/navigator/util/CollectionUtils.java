package com.solvd.navigator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class CollectionUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.COLLECTION_UTILS);

    public static <T> Set<T> setToNullIfEmpty(Set<T> collection) {
        if (BooleanUtils.isEmptyOrNullCollection(collection)) {
            collection = null;
        }
        return collection;
    }

    private CollectionUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
