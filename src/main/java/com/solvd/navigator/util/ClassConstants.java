package com.solvd.navigator.util;

import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.bin.Employee;
import com.solvd.navigator.bin.Item;
import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Order;
import com.solvd.navigator.bin.OrderItem;
import com.solvd.navigator.bin.OrderRecipient;
import com.solvd.navigator.bin.Person;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.bin.Vehicle;
import com.solvd.navigator.dao.DriverDAO;
import com.solvd.navigator.dao.EmployeeDAO;
import com.solvd.navigator.dao.LocationDAO;
import com.solvd.navigator.dao.OrderDAO;
import com.solvd.navigator.dao.OrderRecipientDAO;
import com.solvd.navigator.dao.PersonDAO;
import com.solvd.navigator.dao.StorageDAO;
import com.solvd.navigator.dao.VehicleDAO;
import com.solvd.navigator.dao.jdbc.DriverJDBCImpl;
import com.solvd.navigator.dao.jdbc.EmployeeJDBCImpl;
import com.solvd.navigator.dao.jdbc.LocationJDBCImpl;
import com.solvd.navigator.dao.jdbc.OrderJDBCImpl;
import com.solvd.navigator.dao.jdbc.OrderRecipientJDBCImpl;
import com.solvd.navigator.dao.jdbc.PersonJDBCImpl;
import com.solvd.navigator.dao.jdbc.VehicleJDBCImpl;
import com.solvd.navigator.math.RouteCalculator;
import com.solvd.navigator.math.RouteCalculatorImpl;
import com.solvd.navigator.math.RoutePlan;
import com.solvd.navigator.math.graph.Coordinate;
import com.solvd.navigator.math.graph.Direction;
import com.solvd.navigator.math.graph.Edge;
import com.solvd.navigator.math.graph.GraphConstants;
import com.solvd.navigator.math.graph.GraphFactory;
import com.solvd.navigator.math.graph.GraphType;
import com.solvd.navigator.math.graph.IGraph;
import com.solvd.navigator.math.graph.Point;
import com.solvd.navigator.math.graph.ShortestPathsMatrix;
import com.solvd.navigator.math.graph.Vertex;
import com.solvd.navigator.math.graph.WeightedGraph;
import com.solvd.navigator.math.util.GraphUtils;
import com.solvd.navigator.math.util.JsonDataStore;
import com.solvd.navigator.math.util.OrderConstants;
import com.solvd.navigator.math.util.RouteUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public final class ClassConstants {

    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.class);
    // java.util
    public static final Class<List> JAVA_UTIL_LIST = List.class;


    // com.solvd.navigator.bin
    public static final Class<Driver> DRIVER = Driver.class;
    public static final Class<Employee> EMPLOYEE = Employee.class;
    public static final Class<Item> ITEM = Item.class;
    public static final Class<Location> LOCATION = Location.class;
    public static final Class<Order> ORDER = Order.class;
    public static final Class<OrderItem> ORDER_ITEM = OrderItem.class;
    public static final Class<OrderRecipient> ORDER_RECIPIENT = OrderRecipient.class;
    public static final Class<Person> PERSON = Person.class;
    public static final Class<Storage> STORAGE = Storage.class;
    public static final Class<Vehicle> VEHICLE = Vehicle.class;

    // com.solvd.navigator.dao

    public static final Class<DriverDAO> DRIVER_DAO = DriverDAO.class;
    public static final Class<EmployeeDAO> EMPLOYEE_DAO = EmployeeDAO.class;
    public static final Class<LocationDAO> LOCATION_DAO = LocationDAO.class;
    public static final Class<OrderDAO> ORDER_DAO = OrderDAO.class;
    public static final Class<OrderRecipientDAO> ORDER_RECIPIENT_DAO = OrderRecipientDAO.class;
    public static final Class<PersonDAO> PERSON_DAO = PersonDAO.class;
    public static final Class<StorageDAO> STORAGE_DAO = StorageDAO.class;
    public static final Class<VehicleDAO> VEHICLE_DAO = VehicleDAO.class;

    /* Not used
        public static final Class<ItemDAO> ITEM_DAO = ItemDAO.class;
        public static final Class<OrderItemDAO> ORDER_ITEM_DAO = OrderItemDAO.class;
     */

    // com.solvd.navigator.dao.jdbc

    public static final Class<DriverJDBCImpl> DRIVER_JDBC_IMPL = DriverJDBCImpl.class;
    public static final Class<EmployeeJDBCImpl> EMPLOYEE_JDBC_IMPL = EmployeeJDBCImpl.class;
    public static final Class<LocationJDBCImpl> LOCATION_JDBC_IMPL = LocationJDBCImpl.class;
    public static final Class<OrderJDBCImpl> ORDER_JDBC_IMPL = OrderJDBCImpl.class;
    public static final Class<OrderRecipientJDBCImpl> ORDER_RECIPIENT_JDBC_IMPL = OrderRecipientJDBCImpl.class;
    public static final Class<PersonJDBCImpl> PERSON_JDBC_IMPL = PersonJDBCImpl.class;

//    TODO: Has incorrect name currently
    //    public static final Class<StorageJDBCImpl> STORAGE_JDBC_IMPL = StorageJDBCImpl.class;

    public static final Class<VehicleJDBCImpl> VEHICLE_JDBC_IMPL = VehicleJDBCImpl.class;

    /* Not used
        public static final Class<ItemJDBCImpl> ITEM = ItemJDBCImpl.class;
        public static final Class<OrderItemJDBCImpl> ORDER_ITEM = OrderItemJDBCImpl.class;
    */


    // com.solvd.navigator.math

    public static final Class<IGraph> IGRAPH = IGraph.class;
    public static final Class<Coordinate> COORDINATE = Coordinate.class;
    public static final Class<Direction> DIRECTION = Direction.class;
    public static final Class<Edge> EDGE = Edge.class;
    public static final Class<GraphConstants> GRAPH_CONSTANTS = GraphConstants.class;
    public static final Class<GraphFactory> GRAPH_FACTORY = GraphFactory.class;
    public static final Class<Point> POINT = Point.class;
    public static final Class<ShortestPathsMatrix> SHORTEST_PATHS_MATRIX = ShortestPathsMatrix.class;
    public static final Class<Vertex> VERTEX = Vertex.class;
    public static final Class<WeightedGraph> WEIGHTED_GRAPH = WeightedGraph.class;

    public static final Class<GraphType> GRAPH_TYPE = GraphType.class;
    public static final Class<GraphUtils> GRAPH_UTILS = GraphUtils.class;
    public static final Class<JsonDataStore> JSON_DATA_STORE = JsonDataStore.class;
    public static final Class<OrderConstants> ORDER_CONSTANTS = OrderConstants.class;
    public static final Class<RouteUtils> ROUTE_UTILS = RouteUtils.class;
    public static final Class<RouteCalculator> ROUTE_CALCULATOR = RouteCalculator.class;
    public static final Class<RouteCalculatorImpl> ROUTE_CALCULATOR_IMPL = RouteCalculatorImpl.class;
    public static final Class<RoutePlan> ROUTE_PLAN = RoutePlan.class;

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
    public static final Class<DAOFactory> DATA_ACCESS_PROVIDER = DAOFactory.class;
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
