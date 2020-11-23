/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.Account;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.EmployeeRole;
import com.lawfirm.apps.model.LoanHistory;
import com.lawfirm.apps.model.LoanType;
import com.lawfirm.apps.service.CaseDocumentService;
import com.lawfirm.apps.service.interfaces.AccountServiceIface;
import com.lawfirm.apps.service.interfaces.CaseDetailsServiceIface;
import com.lawfirm.apps.service.interfaces.ClientDataServiceIface;
import com.lawfirm.apps.service.interfaces.DocumentReimburseServiceIface;
import com.lawfirm.apps.service.interfaces.EmployeeRoleServiceIface;
import com.lawfirm.apps.service.interfaces.EmployeeServiceIface;
import com.lawfirm.apps.service.interfaces.EngagementServiceIface;
import com.lawfirm.apps.service.interfaces.LoanServiceIface;
import com.lawfirm.apps.service.interfaces.LoanTypeServiceIface;
import com.lawfirm.apps.service.interfaces.ProfessionalServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import com.lawfirm.apps.support.api.AprovedApi;
import com.lawfirm.apps.support.api.DataEmployee;
import com.lawfirm.apps.utils.CustomErrorType;
import com.lawfirm.apps.response.Response;
import com.lawfirm.apps.support.api.AccountApi;
import com.lawfirm.apps.support.api.AproveAcc;
import com.lawfirm.apps.support.api.EmployeeApi;
import com.lawfirm.apps.support.api.EmployeeInactiveDto;
import com.lawfirm.apps.utils.CreateLog;
import com.lawfirm.apps.utils.Util;
import com.xss.filter.annotation.XxsFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jline.utils.Log;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author newbiecihuy
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "*", maxAge = 3600)
//@Slf4j
@RequestMapping({"/employee"})
public class EmployeeController { //LawfirmController 

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

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
    LoanServiceIface loanService;
    @Autowired
    LoanTypeServiceIface loanTypeService;
    @Autowired
    ProfessionalServiceIface professionalService;
    @Autowired
    ReimbursementServiceIface reimbursementService;
    @Autowired
    TeamMemberServiceIface teamMemberService;
    @Autowired
    private PasswordEncoder encoder;
//    private static final AtomicInteger count = new AtomicInteger(1);

    public EmployeeController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfMonth = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.sdfYear = new SimpleDateFormat("yyyy-MM-dd");
    }

    @PostMapping(path = "/managed-employee", produces = {"application/json"})
    @XxsFilter
    public Response createEmployee(@RequestBody final DataEmployee object, Authentication authentication) {
        try {
            String nama = authentication.getName();
            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            CreateLog.createJson(object, "create-employee_16");
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "create-employee");
                log.info("Error msg" + rs.toString());
                return rs;
            }
//            Employee dataEmp = employeeService.findById(id_employee_admin);
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());

            log.info("dataEmp : " + dataEmp.getRoleName());
            if (dataEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "create-employee");
                log.info("Error msg" + rs.toString());
                return rs;
            }
            if (!dataEmp.getRoleName().matches("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "create-employee");
                log.info("Error msg" + rs.toString());
                return rs;
            }
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
//            Double salary = object.getSalary();
            Boolean cheking = Util.validation(email);
            Boolean process = true;
//            if (!authentication.getPrincipal().toString().contains("admin") || !authentication.getPrincipal().toString().contains("sysadmin")) {
//                rs.setResponse_code(HttpStatus.FORBIDDEN.toString());
//                rs.setInfo("fail");
//                rs.setResponse("role : " + authentication.getAuthorities() + ", cannot access managed-employee, Permission denied");
//                process = false;
//                CreateLog.createJson(rs, "create-employee");
//            }
            if (cheking.equals(false)) {

                rs.setResponse_code("55");
                rs.setInfo("You have entered an invalid email address :" + email);
                rs.setResponse("Create Employee Failed");
                log.info("Error msg" + rs.toString());
                CreateLog.createJson(rs, "create-employee");
                return rs;
            } else {

                Employee cekEMail = employeeService.findByEmployee(email);
                System.out.println("cekEmail" + cekEMail);
                if (cekEMail != null) {
                    rs.setResponse_code("55");
                    rs.setInfo("Email :" + email + "already registered ");
                    rs.setResponse("Create Employee Failed");
                    log.info("Error msg" + rs.toString());
                    CreateLog.createJson(rs, "create-employee");
                    process = false;
                    return rs;
                }
                Employee cekNik = employeeService.findByEmployee(nik);
                System.out.println("cekNik" + cekNik);
                if (cekNik != null) {
                    rs.setResponse_code("55");
                    rs.setInfo("Nik :" + nik + "already registered ");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                name = name.replaceAll("\\s+", "");
                if (name.length() > 25) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field Name maksimum 25 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                if (object.getUser_name().isEmpty()) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field USER Name can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                if (name.length() == 0) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field Name can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                if (npwp.length() == 0) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field NPWP can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                address = address.replaceAll("\\s+", "");
                if (address.length() == 0) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field Address can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                if (nik.length() == 0) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field Nik can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                if (email.length() == 0) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field Email can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                if (tax_status.length() == 0) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field Taxt Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                if (mobile_phone.length() == 0) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field Mobile Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }

                if (object.getBank_name_p().length() == 0 || object.getBank_name_l().length() == 0) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field BANK NAME Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                if (object.getAccount_number_p().length() == 0 || object.getAccount_number_l().length() == 0) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field ACCOUNT NUMBER Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                if (object.getAccount_name_p().length() == 0 || object.getAccount_name_l().length() == 0) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field ACCOUNT NAME Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }

                if (email.length() > 30) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field Email Maximum 30 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }

                if (mobile_phone.length() > 20) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field Mobile  Maximum 20 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                address = address.replaceAll("\\s+", "");
                if (address.length() > 200) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field Address Maximum 200 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                account_name_l = account_name_l.replaceAll("\\s+", "");
                account_name_p = account_name_p.replaceAll("\\s+", "");
                if (account_name_l.length() > 20 || account_name_p.length() > 20) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field ACCOUNT NAME Maximum 20 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                bank_name_l = bank_name_l.replaceAll("\\s+", "");
                bank_name_p = bank_name_p.replaceAll("\\s+", "");
                if (bank_name_l.length() > 25 || bank_name_p.length() > 25) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field BANK NAME Maximum 25 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                if (account_number_l.length() > 25 || account_number_p.length() > 25) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field ACCOUNT NUMBER Maximum 25 character");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                if (nik.length() > 16 || nik.length() < 16) {
                    rs.setResponse_code("55");
                    rs.setInfo("Field Nik maximum 16 digits");
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                Account cekAcp = accountService.findAccount(account_number_p);
                if (cekAcp != null) {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("Account Number : " + account_number_p + " already Registered With Another User");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                Account cekAcl = accountService.findAccount(account_number_l);
                if (cekAcl != null) {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("Account Number : " + account_number_l + " already Registered With Another User");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
//                }
                Employee dataAdmin = this.employeeService.findById(object.getId_employee_admin());
                if (dataAdmin == null) {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("Your Account : " + dataAdmin + " Not Found");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                EmployeeRole cekRole = this.employeeRoleService.findByName(object.getRole_name());
                if (cekRole == null) {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("Your Role Name :" + role_name + ", is not registered ");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }

                String dt = dateFormat.format(object.getJoin_date());
                Date join_date = dateFormat.parse(dt);
//                    if (object.getRegister_date() == null) {
//                        newEmployee.setDateRegister(new Date());
//                    } else {
//                        String reg_date = dateFormat.format(object.getRegister_date());
//                        Date reg_val = dateFormat.parse(reg_date);
//                        newEmployee.setDateRegister(reg_val);
//                    }
                if (join_date == null) {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("Field register_date date can't be NULL");
                    CreateLog.createJson(rs, "create-employee");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
                if (process) {

                    Employee newEmployee = new Employee();
                    Account accP = new Account();
                    Account accl = new Account();

                    newEmployee.setName(name);
                    if (object.getUser_name().equals("") || object.getUser_name().isEmpty()) {
                        newEmployee.setUserName(email.toLowerCase().trim());
                    } else {
                        newEmployee.setUserName(object.getUser_name().toLowerCase().trim());
                    }
                    newEmployee.setMobilePhone(mobile_phone);
                    newEmployee.setAddress(address);
                    newEmployee.setNik(nik);
                    newEmployee.setEmail(email);
                    newEmployee.setNpwp(npwp);
                    switch (tax_status) {
                        case "TK":
                            newEmployee.setTaxStatus("TK0");
                            break;
                        case "TK0":
                            newEmployee.setTaxStatus("TK0");
                            break;
                        case "TK1":
                            newEmployee.setTaxStatus("TK1");
                            break;
                        case "TK2":
                            newEmployee.setTaxStatus("TK2");
                            break;
                        case "TK3":
                            newEmployee.setTaxStatus("TK3");
                            break;
                        case "K0":
                            newEmployee.setTaxStatus("K0");
                            break;
                        case "K1":
                            newEmployee.setTaxStatus("K1");
                            break;
                        case "K2":
                            newEmployee.setTaxStatus("K2");
                            break;
                        case "K3":
                            newEmployee.setTaxStatus("K3");
                            break;
                        default:
                            newEmployee.setTaxStatus(tax_status);
                            break;
                    }

//                    newEmployee.setSalary(object.getSalary());
                    if (object.getSalary() != null) {
                        newEmployee.setSalary(object.getSalary());
                    } else {
                        newEmployee.setSalary(0d);
                    }
                    Log.info("object.getUser_pass() : " + object.getUser_pass());
                    Log.info("encoder.encode(object.getUser_pass()) : " + encoder.encode(object.getUser_pass()));
                    if (object.getUser_pass() != null) {
                        newEmployee.setPassword(encoder.encode(object.getUser_pass()));
                    } else {
                        newEmployee.setPassword(encoder.encode(email));
                    }

                    newEmployee.setParentId(dataAdmin);
                    newEmployee.setRoleName(role_name);
                    newEmployee.setApproved_date(new Date());
                    newEmployee.setDateRegister(join_date);
//                    if (object.getRegister_date() == null) {
//                        newEmployee.setDateRegister(new Date());
//                    } else {
//                        String reg_date = dateFormat.format(object.getRegister_date());
//                        Date reg_val = dateFormat.parse(reg_date);
//                        newEmployee.setDateRegister(reg_val);
//                    }

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
                    return approvedByAdmin(obj, authentication);
                }
            }
        } catch (ParseException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "create-employee");
            log.info("Error msg" + ex.getMessage());
        }
//        rs.setResponse_code("55");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");
        return rs;
    }

//    @PutMapping(path = "/managed-employee/{id_employee}", produces = {"application/json"})
    @RequestMapping(value = "/managed-employee/update-profile", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response updateProfile(@RequestBody final EmployeeApi object, Authentication authentication) {
        try {
            Boolean process = true;
            Boolean cheking = false;

            String nama = authentication.getName();
            log.info("update-profile jsonOject : " + object);
            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "update-profile");
                log.info("Error msg" + rs.toString());
                process = false;
                return rs;
            }
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());

            log.info("dataEmp getRoleName : " + dataEmp.getRoleName());
            if (dataEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "update-profile");
                log.info("Error msg" + rs.toString());
                process = false;
                return rs;
            }

            if (!dataEmp.getRoleName().matches("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "update-profile");
                log.info("Error msg" + rs.toString());
                process = false;
                return rs;
            }

//           Employee updateEmployee = employeeService.findById(id_employee);
            log.info("entityEmp.getIdEmployee()" + entityEmp.getIdEmployee());
            Employee dataEmployee = employeeService.findByEmail(object.getEmail());
            Employee updateEmployee = employeeService.findById(dataEmployee.getIdEmployee());//object.getId_employee()
            log.info("updateEmployee" + updateEmployee);
            if (updateEmployee == null) {
                rs.setResponse_code("55");
                rs.setInfo("Data null");
                rs.setResponse("Employe not Found");
                CreateLog.createJson(rs, "update-profile");
                log.info("Error msg" + rs.toString());
                process = false;
                return rs;
            }

            if (!object.getEmail().isBlank()) {
                cheking = Util.validation(object.getEmail());
                if (cheking.equals(false)) {
                    rs.setResponse_code("55");
                    rs.setInfo("You have entered an invalid email address :" + object.getEmail());
                    rs.setResponse("Create Employee Failed");
                    CreateLog.createJson(rs, "update-profile");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;

                }

                if (object.getEmail().length() > 30) {
                    rs.setResponse_code("55");
                    rs.setInfo("Create Employee Failed");
                    rs.setResponse("Field Email Maximum 30 character");
                    CreateLog.createJson(rs, "update-profile");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
            }

//            
            String namaEmpl = null;
            if (!object.getName().isBlank() || !object.getName().isEmpty()) {
                namaEmpl = object.getName();
            }
            namaEmpl = namaEmpl.replaceAll("\\s+", "");
            if (namaEmpl.length() > 25) {
                rs.setResponse_code("55");
                rs.setInfo("Field Name maksimum 25 character");
                rs.setResponse("Create Employee Failed");
                CreateLog.createJson(rs, "update-profile");
                log.info("Error msg" + rs.toString());
                process = false;
                return rs;
            }
            String npwp = null;
            if (!object.getNpwp().isBlank() || !object.getNpwp().isEmpty()) {
                npwp = object.getNpwp();
                if (npwp.length() > 30) {
                    rs.setResponse_code("55");
                    rs.setInfo("failed");
                    rs.setResponse("Create Employee Failed, NPWP  Field max 20");
                    CreateLog.createJson(rs, "update-profile");
                    log.info("Error msg" + rs.toString());
                    process = false;
                    return rs;
                }
            }

            if (object.getCell_phone().length() > 20) {
                rs.setResponse_code("55");
                rs.setInfo("Create Employee Failed");
                rs.setResponse("Field Mobile  Maximum 20 character");
                CreateLog.createJson(rs, "update-profile");
                log.info("Error msg" + rs.toString());
                process = false;
                return rs;
            }
            String address = null;
            if (!object.getAddress().isEmpty()) {
                address = object.getAddress();
            }

            address = address.replaceAll("\\s+", "");
            if (address.length() > 200) {
                rs.setResponse_code("55");
                rs.setInfo("Create Employee Failed");
                rs.setResponse("Field Address Maximum 200 character");
                CreateLog.createJson(rs, "update-profile");
                log.info("Error msg" + rs.toString());
                process = false;
                return rs;
            }
//                

            if (process) {
//                    List<Account> accoutList = accountService.findByEmployee(id_employee.toString());
//                    System.out.println("accoutList" + accoutList.toString());
                log.info("process : " + process);
                if (object.getName() != null) {
                    if (namaEmpl != null) {
                        updateEmployee.setName(namaEmpl);
                    }
                }
                if (!object.getAddress().isEmpty()) {
                    if (!address.isEmpty()) {
                        updateEmployee.setAddress(address);
                    }
                }
                if (!object.getCell_phone().isEmpty()) {
                    Employee cekPhone = employeeService.findByEmployee(object.getCell_phone());
                    if (cekPhone == null) {
                        updateEmployee.setMobilePhone(object.getCell_phone());
                    } else {
                        if (!cekPhone.getIdEmployee().equals(updateEmployee.getIdEmployee())) {
                            rs.setResponse_code("55");
                            rs.setInfo("Update Failed");
                            rs.setResponse("Phone Number : " + object.getCell_phone() + " Already Registered With Another User");
                            CreateLog.createJson(rs, "update-profile");
                            log.info("Error msg" + rs.toString());
                            return rs;
                        }
                    }
                }
                if (!object.getNik().isEmpty()) {
                    Employee cekNik = employeeService.findByEmployee(object.getNik());
                    if (cekNik == null) {
                        updateEmployee.setNik(object.getNik());
                    } else {
                        if (!cekNik.getIdEmployee().equals(updateEmployee.getIdEmployee())) {
                            rs.setResponse_code("55");
                            rs.setInfo("Update  Failed");
                            rs.setResponse("Nik Number : " + object.getNik() + " Already Registered With Another User");
                            CreateLog.createJson(rs, "update-profile");
                            log.info("Error msg" + rs.toString());
                            return rs;
                        }
                    }
                }
                if (!object.getNpwp().isEmpty()) {
                    Employee cekNpwp = employeeService.findByEmployee(object.getNpwp());
                    if (cekNpwp == null) {
                        updateEmployee.setNpwp(object.getNpwp());
                    } else {
                        if (!cekNpwp.getIdEmployee().equals(updateEmployee.getIdEmployee())) {
                            rs.setResponse_code("55");
                            rs.setInfo("Update  Failed");
                            rs.setResponse("NPWP Number : " + object.getNpwp() + " Already Registered With Another User");
                            CreateLog.createJson(rs, "update-profile");
                            log.info("Error msg" + rs.toString());
                            return rs;
                        }
                    }
                }
                if (!object.getEmail().isEmpty()) {
                    Employee cekEMail = employeeService.findByEmployee(object.getEmail());
                    if (cekEMail == null) {
                        updateEmployee.setEmail(object.getEmail());
                    } else {
                        if (!cekEMail.getIdEmployee().equals(updateEmployee.getIdEmployee())) {
                            rs.setResponse_code("55");
                            rs.setInfo("Update Employee Failed");
                            rs.setResponse("Email Address : " + object.getEmail() + " Already Registered With Another User");
                            CreateLog.createJson(rs, "update-profile");
                            log.info("Error msg" + rs.toString());
                            return rs;
                        }
                    }
                }
                if (!object.getTax_status().isEmpty()) {
                    String tax_status = object.getTax_status();
//                        updateEmployee.setTaxStatus(object.getTax_status());
                    switch (tax_status) {
                        case "TK":
                            updateEmployee.setTaxStatus("TK0");
                            break;
                        case "TK0":
                            updateEmployee.setTaxStatus("TK0");
                            break;
                        case "TK1":
                            updateEmployee.setTaxStatus("TK1");
                            break;
                        case "TK2":
                            updateEmployee.setTaxStatus("TK2");
                            break;
                        case "TK3":
                            updateEmployee.setTaxStatus("TK3");
                            break;
                        case "K0":
                            updateEmployee.setTaxStatus("K0");
                            break;
                        case "K1":
                            updateEmployee.setTaxStatus("K1");
                            break;
                        case "K2":
                            updateEmployee.setTaxStatus("K2");
                            break;
                        case "K3":
                            updateEmployee.setTaxStatus("K3");
                            break;
                        default:
                            updateEmployee.setTaxStatus(tax_status);
                            break;
                    }
                }
                if (!object.getUser_name().isEmpty()) {
                    Employee cekUname = employeeService.findByEmployee(object.getUser_name());
                    if (cekUname == null) {
                        updateEmployee.setUserName(object.getUser_name());
                    } else {
                        if (!cekUname.getIdEmployee().equals(updateEmployee.getIdEmployee())) {
                            rs.setResponse_code("55");
                            rs.setInfo("Update Employee Failed");
                            rs.setResponse("UserName : " + object.getUser_name() + " Already Registered With Another User");
                            CreateLog.createJson(rs, "update-profile");
                            log.info("Error msg" + rs.toString());
                            return rs;
                        }
                    }
                }

                if (!object.getGender().isEmpty()) {
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
                if (object.getLoan_limit() != null) {
                    updateEmployee.setLoanAmount(object.getLoan_limit());
                }

                Employee newEmployee = employeeService.update(updateEmployee);
//                log.info("isi" + newEmployee.getIdEmployee().toString());
                if (newEmployee != null) {

                    rs.setResponse_code("00");
                    rs.setInfo("Succes");
                    rs.setResponse("Employee : " + newEmployee.getEmployeeId() + "Success");//dataAdmin.getName());
                    log.info("Succes msg" + rs.toString());
                    CreateLog.createJson(rs, "update-profile");
                    return rs;
                }
//                    AprovedApi obj = new AprovedApi();
//                    obj.setId_employee_admin(updateEmployee.getParentId().getIdEmployee());
//                    obj.setId_employee(updateEmployee.getIdEmployee());
//                    return approvedByAdmin(obj, authentication);
            }

//            return rs;
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "update-profile");
        }
        rs.setResponse_code("55");
        rs.setInfo("Data null");
        rs.setResponse("Update Employee Failed");
        return rs;
    }

    @RequestMapping(value = "/managed-employee/account", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response updateAccount(@RequestBody final DataEmployee object, Integer val, Authentication authentication) {
        try {
            Boolean process = true;
            String nama = authentication.getName();
            log.info("nama : " + nama);
            log.info("Json Update Account" + object);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "update-account");
                log.info("Error msg" + rs.toString());
                return rs;
            }
            Employee updateEmployee = employeeService.findById(object.getId_employee());//entityEmp.getIdEmployee()
            if (updateEmployee == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Data Employee not found");
                CreateLog.createJson(rs, "update-account");
                process = false;
                log.info("Error msg" + rs.toString());
                return rs;
            }
            Account cekAcp = accountService.findAccount(object.getAccount_number_p());
            if (cekAcp != null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Account Number : " + object.getAccount_number_p() + " already Registered With Another User");
                CreateLog.createJson(rs, "update-account");
                process = false;
                log.info("Error msg" + rs.toString());
                return rs;
            }
            Account cekAcl = accountService.findAccount(object.getAccount_number_l());
            if (cekAcl != null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Account Number : " + object.getAccount_number_l() + " already Registered With Another User");
                CreateLog.createJson(rs, "create-employee");
                process = false;
                log.error("Error msg" + rs.toString());
                return rs;
            }
            if (process) {
                List<Account> accoutList = accountService.findByEmployee(entityEmp.getIdEmployee().toString());
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
                            upd_acc_payroll.setAccountNumberFinance(object.getAccount_number_p());
                            upd_acc_payroll.setIsActive(true);
                        }
                        upd_acc_payroll.setTypeAccount("payroll");
                        upd_acc_payroll = this.accountService.update(upd_acc_payroll);
//                      updateEmployee.addAccount(upd_acc_payroll);
//                      log.info("upd_acc_payroll" + upd_acc_payroll.toString());
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
//                            upd_acc_loan.setAccountNumber(object.getAccount_number_l());
                            upd_acc_loan.setAccountNumberFinance(object.getAccount_number_l());
                            upd_acc_loan.setIsActive(true);
                        }
                        upd_acc_loan.setTypeAccount("loan");
                        upd_acc_loan = this.accountService.update(upd_acc_loan);
//                      updateEmployee.addAccount(upd_acc_loan);
//                      log.info("upd_acc_loan" + upd_acc_loan.toString());
                    }
                }
                updateEmployee = employeeService.update(updateEmployee);
                rs.setResponse_code("00");
                rs.setInfo("Success");
                rs.setResponse("Update account employee Id :" + updateEmployee.getEmployeeId());
                CreateLog.createJson(rs, "update-account");
                log.info("Succes msg" + rs.toString());
                return rs;
            } else {

                CreateLog.createJson(rs, "update-account");
                log.error("Error msg" + rs.toString());
                return rs;
            }
        } catch (Exception ex) {
//            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("55");
            rs.setInfo("failes");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "upload-account");
            log.error("Error msg" + rs.toString());
            return rs;
        }
//            return rs;
//        return rs;

    }

    @RequestMapping(value = "/approval/account", method = RequestMethod.POST)
    @XxsFilter
    public Response approvedAccount(@RequestBody final AproveAcc object, Authentication authentication) {
        try {

            Boolean process = true;
            String nama = authentication.getName();
            log.info("nama : " + nama);
            Employee entityAdmin = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityAdmin);
            if (entityAdmin == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                log.error("Error msg" + rs.toString());
                CreateLog.createJson(rs, "approved-account");
                return rs;
            }
//            Employee updateEmployee = employeeService.findById(entityEmp.getIdEmployee());
            if (!entityAdmin.getRoleName().equals("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "approved-account");
                log.error("Error msg" + rs.toString());
                process = false;
                return rs;
            }
            Employee employee = employeeService.findById(object.getId_employee());
            if (employee == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee NUll");
                CreateLog.createJson(rs, "approved-account");
                log.error("Error msg" + rs.toString());
                process = false;
                return rs;
            }
            if (process) {
                List<Account> listAccount = accountService.findByEmployee(employee.getIdEmployee().toString());
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
                        rs.setResponse_code("00");
                        rs.setInfo("Succes");
                        rs.setResponse("Account approved By : " + entityAdmin.getEmployeeId());
                        CreateLog.createJson(rs, "approved-account");
                        log.error("Succes msg" + rs.toString());
                    } else {
                        rs.setResponse_code("55");
                        rs.setInfo("Warning");
                        rs.setResponse("Employee Null");
                        CreateLog.createJson(rs, "approved-account");
                        log.error("Error msg" + rs.toString());
                    }
                }
            }
            return rs;
        } catch (Exception ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "approved-by-admin");
            log.error("Error msg" + ex.getMessage());
        }
        return rs;

    }
    //
//    @PutMapping(path = "/managed-employee/set-password/{id_employee}", produces = {"application/json"})

    @RequestMapping(path = "/managed-employee/{id_employee}/set-password", produces = {"application/json"}, method = RequestMethod.POST)
    @XxsFilter
    public Response setPassword(@RequestBody
            final DataEmployee object, @PathVariable("id_employee") Long id_employee, Authentication authenticationRequest) {
        try {
            Boolean process = true;

            Date todayDate = new Date();
            Date now = new Date();
            String name = authenticationRequest.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't access this feature");
                CreateLog.createJson(rs, "set-password");
                log.info("Error msg" + rs.toString());
                return rs;
            }
//            Employee entity = employeeService.findById(id_employee);
            Employee entity = employeeService.findById(entityEmp.getIdEmployee());
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("Employee not found");
                CreateLog.createJson(rs, "set-password");
                log.info("Error msg" + rs.toString());
                process = false;
            }
            if (process) {
                entity.setPassword(encoder.encode(object.getUser_pass()));
//                entity.setPassword(object.getUser_pass());
                Employee empSetPassword = this.employeeService.update(entity);
                if (empSetPassword != null) {
                    rs.setResponse_code("00");
                    rs.setInfo("Success");
                    rs.setResponse("set password employee Id " + entity.getEmployeeId() + " success");
                    CreateLog.createJson(rs, "set-password");
                    log.info("Success msg" + rs.toString());
                    return rs;
                }
            }
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse("Employee not found");
            CreateLog.createJson(rs, "set-password");
            return rs;
        } catch (Exception ex) {
//            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "set-password");
            log.info("Error msg" + ex.getMessage());
            return rs;
        }

    }

    @RequestMapping(path = "/managed-employee/password", produces = {"application/json"}, method = RequestMethod.PUT)
    @XxsFilter
    public Response changePassword(@RequestBody
            final DataEmployee object, Authentication authenticationRequest) {
        try {
            Boolean process = true;
            Date todayDate = new Date();
            Date now = new Date();
            String name = authenticationRequest.getName();
            String old_password = object.getOld_password();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't access this feature");
                CreateLog.createJson(rs, "changePassword");
                return rs;
            }
//            Employee entity = employeeService.findById(id_employee);
            Employee entity = employeeService.findById(entityEmp.getIdEmployee());
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("Employee not found");
                CreateLog.createJson(rs, "changePassword");
                process = false;
            }
//            Employee cekPassword = employeeService.findById(entityEmp.getIdEmployee());
            log.info("password lama before : " + object.getOld_password());
            String password_s = entityEmp.getPassword();
            log.info("password_s lama : " + password_s);

//            PasswordEncoder isiPass = passwordEncoder(object.getOld_password());
//            log.info("isiPass : " + isiPass.toString());
//            Employee cekPassword = employeeService.cekPass(isiPass.toString(), entityEmp.getIdEmployee());
//            if (cekPassword == null) {
//                rs.setResponse_code("55");
//                rs.setInfo("Error");
//                rs.setResponse("old password not macth ");
//                CreateLog.createJson(rs, "changePassword");
//                process = false;
//            }
            PasswordEncoder passwordEnocder = new BCryptPasswordEncoder();
            if (passwordEnocder.matches(object.getOld_password(), entityEmp.getPassword())) {
                System.out.println("Matched!");
                process = true;
            } else {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("old password not macth ");
                CreateLog.createJson(rs, "changePassword");
                process = false;
                return rs;
            }
            if (process) {
                entity.setPassword(encoder.encode("lawfirm" + object.getUser_pass()));
//                entity.setPassword(object.getUser_pass());

                Employee setPassword = this.employeeService.update(entity);
                if (setPassword != null) {
                    rs.setResponse_code("00");
                    rs.setInfo("Success");
                    rs.setResponse("set password employee Id " + entityEmp.getEmployeeId() + " success");
                    CreateLog.createJson(rs, "changePassword");
                    return rs;
                }
            }
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse("Employee not found");
            CreateLog.createJson(rs, "changePassword");
            return rs;
        } catch (Exception ex) {
//            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "changePassword");
            return rs;
        }

    }

    @RequestMapping(value = "/managed-employee/{id_employee}/cv", method = RequestMethod.POST)//produces = {"application/json"},
    @XxsFilter
    public Response UploadedCv(@RequestPart("doc_cv") MultipartFile file,
            @PathVariable("id_employee") Long id_employee, Authentication authenticationRequest) throws IOException {
        try {
            Boolean cv = null;
            if (!file.isEmpty()) {
                cv = true;
            }
            log.info("{cv : " + cv + "}," + "{id_employee : " + id_employee + "}");
            Date todayDate = new Date();
            Date now = new Date();

            String name = authenticationRequest.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't access this feature");
                CreateLog.createJson(rs, "upload-cv");
                return rs;
            }
            String pathDoc = null;
            Boolean process = true;
//            Employee entity = employeeService.findById(id_employee);
            Employee entity = employeeService.findById(entityEmp.getIdEmployee());
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("Employee not found");
                CreateLog.createJson(rs, "upload-cv");
                process = false;
            }
            pathDoc = basepathUpload + "employee" + "/" + entity.getIdEmployee() + "/";
            /*-----------------------------------------------*/
            String fileDownloadUri = null;
            String fileName = null;
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
                    fileName = file.getOriginalFilename().replaceAll(" ", "");
                    Path path = Paths.get(pathDoc + file.getOriginalFilename().replaceAll(" ", ""));
                    Files.write(path, bytes);
                    log.info("file getOriginalFilename : " + file.getOriginalFilename().replaceAll(" ", ""));
                    entity.setLinkCv(pathDoc + file.getOriginalFilename().replaceAll(" ", ""));
//                    fileName = pathDoc + file.getOriginalFilename().replaceAll(" ", "");
                    fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path(pathDoc + file.getOriginalFilename().replaceAll(" ", ""))
                            .path(fileName)
                            .toUriString();
                    Employee newEmp = employeeService.update(entity);
                    if (newEmp != null) {
                        AprovedApi obj = new AprovedApi();

                        obj.setId_employee_admin(newEmp.getParentId().getIdEmployee());
                        obj.setId_employee(newEmp.getIdEmployee());
//                        return approvedByAdmin(obj);
                    } else {
                        rs.setResponse_code("55");
                        rs.setInfo("Error");
                        rs.setResponse("Upload: Failed dataemployee :=>" + newEmp);
                        CreateLog.createJson(rs, "upload-cv");
                        return rs;

                    }

                } else {
//                rs.setResponse_code("55");
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
                rs.setResponse("Upload: Succes" + fileName);
                CreateLog.createJson(rs, "upload-cv");
                return rs;
            }
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse("Upload: Failed");
            CreateLog.createJson(rs, "upload-cv");

//            return demployee;
        } catch (IOException ex) {
//            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "upload-cv");
            return rs;
        }
        return rs;

    }

    @RequestMapping(value = "/acount/find-by-employee", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> findAccountByEmployee(@RequestBody
            final AccountApi object, @RequestParam("param") String param) {
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
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(new CustomErrorType("55", "Error", "Data Not Found"),
                HttpStatus.NOT_FOUND);

    }
//    @PreAuthorize("hasRole('sysadmin') or hasRole('admin')")
//     @RequestMapping(value = {"/approvedByAdmin"})

    @RequestMapping(value = "/approved/by-admin", method = RequestMethod.POST)
    @XxsFilter
    public Response approvedByAdmin(@RequestBody
            final AprovedApi object, Authentication authentication
    ) {
        try {

            Boolean process = true;
            String nama = authentication.getName();
            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                log.error("Error msg" + rs.toString());
                CreateLog.createJson(rs, "approved-By-admin");
                return rs;
            }
//            Employee updateEmployee = employeeService.findById(entityEmp.getIdEmployee());
            if (!entityEmp.getRoleName().equals("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                log.error("Error msg" + rs.toString());
                CreateLog.createJson(rs, "approved-By-admin");
                process = false;
                return rs;
            }
            Employee dataAdmin = employeeService.findById(object.getId_employee_admin());
            Employee dataEmployee = employeeService.findById(object.getId_employee());

            if (dataEmployee == null) {
                rs.setResponse_code("55");
                rs.setInfo("Warning");
                rs.setResponse("Employee not Found");
                log.error("Error msg" + rs.toString());
                CreateLog.createJson(rs, "approved-by-admin");
                return rs;
            }

//            }
            Employee cekEmployeeId = employeeService.findByEmployee(object.getEmployee_id());

            if (cekEmployeeId != null) {
                rs.setResponse_code("55");
                rs.setInfo("Warning");
                rs.setResponse("Employee Id : " + object.getEmployee_id() + " Already Registered at " + cekEmployeeId.getNik() + "-" + cekEmployeeId.getName());
                log.error("Error msg" + rs.toString());
                CreateLog.createJson(rs, "approved-by-admin");
                return rs;
            }
//            dataEmployee.setSalary(object.getSalary());
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
                        rs.setResponse_code("00");
                        rs.setInfo("Succes");
                        rs.setResponse("Employee approved By : " + dataAdmin.getName());//dataAdmin.getName());
                        log.info("Succes msg" + rs.toString());
                        CreateLog.createJson(rs, "approved-by-admin");
                        return rs;
                    } else {
                        rs.setResponse_code("55");
                        rs.setInfo("ERORR");
                        rs.setResponse("Employee Null");
                        log.info("Error msg" + rs.toString());
                        CreateLog.createJson(rs, "approved-by-admin");
                        return rs;
                    }
                }
            }

            return rs;
        } catch (Exception ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "approved-by-admin");
            log.info("Error msg" + ex.getMessage());
        }
        return null;

    }

    @RequestMapping(value = "/approved/{id_employee}", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response approved(@RequestBody
            final AprovedApi object,
            @PathVariable("id_employee") Long id_employee
    ) {
        try {
            this.now = new Date();
            this.date_now = timeFormat.format(now);
            Employee dataAdmin = employeeService.findById(object.getId_employee_admin());
            log.info("json approved : " + object);
//        if (!dataAdmin.getRoleName().equalsIgnoreCase("admin")) {
//            rs.setResponse_code("55");
//            rs.setInfo("Warning");
//            rs.setResponse("Your Role can't acces this feature");
//        }
            Employee dataEmployee = employeeService.findById(id_employee);
            if (dataEmployee == null) {
                rs.setResponse_code("55");
                rs.setInfo("Warning");
                rs.setResponse("Employee not Found");
                log.error("ERORR msg : " + rs.toString());
                CreateLog.createJson(rs, "approved");
                return rs;
            }

//        if (dataEmployee.IsActive() == true) {
//            rs.setResponse_code("55");
//            rs.setInfo("Warning");
//            rs.setResponse("Employee status already approved");
//            CreateLog.createJson(rs, "approved");
//        }
            if (dataAdmin.getEmployeeId() == null) {

                Employee cekEmployeeId = employeeService.findByEmployee(object.getEmployee_id());

                if (cekEmployeeId != null) {
                    rs.setResponse_code("55");
                    rs.setInfo("Warning");
                    rs.setResponse("Employee Id : " + object.getEmployee_id() + " Already Registered at " + cekEmployeeId.getNik() + "-" + cekEmployeeId.getName());
                    log.error("ERORR msg : " + rs.toString());
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
                        rs.setResponse_code("00");
                        rs.setInfo("Succes");
                        rs.setResponse("Employee approved By : " + dataAdmin.getName());
                        log.info("Succes msg : " + rs.toString());
                        CreateLog.createJson(rs, "approved");
                    } else {
                        rs.setResponse_code("55");
                        rs.setInfo("Warning");
                        rs.setResponse("Employee Null");
                        log.error("ERORR msg : " + rs.toString());
                        CreateLog.createJson(rs, "approved");
                    }
                }
            }
// 
            return rs;
        } catch (Exception ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "approved");
        }
        return null;

    }

    @PermitAll
    @DeleteMapping(value = "/managed-employee/{id_employee}", produces = {"application/json"})
    @XxsFilter
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

            rs.setResponse_code("00");
            rs.setInfo("Succes");
            rs.setResponse("Employee Deleted");
            CreateLog.createJson(rs, "delete-employee");
        } else {
            rs.setResponse_code("55");
            rs.setInfo("failed");
            rs.setResponse("Employee Null");
            CreateLog.createJson(rs, "delete-employee");
        }
        return rs;
    }

    @PermitAll
    @RequestMapping(value = "/view/list-by-admin", method = RequestMethod.GET, produces = {"application/json"})
//    @PreAuthorize("hasRole('sysadmin') or hasRole('admin')")
    @XxsFilter
    public ResponseEntity<String> viewEmployeeByAdmin(ServletRequest request, Authentication authentication) {
        try {
            int totalPages = 0;
            int totalCount = 0;
            String draw = "0";
            String start = "0";
            String length = "";
            int recordsFiltered = 0;
            String orderColumnIndex = "0";
            String orderDir = "asc";
            draw = request.getParameter("draw");
            start = request.getParameter("start");
            length = request.getParameter("length");

            String nama = authentication.getName();
            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewEmployeeByAdmin");
                log.error("ERORR msg : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
//            Employee dataEmp = employeeService.findById(id_employee_admin);
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());

            log.info("dataEmp : " + dataEmp.getRoleName());
            if (dataEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewEmployeeByAdmin");
                log.error("ERORR msg : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!dataEmp.getRoleName().matches("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewEmployeeByAdmin");
                log.error("ERORR msg : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }

            Map<String, String[]> paramMap = request.getParameterMap();

            int max = employeeService.count();
//            int start = 0;
            String paramString = null;
            if (paramMap.containsKey("start")) {
                start = request.getParameter("start");
            }
            if (paramMap.containsKey("paramString")) {
                paramString = request.getParameter("paramString");
            }
            List<Employee> entityList = null;
            if (start == null) {
                entityList = this.employeeService.listEmployee();
            } else {
                entityList = this.employeeService.listEmployeePaging(paramString, max, Integer.parseInt(start));
            }

            JSONArray array = new JSONArray();
            if (entityList != null) {
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
                    if (entity.getSalary() == null) {
                        jsonobj.put("salary", "");
                    } else {
                        jsonobj.put("salary", entity.getSalary());
                    }
                    if (entity.getDateRegister() == null) {
                        jsonobj.put("join_date", "");
                    } else {
                        jsonobj.put("join_date", dateFormat.format(entity.getDateRegister()));
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
            }
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("draw", request.getParameter("draw"));
            jsonobj.put("totalpages", totalPages);
            jsonobj.put("length", length);
            jsonobj.put("recordsTotal", entityList.size());
            jsonobj.put("recordsFiltered", recordsFiltered);
            jsonobj.put("rows", array);
//            out.println(jsonobj);
            log.debug("list-Employee : " + jsonobj.toString());
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            CreateLog.createJson(ex.getMessage(), "view-list-by-admin");
            System.out.println("ERROR: " + ex.getMessage());
            log.error("ERORR msg : " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/view/list-by-finance", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> viewEmployeeByFinance(ServletRequest request, Authentication authentication
    ) {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            String nama = authentication.getName();
            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                log.error("ERORR msg : " + rs.toString());
                CreateLog.createJson(rs, "viewEmployeeByAdmin");
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
//            Employee dataEmp = employeeService.findById(id_employee_admin);
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());

            log.info("dataEmp : " + dataEmp.getRoleName());
            if (dataEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewEmployeeByAdmin");
                log.error("ERORR msg : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!dataEmp.getRoleName().matches("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewEmployeeByAdmin");
                log.error("ERORR msg : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
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
                if (entity.getSalary() == null) {
                    jsonobj.put("salary", "");
                } else {
                    jsonobj.put("salary", entity.getSalary());
                }
                if (entity.getDateRegister() == null) {
                    jsonobj.put("join_date", "");
                } else {
                    jsonobj.put("join_date", dateFormat.format(entity.getDateRegister()));
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

            log.info("view-list-by-finance msg : " + array.toString());
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            CreateLog.createJson(ex.getMessage(), "view-list-by-finance");
            System.out.println("ERROR: " + ex.getMessage());
            log.error("ERORR msg : " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PermitAll
    @GetMapping(value = "/find-employee", produces = {"application/json"})
    @XxsFilter
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
            log.error("find-employee ERROR msg : " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "find-employee");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(ex.getMessage(),
//                HttpStatus.NOT_FOUND);
    }

    @PermitAll
    @GetMapping(value = "/employe-dash-board", produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> employeDashboard() {
        return new ResponseEntity(new CustomErrorType("55", "Error", ""),
                HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/find-by-id", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> findById(@RequestBody
            final DataEmployee object, Authentication authentication
    ) {
        try {
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "findById");
//                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
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
                if (entity.getSalary() == null) {
                    obj.put("salary", "");
                } else {
                    obj.put("salary", entity.getSalary());
                }
                if (entity.getDateRegister() == null) {
                    obj.put("join_date", "");
                } else {
                    obj.put("join_date", dateFormat.format(entity.getDateRegister()));
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
                    obj.put("doc_cv", entity.getLinkCv());
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
                                obj.put("is_active_acc_p", true);
                                obj.put("account_number_p", enAccount.getAccountNumberFinance());
                            }
                            if (enAccount.getIsActive() == false) {
                                obj.put("is_active_acc", false);
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
                                obj.put("is_active_acc_l", true);
                                obj.put("account_number_l", enAccount.getAccountNumberFinance());
                            }
                            if (enAccount.getIsActive() == false) {
                                obj.put("is_active_acc_l", false);
                                if (enAccount.getAccountNumberFinance() != null) {
                                    obj.put("account_number_l", enAccount.getAccountNumberFinance());
                                } else {
                                    obj.put("account_number_l", enAccount.getAccountNumber());
                                }

                            }
                            if (enAccount.getIsDelete() == true) {
                                obj.put("is_active_acc_l", false);
                                obj.put("is_delete_l", true);
                            }
                            if (enAccount.getIsDelete() == false) {
                                obj.put("is_active_acc_l", true);
                                obj.put("is_delete_l", false);
                            }
                        }

                    }
                }
                return ResponseEntity.ok(obj.toString());
            }

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "find-by-id");
            log.info("find-by-id : " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
        CreateLog.createJson("55" + "Error" + "Data Not Found", "find-by-id");
        return new ResponseEntity(new CustomErrorType("55", "Error", "Data Not Found"),
                HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/managed-employee/{employee_id}/find-by-employee-id", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> findByEmployeeID(@PathVariable("employee_id") String employeeId, Authentication authentication
    ) {
        try {
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "find-by-employee-id");
//                process = false;
                log.error("find-by-employee-id " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            Employee entity = this.employeeService.findByEmployeeId(employeeId);
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
                if (entity.getSalary() == null) {
                    obj.put("salary", "");
                } else {
                    obj.put("salary", entity.getSalary());
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
                                obj.put("is_active_acc_p", true);
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
                                obj.put("is_active_acc_p", false);
                                obj.put("is_delete_p", true);
                            }
                            if (enAccount.getIsDelete() == false) {
                                obj.put("is_active_acc_p", true);
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
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("EData Eemployee Id Not Found");
                CreateLog.createJson(rs, "find-by-employee-id");
                log.error("find-by-employee-id " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Data Eemployee Id Not Found"),
                        HttpStatus.NOT_FOUND);

            }

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "find-by-employee-id");
            log.error("find-by-employee-id " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

//    @PermitAll
    @RequestMapping(value = "/employe-role/role-name", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
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
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/role", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> listRoleNameLawyer(@RequestParam("param") String param, Authentication authentication
    ) {
        try {
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "download-cv");
//                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            List<Employee> entityList = this.employeeService.listEmployeeByRole(param);
            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                Employee data = entityList.get(i);
                JSONObject obj = new JSONObject();
                if (data.getRoleName() == null) {
                    obj.put("role_name", "");
                } else {
                    obj.put("role_name", data.getRoleName());
                }
                if (data.getName() == null) {
                    obj.put("name", "");
                } else {
                    obj.put("name", data.getName());
                }
                if (data.getEmployeeId() == null) {
                    obj.put("employee_id", "");
                } else {
                    obj.put("employee_id", data.getEmployeeId());
                }
                array.put(obj);
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "employe-role_list-role-name");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/managed-employee/{id_employee}/download-cv", method = RequestMethod.GET, produces = {"application/json"})//produces = {"application/json"}
    @XxsFilter
    public ResponseEntity<?> downloadCv(ServletRequest request, HttpServletResponse response,
            @PathVariable("id_employee") @NotBlank Long idEmployee, Authentication authentication) throws IOException {
        try {
            Boolean process = true;
            JSONObject jsonobj = new JSONObject();
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("can't acces this feature :");
                log.error("download-cv error : " + rs.toString());
                CreateLog.createJson(rs, "download-cv");
//                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }

            Employee employee = employeeService.findById(idEmployee);
            Employee entity = null;
            if (employee != null) {
                entity = employeeService.findById(employee.getIdEmployee());
                log.info("employee" + employee.getIdEmployee());
            } else {
                entity = employeeService.findById(entityEmp.getIdEmployee());
                log.info("entityEmp" + entityEmp.getIdEmployee());
            }

            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Null");
                CreateLog.createJson(rs, "download-cv");
                log.error("download-cv error : " + rs.toString());
                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (process) {
                try {
                    log.info("employee-2_" + employee.getIdEmployee());
                    log.info("entityEmp-2_" + entityEmp.getIdEmployee());
                    byte[] input_file = Files.readAllBytes(Paths.get(entity.getLinkCv()));
                    String linkDoc = new String(Base64.getEncoder().encode(input_file));
                    jsonobj.put("response_code", "00");
                    jsonobj.put("response", "success");
                    jsonobj.put("info", linkDoc);
                } catch (JSONException ex) {
                    Logger.getLogger(EmployeeController.class
                            .getName()).log(Level.SEVERE, null, ex);
                    log.error("download-cv error : " + ex.getMessage());
                    return new ResponseEntity(new CustomErrorType("55", "Error", "Employee Null"),
                            HttpStatus.NOT_FOUND);
                }

                return ResponseEntity.ok(jsonobj.toString());

            }
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse("Employee Null");
            log.error("download-cv error : " + rs.toString());
            CreateLog.createJson(rs, "download-cv");
            return new ResponseEntity(new CustomErrorType("55", "Error", "Employee Null"),
                    HttpStatus.NOT_FOUND);
        } catch (IOException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "download-cv");
//            return null;
            log.error("download-cv error : " + rs.toString());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/managed-employee/in-active", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
//    @PutMapping(value = "/approved/by-admin/{loan_id}", produces = {"application/json"})
//    @XxsFilter
    public Response setInActive(@RequestBody
            final EmployeeInactiveDto object, Authentication authentication
    ) {
        try {
            log.info("setInActive json : " + object);
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);

            Date now = new Date();
            Boolean process = true;
            LoanType typeLoan = new LoanType();

            LoanHistory entityHistory = new LoanHistory();
//        Financial dataFinance = new Financial();
//
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "set-InActive");
                log.error("set-InActive : " + rs.toString());
//                process = false;
                return rs;
            }
            if (!entityEmp.getRoleName().contentEquals("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature : " + entityEmp.getRoleName().toUpperCase());
                log.error("set-InActive : " + rs.toString());
                CreateLog.createJson(rs, "set-InActive");
                process = false;
                return rs;
            }
//             Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                log.error("set-InActive : " + rs.toString());
                CreateLog.createJson(rs, "set-InActive");
                process = false;
                return rs;

            }

            Employee employee = employeeService.findById(object.getId_employee());//findById
            if (employee == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee null, Cannot Access This feature");
                CreateLog.createJson(rs, "set-InActive");
                log.error("set-InActive : " + rs.toString());
                process = false;
                return rs;
            }
            if (employee.IsActive().equals(false) || employee.getStatus().contentEquals("i")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("employee id : " + employee.getEmployeeId() + " Status : " + "In Active");
                CreateLog.createJson(rs, "set-InActive");
                log.error("set-InActive : " + rs.toString());
                process = false;
                return rs;
            }
            if (employee.IsActive().equals(false) || employee.getStatus().contentEquals("resign")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("employee id : " + employee.getEmployeeId() + " Status : " + "Resign");
                CreateLog.createJson(rs, "set-InActive");
                log.error("set-InActive : " + rs.toString());
                process = false;
                return rs;
            }

            if (process) {
                employee.setStatus("inactive");
                employee.setIsActive(false);
                this.employeeService.update(employee);
                Employee upd_mployee = this.employeeService.update(employee);
                if (upd_mployee != null) {
                    rs.setResponse_code("01");
                    rs.setInfo("Success");
                    rs.setResponse("set Employee Id : " + employee.getEmployeeId() + "status  in Active");
                    CreateLog.createJson(rs, "set-InActive");
                    log.info("set-InActive : " + rs.toString());
                    return rs;
                }
            }
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse("Employee null, Cannot Access This feature");
            CreateLog.createJson(rs, "set-InActive");
            log.error("set-InActive : " + rs.toString());
            return rs;
        } catch (org.json.JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "set-InActive");
            CreateLog.createJson(ex.getMessage(), "set-InActive");
            log.error("set-InActive : " + rs.toString());
            return rs;

        }

    }

    @RequestMapping(value = "/managed-employee/resign", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
//    @PutMapping(value = "/approved/by-admin/{loan_id}", produces = {"application/json"})
//    @XxsFilter
    public Response setResign(@RequestBody
            final EmployeeInactiveDto object, Authentication authentication
    ) {
        try {
            log.info("setResign json : " + object);
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);

            Date now = new Date();
            Boolean process = true;
            LoanType typeLoan = new LoanType();

            LoanHistory entityHistory = new LoanHistory();
//        Financial dataFinance = new Financial();
//
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "set_Resign");
//                process = false;
                log.error("set_Resign : " + rs.toString());
                return rs;
            }
            if (!entityEmp.getRoleName().contentEquals("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature : " + entityEmp.getRoleName().toUpperCase());
                CreateLog.createJson(rs, "set_Resign");
//                process = false;
                log.error("set_Resign : " + rs.toString());
                return rs;
            }
//             Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "set_Resign");
                process = false;
                log.error("set_Resign : " + rs.toString());
                return rs;
            }

            Employee employee = employeeService.findById(object.getId_employee());//findById
            if (employee == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee null, Cannot Access This feature");
                CreateLog.createJson(rs, "set_Resign");
                process = false;
                log.error("set_Resign : " + rs.toString());
                return rs;
            }
            if (employee.IsActive().equals(false) || employee.getStatus().contentEquals("i")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("employee id : " + employee.getEmployeeId() + " Status : " + "In Active");
                CreateLog.createJson(rs, "set_Resign");
                process = false;
                log.error("set_Resign : " + rs.toString());
                return rs;
            }
            if (employee.IsActive().equals(false) || employee.getStatus().contentEquals("resign")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("employee id : " + employee.getEmployeeId() + " Status : " + "Resign");
                CreateLog.createJson(rs, "set_Resign");
                process = false;
                log.error("set_Resign : " + rs.toString());
                return rs;
            }

            if (process) {
                employee.setStatus("resign");
                employee.setIsActive(false);
                Employee upd_mployee = this.employeeService.update(employee);
                if (upd_mployee != null) {
                    rs.setResponse_code("01");
                    rs.setInfo("Success");
                    rs.setResponse("set Employee Id : " + employee.getEmployeeId() + "status Resign");
                    CreateLog.createJson(rs, "set_Resign");
                    log.info("set_Resign : " + rs.toString());
                    return rs;
                }
            }
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse("Employee null, Cannot Access This feature");
            CreateLog.createJson(rs, "set_Resign");
            log.error("set_Resign : " + rs.toString());
            return rs;
        } catch (org.json.JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "set_Resign");
            CreateLog.createJson(ex.getMessage(), "set_Resign");
            log.error("set_Resign : " + rs.toString());
            return rs;

        }

    }

    @RequestMapping(value = "/managed-employee/active", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
//    @PutMapping(value = "/approved/by-admin/{loan_id}", produces = {"application/json"})
//    @XxsFilter
    public Response setActive(@RequestBody
            final EmployeeInactiveDto object, Authentication authentication
    ) {
        try {
            String name = authentication.getName();
            log.info("setActive json : " + object);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);

            Date now = new Date();
            Boolean process = true;
            LoanType typeLoan = new LoanType();

            LoanHistory entityHistory = new LoanHistory();
//        Financial dataFinance = new Financial();
//
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "set_active");
//                process = false;
                return rs;
            }
            if (!entityEmp.getRoleName().contentEquals("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature : " + entityEmp.getRoleName().toUpperCase());
                CreateLog.createJson(rs, "set_active");
//                process = false;
                return rs;
            }
//             Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "set_active");
                process = false;

            }

            Employee employee = employeeService.findById(object.getId_employee());//findById
            if (employee == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee null, Cannot Access This feature");
                CreateLog.createJson(rs, "set_active");
                process = false;
            }
//            if (employee.IsActive().equals(false) || employee.getStatus().contentEquals("i")) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("employee id : " + employee.getEmployeeId() + " Status : " + "In Active");
//                CreateLog.createJson(rs, "set_active");
//                process = false;
//            }
            if (employee.IsActive().equals(false) || employee.getStatus().contentEquals("resign")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("employee id : " + employee.getEmployeeId() + " Status : " + "Resign");
                CreateLog.createJson(rs, "set_active");
                process = false;
            }

            if (process) {
                employee.setStatus("a");
                employee.setIsActive(true);
                Employee upd_mployee = this.employeeService.update(employee);
                if (upd_mployee != null) {
                    rs.setResponse_code("01");
                    rs.setInfo("Success");
                    rs.setResponse("set Employee Id : " + employee.getEmployeeId() + "status Active");
                    CreateLog.createJson(rs, "set_active");
                    return rs;
                }
            }
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse("Employee null, Cannot Access This feature");
            CreateLog.createJson(rs, "set_active");
            return rs;
        } catch (org.json.JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "set_active");
            CreateLog.createJson(ex.getMessage(), "set_active");
            return rs;

        }

    }
}
