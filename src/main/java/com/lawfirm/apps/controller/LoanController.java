/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.Disbursement;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.Financial;
import com.lawfirm.apps.model.Loan;
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
import com.lawfirm.apps.service.interfaces.FinancialServiceIface;
import com.lawfirm.apps.service.interfaces.LoanServiceIface;
import com.lawfirm.apps.service.interfaces.LoanTypeServiceIface;
import com.lawfirm.apps.service.interfaces.ProfessionalServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import com.lawfirm.apps.support.api.LoanApi;
import com.lawfirm.apps.response.Response;
import com.lawfirm.apps.service.interfaces.LoanHistoryServiceIface;
import com.lawfirm.apps.utils.CreateLog;
import com.lawfirm.apps.utils.CustomErrorType;
import com.lawfirm.apps.utils.Util;
import com.xss.filter.annotation.XxsFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.jline.utils.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author newbiecihuy
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RequestMapping({"/loan"})
public class LoanController {

    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;
    SimpleDateFormat sdfYear;
    SimpleDateFormat sdfMonth;
    SimpleDateFormat sdfMY;
    SimpleDateFormat sdfDisbursM;
    SimpleDateFormat sdfDisbursMY;
//    Date now;
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
    LoanHistoryServiceIface loanHistoryService;

    public LoanController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yy");
        this.sdfMonth = new SimpleDateFormat("MM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
        this.sdfDisbursM = new SimpleDateFormat("MMM");
        this.sdfDisbursMY = new SimpleDateFormat("MMMyyyy");
    }

    @RequestMapping(value = "/manage-loan/loan-a", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response createLoana(@RequestBody final LoanApi object, Authentication authentication) {
        try {
            Date todayDate = new Date();
            Date now = new Date();
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't access this feature");
                CreateLog.createJson(rs, "acreateLoana");
                return rs;
            }
            log.info("isi object " + object.toString());
            Boolean process = true;
            Loan dataLoan = new Loan();
            LoanHistory entityHistory = new LoanHistory();
//            LoanType typeLoan = new LoanType();
//            Financial dataFinance = new Financial();
            LoanType typeLoan = loanTypeService.findByName(object.getLoan_type());
            if (typeLoan == null) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Loan Type Not found");
                process = false;
            }
            if ("A".equalsIgnoreCase(object.getLoan_type())) {
                if (dateFormat.format(todayDate).compareTo(dateFormat.format(object.getRepayment_date())) == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("repayment_date not permission, " + "repayment_date : " + dateFormat.format(object.getRepayment_date()) + " equals  todayDate : " + dateFormat.format(todayDate));
                    process = false;
                    CreateLog.createJson(rs, "createLoan-employee");
                    return rs;
                }
                if (dateFormat.format(todayDate).compareTo(dateFormat.format(object.getRepayment_date())) > 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("repayment_date : " + dateFormat.format(object.getRepayment_date()) + " before  todayDate : " + dateFormat.format(todayDate));
                    process = false;
                    CreateLog.createJson(rs, "createLoan-employee");
                    return rs;
                }
                if (object.getLoan_amount() == 0 || object.getLoan_amount() == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("laon value : 0");
                    process = false;
                    CreateLog.createJson(rs, "createLoan-employee");
                    return rs;
                }
                if (process) {

                    log.info("isi process " + process);

                    Double balance = 0d;
                    String isi_tgl_repayment = null;
                    Date tgl_repayment = null;
                    Integer number = 0;
                    String year_val = sdfYear.format(now);
                    Date y_val = sdfYear.parse(year_val);
                    String load_id = null;
                    String isi_tgl_pengajuan = null;
                    Date tgl_pengajuan = null;
                    if (object.getRepayment_date() != null) {
                        isi_tgl_repayment = dateFormat.format(object.getRepayment_date());

                        tgl_repayment = dateFormat.parse(isi_tgl_repayment);
                    }
                    if (object.getCreated_date() == null) {
                        isi_tgl_pengajuan = dateFormat.format(new Date());
                        tgl_pengajuan = dateFormat.parse(isi_tgl_pengajuan);
                    } else if (object.getCreated_date() != null) {
                        isi_tgl_pengajuan = dateFormat.format(object.getCreated_date());

                        tgl_pengajuan = dateFormat.parse(isi_tgl_pengajuan);
                    }

                    dataLoan.setEmployee(entityEmp);
                    dataLoan.setLoantype(typeLoan);
                    dataLoan.setTgl_input(sdfYear.format(now));
                    dataLoan.setDate_month(sdfMY.format(object.getCreated_date()));//sdfMonth.format(new Date())
                    dataLoan.setRepayment_date(tgl_repayment);
                    dataLoan.setDate_created(tgl_pengajuan);
                    dataLoan.setStatus("s");
                    entityHistory.setUserId(entityEmp.getIdEmployee());
                    entityHistory.setResponse(" submit  : " + typeLoan.getTypeLoan() + "By :" + entityEmp.getIdEmployee());

                    if (entityEmp.getLoanAmount().compareTo(object.getLoan_amount()) < 0) {
                        rs.setResponse_code("05");
                        rs.setInfo("Failed");
                        rs.setResponse("limit loan : " + entityEmp.getLoanAmount());
                        process = false;
                        CreateLog.createJson(rs, "createLoan-employee");
                        return rs;
                    }
//                    }
                    Double cek_loan = this.loanService.loanEmp(entityEmp.getIdEmployee().toString(), sdfMY.format(object.getCreated_date()));
//                    if (cek_loan != null) {
//                        log.info("cek_loan" + cek_loan);
//                        rs.setResponse_code("05");
//                        rs.setInfo("cek_loan");
//                        rs.setResponse("cek_loan: " + cek_loan);
//                        return rs;
//                    }
                    if (cek_loan != null || !cek_loan.equals(0)) {

                        balance = object.getLoan_amount() + cek_loan;
                    } else {
                        balance = object.getLoan_amount();
                    }
//                    if (dataEMploye.getLoanAmount().compareTo(balance) < 0) {
//                        rs.setResponse_code("05");
//                        rs.setInfo("Failed");
//                        rs.setResponse("limit loan : " + String.format("%.0f", dataEMploye.getLoanAmount()) + " Total Loan this month : " + String.format("%.0f", cek_loan));
//                        process = false;
//                        CreateLog.createJson(rs, "createLoan-employee");
//                        return rs;
//                    }
                    if (entityEmp.getLoanAmount().compareTo(balance) < 0) {
                        rs.setResponse_code("05");
                        rs.setInfo("Failed");
                        rs.setResponse("limit loan : " + String.format("%.0f", entityEmp.getLoanAmount()) + " Total Loan this month : " + String.format("%.0f", cek_loan));
                        process = false;
                        CreateLog.createJson(rs, "createLoan-employee");
                        return rs;
                    }

//                    
//                    }
                    dataLoan.setLoanAmount(object.getLoan_amount());
                    if (sdfYear.format(now).compareTo(sdfYear.format(object.getCreated_date())) == 0) {
//                        number = loanService.generateLoanId("A", entityEmp.getEmployeeId(), sdfYear.format(object.getCreated_date()));//dateFormat.format(todayDate));
                        List<Loan> list = loanService.generateLoanId("A", entityEmp.getEmployeeId(), sdfYear.format(object.getCreated_date()));

                        if (list != null || !list.isEmpty()) {
                            number = list.size();
                        }
                        if (number == 0) {
                            number = 1;
                            load_id = "A" + entityEmp.getEmployeeId() + year_val + Util.setNumbering(number.toString());
                        } else {
                            number = number + 1;
                            load_id = "A" + entityEmp.getEmployeeId() + year_val + Util.setNumbering(number.toString());
                        }
                    } else {
                        List<Loan> list = loanService.generateLoanId("A", entityEmp.getEmployeeId(), sdfYear.format(object.getCreated_date()));

                        if (list != null || !list.isEmpty()) {
                            number = list.size();
                        }
                        if (number == 0) {
                            number = 1;
                            load_id = "A" + entityEmp.getEmployeeId() + year_val + Util.setNumbering(number.toString());
                        } else {
                            number = number + 1;
                            load_id = "A" + entityEmp.getEmployeeId() + year_val + Util.setNumbering(number.toString());
                        }
                    }

//                    Loan entity = loanService.findByLoanId("A" + dataEMploye.getEmployeeId() + year_val + Util.setNumbering(number.toString()));
                    log.info("load_id : " + load_id);
                    Loan entity = loanService.findByLoanId(load_id);
//                    if (entity == null) {
//                        number = 1;
//                        load_id = "A" + entityEmp.getEmployeeId() + year_val + Util.setNumbering(number.toString());
//                        dataLoan.setLoanId(load_id);
//                    } else {
//                        number = number + 1;
//                        load_id = "A" + entityEmp.getEmployeeId() + year_val + Util.setNumbering(number.toString());
//                        dataLoan.setLoanId(load_id);
//                    }
                    if (entity != null) {
                        number = number + 1;
                        load_id = "A" + entityEmp.getEmployeeId() + year_val + Util.setNumbering(number.toString());
                    }
                    log.info("load_id now : " + load_id);
                    dataLoan.setLoanId(load_id);
                    dataLoan.addAHistory(entityHistory);
                    dataLoan = loanService.create(dataLoan);
//                   typeLoan.addLoan(dataLoan);
                    if (dataLoan != null) {
                        rs.setResponse_code("01");
                        rs.setInfo("Success");
                        rs.setResponse("Create Loan Apps Success");
                        CreateLog.createJson(rs, "createLoan-employee");
                        return rs;
//                        }
                    } else {
                        rs.setResponse_code("05");
                        rs.setInfo("Failed");
                        rs.setResponse("Create Loan Apps Failed");
                        process = false;
                        CreateLog.createJson(rs, "createLoan-employee");
                    }

                }

            }

            return rs;
        } catch (ParseException ex) {
            Logger.getLogger(LoanController.class.getName()).log(Level.SEVERE, null, ex);
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "createLoan-employee");
            return rs;
        }

    }

    @RequestMapping(value = "/manage-loan/loan-b", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response createLoanb(@RequestBody final LoanApi object, Authentication authentication) {
        try {
            Date todayDate = new Date();
            Date now = new Date();
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't access this feature");
                CreateLog.createJson(rs, "createLoanb-employee");
                return rs;
            }
            log.info("isi object " + object.toString());
            Boolean process = true;
            Loan dataLoan = new Loan();
            LoanHistory entityHistory = new LoanHistory();
//            LoanType typeLoan = new LoanType();
//            Financial dataFinance = new Financial();
            LoanType typeLoan = loanTypeService.findByName(object.getLoan_type());
            if (typeLoan == null) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Loan Type Not found");
                process = false;
            }
            if ("B".equalsIgnoreCase(object.getLoan_type())) {
                if (object.getCase_id() == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Create Loan Apps Type B Failed");
                    process = false;
                    CreateLog.createJson(rs, "createLoanb-employee");
                }
//                Loan dataLoan = new Loan();
//                LoanType typeLoan = new LoanType();
//                Financial dataFinance = new Financial();
                Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
                if (dataEMploye == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Employee Id Not found");
                    process = false;
                    CreateLog.createJson(rs, "createLoanb-employee");
                }
                if (!dataEMploye.getRoleName().contentEquals("dmp")) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Create Loan Apps Type B Failed");
                    process = false;
                    CreateLog.createJson(rs, "createLoanb-employee");
                }
//                CaseDetails dataCase = new CaseDetails();
                if (object.getCase_id() == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Can't Access Loan Apps Type Type B");
                    process = false;
                    CreateLog.createJson(rs, "createLoanb-employee");
                }
//                if (!dataEMploye.getRoleName().equalsIgnoreCase("lawyer")) {
//                    rs.setResponse_code("05");
//                    rs.setInfo("Failed");
//                    rs.setResponse("Can't Access Loan Apps Type B");
//                    process = false;
//                }
                if (dateFormat.format(todayDate).compareTo(dateFormat.format(object.getRepayment_date())) == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("repayment_date not permission" + "repayment_date : " + dateFormat.format(object.getRepayment_date()) + " equals  todayDate : " + dateFormat.format(todayDate));
                    process = false;
                    CreateLog.createJson(rs, "createLoanb-employee");
                }
                if (dateFormat.format(todayDate).compareTo(dateFormat.format(object.getRepayment_date())) > 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("repayment_date : " + dateFormat.format(object.getRepayment_date()) + " before  todayDate : " + dateFormat.format(todayDate));
                    process = false;
                    CreateLog.createJson(rs, "createLoanb-employee");
                }
                if (process) {

                    CaseDetails dataCase = caseDetailsService.findCaseId(object.getCase_id());
                    if (dataCase.getStatus().contains("r")) {
                        rs.setResponse_code("05");
                        rs.setInfo("Failed");
                        rs.setResponse("CASE ID : " + dataCase.getCaseID() + "rejected ");
                        process = false;
                        CreateLog.createJson(rs, "createLoanb-employee");
                    }
                    if (dataCase.getStatus().contains("d")) {
                        rs.setResponse_code("05");
                        rs.setInfo("Failed");
                        rs.setResponse("CASE ID : " + dataCase.getCaseID() + "already disbursement ");
                        process = false;
                        CreateLog.createJson(rs, "createLoanb-employee");
                    }
                    dataLoan.setEmployee(dataEMploye);
                    String isi_tgl_repayment = null;
                    Date tgl_repayment = null;
//                    Integer number = 0;
                    String year_val = sdfYear.format(now);
                    String load_id = null;
                    if (object.getRepayment_date() != null) {
                        isi_tgl_repayment = dateFormat.format(object.getRepayment_date());
                        tgl_repayment = dateFormat.parse(isi_tgl_repayment);
                    }
                    dataLoan.setTgl_input(sdfYear.format(now));
                    dataLoan.setRepayment_date(tgl_repayment);
                    dataLoan.setLoanAmount(object.getLoan_amount());
                    dataLoan.setLoantype(typeLoan);
                    dataLoan.setStatus("s");
                    entityHistory.setUserId(dataEMploye.getIdEmployee());
                    entityHistory.setResponse(typeLoan + " submit");
                    dataLoan.setEngagement(dataCase);//BCS200101
                    Integer number = 0;
                    List<Loan> list = loanService.generateLoanIdB(dataCase.getCaseID(), null, null);
                    if (list != null || !list.isEmpty()) {
                        number = list.size();
                    }
                    if (number == 0) {
                        number = 1;
                        load_id = "BCS" + Util.removeCase(dataCase.getCaseID()) + Util.setNumbering(number.toString());
                    } else {
                        number = number + 1;
                        load_id = "BCS" + Util.removeCase(dataCase.getCaseID()) + Util.setNumbering(number.toString());
                    }
                    Loan entity = loanService.findByLoanIdB(load_id);
                    Log.info("entity b : " + entity);
                    if (entity != null) {
                        number = number + 1;
                        load_id = "BCS" + Util.removeCase(dataCase.getCaseID()) + Util.setNumbering(number.toString());
                    }
                    Log.info("check_load_id b : " + load_id);
                    dataLoan.setLoanId(load_id);
                    dataLoan.addAHistory(entityHistory);
                    Loan dLoan = loanService.create(dataLoan);
                    if (dLoan != null) {
//                        dataFinance.setLoan(dLoan);
//                        dataFinance.setCreated_date(now);
//                        Financial dFinancial = financialService.create(dataFinance);
//                        if (dFinancial != null) {
//                            log.info("isi dLoan " + dLoan.getLoanId());
//                            log.info("isi dFinancial " + dFinancial.getFinancialId().toString());
                        rs.setResponse_code("01");
                        rs.setInfo("Success");
                        rs.setResponse("Create Loan Type B  Succes load_id : " + load_id);
//                        }
                    }
//                    rs.setResponse_code("05");
//                    rs.setInfo("Failed");
//                    rs.setResponse("Create Loan Apps Type B Failed");
                    return rs;
                }

            }
            return rs;
        } catch (ParseException ex) {
            Logger.getLogger(LoanController.class.getName()).log(Level.SEVERE, null, ex);
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "createLoan-employee");
            return rs;
        }

    }

    @RequestMapping(value = "/approval/{id_loan}/by-admin", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
//    @PutMapping(value = "/approved/by-admin/{loan_id}", produces = {"application/json"})
//    @XxsFilter
    public Response approveByAdmin(@RequestBody final LoanApi object, @PathVariable("id_loan") Long id_loan, Authentication authentication) {
        try {
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
                CreateLog.createJson(rs, "loan-approve-Byfinance");
//                process = false;
                return rs;
            }
            if (!entityEmp.getRoleName().contentEquals("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature : " + entityEmp.getRoleName().toUpperCase());
                CreateLog.createJson(rs, "loan-approve-Byfinance");
//                process = false;
                return rs;
            }
//             Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "loan-approve-ByAdmin");
                process = false;

            }

            Loan dataLoan = loanService.findByIdLoan(id_loan);//findById
            if (dataLoan == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loand id null, Cannot Access This feature");
                CreateLog.createJson(rs, "loan-approve-ByAdmin");
                process = false;
            }
            if (dataLoan.getIsActive().contentEquals("0") || dataLoan.getStatus().contentEquals("a")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan Already Approved, at : " + dataLoan.getAprovedByAdmin());
                CreateLog.createJson(rs, "loan-approve-ByAdmin");
                process = false;
            }
            if (dataLoan.getIsActive().contentEquals("2") || dataLoan.getStatus().contentEquals("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loans Rejected, at : " + dataLoan.getAprovedByAdmin());
                CreateLog.createJson(rs, "loan-approve-ByAdmin");
                process = false;
            }
            if (dataLoan.getIsActive().contains("4")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Already reimbursement By Finance :");
                CreateLog.createJson(rs, "loan-approval-ByAdmin");
                return rs;
            }
            if (process) {
                dataLoan.setAprovedByAdmin(dataEMploye.getName());
                dataLoan.setDate_approved(now);
                if (object.getDecision().contains("r")) {
                    dataLoan.setStatus(object.getDecision());
                    dataLoan.setIsActive("2");
                    entityHistory.setResponse(object.getRemarks());
                } else if (object.getDecision().contains("a")) {
                    dataLoan.setStatus(object.getDecision());
                    dataLoan.setIsActive("0");
                    entityHistory.setResponse("approve");
                }
                entityHistory.setLoan(dataLoan);
                entityHistory.setUserId(object.getId_employee_admin());
//        dataLoan.setIsActive("0");
//        dataLoan.addAHistory(entityHistory);
                loanHistoryService.create(entityHistory);
                Loan upDataLoan = loanService.update(dataLoan);
                if (upDataLoan.getIsActive().contentEquals("0")) {
                    rs.setResponse_code("00");
                    rs.setInfo("Success");
                    rs.setResponse("Loan apps : " + dataLoan.getEmployee().getName() + " Approved");
                    CreateLog.createJson(rs, "loan-approve-ByAdmin");
                }
                if (upDataLoan.getIsActive().contentEquals("2")) {
                    rs.setResponse_code("00");
                    rs.setInfo("Success");
                    rs.setResponse("Loan apps : " + dataLoan.getEmployee().getName() + " Rejected");
                    CreateLog.createJson(rs, "loan-approve-ByAdmin");
                }
//            else {
//                rs.setResponse_code("05");
//                rs.setInfo("Failed");
//                rs.setResponse("Loand id null, Cannot Access This feature");
//                CreateLog.createJson(rs, "loan-approve-ByAdmin");
//            }
            }
            return rs;
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "loan-approve-ByAdmin");
            CreateLog.createJson(ex.getMessage(), "loan-approve-ByAdmin");
            return rs;

        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/approval-b/{id_loan}/by-admin", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
//    @PutMapping(value = "/approved/by-admin/{loan_id}", produces = {"application/json"})
//    @XxsFilter
    public Response approveLoanBByAdmin(@RequestBody final LoanApi object, @PathVariable("id_loan") Long id_loan, Authentication authentication) {
        try {
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
                CreateLog.createJson(rs, "loan-approve-Byfinance");
//                process = false;
                return rs;
            }
            if (!entityEmp.getRoleName().contentEquals("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature : " + entityEmp.getRoleName().toUpperCase());
                CreateLog.createJson(rs, "loan-approve-Byfinance");
//                process = false;
                return rs;
            }
//             Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "loan-approve-ByAdmin");
                process = false;

            }

            Loan dataLoan = loanService.findByIdLoan(id_loan);//findById
            if (dataLoan == null) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Loand id null, Cannot Access This feature");
                CreateLog.createJson(rs, "loan-approve-ByAdmin");
                process = false;
            }
            if (dataLoan.getIsActive().contentEquals("0") || dataLoan.getStatus().contentEquals("a")) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Loan Already Approved, at : " + dataLoan.getAprovedByAdmin());
                CreateLog.createJson(rs, "loan-approve-ByAdmin");
                process = false;
            }
            if (dataLoan.getIsActive().contentEquals("2") || dataLoan.getStatus().contentEquals("r")) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Loans Rejected, at : " + dataLoan.getAprovedByAdmin());
                CreateLog.createJson(rs, "loan-approve-ByAdmin");
                process = false;
            }

            if (process) {
                dataLoan.setAprovedByAdmin(dataEMploye.getName());
                dataLoan.setDate_approved(now);
                if (object.getDecision().contains("r")) {
                    dataLoan.setStatus(object.getDecision());
                    dataLoan.setIsActive("2");
                    entityHistory.setResponse(object.getRemarks());
                } else if (object.getDecision().contains("a")) {
                    dataLoan.setStatus(object.getDecision());
                    dataLoan.setIsActive("0");
                    entityHistory.setResponse("approve");
                }
                entityHistory.setLoan(dataLoan);
                entityHistory.setUserId(object.getId_employee_admin());
//        dataLoan.setIsActive("0");
//        dataLoan.addAHistory(entityHistory);
                loanHistoryService.create(entityHistory);
                Loan upDataLoan = loanService.update(dataLoan);
                if (upDataLoan.getIsActive().contentEquals("0")) {
                    rs.setResponse_code("00");
                    rs.setInfo("Success");
                    rs.setResponse("Loan apps : " + dataLoan.getEmployee().getName() + " Approved");
                    CreateLog.createJson(rs, "loan-approve-ByAdmin");
                }
                if (upDataLoan.getIsActive().contentEquals("2")) {
                    rs.setResponse_code("00");
                    rs.setInfo("Success");
                    rs.setResponse("Loan apps : " + dataLoan.getEmployee().getName() + " Rejected");
                    CreateLog.createJson(rs, "loan-approve-ByAdmin");
                }
//            else {
//                rs.setResponse_code("05");
//                rs.setInfo("Failed");
//                rs.setResponse("Loand id null, Cannot Access This feature");
//                CreateLog.createJson(rs, "loan-approve-ByAdmin");
//            }
            }
            return rs;
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "loan-approve-ByAdmin");
            CreateLog.createJson(ex.getMessage(), "loan-approve-ByAdmin");
            return rs;

        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    //    @PutMapping(value = "/approved/by-finance/{id_loan}", produces = {"application/json"})
    @RequestMapping(value = "/approval/{id_loan}/by-finance", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response approveByFinance(@RequestBody final LoanApi object, @PathVariable("id_loan") Long id_loan, Authentication authentication) {
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
//            rs.setResponse_code("05");
//            rs.setInfo("Failed");
//            rs.setResponse("Finance Id Not found");
//            CreateLog.createJson(rs, "loan-approve-Byfinance");
//            process = false;
//        }
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "loan-approve-Byfinance");
//                process = false;
                return rs;
            }
            if (!entityEmp.getRoleName().contentEquals("finance")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature : " + entityEmp.getRoleName().toUpperCase());
                CreateLog.createJson(rs, "loan-approve-Byfinance");
//                process = false;
                return rs;
            }
//             Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());

            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "loan-approve-Byfinance");
                process = false;
            }
//        if (dataEMploye.getRoleName().contentEquals("finance")) {
//            rs.setResponse_code("05");
//            rs.setInfo("Failed");
//            rs.setResponse("Cannot Access This feature");
//            process = false;
//        }
            dataLoan = loanService.findById(id_loan);

            if (dataLoan == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loand id null, Cannot Access This feature");
                CreateLog.createJson(rs, "loan-approve-Byfinance");
                process = false;
            }
            if (dataLoan.getStatus().contentEquals("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan apps are rejected");
                CreateLog.createJson(rs, "loan-approve-Byfinance");
                process = false;
            }
            if (dataLoan.getIsActive().contentEquals("1")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Loan apps Must approve by ADMIN");
                CreateLog.createJson(rs, "loan-approve-Byfinance");
                process = false;
            }
            if (dataLoan.getIsActive().contentEquals("4")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Already approved by finance ");
                CreateLog.createJson(rs, "loan-approve-Byfinance");
                process = false;
            }
            if (process) {
                dataLoan.setAprovedByFinance(dataEMploye.getName());
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
                entityHistory.setResponse("disburse");
                Loan upDataLoan = loanService.update(dataLoan);
                this.loanHistoryService.create(entityHistory);
                if (upDataLoan != null) {
                    dataFinance.setDisburse_date(new Date());
                    if (object.getOut_standing() != null) {
                        dataFinance.setOutStanding(object.getOut_standing());
                    } else {
                        rs.setResponse_code("05");
                        rs.setInfo("Failed");
                        rs.setResponse("out Standing Field can't be null");
                        CreateLog.createJson(rs, "loan-approve-Byfinance");
                    }
                    Financial upd_finance = financialService.update(dataFinance);
                    if (upd_finance != null) {
                        rs.setResponse_code("01");
                        rs.setInfo("Success");
                        rs.setResponse("Loan apps Approved By :" + dataLoan.getAprovedByFinance());
                        CreateLog.createJson(rs, "loan-approve-Byfinance");
                        return rs;
                    }
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Loand id null, Cannot Access This feature");
                    CreateLog.createJson(rs, "loan-approve-Byfinance");
                    return rs;
                } else {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Loand id null, Cannot Access This feature");
                    CreateLog.createJson(rs, "loan-approve-Byfinance");
                    return rs;
                }
            }
            return rs;
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "loan-approve-Byfinance");
            CreateLog.createJson(ex.getMessage(), "loan-approve-Byfinance");
            return rs;

        } catch (ParseException ex) {
            Logger.getLogger(LoanController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR: " + ex.getMessage());
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "loan-approve-Byfinance");
            CreateLog.createJson(ex.getMessage(), "loan-approve-Byfinance");
            return rs;
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }
//
//    @PermitAll
//    @PutMapping(value = "/deleteLoan/{loan_id}", produces = {"application/json"})
//    public Response deleteLoan(@RequestBody final LoanApi object, @PathVariable("loan_id") Long loan_id) {
//        Loan entity = loanService.findById(loan_id);
//        Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
//        if (dataEMploye == null) {
//            rs.setResponse_code("05");
//            rs.setInfo("Failed");
//            rs.setResponse("Employee Id Not found");
//        }
////        if (dataEMploye.getRoleName().contentEquals("admin")) {
////            rs.setResponse_code("05");
////            rs.setInfo("Failed");
////            rs.setResponse("Cannot Access This feature");
////        }
//        if (entity != null) {
//            entity.setIsDelete(true);
//            entity.setIsActive("0");
//
//            rs.setResponse_code("01");
//            rs.setInfo("Succes");
//            rs.setResponse("Loan Deleted");
//        }
//        return rs;
//    }
//

    @RequestMapping(value = "/view/list-of-loan-a", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> viewLoan(Authentication authentication) {
        try {
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "view-Loanb");
//                process = false;
                return new ResponseEntity(new CustomErrorType("05", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
//            int max = loanService.count();
            int max = 0;
            int start = 0;
            List<Loan> entityList = null;
            if (start == 0) {
                entityList = this.loanService.listLoan(0, 0, "a");
            } else {
                entityList = this.loanService.listLoan(max, start, "a");
            }

            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                JSONObject jsonobj = new JSONObject();
                Loan entity = (Loan) entityList.get(i);
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
                    jsonobj.put("aproved_by_admin", entity.getAprovedByAdmin());
                }
                if (entity.getDate_approved() == null) {
                    jsonobj.put("date_approve_by_admin", "");
                } else {
                    jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getDate_approved()));
                }
                if (entity.getAprovedByFinance() == null) {
                    jsonobj.put("disburse_by_finance", "");
                } else {
                    jsonobj.put("disburse_by_finance", entity.getAprovedByFinance());
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
//                if (entity.getLoantype().getTypeLoan().equalsIgnoreCase("B")) {
//
//                    if (entity.getEngagement().getCaseID() == null) {
//                        jsonobj.put("case_id", "");
//                    } else {
//                        jsonobj.put("case_id", entity.getEngagement().getCaseID());
//                    }
//                }

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
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
//            rs.setResponse_code("05");
//            rs.setInfo("Failed");
//            rs.setResponse(ex.getMessage());
//            CreateLog.createJson(rs, "list-of-loan-a");
            CreateLog.createJson(ex.getMessage(), "list-of-loan-a");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);

        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

//    @RequestMapping(value = "/view/loans/{id_employee}", method = RequestMethod.PUT, produces = {"application/json"})
    @GetMapping(value = "/view/{id_employee}/loans-a", produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> viewLoans(@PathVariable("id_employee") Long id_employee, Authentication authentication) {
        try {

            List<Loan> entityList = this.loanService.findByEmployee(id_employee.toString(), "a");

            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                JSONObject jsonobj = new JSONObject();
                Loan entity = (Loan) entityList.get(i);
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
                    jsonobj.put("aproved_by_admin", entity.getAprovedByAdmin());
                }
                if (entity.getDate_approved() == null) {
                    jsonobj.put("date_approve_by_admin", "");
                } else {
                    jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getDate_approved()));
                }
                if (entity.getAprovedByFinance() == null) {
                    jsonobj.put("disburse_by_finance", "");
                } else {
                    jsonobj.put("disburse_by_finance", entity.getAprovedByFinance());
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
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "list-of-loan-a-employee");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/view/list-of-loan-a/by-admin", produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> viewLoanAByAdmin(@RequestParam(value = "param_status", required = false) String param, Authentication authentication) {
        try {
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewLoanAByAdmin");
                return new ResponseEntity(new CustomErrorType("05", "Failed", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
//            Employee dataEmp = employeeService.findById(id_employee_admin);
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());
            log.info("dataEmp : " + dataEmp.getRoleName());
            if (dataEmp == null) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewLoanAByAdmin");
                return new ResponseEntity(new CustomErrorType("05", "Failed", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!dataEmp.getRoleName().matches("admin")) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewLoanAByAdmin");
                return new ResponseEntity(new CustomErrorType("05", "Failed", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }

//            int max = loanService.count();
            int max = 0;
            int start = 0;
            List<Loan> entityList = null;
            if (param == null) {
                param = "s";
            }
            if (start == 0) {
                entityList = this.loanService.listActive(dataEmp.getName(), param, "a");
            } else {
                entityList = this.loanService.listActive(dataEmp.getName(), param, "a");
            }

            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                JSONObject jsonobj = new JSONObject();
                Loan entity = (Loan) entityList.get(i);
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
                    jsonobj.put("aproved_by_admin", entity.getAprovedByAdmin());
                }
                if (entity.getDate_approved() == null) {
                    jsonobj.put("date_approve_by_admin", "");
                } else {
                    jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getDate_approved()));
                }
                if (entity.getAprovedByFinance() == null) {
                    jsonobj.put("disburse_by_finance", "");
                } else {
                    jsonobj.put("disburse_by_finance", entity.getAprovedByFinance());
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
//                if (entity.getLoantype().getTypeLoan().equalsIgnoreCase("B")) {
//
//                    if (entity.getEngagement() == null) {
//                        jsonobj.put("case_id", "");
//                    } else {
//                        jsonobj.put("case_id", entity.getEngagement().getCaseID());
//                    }
//                }

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
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            CreateLog.createJson(ex.getMessage(), "viewLoanAByAdmin");
            System.out.println("ERROR: " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/view/list-of-loan-b/by-admin", produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> viewLoanBByAdmin(@RequestParam(value = "param_status", required = false) String param, Authentication authentication) {
        try {
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewLoanByAdmin");
                return new ResponseEntity(new CustomErrorType("05", "Failed", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
//            Employee dataEmp = employeeService.findById(id_employee_admin);
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());
            log.info("dataEmp : " + dataEmp.getRoleName());
            if (dataEmp == null) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewLoanBByAdmin");
                return new ResponseEntity(new CustomErrorType("05", "Failed", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!dataEmp.getRoleName().matches("admin")) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewLoanBByAdmin");
                return new ResponseEntity(new CustomErrorType("05", "Failed", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }

            int max = loanService.count();
            int start = 0;
            List<Loan> entityList = null;
            if (param == null) {
                param = "s";
            }
            if (start == 0) {
                entityList = this.loanService.listActive(dataEmp.getName(), param, "b");
            } else {
                entityList = this.loanService.listActive(dataEmp.getName(), param, "b");
            }

            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                JSONObject jsonobj = new JSONObject();
                Loan entity = (Loan) entityList.get(i);
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
                    jsonobj.put("aproved_by_admin", entity.getAprovedByAdmin());
                }
                if (entity.getDate_approved() == null) {
                    jsonobj.put("date_approve_by_admin", "");
                } else {
                    jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getDate_approved()));
                }
                if (entity.getAprovedByFinance() == null) {
                    jsonobj.put("disburse_by_finance", "");
                } else {
                    jsonobj.put("disburse_by_finance", entity.getAprovedByFinance());
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
                if (entity.getLoantype().getTypeLoan().equalsIgnoreCase("B")) {

                    if (entity.getEngagement() == null) {
                        jsonobj.put("case_id", "");
                    } else {
                        jsonobj.put("case_id", entity.getEngagement().getCaseID());
                    }
                }

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
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            CreateLog.createJson(ex.getMessage(), "viewLoanBByAdmin");
            System.out.println("ERROR: " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/view/list-of-loan-b", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> viewLoanb(Authentication authentication) {

        try {

            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "view-Loanb");
//                process = false;
                return new ResponseEntity(new CustomErrorType("05", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }

//            int max = loanService.count();
            int max = 0;
            int start = 0;
            List<Loan> entityList = null;
            if (start == 0) {
                entityList = this.loanService.listLoan(0, 0, "b");
            } else {
                entityList = this.loanService.listLoan(max, start, "b");
            }

            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                JSONObject jsonobj = new JSONObject();
                Loan entity = (Loan) entityList.get(i);
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
                    jsonobj.put("aproved_by_admin", entity.getAprovedByAdmin());
                }
                if (entity.getDate_approved() == null) {
                    jsonobj.put("date_approve_by_admin", "");
                } else {
                    jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getDate_approved()));
                }
                if (entity.getAprovedByFinance() == null) {
                    jsonobj.put("disburse_by_finance", "");
                } else {
                    jsonobj.put("disburse_by_finance", entity.getAprovedByFinance());
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
                if (entity.getLoantype().getTypeLoan().equalsIgnoreCase("B")) {

                    if (entity.getEngagement().getCaseID() == null) {
                        jsonobj.put("case_id", "");
                    } else {
                        jsonobj.put("case_id", entity.getEngagement().getCaseID());
                    }
                }

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
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            CreateLog.createJson(ex.getMessage(), "list-of-loan-b");
            System.out.println("ERROR: " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

//    @RequestMapping(value = "/view/loans/{id_employee}", method = RequestMethod.PUT, produces = {"application/json"})
    @GetMapping(value = "/view/{id_employee}/loans-b", produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> viewLoansb(@PathVariable("id_employee") Long id_employee, Authentication authentication) {
        try {
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "view-Loanb");
//                process = false;
                return new ResponseEntity(new CustomErrorType("05", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
//            List<Loan> entityList = this.loanService.findByEmployee(id_employee.toString(), "b");
            List<Loan> entityList = this.loanService.findByEmployee(entityEmp.getIdEmployee().toString(), "b");

            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                JSONObject jsonobj = new JSONObject();
                Loan entity = (Loan) entityList.get(i);
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
                    jsonobj.put("aproved_by_admin", entity.getAprovedByAdmin());
                }
                if (entity.getDate_approved() == null) {
                    jsonobj.put("date_approve_by_admin", "");
                } else {
                    jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getDate_approved()));
                }
                if (entity.getAprovedByFinance() == null) {
                    jsonobj.put("disburse_by_finance", "");
                } else {
                    jsonobj.put("disburse_by_finance", entity.getAprovedByFinance());
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
                if (entity.getLoantype().getTypeLoan().equalsIgnoreCase("B")) {

                    if (entity.getEngagement().getCaseID() == null) {
                        jsonobj.put("case_id", "");
                    } else {
                        jsonobj.put("case_id", entity.getEngagement().getCaseID());
                    }
                }

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
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "list-of-loan-b-employee");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/view/list-of-loan-b/by-admin/{id_employee_admin}", produces = {"application/json"})
    @XxsFilter
//    public ResponseEntity<String> viewLoanByAdminb(@PathVariable("id_employee_admin") Long id_employee_admin, @RequestParam(value = "param_status", required = false) String param, Authentication authentication) {
    public ResponseEntity<String> viewLoanByAdminb(@RequestParam(value = "param_status", required = false) String param, Authentication authentication) {
        try {
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewLoanByAdminb");
                return new ResponseEntity(new CustomErrorType("05", "Failed", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());
            log.info("dataEmp : " + dataEmp.getRoleName());
            if (dataEmp == null || !dataEmp.getRoleName().matches("admin")) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "loan-approve-Byfinance");
                return new ResponseEntity(new CustomErrorType("05", "Failed", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }

            int max = loanService.count();
            int start = 0;
            List<Loan> entityList = null;
            if (param == null) {
                param = "s";
            }
            if (start == 0) {
                entityList = this.loanService.listActive(dataEmp.getName(), param, "b");
            } else {
                entityList = this.loanService.listActive(dataEmp.getName(), param, "b");
            }

            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                JSONObject jsonobj = new JSONObject();
                Loan entity = (Loan) entityList.get(i);
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
                    jsonobj.put("aproved_by_admin", entity.getAprovedByAdmin());
                }
                if (entity.getDate_approved() == null) {
                    jsonobj.put("date_approve_by_admin", "");
                } else {
                    jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getDate_approved()));
                }
                if (entity.getAprovedByFinance() == null) {
                    jsonobj.put("disburse_by_finance", "");
                } else {
                    jsonobj.put("disburse_by_finance", entity.getAprovedByFinance());
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
                if (entity.getLoantype().getTypeLoan().equalsIgnoreCase("B")) {

                    if (entity.getEngagement().getCaseID() == null) {
                        jsonobj.put("case_id", "");
                    } else {
                        jsonobj.put("case_id", entity.getEngagement().getCaseID());
                    }
                }

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
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "list-of-loan-b-admin");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/view/history-of-loan/by-admin", produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> viewHistoryByAdmin(@RequestParam(value = "param_status", required = false) String param, Authentication authentication) {
        try {
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "viewHistoryByAdmin");
                return new ResponseEntity(new CustomErrorType("05", "Failed", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
//            Employee dataEmp = employeeService.findById(id_employee_admin);
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());

            log.info("dataEmp : " + dataEmp.getRoleName());
            if (dataEmp == null) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "loan-approve-Byfinance");
                return new ResponseEntity(new CustomErrorType("05", "Failed", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!dataEmp.getRoleName().matches("admin")) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "loan-approve-Byfinance");
                return new ResponseEntity(new CustomErrorType("05", "Failed", "Cannot Access This feature"),
                        HttpStatus.NOT_FOUND);
            }

            int max = loanService.count();
            int start = 0;
            List<LoanHistory> entityList = null;
            if (param == null) {
                param = "submit";
            }
            if (start == 0) {
                entityList = this.loanHistoryService.findByUserId(entityEmp.getIdEmployee(), param);
            } else {
                entityList = this.loanHistoryService.findByUserId(entityEmp.getIdEmployee(), param);
            }

            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                JSONObject jsonobj = new JSONObject();
                LoanHistory entity = (LoanHistory) entityList.get(i);
                if (entity.getId() == null) {
                    jsonobj.put("id", "");
                } else {
                    jsonobj.put("id", entity.getId());
                }
                if (entity.getLoan().getId() == null) {
                    jsonobj.put("id_loan", "");
                } else {
                    jsonobj.put("id_loan", entity.getLoan().getId());
                }
                if (entity.getLoan().getLoanId() == null) {
                    jsonobj.put("loan_id", "");
                } else {
                    jsonobj.put("loan_id", entity.getLoan().getLoanId());
                }
                if (entity.getLoan().getLoanAmount() == null) {
                    jsonobj.put("amount", "");
                } else {
                    jsonobj.put("amount", String.format("%.0f", entity.getLoan().getLoanAmount()));
                }

                if (entity.getUserId() == null) {
                    jsonobj.put("aproved_by_admin", "");
                } else {
                    jsonobj.put("aproved_by_admin", entity.getUserId());
                }
                if (entity.getLoan().getDate_approved() == null) {
                    jsonobj.put("date_approve_by_admin", "");
                } else {
                    jsonobj.put("date_approve_by_admin", dateFormat.format(entity.getLoan().getDate_approved()));
                }
                if (entity.getUserId() == null) {
                    jsonobj.put("disburse_by_finance", "");
                } else {
                    jsonobj.put("disburse_by_finance", entity.getUserId());
                }
                if (entity.getTgl_input() == null) {
                    jsonobj.put("date_created", "");
                } else {
                    jsonobj.put("date_created", dateFormat.format(entity.getTgl_input()));
                }
                if (entity.getLoan().getDate_approved_by_finance() == null) {
                    jsonobj.put("date_disburse_by_finance", "");
                } else {
                    jsonobj.put("date_disburse_by_finance", dateFormat.format(entity.getLoan().getDate_approved_by_finance()));
                }
                if (entity.getLoan().getLoantype().getTypeLoan() == null) {
                    jsonobj.put("loan_type", "");
                } else {
                    jsonobj.put("loan_type", entity.getLoan().getLoantype().getTypeLoan());
                }
                if (entity.getLoan().getLoantype().getTypeLoan().equalsIgnoreCase("B")) {

                    if (entity.getLoan().getEngagement().getCaseID() == null) {
                        jsonobj.put("case_id", "");
                    } else {
                        jsonobj.put("case_id", entity.getLoan().getEngagement().getCaseID());
                    }
                }

                if (entity.getResponse() == null) {
                    jsonobj.put("loan_type", "");
                } else {
                    jsonobj.put("loan_type", entity.getResponse());
                }
                array.put(jsonobj);
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "list-history-of-loan-b-admin");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }
}
