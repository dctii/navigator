package com.solvd.navigator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class CollectionUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.COLLECTION_UTILS);
    private static final Random random = new Random();

    public static <T> Set<T> setToNullIfEmpty(Set<T> collection) {
        if (BooleanUtils.isEmptyOrNullCollection(collection)) {
            collection = null;
        }
        return collection;
    }

    public static <T> T getRandomItemFromList(List<T> list) {
        if (BooleanUtils.isEmptyOrNullCollection(list)) {
            throw new IllegalStateException("Collection cannot be empty ");
        }

        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }

    private CollectionUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
