package com.codewithsm.springsecuritywithjwtauth.controller;

import com.codewithsm.springsecuritywithjwtauth.dto.ReqRes;
import com.codewithsm.springsecuritywithjwtauth.model.Product;
import com.codewithsm.springsecuritywithjwtauth.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class AdminUsers {

//    private static final Logger logger = LoggerFactory.getLogger(AdminUsers.class);

    private final ProductRepo productRepo;

    @Autowired
    public AdminUsers(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping("/public/product")
    public ResponseEntity<List<Product>> getAllProducts(){
        try {
            List<Product> products = productRepo.findAll();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
//            logger.error("Error fetching products", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/admin/saveproduct")
    public ResponseEntity<Product> saveProduct(@RequestBody ReqRes productRequest){
        try {
            Product productToSave = new Product();
            productToSave.setName(productRequest.getName());
            Product savedProduct = productRepo.save(productToSave);
            return ResponseEntity.status(201).body(savedProduct);
        } catch (Exception e) {
//            logger.error("Error saving product", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/user/alone")
    public ResponseEntity<String> userAlone(){
        return ResponseEntity.ok("Users alone can access this API only");
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/adminuser/both")
    public ResponseEntity<String> bothAdminAndUsersApi(){
        return ResponseEntity.ok("Both Admin and Users can access this API");
    }
}



//==========================default one=================================================================================

//package com.codewithsm.springsecuritywithjwtauth.controller;
//
//import com.codewithsm.springsecuritywithjwtauth.dto.ReqRes;
//import com.codewithsm.springsecuritywithjwtauth.model.Product;
//import com.codewithsm.springsecuritywithjwtauth.repository.ProductRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AdminUsers {
//
//    @Autowired
//    private ProductRepo productRepo;
//
//    @GetMapping("/public/product")
//    public ResponseEntity<Object> getAllProducts(){
//        return ResponseEntity.ok(productRepo.findAll());
//    }
//
//    @PostMapping("/admin/saveproduct")
//    public ResponseEntity<Object> saveProduct(@RequestBody ReqRes productRequest){
//        Product productToSave = new Product();
//        productToSave.setName(productRequest.getName());
//        return ResponseEntity.ok(productRepo.save(productToSave));
//    }
//
//    @GetMapping("/user/alone")
//    public ResponseEntity<Object> userAlone(){
//        return ResponseEntity.ok("USers alone can access this ApI only");
//    }
//
//    @GetMapping("/adminuser/both")
//    public ResponseEntity<Object> bothAdminaAndUsersApi(){
//        return ResponseEntity.ok("Both Admin and Users Can  access the api");
//    }
//
//    /** You can use this to get the details(name,email,role,ip, e.t.c) of user accessing the service*/
////    @GetMapping("/public/email")
////    public String getCurrentUserEmail() {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        System.out.println(authentication); //get all details(name,email,password,roles e.t.c) of the user
////        System.out.println(authentication.getDetails()); // get remote ip
////        System.out.println(authentication.getName()); //returns the email because the email is the unique identifier
////        return authentication.getName(); // returns the email
////    }
//
//}
