package apiservice.car.controllers;

import apiservice.car.model.UserRequest;
import apiservice.car.security.SecurityService;
import apiservice.car.security.roles.IsSuper;
import apiservice.car.security.roles.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin")
public class SuperAdminController {

    @Autowired
    RoleService securityRoleService;
    @Autowired
    FirebaseAuth firebaseAuth;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("user")
    @IsSuper
    public UserRecord getUser(@RequestParam String emailOrUid) throws Exception {
        if (emailOrUid.contains("@")) {
            return firebaseAuth.getUserByEmail(emailOrUid);
        } else {
            return firebaseAuth.getUser(emailOrUid);
        }
    }

    @GetMapping("list-users")
    @IsSuper
    public List<ExportedUserRecord> getListUsers() throws Exception {

        ListUsersPage page = firebaseAuth.listUsers(null);
        List<ExportedUserRecord> users = new ArrayList<>();
        while (page != null) {
            for (ExportedUserRecord user : page.getValues()) {
                users.add(user);
            }
            page = page.getNextPage();
        }
        return users;

    }

    @PostMapping("create-user")
    @IsSuper
    public UserRecord postNewUser(@RequestBody UserRequest request) throws Exception {

        UserRecord.CreateRequest userRequest = new UserRecord.CreateRequest()
                .setEmail(request.getEmail())
                .setEmailVerified(false)
                .setPassword(request.getPassword())
                .setDisplayName(request.getDisplayName())
                .setDisabled(false);

        UserRecord response = firebaseAuth.createUser(userRequest);

        if (request.getCustomClaim() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(request.getCustomClaim(), "true");
            firebaseAuth.setCustomUserClaims(response.getUid(), map);
        }

        response = firebaseAuth.getUserByEmail(request.getEmail());

        return response;
    }

    @GetMapping("reset-password")
    @IsSuper
    public String getResetPassword(@RequestParam String email) throws Exception {

        String response = firebaseAuth.generatePasswordResetLink(email);

        return response;
    }

    @GetMapping("activate-account")
    @IsSuper
    public String getActivateAccount(@RequestParam String email) throws Exception {

        String response = firebaseAuth.generateEmailVerificationLink(email);

        return response;
    }

    @GetMapping("revoke-token")
    @IsSuper
    public String getRevokeToken(@RequestParam String emailOrUid) throws Exception {
        UserRecord user = firebaseAuth.getUserByEmail(emailOrUid);
        try {
            if (emailOrUid.contains("@")) {
                emailOrUid = user.getUid();
            }
            firebaseAuth.revokeRefreshTokens(emailOrUid);
        } catch (FirebaseAuthException e) {
            return "User " + emailOrUid + " tokens couldn't be revoked. " + e.getLocalizedMessage();
        }
        if (user.getDisplayName() != null) {
            return "User " + user.getDisplayName() + " tokens successfully revoked!";
        } else {
            return "User " + user.getEmail() + " tokens successfully revoked!";
        }
    }

    @PostMapping("set-claim")
    @IsSuper
    public UserRecord postSetClaim(@RequestBody UserRequest request) throws Exception {

        UserRecord user = firebaseAuth.getUserByEmail(request.getEmail());

        if (request.getCustomClaim() != null) {
            Map<String, Object> map = user.getCustomClaims();
            map.put(request.getCustomClaim(), "true");
            firebaseAuth.setCustomUserClaims(user.getUid(), map);
        }

        user = firebaseAuth.getUserByEmail(request.getEmail());

        return user;
    }

    @PostMapping("update-user")
    @IsSuper
    public UserRecord postUpdateUser(@RequestBody UserRequest request) throws Exception {

        UserRecord user = firebaseAuth.getUserByEmail(request.getEmail());
        UserRecord.UpdateRequest userRequest = new UserRecord.UpdateRequest(user.getUid());

        if (request.getDisplayName() != null) {
            userRequest.setDisplayName(request.getDisplayName());
        }
        if (request.getCustomClaim() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(request.getCustomClaim(), "true");
            userRequest.setCustomClaims(map);
        }
        if (request.getPassword() != null) {
            userRequest.setPassword(request.getPassword());
        }

        UserRecord response = firebaseAuth.updateUser(userRequest);

        return response;
    }

    @DeleteMapping("delete-user")
    @IsSuper
    public String deleteUser(@RequestParam String emailOrUid) throws Exception {
        UserRecord user = firebaseAuth.getUserByEmail(emailOrUid);
        try {
            if (emailOrUid.contains("@")) {
                emailOrUid = user.getUid();
            }
            firebaseAuth.deleteUser(emailOrUid);
        } catch (FirebaseAuthException e) {
            return "User " + emailOrUid + " couldn't be deleted. " + e.getLocalizedMessage();
        }
        if (user.getDisplayName() != null) {
            return "User " + user.getDisplayName() + " successfully deleted!";
        } else {
            return "User " + user.getEmail() + " successfully deleted!";
        }
    }

    @GetMapping("data")
    @IsSuper
    public String getSuperData() {
        String name = securityService.getUser().getName();
        if (name == null) {
            return "You have accessed super data from spring boot";
        } else {
            return name.split("\\s+")[0] + ", you have accessed super data from spring boot.";
        }
    }

}
