package com.revature.controllers;

import com.revature.dtos.request.StatusUpdateRequest;
import com.revature.dtos.response.ErrorMessage;
import com.revature.models.UserAddress;
import com.revature.services.UserAddressesService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAddressController {

    // first add the logger

    private final Logger logger = LoggerFactory.getLogger(UserAddressController.class);

    private final UserAddressesService userAddressesService;

    public UserAddressController(UserAddressesService userAddressesService){
        this.userAddressesService=userAddressesService;
    }

    public void getUserAddressHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        String userIdFormPath = ctx.pathParam("id");

        if(userIdFormPath == null || userIdFormPath.isEmpty()){
            ctx.status(400);
            ctx.json(new ErrorMessage("User ID is required in the path."));
            return;
        }

        int userAddressId;
        try{
            userAddressId = Integer.parseInt(userIdFormPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid User Address ID format. Must be a number."));
            return;
        }

        UserAddress userAddress = userAddressesService.getUserAddress(userAddressId);

        if(userAddress == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        ctx.status(200);
        ctx.json(userAddress);

    }

    public void registerUserAddressHandler(Context ctx){
        UserAddress requestUserAddress = ctx.bodyAsClass(UserAddress.class);

        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        UserAddress registerUserAddress = userAddressesService.registerNewAddress(
                ctx.sessionAttribute("userId"),
                requestUserAddress.getCountry(),
                requestUserAddress.getState(),
                requestUserAddress.getCity(),
                requestUserAddress.getStreet(),
                requestUserAddress.getHouseNumber(),
                requestUserAddress.getPostalCode()
        );

        if(registerUserAddress == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("New user address registered for userId: "+ctx.sessionAttribute("userId"));

        ctx.status(201);
        ctx.json(registerUserAddress);
    }

    public void updateUserAddressHandler(Context ctx){
        UserAddress requestUserAddress = ctx.bodyAsClass(UserAddress.class);

        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        String userIdFromPath = ctx.pathParam("id"); // Extract userId from path

        if (userIdFromPath == null || userIdFromPath.isEmpty()) {
            ctx.status(400);
            ctx.json(new ErrorMessage("User Address ID is required in the path."));
            return;
        }

        int userAddressId;
        try {
            userAddressId = Integer.parseInt(userIdFromPath); // Parse userId as an integer
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid User Address ID format. Must be a number."));
            return;
        }

        UserAddress registerUserAddress = userAddressesService.updateAddress(
                userAddressId,
                ctx.sessionAttribute("userId"),
                requestUserAddress.getCountry(),
                requestUserAddress.getState(),
                requestUserAddress.getCity(),
                requestUserAddress.getStreet(),
                requestUserAddress.getHouseNumber(),
                requestUserAddress.getPostalCode()
        );

        if(registerUserAddress == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("User address updated with userAddressId: "+userAddressId);

        ctx.status(200);
        ctx.json(registerUserAddress);
    }

    public void updateStatusHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        String userIdFromPath = ctx.pathParam("id"); // Extract userId from path

        if (userIdFromPath == null || userIdFromPath.isEmpty()) {
            ctx.status(400);
            ctx.json(new ErrorMessage("User Address ID is required in the path."));
            return;
        }

        int userAddressId;
        try {
            userAddressId = Integer.parseInt(userIdFromPath); // Parse userId as an integer
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid User Address ID format. Must be a number."));
            return;
        }

        StatusUpdateRequest request = ctx.bodyAsClass(StatusUpdateRequest.class);
        UserAddress registeredUserAddress = userAddressesService.updateStatus(
                userAddressId,
                request.status()
        );

        if(registeredUserAddress == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("User address updated with userAddressId: "+userAddressId);

        ctx.status(200);
        ctx.json(registeredUserAddress);
    }

}
