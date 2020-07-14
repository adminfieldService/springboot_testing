/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.Account;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.EmployeeRole;
import com.lawfirm.apps.service.CaseDocumentService;
import com.lawfirm.apps.service.interfaces.AccountServiceIface;
import com.lawfirm.apps.service.interfaces.CaseDetailsServiceIface;
import com.lawfirm.apps.service.interfaces.ClientDataServiceIface;
import com.lawfirm.apps.service.interfaces.DocumentReimburseServiceIface;
import com.lawfirm.apps.service.interfaces.EmployeeRoleServiceIface;
import com.lawfirm.apps.service.interfaces.EmployeeServiceIface;
import com.lawfirm.apps.service.interfaces.EngagementServiceIface;
import com.lawfirm.apps.service.interfaces.FinancialServiceIface;
import com.lawfirm.apps.service.interfaces.LoanServiceIface;
import com.lawfirm.apps.service.interfaces.LoanTypeServiceIface;
import com.lawfirm.apps.service.interfaces.MemberServiceIface;
import com.lawfirm.apps.service.interfaces.ProfessionalServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import com.lawfirm.apps.support.api.AprovedApi;
import com.lawfirm.apps.support.api.DataEmployee;
import com.lawfirm.apps.utils.CustomErrorType;
import com.lawfirm.apps.response.Response;
import com.lawfirm.apps.support.api.AccountApi;
import com.lawfirm.apps.utils.CreateLog;
import com.lawfirm.apps.utils.Util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.ServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jpos.iso.ISOUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

/**
 *
 * @author newbiecihuy
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RequestMapping({"/employee"})
public class EmployeeController { //LawfirmController

    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;

    SimpleDateFormat sdfMonth;// = new SimpleDateFormat("MM");
    SimpleDateFormat sdfYear;// = new SimpleDateFormat("yyyy");
    Date now;
    String date_now;
    String notif = null;
    String jsonString = null;
    static String basepathUpload = "/opt/lawfirm/UploadFile/";
//    static String basepathUpload = "C:\\opt\\lawfirm\\UploadFile\\";
    String json = null;
    final Response rs = new Response();
    @Autowired
    EmployeeServiceIface employeeService;
    @Autowired
    EmployeeRoleServiceIface employeeRoleService;
    @Autowired
    AccountServiceIface accountService;
    @Autowired
    CaseDetailsServiceIface caseDetailsService;
    @Autowired
    CaseDocumentService caseDocumentService;
    @Autowired
    ClientDataServiceIface clientDataService;
    @Autowired
    DocumentReimburseServiceIface documentReimburseService;
    @Autowired
    EngagementServiceIface engagementService;
    @Autowired
    FinancialServiceIface financialService;
    @Autowired
    LoanServiceIface loanService;
    @Autowired
    LoanTypeServiceIface loanTypeService;
    @Autowired
    MemberServiceIface memberService;
    @Autowired
    ProfessionalServiceIface professionalService;
    @Autowired
    ReimbursementServiceIface reimbursementService;
    @Autowired
    TeamMemberServiceIface teamMemberService;
    @Autowired
    PasswordEncoder encoder;
    private static final AtomicInteger count = new AtomicInteger(1);

    public EmployeeController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfMonth = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.sdfYear = new SimpleDateFormat("yyyy-MM-dd");
    }

//    @PostMapping(path = "/managed-employee", consumes = {"multipart/form-data"}, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
////    @PreAuthorize("hasRole('admin') or hasRole('sysadmin')")
//    public Response createEmployee(@RequestPart("jsonfield") final DataEmployee object, @RequestPart("doc_cv") MultipartFile file, Authentication authenticationRequest) {//@RequestParam(value = "doc_cv", required = false) MultipartFile file,
//        try {
//
////            System.out.print("isi object" + object.toString());
//            System.out.print("isi JwtResponse " + authenticationRequest.getName());
//
//            String email = object.getEmail();
//            String nik = object.getNik();
//            String name = object.getName();
//            String address = object.getAddress();
//            String tax_status = object.getTax_status();
//            String mobile_phone = object.getCell_phone();
//            String gender = object.getGender();
//            String npwp = object.getNpwp();
//            String role_name = object.getRole_name();
//            String bank_name_p = object.getBank_name_p();
//            String account_number_p = object.getAccount_number_p();
//            String account_name_p = object.getAccount_name_p();
//            String bank_name_l = object.getBank_name_l();
//            String account_number_l = object.getAccount_number_l();
//            String account_name_l = object.getAccount_name_l();
//            Boolean cheking = Util.validation(email);
//            Boolean process = true;
//            if (authenticationRequest.getPrincipal().toString().equalsIgnoreCase("admin") || authenticationRequest.getPrincipal().toString().equalsIgnoreCase("sysadmin")) {
//                rs.setResponse_code(HttpStatus.FORBIDDEN.toString());
//                rs.setInfo("fail");
//                rs.setResponse("role : " + authenticationRequest.getPrincipal() + ", cannot access managed-employee, Permission denied");
//                process = false;
//                CreateLog.createJson(rs, "create-employee");
//            }
//            if (cheking.equals(false)) {
//
//                rs.setResponse_code("05");
//                rs.setInfo("You have entered an invalid email address :" + email);
//                rs.setResponse("Create Employee Failed");
//                CreateLog.createJson(rs, "create-employee");
//
//            } else {
//
//                Employee cekEMail = employeeService.findByEmployee(email);
//                System.out.println("cekEmail" + cekEMail);
//                if (cekEMail != null) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Email :" + email + "already registered ");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                Employee cekNik = employeeService.findByEmployee(nik);
//                System.out.println("cekNik" + cekNik);
//                if (cekNik != null) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Nik :" + nik + "already registered ");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                if (name.length() > 20) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field Name maksimum 20 character");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                if (name.length() == 0) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field Name can't be empty");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                if (npwp.length() == 0) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field NPWP can't be empty");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                if (address.length() == 0) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field Address can't be empty");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                if (nik.length() == 0) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field Nik can't be empty");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                if (email.length() == 0) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field Email can't be empty");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                if (tax_status.length() == 0) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field Taxt Can't be empty");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                if (mobile_phone.length() == 0) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field Mobile Can't be empty");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//
//                if (object.getBank_name_p().length() == 0 || object.getBank_name_l().length() == 0) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field BANK NAME Can't be empty");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                if (object.getAccount_number_p().length() == 0 || object.getAccount_number_l().length() == 0) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field ACCOUNT NUMBER Can't be empty");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                if (object.getAccount_name_p().length() == 0 || object.getAccount_name_l().length() == 0) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field ACCOUNT NAME Can't be empty");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//
//                if (email.length() > 25) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field Email Maximum 25 character");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//
//                if (mobile_phone.length() > 20) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field Mobile  Maximum 20 character");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                if (address.length() > 200) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field Address Maximum 200 character");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//
//                if (account_name_l.length() > 20 || account_name_p.length() > 20) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field ACCOUNT NAME Maximum 20 character");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//
//                if (bank_name_l.length() > 20 || bank_name_p.length() > 20) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field BANK NAME Maximum 200 character");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                if (account_number_l.length() > 20 || account_number_p.length() > 20) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Field BANK NAME Maximum 20 character");
//                    rs.setResponse("Create Employee Failed");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                Account cekAcp = accountService.findAccount(account_number_p);
//                if (cekAcp != null) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Failed");
//                    rs.setResponse("Account Number : " + account_number_p + " already Registered " + cekAcp.getAccountName());
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                Account cekAcl = accountService.findAccount(account_number_l);
//                if (cekAcl != null) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Failed");
//                    rs.setResponse("Account Number : " + account_number_l + " already Registered " + cekAcl.getAccountName());
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
////                }
//                Employee dataAdmin = this.employeeService.findById(object.getId_employee_admin());
//                if (dataAdmin == null) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Failed");
//                    rs.setResponse("Your Account : " + dataAdmin + " Not Found");
//                    CreateLog.createJson(rs, "create-employee");
//                    process = false;
//                }
//                
//                if (process) {
//
//                    Employee newEmployee = new Employee();
//                    Account accP = new Account();
//                    Account accl = new Account();
//
//                    newEmployee.setName(name);
//                    newEmployee.setUserName(name);
//                    newEmployee.setMobilePhone(mobile_phone);
//                    newEmployee.setAddress(address);
//                    newEmployee.setNik(nik);
//                    newEmployee.setEmail(email);
//                    newEmployee.setNpwp(npwp);
//                    newEmployee.setTaxStatus(tax_status);
//                    if (object.getUser_pass() != null) {
//                        newEmployee.setPassword(object.getUser_pass());
//                    } else {
//                        newEmployee.setPassword(encoder.encode(email));
//                    }
//
//                    newEmployee.setParentId(dataAdmin);
//                    newEmployee.setRoleName(role_name);
//                    newEmployee.setApproved_date(new Date());
//                    Integer number = 1;
//
//                    if (object.getLoan_limit() != null) {
//                        newEmployee.setLoanAmount(object.getLoan_limit());
//                    }
//                    if (role_name.equalsIgnoreCase("lawyer") || role_name.equalsIgnoreCase("law")) {
//                        Employee chekEmpId = this.employeeService.findByEmployeeId("LAW" + number);
//                        if (chekEmpId != null) {
//                            number = count.incrementAndGet();
//                            newEmployee.setEmployeeId("LAW" + Util.setNumber(number.toString()));
//                        } else {
//                            number = count.incrementAndGet();
//                            newEmployee.setEmployeeId("LAW" + Util.setNumber(number.toString()));
//                        }
//                    }
//                    if (role_name.equalsIgnoreCase("dmp")) {
//                        Employee chekEmpId = this.employeeService.findByEmployeeId("DMP" + number);
//                        if (chekEmpId != null) {
//                            number = count.incrementAndGet();
//                            newEmployee.setEmployeeId("DMP" + Util.setNumber(number.toString()));
//                        } else {
//                            number = count.incrementAndGet();
//                            newEmployee.setEmployeeId("DMP" + Util.setNumber(number.toString()));
//                        }
//                    }
//                    if (role_name.equalsIgnoreCase("finance") || role_name.equalsIgnoreCase("fin")) {
//                        Employee chekEmpId = this.employeeService.findByEmployeeId("FIN" + number);
//                        if (chekEmpId != null) {
//                            number = count.incrementAndGet();
//                            newEmployee.setEmployeeId("FIN" + Util.setNumber(number.toString()));
//                        } else {
//                            number = count.incrementAndGet();
//                            newEmployee.setEmployeeId("FIN" + Util.setNumber(number.toString()));
//                        }
//
//                    }
//                    if (role_name.equalsIgnoreCase("support") || role_name.equalsIgnoreCase("sup")
//                            || role_name.equalsIgnoreCase("suport") || role_name.equalsIgnoreCase("supp")) {
//                        Employee chekEmpId = this.employeeService.findByEmployeeId("FIN" + number);
//                        if (chekEmpId != null) {
//                            number = count.incrementAndGet();
//                            newEmployee.setEmployeeId("SUP" + Util.setNumber(number.toString()));
//                        } else {
//                            number = count.incrementAndGet();
//                            newEmployee.setEmployeeId("SUP" + Util.setNumber(number.toString()));
//                        }
//                    }
////                    dataEmployee.setRoleName(object.getRole_name());
//                    if (gender.equalsIgnoreCase("L")
//                            || gender.equalsIgnoreCase("laki")
//                            || gender.equalsIgnoreCase("pria")
//                            || gender.equalsIgnoreCase("male")
//                            || gender.equalsIgnoreCase("m")) {
//                        newEmployee.setGender("m");
//                    } else {
//                        newEmployee.setGender("f");
//                    }
//
////                    if (demployee != null) {
////                        accP.setEmployee(newEmployee);
//                    accP.setAccountName(account_name_p);
//                    accP.setBankName(bank_name_p);
//                    accP.setAccountNumber(account_number_p);
//                    accP.setIsActive(false);
//                    accP.setTypeAccount("payroll");
//                    newEmployee.addAccount(accP);
////                        
//
//                    accl.setAccountName(account_name_l);
//                    accl.setBankName(bank_name_l);
//                    accl.setAccountNumber(account_number_l);
//                    accP.setIsActive(false);
//                    accl.setTypeAccount("loan");
//                    newEmployee.addAccount(accl);
//                    Employee dEmployee = employeeService.create(newEmployee);
////                    if (!file.isEmpty()) {
//                    saveUploadedFile(file, dEmployee, 0, authenticationRequest);
////                    } else {
////                        saveUploadedFile(file, dEmployee, 0, authenticationRequest);
////                    }
////                    }
//                }
//            }
//        } catch (IOException ex) {
//            // TODO Auto-generated catch block
//            System.out.println("ERROR: " + ex.getMessage());
//            CreateLog.createJson(ex.getMessage(), "create-employee");
//        }
////        rs.setResponse_code("05");
////        rs.setInfo("Data null");
////        rs.setResponse("Create Employee Failed");
//        return rs;
//    }
//    @PostMapping(path = "/managed-employee", consumes = {"multipart/form-data"}, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    @PreAuthorize("hasRole('admin') or hasRole('sysadmin')")//RequestPart
//    public Response createEmployee(@RequestPart("jsonfield") final DataEmployee object, Authentication authenticationRequest) {//@RequestParam(value = "doc_cv", required = false) MultipartFile file,
    @PostMapping(path = "/managed-employee", produces = {"application/json"})
    public Response createEmployee(@RequestBody final DataEmployee object, Authentication authenticationRequest) {
        try {

//            System.out.print("isi object" + object.toString());
            System.out.print("isi JwtResponse " + authenticationRequest.getName());

            String email = object.getEmail();
            String nik = object.getNik();
            String name = object.getName();
            String address = object.getAddress();
            String tax_status = object.getTax_status();
            String mobile_phone = object.getCell_phone();
            String gender = object.getGender();
            String npwp = object.getNpwp();
            String role_name = object.getRole_name();
            String bank_name_p = object.getBank_name_p();
            String account_number_p = object.getAccount_number_p();
            String account_name_p = object.getAccount_name_p();
            String bank_name_l = object.getBank_name_l();
            String account_number_l = object.getAccount_number_l();
            String account_name_l = object.getAccount_name_l();
            Boolean cheking = Util.validation(email);
            Boolean process = true;
            if (authenticationRequest.getPrincipal().toString().equalsIgnoreCase("admin") || authenticationRequest.getPrincipal().toString().equalsIgnoreCase("sysadmin")) {
                rs.setResponse_code(HttpStatus.FORBIDDEN.toString());
                rs.setInfo("fail");
                rs.setResponse("role : " + authenticationRequest.getPrincipal() + ", cannot access managed-employee, Permission denied");
                process = false;
                CreateLog.createJson(rs, "create-employee");
            }
            if (cheking.equals(false)) {

                rs.setResponse_code("05");
                rs.setInfo("You have entered an invalid email address :" + email);
                rs.setResponse("Create Employee Failed");
                CreateLog.createJson(rs, "create-employee");

            } else {

                Employee cekEMail = employeeService.findByEmployee(email);
                System.out.println("cekEmail" + cekEMail);
                if (cekEMail != null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Email :" + email + "already registered ");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                Employee cekNik = employeeService.findByEmployee(nik);
                System.out.println("cekNik" + cekNik);
                if (cekNik != null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Nik :" + nik + "already registered ");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (name.length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Name maksimum 20 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (name.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Name can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (npwp.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field NPWP can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (address.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Address can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (nik.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Nik can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (email.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Email can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (tax_status.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Taxt Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (mobile_phone.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Mobile Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }

                if (object.getBank_name_p().length() == 0 || object.getBank_name_l().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field BANK NAME Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (object.getAccount_number_p().length() == 0 || object.getAccount_number_l().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field ACCOUNT NUMBER Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (object.getAccount_name_p().length() == 0 || object.getAccount_name_l().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field ACCOUNT NAME Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }

                if (email.length() > 30) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Email Maximum 30 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }

                if (mobile_phone.length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Mobile  Maximum 20 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (address.length() > 200) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Address Maximum 200 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }

                if (account_name_l.length() > 20 || account_name_p.length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field ACCOUNT NAME Maximum 20 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }

                if (bank_name_l.length() > 20 || bank_name_p.length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field BANK NAME Maximum 200 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (account_number_l.length() > 20 || account_number_p.length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field BANK NAME Maximum 20 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                Account cekAcp = accountService.findAccount(account_number_p);
                if (cekAcp != null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Account Number : " + account_number_p + " already Registered " + cekAcp.getAccountName());
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                Account cekAcl = accountService.findAccount(account_number_l);
                if (cekAcl != null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Account Number : " + account_number_l + " already Registered " + cekAcl.getAccountName());
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
//                }
                Employee dataAdmin = this.employeeService.findById(object.getId_employee_admin());
                if (dataAdmin == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Your Account : " + dataAdmin + " Not Found");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                EmployeeRole cekRole = this.employeeRoleService.findByName(object.getRole_name());
                if (cekRole == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Your Role Name :" + role_name + ", is not registered ");
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                }
                if (process) {

                    Employee newEmployee = new Employee();
                    Account accP = new Account();
                    Account accl = new Account();

                    newEmployee.setName(name);
                    if (object.getUser_name() == null) {
                        newEmployee.setUserName(email.toLowerCase().trim());
                    } else {
                        newEmployee.setUserName(object.getUser_name().toLowerCase().trim());
                    }
                    newEmployee.setMobilePhone(mobile_phone);
                    newEmployee.setAddress(address);
                    newEmployee.setNik(nik);
                    newEmployee.setEmail(email);
                    newEmployee.setNpwp(npwp);
                    newEmployee.setTaxStatus(tax_status);
//                    if (object.getUser_pass() != null) {
//                        newEmployee.setPassword(object.getUser_pass());
//                    } else {
//                        newEmployee.setPassword(object.getUser_pass());
//                    }
                    if (object.getUser_pass() != null) {
                        newEmployee.setPassword(encoder.encode(object.getUser_pass()));
                    } else {
                        newEmployee.setPassword(encoder.encode(email));
                    }

                    newEmployee.setParentId(dataAdmin);
                    newEmployee.setRoleName(role_name);
                    newEmployee.setApproved_date(new Date());
                    Integer number = 0;

                    if (object.getLoan_limit() != null) {
                        newEmployee.setLoanAmount(object.getLoan_limit());
                    } else {
                        newEmployee.setLoanAmount(0d);
                    }
                    if (role_name.equalsIgnoreCase("admin") || role_name.equalsIgnoreCase("adm")) {
                        number = this.employeeService.generateEmpId("admin");
                        Employee chekEmpId = this.employeeService.findByEmployeeId("ADM" + Util.setNumber(number.toString()));
                        if (chekEmpId == null) {
                            number = 1;
                            newEmployee.setEmployeeId("ADM" + Util.setNumber(number.toString()));
                        } else {
                            number = number + 1;
                            newEmployee.setEmployeeId("ADM" + Util.setNumber(number.toString()));
                        }
                    }
                    if (role_name.equalsIgnoreCase("lawyer") || role_name.equalsIgnoreCase("law")) {
                        number = this.employeeService.generateEmpId("lawyer");
                        Employee chekEmpId = this.employeeService.findByEmployeeId("LAW" + Util.setNumber(number.toString()));
                        if (chekEmpId == null) {
                            number = 1;
                            newEmployee.setEmployeeId("LAW" + Util.setNumber(number.toString()));
                        } else {
                            number = number + 1;
                            newEmployee.setEmployeeId("LAW" + Util.setNumber(number.toString()));
                        }
                    }

                    if (role_name.equalsIgnoreCase("dmp")) {
                        number = this.employeeService.generateEmpId("dmp");
                        Employee chekEmpId = this.employeeService.findByEmployeeId("DMP" + Util.setNumber(number.toString()));
                        if (chekEmpId == null) {
                            number = 1;
                            newEmployee.setEmployeeId("DMP" + Util.setNumber(number.toString()));
                        } else {
                            number = number + 1;
                            newEmployee.setEmployeeId("DMP" + Util.setNumber(number.toString()));
                        }
                    }
                    if (role_name.equalsIgnoreCase("finance") || role_name.equalsIgnoreCase("fin")) {
                        number = this.employeeService.generateEmpId("finance");
                        Employee chekEmpId = this.employeeService.findByEmployeeId("FIN" + Util.setNumber(number.toString()));
                        if (chekEmpId == null) {
                            number = 1;
                            newEmployee.setEmployeeId("FIN" + Util.setNumber(number.toString()));
                        } else {
                            number = number + 1;
                            newEmployee.setEmployeeId("FIN" + Util.setNumber(number.toString()));
                        }

                    }
                    if (role_name.equalsIgnoreCase("support") || role_name.equalsIgnoreCase("sup")
                            || role_name.equalsIgnoreCase("suport") || role_name.equalsIgnoreCase("supp")) {
                        number = this.employeeService.generateEmpId("support");
                        Employee chekEmpId = this.employeeService.findByEmployeeId("SUP" + Util.setNumber(number.toString()));
                        if (chekEmpId == null) {
                            number = 1;
                            newEmployee.setEmployeeId("SUP" + Util.setNumber(number.toString()));
                        } else {
                            number = number + 1;
                            newEmployee.setEmployeeId("SUP" + Util.setNumber(number.toString()));
                        }
                    }
//                    dataEmployee.setRoleName(object.getRole_name());
                    if (gender.equalsIgnoreCase("L")
                            || gender.equalsIgnoreCase("laki")
                            || gender.equalsIgnoreCase("pria")
                            || gender.equalsIgnoreCase("male")
                            || gender.equalsIgnoreCase("m")) {
                        newEmployee.setGender("m");
                    } else {
                        newEmployee.setGender("f");
                    }

//                    if (demployee != null) {
//                        accP.setEmployee(newEmployee);
                    accP.setAccountName(account_name_p);
                    accP.setBankName(bank_name_p);
                    accP.setAccountNumber(account_number_p);
                    accP.setIsActive(false);
                    accP.setTypeAccount("payroll");
                    newEmployee.addAccount(accP);
//                        

                    accl.setAccountName(account_name_l);
                    accl.setBankName(bank_name_l);
                    accl.setAccountNumber(account_number_l);
                    accP.setIsActive(false);
                    accl.setTypeAccount("loan");
                    newEmployee.addAccount(accl);
                    Employee dEmployee = employeeService.create(newEmployee);

                    AprovedApi obj = new AprovedApi();
                    obj.setId_employee_admin(dEmployee.getParentId().getIdEmployee());
                    obj.setId_employee(dEmployee.getIdEmployee());
                    return approvedByAdmin(obj);
                }
            }
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "create-employee");
        }
//        rs.setResponse_code("05");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");
        return rs;
    }

//    @PutMapping(value = "/managed-employee/{id_employee}", consumes = {"multipart/form-data"}, produces = MediaType.APPLICATION_JSON_VALUE)
//    public Response updateEmployee(@RequestPart("jsonfield") final DataEmployee object, @RequestPart("doc_cv") MultipartFile file, @PathVariable("id_employee") Long id_employee, Authentication authenticationRequest) throws IOException {
    @PutMapping(path = "/managed-employee/{id_employee}", produces = {"application/json"})
    public Response updateEmployee(@RequestBody
            final DataEmployee object, @PathVariable("id_employee") Long id_employee, Authentication authenticationRequest) {
        try {
            Boolean process = true;
            Boolean cheking = false;
            if (authenticationRequest.getPrincipal().toString().equals("") || authenticationRequest.getPrincipal() == null) {
                rs.setResponse_code(HttpStatus.FORBIDDEN.toString());
                rs.setInfo("fail");
                rs.setResponse("role : " + authenticationRequest.getPrincipal() + ", cannot access managed-employee, Permission denied");
                process = false;
                CreateLog.createJson(rs, "create-employee");
            }
            Employee updateEmployee = employeeService.findById(id_employee);

            if (updateEmployee == null) {
                rs.setResponse_code("05");
                rs.setInfo("Data null");
                rs.setResponse("Employe not Found");
                CreateLog.createJson(rs, "update-employee");
            }

//
            if (object.getEmail() != null) {
                cheking = Util.validation(object.getEmail());
            }
//            

            if (cheking.equals(false)) {

                rs.setResponse_code("05");
                rs.setInfo("You have entered an invalid email address :" + object.getEmail());
                rs.setResponse("Create Employee Failed");
                CreateLog.createJson(rs, "update-employee");

            } else {

                if (object.getName().length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Name maksimum 20 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "update-employee");
                    process = false;
                }
                if (object.getName().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Name can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "update-employee");
                    process = false;
                }
                if (object.getNpwp().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field NPWP can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "update-employee");
                    process = false;
                }
                if (object.getNpwp().length() > 30) {
                    rs.setResponse_code("05");
                    rs.setInfo("failed");
                    rs.setResponse("Create Employee Failed, NPWP  Field max 20");
                    CreateLog.createJson(rs, "update-employee");
                    process = false;
                }
                if (object.getAddress().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Address can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "update-employee");
                    process = false;
                }
                if (object.getNik().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Nik can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "update-employee");
                    process = false;
                }
                if (object.getEmail().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Email can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "update-employee");
                    process = false;
                }
                if (object.getTax_status().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Taxt Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "update-employee");
                    process = false;
                }
                if (object.getCell_phone().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Create Employee Failed");
                    rs.setResponse("Field Mobile Can't be empty");
                    CreateLog.createJson(rs, "update-employee");
                    process = false;
                }

                if (object.getEmail().length() > 30) {
                    rs.setResponse_code("05");
                    rs.setInfo("Create Employee Failed");
                    rs.setResponse("Field Email Maximum 30 character");
                    CreateLog.createJson(rs, "update-employee");
                    process = false;
                }

                if (object.getCell_phone().length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Create Employee Failed");
                    rs.setResponse("Field Mobile  Maximum 20 character");
                    CreateLog.createJson(rs, "update-employee");
                    process = false;
                }
                if (object.getAddress().length() > 200) {
                    rs.setResponse_code("05");
                    rs.setInfo("Create Employee Failed");
                    rs.setResponse("Field Address Maximum 200 character");
                    CreateLog.createJson(rs, "update-employee");
                    process = false;
                }
//                

                if (process) {
                    List<Account> accoutList = accountService.findByEmployee(id_employee.toString());
                    System.out.println("accoutList" + accoutList.toString());

                    if (updateEmployee.getName() == null) {
                        if (object.getName() != null) {
                            updateEmployee.setName(object.getName());
                        }
                    }
                    if (updateEmployee.getAddress() == null) {
                        if (object.getAddress() != null) {
                            updateEmployee.setAddress(object.getAddress());
                        }
                    }
                    if (updateEmployee.getMobilePhone() == null) {
                        Employee cekPhone = employeeService.findByEmployee(object.getCell_phone());
                        if (cekPhone == null) {
                            updateEmployee.setMobilePhone(object.getCell_phone());
                        }
                    }
                    if (updateEmployee.getNik() == null) {
                        Employee cekNik = employeeService.findByEmployee(object.getNik());
                        if (cekNik == null) {
                            updateEmployee.setNik(object.getNik());
                        }
                    }
                    if (updateEmployee.getNpwp() == null) {
                        Employee cekNpwp = employeeService.findByEmployee(object.getNpwp());
                        if (cekNpwp == null) {
                            updateEmployee.setNpwp(object.getNpwp());
                        }
                    }
                    if (updateEmployee.getEmail() == null) {
                        Employee cekEMail = employeeService.findByEmployee(object.getEmail());
                        if (cekEMail == null) {
                            updateEmployee.setEmail(object.getEmail());
                        }
                    }
                    if (object.getTax_status() != null) {
                        updateEmployee.setTaxStatus(object.getTax_status());
                    }
                    if (object.getUser_name() != null) {
                        Employee cekUname = employeeService.findByEmployee(object.getUser_name());
                        if (cekUname == null) {
                            updateEmployee.setUserName(object.getUser_name());
                        }
                    }

                    if (object.getGender() != null) {
                        String gdr = null;
                        if (object.getGender().equalsIgnoreCase("L")
                                || object.getGender().equalsIgnoreCase("laki")
                                || object.getGender().equalsIgnoreCase("pria")
                                || object.getGender().equalsIgnoreCase("male")
                                || object.getGender().equalsIgnoreCase("m")) {
                            gdr = "m";
                        } else {
                            gdr = "f";

                        }
                        updateEmployee.setGender(gdr);
                    }
                    for (int k = 0; k < accoutList.size(); k++) {
                        if (accoutList.get(k).getTypeAccount().equalsIgnoreCase("payroll")) {
                            Account upd_acc_payroll = accountService.findById(accoutList.get(k).getAccountId());
                            System.out.println("upd_acc_payroll" + upd_acc_payroll.toString());
                            if (object.getAccount_name_p() != null) {
                                if (upd_acc_payroll.getAccountName() == null ? object.getAccount_name_p() != null : !upd_acc_payroll.getAccountName().equals(object.getAccount_name_p())) {
                                    upd_acc_payroll.setAccountName(object.getAccount_name_p());
                                }
                            }
                            if (object.getBank_name_p() != null) {
                                if (upd_acc_payroll.getBankName() == null ? object.getBank_name_p() != null : !upd_acc_payroll.getBankName().equals(object.getBank_name_p())) {
                                    upd_acc_payroll.setBankName(object.getBank_name_p());
                                }
                            }
                            if (object.getAccount_number_p() != null) {
                                if (upd_acc_payroll.getAccountNumber() == null ? object.getAccount_number_p() != null : !upd_acc_payroll.getAccountNumber().equals(object.getAccount_number_p())) {
                                    upd_acc_payroll.setAccountNumber(object.getAccount_number_p());
                                    upd_acc_payroll.setIsActive(false);
                                }
                            }
                            upd_acc_payroll.setTypeAccount("payroll");
                            upd_acc_payroll = this.accountService.update(upd_acc_payroll);
//                            updateEmployee.addAccount(upd_acc_payroll);
                        }
//                        
//                    accl.setEmployee(newEmployee);

                        if (accoutList.get(k).getTypeAccount().equalsIgnoreCase("loan")) {

                            Account upd_acc_loan = accountService.findById(accoutList.get(k).getAccountId());
                            System.out.println("upd_acc_loan" + upd_acc_loan.toString());

                            if (object.getAccount_name_l() != null) {
                                if (upd_acc_loan.getAccountName() == null ? object.getAccount_name_l() != null : !upd_acc_loan.getAccountName().equals(object.getAccount_name_l())) {
                                    upd_acc_loan.setAccountName(object.getAccount_name_l());
                                }
                            }
                            if (object.getBank_name_l() != null) {
                                if (upd_acc_loan.getBankName() == null ? object.getBank_name_l() != null : !upd_acc_loan.getBankName().equals(object.getBank_name_l())) {
                                    upd_acc_loan.setBankName(object.getBank_name_l());
                                }
                            }
                            if (object.getAccount_number_l() != null) {
                                if (upd_acc_loan.getAccountNumber() == null ? object.getAccount_number_l() != null : !upd_acc_loan.getAccountNumber().equals(object.getAccount_number_l())) {
                                    upd_acc_loan.setAccountNumber(object.getAccount_number_l());
                                    upd_acc_loan.setIsActive(false);
                                }
                            }
                            upd_acc_loan.setTypeAccount("loan");
                            upd_acc_loan = accountService.update(upd_acc_loan);
//                          updateEmployee.addAccount(upd_acc_loan);
                        }
                    }
                    updateEmployee = employeeService.update(updateEmployee);

                    log.info("isi" + updateEmployee.getIdEmployee().toString());

                    AprovedApi obj = new AprovedApi();
                    obj.setId_employee_admin(updateEmployee.getParentId().getIdEmployee());
                    obj.setId_employee(updateEmployee.getIdEmployee());
                    return approvedByAdmin(obj);
                }
            }
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "update-employee");
        }
//        rs.setResponse_code("05");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");
        return rs;
    }

    @PutMapping(path = "/managed-employee/set-password/{id_employee}", produces = {"application/json"})
    public Response changePassword(@RequestBody
            final DataEmployee object, @PathVariable("id_employee") Long id_employee, Authentication authenticationRequest) {
        try {
            Boolean process = true;
            Employee entity = employeeService.findById(id_employee);
            if (entity == null) {
                rs.setResponse_code("05");
                rs.setInfo("Error");
                rs.setResponse("Employee not found");
                CreateLog.createJson(rs, "set-password");
                process = false;
            }
            if (process) {
                entity.setPassword(encoder.encode(object.getUser_pass()));
//                entity.setPassword(object.getUser_pass());
                Employee setPassword = this.employeeService.update(entity);
                if (setPassword != null) {
                    rs.setResponse_code("01");
                    rs.setInfo("Success");
                    rs.setResponse("set password employee Id " + entity.getEmployeeId() + " success");
                    CreateLog.createJson(rs, "set-password");
                    return rs;
                }
            }
            rs.setResponse_code("05");
            rs.setInfo("Error");
            rs.setResponse("Employee not found");
            CreateLog.createJson(rs, "set-password");
            return rs;
        } catch (Exception ex) {
//            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("05");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "set-password");
            return rs;
        }

    }

    @RequestMapping(value = "/cv/managed-cv/{id_employee}", produces = {"application/json"}, method = RequestMethod.PUT)
    public Response saveUploadedFile(@RequestPart("doc_cv") MultipartFile file,
            @PathVariable("id_employee") Long id_employee, Authentication authenticationRequest) throws IOException {
        try {
//            String link_cv = null;s
//            String nama = object.getName().replaceAll("\\s", "").toLowerCase();
//            System.out.println("nama" + nama);
//            System.out.println("isi_file : " + file);
            String pathDoc = null;
            Boolean process = true;
            Employee entity = employeeService.findById(id_employee);
            if (entity == null) {
                rs.setResponse_code("05");
                rs.setInfo("Error");
                rs.setResponse("Employee not found");
                CreateLog.createJson(rs, "upload-file");
                process = false;
            }
            pathDoc = basepathUpload + "employee" + "/" + entity.getIdEmployee() + "/";
            /*-----------------------------------------------*/
            if (process) {
                if (!file.isEmpty()) {

                    File newFolder = new File(pathDoc);
                    boolean created = newFolder.mkdir();
                    if (!newFolder.getParentFile().exists()) {
                        newFolder.getParentFile().mkdirs();
                    } else {
                        newFolder.getParentFile().mkdirs();
                    }
                    if (!newFolder.exists()) {
                        if (created) {
                            System.out.println("Folder was created !");
                        } else {
                            System.out.println("Folder exists");
                        }
                    }

                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(pathDoc + file.getOriginalFilename());
                    Files.write(path, bytes);
                    entity.setLinkCv(pathDoc + file.getOriginalFilename());
                    Employee newEmp = employeeService.update(entity);
                    if (newEmp != null) {
                        AprovedApi obj = new AprovedApi();

                        obj.setId_employee_admin(newEmp.getParentId().getIdEmployee());
                        obj.setId_employee(newEmp.getIdEmployee());
//                        return approvedByAdmin(obj);
                    } else {
                        rs.setResponse_code("05");
                        rs.setInfo("Error");
                        rs.setResponse("Upload: Failed dataemployee :=>" + newEmp);
                        CreateLog.createJson(rs, "upload-file");
                        return rs;

                    }

                } else {
//                rs.setResponse_code("05");
//                rs.setInfo("Data null");
//                rs.setResponse("Create Employee Failed");
//                CreateLog.createJson(rs, "upload-file");
                    entity.setLinkCv("-");
                    Employee newEmp = employeeService.update(entity);
                    AprovedApi obj = new AprovedApi();

                    obj.setId_employee_admin(newEmp.getParentId().getIdEmployee());
                    obj.setId_employee(newEmp.getIdEmployee());

                }
                rs.setResponse_code("00");
                rs.setInfo("Succes");
                rs.setResponse("Upload: Succes");
                CreateLog.createJson(rs, "upload-file");
                return rs;
            }
            rs.setResponse_code("05");
            rs.setInfo("Error");
            rs.setResponse("Upload: Failed");
            CreateLog.createJson(rs, "upload-file");

//            return demployee;
        } catch (IOException ex) {
//            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("05");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "upload-file");
            return rs;
        }
//            return rs;
        return rs;

    }

    @PermitAll
    @PutMapping(value = "/account/managed-account/{id_employee}", produces = {"application/json"})
//     @PreAuthorize("hasRole('lawfirm') or hasRole('admin')or hasRole('dpm')or hasRole('admin')")
    public Response updateAccount(@RequestBody
            final DataEmployee object,
            @PathVariable("id_employee") Long id_employee
    ) {
        try {
            Boolean process = true;
            Employee updateEmployee = employeeService.findById(id_employee);
            if (updateEmployee == null) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Data Employee not found");
                process = false;
            }

            if (process) {
                List<Account> accoutList = accountService.findByEmployee(id_employee.toString());
                for (int k = 0; k < accoutList.size(); k++) {

                    if (accoutList.get(k).getTypeAccount().equalsIgnoreCase("payroll")) {
                        Account upd_acc_payroll = accountService.findById(accoutList.get(k).getAccountId());
                        System.out.println("upd_acc_payroll" + upd_acc_payroll.toString());
                        if (object.getAccount_name_p() != null) {
                            upd_acc_payroll.setAccountName(object.getAccount_name_p());
                        }
                        if (object.getBank_name_p() != null) {
                            upd_acc_payroll.setBankName(object.getBank_name_p());
                        }
                        if (object.getAccount_number_p() != null) {
                            upd_acc_payroll.setAccountNumber(object.getAccount_number_p());
                            upd_acc_payroll.setIsActive(false);
                        }
                        upd_acc_payroll.setTypeAccount("payroll");
                        upd_acc_payroll = accountService.update(upd_acc_payroll);
//                    updateEmployee.addAccount(upd_acc_payroll);
//                        log.info("upd_acc_payroll" + upd_acc_payroll.toString());
                    }
//                        
//                    accl.setEmployee(newEmployee);

                    if (accoutList.get(k).getTypeAccount().equalsIgnoreCase("loan")) {

                        Account upd_acc_loan = accountService.findById(accoutList.get(k).getAccountId());
                        System.out.println("upd_acc_loan" + upd_acc_loan.toString());
                        if (object.getAccount_name_l() != null) {
                            upd_acc_loan.setAccountName(object.getAccount_name_l());
                        }
                        if (object.getBank_name_l() != null) {
                            upd_acc_loan.setBankName(object.getBank_name_l());
                        }
                        if (object.getAccount_number_l() != null) {
                            upd_acc_loan.setAccountNumber(object.getAccount_number_l());
                            upd_acc_loan.setIsActive(false);
                        }
                        upd_acc_loan.setTypeAccount("loan");
                        upd_acc_loan = accountService.update(upd_acc_loan);
//                    updateEmployee.addAccount(upd_acc_loan);
//                        log.info("upd_acc_loan" + upd_acc_loan.toString());
                    }
                }
                updateEmployee = employeeService.update(updateEmployee);
                rs.setResponse_code("01");
                rs.setInfo("Success");
                rs.setResponse("update account employee");
                CreateLog.createJson(rs, "update-account");
            } else {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Data Account can't be update");
                CreateLog.createJson(rs, "update-account");
            }
            return rs;
        } catch (Exception ex) {
//            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("05");
            rs.setInfo("failes");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "upload-account");
            return rs;
        }
//            return rs;
//        return rs;

    }

    @RequestMapping(value = "/acount/find-by-employee", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<String> findAccountByEmployee(@RequestBody
            final AccountApi object,
            @RequestParam("param") String param
    ) {
        try {
            List<Account> listAct = this.accountService.findByEmployee(param);

            if (listAct != null) {
                JSONObject obj = new JSONObject();
                JSONArray array = new JSONArray();
                for (int i = 0; i < listAct.size(); i++) {

                    Account entity = listAct.get(i);
                    if (entity.getAccountId() == null) {
                        obj.put("account_id", "");
                    } else {
                        obj.put("account_id", entity.getAccountId());
                    }

                    if (entity.getEmployee() == null) {
                        obj.put("nama", "");
                    } else {
                        obj.put("nama", entity.getEmployee().getName());
                        obj.put("nik", entity.getEmployee().getNik());
                        obj.put("npwp", entity.getEmployee().getNpwp());
                    }
                    if (entity.getTypeAccount().equalsIgnoreCase("payroll")) {
                        if (entity.getBankName() == null) {
                            obj.put("bank_name_p", "");
                        } else {
                            obj.put("bank_name_p", entity.getBankName());
                        }
                        if (entity.getAccountName() == null) {
                            obj.put("account_name_p", "");
                        } else {
                            obj.put("account_name_p", entity.getAccountName());
                        }
                        if (entity.getIsActive() == true) {
                            obj.put("is_active", true);
                            obj.put("account_number_p", entity.getAccountNumberFinance());
                        }
                        if (entity.getIsActive() == false) {
                            obj.put("is_active", false);
                            if (entity.getAccountNumberFinance() != null) {
                                obj.put("account_number_p", entity.getAccountNumberFinance());
                            } else {
                                obj.put("account_number_p", entity.getAccountNumber());
                            }
                            obj.put("account_number_p", entity.getAccountNumber());
                        }
                        if (entity.getIsDelete() == true) {
                            obj.put("is_active_p", false);
                            obj.put("is_delete_p", true);
                        }
                        if (entity.getIsDelete() == false) {
                            obj.put("is_active_p", true);
                            obj.put("is_delete_p", false);
                        }
                    }
                    if (entity.getTypeAccount().equalsIgnoreCase("loan")) {
                        if (entity.getBankName() == null) {
                            obj.put("bank_name_l", "");
                        } else {
                            obj.put("bank_name_l", entity.getBankName());
                        }
                        if (entity.getAccountName() == null) {
                            obj.put("account_name_l", "");
                        } else {
                            obj.put("account_name_l", entity.getAccountName());
                        }
                        if (entity.getIsActive() == true) {
                            obj.put("is_active", true);
                            obj.put("account_number_l", entity.getAccountNumberFinance());
                        }
                        if (entity.getIsActive() == false) {
                            obj.put("is_active", false);
                            if (entity.getAccountNumberFinance() != null) {
                                obj.put("account_number_l", entity.getAccountNumberFinance());
                            } else {
                                obj.put("account_number_l", entity.getAccountNumber());
                            }

                        }
                        if (entity.getIsDelete() == true) {
                            obj.put("is_active_l", false);
                            obj.put("is_delete_l", true);
                        }
                        if (entity.getIsDelete() == false) {
                            obj.put("is_active_l", true);
                            obj.put("is_delete_l", false);
                        }
                    }
                    array.put(obj);
                }
                return ResponseEntity.ok(array.toString());
            }
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "acount-find-by-employee");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(new CustomErrorType("05", "Error", "Data Not Found"),
                HttpStatus.NOT_FOUND);

    }
//    @PreAuthorize("hasRole('sysadmin') or hasRole('admin')")
//     @RequestMapping(value = {"/approvedByAdmin"})

    @RequestMapping(value = "/approved/by-admin", method = RequestMethod.POST)
    public Response approvedByAdmin(@RequestBody
            final AprovedApi object
    ) {
        try {
            Employee dataAdmin = employeeService.findById(object.getId_employee_admin());
            Employee dataEmployee = employeeService.findById(object.getId_employee());

            if (dataEmployee == null) {
                rs.setResponse_code("05");
                rs.setInfo("Warning");
                rs.setResponse("Employee not Found");
                CreateLog.createJson(rs, "approved-by-admin");
                return rs;
            }

//            }
            Employee cekEmployeeId = employeeService.findByEmployee(object.getEmployee_id());

            if (cekEmployeeId != null) {
                rs.setResponse_code("05");
                rs.setInfo("Warning");
                rs.setResponse("Employee Id : " + object.getEmployee_id() + " Already Registered at " + cekEmployeeId.getNik() + "-" + cekEmployeeId.getName());
                CreateLog.createJson(rs, "approved-by-admin");
                return rs;
            }
            dataEmployee.setSalary(object.getSalary());
//            dataEmployee.setLoanAmount(object.getLoan_amount());

            dataEmployee.setApproved_date(new Date());
            dataEmployee.setStatus("a");
//            dataEmployee.setIsActive(Boolean.TRUE);
            Employee approovedEmployee = employeeService.approved(dataEmployee);

            if (approovedEmployee != null) {
                List<Account> listAccount = accountService.findByEmployee(approovedEmployee.getIdEmployee().toString());
//            Account upd_account = new Account();
                for (int i = 0; i < listAccount.size(); i++) {
                    Account upd_account = accountService.findById(listAccount.get(i).getAccountId());
                    if (listAccount.get(i).getTypeAccount().contentEquals("loan")) {
                        if (!listAccount.get(i).getAccountNumber().isEmpty()) {
                            upd_account.setAccountNumberFinance(listAccount.get(i).getAccountNumber());
//                        upd_account.setAccountNumber(null);
                        } else {
                            upd_account.setAccountNumberFinance(listAccount.get(i).getAccountNumber());
//                        upd_account.setAccountNumber(null);
                        }
                        upd_account.setAccountNumber("");
                        upd_account.setIsActive(true);
                    }
                    if (listAccount.get(i).getTypeAccount().contentEquals("payroll")) {
                        if (!listAccount.get(i).getAccountNumber().isEmpty()) {
                            upd_account.setAccountNumberFinance(listAccount.get(i).getAccountNumber());
//                        upd_account.setAccountNumber("");
                        } else {
                            upd_account.setAccountNumberFinance(listAccount.get(i).getAccountNumber());
//                        upd_account.setAccountNumber("");
                        }
                        upd_account.setAccountNumber("");
                        upd_account.setIsActive(true);
                    }
                    upd_account = accountService.update(upd_account);

                    if (upd_account != null) {
                        rs.setResponse_code("01");
                        rs.setInfo("Succes");
                        rs.setResponse("Employee approved By : " + dataAdmin.getName());//dataAdmin.getName());
                        CreateLog.createJson(rs, "approved-by-admin");
//                        return rs;
                    } else {
                        rs.setResponse_code("05");
                        rs.setInfo("Warning");
                        rs.setResponse("Employee Null");
                        CreateLog.createJson(rs, "approved-by-admin");
//                        return rs;
                    }
                }
            }

            return rs;
        } catch (Exception ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "approved-by-admin");
        }
        return null;

    }

//    @PermitAll
    @PutMapping(value = "/approved/{id_employee}", produces = {"application/json"})
    public Response approved(@RequestBody
            final AprovedApi object,
            @PathVariable("id_employee") Long id_employee
    ) {
        this.now = new Date();
        this.date_now = timeFormat.format(now);
        Employee dataAdmin = employeeService.findById(object.getId_employee_admin());
//        if (!dataAdmin.getRoleName().equalsIgnoreCase("admin")) {
//            rs.setResponse_code("05");
//            rs.setInfo("Warning");
//            rs.setResponse("Your Role can't acces this feature");
//        }
        Employee dataEmployee = employeeService.findById(id_employee);
        if (dataEmployee == null) {
            rs.setResponse_code("05");
            rs.setInfo("Warning");
            rs.setResponse("Employee not Found");
            CreateLog.createJson(rs, "approved");
            return rs;
        }

//        if (dataEmployee.IsActive() == true) {
//            rs.setResponse_code("05");
//            rs.setInfo("Warning");
//            rs.setResponse("Employee status already approved");
//            CreateLog.createJson(rs, "approved");
//        }
        if (dataAdmin.getEmployeeId() == null) {

            Employee cekEmployeeId = employeeService.findByEmployee(object.getEmployee_id());

            if (cekEmployeeId != null) {
                rs.setResponse_code("05");
                rs.setInfo("Warning");
                rs.setResponse("Employee Id : " + object.getEmployee_id() + " Already Registered at " + cekEmployeeId.getNik() + "-" + cekEmployeeId.getName());
                CreateLog.createJson(rs, "approved");
                return rs;
            }
            dataEmployee.setEmployeeId(object.getEmployee_id());
        }

//        dataEmployee.setSalary(object.getSalary());
//        dataEmployee.setLoanAmount(object.getLoan_amount());
//
//        dataEmployee.setRoleName(dataAdmin.getRoleName());
        dataEmployee.setApproved_date(now);
        dataEmployee.setIsActive(Boolean.TRUE);
        dataEmployee.setStatus("a");

        Employee approovedEmployee = employeeService.approved(dataEmployee);

        if (approovedEmployee != null) {
            List<Account> listAccount = accountService.findByEmployee(id_employee.toString());
//            Account upd_account = new Account();
            for (int i = 0; i < listAccount.size(); i++) {
                Account upd_account = accountService.findById(listAccount.get(i).getAccountId());
                if (listAccount.get(i).getTypeAccount().contentEquals("loan")) {
                    if (!listAccount.get(i).getAccountNumber().isEmpty()) {
                        upd_account.setAccountNumberFinance(listAccount.get(i).getAccountNumber());
                    }
                    upd_account.setAccountNumber("");
                    upd_account.setIsActive(true);
                }
                if (listAccount.get(i).getTypeAccount().contentEquals("payroll")) {
                    if (!listAccount.get(i).getAccountNumber().isEmpty()) {
                        upd_account.setAccountNumberFinance(listAccount.get(i).getAccountNumber());
                    }
                    upd_account.setAccountNumber("");
                    upd_account.setIsActive(true);
                }
                upd_account = accountService.update(upd_account);

                if (upd_account != null) {
                    rs.setResponse_code("01");
                    rs.setInfo("Succes");
                    rs.setResponse("Employee approved By : " + dataAdmin.getName());
                    CreateLog.createJson(rs, "approved");
                } else {
                    rs.setResponse_code("05");
                    rs.setInfo("Warning");
                    rs.setResponse("Employee Null");
                    CreateLog.createJson(rs, "approved");
                }
            }
        }
// 
        return rs;
    }

    @PermitAll
    @DeleteMapping(value = "/managed-employee/{id_employee}", produces = {"application/json"})//@PutMapping
//    @PreAuthorize("hasRole('admin')")
    public Response deleteEmployee(@PathVariable("id_employee") Long idEmployee
    ) {
        Employee entity = employeeService.findById(idEmployee);

        if (entity != null) {
            entity = this.employeeService.delete(entity);
            List<Account> listAccount = accountService.findByEmployee(entity.getIdEmployee().toString());
            for (int i = 0; i < listAccount.size(); i++) {
                Account upd_account = accountService.findById(listAccount.get(i).getAccountId());
                upd_account = this.accountService.delete(upd_account);
            }

            rs.setResponse_code("01");
            rs.setInfo("Succes");
            rs.setResponse("Employee Deleted");
            CreateLog.createJson(rs, "delete-employee");
        } else {
            rs.setResponse_code("05");
            rs.setInfo("failed");
            rs.setResponse("Employee Null");
            CreateLog.createJson(rs, "delete-employee");
        }
        return rs;
    }

    @PermitAll
    @RequestMapping(value = "/view/list-by-admin", method = RequestMethod.GET, produces = {"application/json"})
//    @PreAuthorize("hasRole('sysadmin') or hasRole('admin')")
    public ResponseEntity<String> viewEmployeeByAdmin(ServletRequest request
    ) {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();

            int max = employeeService.count();
            int start = 0;
            String paramString = null;
            if (paramMap.containsKey("start")) {
                start = Integer.parseInt(request.getParameter("start"));
            }
            if (paramMap.containsKey("paramString")) {
                paramString = request.getParameter("paramString");
            }
            List<Employee> entityList = null;
            if (start == 0) {
                entityList = this.employeeService.listEmployee();
            } else {
                entityList = this.employeeService.listEmployeePaging(paramString, max, start);
            }

            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                JSONObject jsonobj = new JSONObject();
                JSONObject jsonacc = new JSONObject();
                Employee entity = (Employee) entityList.get(i);
                if (entity.getIdEmployee() == null) {
                    jsonobj.put("id_employee", "");
                } else {
                    jsonobj.put("id_employee", entity.getIdEmployee());
                }
                if (entity.getName() == null) {
                    jsonobj.put("name", "");
                } else {
                    jsonobj.put("name", entity.getName());
                }
                if (entity.getUserName() == null) {
                    jsonobj.put("user_name", "");
                } else {
                    jsonobj.put("user_name", entity.getUserName());
                }
                if (entity.getEmployeeId() == null) {
                    jsonobj.put("employee_id", "");
                } else {
                    jsonobj.put("employee_id", entity.getEmployeeId());
                }
                if (entity.getAddress() == null) {
                    jsonobj.put("address", "");
                } else {
                    jsonobj.put("address", entity.getAddress());
                }
                if (entity.getNik() == null) {
                    jsonobj.put("nik", "");
                } else {
                    jsonobj.put("nik", entity.getNik());
                }
                if (entity.getMobilePhone() == null) {
                    jsonobj.put("cellphone", "");
                } else {
                    jsonobj.put("cellphone", entity.getMobilePhone());
                }
                if (entity.getNpwp() == null) {
                    jsonobj.put("npwp", "");
                } else {
                    jsonobj.put("npwp", entity.getNpwp());
                }
                if (entity.getRoleName() == null) {
                    jsonobj.put("role", "");
                } else {
                    jsonobj.put("role", entity.getRoleName());
                }
                if (entity.getLoanAmount() == null) {
                    jsonobj.put("loan_limit", 0);
                } else {
                    jsonobj.put("loan_limit", entity.getLoanAmount());
                }
                if (entity.getTaxStatus() == null) {
                    jsonobj.put("tax_status", "");
                } else {
                    jsonobj.put("tax_status", entity.getTaxStatus());
                }
                if (entity.IsActive() == true) {
                    jsonobj.put("is_active", true);
                } else {
                    jsonobj.put("is_active", false);
                }
                if (entity.getIsDelete() == false) {
                    jsonobj.put("is_delete", false);
                } else {
                    jsonobj.put("is_delete", true);
                }
                if (entity.getStatus() == null) {
                    jsonobj.put("status_employee", "");
                } else {
                    jsonobj.put("status_employee", entity.getStatus());
                }
                if (entity.getLinkCv() == null) {
                    jsonobj.put("doc_cv", "");
                } else {
                    jsonobj.put("doc_cv", entity.getLinkCv());
//                    jsonobj.put("status_employee", "d");
                }
                if (entity.getGender().equalsIgnoreCase("m") || entity.getGender().equalsIgnoreCase("male")) {
                    jsonobj.put("gender", "m");
                } else {
                    jsonobj.put("gender", "f");
                }
                if (entity.getParentId() == null) {
                    jsonobj.put("approved by", "");
                } else {
                    jsonobj.put("approved by", entity.getParentId().getName());
                }
                if (entity.getIdEmployee() != null) {
                    List<Account> listAccount = accountService.findByEmployee(entity.getIdEmployee().toString());
                    for (int k = 0; k < listAccount.size(); k++) {
                        Account enAccount = (Account) listAccount.get(k);
                        if (enAccount.getTypeAccount().equalsIgnoreCase("payroll")) {
                            if (enAccount.getBankName() == null) {
                                jsonobj.put("bank_name_p", "");
                            } else {
                                jsonobj.put("bank_name_p", enAccount.getBankName());
                            }
                            if (enAccount.getAccountName() == null) {
                                jsonobj.put("account_name_p", "");
                            } else {
                                jsonobj.put("account_name_p", enAccount.getAccountName());
                            }
                            if (enAccount.getIsActive() == true) {
                                jsonobj.put("is_active_p", true);
                            } else {
                                jsonobj.put("is_active_p", false);
                            }
                            if (enAccount.getAccountNumber() == null) {
                                jsonobj.put("account_number_p", "");
                            } else {
                                if (enAccount.getIsActive() == true) {
                                    jsonobj.put("account_number_p", "");
                                } else {
                                    jsonobj.put("account_number_p", enAccount.getAccountNumber());
                                }
                            }
                            if (enAccount.getAccountNumberFinance() == null) {
                                jsonobj.put("account_number_finance_p", "");
                            } else {
                                jsonobj.put("account_number_finance_p", enAccount.getAccountNumberFinance());
                            }
                            if (enAccount.getIsDelete() == true) {
                                jsonobj.put("is_active_p", false);
                                jsonobj.put("is_delete_p", true);
                            }
                            if (enAccount.getIsDelete() == false) {
                                jsonobj.put("is_active_p", true);
                                jsonobj.put("is_delete_p", false);
                            }
                        }
                        if (enAccount.getTypeAccount().equalsIgnoreCase("loan")) {
                            if (enAccount.getBankName() == null) {
                                jsonobj.put("bank_name_l", "");
                            } else {
                                jsonobj.put("bank_name_l", enAccount.getBankName());
                            }
                            if (enAccount.getAccountName() == null) {
                                jsonobj.put("account_name_l", "");
                            } else {
                                jsonobj.put("account_name_l", enAccount.getAccountName());
                            }
                            if (enAccount.getIsActive() == true) {
                                jsonobj.put("is_active_l", true);
                            } else {
                                jsonobj.put("is_active_l", false);
                            }

                            if (enAccount.getAccountNumber() == null) {
                                jsonobj.put("account_number_l", "");
                            } else {
                                if (enAccount.getIsActive() == true) {
                                    jsonobj.put("account_number_l", "");
                                } else {
                                    jsonobj.put("account_number_l", enAccount.getAccountNumber());
                                }
                            }
                            if (enAccount.getAccountNumberFinance() == null) {
                                jsonobj.put("account_number_finance_l", "");
                            } else {
                                jsonobj.put("account_number_finance_l", enAccount.getAccountNumberFinance());
                            }
                            if (enAccount.getIsDelete() == true) {
                                jsonobj.put("is_active_l", false);
                                jsonobj.put("is_delete_l", true);
                            }
                            if (enAccount.getIsDelete() == false) {
                                jsonobj.put("is_active_l", true);
                                jsonobj.put("is_delete_l", false);
                            }
                        }
                    }
                }
                array.put(jsonobj);
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            CreateLog.createJson(ex.getMessage(), "view-list-by-admin");
            System.out.println("ERROR: " + ex.getMessage());;
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @PermitAll
    @RequestMapping(value = "/view/list-by-finance", method = RequestMethod.GET, produces = {"application/json"})
//    @PreAuthorize("hasRole('sysadmin') or hasRole('admin')  or hasRole('finance')")
    public ResponseEntity<String> viewEmployeeByFinance(ServletRequest request
    ) {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();

            int max = employeeService.count();
            int start = 0;
            String paramString = null;
            if (paramMap.containsKey("start")) {
                start = Integer.parseInt(request.getParameter("start"));
            }
            if (paramMap.containsKey("paramString")) {
                paramString = request.getParameter("paramString");
            }
            List<Employee> entityList = null;
            if (start == 0) {
                entityList = this.employeeService.listEmployee();
            } else {
                entityList = this.employeeService.listEmployeePaging(paramString, max, start);
            }

            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                JSONObject jsonobj = new JSONObject();
                JSONObject jsonacc = new JSONObject();
                Employee entity = (Employee) entityList.get(i);
                if (entity.getIdEmployee() == null) {
                    jsonobj.put("id_employee", "");
                } else {
                    jsonobj.put("id_employee", entity.getIdEmployee());
                }
                if (entity.getName() == null) {
                    jsonobj.put("name", "");
                } else {
                    jsonobj.put("name", entity.getName());
                }
                if (entity.getUserName() == null) {
                    jsonobj.put("user_name", "");
                } else {
                    jsonobj.put("user_name", entity.getUserName());
                }
                if (entity.getEmployeeId() == null) {
                    jsonobj.put("employee_id", "");
                } else {
                    jsonobj.put("employee_id", entity.getEmployeeId());
                }
                if (entity.getAddress() == null) {
                    jsonobj.put("address", "");
                } else {
                    jsonobj.put("address", entity.getAddress());
                }
                if (entity.getNik() == null) {
                    jsonobj.put("nik", "");
                } else {
                    jsonobj.put("nik", entity.getNik());
                }
                if (entity.getMobilePhone() == null) {
                    jsonobj.put("cell_phone", "");
                } else {
                    jsonobj.put("cell_phone", entity.getMobilePhone());
                }
                if (entity.getNpwp() == null) {
                    jsonobj.put("npwp", "");
                } else {
                    jsonobj.put("npwp", entity.getNpwp());
                }
                if (entity.getRoleName() == null) {
                    jsonobj.put("role", "");
                } else {
                    jsonobj.put("role", entity.getRoleName());
                }
                if (entity.getLoanAmount() == null) {
                    jsonobj.put("loan_limit", 0);
                } else {
                    jsonobj.put("loan_limit", entity.getLoanAmount());
                }
                if (entity.getTaxStatus() == null) {
                    jsonobj.put("tax_status", "");
                } else {
                    jsonobj.put("tax_status", entity.getTaxStatus());
                }
                if (entity.getStatus() == null) {
                    jsonobj.put("status_employee", "");
                } else {
                    jsonobj.put("status_employee", entity.getStatus());
                }

                if (entity.IsActive() == true) {
                    jsonobj.put("is_active", true);
//                    jsonobj.put("status_employee", "a");
                } else {
                    jsonobj.put("is_active", false);
//                    jsonobj.put("status_employee", "p");
                }
                if (entity.getIsDelete() == false) {
                    jsonobj.put("is_delete", false);
                } else {
                    jsonobj.put("is_delete", true);
//                    jsonobj.put("status_employee", "d");
                }
                if (entity.getLinkCv() == null) {
                    jsonobj.put("doc_cv", "");
                } else {
                    jsonobj.put("doc_cv", entity.getLinkCv());
//                    jsonobj.put("status_employee", "d");
                }
                if (entity.getGender().equalsIgnoreCase("m") || entity.getGender().equalsIgnoreCase("male")) {
                    jsonobj.put("gender", "m");
                } else {
                    jsonobj.put("gender", "f");
                }
                if (entity.getParentId() == null) {
                    jsonobj.put("aprroved_by", "");
                } else {
                    jsonobj.put("aprroved_by", entity.getParentId().getName());
                }
                if (entity.getIdEmployee() != null) {
                    List<Account> listAccount = accountService.findByEmployee(entity.getIdEmployee().toString());
                    for (int k = 0; k < listAccount.size(); k++) {
                        Account enAccount = (Account) listAccount.get(k);
                        if (enAccount.getTypeAccount().equalsIgnoreCase("payroll")) {
                            if (enAccount.getBankName() == null) {
                                jsonobj.put("bank_name_p", "");
                            } else {
                                jsonobj.put("bank_name_p", enAccount.getBankName());
                            }
                            if (enAccount.getAccountName() == null) {
                                jsonobj.put("account_name_p", "");
                            } else {
                                jsonobj.put("account_name_p", enAccount.getAccountName());
                            }
                            if (enAccount.getIsActive() == true) {
                                jsonobj.put("is_active", true);
                                jsonobj.put("account_number_p", enAccount.getAccountNumberFinance());
                            }
                            if (enAccount.getIsActive() == false) {
                                jsonobj.put("is_active", false);
                                if (enAccount.getAccountNumberFinance() != null) {
                                    jsonobj.put("account_number_p", enAccount.getAccountNumberFinance());
                                } else {
                                    jsonobj.put("account_number_p", enAccount.getAccountNumber());
                                }
                                jsonobj.put("account_number_p", enAccount.getAccountNumber());
                            }
                            if (enAccount.getIsDelete() == true) {
                                jsonobj.put("is_active_p", false);
                                jsonobj.put("is_delete_p", true);
                            }
                            if (enAccount.getIsDelete() == false) {
                                jsonobj.put("is_active_p", true);
                                jsonobj.put("is_delete_p", false);
                            }
                        }
                        if (enAccount.getTypeAccount().equalsIgnoreCase("loan")) {
                            if (enAccount.getBankName() == null) {
                                jsonobj.put("bank_name_l", "");
                            } else {
                                jsonobj.put("bank_name_l", enAccount.getBankName());
                            }
                            if (enAccount.getAccountName() == null) {
                                jsonobj.put("account_name_l", "");
                            } else {
                                jsonobj.put("account_name_l", enAccount.getAccountName());
                            }
                            if (enAccount.getIsActive() == true) {
                                jsonobj.put("is_active", true);
                                jsonobj.put("account_number_l", enAccount.getAccountNumberFinance());
                            }
                            if (enAccount.getIsActive() == false) {
                                jsonobj.put("is_active", false);
                                if (enAccount.getAccountNumberFinance() != null) {
                                    jsonobj.put("account_number_l", enAccount.getAccountNumberFinance());
                                } else {
                                    jsonobj.put("account_number_l", enAccount.getAccountNumber());
                                }

                            }
                            if (enAccount.getIsDelete() == true) {
                                jsonobj.put("is_active_l", false);
                                jsonobj.put("is_delete_l", true);
                            }
                            if (enAccount.getIsDelete() == false) {
                                jsonobj.put("is_active_l", true);
                                jsonobj.put("is_delete_l", false);
                            }
                        }
                    }
                }
                array.put(jsonobj);
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            CreateLog.createJson(ex.getMessage(), "view-list-by-finance");
            System.out.println("ERROR: " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @PermitAll
    @GetMapping(value = "/find-employee", produces = {"application/json"})
    public ResponseEntity<String> findByEmployee(@RequestParam("employee_id") String employee_id,
            @RequestParam("name") String name, Pageable pageable
    ) {
        try {
            JSONObject obj = new JSONObject();
            if (employee_id != null) {
                Employee entity = this.employeeService.findByEmployee(employee_id);
                if (entity.getEmployeeId() == null) {
                    obj.put("employee_id", "");
                } else {
                    obj.put("employee_id", entity.getEmployeeId());
                }
                if (entity.getName() == null) {
                    obj.put("name", "");
                } else {
                    obj.put("name", entity.getName());
                }

                return ResponseEntity.ok(obj.toString());
            }
            if (name != null) {
                Employee entity = this.employeeService.findByEmployee(name);
                if (entity.getEmployeeId() == null) {
                    obj.put("employee_id", "");
                } else {
                    obj.put("employee_id", entity.getEmployeeId());
                }
                if (entity.getName() == null) {
                    obj.put("name", "");
                } else {
                    obj.put("name", entity.getName());
                }
                return ResponseEntity.ok(obj.toString());
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "find-employee");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(ex.getMessage(),
//                HttpStatus.NOT_FOUND);
    }

    @PermitAll
    @GetMapping(value = "/employe-dash-board", produces = {"application/json"})
    public ResponseEntity<String> employeDashboard() {
        return new ResponseEntity(new CustomErrorType("05", "Error", ""),
                HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/find-by-id", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<String> findById(@RequestBody
            final DataEmployee object
    ) {
        try {
            Employee entity = this.employeeService.findById(object.getId_employee());
            JSONObject obj = new JSONObject();

            if (entity != null) {

                if (entity.getIdEmployee() == null) {
                    obj.put("id_employee", "");
                } else {
                    obj.put("id_employee", entity.getIdEmployee());
                }
                if (entity.getName() == null) {
                    obj.put("name", "");
                } else {
                    obj.put("name", entity.getName());
                }
                if (entity.getUserName() == null) {
                    obj.put("user_name", "");
                } else {
                    obj.put("user_name", entity.getUserName());
                }
                if (entity.getEmployeeId() == null) {
                    obj.put("employee_id", "");
                } else {
                    obj.put("employee_id", entity.getEmployeeId());
                }
                if (entity.getEmail() == null) {
                    obj.put("email", "");
                } else {
                    obj.put("email", entity.getEmail());
                }
                if (entity.getAddress() == null) {
                    obj.put("address", "");
                } else {
                    obj.put("address", entity.getAddress());
                }
                if (entity.getNik() == null) {
                    obj.put("nik", "");
                } else {
                    obj.put("nik", entity.getNik());
                }
                if (entity.getMobilePhone() == null) {
                    obj.put("cellphone", "");
                } else {
                    obj.put("cellphone", entity.getMobilePhone());
                }
                if (entity.getNpwp() == null) {
                    obj.put("npwp", "");
                } else {
                    obj.put("npwp", entity.getNpwp());
                }
                if (entity.getRoleName() == null) {
                    obj.put("role", "");
                } else {
                    obj.put("role", entity.getRoleName());
                }
                if (entity.getLoanAmount() == null) {
                    obj.put("loan_limit", 0);
                } else {
                    obj.put("loan_limit", entity.getLoanAmount());
                }
                if (entity.getTaxStatus() == null) {
                    obj.put("tax_status", "");
                } else {
                    obj.put("tax_status", entity.getTaxStatus());
                }
                if (entity.getStatus() == null) {
                    obj.put("status_employee", "");
                } else {
                    obj.put("status_employee", entity.getStatus());
                }

                if (entity.IsActive() == true) {
                    obj.put("is_active", true);
//                    jsonobj.put("status_employee", "a");
                } else {
                    obj.put("is_active", false);
//                    jsonobj.put("status_employee", "p");
                }
                if (entity.getIsDelete() == false) {
                    obj.put("is_delete", false);
                } else {
                    obj.put("is_delete", true);
//                    jsonobj.put("status_employee", "d");
                }
                if (entity.getGender().equalsIgnoreCase("m") || entity.getGender().equalsIgnoreCase("male")) {
                    obj.put("gender", "m");
                } else {
                    obj.put("gender", "f");
                }
                if (entity.getParentId() == null) {
                    obj.put("aprroved_by", "");
                } else {
                    obj.put("aprroved_by", entity.getParentId().getName());
                }
                if (entity.getLinkCv() == null) {
                    obj.put("doc_cv", "");
                } else {
//                    obj.put("doc_cv", entity.getLinkCv());
//                    jsonobj.put("status_employee", "d");
                    FileOutputStream fop = null;

                    String bytenya = entity.getLinkCv();
                    byte[] imageInByte = ISOUtil.hex2byte(bytenya);
                    File file = new File(entity.getLinkCv());
                    fop = new FileOutputStream(file);

                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fop.write(imageInByte);
                    fop.flush();
                    fop.close();

                    String baseUrl = FilenameUtils.getPath(file.getPath());
                    String myFile_1 = FilenameUtils.getBaseName(file.getPath()) + "." + FilenameUtils.getExtension(file.getPath());

                    obj.put("doc_cv", baseUrl + myFile_1);
                }
                if (entity.getIdEmployee() != null) {
                    List<Account> listAccount = accountService.findByEmployee(entity.getIdEmployee().toString());
                    for (int k = 0; k < listAccount.size(); k++) {
                        Account enAccount = (Account) listAccount.get(k);
                        if (enAccount.getTypeAccount().equalsIgnoreCase("payroll")) {
                            if (enAccount.getBankName() == null) {
                                obj.put("bank_name_p", "");
                            } else {
                                obj.put("bank_name_p", enAccount.getBankName());
                            }
                            if (enAccount.getAccountName() == null) {
                                obj.put("account_name_p", "");
                            } else {
                                obj.put("account_name_p", enAccount.getAccountName());
                            }
                            if (enAccount.getIsActive() == true) {
                                obj.put("is_active", true);
                                obj.put("account_number_p", enAccount.getAccountNumberFinance());
                            }
                            if (enAccount.getIsActive() == false) {
                                obj.put("is_active", false);
                                if (enAccount.getAccountNumberFinance() != null) {
                                    obj.put("account_number_p", enAccount.getAccountNumberFinance());
                                } else {
                                    obj.put("account_number_p", enAccount.getAccountNumber());
                                }
                                obj.put("account_number_p", enAccount.getAccountNumber());
                            }
                            if (enAccount.getIsDelete() == true) {
                                obj.put("is_active_p", false);
                                obj.put("is_delete_p", true);
                            }
                            if (enAccount.getIsDelete() == false) {
                                obj.put("is_active_p", true);
                                obj.put("is_delete_p", false);
                            }
                        }
                        if (enAccount.getTypeAccount().equalsIgnoreCase("loan")) {
                            if (enAccount.getBankName() == null) {
                                obj.put("bank_name_l", "");
                            } else {
                                obj.put("bank_name_l", enAccount.getBankName());
                            }
                            if (enAccount.getAccountName() == null) {
                                obj.put("account_name_l", "");
                            } else {
                                obj.put("account_name_l", enAccount.getAccountName());
                            }
                            if (enAccount.getIsActive() == true) {
                                obj.put("is_active", true);
                                obj.put("account_number_l", enAccount.getAccountNumberFinance());
                            }
                            if (enAccount.getIsActive() == false) {
                                obj.put("is_active", false);
                                if (enAccount.getAccountNumberFinance() != null) {
                                    obj.put("account_number_l", enAccount.getAccountNumberFinance());
                                } else {
                                    obj.put("account_number_l", enAccount.getAccountNumber());
                                }

                            }
                            if (enAccount.getIsDelete() == true) {
                                obj.put("is_active_l", false);
                                obj.put("is_delete_l", true);
                            }
                            if (enAccount.getIsDelete() == false) {
                                obj.put("is_active_l", true);
                                obj.put("is_delete_l", false);
                            }
                        }

                    }
                }
                return ResponseEntity.ok(obj.toString());
            }

        } catch (JSONException | IOException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "find-by-id");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
        CreateLog.createJson("05" + "Error" + "Data Not Found", "find-by-id");
        return new ResponseEntity(new CustomErrorType("05", "Error", "Data Not Found"),
                HttpStatus.NOT_FOUND);

    }

//    @RequestMapping(value = "/employee/find-by-employee-id", method = RequestMethod.POST, produces = {"application/json"})
    @PutMapping(value = "/find-by-employee-id/{employeeId}", produces = {"application/json"})
    public ResponseEntity<String> findByEmployeeID(@RequestBody
            final DataEmployee object,
            @PathVariable("employeeId") String employeeId
    ) {
        try {
            Employee entity = this.employeeService.findByEmployeeId(object.getEmployeeId());
            JSONObject obj = new JSONObject();
            if (entity != null) {

                if (entity.getIdEmployee() == null) {
                    obj.put("id_employee", "");
                } else {
                    obj.put("id_employee", entity.getIdEmployee());
                }

                if (entity.getEmployeeId() == null) {
                    obj.put("employee_id", "");
                } else {
                    obj.put("employee_id", entity.getEmployeeId());
                }
                if (entity.getName() == null) {
                    obj.put("nama", "");
                } else {
                    obj.put("nama", entity.getName());
                }
                if (entity.getUserName() == null) {
                    obj.put("user_name", "");
                } else {
                    obj.put("user_name", entity.getUserName());
                }
                if (entity.getEmployeeId() == null) {
                    obj.put("employee_id", "");
                } else {
                    obj.put("employee_id", entity.getEmployeeId());
                }
                if (entity.getNik() == null) {
                    obj.put("nik", "");
                } else {
                    obj.put("nik", entity.getNik());
                }
                if (entity.getNpwp() == null) {
                    obj.put("npwp", "");
                } else {
                    obj.put("npwp", entity.getNpwp());
                }
                if (entity.getAddress() == null) {
                    obj.put("address", "");
                } else {
                    obj.put("address", entity.getNpwp());
                }
                if (entity.getIdEmployee() != null) {
                    List<Account> listAccount = accountService.findByEmployee(entity.getIdEmployee().toString());
                    for (int k = 0; k < listAccount.size(); k++) {
                        Account enAccount = (Account) listAccount.get(k);
                        if (enAccount.getTypeAccount().equalsIgnoreCase("payroll")) {
                            if (enAccount.getBankName() == null) {
                                obj.put("bank_name_p", "");
                            } else {
                                obj.put("bank_name_p", enAccount.getBankName());
                            }
                            if (enAccount.getAccountName() == null) {
                                obj.put("account_name_p", "");
                            } else {
                                obj.put("account_name_p", enAccount.getAccountName());
                            }
                            if (enAccount.getIsActive() == true) {
                                obj.put("is_active", true);
                                obj.put("account_number_p", enAccount.getAccountNumberFinance());
                            }
                            if (enAccount.getIsActive() == false) {
                                obj.put("is_active", false);
                                if (enAccount.getAccountNumberFinance() != null) {
                                    obj.put("account_number_p", enAccount.getAccountNumberFinance());
                                } else {
                                    obj.put("account_number_p", enAccount.getAccountNumber());
                                }
                                obj.put("account_number_p", enAccount.getAccountNumber());
                            }
                            if (enAccount.getIsDelete() == true) {
                                obj.put("is_active_p", false);
                                obj.put("is_delete_p", true);
                            }
                            if (enAccount.getIsDelete() == false) {
                                obj.put("is_active_p", true);
                                obj.put("is_delete_p", false);
                            }
                        }
                        if (enAccount.getTypeAccount().equalsIgnoreCase("loan")) {
                            if (enAccount.getBankName() == null) {
                                obj.put("bank_name_l", "");
                            } else {
                                obj.put("bank_name_l", enAccount.getBankName());
                            }
                            if (enAccount.getAccountName() == null) {
                                obj.put("account_name_l", "");
                            } else {
                                obj.put("account_name_l", enAccount.getAccountName());
                            }
                            if (enAccount.getIsActive() == true) {
                                obj.put("is_active", true);
                                obj.put("account_number_l", enAccount.getAccountNumberFinance());
                            }
                            if (enAccount.getIsActive() == false) {
                                obj.put("is_active", false);
                                if (enAccount.getAccountNumberFinance() != null) {
                                    obj.put("account_number_l", enAccount.getAccountNumberFinance());
                                } else {
                                    obj.put("account_number_l", enAccount.getAccountNumber());
                                }

                            }
                            if (enAccount.getIsDelete() == true) {
                                obj.put("is_active_l", false);
                                obj.put("is_delete_l", true);
                            }
                            if (enAccount.getIsDelete() == false) {
                                obj.put("is_active_l", true);
                                obj.put("is_delete_l", false);
                            }
                        }

                    }
                }
                return ResponseEntity.ok(obj.toString());
            } else {
                rs.setResponse_code("05");
                rs.setInfo("Error");
                rs.setResponse("EData Eemployee Id Not Found");
                CreateLog.createJson(rs, "find-by-employee-id");
                return new ResponseEntity(new CustomErrorType("05", "Error", "Data Eemployee Id Not Found"),
                        HttpStatus.NOT_FOUND);

            }

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "find-by-employee-id");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @PermitAll
    @RequestMapping(value = "/employe-role/role-name", method = RequestMethod.GET, produces = {"application/json"})
//    @PreAuthorize("hasRole('sysadmin') or hasRole('admin')  or hasRole('finance')")
    public ResponseEntity<String> listRoleName(ServletRequest request
    ) {
        try {
            List<EmployeeRole> entityList = this.employeeRoleService.listRole();
            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                EmployeeRole data = entityList.get(i);
                JSONObject obj = new JSONObject();
                if (data.getRoleName() == null) {
                    obj.put("role_name", "");
                } else {
                    obj.put("role_name", data.getRoleName());
                }
                array.put(obj);
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "employe-role_list-role-name");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @PermitAll
    @PutMapping(value = "/download-cv/{employeeId}", produces = {"application/json"})
    public ResponseEntity<String> downloadCv(ServletRequest request,
            @PathVariable("employeeId") String employeeId
    ) {
        try {
            return null;
        } catch (Exception ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "download-cv");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }
}
