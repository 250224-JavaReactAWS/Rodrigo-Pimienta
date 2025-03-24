
import com.revature.dtos.response.OrderCalculations;
import com.revature.models.*;
import com.revature.repos.*;
import com.revature.services.OrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class OrderServicesTest {

    private OrderService orderService;
    private OrderDAOImpl mockOrderDAO;
    private OrderItemDAOImpl mockOrderItemDAO;
    private CartItemDaoImpl mockCartItemDAO;
    private OrderDiscountDAOImpl mockOrderDiscountDAO;
    private ProductDAOImpl mockProductDAO;

    @Before
    public void setup() {
        mockOrderDAO = Mockito.mock(OrderDAOImpl.class);
        mockOrderItemDAO = Mockito.mock(OrderItemDAOImpl.class);
        mockCartItemDAO = Mockito.mock(CartItemDaoImpl.class);
        mockOrderDiscountDAO = Mockito.mock(OrderDiscountDAOImpl.class);
        mockProductDAO = Mockito.mock(ProductDAOImpl.class);
        orderService = new OrderService(mockOrderDAO, mockOrderItemDAO, mockCartItemDAO, mockOrderDiscountDAO, mockProductDAO);
    }

    @Test
    public void isCartEmpty_CartEmpty_ReturnsNull() {
        int userId = 1;
        when(mockCartItemDAO.getCartItemsByUser(userId)).thenReturn(null);
        Assert.assertNull(orderService.isCartEmpty(userId));
    }

    @Test
    public void isCartEmpty_CartWithInvalidItems_ReturnsEmptyList() {
        int userId = 1;
        List<CartItem> cartItems = Arrays.asList(
                new CartItem(1, userId, 1, 0, "Category1", "Product1", 10.0, 5, true, 0.0),
                new CartItem(2, userId, 2, 1, "Category2", "Product2", 20.0, 0, true, 20.0),
                new CartItem(3, userId, 3, 2, "Category3", "Product3", 30.0, 1, true, 60.0),
                new CartItem(4, userId, 4, 1, "Category4", "Product4", 40.0, 2, false, 40.0)
        );
        when(mockCartItemDAO.getCartItemsByUser(userId)).thenReturn(cartItems);
        Assert.assertEquals(0, orderService.isCartEmpty(userId).size());
    }

    @Test
    public void isCartEmpty_CartWithValidItems_ReturnsValidList() {
        int userId = 1;
        List<CartItem> cartItems = Arrays.asList(
                new CartItem(1, userId, 1, 1, "Category1", "Product1", 10.0, 5, true, 10.0),
                new CartItem(2, userId, 2, 2, "Category2", "Product2", 20.0, 5, true, 40.0)
        );
        when(mockCartItemDAO.getCartItemsByUser(userId)).thenReturn(cartItems);
        Assert.assertEquals(2, orderService.isCartEmpty(userId).size());
    }

    @Test
    public void calculateTotalDiscountAndApply_CalculatesCorrectly() {
        List<CartItem> cartItems = Arrays.asList(
                new CartItem(1, 1, 1, 1, "Category1", "Product1", 10.0, 5, true, 10.0),
                new CartItem(2, 1, 2, 1, "Category2", "Product2", 20.0, 2, true, 20.0)
        );
        List<OrderDiscount> discounts = Arrays.asList(
                new OrderDiscount(1, 10.0, 1),
                new OrderDiscount(2, 5.0, 2)
        );
        OrderCalculations calculations = orderService.calculateTotalDiscountAndApply(discounts, cartItems);
        Assert.assertEquals(25.65, calculations.getTotal(), 0.001);
        Assert.assertEquals(30.0, calculations.getSubTotal(), 0.001);
        Assert.assertEquals(4.35, calculations.getDiscount(), 0.001);
    }

    @Test
    public void registerOrder_CreatesOrderAndItems() {
        int userId = 1;
        int addressId = 1;
        double total = 28.5;
        double subTotal = 30.0;
        double discountTotal = 1.5;
        List<CartItem> cartItems = Arrays.asList(
                new CartItem(1, userId, 1, 1, "Category1", "Product1", 10.0, 5, true, 10.0),
                new CartItem(2, userId, 2, 1, "Category2", "Product2", 20.0, 2, true, 20.0)
        );
        List<OrderDiscount> discounts = Arrays.asList(
                new OrderDiscount(1, 10.0, 1),
                new OrderDiscount(2, 5.0, 2)
        );
        Order savedOrder = new Order(1, userId, addressId, total, discountTotal, subTotal);
        when(mockOrderDAO.create(Mockito.any(Order.class))).thenReturn(savedOrder);
        when(mockOrderItemDAO.create(Mockito.any(OrderItem.class))).thenReturn(new OrderItem());
        when(mockProductDAO.updateStockById(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new Product());
        when(mockCartItemDAO.deleteCartItemByProductAndUser(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        when(mockOrderDiscountDAO.create(Mockito.any(OrderDiscount.class))).thenReturn(new OrderDiscount());
        when(mockOrderItemDAO.getOrderItemsBy(Mockito.anyInt())).thenReturn(Arrays.asList(new OrderItem()));
        when(mockOrderDiscountDAO.getOrderDiscounts(Mockito.anyInt())).thenReturn(Arrays.asList(new OrderDiscount()));

        Order newOrder = orderService.registerOrder(userId, addressId, total, subTotal, discountTotal, cartItems, discounts);
        Assert.assertEquals(savedOrder.getOrderId(), newOrder.getOrderId());
    }

    @Test
    public void getAllOrders_ReturnsListOfOrders() {
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(mockOrderDAO.getAll()).thenReturn(orders);
        when(mockOrderItemDAO.getOrderItemsBy(Mockito.anyInt())).thenReturn(Arrays.asList(new OrderItem()));
        when(mockOrderDiscountDAO.getOrderDiscounts(Mockito.anyInt())).thenReturn(Arrays.asList(new OrderDiscount()));

        List<Order> allOrders = orderService.getAllOrders();
        Assert.assertEquals(orders.size(), allOrders.size());
    }

    @Test
    public void getUserOrders_ReturnsListOfOrders() {
        int userId = 1;
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(mockOrderDAO.getOrdersByUser(userId)).thenReturn(orders);
        when(mockOrderItemDAO.getOrderItemsBy(Mockito.anyInt())).thenReturn(Arrays.asList(new OrderItem()));
        when(mockOrderDiscountDAO.getOrderDiscounts(Mockito.anyInt())).thenReturn(Arrays.asList(new OrderDiscount()));
        List<Order> userOrders = orderService.getUserOrders(userId);
        Assert.assertEquals(orders.size(), userOrders.size());
    }

    @Test
    public void getOrdersByStatus_ReturnsListOfOrders() {
        OrderStatus status = OrderStatus.PENDING;
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(mockOrderDAO.getOrdersByStatus(status)).thenReturn(orders);
        when(mockOrderItemDAO.getOrderItemsBy(Mockito.anyInt())).thenReturn(Arrays.asList(new OrderItem()));
        when(mockOrderDiscountDAO.getOrderDiscounts(Mockito.anyInt())).thenReturn(Arrays.asList(new OrderDiscount()));
        List<Order> statusOrders = orderService.getOrdersByStatus(status);
        Assert.assertEquals(orders.size(), statusOrders.size());
    }

    @Test
    public void getOrder_ReturnsOrder() {
        int orderId = 1;
        Order order = new Order();
        when(mockOrderDAO.getById(orderId)).thenReturn(order);
        when(mockOrderItemDAO.getOrderItemsBy(Mockito.anyInt())).thenReturn(Arrays.asList(new OrderItem()));
        when(mockOrderDiscountDAO.getOrderDiscounts(Mockito.anyInt())).thenReturn(Arrays.asList(new OrderDiscount()));
        Order foundOrder = orderService.getOrder(orderId);
        Assert.assertEquals(order, foundOrder);
    }

    @Test
    public void updateStatus_UpdatesOrderStatus() {
        int orderId = 1;
        OrderStatus status = OrderStatus.SHIPPED;
        Order updatedOrder = new Order();
        when(mockOrderDAO.updateOrderStatus(orderId, status)).thenReturn(updatedOrder);
        Order result = orderService.updateStatus(orderId, status);
        Assert.assertEquals(updatedOrder, result);
    }
}