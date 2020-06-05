/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.Account;
import com.lawfirm.apps.model.Employee;
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
import com.lawfirm.apps.utils.Response;
import com.lawfirm.apps.utils.Util;
import java.io.File;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author newbiecihuy
 */
@RestController
@Slf4j
@RequestMapping({"/employee"})
public class EmployeeController { //LawfirmController

    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;
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

    public EmployeeController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @PostMapping(path = "/createEmployee", consumes = {"multipart/form-data"})
    @ResponseBody
    public Response createEmployee(@RequestPart("jsonfield") final DataEmployee object, @RequestPart("doc_cv") MultipartFile file) {//@RequestParam(value = "doc_cv", required = false) MultipartFile file,
        try {

            System.out.print("isi object" + object.toString());

            String email = object.getEmail();
            String nik = object.getNik();
            String name = object.getName();
            String address = object.getAddress();
            String tax_status = object.getTax_status();
            String mobile_phone = object.getMobile_phone();
            String gender = object.getGender();
            String npwp = object.getNpwp();

            String bank_name_p = object.getBank_name_p();
            String account_number_p = object.getAccount_number_p();
            String account_name_p = object.getAccount_name_p();
            String bank_name_l = object.getBank_name_l();
            String account_number_l = object.getAccount_number_l();
            String account_name_l = object.getAccount_name_l();
            Boolean process = true;
            Boolean cheking = Util.validation(email);

            if (cheking.equals(false)) {

                rs.setResponse_code("05");
                rs.setInfo("You have entered an invalid email address :" + email);
                rs.setResponse("Create Employee Failed");

            } else {

                Employee cekEMail = employeeService.findByEmployee(email);
                System.out.println("cekEmail" + cekEMail);
                if (cekEMail != null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Email :" + email + "already registered ");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                Employee cekNik = employeeService.findByEmployee(nik);
                System.out.println("cekNik" + cekNik);
                if (cekNik != null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Nik :" + nik + "already registered ");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
//                Employee cekNPWP = employeeService.findByEmployee(npwp);
//                System.out.println("cekNPWP" + cekNPWP);
//                if (cekNPWP != null) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("NPWP :" + npwp + "already registered ");
//                    rs.setResponse("Create Employee Failed");
//                    process = false;
//                }
//                Employee entity = this.employeeService.create(object);
                if (name.length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Name maksimum 20 character");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (name.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Name can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (npwp.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field NPWP can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (address.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Address can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (nik.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Nik can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (email.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Email can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (tax_status.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Taxt Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (mobile_phone.length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Mobile Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (object.getIdem() == '1') {
                    if (object.getAccount_number_p().length() == 0) {
                        rs.setResponse_code("05");
                        rs.setInfo("Field ACCOUNT NUMBER Can't be empty");
                        rs.setResponse("Create Employee Failed");
                        process = false;
                    }
                    if (object.getAccount_name_p().length() == 0) {
                        rs.setResponse_code("05");
                        rs.setInfo("Field ACCOUNT NAME Can't be empty");
                        rs.setResponse("Create Employee Failed");
                        process = false;
                    }
                    if (object.getBank_name_p().length() == 0) {
                        rs.setResponse_code("05");
                        rs.setInfo("Field BANK NAME Can't be empty");
                        rs.setResponse("Create Employee Failed");
                        process = false;
                    }
                } else {
                    if (object.getBank_name_p().length() == 0 || object.getBank_name_l().length() == 0) {
                        rs.setResponse_code("05");
                        rs.setInfo("Field BANK NAME Can't be empty");
                        rs.setResponse("Create Employee Failed");
                        process = false;
                    }
                    if (object.getAccount_number_p().length() == 0 || object.getAccount_number_l().length() == 0) {
                        rs.setResponse_code("05");
                        rs.setInfo("Field ACCOUNT NUMBER Can't be empty");
                        rs.setResponse("Create Employee Failed");
                        process = false;
                    }
                    if (object.getAccount_name_p().length() == 0 || object.getAccount_name_l().length() == 0) {
                        rs.setResponse_code("05");
                        rs.setInfo("Field ACCOUNT NAME Can't be empty");
                        rs.setResponse("Create Employee Failed");
                        process = false;
                    }
                }

                if (email.length() > 25) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Email Maximum 25 character");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }

                if (mobile_phone.length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Mobile  Maximum 20 character");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (address.length() > 200) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Address Maximum 200 character");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }

                if (account_name_l.length() > 20 || account_name_p.length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field ACCOUNT NAME Maximum 20 character");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (bank_name_l.length() > 20 || bank_name_p.length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field BANK NAME Maximum 200 character");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (account_number_l.length() > 20 || account_number_p.length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field BANK NAME Maximum 20 character");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
//                }

                if (process) {
//                    uploadDOcument(object);
//                    return "redirect:/uploadDOcument";
                    Employee newEmployee = new Employee();
                    Account accP = new Account();
                    Account accl = new Account();

                    newEmployee.setName(name);
                    newEmployee.setMobilePhone(mobile_phone);
                    newEmployee.setAddress(address);
                    newEmployee.setNik(nik);
                    newEmployee.setEmail(email);
                    newEmployee.setNpwp(npwp);
                    newEmployee.setTaxStatus(tax_status);

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
//                        accP = accountService.create(accP);
                    newEmployee.addAccount(accP);
//                        
//                    accl.setEmployee(newEmployee);
//                    if (object.getIdem() == '1') {
//                        accl.setAccountName(account_name_p);
//                        accl.setBankName(bank_name_p);
//                        accl.setAccountNumber(account_number_p);
//                        accl.setTypeAccount("loan");
//                    } else {
                    accl.setAccountName(account_name_l);
                    accl.setBankName(bank_name_l);
                    accl.setAccountNumber(account_number_l);
                    accP.setIsActive(false);
                    accl.setTypeAccount("loan");
//                    }
//                  accl = accountService.create(accl);
                    newEmployee.addAccount(accl);
                    Employee dEmployee = employeeService.create(newEmployee);

                    log.info("isi" + dEmployee.getIdEmployee().toString());

                    if (!file.isEmpty()) {
                        saveUploadedFile(file, dEmployee, 0);
                    }
//                    }
                }
            }
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
        }
//        rs.setResponse_code("05");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");
        return rs;
    }

    @PutMapping(value = "/updateEmployee/{id_employee}", consumes = {"multipart/form-data"})
    @ResponseBody
    public Response updateEmployee(@RequestPart("jsonfield") final DataEmployee object, @RequestPart("doc_cv") MultipartFile file, @PathVariable("id_employee") Long id_employee) throws IOException {
        try {
            Employee updateEmployee = employeeService.findById(id_employee);

            if (updateEmployee == null) {
                rs.setResponse_code("05");
                rs.setInfo("Data null");
                rs.setResponse("Employe not Found");
            }
            Boolean process = true;
            Boolean cheking = false;
//
            if (object.getEmail() != null) {
                cheking = Util.validation(object.getEmail());
            }
//            

            if (cheking.equals(false)) {

                rs.setResponse_code("05");
                rs.setInfo("You have entered an invalid email address :" + object.getEmail());
                rs.setResponse("Create Employee Failed");

            } else {

                if (object.getName().length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Name maksimum 20 character");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (object.getName().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Name can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (object.getNpwp().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field NPWP can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (object.getAddress().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Address can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (object.getNik().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Nik can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (object.getEmail().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Email can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (object.getTax_status().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Taxt Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (object.getMobile_phone().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Mobile Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }

                if (object.getBank_name_p().length() == 0 || object.getBank_name_l().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field BANK NAME Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (object.getAccount_number_p().length() == 0 || object.getAccount_number_l().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field ACCOUNT NUMBER Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (object.getAccount_name_p().length() == 0 || object.getAccount_name_l().length() == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field ACCOUNT NAME Can't be empty");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }

                if (object.getEmail().length() > 25) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Email Maximum 25 character");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }

                if (object.getMobile_phone().length() > 20) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Mobile  Maximum 20 character");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (object.getAddress().length() > 200) {
                    rs.setResponse_code("05");
                    rs.setInfo("Field Address Maximum 200 character");
                    rs.setResponse("Create Employee Failed");
                    process = false;
                }
                if (object.getIdem() == '1') {
                    if (object.getAccount_name_p().length() > 20) {
                        rs.setResponse_code("05");
                        rs.setInfo("Field ACCOUNT NAME Maximum 20 character");
                        rs.setResponse("Create Employee Failed");
                        process = false;
                    }
                    if (object.getBank_name_p().length() > 20) {
                        rs.setResponse_code("05");
                        rs.setInfo("Field BANK NAME Maximum 200 character");
                        rs.setResponse("Create Employee Failed");
                        process = false;
                    }
                    if (object.getAccount_number_p().length() > 20) {
                        rs.setResponse_code("05");
                        rs.setInfo("Field Account Number Maximum 20 character");
                        rs.setResponse("Create Employee Failed");
                        process = false;
                    }
                } else {
                    if (object.getAccount_name_p().length() > 20 || object.getAccount_name_l().length() > 20) {
                        rs.setResponse_code("05");
                        rs.setInfo("Field ACCOUNT NAME Maximum 20 character");
                        rs.setResponse("Create Employee Failed");
                        process = false;
                    }
                    if (object.getBank_name_p().length() > 20 || object.getBank_name_l().length() > 20) {
                        rs.setResponse_code("05");
                        rs.setInfo("Field BANK NAME Maximum 200 character");
                        rs.setResponse("Create Employee Failed");
                        process = false;
                    }
                    if (object.getAccount_number_p().length() > 20 || object.getAccount_number_l().length() > 20) {
                        rs.setResponse_code("05");
                        rs.setInfo("Field  Account Number Maximum 20 character");
                        rs.setResponse("Create Employee Failed");
                        process = false;
                    }
                }

                if (process) {
                    List<Account> accoutList = accountService.findByEmployee(id_employee.toString());
                    System.out.println("accoutList" + accoutList.toString());
//                    Account accP = new Account();
//                    Account accl = new Account();
//
                    if (object.getName() != null) {
                        updateEmployee.setName(object.getName());
                    }
                    if (object.getAddress() != null) {
                        updateEmployee.setAddress(object.getAddress());
                    }
                    if (object.getMobile_phone() != null) {
                        updateEmployee.setMobilePhone(object.getMobile_phone());
                    }
                    if (object.getNik() != null) {
                        updateEmployee.setNik(object.getNik());
                    }
                    if (object.getNpwp() != null) {
                        updateEmployee.setNpwp(object.getNpwp());
                    }
                    if (object.getEmail() != null) {
                        updateEmployee.setEmail(object.getEmail());
                    }
                    if (object.getTax_status() != null) {
                        updateEmployee.setTaxStatus(object.getTax_status());
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

                    if (accoutList.get(0).getTypeAccount().equalsIgnoreCase("payroll")) {
                        Account upd_acc_payroll = accountService.findById(accoutList.get(0).getAccountId());
                        if (object.getAccount_name_p() == null) {
                            upd_acc_payroll.setAccountName(object.getAccount_name_p());
                        }
                        if (object.getBank_name_p() == null) {
                            upd_acc_payroll.setAccountName(object.getBank_name_p());
                        }
                        if (object.getAccount_number_p() == null) {
                            upd_acc_payroll.setAccountNumber(object.getAccount_number_p());
                            upd_acc_payroll.setIsActive(false);
                        }
                        upd_acc_payroll.setTypeAccount("payroll");

//                        updateEmployee.addAccount(upd_acc_payroll);
                        upd_acc_payroll = this.accountService.update(upd_acc_payroll);
                    }
//                        
//                    accl.setEmployee(newEmployee);

                    if (accoutList.get(0).getTypeAccount().equalsIgnoreCase("loan")) {

                        Account upd_acc_loan = accountService.findById(accoutList.get(0).getAccountId());

                        if (object.getAccount_name_l() == null) {
                            upd_acc_loan.setAccountName(object.getAccount_name_l());
                        }
                        if (object.getBank_name_l() == null) {
                            upd_acc_loan.setAccountName(object.getBank_name_l());
                        }
                        if (object.getAccount_number_l() == null) {
                            upd_acc_loan.setAccountNumber(object.getAccount_number_l());
                            upd_acc_loan.setIsActive(false);
                        }
                        upd_acc_loan.setTypeAccount("loan");
//                        updateEmployee.addAccount(upd_acc_loan);
                        upd_acc_loan = this.accountService.update(upd_acc_loan);
                    }

                    updateEmployee = employeeService.update(updateEmployee);

                    log.info("isi" + updateEmployee.getIdEmployee().toString());

                    if (!file.isEmpty()) {
                        saveUploadedFile(file, updateEmployee, 1);
                    } else {
                        rs.setResponse_code("01");
                        rs.setInfo("Success");
                        rs.setResponse("Update Employee Succes");
                    }
                }
            }
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
        }
//        rs.setResponse_code("05");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");
        return rs;
    }

    public Response saveUploadedFile(MultipartFile file, Employee object, int s) throws IOException {
        try {
//            String link_cv = null;s
            String nama = object.getName().replaceAll("\\s", "").toLowerCase();
//            System.out.println("nama" + nama);
            System.out.println("isi_file : " + file);
            String pathDoc = null;

            Employee entity = employeeService.findById(object.getIdEmployee());
            pathDoc = basepathUpload + "/" + "employee" + "/" + entity.getIdEmployee() + "/";
//            pathDoc = basepathUpload + "\\" + "employee" + "\\" + entity.getIdEmployee() + "\\";
            /*-----------------------------------------------*/
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

            if (!file.isEmpty()) {

                byte[] bytes = file.getBytes();
                Path path = Paths.get(pathDoc + file.getOriginalFilename());
                Files.write(path, bytes);
                entity.setLinkCv(pathDoc + file.getOriginalFilename());
                employeeService.update(entity);
                if (s == 0) {
                    rs.setResponse_code("01");
                    rs.setInfo("Success");
                    rs.setResponse("Create Employee Succes");
                }
                if (s == 1) {
                    rs.setResponse_code("01");
                    rs.setInfo("Success");
                    rs.setResponse("Update Employee Succes");
                }
            } else {
                rs.setResponse_code("05");
                rs.setInfo("Data null");
                rs.setResponse("Create Employee Failed");
            }

//            return demployee;
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
//        return null;
        return rs;
    }

    @PermitAll
    @PutMapping(value = "/updateAccount/{id_employee}", produces = {"application/json"})
    public Response updateAccount(@RequestBody final DataEmployee object, @PathVariable("id_employee") Long id_employee) {
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
            if (accoutList.get(0).getTypeAccount().equalsIgnoreCase("payroll")) {
                Account upd_acc_payroll = accountService.findById(accoutList.get(0).getAccountId());
                if (object.getAccount_name_p() == null) {
                    upd_acc_payroll.setAccountName(object.getAccount_name_p());
                }
                if (object.getBank_name_p() == null) {
                    upd_acc_payroll.setAccountName(object.getBank_name_p());
                }
                if (object.getAccount_number_p() == null) {
                    upd_acc_payroll.setAccountNumber(object.getAccount_number_p());
                    upd_acc_payroll.setIsActive(false);
                }
                upd_acc_payroll.setTypeAccount("payroll");

                updateEmployee.addAccount(upd_acc_payroll);
            }
//                        
//                    accl.setEmployee(newEmployee);

            if (accoutList.get(0).getTypeAccount().equalsIgnoreCase("loan")) {

                Account upd_acc_loan = accountService.findById(accoutList.get(0).getAccountId());

                if (object.getAccount_name_l() == null) {
                    upd_acc_loan.setAccountName(object.getAccount_name_l());
                }
                if (object.getBank_name_l() == null) {
                    upd_acc_loan.setAccountName(object.getBank_name_l());
                }
                if (object.getAccount_number_l() == null) {
                    upd_acc_loan.setAccountNumber(object.getAccount_number_l());
                    upd_acc_loan.setIsActive(false);
                }
                upd_acc_loan.setTypeAccount("loan");
                updateEmployee.addAccount(upd_acc_loan);
            }
        }
        return rs;
    }

    @PermitAll
    @PutMapping(value = "/approved/{id_employee}", produces = {"application/json"})
    public Response approved(@RequestBody final AprovedApi object, @PathVariable("id_employee") Long id_employee) {
        this.now = new Date();
        this.date_now = timeFormat.format(now);
        Employee dataAdmin = employeeService.findById(object.getId_employee_admin());
        if (!dataAdmin.getRoleName().equalsIgnoreCase("admin")) {
            rs.setResponse_code("05");
            rs.setInfo("Warning");
            rs.setResponse("Your Role can't acces this feature");
        }
        Employee dataEmployee = employeeService.findById(id_employee);
        if (dataEmployee == null) {
            rs.setResponse_code("05");
            rs.setInfo("Warning");
            rs.setResponse("Employee not Found");
            return rs;
        }

        if (dataEmployee.getIsActive() == true) {
            rs.setResponse_code("05");
            rs.setInfo("Warning");
            rs.setResponse("Employee status already approved");
        }
        Employee cekEmployeeId = employeeService.findByEmployee(object.getEmployee_id());

        if (cekEmployeeId != null) {
            rs.setResponse_code("05");
            rs.setInfo("Warning");
            rs.setResponse("Employee Id : " + object.getEmployee_id() + " Already Registered at " + cekEmployeeId.getNik() + "-" + cekEmployeeId.getName());
            return rs;
        }
        dataEmployee.setSalary(object.getSalary());
        dataEmployee.setLoanAmount(object.getLoan_amount());
        dataEmployee.setApprovedBy(dataAdmin.getName());
        dataEmployee.setEmployeeId(object.getEmployee_id());
        dataEmployee.setRoleName(object.getRole_name());
        dataEmployee.setAprroved_date(now);
        dataEmployee.setIsActive(Boolean.TRUE);

        Employee approovedEmployee = employeeService.approved(dataEmployee);

        if (approovedEmployee != null) {
            List<Account> listAccount = accountService.findByEmployee(id_employee.toString());
//            Account upd_account = new Account();
            for (int i = 0; i < listAccount.size(); i++) {
                Account upd_account = accountService.findById(listAccount.get(i).getAccountId());
                if (listAccount.get(i).getTypeAccount().contentEquals("loan")) {
                    upd_account.setAccountNumberFinance(listAccount.get(i).getAccountNumber());
                    upd_account.setAccountNumber("");
                    upd_account.setIsActive(true);
                }
                if (listAccount.get(i).getTypeAccount().contentEquals("payroll")) {
                    upd_account.setAccountNumberFinance(listAccount.get(i).getAccountNumber());
                    upd_account.setAccountNumber("");
                    upd_account.setIsActive(true);
                }
                upd_account = accountService.update(upd_account);

                if (upd_account != null) {
                    rs.setResponse_code("01");
                    rs.setInfo("Succes");
                    rs.setResponse("Employee approved By : " + dataAdmin.getName());
                } else {
                    rs.setResponse_code("05");
                    rs.setInfo("Warning");
                    rs.setResponse("Employee Null");
                }
            }
        }
// 
        return rs;
    }

    @PermitAll
    @DeleteMapping(value = "/deleteEmployee/{id_employee}", produces = {"application/json"})//@PutMapping
    public Response deleteEmployee(@PathVariable("id_employee") Long idEmployee) {
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
        } else {
            rs.setResponse_code("05");
            rs.setInfo("failed");
            rs.setResponse("Employee Null");
        }
        return rs;
    }

    @PermitAll
    @RequestMapping(value = "/viewEmployeeByAdmin", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<String> viewEmployeeByAdmin() {
        try {
            int max = employeeService.count();
            int start = 0;
            List<Employee> entityList = null;
            if (start == 0) {
                entityList = this.employeeService.listEmployee();
            } else {
                entityList = this.employeeService.listEmployeePaging(max, start);
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
                if (entity.getTaxStatus() == null) {
                    jsonobj.put("tax_status", "");
                } else {
                    jsonobj.put("tax_status", entity.getTaxStatus());
                }
                if (entity.getIsActive() == true) {
                    jsonobj.put("is_active", entity.getIsActive());
                } else {
                    jsonobj.put("is_active", entity.getTaxStatus());
                }
                if (entity.getIsDelete() == false) {
                    jsonobj.put("is_delete", entity.getIsActive());
                } else {
                    jsonobj.put("is_delete", entity.getTaxStatus());
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
                        }
                    }
                }
                array.put(jsonobj);
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());;
            return new ResponseEntity(new CustomErrorType(ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @PermitAll
    @RequestMapping(value = "/viewEmployeeByFinance", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<String> viewEmployeeByFinance() {
        try {
            int max = employeeService.count();
            int start = 0;
            List<Employee> entityList = null;
            if (start == 0) {
                entityList = this.employeeService.listEmployee();
            } else {
                entityList = this.employeeService.listEmployeePaging(max, start);
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
                if (entity.getTaxStatus() == null) {
                    jsonobj.put("tax_status", "");
                } else {
                    jsonobj.put("tax_status", entity.getTaxStatus());
                }
                if (entity.getIsActive() == true) {
                    jsonobj.put("is_active", entity.getIsActive());
                } else {
                    jsonobj.put("is_active", entity.getTaxStatus());
                }
                if (entity.getIsDelete() == false) {
                    jsonobj.put("is_delete", entity.getIsActive());
                } else {
                    jsonobj.put("is_delete", entity.getTaxStatus());
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
                                jsonobj.put("account_number_p", enAccount.getAccountNumber());
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
                                jsonobj.put("account_number_l", enAccount.getAccountNumber());
                            }
                        }
                    }
                }
                array.put(jsonobj);
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType(ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

//    @PermitAll
//    @PutMapping(value = "/approvedByFinance/{id_employee}", produces = {"application/json"})
//    public Response approvedByFinance(@RequestBody
//            final AprovedApi object,
//            @PathVariable("id_employee") Long id_employee
//    ) {
//        Boolean process = true;
//        this.now = new Date();
//        this.date_now = timeFormat.format(now);
//        Employee dataFinance = employeeService.findById(object.getId_employee_finance());
//        if (dataFinance == null) {
//            rs.setResponse_code("05");
//            rs.setInfo("failed");
//            rs.setResponse("can't acces this feature");
//            process = false;
//        }
//        if (!dataFinance.getRoleName().equalsIgnoreCase("finance")) {
//            rs.setResponse_code("05");
//            rs.setInfo("failed");
//            rs.setResponse("Your Role can't acces this feature");
//            process = false;
//        }
//        Employee dataemployee = employeeService.findById(id_employee);
//        if (dataemployee == null) {
//            rs.setResponse_code("05");
//            rs.setInfo("failed");
//            rs.setResponse("Employee not Found");
//            process = false;
//        }
//        if (process) {
//            List<Account> listAccount = accountService.findByEmployee(id_employee.toString());
////            Account upd_account = new Account();
//            for (int i = 0; i <= listAccount.size(); i++) {
//                Account upd_account = accountService.findById(listAccount.get(i).getAccountId());
//                if (listAccount.get(i).getTypeAccount().contentEquals("loan")) {
//                    upd_account.setAccountNumberFinance(listAccount.get(i).getAccountNumber());
//                    upd_account.setAccountNumber("");
//                    upd_account.setIsActive(true);
//                }
//                if (listAccount.get(i).getTypeAccount().contentEquals("payroll")) {
//                    upd_account.setAccountNumberFinance(listAccount.get(i).getAccountNumber());
//                    upd_account.setAccountNumber("");
//                    upd_account.setIsActive(true);
//                }
//                upd_account = accountService.update(upd_account);
//
//                if (upd_account != null) {
//                    rs.setResponse_code("01");
//                    rs.setInfo("Success");
//                    rs.setResponse("approved by Finance");
//                }
//            }
////            else {
//            rs.setResponse_code("05");
//            rs.setInfo("failed");
//            rs.setResponse("can't acces this feature");
////                } 
//        }
//        return rs;
//    }
}
