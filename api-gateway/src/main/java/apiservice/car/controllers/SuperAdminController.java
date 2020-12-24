package apiservice.car.controllers;

import apiservice.car.model.UserRequest;
import apiservice.car.security.roles.IsSuper;
import apiservice.car.security.roles.RoleService;
import com.google.firebase.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("admin")
public class SuperAdminController {

    @Autowired
    RoleService securityRoleService;
    @Autowired
    FirebaseAuth firebaseAuth;

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
    public List<ExportedUserRecord> getListUsers(@RequestParam Optional<Integer> maxResults) throws Exception {
        ListUsersPage page;
        if (maxResults.isPresent()) {
            page = firebaseAuth.listUsers(null, maxResults.get());
        } else {
            page = firebaseAuth.listUsers(null, 10);
        }

        List<ExportedUserRecord> users = new ArrayList<>();
        //while (page != null) {
            for (ExportedUserRecord user : page.getValues()) {
                users.add(user);
            }
          //  page = page.getNextPage();
        //}
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

        if (request.getDisabled().equals("true")) {
            userRequest.setDisabled(true);
        }
        if (request.getEmailVerified().equals("true")) {
            userRequest.setEmailVerified(true);
        }
        if (request.getPhoneNumber() != null) {
            userRequest.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getPhotoURL() != null) {
            userRequest.setPhotoUrl(request.getPhotoURL());
        }

        UserRecord response = firebaseAuth.createUser(userRequest);

        if (request.getCustomClaim() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(request.getCustomClaim(), "true");
            firebaseAuth.setCustomUserClaims(response.getUid(), map);
        }

        response = firebaseAuth.getUserByEmail(request.getEmail());

        return response;
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
        if (request.getDisabled() != null && request.getDisabled().equals("true")) {
            userRequest.setDisabled(true);
        }
        if (request.getEmailVerified() != null && request.getEmailVerified().equals("true")) {
            userRequest.setEmailVerified(true);
        }
        if (request.getPhoneNumber() != null) {
            userRequest.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getPhotoURL() != null) {
            userRequest.setPhotoUrl(request.getPhotoURL());
        }

        return firebaseAuth.updateUser(userRequest);
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

    @GetMapping("activate-account")
    @IsSuper
    public String getActivateAccount(@RequestParam String email) throws Exception {
        return firebaseAuth.generateEmailVerificationLink(email);
    }

    @GetMapping("reset-password")
    @IsSuper
    public String getResetPassword(@RequestParam String email) throws Exception {
        return firebaseAuth.generatePasswordResetLink(email);
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

}
