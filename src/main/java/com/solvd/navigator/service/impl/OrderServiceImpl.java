//package com.solvd.navigator.service.impl;
//
//import com.solvd.navigator.bin.Order;
//import com.solvd.navigator.bin.Storage;
//import com.solvd.navigator.service.OrderStatus;
//import com.solvd.navigator.util.FilepathConstants;
//import com.solvd.navigator.util.JacksonUtils;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class OrderServiceImpl implements OrderStatus {
//
//
//    @Override
//    public List<Order> getAwaitingOrders(Storage storage, int orderLimit) {
//        List<Order> allOrders = JacksonUtils.extractItems(FilepathConstants.ORDERS_JSON, Order.class);
//
//        return allOrders.stream()
//                .filter(order -> order.getStorageId() == storage.getStorageId()
//                        && order.getOrderStatus().equals(OrderConstants.ORDER_STATUS_AWAITING_DELIVERY))
//                .limit(orderLimit)
//                .collect(Collectors.toList());
//
//    }
//
//
//
//    @Override
//    public void updateOrderStatus(List<Order> orders, String fromStatus, String toStatus) {
//
//        orders.forEach(order -> {
//            if (order.getOrderStatus().equals(fromStatus)) {
//                order.setOrderStatus(toStatus);
//                JacksonUtils.updateOrderInJsonById(FilepathConstants.ORDERS_JSON, order);
//            }
//        });
//    }
//}
