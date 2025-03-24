package com.revature.controllers;

import com.revature.dtos.request.PasswordUpdateRequest;
import com.revature.dtos.request.StatusUpdateRequest;
import com.revature.dtos.response.ErrorMessage;
import com.revature.models.User;
import com.revature.models.UserResetCode;
import com.revature.models.UserRole;
import com.revature.services.UserResetCodeService;
import com.revature.services.UserService;
import com.revature.util.SendGridUtil;
import com.revature.util.UserResetCodeUtil;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserController {

    // first add the logger

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserResetCodeService userResetCodeService;

    public UserController(UserService userService, UserResetCodeService userResetCodeService){
        this.userService=userService;
        this.userResetCodeService=userResetCodeService;
    }

    // GET
    public void getAllUsersHandler(Context ctx){
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

        ctx.json(userService.getAllUsers());
    }

    public void getUserAddressHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }

        ctx.json(userService.getUserAddresses(ctx.sessionAttribute("userId")));
    }

    // POST
    public void registerUserHandler(Context ctx){

        User requestUser = ctx.bodyAsClass(User.class);

        if(!userService.validateEmail(requestUser.getEmail())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Email is not valid."));
            return;
        }

        if(!userService.validatePassword(requestUser.getPassword())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Password is not valid. It must be at least 8 characters and contain a capital " +
                    ", lowercase letter and at least 1 special character of the pull (!@#$%^&*) "));

            return;
        }

        if(!userService.isEmailAvailable(requestUser.getEmail())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Email is not available, please select a new one"));

            logger.warn("Register attempt made for taken email: "+ requestUser.getEmail());
            return;
        }

        User registeredUser = userService.registerNewUser(
                requestUser.getFirstName(),
                requestUser.getLastName(),
                requestUser.getEmail(),
                requestUser.getPassword()
        );

        if (registeredUser == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("New user registered with username: " + registeredUser.getEmail());

        ctx.status(201);
        ctx.json(registeredUser);
    }

    public void loginHandler(Context ctx){

        User requestUser = ctx.bodyAsClass(User.class);

        User returnedUser = userService.loginUser(requestUser.getEmail(), requestUser.getPassword());

        if (returnedUser == null){
            ctx.json(new ErrorMessage("Username or Password Incorrect"));
            ctx.status(400);
            return;
        }

        if (!returnedUser.isActive()) {
            ctx.status(400);
            ctx.json(new ErrorMessage("User is not active, please contact technical support for more information."));
            logger.warn("Login attempt made for inactive user: "+ requestUser.getEmail());
            return;
        }

        ctx.status(200);
        ctx.json(returnedUser);

        // Add the userId to the session
        ctx.sessionAttribute("userId", returnedUser.getUserId());
        ctx.sessionAttribute("role", returnedUser.getRole());
    }

    public void generatedResetCodeHandler(Context ctx){
        User requestUser = ctx.bodyAsClass(User.class);

        User u = userService.getUserByEmail(requestUser.getEmail());

        if(u == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid email!"));
            return;
        }

        // check if the user have an active user reset code
        UserResetCode activeResetCode =  userResetCodeService.getActiveUserResetCode(u.getUserId());

        if(activeResetCode != null){

            boolean sentEmail = userService.sendResetCodeEmail(requestUser.getEmail(),activeResetCode);

            if(!sentEmail){
                ctx.status(400);
                ctx.json(new ErrorMessage("Error to sent reset code by Email"));
                return;
            }

            ctx.status(201);
            ctx.json(new ErrorMessage("Reset code sent by Email"));
            return;
        }

        // generate reset code
        UserResetCodeUtil userResetCodeUtil = new UserResetCodeUtil();
        String resetCode = userResetCodeUtil.generateResetCode(10);

        while(!userResetCodeService.validateResetCode(resetCode) || !userResetCodeService.isResetCodeExist(resetCode)){
            resetCode = userResetCodeUtil.generateResetCode(10);
        }

        UserResetCode userResetCode = userResetCodeService.registerNewResetCode(
                u.getUserId(),
                resetCode
        );

        if (userResetCode == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        // TODO SEND THE CODE BY EMAIL

        logger.info("New user reset code ("+ resetCode +") registered to userId: " + u.getUserId());

        boolean sentEmail = userService.sendResetCodeEmail(requestUser.getEmail(),userResetCode);

        if(!sentEmail){
            ctx.status(400);
            ctx.json(new ErrorMessage("Error to sent reset code by Email"));
            return;
        }

        ctx.status(201);
        ctx.json(new ErrorMessage("Reset code sent by Email"));
        return;
    }

    // UPDATE
    public void updateUserHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }

        User requestUser = ctx.bodyAsClass(User.class);
        requestUser.setUserId(ctx.sessionAttribute("userId"));

        if(!userService.validateEmail(requestUser.getEmail())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Email is not valid."));
            return;
        }

        if(!userService.validatePassword(requestUser.getPassword())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Password is not valid. It must be at least 8 characters and contain a capital " +
                    ", lowercase letter and at least 1 special character of the pull (!@#$%^&*) "));

            return;
        }

        if(!userService.isEmailAvailableForUpdate(requestUser.getUserId(), requestUser.getEmail())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Email is not available, please select a new one"));
            logger.warn("Update attempt made for taken email: "+ requestUser.getEmail());
            return;
        }

        User registeredUser = userService.updateUser(
                requestUser.getUserId(),
                requestUser.getFirstName(),
                requestUser.getLastName(),
                requestUser.getEmail(),
                requestUser.getPassword()
        );

        if (registeredUser == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("User update successfully: " + registeredUser.getEmail());

        ctx.status(200);
        ctx.json(registeredUser);
    }

    public void updateUserStatusHandler(Context ctx){
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
        String userIdFromPath = ctx.pathParam("id"); // Extract userId from path

        if (userIdFromPath == null || userIdFromPath.isEmpty()) {
            ctx.status(400);
            ctx.json(new ErrorMessage("User ID is required in the path."));
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdFromPath); // Parse userId as an integer
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid User ID format. Must be a number."));
            return;
        }

        int sessionID = (Integer) ctx.sessionAttribute("userId");
        if(userId == sessionID){
            ctx.status(400);
            ctx.json(new ErrorMessage("You can not update the status of your own user"));
            return;
        }


        StatusUpdateRequest request = ctx.bodyAsClass(StatusUpdateRequest.class);
        User registeredUser = userService.updateStatus(
                userId,
                request.status()
        );

        if (registeredUser == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("User status update successfully: " + registeredUser.getEmail());

        ctx.status(200);
        ctx.json(registeredUser);
    }

    public void updateUserPasswordHandler(Context ctx){
        PasswordUpdateRequest request = ctx.bodyAsClass(PasswordUpdateRequest.class);

        if(!userResetCodeService.isResetCodeAvailable(request.resetCode())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid reset code. Please contact technical support for more information."));
            return;
        }

        UserResetCode userResetCode = userResetCodeService.getResetCodeByCode(request.resetCode());

        User updateUser = userService.updatePassword(
                userResetCode.getUserId(),
                request.password()
        );

        if (updateUser == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong updating the password!"));
            return;
        }

        userResetCode = userResetCodeService.updateResetCode(userResetCode.getResetCodeId(), userResetCode.getUserId());

        if(userResetCode == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        ctx.status(200);
        ctx.json(updateUser);
    }


}
