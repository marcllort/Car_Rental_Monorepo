package apiservice.car.controller.security.roles;

import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    RoleService roleService;
    @Autowired
    FirebaseAuth firebaseAuth;

    @PutMapping("role/add")
    @IsSuper
    public void addRole(@RequestParam String uid, @RequestParam String role) throws Exception {
        roleService.addRole(uid, role);
    }

    @DeleteMapping("role/remove")
    @IsSuper
    public void removeRole(@RequestParam String uid, @RequestParam String role) {
        roleService.removeRole(uid, role);
    }

}
