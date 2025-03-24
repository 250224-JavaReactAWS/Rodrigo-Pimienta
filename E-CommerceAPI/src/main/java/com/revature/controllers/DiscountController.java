package com.revature.controllers;

import com.revature.config.TimeZoneConfig;
import com.revature.dtos.request.DiscountRequest;
import com.revature.dtos.request.StatusUpdateRequest;
import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Discount;
import com.revature.models.UserRole;
import com.revature.services.DiscountService;
import com.revature.util.DiscountCodeUtil;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DiscountController {
    private final Logger logger = LoggerFactory.getLogger(DiscountController.class);

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService){
        this.discountService=discountService;
    }

    public void getAllDiscountsHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }

        if (ctx.sessionAttribute("role") != UserRole.ADMIN){
            ctx.status(403);
            ctx.json(new ErrorMessage("You must be an admin to access this endpoint!"));
            return;
        }

        ctx.json(discountService.getAllDiscounts());
    }

    public void getUserDiscountsHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }
        ctx.json(discountService.getAvailableUserDiscounts(ctx.sessionAttribute("userId")));
    }

    public void getDiscountHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        String discountIdFormPath = ctx.pathParam("id");

        if(discountIdFormPath == null || discountIdFormPath.isEmpty()){
            ctx.status(400);
            ctx.json(new ErrorMessage("Category ID is required in the path."));
            return;
        }

        int discountId;
        try{
            discountId = Integer.parseInt(discountIdFormPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Discount ID format. Must be a number."));
            return;
        }

        Discount discount = discountService.getDiscount(discountId);

        if(discount == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        ctx.status(200);
        ctx.json(discount);
    }

    // admin
    public void registerDiscountHandler(Context ctx){
        DiscountRequest requestDiscount = ctx.bodyAsClass(DiscountRequest.class);

        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        if (ctx.sessionAttribute("role") != UserRole.ADMIN){
            ctx.status(403);
            ctx.json(new ErrorMessage("You must be an admin to access this endpoint!"));
            return;
        }

        // generate reset code
        DiscountCodeUtil discountCodeUtil = new DiscountCodeUtil();
        String resetCode = discountCodeUtil.generateResetCode(10);

        while(!discountService.validateDiscount(resetCode) || !discountService.isDiscountExist(resetCode)){
            resetCode = discountCodeUtil.generateResetCode(10);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        ZoneId zoneId = ZoneId.of(TimeZoneConfig.ZONE_ID);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(requestDiscount.expiredAt(), formatter.withZone(zoneId));
        OffsetDateTime expiredAt = zonedDateTime.toOffsetDateTime();
        OffsetDateTime now = OffsetDateTime.now(zoneId);

        // check if expired date is after now
        if(!expiredAt.isAfter(now)) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Date of expired should be after current time, please select a new one"));
            logger.warn("Register attempt made invalid expiredAt: "+ requestDiscount.expiredAt());
            return;
        }

        if(requestDiscount.maxUsesPerUser() <= 0){
            ctx.status(400);
            ctx.json(new ErrorMessage("Max uses per user should be grader than 0, please select a new one"));
            logger.warn("Register attempt made invalid maxUsesPerUser: "+ requestDiscount.maxUsesPerUser());
            return;
        }

        if(requestDiscount.percentage() <=0){
            ctx.status(400);
            ctx.json(new ErrorMessage("Percentage should be grader than 0, please select a new one"));
            logger.warn("Register attempt made invalid percentage: "+ requestDiscount.percentage());
            return;
        }

        Discount registeredDiscount = discountService.registerDiscount(
                resetCode,
                requestDiscount.percentage(),
                requestDiscount.maxUsesPerUser(),
                expiredAt
        );

        if(registeredDiscount == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("New discount registered with code: "+resetCode);

        ctx.status(201);
        ctx.json(registeredDiscount);
    }

    // admin
    public void updateDiscountHandler(Context ctx){
        DiscountRequest requestDiscount = ctx.bodyAsClass(DiscountRequest.class);

        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        if (ctx.sessionAttribute("role") != UserRole.ADMIN){
            ctx.status(403);
            ctx.json(new ErrorMessage("You must be an admin to access this endpoint!"));
            return;
        }

        String discountIdFormPath = ctx.pathParam("id");

        if(discountIdFormPath == null || discountIdFormPath.isEmpty()){
            ctx.status(400);
            ctx.json(new ErrorMessage("Category ID is required in the path."));
            return;
        }

        int discountId;
        try{
            discountId = Integer.parseInt(discountIdFormPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Discount ID format. Must be a number."));
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        ZoneId zoneId = ZoneId.of(TimeZoneConfig.ZONE_ID);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(requestDiscount.expiredAt(), formatter.withZone(zoneId));
        OffsetDateTime expiredAt = zonedDateTime.toOffsetDateTime();
        OffsetDateTime now = OffsetDateTime.now(zoneId);

        // check if expired date is after now
        if(!expiredAt.isAfter(now)) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Date of expired should be after current time, please select a new one"));
            logger.warn("Register attempt made invalid expiredAt: "+ requestDiscount.expiredAt());
            return;
        }


        Discount registeredDiscount = discountService.updateDiscount(
                discountId,
                requestDiscount.percentage(),
                requestDiscount.maxUsesPerUser(),
                expiredAt
        );

        if(registeredDiscount == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("Discount updated with discount ID: "+discountId);

        ctx.status(200);
        ctx.json(registeredDiscount);
    }

    // admin
    public void updateStatusHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        if (ctx.sessionAttribute("role") != UserRole.ADMIN){
            ctx.status(403);
            ctx.json(new ErrorMessage("You must be an admin to access this endpoint!"));
            return;
        }

        String discountIdFormPath = ctx.pathParam("id");

        if(discountIdFormPath == null || discountIdFormPath.isEmpty()){
            ctx.status(400);
            ctx.json(new ErrorMessage("Category ID is required in the path."));
            return;
        }

        int discountId;
        try{
            discountId = Integer.parseInt(discountIdFormPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Discount ID format. Must be a number."));
            return;
        }

        StatusUpdateRequest request = ctx.bodyAsClass(StatusUpdateRequest.class);
        Discount registeredDiscount = discountService.updateStatus(
                discountId,
                request.status()
        );

        if(registeredDiscount == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("Discount status updated with discount ID: "+discountId);

        ctx.status(200);
        ctx.json(registeredDiscount);
    }
}
