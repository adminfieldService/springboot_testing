/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.model.Reimbursement;
import com.lawfirm.apps.model.ReimbursementHistory;
import com.lawfirm.apps.response.Response;
import com.lawfirm.apps.service.CaseDocumentService;
import com.lawfirm.apps.service.interfaces.AccountServiceIface;
import com.lawfirm.apps.service.interfaces.CaseDetailsServiceIface;
import com.lawfirm.apps.service.interfaces.ClientDataServiceIface;
import com.lawfirm.apps.service.interfaces.DocumentReimburseServiceIface;
import com.lawfirm.apps.service.interfaces.EmployeeRoleServiceIface;
import com.lawfirm.apps.service.interfaces.EmployeeServiceIface;
import com.lawfirm.apps.service.interfaces.EngagementServiceIface;
import com.lawfirm.apps.service.interfaces.EventServiceIface;
import com.lawfirm.apps.service.interfaces.FinancialServiceIface;
import com.lawfirm.apps.service.interfaces.LoanServiceIface;
import com.lawfirm.apps.service.interfaces.LoanTypeServiceIface;
import com.lawfirm.apps.service.interfaces.MemberServiceIface;
import com.lawfirm.apps.service.interfaces.ProfessionalServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementHistoryServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import com.lawfirm.apps.support.api.AapprovalReimbursementDto;
import com.lawfirm.apps.utils.CreateLog;
import com.lawfirm.apps.utils.CustomErrorType;
import com.lawfirm.apps.utils.Util;
import com.xss.filter.annotation.XxsFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author newbiecihuy
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
//@RequestMapping({"/reimbursement"})//pengembalian
public class ReimbursementController {

    static String basepathUpload = "/opt/lawfirm/UploadFile/";
    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;
    SimpleDateFormat sdfYear;
    SimpleDateFormat sdfMonth;
    SimpleDateFormat sdfMY;
    Date now;
    String date_now;
    String notif = null;
    String jsonString = null;

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
    ProfessionalServiceIface professionalService;
    @Autowired
    ReimbursementServiceIface reimbursementService;
    @Autowired
    ReimbursementHistoryServiceIface reimbursementHistoryService;
    @Autowired
    TeamMemberServiceIface teamMemberService;
    @Autowired
    MemberServiceIface memberServiceIface;
    @Autowired
    EventServiceIface eventServiceIface;

    public ReimbursementController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yy");
        this.sdfMonth = new SimpleDateFormat("MM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
    }

    @RequestMapping(value = "/reimbursement/{id_loan}", method = RequestMethod.POST, consumes = {"multipart/form-data"}, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}
    @XxsFilter
//     public Response createReimburse(@RequestParam ReimbursementApi object, @RequestPart("attach") MultipartFile file, @PathVariable("id_loan") Long id_loan, Authentication authentication) {
    public Response createReimburse(
            @PathVariable("id_loan") Long id_loan,
            @RequestParam("case_id") String case_id,
            @RequestParam("expense_date") String expense_date,
            @RequestParam("amount") Double amount,
            @RequestParam("note") String note,
            @RequestPart("attach") MultipartFile file, Authentication authentication) throws IOException {

        try {
            Date now = new Date();
            String nama = authentication.getName();
            Boolean process = true;
            Date expense_date_value = null;
            log.info("object value : " + case_id + ": " + expense_date + ": " + amount + ": " + note);
            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }
//            Employee dataEmp = employeeService.findById(id_employee_admin); 
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());

            log.info("dataEmp : " + dataEmp.getRoleName());
            if (dataEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }
            if (!dataEmp.getRoleName().matches("dmp")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }
//            CaseDetails entity = caseDetailsService.findCaseId(case_id);
//            if (entity == null) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("Case ID : " + case_id + " not Found");
//                CreateLog.createJson(rs, "create-reimburse");
//                process = false;
//                return rs;
//            }
            CaseDetails findByCaseID = engagementService.findByCaseID(case_id);
            if (findByCaseID == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Case Id : " + case_id + " not Found");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }
            if (findByCaseID.getStatus().contains("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Case Id : " + case_id + " Rejected ");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }

            Loan entityLoan = loanService.findByIdLoan(id_loan);
            if (entityLoan == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature, Loan type : " + entityLoan.getLoantype().getTypeLoan());
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }
            if (entityLoan.getStatus().contentEquals("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature,  Loan status : " + "Rejected");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }
            if (entityLoan.getStatus().contentEquals("s")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature,  Loan Must : " + "Approved");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }
            if (entityLoan.getLoantype().getTypeLoan().contains("a")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("id Loan : " + id_loan + " not Found");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }
            String pathDoc = basepathUpload + "reimbursement" + "/" + entityLoan.getLoanId() + "/";
            String fileDownloadUri = null;
            String fileName = null;
            if (process) {
                expense_date_value = dateFormat.parse(expense_date);
                ReimbursementHistory history = new ReimbursementHistory();
                Reimbursement dataReimbursement = new Reimbursement();
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
                }
                byte[] bytes = file.getBytes();
                fileName = file.getOriginalFilename().replaceAll(" ", "");
                Path path = Paths.get(pathDoc + file.getOriginalFilename().replaceAll(" ", ""));
                Files.write(path, bytes);
                log.info("file getOriginalFilename : " + file.getOriginalFilename().replaceAll(" ", ""));
                dataReimbursement.setLinkDocument(pathDoc + file.getOriginalFilename().replaceAll(" ", ""));
                fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(pathDoc + file.getOriginalFilename().replaceAll(" ", ""))
                        .path(fileName)
                        .toUriString();
                dataReimbursement.setReimburseAmount(amount);
                dataReimbursement.setNote(note);
                dataReimbursement.setStatus("s");
                dataReimbursement.setLoan(entityLoan);
                dataReimbursement.setReimbursementId(Util.changeBCS(entityLoan.getLoanId()));
                dataReimbursement.setExpenseDate(expense_date_value);
                dataReimbursement.setEmployee(entityEmp);
                history.setReimbursement(dataReimbursement);
                history.setTgl_input(now);
                history.setResponse("submited by : " + entityEmp.getEmployeeId());
                history.setUserId(entityEmp.getIdEmployee());
                Reimbursement create = this.reimbursementService.create(dataReimbursement);
                if (create != null) {
                    this.reimbursementHistoryService.create(history);
                    rs.setResponse_code("00");
                    rs.setInfo("Success");
                    rs.setResponse("Create Reimburse Success");
                    CreateLog.createJson(rs, "create-reimburse");

                    return rs;
                }

            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("id Loan : " + id_loan + " not Found");
                CreateLog.createJson(rs, "create-reimburse");
                return rs;
            }

        } catch (Exception ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "create-reimburse");
        }
//        rs.setResponse_code("55");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");
        return rs;
    }

    @RequestMapping(value = "/reimbursements", method = RequestMethod.GET, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}
    @XxsFilter
    public ResponseEntity<String> listReimbursement(Authentication authentication) {
        try {
            String nama = authentication.getName();
            Boolean process = true;
            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "listReimbursement");
                process = false;
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
                CreateLog.createJson(rs, "listReimbursement");
                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            List<Reimbursement> reimbursementList = reimbursementService.listReimbursement();
            log.info("listReimbursement.size() : " + reimbursementList.size());
            JSONArray array = new JSONArray();
            for (int i = 0; i < reimbursementList.size(); i++) {
                JSONObject obj = new JSONObject();
                Reimbursement reimbursement = reimbursementList.get(i);

                if (reimbursement.getReimburseId() == null) {
                    obj.put("reimburse_id", "");
                } else {
                    obj.put("reimburse_id", reimbursement.getReimburseId());
                }
                if (reimbursement.getReimburseId() == null) {
                    obj.put("reimbursement_id", "");
                } else {
                    obj.put("reimbursement_id", reimbursement.getReimbursementId());
                }
                if (reimbursement.getExpenseDate() == null) {
                    obj.put("expense_date", "");
                } else {
                    obj.put("expense_date", dateFormat.format(reimbursement.getExpenseDate()));
                }
                if (reimbursement.getEmployee() == null) {
                    obj.put("employee_id", "");
                    obj.put("name", "");
                } else {
                    obj.put("employee_id", reimbursement.getEmployee().getEmployeeId());
                    obj.put("name", reimbursement.getEmployee().getName());
                }
                if (reimbursement.getLoan() == null) {
                    obj.put("loan_id", "");
                    obj.put("loan_type", "");
                    obj.put("case_id", "");
                } else {
                    obj.put("loan_id", reimbursement.getLoan().getLoanId());
                    obj.put("loan_type", "b");
                    obj.put("case_id", reimbursement.getLoan().getEngagement().getCaseID());
                }
                if (reimbursement.getNote() == null) {
                    obj.put("note", "");
                } else {
                    obj.put("note", reimbursement.getNote());
                }
                if (reimbursement.getReimburseAmount() == null) {
                    obj.put("reimburse_amount", "");
                } else {
                    obj.put("reimburse_amount", String.format("%.0f", reimbursement.getReimburseAmount()));
                }
                if (reimbursement.getApprovedBy() == null) {
                    obj.put("approved_by", "");
                } else {
                    Employee dataAdmin = this.employeeService.findById(reimbursement.getApprovedBy());
                    obj.put("approved_by", dataAdmin.getEmployeeId());
                }
                if (reimbursement.getApprovedDate() == null) {
                    obj.put("approval_date", "");
                } else {
                    obj.put("approval_date", dateFormat.format(reimbursement.getApprovedDate()));
                }
                if (reimbursement.getReimbursedBy() == null) {
                    obj.put("reimburse_by", "");
                } else {
                    Employee dataFinance = this.employeeService.findById(reimbursement.getReimbursedBy());
                    obj.put("reimburse_by", dataFinance.getEmployeeId());
                }
                if (reimbursement.getReimbursedDate() == null) {
                    obj.put("reimbursed_date", "");
                } else {
                    obj.put("reimbursed_date", dateFormat.format(reimbursement.getReimbursedDate()));
                }
                if (reimbursement.getApprovedAmount() == null) {
                    obj.put("approved_amount", "");
                } else {
                    obj.put("approved_amount", String.format("%.0f", reimbursement.getApprovedAmount()));
                }
                if (reimbursement.getStatus() == null) {
                    obj.put("status", "");
                } else {
                    obj.put("status", reimbursement.getStatus());
                }
                if (reimbursement.getLinkDocument() == null) {
                    obj.put("link_document", "");
                } else {
                    obj.put("link_document", reimbursement.getLinkDocument());
                }
                array.put(obj);
            }
            return ResponseEntity.ok(array.toString());

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "listReimbursement");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/reimbursement/{reimburse_id}/find-by-id", method = RequestMethod.POST, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}
    @XxsFilter
    public ResponseEntity<?> reimbursementFindById(@PathVariable("reimburse_id") Long reimburse_id, Authentication authentication) {
        try {
            Date now = new Date();
            String nama = authentication.getName();
            Boolean process = true;
            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "reimbursementFindById");
                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());
            log.info("dataEmp : " + dataEmp.getRoleName());
            if (dataEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "reimbursementFindById");
                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (process) {
                Reimbursement reimbursement = reimbursementService.findById(reimburse_id);
                JSONObject obj = new JSONObject();
                if (reimbursement != null) {

                    if (reimbursement.getReimburseId() == null) {
                        obj.put("reimburse_id", "");
                    } else {
                        obj.put("reimburse_id", reimbursement.getReimburseId());
                    }
                    if (reimbursement.getReimburseId() == null) {
                        obj.put("reimbursement_id", "");
                    } else {
                        obj.put("reimbursement_id", reimbursement.getReimbursementId());
                    }
                    if (reimbursement.getExpenseDate() == null) {
                        obj.put("expense_date", "");
                    } else {
                        obj.put("expense_date", dateFormat.format(reimbursement.getExpenseDate()));
                    }
                    if (reimbursement.getEmployee() == null) {
                        obj.put("employee_id", "");
                        obj.put("name", "");
                    } else {
                        obj.put("employee_id", reimbursement.getEmployee().getEmployeeId());
                        obj.put("name", reimbursement.getEmployee().getName());
                    }
                    if (reimbursement.getLoan() == null) {
                        obj.put("loan_id", "");
                        obj.put("loan_type", "");
                        obj.put("case_id", "");
                    } else {
                        obj.put("loan_id", reimbursement.getLoan().getLoanId());
                        obj.put("loan_type", "b");
                        obj.put("case_id", reimbursement.getLoan().getEngagement().getCaseID());
                    }
                    if (reimbursement.getNote() == null) {
                        obj.put("note", "");
                    } else {
                        obj.put("note", reimbursement.getNote());
                    }
                    if (reimbursement.getReimburseAmount() == null) {
                        obj.put("reimburse_amount", "");
                    } else {
                        obj.put("reimburse_amount", String.format("%.0f", reimbursement.getReimburseAmount()));
                    }
                    if (reimbursement.getApprovedBy() == null) {
                        obj.put("approved_by", "");
                    } else {
                        Employee dataAdmin = this.employeeService.findById(reimbursement.getApprovedBy());
                        obj.put("approved_by", dataAdmin.getEmployeeId());
                    }
                    if (reimbursement.getApprovedDate() == null) {
                        obj.put("approval_date", "");
                    } else {
                        obj.put("approval_date", dateFormat.format(reimbursement.getApprovedDate()));
                    }
                    if (reimbursement.getReimbursedBy() == null) {
                        obj.put("reimburse_by", "");
                    } else {
                        Employee dataFinance = this.employeeService.findById(reimbursement.getReimbursedBy());
                        obj.put("reimburse_by", dataFinance.getEmployeeId());
                    }
                    if (reimbursement.getReimbursedDate() == null) {
                        obj.put("reimbursed_date", "");
                    } else {
                        obj.put("reimbursed_date", dateFormat.format(reimbursement.getReimbursedDate()));
                    }
                    if (reimbursement.getApprovedAmount() == null) {
                        obj.put("approved_amount", "");
                    } else {
                        obj.put("approved_amount", String.format("%.0f", reimbursement.getApprovedAmount()));
                    }
                    if (reimbursement.getStatus() == null) {
                        obj.put("status", "");
                    } else {
                        obj.put("status", reimbursement.getStatus());
                    }
                    if (reimbursement.getLinkDocument() == null) {
                        obj.put("link_document", "");
                    } else {
                        obj.put("link_document", reimbursement.getLinkDocument());
                    }
                }
                return ResponseEntity.ok(obj.toString());
            }

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "reimbursementFindById");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/reimbursement/{reimburse_id}/approval", method = RequestMethod.POST, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}
    @XxsFilter
    public Response approvalReimbursement(@RequestBody AapprovalReimbursementDto object, @PathVariable("reimburse_id") Long reimburse_id, Authentication authentication) {
        try {
            Date now = new Date();
            String nama = authentication.getName();
            Boolean process = true;
            Date expense_date_value = null;
            log.info("object value : " + object.toString());

            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "approve-reimbursement");
                process = false;
                return rs;
            }
//            Employee dataEmp = employeeService.findById(id_employee_admin); 
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());
            log.info("dataEmp : " + dataEmp.getRoleName());
            if (dataEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "approval-reimbursement");
                process = false;
                return rs;
            }
            if (!dataEmp.getRoleName().matches("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "approval-reimbursement");
                process = false;
                return rs;
            }
            Reimbursement dataReimbursement = reimbursementService.findById(reimburse_id);
            if (dataReimbursement == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + reimburse_id + " not Found");
                CreateLog.createJson(rs, "approval-reimbursement");
                process = false;
                return rs;
            }
            if (dataReimbursement.getStatus().contains("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + reimburse_id + " Rejected by admin");
                CreateLog.createJson(rs, "approval-reimbursement");
                process = false;
                return rs;
            }
            if (dataReimbursement.getStatus().contains("reimburse")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + reimburse_id + " already reimburse");
                CreateLog.createJson(rs, "approval-reimbursement");
                process = false;
                return rs;
            }
            if (dataReimbursement.getStatus().contains("a")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + reimburse_id + " already Approve ");
                CreateLog.createJson(rs, "approval-reimbursement");
                process = false;
                return rs;
            }
            if (process) {
                ReimbursementHistory history = new ReimbursementHistory();
//                if (object.getDecision().contentEquals("r")) {
//                    dataReimbursement.setApprovedBy(dataEmp.getIdEmployee());
//                    dataReimbursement.setStatus("r");
//                    history.setReimbursement(dataReimbursement);
//                    history.setResponse("reject by : " + dataEmp.getEmployeeId());
//                }
//                if (object.getDecision().contentEquals("a")) {
                dataReimbursement.setApprovedBy(dataEmp.getIdEmployee());
                dataReimbursement.setStatus("a");
                dataReimbursement.setApprovedDate(now);
                dataReimbursement.setApprovedAmount(object.getApproved_amount());

                history.setResponse("approved by : " + dataEmp.getEmployeeId());
                history.setUserId(dataEmp.getIdEmployee());
                Reimbursement updateReimbursement = reimbursementService.update(dataReimbursement);
                if (updateReimbursement != null) {
                    history.setReimbursement(updateReimbursement);
                    this.reimbursementHistoryService.create(history);
                    rs.setResponse_code("00");
                    rs.setInfo("Success");
                    rs.setResponse("data reimbursement approved by admin " + entityEmp.getEmployeeId());
                    return rs;
                }

//                }
            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("data reimbursement not Found");
                CreateLog.createJson(rs, "approval-reimbursement");
                return rs;
            }
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse("data id : " + reimburse_id + " not Found");
            CreateLog.createJson(rs, "reimbursement");
            return rs;
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "reimbursement");
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "approval-reimbursement");
            return rs;
        }
//        rs.setResponse_code("55");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");
//        return rs;
    }

    @RequestMapping(value = "/reimbursement/{reimburse_id}", method = RequestMethod.POST, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}
    @XxsFilter
    public Response reimbursement(@PathVariable("reimburse_id") Long reimburse_id, Authentication authentication) {//@RequestBody ReimbursementApi object, 
        try {
            Date now = new Date();
            String nama = authentication.getName();
            Boolean process = true;
            Date expense_date_value = null;
//            log.info("object value : " + object.toString());

            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "reimbursement");
                process = false;
                return rs;
            }
//            Employee dataEmp = employeeService.findById(id_employee_admin); 
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());
            log.info("dataEmp : " + dataEmp.getRoleName());
            if (dataEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "reimbursement");
                process = false;
                return rs;
            }
            if (!dataEmp.getRoleName().matches("finance")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "reimbursement");
                process = false;
                return rs;
            }
            Reimbursement dataReimbursement = this.reimbursementService.findById(reimburse_id);
            if (dataReimbursement == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + reimburse_id + " not Found");
                CreateLog.createJson(rs, "reimbursement");
                process = false;
                return rs;
            }
            if (dataReimbursement.getStatus().contains("reimburse")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + reimburse_id + " already Reimburse ");
                CreateLog.createJson(rs, "reimbursement");
                process = false;
                return rs;
            }
            if (dataReimbursement.getStatus().contains("s")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + reimburse_id + " must approve by admin");
                CreateLog.createJson(rs, "reimbursement");
                process = false;
                return rs;
            }
            if (process) {
                ReimbursementHistory history = new ReimbursementHistory();
//                if (object.getDecision().contentEquals("reimburse")) {
                dataReimbursement.setReimbursedBy(dataEmp.getIdEmployee());
                dataReimbursement.setStatus("reimburse");
                dataReimbursement.setReimbursedDate(now);

                history.setResponse("reimburse by : " + dataEmp.getEmployeeId());
                history.setUserId(dataEmp.getIdEmployee());
                Reimbursement updateReimbursement = reimbursementService.update(dataReimbursement);
                if (updateReimbursement != null) {
                    history.setReimbursement(updateReimbursement);
                    this.reimbursementHistoryService.create(history);
                    rs.setResponse_code("00");
                    rs.setInfo("Success");
                    rs.setResponse("data reimbursement reimburse by : " + entityEmp.getEmployeeId());
                    return rs;
                }
//                }
            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + reimburse_id + " not Found");
                CreateLog.createJson(rs, "reimbursement");
                return rs;
            }
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse("Cannot Access This feature");
            CreateLog.createJson(rs, "reimbursement");
            return rs;
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "reimbursement");
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "reimbursement");
            return rs;
        }

    }

    @RequestMapping(value = "/reimbursements/finance", method = RequestMethod.GET, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}
    @XxsFilter
    public ResponseEntity<String> listReimbursementFinace(Authentication authentication) {
        try {
            String nama = authentication.getName();
            Boolean process = true;
            Date expense_date_value = null;

            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "listReimbursementFinace");
                process = false;
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
                CreateLog.createJson(rs, "listReimbursementFinace");
                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!dataEmp.getRoleName().contains("finance")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("your Role : " + dataEmp.getRoleName() + " Cannot Access This feature");
                CreateLog.createJson(rs, "listReimbursementFinace");
                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            List<Reimbursement> listReimbursement = this.reimbursementService.listBy("finance", dataEmp.getIdEmployee());
            JSONArray array = new JSONArray();
            for (int i = 0; i < listReimbursement.size(); i++) {
                JSONObject obj = new JSONObject();
                Reimbursement reimbursement = (Reimbursement) listReimbursement.get(i);

                if (reimbursement.getReimburseId() == null) {
                    obj.put("reimburse_id", "");
                } else {
                    obj.put("reimburse_id", reimbursement.getReimburseId());
                }
                if (reimbursement.getReimburseId() == null) {
                    obj.put("reimbursement_id", "");
                } else {
                    obj.put("reimbursement_id", reimbursement.getReimbursementId());
                }
                if (reimbursement.getExpenseDate() == null) {
                    obj.put("expense_date", "");
                } else {
                    obj.put("expense_date", dateFormat.format(reimbursement.getExpenseDate()));
                }
                if (reimbursement.getEmployee() == null) {
                    obj.put("employee_id", "");
                    obj.put("name", "");
                } else {

                    obj.put("employee_id", reimbursement.getEmployee().getEmployeeId());
                    obj.put("name", reimbursement.getEmployee().getName());
                }
                if (reimbursement.getLoan() == null) {
                    obj.put("loan_id", "");
                    obj.put("loan_type", "");
                    obj.put("case_id", "");
                } else {
                    obj.put("loan_id", reimbursement.getLoan().getLoanId());
                    obj.put("loan_type", "b");
                    obj.put("case_id", reimbursement.getLoan().getEngagement().getCaseID());
                }
                if (reimbursement.getNote() == null) {
                    obj.put("note", "");
                } else {
                    obj.put("note", reimbursement.getNote());
                }
                if (reimbursement.getReimburseAmount() == null) {
                    obj.put("reimburse_amount", "");
                } else {
                    obj.put("reimburse_amount", String.format("%.0f", reimbursement.getReimburseAmount()));
                }
                if (reimbursement.getApprovedBy() == null) {
                    obj.put("approved_by", "");
                } else {
                    Employee dataAdmin = this.employeeService.findById(reimbursement.getApprovedBy());
                    obj.put("approved_by", dataAdmin.getEmployeeId());
                }
                if (reimbursement.getApprovedDate() == null) {
                    obj.put("approval_date", "");
                } else {
                    obj.put("approval_date", dateFormat.format(reimbursement.getApprovedDate()));
                }
                if (reimbursement.getReimbursedBy() == null) {
                    obj.put("reimburse_by", "");
                } else {
                    Employee dataFinance = this.employeeService.findById(reimbursement.getReimbursedBy());
                    obj.put("reimburse_by", dataFinance.getEmployeeId());
                }
                if (reimbursement.getReimbursedDate() == null) {
                    obj.put("reimbursed_date", "");
                } else {
                    obj.put("reimbursed_date", dateFormat.format(reimbursement.getReimbursedDate()));
                }
                if (reimbursement.getApprovedAmount() == null) {
                    obj.put("approved_amount", "");
                } else {
                    obj.put("approved_amount", String.format("%.0f", reimbursement.getApprovedAmount()));
                }
                if (reimbursement.getStatus() == null) {
                    obj.put("status", "");
                } else {
                    obj.put("status", reimbursement.getStatus());
                }
                if (reimbursement.getLinkDocument() == null) {
                    obj.put("link_document", "");
                } else {
                    obj.put("link_document", reimbursement.getLinkDocument());
                }
                array.put(obj);
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "listReimbursementFinace");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/reimbursements/admin", method = RequestMethod.GET, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}
    @XxsFilter
    public ResponseEntity<String> listReimbursementAdmin(Authentication authentication) {
        try {
            String nama = authentication.getName();
            Boolean process = true;
            Date expense_date_value = null;

            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "listReimbursementAdmin");
                process = false;
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
                CreateLog.createJson(rs, "listReimbursementAdmin");
                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!dataEmp.getRoleName().contains("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("your Role : " + dataEmp.getRoleName() + " Cannot Access This feature");
                CreateLog.createJson(rs, "listReimbursementAdmin");
                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            List<Reimbursement> listReimbursement = this.reimbursementService.listBy("admin", dataEmp.getIdEmployee());
            JSONArray array = new JSONArray();
            for (int i = 0; i < listReimbursement.size(); i++) {
                JSONObject obj = new JSONObject();
                Reimbursement reimbursement = (Reimbursement) listReimbursement.get(i);

                if (reimbursement.getReimburseId() == null) {
                    obj.put("reimburse_id", "");
                } else {
                    obj.put("reimburse_id", reimbursement.getReimburseId());
                }
                if (reimbursement.getReimburseId() == null) {
                    obj.put("reimbursement_id", "");
                } else {
                    obj.put("reimbursement_id", reimbursement.getReimbursementId());
                }
                if (reimbursement.getExpenseDate() == null) {
                    obj.put("expense_date", "");
                } else {
                    obj.put("expense_date", dateFormat.format(reimbursement.getExpenseDate()));
                }
                if (reimbursement.getEmployee() == null) {
                    obj.put("employee_id", "");
                    obj.put("name", "");
                } else {
                    obj.put("employee_id", reimbursement.getEmployee().getEmployeeId());
                    obj.put("name", reimbursement.getEmployee().getName());
                }
                if (reimbursement.getLoan() == null) {
                    obj.put("loan_id", "");
                    obj.put("loan_type", "");
                    obj.put("case_id", "");
                } else {
                    obj.put("loan_id", reimbursement.getLoan().getLoanId());
                    obj.put("loan_type", "b");
                    obj.put("case_id", reimbursement.getLoan().getEngagement().getCaseID());
                }
                if (reimbursement.getNote() == null) {
                    obj.put("note", "");
                } else {
                    obj.put("note", reimbursement.getNote());
                }
                if (reimbursement.getReimburseAmount() == null) {
                    obj.put("reimburse_amount", "");
                } else {
                    obj.put("reimburse_amount", String.format("%.0f", reimbursement.getReimburseAmount()));
                }
                if (reimbursement.getApprovedBy() == null) {
                    obj.put("approved_by", "");
                } else {
                    Employee dataAdmin = this.employeeService.findById(reimbursement.getApprovedBy());
                    obj.put("approved_by", dataAdmin.getEmployeeId());
                }
                if (reimbursement.getApprovedDate() == null) {
                    obj.put("approval_date", "");
                } else {
                    obj.put("approval_date", dateFormat.format(reimbursement.getApprovedDate()));
                }
                if (reimbursement.getReimbursedBy() == null) {
                    obj.put("reimburse_by", "");
                } else {
                    Employee dataFinance = this.employeeService.findById(reimbursement.getReimbursedBy());
                    obj.put("reimburse_by", dataFinance.getEmployeeId());
                }
                if (reimbursement.getReimbursedDate() == null) {
                    obj.put("reimbursed_date", "");
                } else {
                    obj.put("reimbursed_date", dateFormat.format(reimbursement.getReimbursedDate()));
                }
                if (reimbursement.getApprovedAmount() == null) {
                    obj.put("approved_amount", "");
                } else {
                    obj.put("approved_amount", String.format("%.0f", reimbursement.getApprovedAmount()));
                }
                if (reimbursement.getStatus() == null) {
                    obj.put("status", "");
                } else {
                    obj.put("status", reimbursement.getStatus());
                }
                if (reimbursement.getLinkDocument() == null) {
                    obj.put("link_document", "");
                } else {
                    obj.put("link_document", reimbursement.getLinkDocument());
                }
                array.put(obj);
            }
            return ResponseEntity.ok(array.toString());

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "listReimbursementAdmin");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/reimbursement/{reimburse_id}/document", method = RequestMethod.GET, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}
    @XxsFilter
    public ResponseEntity<?> viewDocumentReimbursement(ServletRequest request, HttpServletResponse response, @PathVariable("reimburse_id") Long reimburse_id, Authentication authentication) throws IOException {
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
                CreateLog.createJson(rs, "viewDocument-reimbursement");
                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
//            if (!entityEmp.getRoleName().contentEquals("dmp")) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("role : " + entityEmp.getRoleName() + " permission deny ");
//                process = false;
//                CreateLog.createJson(rs, "uploadMultipleDocument");
//                return rs;
//            }

            Employee employee = employeeService.findById(entityEmp.getIdEmployee());

            if (employee == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entityEmp.getRoleName() + " permission deny ");
                process = false;
                CreateLog.createJson(rs, "viewDocument-reimbursement");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (process) {

                Reimbursement rembursement = reimbursementService.findById(reimburse_id);
                log.info("rembursement : " + rembursement.getReimbursementId());
                try {
                    log.info("employee-2_" + employee.getIdEmployee());
                    log.info("entityEmp-2_" + entityEmp.getIdEmployee());
                    byte[] input_file = Files.readAllBytes(Paths.get(rembursement.getLinkDocument()));
                    String linkDoc = new String(Base64.getEncoder().encode(input_file));
                    jsonobj.put("response_code", "00");
                    jsonobj.put("response", "success");
                    jsonobj.put("info", linkDoc);
                } catch (JSONException ex) {
                    Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                    return new ResponseEntity(new CustomErrorType("55", "Error", "Employee Null"),
                            HttpStatus.NOT_FOUND);
                }

                return ResponseEntity.ok(jsonobj.toString());
            }
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse("Employee Null");
            CreateLog.createJson(rs, "viewDocument-reimbursement");
            return new ResponseEntity(new CustomErrorType("55", "Error", "Employee Null"),
                    HttpStatus.NOT_FOUND);
        } catch (IOException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "viewDocument-reimbursement");
//            return null;
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/reimbursements/dmp", method = RequestMethod.GET, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}
    @XxsFilter
    public ResponseEntity<String> listReimbursementDMP(Authentication authentication) {
        try {
            String nama = authentication.getName();
            Boolean process = true;
            Date expense_date_value = null;

            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "listReimbursementAdmin");
                process = false;
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
                CreateLog.createJson(rs, "listReimbursementAdmin");
                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!dataEmp.getRoleName().contains("dmp")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("your Role : " + dataEmp.getRoleName() + " Cannot Access This feature");
                CreateLog.createJson(rs, "listReimbursementAdmin");
                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            List<Reimbursement> listReimbursement = this.reimbursementService.listBy("dmp", dataEmp.getIdEmployee());
            JSONArray array = new JSONArray();
            for (int i = 0; i < listReimbursement.size(); i++) {
                JSONObject obj = new JSONObject();
                Reimbursement reimbursement = (Reimbursement) listReimbursement.get(i);

                if (reimbursement.getReimburseId() == null) {
                    obj.put("reimburse_id", "");
                } else {
                    obj.put("reimburse_id", reimbursement.getReimburseId());
                }
                if (reimbursement.getReimburseId() == null) {
                    obj.put("reimbursement_id", "");
                } else {
                    obj.put("reimbursement_id", reimbursement.getReimbursementId());
                }
                if (reimbursement.getExpenseDate() == null) {
                    obj.put("expense_date", "");
                } else {
                    obj.put("expense_date", dateFormat.format(reimbursement.getExpenseDate()));
                }
                if (reimbursement.getEmployee() == null) {
                    obj.put("employee_id", "");
                    obj.put("name", "");
                } else {
                    obj.put("employee_id", reimbursement.getEmployee().getEmployeeId());
                    obj.put("name", reimbursement.getEmployee().getName());
                }
                if (reimbursement.getLoan() == null) {
                    obj.put("loan_id", "");
                    obj.put("loan_type", "");
                    obj.put("case_id", "");
                } else {
                    obj.put("loan_id", reimbursement.getLoan().getLoanId());
                    obj.put("loan_type", "b");
                    obj.put("case_id", reimbursement.getLoan().getEngagement().getCaseID());
                }
                if (reimbursement.getNote() == null) {
                    obj.put("note", "");
                } else {
                    obj.put("note", reimbursement.getNote());
                }
                if (reimbursement.getReimburseAmount() == null) {
                    obj.put("reimburse_amount", "");
                } else {
                    obj.put("reimburse_amount", String.format("%.0f", reimbursement.getReimburseAmount()));
                }
                if (reimbursement.getApprovedBy() == null) {
                    obj.put("approved_by", "");
                } else {
                    Employee dataAdmin = this.employeeService.findById(reimbursement.getApprovedBy());
                    obj.put("approved_by", dataAdmin.getEmployeeId());
                }
                if (reimbursement.getApprovedDate() == null) {
                    obj.put("approval_date", "");
                } else {
                    obj.put("approval_date", dateFormat.format(reimbursement.getApprovedDate()));
                }
                if (reimbursement.getReimbursedBy() == null) {
                    obj.put("reimburse_by", "");
                } else {
                    Employee dataFinance = this.employeeService.findById(reimbursement.getReimbursedBy());
                    obj.put("reimburse_by", dataFinance.getEmployeeId());
                }
                if (reimbursement.getReimbursedDate() == null) {
                    obj.put("reimbursed_date", "");
                } else {
                    obj.put("reimbursed_date", dateFormat.format(reimbursement.getReimbursedDate()));
                }
                if (reimbursement.getApprovedAmount() == null) {
                    obj.put("approved_amount", "");
                } else {
                    obj.put("approved_amount", String.format("%.0f", reimbursement.getApprovedAmount()));
                }
                if (reimbursement.getStatus() == null) {
                    obj.put("status", "");
                } else {
                    obj.put("status", reimbursement.getStatus());
                }
                if (reimbursement.getLinkDocument() == null) {
                    obj.put("link_document", "");
                } else {
                    obj.put("link_document", reimbursement.getLinkDocument());
                }
                array.put(obj);
            }
            return ResponseEntity.ok(array.toString());

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "listReimbursementAdmin");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }
}
