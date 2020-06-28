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
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
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

    @PostMapping("/signin")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())// encoder.encode(authenticationRequest.getPassword())
        );

        //if authentication was succesful else throw an exception
//        log.info("authenticate : " + authenticate);
        final MyUserDetails userDetails = (MyUserDetails) userRepository
                .loadUserByUsername(authenticationRequest.getUsername());
//        log.info("userDetails : " + userDetails);
        final String jwt = jwtTokenUtil.generateJwtToken(userDetails);
//        log.info("jwt : " + jwt);
        AuthenticationResponse response = new AuthenticationResponse(jwt);
//        int id = (int) (long) userDetails.getId();
        response.setId(userDetails.getId());
        response.setUsername(userDetails.getUsername());
        List<String> roles = new ArrayList<>();
        userDetails.getAuthorities().forEach((a) -> roles.add(a.getAuthority()));
        response.setRoles(roles);

        return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);

    }

//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginApi loginRequest) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = jwtUtils.generateJwtToken(authentication);
//
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());
//
////        return ResponseEntity.ok(new JwtResponse(jwt,
////                userDetails.getIdEmployee(),
////                userDetails.getUserName(),
////                userDetails.getEmail(),
////                roles));
//        return ResponseEntity.ok(new JwtResponse(jwt,
//                userDetails.getId(),
//                userDetails.getUsername(),
//                userDetails.getEmail(),
//                roles));
//    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupApi signUpRequest) {
        Employee entity = new Employee();
        if (empRepository.existsByUsername(signUpRequest.getUserName())) {
            rs.setResponse_code("05");
            rs.setInfo("You have entered an invalid email address :" + signUpRequest.getUserName());
            rs.setResponse("Create Employee Failed");
            return ResponseEntity
                    .badRequest()
                    .body(rs);
        }

        if (empRepository.existsByEmail(signUpRequest.getEmail())) {
            rs.setResponse_code("05");
            rs.setInfo("You have entered an invalid email address :" + signUpRequest.getEmail());
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
        entity.setPassword(encoder.encode(signUpRequest.getEmail()));
        entity.setRoleName(signUpRequest.getUserName());
        Employee dataEmp = this.empRepository.create(entity);

        if (dataEmp == null) {
            return new ResponseEntity(new CustomErrorType("05", "Fail", "User registered Failed"),
                    HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(new CustomErrorType("00", "Success", "User registered successfully"),
                    HttpStatus.NOT_FOUND);
        }

    }
}
