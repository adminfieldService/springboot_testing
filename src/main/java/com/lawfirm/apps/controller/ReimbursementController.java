/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.Engagement;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.model.OutStandingLoanB;
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
import com.lawfirm.apps.service.interfaces.LoanServiceIface;
import com.lawfirm.apps.service.interfaces.LoanTypeServiceIface;
import com.lawfirm.apps.service.interfaces.MemberServiceIface;
import com.lawfirm.apps.service.interfaces.ProfessionalServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementHistoryServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import com.lawfirm.apps.support.api.ApprovalReimbursementDto;
import com.lawfirm.apps.support.api.DatareimbursementDto;
import com.lawfirm.apps.support.api.RejectReimbursementDto;
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
import com.lawfirm.apps.service.interfaces.OutStandingLoanBServiceIface;
import java.text.ParseException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author newbiecihuy
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "*", maxAge = 3600)
//@Slf4j
//@RequestMapping({"/reimbursement"})//pengembalian
public class ReimbursementController {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
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
    @Autowired
    OutStandingLoanBServiceIface outStandingLoanBService;

    public ReimbursementController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yy");
        this.sdfMonth = new SimpleDateFormat("MM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
    }

    @RequestMapping(value = "/reimbursement/{id_loan}", method = RequestMethod.POST, consumes = {"multipart/form-data"}, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}, produces = {"application/json"}
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
            Boolean attached = null;
            Date expense_date_value = null;
            if (!file.isEmpty()) {
                attached = true;
            } else {
                attached = false;
            }
            log.info("createReimburse object value case_id :" + case_id + " expense_date : " + expense_date + " amount : " + amount + " note : " + note + " isattached: " + attached);
            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("createReimburse : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "create-reimburse");
                log.error("createReimburse : " + rs.toString());
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
                log.error("createReimburse : " + rs.toString());
                return rs;
            }
            if (!dataEmp.getRoleName().matches("dmp")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                log.error("createReimburse : " + rs.toString());
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
                log.error("createReimburse : " + rs.toString());
                return rs;
            }
            if (findByCaseID.getStatus().contentEquals("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Case Id : " + case_id + " Rejected ");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                log.error("createReimburse : " + rs.toString());
                return rs;
            }
            if (findByCaseID.getStatus().contentEquals("s")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Case Id : " + case_id + " Need Admin Approval");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                log.error("createReimburse : " + rs.toString());
                return rs;
            }
            if (findByCaseID.getStatus().contentEquals("closed")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Case Id : " + case_id + " Status Close");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                log.error("createReimburse : " + rs.toString());
                return rs;
            }

            Loan entityLoan = loanService.findByIdLoan(id_loan);
            if (entityLoan == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature, Loan type : " + entityLoan.getLoantype().getTypeLoan());
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                log.error("createReimburse : " + rs.toString());
                return rs;
            }
            if (entityLoan.getStatus().contentEquals("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature,  Loan status : " + "Rejected");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                log.error("createReimburse : " + rs.toString());
                return rs;
            }
            if (entityLoan.getStatus().contentEquals("s")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature,  Loan Must : " + "Approved");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                log.error("createReimburse : " + rs.toString());
                return rs;
            }
            if (entityLoan.getLoantype().getTypeLoan().contains("a")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("id Loan : " + id_loan + " not Found");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                log.error("createReimburse : " + rs.toString());
                return rs;
            }
//            Double loanB = entityLoan.getLoanAmount();
            Double loanB = 0d;
            loanB = this.loanService.sumLoanByCaseId(case_id);

            if (amount > loanB) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse Amount : " + String.format("%.0f", amount) + " Greater than loanB " + String.format("%.0f", loanB));
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                log.error("createReimburse : " + rs.toString());
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
                    log.info("createReimburse success: " + rs.toString());
                    return rs;
                }

            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("id Loan : " + id_loan + " not Found");
                CreateLog.createJson(rs, "create-reimburse");
                log.error("createReimburse : " + rs.toString());
                return rs;
            }

        } catch (IOException | ParseException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "create-reimburse");
            log.error("createReimburse : " + ex.getMessage());
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
                log.error("listReimbursement : " + rs.toString());
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
                log.error("listReimbursement : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            List<Reimbursement> listReimbursement = reimbursementService.listReimbursement();
            log.info("listReimbursement.size() : " + listReimbursement.size());
            JSONArray array = new JSONArray();
            for (int i = 0; i < listReimbursement.size(); i++) {

                JSONObject obj = new JSONObject();
                Reimbursement reimbursement = (Reimbursement) listReimbursement.get(i);

                if (reimbursement.getReimburseId() == null) {
                    obj.put("reimburse_id", "");
                } else {
                    obj.put("reimburse_id", reimbursement.getReimburseId());
                }
                if (reimbursement.getReimbursementId() == null) {
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
                    obj.put("loan_amount", "");
                    obj.put("case_id", "");
                } else {
                    obj.put("loan_id", reimbursement.getLoan().getLoanId());
                    obj.put("loan_type", "b");
                    obj.put("loan_amount", reimbursement.getLoan().getLoanAmount());
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
            log.error("listReimbursement : " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "listReimbursement");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

//    @RequestMapping(value = "/reimbursement/{reimburse_id}/find-by-id", method = RequestMethod.POST, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}
    @RequestMapping(value = "/reimbursement/find-by-id", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> reimbursementFindById(@RequestBody final DatareimbursementDto object, Authentication authentication) {
        try {
            Date now = new Date();
            String nama = authentication.getName();
            Boolean process = true;
            log.info("nama : " + nama);
            log.info("reimbursementFindById json : " + object);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                log.error("reimbursementFindById : " + rs.toString());
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
                log.error("reimbursementFindById : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (process) {
                Reimbursement reimbursement = reimbursementService.findById(object.getReimburse_id());
                JSONObject obj = new JSONObject();
                if (reimbursement != null) {

                    if (reimbursement.getReimburseId() == null) {
                        obj.put("reimburse_id", "");
                    } else {
                        obj.put("reimburse_id", reimbursement.getReimburseId());
                    }
                    if (reimbursement.getReimbursementId() == null) {
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
                        obj.put("loan_amount", "");
                        obj.put("case_id", "");
                    } else {
                        obj.put("loan_id", reimbursement.getLoan().getLoanId());
                        obj.put("loan_type", "b");
                        obj.put("loan_amount", reimbursement.getLoan().getLoanAmount());
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
                log.info("reimbursementFindById : " + obj.toString());
                return ResponseEntity.ok(obj.toString());
            }

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "reimbursementFindById");
            log.error("error : " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/reimbursement/{reimburse_id}/approval", method = RequestMethod.POST, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}
    @XxsFilter
    public Response approvalReimbursement(@RequestBody ApprovalReimbursementDto object, @PathVariable("reimburse_id") Long reimburse_id, Authentication authentication) {
        try {
            Date now = new Date();
            String nama = authentication.getName();
            Boolean process = true;
            Date expense_date_value = null;
            log.info("approvalReimbursement json  : " + object.toString());

            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "approve-reimbursement");
                process = false;
                log.error("approval-reimbursement : " + rs.toString());
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
                log.error("approval-reimbursement : " + rs.toString());
                return rs;
            }
            if (!dataEmp.getRoleName().matches("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "approval-reimbursement");
                process = false;
                log.error("approval-reimbursement : " + rs.toString());
                return rs;
            }
            Reimbursement dataReimbursement = reimbursementService.findById(reimburse_id);
            if (dataReimbursement == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + reimburse_id + " not Found");
                CreateLog.createJson(rs, "approval-reimbursement");
                process = false;
                log.error("approval-reimbursement : " + rs.toString());
                return rs;
            }
            if (dataReimbursement.getStatus().contentEquals("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + dataReimbursement.getReimbursementId() + " Rejected by admin");
                CreateLog.createJson(rs, "approval-reimbursement");
                process = false;
                log.error("approval-reimbursement : " + rs.toString());
                return rs;
            }
            if (dataReimbursement.getStatus().contentEquals("reimburse")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + dataReimbursement.getReimbursementId() + " already reimburse");
                CreateLog.createJson(rs, "approval-reimbursement");
                process = false;
                log.error("approval-reimbursement : " + rs.toString());
                return rs;
            }
            if (dataReimbursement.getStatus().contentEquals("a")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + dataReimbursement.getReimbursementId() + " already Approve ");
                CreateLog.createJson(rs, "approval-reimbursement");
                process = false;
                log.error("approval-reimbursement : " + rs.toString());
                return rs;
            }
            if (object.getApproved_amount() > dataReimbursement.getReimburseAmount()) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse(" Approved Amount : " + String.format("%.0f", object.getApproved_amount()) + " Greater than Reimburse Amount : " + String.format("%.0f", dataReimbursement.getReimburseAmount()));
                CreateLog.createJson(rs, "approval-reimbursement");
                process = false;
                log.error("approval-reimbursement : " + rs.toString());
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
                    rs.setResponse("data reimbursement : " + dataReimbursement.getReimbursementId() + " approved by admin " + entityEmp.getEmployeeId());
                    log.error("approval-reimbursement : " + rs.toString());
                    return rs;
                }

//                }
            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("data reimbursement not Found");
                log.error("approval-reimbursement : " + rs.toString());
                CreateLog.createJson(rs, "approval-reimbursement");
                return rs;
            }
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse("data id : " + reimburse_id + " not Found");
            log.error("approval-reimbursement : " + rs.toString());
            CreateLog.createJson(rs, "reimbursement");
            return rs;
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "reimbursement");
            System.out.println("ERROR: " + ex.getMessage());
            log.error("approval-reimbursement : " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "approval-reimbursement");
            return rs;
        }
//        rs.setResponse_code("55");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");
//        return rs;
    }

    @RequestMapping(value = "/reimbursement/{reimburse_id}/reject", method = RequestMethod.POST, produces = {"application/json"})//{id_loan}, consumes = {"multipart/form-data"}
    @XxsFilter
    public Response rejectReimbursement(@RequestBody RejectReimbursementDto object, @PathVariable("reimburse_id") Long reimburse_id, Authentication authentication) {
        try {
            Date now = new Date();
            String nama = authentication.getName();
            Boolean process = true;
            Date expense_date_value = null;
            log.info("reject-reimbursement json  : " + object.toString());

//            log.info("nama : " + nama);
            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "reject-reimbursement");
                log.error("reject-reimbursement : " + rs.toString());
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
                CreateLog.createJson(rs, "reject-reimbursement");
                process = false;
                log.error("reject-reimbursement : " + rs.toString());
                return rs;
            }
            if (!dataEmp.getRoleName().matches("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "reject-reimbursement");
                process = false;
                log.error("reject-reimbursement : " + rs.toString());
                return rs;
            }
            Reimbursement dataReimbursement = reimbursementService.findById(reimburse_id);
            if (dataReimbursement == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + reimburse_id + " not Found");
                CreateLog.createJson(rs, "reject-reimbursement");
                process = false;
                return rs;
            }
            if (dataReimbursement.getStatus().contentEquals("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + dataReimbursement.getReimbursementId() + " Rejected by admin");
                CreateLog.createJson(rs, "reject-reimbursement");
                process = false;
                return rs;
            }
            if (dataReimbursement.getStatus().contentEquals("reimburse")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + dataReimbursement.getReimbursementId() + " already reimburse");
                CreateLog.createJson(rs, "reject-reimbursement");
                process = false;
                return rs;
            }
            if (dataReimbursement.getStatus().contentEquals("a")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + dataReimbursement.getReimbursementId() + " already Approve ");
                CreateLog.createJson(rs, "reject-reimbursement");
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
                dataReimbursement.setStatus("r");
                dataReimbursement.setRemarks(object.getRemarks());
                dataReimbursement.setApprovedDate(now);
                dataReimbursement.setApprovedAmount(0.0);

                history.setResponse("reject by : " + dataEmp.getEmployeeId() + " remarks : " + object.getRemarks());
                history.setUserId(dataEmp.getIdEmployee());
                Reimbursement updateReimbursement = reimbursementService.update(dataReimbursement);
                if (updateReimbursement != null) {
                    history.setReimbursement(updateReimbursement);
                    this.reimbursementHistoryService.create(history);
                    rs.setResponse_code("00");
                    rs.setInfo("Success");
                    rs.setResponse("data reimbursement : " + dataReimbursement.getReimbursementId() + " rejected by admin " + entityEmp.getEmployeeId());
                    log.info("reject-reimbursement  : " + rs.toString());
                    return rs;
                }

//                }
            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("data reimbursement not Found");
                CreateLog.createJson(rs, "approval-reimbursement");
                log.error("reject-reimbursement  : " + rs.toString());
                return rs;
            }
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse("data id : " + reimburse_id + " not Found");
            CreateLog.createJson(rs, "reimbursement");
            log.error("reject-reimbursement  : " + rs.toString());
            return rs;
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "reimbursement");
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "approval-reimbursement");
            log.error("reject-reimbursement  : " + ex.getMessage());
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
            log.info("nama : " + nama);
            Boolean process = true;
            Date expense_date_value = null;
            String caseId = null;
            Double totalLoanB = 0d;
            Double totalOutStanding = 0d;
//            log.info("object value : " + object.toString());
            log.info("reimburse_id : " + reimburse_id);

            Employee entityEmp = employeeService.findByEmployee(nama);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "reimbursement");
                process = false;
                log.error("reimburse_id : " + rs.toString());
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
                log.error("reimburse_id : " + rs.toString());
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
                log.error("reimburse_id : " + rs.toString());
                return rs;
            }
            if (dataReimbursement.getStatus().contentEquals("reimburse")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + dataReimbursement.getReimbursementId() + " already Reimburse ");
                CreateLog.createJson(rs, "reimbursement");
                process = false;
                log.error("reimburse_id : " + rs.toString());
                return rs;
            }
            if (dataReimbursement.getStatus().contentEquals("s")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + dataReimbursement.getReimbursementId() + " must approve by admin");
                CreateLog.createJson(rs, "reimbursement");
                process = false;
                log.error("reimburse_id : " + rs.toString());
                return rs;
            }
            if (dataReimbursement.getStatus().contentEquals("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + dataReimbursement.getReimbursementId() + " Rejected by admin");
                CreateLog.createJson(rs, "reimbursement");
                process = false;
                log.error("reimburse_id : " + rs.toString());
                return rs;
            }

            if (process) {
                ReimbursementHistory history = new ReimbursementHistory();
                OutStandingLoanB outStanding = new OutStandingLoanB();
//                if (object.getDecision().contentEquals("reimburse")) {
                if (dataReimbursement.getApprovedAmount() > dataReimbursement.getReimburseAmount()) {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse(" Approved Amount : " + String.format("%.0f", dataReimbursement.getApprovedAmount()) + " Greater than Reimburse Amount : " + String.format("%.0f", dataReimbursement.getReimburseAmount()));
                    CreateLog.createJson(rs, "reimbursement");
                    process = false;
                    log.error("reimburse_id : " + rs.toString());
                    return rs;
                }
                dataReimbursement.setReimbursedBy(dataEmp.getIdEmployee());
                dataReimbursement.setStatus("reimburse");
                dataReimbursement.setReimbursedDate(now);

                outStanding.setIdEmployee(dataEmp.getIdEmployee());
                outStanding.setReimburseAmount(dataReimbursement.getApprovedAmount());
                outStanding.setReimburseId(reimburse_id);
                Loan dataLoan = this.loanService.findByIdLoan(dataReimbursement.getLoan().getId());
//                outStanding.setLoan(dataLoan);
                Engagement engagement = this.engagementService.findById(dataLoan.getEngagement().getEngagementId());
                if (engagement == null) {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("CASE ID Not Found");
                    CreateLog.createJson(rs, "reimbursement");
                    process = false;
                    log.error("reimburse_id : " + rs.toString());
                    return rs;
                }

                if (engagement.getStatus().contentEquals("s")) {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("CASE ID : " + engagement.getCaseID() + " Need admin approval");
                    CreateLog.createJson(rs, "reimbursement");
                    process = false;
                    log.error("reimburse_id : " + rs.toString());
                    return rs;
                }
                if (engagement.getStatus().contentEquals("r")) {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("CASE ID : " + engagement.getCaseID() + " Rejected by admin");
                    CreateLog.createJson(rs, "reimbursement");
                    process = false;
                    log.error("reimburse_id : " + rs.toString());
                    return rs;
                }
                if (engagement.getStatus().contentEquals("closed")) {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("CASE ID : " + engagement.getCaseID() + " Status CLOSE");
                    CreateLog.createJson(rs, "reimbursement");
                    process = false;
                    log.error("reimburse_id : " + rs.toString());
                    return rs;
                }

                if (engagement != null) {
                    outStanding.setCaseId(engagement.getCaseID());
                    caseId = engagement.getCaseID();
                }

                totalLoanB = this.loanService.sumLoanByCaseId(caseId);
                outStanding.setLoanAmount(totalLoanB);//total_loan_amount
                totalOutStanding = totalLoanB - dataReimbursement.getApprovedAmount();//out_standing = total_loanB By case id - total reimbursement approve By case id;
                outStanding.setOutStanding(totalOutStanding);//out_standing
                outStanding.setTahun_input(sdfYear.format(now));
                history.setResponse("reimburse by : " + dataEmp.getEmployeeId());
                history.setUserId(dataEmp.getIdEmployee());
                Reimbursement updateReimbursement = reimbursementService.update(dataReimbursement);
                if (updateReimbursement != null) {
                    history.setReimbursement(updateReimbursement);
                    this.reimbursementHistoryService.create(history);
                    this.outStandingLoanBService.create(outStanding);
                    rs.setResponse_code("00");
                    rs.setInfo("Success");
                    rs.setResponse("data reimbursement reimburse by : " + entityEmp.getEmployeeId());
                    log.info("Success reimbursement : " + rs.toString());
                    return rs;
                }
//                }
            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reimburse id : " + reimburse_id + " not Found");
                CreateLog.createJson(rs, "reimbursement");
                log.error("reimburse_id : " + rs.toString());
                return rs;
            }
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse("Cannot Access This feature");
            CreateLog.createJson(rs, "reimbursement");
            log.error("reimburse_id : " + rs.toString());
            return rs;
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "reimbursement");
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "reimbursement");
            log.error("reimburse_id : " + ex.getMessage());
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
                log.error("listReimbursementFinace : " + rs.toString());
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
                log.error("listReimbursementFinace : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!dataEmp.getRoleName().contains("finance")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("your Role : " + dataEmp.getRoleName() + " Cannot Access This feature");
                CreateLog.createJson(rs, "listReimbursementFinace");
                process = false;
                log.error("listReimbursementFinace : " + rs.toString());
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
                if (reimbursement.getReimbursementId() == null) {
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
                    obj.put("loan_amount", "");
                    obj.put("case_id", "");
                } else {
                    obj.put("loan_id", reimbursement.getLoan().getLoanId());
                    obj.put("loan_type", "b");
                    obj.put("loan_amount", reimbursement.getLoan().getLoanAmount());
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
            log.info("listReimbursementFinace : " + array.toString());
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "listReimbursementFinace");
            log.error("listReimbursementFinace Error : " + ex.getMessage());
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
                log.error("listReimbursementAdmin " + rs.toString());
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
                log.error("listReimbursementAdmin " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!dataEmp.getRoleName().contains("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("your Role : " + dataEmp.getRoleName() + " Cannot Access This feature");
                CreateLog.createJson(rs, "listReimbursementAdmin");
                process = false;
                log.error("listReimbursementAdmin " + rs.toString());
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
                if (reimbursement.getReimbursementId() == null) {
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
                    obj.put("loan_amount", "");
                    obj.put("case_id", "");
                } else {
                    obj.put("loan_id", reimbursement.getLoan().getLoanId());
                    obj.put("loan_type", "b");
                    obj.put("loan_amount", reimbursement.getLoan().getLoanAmount());
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
            log.info("listReimbursementAdmin : " + array.toString());
            return ResponseEntity.ok(array.toString());

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "listReimbursementAdmin");
            log.error("listReimbursementAdmin : " + ex.getMessage());
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
                log.error("viewDocument-reimbursement : " + rs.toString());
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
                log.error("viewDocument-reimbursement : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (process) {

                Reimbursement rembursement = reimbursementService.findById(reimburse_id);
                log.info("rembursement : " + rembursement.getReimbursementId());
                try {
                    String mime = rembursement.getMimeType();
                    byte[] input_file = Files.readAllBytes(Paths.get(rembursement.getLinkDocument()));
                    String linkDoc = new String(Base64.getEncoder().encode(input_file));
                    jsonobj.put("response_code", "00");
                    jsonobj.put("response", "success");
                    jsonobj.put("info", linkDoc);
                } catch (JSONException ex) {
                    log.error("viewDocument-reimbursement : " + ex.getMessage());
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
            log.error("viewDocument-reimbursement : " + ex.getMessage());
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
                CreateLog.createJson(rs, "listReimbursementDMP");
                process = false;
                log.error("listReimbursementDMP : " + rs.toString());
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
                CreateLog.createJson(rs, "listReimbursementDMP");
                process = false;
                log.error("listReimbursementDMP : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
//            if (!dataEmp.getRoleName().contains("dmp")) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("your Role : " + dataEmp.getRoleName() + " Cannot Access This feature");
//                CreateLog.createJson(rs, "listReimbursementDMP");
//                process = false;
//                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
//                        HttpStatus.NOT_FOUND);
//            }
//            if (!dataEmp.getRoleName().contains("finance")) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("your Role : " + dataEmp.getRoleName() + " Cannot Access This feature");
//                CreateLog.createJson(rs, "listReimbursementDMP");
//                process = false;
//                return new ResponseEntity(new CustomErrorType("55", "Error", "Cannot Access This feature"),
//                        HttpStatus.NOT_FOUND);
//            }
            List<Reimbursement> listReimbursement = null;
            if (dataEmp.getRoleName().contains("dmp")) {
                listReimbursement = this.reimbursementService.listBy("dmp", dataEmp.getIdEmployee());
            }
            if (dataEmp.getRoleName().contains("finance")) {
                listReimbursement = this.reimbursementService.listBy("finance", dataEmp.getIdEmployee());
            }
            if (dataEmp.getRoleName().contains("admin")) {
                listReimbursement = this.reimbursementService.listBy("admin", dataEmp.getIdEmployee());
            }
            JSONArray array = new JSONArray();
            for (int i = 0; i < listReimbursement.size(); i++) {
                JSONObject obj = new JSONObject();
                Reimbursement reimbursement = (Reimbursement) listReimbursement.get(i);

                if (reimbursement.getReimburseId() == null) {
                    obj.put("reimburse_id", "");
                } else {
                    obj.put("reimburse_id", reimbursement.getReimburseId());
                }
                if (reimbursement.getReimbursementId() == null) {
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
                    obj.put("loan_amount", "");
                    obj.put("case_id", "");
                } else {
                    obj.put("loan_id", reimbursement.getLoan().getLoanId());
                    obj.put("loan_type", "b");
                    obj.put("loan_amount", reimbursement.getLoan().getLoanAmount());
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
            log.info("listReimbursementDMP : " + array.toString());
            return ResponseEntity.ok(array.toString());

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "listReimbursementDMP");
            log.error("listReimbursementDMP : " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }
}
