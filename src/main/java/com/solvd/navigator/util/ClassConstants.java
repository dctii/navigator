package com.solvd.navigator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public final class ClassConstants {

    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.class);
    // java.util
    public static final Class<List> JAVA_UTIL_LIST = List.class;

    // com.solvd.navigator.util
    public static final Class<AnsiCodes> ANSI_CODES = AnsiCodes.class;
    public static final Class<ArrayUtils> ARRAY_UTILS = ArrayUtils.class;
    public static final Class<AuthConnection> AUTH_CONNECTION = AuthConnection.class;
    public static final Class<BigDecimalUtils> BIG_DECIMAL_UTILS = BigDecimalUtils.class;
    public static final Class<BooleanUtils> BOOLEAN_UTILS = BooleanUtils.class;
    public static final Class<CollectionUtils> COLLECTION_UTILS = CollectionUtils.class;
    public static final Class<ClassConstants> CLASS_CONSTANTS = ClassConstants.class;
    public static final Class<ConfigConstants> CONFIG_CONSTANTS = ConfigConstants.class;
    public static final Class<ConfigLoader> CONFIG_LOADER = ConfigLoader.class;
    public static final Class<ExceptionUtils> EXCEPTION_UTILS = ExceptionUtils.class;
    public static final Class<DataAccessProvider> DATA_ACCESS_PROVIDER = DataAccessProvider.class;
    public static final Class<DBConnectionPool> DB_CONNECTION_POOL = DBConnectionPool.class;
    public static final Class<FilepathConstants> FILEPATH_CONSTANTS = FilepathConstants.class;
    public static final Class<JacksonUtils> JACKSON_UTILS = JacksonUtils.class;
    public static final Class<MyBatisUtils> MYBATIS_UTILS = MyBatisUtils.class;
    public static final Class<NumberUtils> NUMBER_UTILS = NumberUtils.class;
    public static final Class<ReflectionUtils> REFLECTION_UTILS = ReflectionUtils.class;
    public static final Class<RegExpConstants> REG_EXP_CONSTANTS = RegExpConstants.class;
    public static final Class<ScannerUtils> SCANNER_UTILS = ScannerUtils.class;
    public static final Class<SQLConstants> SQL_CONSTANTS = SQLConstants.class;
    public static final Class<SQLUtils> SQL_UTILS = SQLUtils.class;
    public static final Class<StringConstants> STRING_CONSTANTS = StringConstants.class;
    public static final Class<StringFormatters> STRING_FORMATTERS = StringFormatters.class;


    private ClassConstants() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
