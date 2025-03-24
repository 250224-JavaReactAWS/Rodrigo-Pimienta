package com.revature.util;

import com.revature.controllers.*;
import com.revature.repos.*;
import com.revature.services.*;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;


public class JavalinUtil {

    // init variables
    // first the DAOImpl
    // Second the service
    // Last the controller

    public static Javalin create(int port){

        // init the impl
        UserDAO userDAO = new UserDAOImpl();
        UserResetCodeDAO userResetCodeDAO = new UserResetCodeDAOImpl();
        UserAddressDAO userAddressDAO = new UserAddressDAOImpl();
        CategoryDAO categoryDAO = new CategoryDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        ProductReviewDAO productReviewDAO = new ProductReviewDAOImpl();
        CartItemDAO cartItemDAO = new CartItemDaoImpl();
        DiscountDAO discountDAO = new DiscountDAOImpl();
        OrderDAO orderDAO = new OrderDAOImpl();
        OrderItemDAO orderItemDAO = new OrderItemDAOImpl();
        OrderDiscountDAO orderDiscountDAO = new OrderDiscountDAOImpl();

        // init services
        UserService userService = new UserService(userDAO,userAddressDAO);
        UserResetCodeService userResetCodeService = new UserResetCodeService(userResetCodeDAO);
        UserAddressesService userAddressesService = new UserAddressesService(userAddressDAO);
        CategoryService categoryService = new CategoryService(categoryDAO,productDAO);
        ProductService productService = new ProductService(productDAO, categoryDAO);
        ProductReviewService productReviewService = new ProductReviewService(productReviewDAO, orderItemDAO);
        CartItemService cartItemService = new CartItemService(cartItemDAO, productDAO);
        DiscountService discountService = new DiscountService(discountDAO);
        OrderService orderService = new OrderService(orderDAO,orderItemDAO,cartItemDAO, orderDiscountDAO, productDAO);

        // init controllers
        UserController userController = new UserController(userService,userResetCodeService);
        UserAddressController userAddressController = new UserAddressController(userAddressesService);
        CategoryController categoryController = new CategoryController(categoryService);
        ProductController productController = new ProductController(productService);
        ProductReviewsController productReviewsController = new ProductReviewsController(productReviewService);
        CartItemController cartItemController = new CartItemController(cartItemService);
        DiscountController discountController = new DiscountController(discountService);
        OrderController orderController = new OrderController(orderService,discountService);

        return Javalin.create(config -> {
                    config.router.apiBuilder(() -> {
                        path("/users", () -> {
                            // GET
                            get("/", userController::getAllUsersHandler);
                            get("/address", userController::getUserAddressHandler);
                            //POST
                            post("/register", userController::registerUserHandler);
                            post("/login", userController::loginHandler);
                            // PUT
                            put("/", userController::updateUserHandler);
                            put("/password", userController::updateUserPasswordHandler);
                            post("/resetCode", userController::generatedResetCodeHandler);

                            // user id required
                            path("/{id}", () -> {
                                put("/status", userController::updateUserStatusHandler);
                            });
                        });
                        path("/userAddress", () -> {
                            post("/", userAddressController::registerUserAddressHandler);
                            // userAddress id required
                            path("/{id}", () -> {
                                get("/", userAddressController::getUserAddressHandler);
                                put("/", userAddressController::updateUserAddressHandler);
                                put("/status",userAddressController::updateStatusHandler);
                            });
                        });
                        path("/categories", () -> {
                            get("/", categoryController::getAllCategoriesHandler);
                            post("/", categoryController::registerCategoryHandler);
                            // category id required
                            path("/{id}", () -> {
                                get("/", categoryController::getCategoryHandler);
                                get("/products", categoryController::getCategoryProducts);
                                put("/", categoryController::updateCategoryHandler);
                                put("/status",categoryController::updateStatusHandler);
                            });
                        });
                        path("/products", () -> {
                            get("/", productController::getProductsHandler);
                            post("/", productController::registerNewProductHandler);
                            // product id required
                            path("/{id}", () -> {
                                get("/", productController::getProductHandler);
                                get("/reviews", productReviewsController::getProductReviewsHandler);
                                put("/", productController::updateProductHandler);
                                put("/status",productController::updateStatusHandler);
                            });
                        });
                        path("/cartItems", () -> {
                            get("/", cartItemController::gatUserCartHandler);
                            post("/", cartItemController::addCartItemHandler);
                            delete("/", cartItemController::deleteUserCartItemHandler);
                            // CartItem required
                            path("/{id}", () -> {
                                put("/", cartItemController::updateCartItemHandler);
                                delete("/",cartItemController::deleteCartItemHandler);
                            });
                        });
                        path("/discounts", () -> {
                            get("/", discountController::getAllDiscountsHandler);
                            get("/user", discountController::getUserDiscountsHandler);
                            post("/", discountController::registerDiscountHandler);
                            // discount id required
                            path("/{id}", () -> {
                                get("/", discountController::getDiscountHandler);
                                put("/", discountController::updateDiscountHandler);
                                put("/status", discountController::updateStatusHandler);
                            });
                        });
                        path("/orders", () -> {
                            get("/", orderController::getAllOrdersHandler);
                            get("/user", orderController::getUserOrdersHandler);
                            get("/search/{status}", orderController::getOrdersByStatusHandler);
                            post("/", orderController::registerNewOrderHandler);
                            // order id required
                            path("/{id}", () -> {
                                get("/", orderController::getOrderHandler);
                                put("/status", orderController::updateStatusHandler);
                            });
                        });
                        path("/reviews", () -> {
                            get("/", productReviewsController::getAllReviewsHandler);  // all
                            get("/user", productReviewsController::getUserReviewsHandler); // user reviews
                            post("/", productReviewsController::registerNewReviewHandler);
                            // order id required
                            path("/{id}", () -> {
                                get("/", productReviewsController::getReviewHandler); // get specific review
                            });
                        });
                    });
                })
                .start(port);
    }
}
