/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.AuthenticationResponse;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.security.jwt.JwtUtils;
import com.lawfirm.apps.service.EmployeeRoleService;
import com.lawfirm.apps.service.EmployeeService;
import com.lawfirm.apps.response.Response;
import com.lawfirm.apps.service.UserServiceImpl;
import com.lawfirm.apps.support.api.AuthenticationRequest;
import com.lawfirm.apps.support.api.MyUserDetails;
import com.lawfirm.apps.support.api.SignupApi;
import com.lawfirm.apps.utils.CreateLog;
import com.lawfirm.apps.utils.CustomErrorType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author newbiecihuy
 */
//@CrossOrigin(origins = "*", maxAge = 3600)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtTokenUtil;

    @Autowired
    EmployeeService empRepository;
    @Autowired
    UserServiceImpl userRepository;

    @Autowired
    EmployeeRoleService roleRepository;

    final Response rs = new Response();

    @RequestMapping("/")
    public String home() {
//        return "redirect:/login";
        return login();
    }

    @RequestMapping("/login")
    public String login() {
        return "createAuthenticationToken";
//        return "redirect:/sign-in";
    }

    @RequestMapping("/sign-out")
    public String logout() {
        return "createAuthenticationToken";
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            HttpHeaders responseHeaders = new HttpHeaders();
//            Employee cekEmp = empRepository.chekUserName(authenticationRequest.getUsername());
//            
//            Authentication authenticate = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), "lawfirm" + cekEmp.getEmail() + authenticationRequest.getPassword())// encoder.encode(authenticationRequest.getPassword())
//            );
            log.info("getPassword : " + authenticationRequest.getUsername());
            log.info("getPassword : " + authenticationRequest.getPassword());
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())// encoder.encode(authenticationRequest.getPassword())
            );
            AuthenticationResponse response = null;
            //if authentication was succesful else throw an exception
//        log.info("authenticate : " + authenticate);
            final MyUserDetails userDetails = (MyUserDetails) userRepository
                    .loadUserByUsername(authenticationRequest.getUsername());
//            log.info("userDetails : " + userDetails);
            if (userDetails == null) {
                response.setUsername(null);
//                CreateLog.createJson(rs, "signin");
                return new ResponseEntity(new CustomErrorType("55", "Error", " Login Failed For User"),
                        HttpStatus.NOT_FOUND);
            }
            final String jwt = jwtTokenUtil.generateJwtToken(userDetails);
//          log.info("jwt : " + jwt);
            response = new AuthenticationResponse(jwt);
//          int id = (int) (long) userDetails.getId();
            response.setId(userDetails.getId());
            response.setUsername(userDetails.getUsername());
            response.setActive(userDetails.isEnabled());
            response.setEmployeeId(userDetails.getEmployeeId());
            List<String> roles = new ArrayList<>();
            userDetails.getAuthorities().forEach((a) -> roles.add(a.getAuthority()));
            response.setRoles(roles);
            return new ResponseEntity(response, responseHeaders, HttpStatus.OK);

        } catch (AuthenticationException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.toString());
            CreateLog.createJson(ex.toString(), "signin");
            return new ResponseEntity(new CustomErrorType("55", "Error", "Invalid username or password"),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupApi signUpRequest) {
        try {
            Employee entity = new Employee();
            if (!signUpRequest.getUserName().equalsIgnoreCase("sysadmin")) {
                rs.setResponse_code("55");
                rs.setInfo("ca'nt acces this feature");
                rs.setResponse("Create Employee Failed");
                return ResponseEntity
                        .badRequest()
                        .body(rs);
            }
            if (empRepository.existsByUsername(signUpRequest.getUserName())) {
                rs.setResponse_code("55");
                rs.setInfo("You  username :" + signUpRequest.getUserName() + " already Exist");
                rs.setResponse("Create Employee Failed");
                return ResponseEntity
                        .badRequest()
                        .body(rs);
            }

            if (empRepository.existsByEmail(signUpRequest.getEmail())) {
                rs.setResponse_code("55");
                rs.setInfo("You have entered an invalid email address :" + signUpRequest.getEmail() + " already Exist");
                rs.setResponse("Create Employee Failed");
                return ResponseEntity
                        .badRequest()
                        .body(rs);
            }

            // Create new user's account
            entity.setUserName(signUpRequest.getUserName());
            entity.setName(signUpRequest.getUserName());
            entity.setEmail(signUpRequest.getEmail());
            entity.setIsActive(Boolean.TRUE);
            entity.setIsDelete(Boolean.FALSE);
            entity.setIsLogin(Boolean.FALSE);
            entity.setApproved_date(new Date());
            entity.setPassword(encoder.encode(signUpRequest.getEmail() + signUpRequest.getEmail()));
            entity.setRoleName("sysadmin");

            Employee dataEmp = this.empRepository.create(entity);

            if (dataEmp == null) {
                return new ResponseEntity(new CustomErrorType("55", "Fail", "User registered Failed"),
                        HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity(new CustomErrorType("00", "Success", "User registered successfully"),
                        HttpStatus.NOT_FOUND);
            }
        } catch (AuthenticationException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "signup");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

//    @RequestMapping("/logout")
//    public String logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
//        JwtUtils.invalidateRelatedTokens(httpServletRequest);
//        CookieUtil.clear(httpServletResponse, jwtTokenCookieName);
//        return "redirect:/";
//    }
}
