/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.Disbursement;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.Financial;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.model.LoanHistory;
import com.lawfirm.apps.model.LoanType;
import com.lawfirm.apps.response.Response;
import com.lawfirm.apps.service.CaseDocumentService;
import com.lawfirm.apps.service.interfaces.AccountServiceIface;
import com.lawfirm.apps.service.interfaces.CaseDetailsServiceIface;
import com.lawfirm.apps.service.interfaces.ClientDataServiceIface;
import com.lawfirm.apps.service.interfaces.DisbursementServiceIface;
import com.lawfirm.apps.service.interfaces.DocumentReimburseServiceIface;
import com.lawfirm.apps.service.interfaces.EmployeeRoleServiceIface;
import com.lawfirm.apps.service.interfaces.EmployeeServiceIface;
import com.lawfirm.apps.service.interfaces.EngagementServiceIface;
import com.lawfirm.apps.service.interfaces.EventServiceIface;
import com.lawfirm.apps.service.interfaces.FinancialServiceIface;
import com.lawfirm.apps.service.interfaces.LoanHistoryServiceIface;
import com.lawfirm.apps.service.interfaces.LoanServiceIface;
import com.lawfirm.apps.service.interfaces.LoanTypeServiceIface;
import com.lawfirm.apps.service.interfaces.MemberServiceIface;
import com.lawfirm.apps.service.interfaces.ProfessionalServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import com.lawfirm.apps.support.api.LoanApi;
import com.lawfirm.apps.utils.CreateLog;
import com.lawfirm.apps.utils.CustomErrorType;
import com.xss.filter.annotation.XxsFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author newbiecihuy
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
//@RequestMapping({"/disbursement"})//pembayaran
public class DisbursementController {

    static String basepathUpload = "/opt/lawfirm/UploadFile/";

    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;
    SimpleDateFormat sdfYear;
    SimpleDateFormat sdfMonth;
    SimpleDateFormat sdfMY;
    SimpleDateFormat sdfDisbursM;
    SimpleDateFormat sdfDisbursMY;

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
    TeamMemberServiceIface teamMemberService;
    @Autowired
    MemberServiceIface memberServiceIface;
    @Autowired
    EventServiceIface eventServiceIface;
    @Autowired
    DisbursementServiceIface disbursementService;
    @Autowired
    LoanHistoryServiceIface loanHistoryService;

    public DisbursementController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yy");
        this.sdfMonth = new SimpleDateFormat("MM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
        this.sdfDisbursM = new SimpleDateFormat("MMM");
        this.sdfDisbursMY = new SimpleDateFormat("MMMyyyy");
    }

    @RequestMapping(value = "/disbursements/loans", method = RequestMethod.GET, produces = {"application/json"})//produces = {"application/json"}
    @XxsFilter
    public ResponseEntity<?> disbursements(Authentication authentication) {
        try {
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "disbursements");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "disbursements");
                process = false;

            }
            if (process) {
                List<Loan> listLoanDisburse = disbursementService.listDisburse("0");
                JSONArray array = new JSONArray();
                for (int i = 0; i < listLoanDisburse.size(); i++) {
                    JSONObject jsonobj = new JSONObject();
                    Loan entity = (Loan) listLoanDisburse.get(i);
                    if (entity.getId() == null) {
                        jsonobj.put("id_loan", "");
                    } else {
                        jsonobj.put("id_loan", entity.getId());
                    }
                    if (entity.getLoanId() == null) {
                        jsonobj.put("loan_id", "");
                    } else {
                        jsonobj.put("loan_id", entity.getLoanId());
                    }
                    if (entity.getLoanAmount() == null) {
                        jsonobj.put("amount", "");
                    } else {
                        jsonobj.put("amount", String.format("%.0f", entity.getLoanAmount()));
                    }
                    if (entity.getAprovedByAdmin() == null) {
                        jsonobj.put("aproved_by_admin", "");
                    } else {
                        Employee dataAdmin = this.employeeService.findById(Long.parseLong(entity.getAprovedByAdmin()));
                        jsonobj.put("aproved_by_admin", dataAdmin.getEmployeeId());
                    }
                    if (entity.getDate_approved() == null) {
                        jsonobj.put("date_approve_by_admin", "");
                    } else {
                        jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getDate_approved()));
                    }
                    if (entity.getAprovedByFinance() == null) {
                        jsonobj.put("disburse_by_finance", "");
                    } else {
                        Employee dataFinance = this.employeeService.findById(Long.parseLong(entity.getAprovedByFinance()));
                        jsonobj.put("disburse_by_finance", dataFinance.getEmployeeId());
                    }
                    if (entity.getDate_created() == null) {
                        jsonobj.put("date_created", "");
                    } else {
                        jsonobj.put("date_created", dateFormat.format(entity.getDate_created()));
                    }
                    if (entity.getDate_approved_by_finance() == null) {
                        jsonobj.put("date_disburse_by_finance", "");
                    } else {
                        jsonobj.put("date_disburse_by_finance", dateFormat.format(entity.getDate_approved_by_finance()));
                    }
                    if (entity.getLoantype().getTypeLoan() == null) {
                        jsonobj.put("loan_type", "");
                    } else {
                        jsonobj.put("loan_type", entity.getLoantype().getTypeLoan());
                    }
//           
                    if (entity.getEmployee().getIdEmployee() == null) {
                        jsonobj.put("id_employee", "");
                    } else {
                        jsonobj.put("id_employee", entity.getEmployee().getIdEmployee());
                    }
                    if (entity.getEmployee().getEmployeeId() == null) {
                        jsonobj.put("employee_id", "");
                    } else {
                        jsonobj.put("employee_id", entity.getEmployee().getEmployeeId());
                    }
                    if (entity.getEmployee().getNik() == null) {
                        jsonobj.put("nik", "");
                    } else {
                        jsonobj.put("nik", entity.getEmployee().getNik());
                    }
                    if (entity.getEmployee().getNpwp() == null) {
                        jsonobj.put("npwp", "");
                    } else {
                        jsonobj.put("npwp", entity.getEmployee().getNpwp());
                    }
                    if (entity.getEmployee().getName() == null) {
                        jsonobj.put("nama", "");
                    } else {
                        jsonobj.put("nama", entity.getEmployee().getName());
                    }
                    if (entity.getStatus() == null) {
                        jsonobj.put("status_loan", "");
                    } else {
                        jsonobj.put("status_loan", entity.getStatus());
                    }
                    array.put(jsonobj);
                }
                return ResponseEntity.ok(array.toString());
            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan Data Null ");
                CreateLog.createJson(rs, "disbursements");
                return new ResponseEntity(new CustomErrorType("55", "Error", "Loan Data Null"),
                        HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException | org.json.JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "disbursements");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/disburse/{id_loan}", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response disburse(@RequestBody final LoanApi object, @PathVariable("id_loan") Long id_loan, Authentication authentication) {
        try {
            Date now = new Date();
            Date dateDisburs = new Date();
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);

            Boolean process = true;
            Loan dataLoan = new Loan();
            LoanType typeLoan = new LoanType();
            LoanHistory entityHistory = new LoanHistory();
            Disbursement entityDisbursement = new Disbursement();
            Financial dataFinance = new Financial();
//        if (dataFinance == null) {
//            rs.setResponse_code("55");
//            rs.setInfo("Failed");
//            rs.setResponse("Finance Id Not found");
//            CreateLog.createJson(rs, "loan-approve-Byfinance");
//            process = false;
//        }
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "disburse");
//                process = false;
                return rs;
            }
            if (!entityEmp.getRoleName().contentEquals("finance")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature : " + entityEmp.getRoleName().toUpperCase());
                CreateLog.createJson(rs, "disburse");
//                process = false;
                return rs;
            }
//             Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());

            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "disburse");
                process = false;
            }
//        if (dataEMploye.getRoleName().contentEquals("finance")) {
//            rs.setResponse_code("55");
//            rs.setInfo("Failed");
//            rs.setResponse("Cannot Access This feature");
//            process = false;
//        }
            dataLoan = loanService.findById(id_loan);

            if (dataLoan == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loand id null, Cannot Access This feature");
                CreateLog.createJson(rs, "disburse");
                process = false;
            }
            if (dataLoan.getStatus().contentEquals("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan apps are rejected");
                CreateLog.createJson(rs, "disburse");
                process = false;
            }
            if (dataLoan.getIsActive().contentEquals("1")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan apps Must approve by ADMIN");
                CreateLog.createJson(rs, "disburse");
                process = false;
            }
            if (dataLoan.getIsActive().contentEquals("4")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Already approved by finance ");
                CreateLog.createJson(rs, "disburse");
                process = false;
            }
            if (process) {
                dataLoan.setAprovedByFinance(dataEMploye.getIdEmployee().toString());//dataEMploye.getName()
                dataLoan.setDate_approved_by_finance(new Date());
                dataLoan.setStatus("d");
                dataLoan.setIsActive("4");
                System.out.println("isi : " + object.getDisburse_date());
                String dt = dateFormat.format(object.getDisburse_date());
                Date disburse = dateFormat.parse(dt);
                dataLoan.setDisburse_date(disburse);
                String disburseM = sdfDisbursM.format(new Date());
                String disburseMy = sdfDisbursMY.format(new Date());
                System.out.println("isi disburseM : " + disburseM);

                entityDisbursement.setBulanInput(disburseM);
                String dsb_id = "DSB" + disburseMy;
                entityDisbursement.setDisbursementId(dsb_id);
                entityDisbursement.setDisburse_date(disburse);

                entityHistory.setLoan(dataLoan);
                entityHistory.setUserId(entityEmp.getIdEmployee());
                entityHistory.setResponse("disburse by : " + entityEmp.getEmployeeId());
                Loan upDataLoan = loanService.update(dataLoan);
                this.loanHistoryService.create(entityHistory);
                if (upDataLoan != null) {
                    dataFinance.setDisburse_date(new Date());
                    if (object.getOut_standing() != null) {
                        dataFinance.setOutStanding(object.getOut_standing());
                    } else {
                        rs.setResponse_code("55");
                        rs.setInfo("Failed");
                        rs.setResponse("out Standing Field can't be null");
                        CreateLog.createJson(rs, "disburse");
                    }
                    Financial upd_finance = financialService.update(dataFinance);
                    if (upd_finance != null) {
                        rs.setResponse_code("01");
                        rs.setInfo("Success");
                        rs.setResponse("Loan apps Approved By :" + dataEMploye.getEmployeeId());//dataLoan.getAprovedByFinance()
                        CreateLog.createJson(rs, "disburse");
                        return rs;
                    }
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("Loand id null, Cannot Access This feature");
                    CreateLog.createJson(rs, "disburse");
                    return rs;
                } else {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("Loand id null, Cannot Access This feature");
                    CreateLog.createJson(rs, "disburse");
                    return rs;
                }
            }
            return rs;
        } catch (org.json.JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "disburse");
            CreateLog.createJson(ex.getMessage(), "disburse");
            return rs;

        } catch (ParseException ex) {
            Logger.getLogger(LoanController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "disburse");
            CreateLog.createJson(ex.getMessage(), "disburse");
            return rs;
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/disbursements/loan-a", method = RequestMethod.GET, produces = {"application/json"})//produces = {"application/json"}
    @XxsFilter
    public ResponseEntity<?> disbursementsAFinance(Authentication authentication) {
        try {
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "disbursementsFinance-loanA");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "disbursementsFinance-loanA");
                process = false;

            }
            if (!dataEMploye.getRoleName().contains("finance")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("YOur Role can't acces this feature : ");
                CreateLog.createJson(rs, "disbursementsFinance-loanA");
                process = false;

            }
            if (process) {
                List<Loan> listLoanDisburse = disbursementService.viewDisburseByFinance(dataEMploye.getIdEmployee().toString(), "a");
                JSONArray array = new JSONArray();
                for (int i = 0; i < listLoanDisburse.size(); i++) {
                    JSONObject jsonobj = new JSONObject();
                    Loan entity = (Loan) listLoanDisburse.get(i);
                    if (entity.getId() == null) {
                        jsonobj.put("id_loan", "");
                    } else {
                        jsonobj.put("id_loan", entity.getId());
                    }
                    if (entity.getLoanId() == null) {
                        jsonobj.put("loan_id", "");
                    } else {
                        jsonobj.put("loan_id", entity.getLoanId());
                    }
                    if (entity.getLoanAmount() == null) {
                        jsonobj.put("amount", "");
                    } else {
                        jsonobj.put("amount", String.format("%.0f", entity.getLoanAmount()));
                    }
                    if (entity.getAprovedByAdmin() == null) {
                        jsonobj.put("aproved_by_admin", "");
                    } else {
                        Employee dataAdmin = this.employeeService.findById(Long.parseLong(entity.getAprovedByAdmin()));
                        jsonobj.put("aproved_by_admin", dataAdmin.getEmployeeId());
                    }
                    if (entity.getDate_approved() == null) {
                        jsonobj.put("date_approve_by_admin", "");
                    } else {
                        jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getDate_approved()));
                    }
                    if (entity.getAprovedByFinance() == null) {
                        jsonobj.put("disburse_by_finance", "");
                    } else {
                        Employee dataFinance = this.employeeService.findById(Long.parseLong(entity.getAprovedByFinance()));
                        jsonobj.put("disburse_by_finance", dataFinance.getEmployeeId());
                    }
                    if (entity.getDate_created() == null) {
                        jsonobj.put("date_created", "");
                    } else {
                        jsonobj.put("date_created", dateFormat.format(entity.getDate_created()));
                    }
                    if (entity.getDate_approved_by_finance() == null) {
                        jsonobj.put("date_disburse_by_finance", "");
                    } else {
                        jsonobj.put("date_disburse_by_finance", dateFormat.format(entity.getDate_approved_by_finance()));
                    }
                    if (entity.getLoantype().getTypeLoan() == null) {
                        jsonobj.put("loan_type", "");
                    } else {
                        jsonobj.put("loan_type", entity.getLoantype().getTypeLoan());
                    }
//           
                    if (entity.getEmployee().getIdEmployee() == null) {
                        jsonobj.put("id_employee", "");
                    } else {
                        jsonobj.put("id_employee", entity.getEmployee().getIdEmployee());
                    }
                    if (entity.getEmployee().getEmployeeId() == null) {
                        jsonobj.put("employee_id", "");
                    } else {
                        jsonobj.put("employee_id", entity.getEmployee().getEmployeeId());
                    }
                    if (entity.getEmployee().getNik() == null) {
                        jsonobj.put("nik", "");
                    } else {
                        jsonobj.put("nik", entity.getEmployee().getNik());
                    }
                    if (entity.getEmployee().getNpwp() == null) {
                        jsonobj.put("npwp", "");
                    } else {
                        jsonobj.put("npwp", entity.getEmployee().getNpwp());
                    }
                    if (entity.getEmployee().getName() == null) {
                        jsonobj.put("nama", "");
                    } else {
                        jsonobj.put("nama", entity.getEmployee().getName());
                    }
                    if (entity.getStatus() == null) {
                        jsonobj.put("status_loan", "");
                    } else {
                        jsonobj.put("status_loan", entity.getStatus());
                    }
                    array.put(jsonobj);
                }
                return ResponseEntity.ok(array.toString());
            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan Data Null ");
                CreateLog.createJson(rs, "disbursementsFinance-loanA");
                return new ResponseEntity(new CustomErrorType("55", "Error", "Loan Data Null"),
                        HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException | org.json.JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "disbursementsFinance-loanA");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/disbursements/loan-b", method = RequestMethod.GET, produces = {"application/json"})//produces = {"application/json"}
    @XxsFilter
    public ResponseEntity<?> disbursementsBFinance(Authentication authentication) {
        try {
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "disbursementsFinance-loanB");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "disbursementsFinance-loanB");
                process = false;

            }
            if (!dataEMploye.getRoleName().contains("finance")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("YOur Role can't acces this feature : ");
                CreateLog.createJson(rs, "disbursementsFinance-loanB");
                process = false;

            }
            if (process) {
                List<Loan> listLoanDisburse = disbursementService.viewDisburseByFinance(dataEMploye.getIdEmployee().toString(), "b");
                JSONArray array = new JSONArray();
                for (int i = 0; i < listLoanDisburse.size(); i++) {
                    JSONObject jsonobj = new JSONObject();
                    Loan entity = (Loan) listLoanDisburse.get(i);
                    if (entity.getId() == null) {
                        jsonobj.put("id_loan", "");
                    } else {
                        jsonobj.put("id_loan", entity.getId());
                    }
                    if (entity.getLoanId() == null) {
                        jsonobj.put("loan_id", "");
                    } else {
                        jsonobj.put("loan_id", entity.getLoanId());
                    }
                    if (entity.getLoanAmount() == null) {
                        jsonobj.put("amount", "");
                    } else {
                        jsonobj.put("amount", String.format("%.0f", entity.getLoanAmount()));
                    }
                    if (entity.getAprovedByAdmin() == null) {
                        jsonobj.put("aproved_by_admin", "");
                    } else {
                        Employee dataAdmin = this.employeeService.findById(Long.parseLong(entity.getAprovedByAdmin()));
                        jsonobj.put("aproved_by_admin", dataAdmin.getEmployeeId());
                    }
                    if (entity.getDate_approved() == null) {
                        jsonobj.put("date_approve_by_admin", "");
                    } else {
                        jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getDate_approved()));
                    }
                    if (entity.getAprovedByFinance() == null) {
                        jsonobj.put("disburse_by_finance", "");
                    } else {
                        Employee dataFinance = this.employeeService.findById(Long.parseLong(entity.getAprovedByFinance()));
                        jsonobj.put("disburse_by_finance", dataFinance.getEmployeeId());
                    }
                    if (entity.getDate_created() == null) {
                        jsonobj.put("date_created", "");
                    } else {
                        jsonobj.put("date_created", dateFormat.format(entity.getDate_created()));
                    }
                    if (entity.getDate_approved_by_finance() == null) {
                        jsonobj.put("date_disburse_by_finance", "");
                    } else {
                        jsonobj.put("date_disburse_by_finance", dateFormat.format(entity.getDate_approved_by_finance()));
                    }
                    if (entity.getLoantype().getTypeLoan() == null) {
                        jsonobj.put("loan_type", "");
                    } else {
                        jsonobj.put("loan_type", entity.getLoantype().getTypeLoan());
                    }
//           
                    if (entity.getEmployee().getIdEmployee() == null) {
                        jsonobj.put("id_employee", "");
                    } else {
                        jsonobj.put("id_employee", entity.getEmployee().getIdEmployee());
                    }
                    if (entity.getEmployee().getEmployeeId() == null) {
                        jsonobj.put("employee_id", "");
                    } else {
                        jsonobj.put("employee_id", entity.getEmployee().getEmployeeId());
                    }
                    if (entity.getEmployee().getNik() == null) {
                        jsonobj.put("nik", "");
                    } else {
                        jsonobj.put("nik", entity.getEmployee().getNik());
                    }
                    if (entity.getEmployee().getNpwp() == null) {
                        jsonobj.put("npwp", "");
                    } else {
                        jsonobj.put("npwp", entity.getEmployee().getNpwp());
                    }
                    if (entity.getEmployee().getName() == null) {
                        jsonobj.put("nama", "");
                    } else {
                        jsonobj.put("nama", entity.getEmployee().getName());
                    }
                    if (entity.getStatus() == null) {
                        jsonobj.put("status_loan", "");
                    } else {
                        jsonobj.put("status_loan", entity.getStatus());
                    }
                    array.put(jsonobj);
                }
                return ResponseEntity.ok(array.toString());
            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan Data Null ");
                CreateLog.createJson(rs, "disbursementsFinance-loanB");
                return new ResponseEntity(new CustomErrorType("55", "Error", "Loan Data Null"),
                        HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException | org.json.JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "disbursementsFinance-loanB");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/disbursement/{id_loan}", method = RequestMethod.GET, produces = {"application/json"})//produces = {"application/json"}
    @XxsFilter
    public ResponseEntity<?> viewDisbursementsbyLoan(Authentication authentication, @PathVariable("id_loan") Long id_loan) {
        try {
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "view-disbursements-by-loan");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "view-disbursements-by-loan");
                process = false;

            }
            if (process) {
//                List<Loan> listLoanDisburse = disbursementService.listDisburse("a");
//                JSONArray array = new JSONArray();

                Loan entity = loanService.findById(id_loan);

                JSONObject jsonobj = new JSONObject();
                if (entity.getId() == null) {
                    jsonobj.put("id_loan", "");
                } else {
                    jsonobj.put("id_loan", entity.getId());
                }
                if (entity.getLoanId() == null) {
                    jsonobj.put("loan_id", "");
                } else {
                    jsonobj.put("loan_id", entity.getLoanId());
                }
                if (entity.getLoanAmount() == null) {
                    jsonobj.put("amount", "");
                } else {
                    jsonobj.put("amount", String.format("%.0f", entity.getLoanAmount()));
                }
                if (entity.getAprovedByAdmin() == null) {
                    jsonobj.put("aproved_by_admin", "");
                } else {
                    Employee dataAdmin = this.employeeService.findById(Long.parseLong(entity.getAprovedByAdmin()));
                    jsonobj.put("aproved_by_admin", dataAdmin.getEmployeeId());
                }
                if (entity.getDate_approved() == null) {
                    jsonobj.put("date_approve_by_admin", "");
                } else {
                    jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getDate_approved()));
                }
                if (entity.getAprovedByFinance() == null) {
                    jsonobj.put("disburse_by_finance", "");
                } else {
                    Employee dataFinance = this.employeeService.findById(Long.parseLong(entity.getAprovedByFinance()));
                    jsonobj.put("disburse_by_finance", dataFinance.getEmployeeId());
                }
                if (entity.getDate_created() == null) {
                    jsonobj.put("date_created", "");
                } else {
                    jsonobj.put("date_created", dateFormat.format(entity.getDate_created()));
                }
                if (entity.getDate_approved_by_finance() == null) {
                    jsonobj.put("date_disburse_by_finance", "");
                } else {
                    jsonobj.put("date_disburse_by_finance", dateFormat.format(entity.getDate_approved_by_finance()));
                }
                if (entity.getLoantype().getTypeLoan() == null) {
                    jsonobj.put("loan_type", "");
                } else {
                    jsonobj.put("loan_type", entity.getLoantype().getTypeLoan());
                }
//           
                if (entity.getEmployee().getIdEmployee() == null) {
                    jsonobj.put("id_employee", "");
                } else {
                    jsonobj.put("id_employee", entity.getEmployee().getIdEmployee());
                }
                if (entity.getEmployee().getEmployeeId() == null) {
                    jsonobj.put("employee_id", "");
                } else {
                    jsonobj.put("employee_id", entity.getEmployee().getEmployeeId());
                }
                if (entity.getEmployee().getNik() == null) {
                    jsonobj.put("nik", "");
                } else {
                    jsonobj.put("nik", entity.getEmployee().getNik());
                }
                if (entity.getEmployee().getNpwp() == null) {
                    jsonobj.put("npwp", "");
                } else {
                    jsonobj.put("npwp", entity.getEmployee().getNpwp());
                }
                if (entity.getEmployee().getName() == null) {
                    jsonobj.put("nama", "");
                } else {
                    jsonobj.put("nama", entity.getEmployee().getName());
                }
                if (entity.getStatus() == null) {
                    jsonobj.put("status_loan", "");
                } else {
                    jsonobj.put("status_loan", entity.getStatus());
                }
                return ResponseEntity.ok(jsonobj.toString());
            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan Data Null ");
                CreateLog.createJson(rs, "view-disbursements-by-loan");
                return new ResponseEntity(new CustomErrorType("55", "Error", "Loan Data Null"),
                        HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException | org.json.JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "view-disbursements-by-loan");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

//    @RequestMapping(value = "/disbursements/loan-b", method = RequestMethod.GET, produces = {"application/json"})//produces = {"application/json"}
//    @XxsFilter
//    public ResponseEntity<?> disbursementsLoanB(Authentication authentication) {
//        try {
//            Boolean process = true;
//            String name = authentication.getName();
//            log.info("name : " + name);
//            Employee entityEmp = employeeService.findByEmployee(name);
//            log.info("entity : " + entityEmp);
//            if (entityEmp == null) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("can't acces this feature :");
//                CreateLog.createJson(rs, "disbursementsLoanB");
//                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
//                        HttpStatus.NOT_FOUND);
//            }
//            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
//            if (dataEMploye == null) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("Employee Id Not found");
//                CreateLog.createJson(rs, "disbursementsLoanB");
//                process = false;
//
//            }
//
//            if (process) {
//                List<Loan> listLoanDisburse = disbursementService.listDisburse("b");
//                JSONArray array = new JSONArray();
//                for (int i = 0; i < listLoanDisburse.size(); i++) {
//                    JSONObject jsonobj = new JSONObject();
//                    Loan entity = (Loan) listLoanDisburse.get(i);
//                    if (entity.getId() == null) {
//                        jsonobj.put("id_loan", "");
//                    } else {
//                        jsonobj.put("id_loan", entity.getId());
//                    }
//                    if (entity.getLoanId() == null) {
//                        jsonobj.put("loan_id", "");
//                    } else {
//                        jsonobj.put("loan_id", entity.getLoanId());
//                    }
//                    if (entity.getLoanAmount() == null) {
//                        jsonobj.put("amount", "");
//                    } else {
//                        jsonobj.put("amount", String.format("%.0f", entity.getLoanAmount()));
//                    }
//                    if (entity.getAprovedByAdmin() == null) {
//                        jsonobj.put("aproved_by_admin", "");
//                    } else {
//                        Employee dataAdmin = this.employeeService.findById(Long.parseLong(entity.getAprovedByAdmin()));
//                        jsonobj.put("aproved_by_admin", dataAdmin.getEmployeeId());
//                    }
//                    if (entity.getDate_approved() == null) {
//                        jsonobj.put("date_approve_by_admin", "");
//                    } else {
//                        jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getDate_approved()));
//                    }
//                    if (entity.getAprovedByFinance() == null) {
//                        jsonobj.put("disburse_by_finance", "");
//                    } else {
//                        Employee dataFinance = this.employeeService.findById(Long.parseLong(entity.getAprovedByFinance()));
//                        jsonobj.put("disburse_by_finance", dataFinance.getEmployeeId());
//                    }
//                    if (entity.getDate_created() == null) {
//                        jsonobj.put("date_created", "");
//                    } else {
//                        jsonobj.put("date_created", dateFormat.format(entity.getDate_created()));
//                    }
//                    if (entity.getDate_approved_by_finance() == null) {
//                        jsonobj.put("date_disburse_by_finance", "");
//                    } else {
//                        jsonobj.put("date_disburse_by_finance", dateFormat.format(entity.getDate_approved_by_finance()));
//                    }
//                    if (entity.getLoantype().getTypeLoan() == null) {
//                        jsonobj.put("loan_type", "");
//                    } else {
//                        jsonobj.put("loan_type", entity.getLoantype().getTypeLoan());
//                    }
////           
//                    if (entity.getEmployee().getIdEmployee() == null) {
//                        jsonobj.put("id_employee", "");
//                    } else {
//                        jsonobj.put("id_employee", entity.getEmployee().getIdEmployee());
//                    }
//                    if (entity.getEmployee().getEmployeeId() == null) {
//                        jsonobj.put("employee_id", "");
//                    } else {
//                        jsonobj.put("employee_id", entity.getEmployee().getEmployeeId());
//                    }
//                    if (entity.getEmployee().getNik() == null) {
//                        jsonobj.put("nik", "");
//                    } else {
//                        jsonobj.put("nik", entity.getEmployee().getNik());
//                    }
//                    if (entity.getEmployee().getNpwp() == null) {
//                        jsonobj.put("npwp", "");
//                    } else {
//                        jsonobj.put("npwp", entity.getEmployee().getNpwp());
//                    }
//                    if (entity.getEmployee().getName() == null) {
//                        jsonobj.put("nama", "");
//                    } else {
//                        jsonobj.put("nama", entity.getEmployee().getName());
//                    }
//                    if (entity.getStatus() == null) {
//                        jsonobj.put("status_loan", "");
//                    } else {
//                        jsonobj.put("status_loan", entity.getStatus());
//                    }
//                    array.put(jsonobj);
//                }
//                return ResponseEntity.ok(array.toString());
//            } else {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("Loan Data Null ");
//                CreateLog.createJson(rs, "disbursementsLoanB");
//                return new ResponseEntity(new CustomErrorType("55", "Error", "Loan Data Null"),
//                        HttpStatus.NOT_FOUND);
//            }
//        } catch (NumberFormatException | org.json.JSONException ex) {
//            // TODO Auto-generated catch block
////            e.printStackTrace();
//            CreateLog.createJson(ex.getMessage(), "disbursementsLoanB");
//            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
//                    HttpStatus.NOT_FOUND);
//        }
//
//    }
}
