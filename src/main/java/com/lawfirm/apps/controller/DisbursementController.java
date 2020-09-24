/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.Disbursement;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.EntityPTKP;
import com.lawfirm.apps.model.Financial;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.model.LoanHistory;
import com.lawfirm.apps.model.LoanType;
import com.lawfirm.apps.model.Member;
import com.lawfirm.apps.model.OutStandingLoanB;
import com.lawfirm.apps.model.TeamMember;
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
import com.lawfirm.apps.service.interfaces.PtkpServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import com.lawfirm.apps.support.api.DisbursementCaseIdDto;
import com.lawfirm.apps.support.api.LoanApi;
import com.lawfirm.apps.utils.CreateLog;
import com.lawfirm.apps.utils.CustomErrorType;
import com.lawfirm.apps.utils.Util;
import com.sun.tools.sjavac.Log;
import com.xss.filter.annotation.XxsFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
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
import com.lawfirm.apps.service.interfaces.OutStandingLoanBServiceIface;

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
    SimpleDateFormat sdfDisburse;

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
    @Autowired
    OutStandingLoanBServiceIface outStandingLoanBService;
    @Autowired
    MemberServiceIface memberService;
    @Autowired
    PtkpServiceIface ptkpService;

    public DisbursementController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yy");
        this.sdfMonth = new SimpleDateFormat("MM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
        this.sdfDisbursM = new SimpleDateFormat("MMM");
        this.sdfDisbursMY = new SimpleDateFormat("MMMyyyy");
        this.sdfDisburse = new SimpleDateFormat("dd-MMMM-yyyy");
    }

    @RequestMapping(value = "/disbursements/loans", method = RequestMethod.GET, produces = {"application/json"})//produces = {"application/json"}
    @XxsFilter
    public ResponseEntity<?> disbursementsLoans(Authentication authentication) {
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
                CreateLog.createJson(rs, "disbursementsLoans");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "disbursementsLoans");
                process = false;

            }
            if (process) {
                List<Loan> listLoanDisburse = disbursementService.listDisburseByloan("0");
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
                CreateLog.createJson(rs, "disbursementsLoans");
                return new ResponseEntity(new CustomErrorType("55", "Error", "Loan Data Null"),
                        HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException | org.json.JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "disbursementsLoans");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/disburse/{id_loan}/loan-a", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response disburseLoanA(@RequestBody final LoanApi object, @PathVariable("id_loan") Long id_loan, Authentication authentication) {
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
            OutStandingLoanB outStanding = new OutStandingLoanB();
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
                CreateLog.createJson(rs, "disburseLoanA");
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
                CreateLog.createJson(rs, "disburseLoanA");
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
                CreateLog.createJson(rs, "disburseLoanA");
                process = false;
            }
            if (dataLoan.getStatus().contentEquals("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan apps are rejected");
                CreateLog.createJson(rs, "disburseLoanA");
                process = false;
            }
            if (dataLoan.getIsActive().contentEquals("1")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan apps Must approve by ADMIN");
                CreateLog.createJson(rs, "disburseLoanA");
                process = false;
            }
            if (dataLoan.getIsActive().contentEquals("4")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Already approved by finance ");
                CreateLog.createJson(rs, "disburseLoanA");
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
//                outStanding.setLoan(dataLoan);
//                outStanding.setTahun_input(date_now);
//                outStanding.setUserId(dataLoan.getEmployee().getIdEmployee());
//                this.OutStandingService.create(outStanding);
                this.loanHistoryService.create(entityHistory);
                if (upDataLoan != null) {
                    dataFinance.setDisburse_date(new Date());
                    if (object.getOut_standing() != null) {
                        dataFinance.setOutStanding(object.getOut_standing());
                    } else {
                        rs.setResponse_code("55");
                        rs.setInfo("Failed");
                        rs.setResponse("out Standing Field can't be null");
                        CreateLog.createJson(rs, "disburseLoanA");
                    }
                    Financial upd_finance = financialService.update(dataFinance);
                    if (upd_finance != null) {
                        rs.setResponse_code("01");
                        rs.setInfo("Success");
                        rs.setResponse("Loan apps Approved By :" + dataEMploye.getEmployeeId());//dataLoan.getAprovedByFinance()
                        CreateLog.createJson(rs, "disburseLoanA");
                        return rs;
                    }
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("Loand id null, Cannot Access This feature");
                    CreateLog.createJson(rs, "disburseLoanA");
                    return rs;
                } else {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("Loand id null, Cannot Access This feature");
                    CreateLog.createJson(rs, "disburseLoanA");
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
            CreateLog.createJson(rs, "disburseLoanA");
            CreateLog.createJson(ex.getMessage(), "disburseLoanA");
            return rs;

        } catch (ParseException ex) {
            Logger.getLogger(LoanController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "disburseLoanA");
            CreateLog.createJson(ex.getMessage(), "disburseLoanA");
            return rs;
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/disburse/{id_loan}/loan-b", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response disburseLoanB(@RequestBody final LoanApi object, @PathVariable("id_loan") Long id_loan, Authentication authentication) {
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
            OutStandingLoanB outStanding = new OutStandingLoanB();
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
                CreateLog.createJson(rs, "disburseLoanB");
//                process = false;
                return rs;
            }
            if (!entityEmp.getRoleName().contentEquals("finance")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature : " + entityEmp.getRoleName().toUpperCase());
                CreateLog.createJson(rs, "disburseLoanB");
//                process = false;
                return rs;
            }
//             Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());

            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "disburseLoanB");
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
                CreateLog.createJson(rs, "disburseLoanB");
                process = false;
            }
            if (dataLoan.getStatus().contentEquals("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan apps are rejected");
                CreateLog.createJson(rs, "disburseLoanB");
                process = false;
            }
            if (dataLoan.getIsActive().contentEquals("1")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan apps Must approve by ADMIN");
                CreateLog.createJson(rs, "disburseLoanB");
                process = false;
            }
            if (dataLoan.getIsActive().contentEquals("4")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Already approved by finance ");
                CreateLog.createJson(rs, "disburseLoanB");
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
//                OutStandingLoanB checkLoan = this.outStandingLoanBService.checkLoan(dataLoan.getEngagement().getCaseID(), dataLoan.getEmployee().getIdEmployee());
//                OutStandingLoanB checkLoan = this.outStandingLoanBService.findByCaseId(dataLoan.getEngagement().getCaseID());
//                log.info("checkLoan : " + checkLoan);
//                if (checkLoan == null) {
//                    outStanding.setLoan(dataLoan);
//                    outStanding.setTahun_input(date_now);
//                    outStanding.setLoanAmount(dataLoan.getLoanAmount());
//                    outStanding.setUserId(dataLoan.getEmployee().getIdEmployee());
//                    this.OutStandingService.create(outStanding);
//                } else {
//                Double newAmount = 0d;
//                newAmount = (checkLoan.getLoanAmount() + outStanding.getLoanAmount());
//                checkLoan.setLoanAmount(newAmount);
//                this.OutStandingService.update(checkLoan);
//                }
                this.loanHistoryService.create(entityHistory);

                if (upDataLoan != null) {
                    dataFinance.setDisburse_date(new Date());
                    if (object.getOut_standing() != null) {
                        dataFinance.setOutStanding(object.getOut_standing());
                    } else {
                        rs.setResponse_code("55");
                        rs.setInfo("Failed");
                        rs.setResponse("out Standing Field can't be null");
                        CreateLog.createJson(rs, "disburseLoanB");
                    }
                    Financial upd_finance = financialService.update(dataFinance);
                    if (upd_finance != null) {
                        rs.setResponse_code("01");
                        rs.setInfo("Success");
                        rs.setResponse("Loan apps Approved By :" + dataEMploye.getEmployeeId());//dataLoan.getAprovedByFinance()
                        CreateLog.createJson(rs, "disburseLoanB");
                        return rs;
                    }
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("Loand id null, Cannot Access This feature");
                    CreateLog.createJson(rs, "disburseLoanB");
                    return rs;
                } else {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("Loand id null, Cannot Access This feature");
                    CreateLog.createJson(rs, "disburseLoanB");
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
            CreateLog.createJson(rs, "disburseLoanB");
            CreateLog.createJson(ex.getMessage(), "disburseLoanB");
            return rs;

        } catch (ParseException ex) {
            Logger.getLogger(LoanController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "disburseLoanB");
            CreateLog.createJson(ex.getMessage(), "disburseLoanB");
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

//    @RequestMapping(value = "/disbursement/case-id", method = RequestMethod.POST, produces = {"application/json"})
//    @XxsFilter
//    public ResponseEntity<?> disbursementbyCaseId(@RequestBody final DisbursementCaseIdDto object, Authentication authentication) {
//        try {
//            Date now = new Date();
//            Date dateDisburs = new Date();
//            Boolean process = true;
//            String name = authentication.getName();
//            log.info("name : " + name);
//            Employee entityEmp = employeeService.findByEmployee(name);
//            log.info("entity : " + entityEmp);
//            if (entityEmp == null) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("can't acces this feature :");
//                CreateLog.createJson(rs, "disbursementbyCaseId");
//                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
//                        HttpStatus.NOT_FOUND);
//            }
//            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
//            if (dataEMploye == null) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("Employee Id Not found");
//                CreateLog.createJson(rs, "disbursementbyCaseId");
//                process = false;
//
//            }
//            if (process) {
//                String param = null;
//                log.info("case_id : " + object.getCase_id());
//                List<Loan> loanlist = disbursementService.disbursementbyCaseId(object.getCase_id());
//                JSONArray array = new JSONArray();
//                log.info("loanlist : " + loanlist.size());
//                if (loanlist.size() > 0) {
//                    for (int d = 0; d < loanlist.size(); d++) {
//                        Loan dataLoan = loanlist.get(d);
//
//                        JSONObject obj = new JSONObject();
////                        JSONObject objMember = new JSONObject();
//                        JSONArray arrayM = new JSONArray();
//                        Long EngagementId = 0l;
//                        Long idLoanB = 0l;
//                        Long id_team = 0l;
//                        Double dmpPortion = 0d;
//                        if (dataLoan.getEngagement() == null) {
//                            obj.put("case_id", "");
//                        } else {
//                            obj.put("case_id", object.getCase_id());// disbursements.getLoan().getEngagement().getCaseID()
//                            EngagementId = dataLoan.getEngagement().getEngagementId();
//
//                            idLoanB = dataLoan.getId();
//                            log.info("idLoanB : " + idLoanB);
//                        }
//                        CaseDetails caseDetails = caseDetailsService.findById(EngagementId);
//                        if (caseDetails != null) {
////                        obj.put("professionalFee", caseDetails.getProfesionalFee());
//                            obj.put("dmp_portion_case_id", String.format("%.0f", (caseDetails.getDmpPortion())));
//                            dmpPortion = caseDetails.getDmpPortion();
//                        } else {
////                        obj.put("professionalFee", "");
//                            obj.put("dmp_portion_case_id", "");
//                            dmpPortion = 0d;
//                        }
////                        Double total = loanService.sumLoan(idLoanB);
////                        log.info("total : " + total);
//                        List<TeamMember> entityTeam = teamMemberService.listTeamMemberByEngagement(EngagementId);
//                        if (entityTeam != null) {
//                            for (int j = 0; j < entityTeam.size(); j++) {
////                                JSONObject objTeam = new JSONObject();
//                                TeamMember dataTeam = entityTeam.get(j);
//                                Employee getDmp = employeeService.findById(dataTeam.getDmpId());
//                                if (getDmp == null) {
//                                    obj.put("employee_id_dmp", "");
//                                    obj.put("fee_share_dmp", "");
//                                    obj.put("amount_portion_dmp", "");
//                                    obj.put("masa_kerja_dmp", "");
//                                    obj.put("jabatan_dmp_per_bulan", "");
//                                    obj.put("jabatan_dmp_per_tahun", "");
//                                    obj.put("ptkp_dmp", "");
//                                    obj.put("previous_disbursement_dmp", "");
//                                    obj.put("anual_salary_dmp", "");
//                                } else {
//                                    if (dataTeam.getTeamMemberId() != null) {
//                                        id_team = dataTeam.getTeamMemberId();
//                                    }
//                                    Integer anual_salary_dmp = (4000000 * 13);
//                                    int a_jabatan_dmp = (60000000 * 5) / 100;
//                                    int b_jabatan_dmp = ((anual_salary_dmp * 5) / 100) / 13;
//                                    int masa_kerja_dmp = 0;
//                                    Double amount_portion_dmp = 0d;
//
//                                    Double previous_disbursement_dmp = 0d;
//                                    Double total_income_fortax_purpose_dmp = 0d;
//                                    String dmp_tax_status = getDmp.getTaxStatus();
//                                    Double income_tax_dmp = 0d;
//                                    Double taxable_income_dmp = 0d;
//                                    Double income_tax_paid_on_prior_period_dmp = 0d;
//                                    Double net_income_tax_deducted_dmp = 0d;
//                                    Double net_income_dmp = 0d;
//                                    Double outstanding_loan_b_dmp = 0d;
//                                    Double disbursable_amount_dmp = 0d;
//                                    Double outstanding_loan_a_dmp = 0d;
//                                    Double disbursed_amount_dmp = 0d;
//                                    Double ptkp = 0d;
//                                    log.info("dmp_tax_status : " + dmp_tax_status);
////                                    String jabatan_perbulan_dmp = String.format("%d", (((4000000 * 13) * 5) / 100) / 13);
//                                    EntityPTKP ptkpDmp = this.ptkpService.findPTKPByTaxStatus(dmp_tax_status);
//                                    log.info("ptkpDmp : " + ptkpDmp);
//                                    if (ptkpDmp != null) {
//                                        ptkp = ptkpDmp.getPtkp();
//                                    }
//                                    obj.put("employee_id_dmp", getDmp.getEmployeeId());
//                                    obj.put("fee_share_dmp", dataTeam.getFeeShare());
//                                    amount_portion_dmp = (dmpPortion * dataTeam.getFeeShare()) / 100;
//                                    obj.put("amount_portion_dmp", String.format("%.0f", amount_portion_dmp));
//                                    obj.put("previous_disbursement_dmp", previous_disbursement_dmp);
//
//                                    obj.put("anual_salary_dmp", String.format("%d", anual_salary_dmp));
//                                    total_income_fortax_purpose_dmp = amount_portion_dmp + previous_disbursement_dmp + anual_salary_dmp;
//                                    obj.put("total_income_fortax_purpose_dmp", String.format("%.0f", total_income_fortax_purpose_dmp));
//                                    masa_kerja_dmp = Util.hitungMasakerja(getDmp.getDateRegister());
//                                    obj.put("masa_kerja_dmp", masa_kerja_dmp);
//                                    String jabatan_per_bulan_dmp = String.format("%d", Math.min(a_jabatan_dmp, b_jabatan_dmp));
//                                    obj.put("jabatan_per_bulan_dmp", jabatan_per_bulan_dmp);
//                                    Integer jabatan_per_tahun_dmp = (masa_kerja_dmp * Integer.parseInt(jabatan_per_bulan_dmp));
//                                    obj.put("jabatan_per_tahun_dmp", String.format("%d", jabatan_per_tahun_dmp));
//                                    obj.put("tax_status_dmp", dmp_tax_status);
//                                    obj.put("ptkp_dmp", String.format("%.0f", ptkp));
//                                    taxable_income_dmp = (total_income_fortax_purpose_dmp - jabatan_per_tahun_dmp - ptkp);
//                                    obj.put("taxable_income_dmp", String.format("%.0f", Math.max(taxable_income_dmp, 0)));
//                                    income_tax_dmp = Util.hitungPajak(taxable_income_dmp);
//                                    obj.put("income_tax_dmp", String.format("%.0f", income_tax_dmp));
//                                    income_tax_paid_on_prior_period_dmp = 0d;
//                                    obj.put("income_tax_paid_on_prior_period_dmp", String.format("%.0f", income_tax_paid_on_prior_period_dmp));
//                                    net_income_tax_deducted_dmp = (income_tax_dmp - income_tax_paid_on_prior_period_dmp);
//                                    obj.put("net_income_tax_deducted_dmp", String.format("%.0f", net_income_tax_deducted_dmp));
//                                    net_income_dmp = (amount_portion_dmp - net_income_tax_deducted_dmp);
//                                    obj.put("net_income_dmp", String.format("%.0f", net_income_dmp));
//                                    outstanding_loan_b_dmp = 0d;
//                                    obj.put("outstanding_loan_b_dmp", String.format("%.0f", outstanding_loan_b_dmp));
//                                    disbursable_amount_dmp = (net_income_dmp - outstanding_loan_b_dmp);
//                                    obj.put("disbursable_amount_dmp", String.format("%.0f", Math.abs(disbursable_amount_dmp)));
//                                    outstanding_loan_a_dmp = 0d;
//                                    obj.put("outstanding_loan_a_dmp", String.format("%.0f", outstanding_loan_a_dmp));
//                                    disbursed_amount_dmp = (disbursable_amount_dmp - outstanding_loan_a_dmp);
//                                    obj.put("disbursed_amount_dmp", String.format("%.0f", Math.max(disbursed_amount_dmp, 0)));
//
//                                }
//                            }
//                            if (id_team != null) {
//                                List<Member> entityMember = memberService.findByIdTeam(id_team);
//                                log.info("entityMember size : " + entityMember.size());
//                                for (int k = d; k < entityMember.size(); k++) {
//                                    JSONObject objMember = new JSONObject();
//                                    Member dataMember = entityMember.get(k);
//                                    if (dataMember == null) {
//                                        objMember.put("employee_id_team", "");
//                                        objMember.put("fee_share_team", "");
//                                        objMember.put("amount_portion_team", "");
//                                        objMember.put("masa_kerja_team", "");
//                                        objMember.put("jabatan_per_bulan_team", "");
//                                        objMember.put("jabatan_per_tahun_team", "");
//                                        objMember.put("ptkp_team", "");
//                                        objMember.put("previous_disbursement_team", "");
//                                        objMember.put("anual_salary_team", "");
//
//                                    } else {
//                                        Integer anual_salary_team = (4000000 * 13);
//                                        int a_jabatan_team = (60000000 * 5) / 100;
//                                        int b_jabatan_team = ((anual_salary_team * 5) / 100) / 13;
//                                        int masa_kerja_team = 0;
//                                        Double amount_portion_team = 0d;
//                                        Double previous_disbursement_team = 0d;
//
//                                        Double total_income_fortax_purpose_team = 0d;
//                                        String team_tax_status = dataMember.getEmployee().getTaxStatus();
//                                        Double taxable_income_team = 0d;
//                                        Double income_tax_team = 0d;
//                                        Double income_tax_paid_on_prior_period_team = 0d;
//                                        Double net_income_tax_deducted_team = 0d;
//                                        Double net_income_team = 0d;
//                                        Double outstanding_loan_b_team = 0d;
//                                        Double disbursable_amount_team = 0d;
//                                        Double outstanding_loan_a_team = 0d;
//                                        Double disbursed_amount_team = 0d;
//                                        Double ptkp = 0d;
////                                        log.info("team_tax_status : " + team_tax_status);
////                                      String jabatan_perbulan_team = String.format("%d", (((4000000 * 13) * 5) / 100) / 13);
//                                        EntityPTKP ptkpTeam = this.ptkpService.findPTKPByTaxStatus(team_tax_status);
//                                        log.info("ptkpTeam : " + ptkpTeam);
//                                        if (ptkpTeam != null) {
//                                            ptkp = ptkpTeam.getPtkp();
//                                        }
//                                        objMember.put("employee_id_team", dataMember.getEmployee().getEmployeeId());
//                                        objMember.put("fee_share_team", dataMember.getFeeShare());//"fee_share_team
//                                        amount_portion_team = ((dmpPortion * dataMember.getFeeShare()) / 100);
//                                        objMember.put("amount_portion_team", String.format("%.0f", (amount_portion_team)));
//                                        objMember.put("previous_disbursement_team", String.format("%.0f", previous_disbursement_team));
//
//                                        objMember.put("anual_salary_team", String.format("%d", anual_salary_team));
//                                        total_income_fortax_purpose_team = amount_portion_team + previous_disbursement_team + anual_salary_team;
//                                        objMember.put("total_income_fortax_purpose_team", String.format("%.0f", (total_income_fortax_purpose_team)));
//                                        masa_kerja_team = Util.hitungMasakerja(dataMember.getEmployee().getDateRegister());
//                                        objMember.put("masa_kerja_team", masa_kerja_team);
//                                        String jabatan_per_bulan_team = String.format("%d", Math.min(a_jabatan_team, b_jabatan_team));
//                                        Integer jabatan_per_tahun_team = (masa_kerja_team * Integer.parseInt(jabatan_per_bulan_team));
//                                        objMember.put("jabatan_per_bulan_team", jabatan_per_bulan_team);
//                                        objMember.put("jabatan_per_tahun_team", String.format("%d", jabatan_per_tahun_team));
//                                        objMember.put("tax_status_team", team_tax_status);
//                                        objMember.put("ptkp_team", String.format("%.0f", ptkp));
//                                        taxable_income_team = (total_income_fortax_purpose_team - jabatan_per_tahun_team - ptkp);
//                                        objMember.put("taxable_income_team", String.format("%.0f", Math.max(taxable_income_team, 0)));
//                                        income_tax_team = Util.hitungPajak(taxable_income_team);
//                                        objMember.put("income_tax_team", String.format("%.0f", income_tax_team));
//                                        income_tax_paid_on_prior_period_team = 0d;
//                                        objMember.put("income_tax_paid_on_prior_period_team", String.format("%.0f", income_tax_paid_on_prior_period_team));
//                                        net_income_tax_deducted_team = (income_tax_team - income_tax_paid_on_prior_period_team);
//                                        objMember.put("net_income_tax_deducted_team", String.format("%.0f", net_income_tax_deducted_team));
//                                        net_income_team = (amount_portion_team - net_income_tax_deducted_team);
//                                        objMember.put("net_income_team", String.format("%.0f", net_income_team));
//                                        outstanding_loan_b_team = 0d;
//                                        objMember.put("outstanding_loan_b_team", String.format("%.0f", outstanding_loan_b_team));
//                                        disbursable_amount_team = (net_income_team - outstanding_loan_b_team);
//                                        objMember.put("disbursable_amount_team", String.format("%.0f", Math.abs(disbursable_amount_team)));
//                                        outstanding_loan_a_team = 0d;
//                                        objMember.put("outstanding_loan_a_team", String.format("%.0f", outstanding_loan_a_team));
//                                        disbursed_amount_team = (disbursable_amount_team - outstanding_loan_a_team);
//                                        objMember.put("disbursed_amount_team", String.format("%.0f", Math.max(disbursed_amount_team, 0)));
//
//                                    }
//                                    arrayM.put(objMember);
//
//                                }
//                            } else {
////                                objMember.put("member_name", "");
////                                objMember.put("employee_id", "");
////                                objMember.put("fee_share", "");
////                                arrayM.put(objMember);
//                            }
//                            obj.put("members", arrayM);
//                            array.put(obj);
//                        }
//                    }
//                    return ResponseEntity.ok(array.toString());
//                }
//            }
//        } catch (JSONException ex) {
//            // TODO Auto-generated catch block
////            e.printStackTrace();
//            CreateLog.createJson(ex.getMessage(), "disbursementbyCaseId");
//            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
//                    HttpStatus.NOT_FOUND);
//        }
////
//        return null;
//    }
    @RequestMapping(value = "/disbursement/case-id", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> disbursementbyCaseId(@RequestBody final DisbursementCaseIdDto object, Authentication authentication) {
        try {
            Date now = new Date();
            Date dateDisburs = new Date();
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "disbursementbyCaseId");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "disbursementbyCaseId");
                process = false;

            }
            if (process) {
                String param = null;
                log.info("case_id : " + object.getCase_id());
//                List<Disbursement> disbursementlist = disbursementService.disbursementbyCaseId(object.getCase_id());
                Disbursement dataDisbursement = disbursementService.disbursementFindbyCaseId(object.getCase_id());
//                JSONArray array = new JSONArray();s
//                log.info("loanlist : " + disbursementlist.size());
//                if (disbursementlist.size() > 0) {
//                    for (int d = 0; d < disbursementlist.size(); d++) {
//                        Disbursement dataDisbursement = disbursementlist.get(d);

                JSONObject obj = new JSONObject();
//                        JSONObject objMember = new JSONObject();
                JSONArray arrayM = new JSONArray();
                Long EngagementId = 0l;
                Long idLoanB = 0l;
                Long id_team = 0l;
                Double dmpPortion = 0d;
                Double outStandingLoanB = 0d;
                String caseId = null;
                if (dataDisbursement.getDisbursementId() == null) {
                    obj.put("disbursement_id", "");
                } else {
                    obj.put("disbursement_id", dataDisbursement.getDisbursementId());
                }
                if (dataDisbursement.getNumberOfDisbursement() == null) {
                    obj.put("number_of_disbursement", "");
                } else {
                    obj.put("number_of_disbursement", dataDisbursement.getNumberOfDisbursement());
                }
                if (dataDisbursement.getTahunInput() == null) {
                    obj.put("tax_year", "");
                } else {
                    obj.put("tax_year", dataDisbursement.getTahunInput());
                }
                if (dataDisbursement.getDisburse_date() == null) {
                    obj.put("disbursement_date", "");
                } else {
                    obj.put("disbursement_date", sdfDisburse.format(dataDisbursement.getDisburse_date()));
                }
                if (dataDisbursement.getEngagement() == null) {
                    obj.put("case_id", "");
                } else {
                    obj.put("case_id", object.getCase_id());// disbursements.getLoan().getEngagement().getCaseID()
                    EngagementId = dataDisbursement.getEngagement().getEngagementId();
                    caseId = object.getCase_id();
                }
                CaseDetails caseDetails = caseDetailsService.findById(EngagementId);
                if (caseDetails != null) {
//                        obj.put("professionalFee", caseDetails.getProfesionalFee());
                    obj.put("dmp_portion_case_id", String.format("%.0f", (caseDetails.getDmpPortion())));
                    dmpPortion = caseDetails.getDmpPortion();
                } else {
//                        obj.put("professionalFee", "");
                    obj.put("dmp_portion_case_id", "");
                    dmpPortion = 0d;
                }

                OutStandingLoanB outStandingB = this.outStandingLoanBService.findByCaseId(caseId);

                if (outStandingB == null) {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("caseId : " + caseId + " Not found");
                    CreateLog.createJson(rs, "disbursementbyCaseId");
                    process = false;
                }

                outStandingLoanB = outStandingB.getOutStanding();
                obj.put("out_standing_loan_b", String.format("%.0f", outStandingLoanB));

                List<TeamMember> entityTeam = teamMemberService.listTeamMemberByEngagement(EngagementId);
                if (entityTeam != null) {
                    for (int j = 0; j < entityTeam.size(); j++) {
//                                JSONObject objTeam = new JSONObject();
                        TeamMember dataTeam = entityTeam.get(j);
                        Employee getDmp = employeeService.findById(dataTeam.getDmpId());
                        if (getDmp == null) {
                            obj.put("employee_id_dmp", "");
                            obj.put("fee_share_dmp", "");

                            obj.put("amount_portion_dmp", "");
                            obj.put("previous_disbursement_dmp", "");

                            obj.put("anual_salary_dmp", "");
                            obj.put("total_income_fortax_purpose_dmp", "");
                            obj.put("masa_kerja_dmp", "");
                            obj.put("jabatan_per_bulan_dmp", "");
                            obj.put("jabatan_per_tahun_dmp", "");
                            obj.put("tax_status_dmp", "");
                            obj.put("ptkp_dmp", "");
                            obj.put("taxable_income_dmp", "");
                            obj.put("income_tax_dmp", "");
                            obj.put("income_tax_paid_on_prior_period_dmp", "");
                            obj.put("net_income_tax_deducted_dmp", "");
                            obj.put("net_income_dmp", "");
                            obj.put("outstanding_loan_b_dmp", "");
                            obj.put("disbursable_amount_dmp", "");
                            obj.put("outstanding_loan_a_dmp", "");
                            obj.put("disbursed_amount_dmp", "");
                        } else {
                            if (dataTeam.getTeamMemberId() != null) {
                                id_team = dataTeam.getTeamMemberId();
                            }
                            Integer anual_salary_dmp = (4000000 * 13);
                            int a_jabatan_dmp = (60000000 * 5) / 100;
                            int b_jabatan_dmp = ((anual_salary_dmp * 5) / 100) / 13;
                            int masa_kerja_dmp = 0;
                            Double amount_portion_dmp = 0d;

                            Double previous_disbursement_dmp = 0d;
                            Double total_income_fortax_purpose_dmp = 0d;
                            String dmp_tax_status = getDmp.getTaxStatus();
                            Double income_tax_dmp = 0d;
                            Double taxable_income_dmp = 0d;
                            Double income_tax_paid_on_prior_period_dmp = 0d;
                            Double net_income_tax_deducted_dmp = 0d;
                            Double net_income_dmp = 0d;
                            Double outstanding_loan_b_dmp = 0d;
                            Double disbursable_amount_dmp = 0d;
                            Double outstanding_loan_a_dmp = 0d;
                            Double disbursed_amount_dmp = 0d;
                            Double ptkp = 0d;
                            log.info("dmp_tax_status : " + dmp_tax_status);
//                                    String jabatan_perbulan_dmp = String.format("%d", (((4000000 * 13) * 5) / 100) / 13);
                            EntityPTKP ptkpDmp = this.ptkpService.findPTKPByTaxStatus(dmp_tax_status);
                            log.info("ptkpDmp : " + ptkpDmp);
                            if (ptkpDmp != null) {
                                ptkp = ptkpDmp.getPtkp();
                            }
                            obj.put("employee_id_dmp", getDmp.getEmployeeId());
                            obj.put("fee_share_dmp", dataTeam.getFeeShare());
                            amount_portion_dmp = (dmpPortion * dataTeam.getFeeShare()) / 100;
                            obj.put("amount_portion_dmp", String.format("%.0f", amount_portion_dmp));
                            obj.put("previous_disbursement_dmp", previous_disbursement_dmp);

                            obj.put("anual_salary_dmp", String.format("%d", anual_salary_dmp));
                            total_income_fortax_purpose_dmp = amount_portion_dmp + previous_disbursement_dmp + anual_salary_dmp;
                            obj.put("total_income_fortax_purpose_dmp", String.format("%.0f", total_income_fortax_purpose_dmp));
                            masa_kerja_dmp = Util.hitungMasakerja(getDmp.getDateRegister());
                            obj.put("masa_kerja_dmp", masa_kerja_dmp);
                            String jabatan_per_bulan_dmp = String.format("%d", Math.min(a_jabatan_dmp, b_jabatan_dmp));
                            obj.put("jabatan_per_bulan_dmp", jabatan_per_bulan_dmp);
                            Integer jabatan_per_tahun_dmp = (masa_kerja_dmp * Integer.parseInt(jabatan_per_bulan_dmp));
                            obj.put("jabatan_per_tahun_dmp", String.format("%d", jabatan_per_tahun_dmp));
                            obj.put("tax_status_dmp", dmp_tax_status);
                            obj.put("ptkp_dmp", String.format("%.0f", ptkp));
                            taxable_income_dmp = (total_income_fortax_purpose_dmp - jabatan_per_tahun_dmp - ptkp);
                            obj.put("taxable_income_dmp", String.format("%.0f", Math.max(taxable_income_dmp, 0)));
                            income_tax_dmp = Util.hitungPajak(taxable_income_dmp);
                            obj.put("income_tax_dmp", String.format("%.0f", income_tax_dmp));
                            income_tax_paid_on_prior_period_dmp = 0d;
                            obj.put("income_tax_paid_on_prior_period_dmp", String.format("%.0f", income_tax_paid_on_prior_period_dmp));
                            net_income_tax_deducted_dmp = (income_tax_dmp - income_tax_paid_on_prior_period_dmp);
                            obj.put("net_income_tax_deducted_dmp", String.format("%.0f", net_income_tax_deducted_dmp));
                            net_income_dmp = (amount_portion_dmp - net_income_tax_deducted_dmp);
                            obj.put("net_income_dmp", String.format("%.0f", net_income_dmp));
                            outstanding_loan_b_dmp = (outStandingLoanB * dataTeam.getFeeShare()) / 100;;
                            obj.put("outstanding_loan_b_dmp", String.format("%.0f", outstanding_loan_b_dmp));
                            disbursable_amount_dmp = (net_income_dmp - outstanding_loan_b_dmp);
                            obj.put("disbursable_amount_dmp", String.format("%.0f", Math.abs(disbursable_amount_dmp)));
                            outstanding_loan_a_dmp = 0d;
                            obj.put("outstanding_loan_a_dmp", String.format("%.0f", outstanding_loan_a_dmp));
                            disbursed_amount_dmp = (disbursable_amount_dmp - outstanding_loan_a_dmp);
                            obj.put("disbursed_amount_dmp", String.format("%.0f", Math.max(disbursed_amount_dmp, 0)));

                        }
                    }
                    if (id_team != null) {
                        List<Member> entityMember = memberService.findByIdTeam(id_team);
                        log.info("entityMember size : " + entityMember.size());
                        for (int k = 0; k < entityMember.size(); k++) {
                            JSONObject objMember = new JSONObject();
                            Member dataMember = entityMember.get(k);
                            if (dataMember == null) {
                                objMember.put("employee_id_team", "");
                                objMember.put("fee_share_team", "");//"fee_share_team
                                objMember.put("amount_portion_team", "");
                                objMember.put("previous_disbursement_team", "");
                                objMember.put("anual_salary_team", "");
                                objMember.put("total_income_fortax_purpose_team", "");
                                objMember.put("masa_kerja_team", "");
                                objMember.put("jabatan_per_bulan_team", "");
                                objMember.put("jabatan_per_tahun_team", "");
                                objMember.put("tax_status_team", "");
                                objMember.put("ptkp_team", "");
                                objMember.put("taxable_income_team", "");
                                objMember.put("income_tax_team", "");
                                objMember.put("income_tax_paid_on_prior_period_team", "");
                                objMember.put("net_income_tax_deducted_team", "");
                                objMember.put("net_income_team", "");
                                objMember.put("outstanding_loan_b_team", "");
                                objMember.put("disbursable_amount_team", "");
                                objMember.put("outstanding_loan_a_team", "");
                                objMember.put("disbursed_amount_team", "");

                            } else {
                                Integer anual_salary_team = (4000000 * 13);
                                int a_jabatan_team = (60000000 * 5) / 100;
                                int b_jabatan_team = ((anual_salary_team * 5) / 100) / 13;
                                int masa_kerja_team = 0;
                                Double amount_portion_team = 0d;
                                Double previous_disbursement_team = 0d;

                                Double total_income_fortax_purpose_team = 0d;
                                String team_tax_status = dataMember.getEmployee().getTaxStatus();
                                Double taxable_income_team = 0d;
                                Double income_tax_team = 0d;
                                Double income_tax_paid_on_prior_period_team = 0d;
                                Double net_income_tax_deducted_team = 0d;
                                Double net_income_team = 0d;
                                Double outstanding_loan_b_team = 0d;
                                Double disbursable_amount_team = 0d;
                                Double outstanding_loan_a_team = 0d;
                                Double disbursed_amount_team = 0d;
                                Double ptkp = 0d;
//                                        log.info("team_tax_status : " + team_tax_status);
//                                      String jabatan_perbulan_team = String.format("%d", (((4000000 * 13) * 5) / 100) / 13);
                                EntityPTKP ptkpTeam = this.ptkpService.findPTKPByTaxStatus(team_tax_status);
                                log.info("ptkpTeam : " + ptkpTeam);
                                if (ptkpTeam != null) {
                                    ptkp = ptkpTeam.getPtkp();
                                }
                                objMember.put("employee_id_team", dataMember.getEmployee().getEmployeeId());
                                objMember.put("fee_share_team", dataMember.getFeeShare());//"fee_share_team
                                amount_portion_team = ((dmpPortion * dataMember.getFeeShare()) / 100);
                                objMember.put("amount_portion_team", String.format("%.0f", (amount_portion_team)));
                                objMember.put("previous_disbursement_team", String.format("%.0f", previous_disbursement_team));

                                objMember.put("anual_salary_team", String.format("%d", anual_salary_team));
                                total_income_fortax_purpose_team = amount_portion_team + previous_disbursement_team + anual_salary_team;
                                objMember.put("total_income_fortax_purpose_team", String.format("%.0f", (total_income_fortax_purpose_team)));
                                masa_kerja_team = Util.hitungMasakerja(dataMember.getEmployee().getDateRegister());
                                objMember.put("masa_kerja_team", masa_kerja_team);
                                String jabatan_per_bulan_team = String.format("%d", Math.min(a_jabatan_team, b_jabatan_team));
                                Integer jabatan_per_tahun_team = (masa_kerja_team * Integer.parseInt(jabatan_per_bulan_team));
                                objMember.put("jabatan_per_bulan_team", jabatan_per_bulan_team);
                                objMember.put("jabatan_per_tahun_team", String.format("%d", jabatan_per_tahun_team));
                                objMember.put("tax_status_team", team_tax_status);
                                objMember.put("ptkp_team", String.format("%.0f", ptkp));
                                taxable_income_team = (total_income_fortax_purpose_team - jabatan_per_tahun_team - ptkp);
                                objMember.put("taxable_income_team", String.format("%.0f", Math.max(taxable_income_team, 0)));
                                income_tax_team = Util.hitungPajak(taxable_income_team);
                                objMember.put("income_tax_team", String.format("%.0f", income_tax_team));
                                income_tax_paid_on_prior_period_team = 0d;
                                objMember.put("income_tax_paid_on_prior_period_team", String.format("%.0f", income_tax_paid_on_prior_period_team));
                                net_income_tax_deducted_team = (income_tax_team - income_tax_paid_on_prior_period_team);
                                objMember.put("net_income_tax_deducted_team", String.format("%.0f", net_income_tax_deducted_team));
                                net_income_team = (amount_portion_team - net_income_tax_deducted_team);
                                objMember.put("net_income_team", String.format("%.0f", net_income_team));
                                outstanding_loan_b_team = (outStandingLoanB * dataMember.getFeeShare()) / 100;;
                                objMember.put("outstanding_loan_b_team", String.format("%.0f", outstanding_loan_b_team));
                                disbursable_amount_team = (net_income_team - outstanding_loan_b_team);
                                objMember.put("disbursable_amount_team", String.format("%.0f", Math.abs(disbursable_amount_team)));
                                outstanding_loan_a_team = 0d;
                                objMember.put("outstanding_loan_a_team", String.format("%.0f", outstanding_loan_a_team));
                                disbursed_amount_team = (disbursable_amount_team - outstanding_loan_a_team);
                                objMember.put("disbursed_amount_team", String.format("%.0f", Math.max(disbursed_amount_team, 0)));

                            }
                            arrayM.put(objMember);

                        }
                    } else {
//                                objMember.put("member_name", "");
//                                objMember.put("employee_id", "");
//                                objMember.put("fee_share", "");
//                                arrayM.put(objMember);
                    }
                    obj.put("members", arrayM);
//                    array.put(obj);
                    return ResponseEntity.ok(obj.toString());
                }
            }
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "disbursementbyCaseId");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//
        return null;
    }

    @RequestMapping(value = "/disbursements", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> disbursements(Authentication authentication) {
        try {
            Date now = new Date();
            Date dateDisburs = new Date();
            Boolean process = true;
            String name = authentication.getName();
            Double outStandingLoanB = 0d;
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "disbursementbyCaseId");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "disbursementbyCaseId");
                process = false;

            }
            if (process) {
                List<Disbursement> listDisbursements = disbursementService.listDisburse();
                JSONArray array = new JSONArray();
                log.info("loanlist : " + listDisbursements.size());
                if (listDisbursements.size() > 0) {
                    for (int d = 0; d < listDisbursements.size(); d++) {
                        Disbursement dataDisbursement = listDisbursements.get(d);

                        JSONObject obj = new JSONObject();
//                        JSONObject objMember = new JSONObject();
                        JSONArray arrayM = new JSONArray();
                        Long EngagementId = 0l;
                        String caseId = null;
                        Long idLoanB = 0l;
                        Long id_team = 0l;
                        Double dmpPortion = 0d;
                        if (dataDisbursement.getDisbursementId() == null) {
                            obj.put("disbursement_id", "");
                        } else {
                            obj.put("disbursement_id", dataDisbursement.getDisbursementId());
                        }
                        if (dataDisbursement.getNumberOfDisbursement() == null) {
                            obj.put("number_of_disbursement", "");
                        } else {
                            obj.put("number_of_disbursement", dataDisbursement.getNumberOfDisbursement());
                        }
                        if (dataDisbursement.getTahunInput() == null) {
                            obj.put("tax_year", "");
                        } else {
                            obj.put("tax_year", dataDisbursement.getTahunInput());
                        }
                        if (dataDisbursement.getDisburse_date() == null) {
                            obj.put("disbursement_date", "");
                        } else {
                            obj.put("disbursement_date", sdfDisburse.format(dataDisbursement.getDisburse_date()));
                        }
                        if (dataDisbursement.getEngagement() == null) {
                            obj.put("case_id", "");
                        } else {
                            obj.put("case_id", dataDisbursement.getEngagement().getCaseID());// disbursements.getLoan().getEngagement().getCaseID()
                            EngagementId = dataDisbursement.getEngagement().getEngagementId();
                        }
                        CaseDetails caseDetails = caseDetailsService.findById(EngagementId);
                        if (caseDetails != null) {
//                        obj.put("professionalFee", caseDetails.getProfesionalFee());
                            obj.put("dmp_portion_case_id", String.format("%.0f", (caseDetails.getDmpPortion())));
                            dmpPortion = caseDetails.getDmpPortion();
                        } else {
//                        obj.put("professionalFee", "");
                            obj.put("dmp_portion_case_id", "");
                            dmpPortion = 0d;
                        }
                        if (dataDisbursement.getEngagement() == null) {
                            obj.put("case_id", "");
                        } else {
                            obj.put("case_id", dataDisbursement.getEngagement().getCaseID());// disbursements.getLoan().getEngagement().getCaseID()
                            EngagementId = dataDisbursement.getEngagement().getEngagementId();
                            caseId = dataDisbursement.getEngagement().getCaseID();

                        }

                        OutStandingLoanB outStandingB = this.outStandingLoanBService.findByCaseId(caseId);

                        if (outStandingB == null) {
                            rs.setResponse_code("55");
                            rs.setInfo("Failed");
                            rs.setResponse("caseId : " + caseId + " Not found");
                            CreateLog.createJson(rs, "disbursementbyCaseId");
                            process = false;
                        }

                        outStandingLoanB = outStandingB.getOutStanding();
                        obj.put("out_standing_loan_b",  String.format("%.0f",outStandingLoanB));
                        List<TeamMember> entityTeam = teamMemberService.listTeamMemberByEngagement(EngagementId);
                        if (entityTeam != null) {
                            for (int j = 0; j < entityTeam.size(); j++) {
//                                JSONObject objTeam = new JSONObject();
                                TeamMember dataTeam = entityTeam.get(j);
                                Employee getDmp = employeeService.findById(dataTeam.getDmpId());
                                if (getDmp == null) {
                                    obj.put("employee_id_dmp", "");
                                    obj.put("fee_share_dmp", "");

                                    obj.put("amount_portion_dmp", "");
                                    obj.put("previous_disbursement_dmp", "");

                                    obj.put("anual_salary_dmp", "");
                                    obj.put("total_income_fortax_purpose_dmp", "");
                                    obj.put("masa_kerja_dmp", "");
                                    obj.put("jabatan_per_bulan_dmp", "");
                                    obj.put("jabatan_per_tahun_dmp", "");
                                    obj.put("tax_status_dmp", "");
                                    obj.put("ptkp_dmp", "");
                                    obj.put("taxable_income_dmp", "");
                                    obj.put("income_tax_dmp", "");
                                    obj.put("income_tax_paid_on_prior_period_dmp", "");
                                    obj.put("net_income_tax_deducted_dmp", "");
                                    obj.put("net_income_dmp", "");
                                    obj.put("outstanding_loan_b_dmp", "");
                                    obj.put("disbursable_amount_dmp", "");
                                    obj.put("outstanding_loan_a_dmp", "");
                                    obj.put("disbursed_amount_dmp", "");
                                } else {
                                    if (dataTeam.getTeamMemberId() != null) {
                                        id_team = dataTeam.getTeamMemberId();
                                    }
                                    Integer anual_salary_dmp = (4000000 * 13);
                                    int a_jabatan_dmp = (60000000 * 5) / 100;
                                    int b_jabatan_dmp = ((anual_salary_dmp * 5) / 100) / 13;
                                    int masa_kerja_dmp = 0;
                                    Double amount_portion_dmp = 0d;

                                    Double previous_disbursement_dmp = 0d;
                                    Double total_income_fortax_purpose_dmp = 0d;
                                    String dmp_tax_status = getDmp.getTaxStatus();
                                    Double income_tax_dmp = 0d;
                                    Double taxable_income_dmp = 0d;
                                    Double income_tax_paid_on_prior_period_dmp = 0d;
                                    Double net_income_tax_deducted_dmp = 0d;
                                    Double net_income_dmp = 0d;
                                    Double outstanding_loan_b_dmp = 0d;
                                    Double disbursable_amount_dmp = 0d;
                                    Double outstanding_loan_a_dmp = 0d;
                                    Double disbursed_amount_dmp = 0d;
                                    Double ptkp = 0d;
                                    log.info("dmp_tax_status : " + dmp_tax_status);
//                                    String jabatan_perbulan_dmp = String.format("%d", (((4000000 * 13) * 5) / 100) / 13);
                                    EntityPTKP ptkpDmp = this.ptkpService.findPTKPByTaxStatus(dmp_tax_status);
                                    log.info("ptkpDmp : " + ptkpDmp);
                                    if (ptkpDmp != null) {
                                        ptkp = ptkpDmp.getPtkp();
                                    }
                                    obj.put("employee_id_dmp", getDmp.getEmployeeId());
                                    obj.put("fee_share_dmp", dataTeam.getFeeShare());
                                    amount_portion_dmp = (dmpPortion * dataTeam.getFeeShare()) / 100;
                                    obj.put("amount_portion_dmp", String.format("%.0f", amount_portion_dmp));
                                    obj.put("previous_disbursement_dmp", previous_disbursement_dmp);

                                    obj.put("anual_salary_dmp", String.format("%d", anual_salary_dmp));
                                    total_income_fortax_purpose_dmp = amount_portion_dmp + previous_disbursement_dmp + anual_salary_dmp;
                                    obj.put("total_income_fortax_purpose_dmp", String.format("%.0f", total_income_fortax_purpose_dmp));
                                    masa_kerja_dmp = Util.hitungMasakerja(getDmp.getDateRegister());
                                    obj.put("masa_kerja_dmp", masa_kerja_dmp);
                                    String jabatan_per_bulan_dmp = String.format("%d", Math.min(a_jabatan_dmp, b_jabatan_dmp));
                                    obj.put("jabatan_per_bulan_dmp", jabatan_per_bulan_dmp);
                                    Integer jabatan_per_tahun_dmp = (masa_kerja_dmp * Integer.parseInt(jabatan_per_bulan_dmp));
                                    obj.put("jabatan_per_tahun_dmp", String.format("%d", jabatan_per_tahun_dmp));
                                    obj.put("tax_status_dmp", dmp_tax_status);
                                    obj.put("ptkp_dmp", String.format("%.0f", ptkp));
                                    taxable_income_dmp = (total_income_fortax_purpose_dmp - jabatan_per_tahun_dmp - ptkp);
                                    obj.put("taxable_income_dmp", String.format("%.0f", Math.max(taxable_income_dmp, 0)));
                                    income_tax_dmp = Util.hitungPajak(taxable_income_dmp);
                                    obj.put("income_tax_dmp", String.format("%.0f", income_tax_dmp));
                                    income_tax_paid_on_prior_period_dmp = 0d;
                                    obj.put("income_tax_paid_on_prior_period_dmp", String.format("%.0f", income_tax_paid_on_prior_period_dmp));
                                    net_income_tax_deducted_dmp = (income_tax_dmp - income_tax_paid_on_prior_period_dmp);
                                    obj.put("net_income_tax_deducted_dmp", String.format("%.0f", net_income_tax_deducted_dmp));
                                    net_income_dmp = (amount_portion_dmp - net_income_tax_deducted_dmp);
                                    obj.put("net_income_dmp", String.format("%.0f", net_income_dmp));
                                    outstanding_loan_b_dmp = (outStandingLoanB * dataTeam.getFeeShare()) / 100;
                                    obj.put("outstanding_loan_b_dmp", String.format("%.0f", outstanding_loan_b_dmp));
                                    disbursable_amount_dmp = (net_income_dmp - outstanding_loan_b_dmp);
                                    obj.put("disbursable_amount_dmp", String.format("%.0f", Math.abs(disbursable_amount_dmp)));
                                    outstanding_loan_a_dmp = 0d;
                                    obj.put("outstanding_loan_a_dmp", String.format("%.0f", outstanding_loan_a_dmp));
                                    disbursed_amount_dmp = (disbursable_amount_dmp - outstanding_loan_a_dmp);
                                    obj.put("disbursed_amount_dmp", String.format("%.0f", Math.max(disbursed_amount_dmp, 0)));

                                }
                            }
                            if (id_team != null) {
                                List<Member> entityMember = memberService.findByIdTeam(id_team);
                                log.info("entityMember size : " + entityMember.size());
                                for (int k = d; k < entityMember.size(); k++) {
                                    JSONObject objMember = new JSONObject();
                                    Member dataMember = entityMember.get(k);
                                    if (dataMember == null) {
                                        objMember.put("employee_id_team", "");
                                        objMember.put("fee_share_team", "");//"fee_share_team
                                        objMember.put("amount_portion_team", "");
                                        objMember.put("previous_disbursement_team", "");
                                        objMember.put("anual_salary_team", "");
                                        objMember.put("total_income_fortax_purpose_team", "");
                                        objMember.put("masa_kerja_team", "");
                                        objMember.put("jabatan_per_bulan_team", "");
                                        objMember.put("jabatan_per_tahun_team", "");
                                        objMember.put("tax_status_team", "");
                                        objMember.put("ptkp_team", "");
                                        objMember.put("taxable_income_team", "");
                                        objMember.put("income_tax_team", "");
                                        objMember.put("income_tax_paid_on_prior_period_team", "");
                                        objMember.put("net_income_tax_deducted_team", "");
                                        objMember.put("net_income_team", "");
                                        objMember.put("outstanding_loan_b_team", "");
                                        objMember.put("disbursable_amount_team", "");
                                        objMember.put("outstanding_loan_a_team", "");
                                        objMember.put("disbursed_amount_team", "");

                                    } else {
                                        Integer anual_salary_team = (4000000 * 13);
                                        int a_jabatan_team = (60000000 * 5) / 100;
                                        int b_jabatan_team = ((anual_salary_team * 5) / 100) / 13;
                                        int masa_kerja_team = 0;
                                        Double amount_portion_team = 0d;
                                        Double previous_disbursement_team = 0d;

                                        Double total_income_fortax_purpose_team = 0d;
                                        String team_tax_status = dataMember.getEmployee().getTaxStatus();
                                        Double taxable_income_team = 0d;
                                        Double income_tax_team = 0d;
                                        Double income_tax_paid_on_prior_period_team = 0d;
                                        Double net_income_tax_deducted_team = 0d;
                                        Double net_income_team = 0d;
                                        Double outstanding_loan_b_team = 0d;
                                        Double disbursable_amount_team = 0d;
                                        Double outstanding_loan_a_team = 0d;
                                        Double disbursed_amount_team = 0d;
                                        Double ptkp = 0d;
//                                        log.info("team_tax_status : " + team_tax_status);
//                                      String jabatan_perbulan_team = String.format("%d", (((4000000 * 13) * 5) / 100) / 13);
                                        EntityPTKP ptkpTeam = this.ptkpService.findPTKPByTaxStatus(team_tax_status);
                                        log.info("ptkpTeam : " + ptkpTeam);
                                        if (ptkpTeam != null) {
                                            ptkp = ptkpTeam.getPtkp();
                                        }
                                        objMember.put("employee_id_team", dataMember.getEmployee().getEmployeeId());
                                        objMember.put("fee_share_team", dataMember.getFeeShare());//"fee_share_team
                                        amount_portion_team = ((dmpPortion * dataMember.getFeeShare()) / 100);
                                        objMember.put("amount_portion_team", String.format("%.0f", (amount_portion_team)));
                                        objMember.put("previous_disbursement_team", String.format("%.0f", previous_disbursement_team));

                                        objMember.put("anual_salary_team", String.format("%d", anual_salary_team));
                                        total_income_fortax_purpose_team = amount_portion_team + previous_disbursement_team + anual_salary_team;
                                        objMember.put("total_income_fortax_purpose_team", String.format("%.0f", (total_income_fortax_purpose_team)));
                                        masa_kerja_team = Util.hitungMasakerja(dataMember.getEmployee().getDateRegister());
                                        objMember.put("masa_kerja_team", masa_kerja_team);
                                        String jabatan_per_bulan_team = String.format("%d", Math.min(a_jabatan_team, b_jabatan_team));
                                        Integer jabatan_per_tahun_team = (masa_kerja_team * Integer.parseInt(jabatan_per_bulan_team));
                                        objMember.put("jabatan_per_bulan_team", jabatan_per_bulan_team);
                                        objMember.put("jabatan_per_tahun_team", String.format("%d", jabatan_per_tahun_team));
                                        objMember.put("tax_status_team", team_tax_status);
                                        objMember.put("ptkp_team", String.format("%.0f", ptkp));
                                        taxable_income_team = (total_income_fortax_purpose_team - jabatan_per_tahun_team - ptkp);
                                        objMember.put("taxable_income_team", String.format("%.0f", Math.max(taxable_income_team, 0)));
                                        income_tax_team = Util.hitungPajak(taxable_income_team);
                                        objMember.put("income_tax_team", String.format("%.0f", income_tax_team));
                                        income_tax_paid_on_prior_period_team = 0d;
                                        objMember.put("income_tax_paid_on_prior_period_team", String.format("%.0f", income_tax_paid_on_prior_period_team));
                                        net_income_tax_deducted_team = (income_tax_team - income_tax_paid_on_prior_period_team);
                                        objMember.put("net_income_tax_deducted_team", String.format("%.0f", net_income_tax_deducted_team));
                                        net_income_team = (amount_portion_team - net_income_tax_deducted_team);
                                        objMember.put("net_income_team", String.format("%.0f", net_income_team));
                                        outstanding_loan_b_team = (outStandingLoanB * dataMember.getFeeShare()) / 100;
                                        objMember.put("outstanding_loan_b_team", String.format("%.0f", outstanding_loan_b_team));
                                        disbursable_amount_team = (net_income_team - outstanding_loan_b_team);
                                        objMember.put("disbursable_amount_team", String.format("%.0f", Math.abs(disbursable_amount_team)));
                                        outstanding_loan_a_team = 0d;
                                        objMember.put("outstanding_loan_a_team", String.format("%.0f", outstanding_loan_a_team));
                                        disbursed_amount_team = (disbursable_amount_team - outstanding_loan_a_team);
                                        objMember.put("disbursed_amount_team", String.format("%.0f", Math.max(disbursed_amount_team, 0)));

                                    }
                                    arrayM.put(objMember);

                                }
                            } else {
//                                objMember.put("member_name", "");
//                                objMember.put("employee_id", "");
//                                objMember.put("fee_share", "");
//                                arrayM.put(objMember);
                            }
                            obj.put("members", arrayM);
                            array.put(obj);
                        }
                    }
                    return ResponseEntity.ok(array.toString());
                }
            }
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "disbursementbyCaseId");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//
        return null;
    }
}
