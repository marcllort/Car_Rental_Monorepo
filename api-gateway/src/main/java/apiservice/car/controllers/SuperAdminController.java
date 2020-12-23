package apiservice.car.controllers;

import apiservice.car.security.SecurityService;
import apiservice.car.security.roles.IsSuper;
import apiservice.car.security.roles.RoleService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("super")
public class SuperAdminController {

    @Autowired
    RoleService securityRoleService;
    @Autowired
    FirebaseAuth firebaseAuth;
    @Autowired
    private SecurityService securityService;

    @GetMapping("user")
    @IsSuper
    public UserRecord getUser(@RequestParam String email) throws Exception {
        return firebaseAuth.getUserByEmail(email);
    }

    @GetMapping("data")
    @IsSuper
    public String getSuperData() {
        String name = securityService.getUser().getName();
        return name.split("\\s+")[0] + ", you have accessed super data from spring boot";
    }

}
