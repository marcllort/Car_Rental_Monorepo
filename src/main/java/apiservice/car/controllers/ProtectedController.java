package apiservice.car.controllers;

import apiservice.car.security.SecurityService;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("protected")
public class ProtectedController {

    @Autowired
    FirebaseAuth firebaseAuth;
    @Autowired
    private SecurityService securityService;

    @GetMapping("data")
    public String getProtectedData() {
        String name = securityService.getUser().getName();
        if (name == null) {
            return "You have accessed protected data from spring boot";
        } else {
            return name.split("\\s+")[0] + ", you have accessed protected data from spring boot";
        }
    }

}
