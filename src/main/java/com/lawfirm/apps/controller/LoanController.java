/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.Financial;
import com.lawfirm.apps.model.Loan;
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
import com.lawfirm.apps.service.interfaces.MemberServiceIface;
import com.lawfirm.apps.service.interfaces.ProfessionalServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import com.lawfirm.apps.support.api.LoanApi;
import com.lawfirm.apps.response.Response;
import com.lawfirm.apps.utils.CreateLog;
import com.lawfirm.apps.utils.CustomErrorType;
import com.lawfirm.apps.utils.Util;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping({"/loan"})
public class LoanController {

    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;
    SimpleDateFormat sdfYear;
    SimpleDateFormat sdfMonth;
    SimpleDateFormat sdfMY;
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
    MemberServiceIface memberService;
    @Autowired
    ProfessionalServiceIface professionalService;
    @Autowired
    ReimbursementServiceIface reimbursementService;
    @Autowired
    TeamMemberServiceIface teamMemberService;

    public LoanController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yy");
        this.sdfMonth = new SimpleDateFormat("MM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
    }

    @RequestMapping(value = "/manage-loan", method = RequestMethod.POST, produces = {"application/json"})
    public Response createLoan(@RequestBody final LoanApi object) {
        try {
            Date todayDate = new Date();
            Date now = new Date();

            log.info("isi object " + object.toString());
            Boolean process = true;
            Loan dataLoan = new Loan();
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
                Employee dataEMploye = null;
                if (object.getId_employee() != null) {
                    dataEMploye = employeeService.findById(object.getId_employee());
                }
                if (object.getEmployeeId() != null) {
                    dataEMploye = employeeService.findByEmployee(object.getEmployeeId());
                }
//                log.info("isi dataEMploye " + dataEMploye.toString());
                if (dataEMploye == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Employee Id Not found");
                    process = false;
                    CreateLog.createJson(rs, "createLoan-employee");
                    return rs;
                }
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
                    if (object.getCreated_date() != null) {
                        isi_tgl_pengajuan = dateFormat.format(object.getCreated_date());

                        tgl_pengajuan = dateFormat.parse(isi_tgl_pengajuan);
                    }
                    dataLoan.setEmployee(dataEMploye);
                    dataLoan.setLoantype(typeLoan);
                    dataLoan.setTgl_input(sdfYear.format(now));
                    dataLoan.setDate_month(sdfMY.format(object.getCreated_date()));//sdfMonth.format(new Date())
                    dataLoan.setRepayment_date(tgl_repayment);
                    dataLoan.setDate_created(tgl_pengajuan);
                    dataLoan.setStatus("p");
//                    if (dataEMploye.getLoanAmount().compareTo(object.getLoan_amount()) == 0) {
//                       
//                    }
//                    if (dataEMploye.getLoanAmount() != null) {
                    if (dataEMploye.getLoanAmount().compareTo(object.getLoan_amount()) < 0) {
                        rs.setResponse_code("05");
                        rs.setInfo("Failed");
                        rs.setResponse("limit loan : " + dataEMploye.getLoanAmount());
                        process = false;
                        CreateLog.createJson(rs, "createLoan-employee");
                        return rs;
                    }
//                    }
                    Double cek_loan = this.loanService.loanEmp(dataEMploye.getIdEmployee().toString(), sdfMY.format(object.getCreated_date()));
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

                    if (dataEMploye.getLoanAmount().compareTo(balance) < 0) {
                        rs.setResponse_code("05");
                        rs.setInfo("Failed");
                        rs.setResponse("limit loan : " + String.format("%.0f", dataEMploye.getLoanAmount()) + " Total Loan this month : " + cek_loan);
                        process = false;
                        CreateLog.createJson(rs, "createLoan-employee");
                        return rs;
                    }
//                    }
                    dataLoan.setLoanAmount(object.getLoan_amount());
                    if (sdfYear.format(now).compareTo(sdfYear.format(object.getCreated_date())) == 0) {
                        number = loanService.generateLoanId("A", dataEMploye.getEmployeeId(), sdfYear.format(object.getCreated_date()));//dateFormat.format(todayDate));
                        if (number == 0) {
                            number = 1;
                        }
                    } else {
                        number = 1;
                    }

                    Loan entity = loanService.findByLoanId("A" + dataEMploye.getEmployeeId() + year_val + Util.setNumbering(number.toString()));
                    if (entity == null) {
                        number = 1;
                        load_id = "A" + dataEMploye.getEmployeeId() + year_val + Util.setNumbering(number.toString());
                        dataLoan.setLoanId(load_id);
                    } else {
                        number = number + 1;
                        load_id = "A" + dataEMploye.getEmployeeId() + year_val + Util.setNumbering(number.toString());
                        dataLoan.setLoanId(load_id);

                    }

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

            if ("B".equalsIgnoreCase(object.getLoan_type())) {
                if (object.getCase_id() == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Create Loan Apps Type B Failed");
                    process = false;
                    CreateLog.createJson(rs, "createLoan-employee");
                }
//                Loan dataLoan = new Loan();
//                LoanType typeLoan = new LoanType();
//                Financial dataFinance = new Financial();
                Employee dataEMploye = employeeService.findById(object.getId_employee());
                if (dataEMploye == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Employee Id Not found");
                    process = false;
                    CreateLog.createJson(rs, "createLoan-employee");
                }
//                CaseDetails dataCase = new CaseDetails();
                if (object.getCase_id() == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Can't Access Loan Apps Type Type B");
                    process = false;
                    CreateLog.createJson(rs, "createLoan-employee");
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
                    CreateLog.createJson(rs, "createLoan-employee");
                }
                if (dateFormat.format(todayDate).compareTo(dateFormat.format(object.getRepayment_date())) > 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("repayment_date : " + dateFormat.format(object.getRepayment_date()) + " before  todayDate : " + dateFormat.format(todayDate));
                    process = false;
                    CreateLog.createJson(rs, "createLoan-employee");
                }
                if (process) {

                    CaseDetails dataCase = caseDetailsService.findById(object.getCase_id());
                    dataLoan.setEmployee(dataEMploye);
                    String isi_tgl_repayment = null;
                    Date tgl_repayment = null;
                    Integer number = 1;
                    String year_val = sdfYear.format(now);
                    String load_id = null;
                    if (object.getRepayment_date() != null) {
                        isi_tgl_repayment = dateFormat.format(object.getRepayment_date());
                        tgl_repayment = dateFormat.parse(isi_tgl_repayment);
                    }
                    dataLoan.setTgl_input(sdfYear.format(now));
                    dataLoan.setRepayment_date(tgl_repayment);
                    dataLoan.setLoanAmount(object.getLoan_amount());
                    dataLoan.setStatus("p");
                    dataLoan.setEngagement(dataCase);
                    number = loanService.generateLoanId("B", dataEMploye.getEmployeeId(), dateFormat.format(todayDate));
                    Loan entity = loanService.findByLoanId("B" + dataEMploye.getEmployeeId() + year_val + Util.setNumbering(number.toString()));
                    if (entity == null) {
                        number = 1;
                        load_id = "B" + dataEMploye.getEmployeeId() + year_val + Util.setNumbering(number.toString());
                        dataLoan.setLoanId(load_id);
                    } else {
                        number = number + 1;
                        load_id = "B" + dataEMploye.getEmployeeId() + year_val + Util.setNumbering(number.toString());
                        dataLoan.setLoanId(load_id);

                    }
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
                        rs.setResponse("Create Loan Apps Type B  Succes");
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

//    @RequestMapping(value = "/approveByAdmin", method = RequestMethod.POST, produces = {"application/json"})
    @PutMapping(value = "/approved/by-admin/{loan_id}", produces = {"application/json"})
    public Response approveByAdmin(@RequestBody final LoanApi object, @PathVariable("loan_id") Long loan_id
    ) {
//  public Response approveByAdmin(@RequestBody final LoanApi object) {
        Date now = new Date();
        Boolean process = true;
//      Loan dataLoan = new Loan();
        LoanType typeLoan = new LoanType();
        Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
//        Financial dataFinance = new Financial();
//
        if (dataEMploye == null) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Employee Id Not found");
            CreateLog.createJson(rs, "loan-approve-ByAdmin");
            process = false;

        }
//        if (dataEMploye.getRoleName().contentEquals("admin")) {
//            rs.setResponse_code("05");
//            rs.setInfo("Failed");
//            rs.setResponse("Cannot Access This feature");
//            process = false;
//        }
        Loan dataLoan = loanService.findByIdLoan(loan_id);//findById
        if (dataLoan == null) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Loand id null, Cannot Access This feature");
            CreateLog.createJson(rs, "loan-approve-ByAdmin");
            process = false;
        }
        if (dataLoan.getIsActive().contentEquals("0") || dataLoan.getStatus().contentEquals("d")) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Loand Already Approved, at : " + dataLoan.getAprovedByAdmin());
            CreateLog.createJson(rs, "loan-approve-ByAdmin");
            process = false;
        }

        if (process) {
            dataLoan.setAprovedByAdmin(dataEMploye.getName());
            dataLoan.setDate_approved(now);
            dataLoan.setStatus("a");
            dataLoan.setIsActive("0");
//        dataLoan.setIsActive("0");
            Loan upDataLoan = loanService.update(dataLoan);
            if (upDataLoan != null) {
                rs.setResponse_code("01");
                rs.setInfo("Success");
                rs.setResponse("Loan apps : " + dataLoan.getEmployee().getName() + " Approved");
                CreateLog.createJson(rs, "loan-approve-ByAdmin");
            } else {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Loand id null, Cannot Access This feature");
                CreateLog.createJson(rs, "loan-approve-ByAdmin");
            }
        }
        return rs;
    }

//    @RequestMapping(value = "/approved/by-finance", method = RequestMethod.POST, produces = {"application/json"})
    @PutMapping(value = "/approved/by-finance/{loan_id}", produces = {"application/json"})
    public Response approveByFinance(@RequestBody final LoanApi object, @PathVariable("loan_id") Long loan_id) {
        Boolean process = true;
        Loan dataLoan = new Loan();
        LoanType typeLoan = new LoanType();
        Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
//        Financial dataFinance = financialService.findById(object.getFinancial_id());
        Financial dataFinance = new Financial();
//        if (dataFinance == null) {
//            rs.setResponse_code("05");
//            rs.setInfo("Failed");
//            rs.setResponse("Finance Id Not found");
//            CreateLog.createJson(rs, "loan-approve-Byfinance");
//            process = false;
//        }
        if (dataEMploye == null) {
            rs.setResponse_code("05");
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
        dataLoan = loanService.findById(object.getLoan_id());
        if (dataLoan == null) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Loand id null, Cannot Access This feature");
            CreateLog.createJson(rs, "loan-approve-Byfinance");
            process = false;
        }
        if (dataLoan.getIsActive().contentEquals("1")) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Loan apps Must approve by ADMIN");
            CreateLog.createJson(rs, "loan-approve-Byfinance");
            process = false;
        }
        if (process) {
            dataLoan.setAprovedByFinance(dataEMploye.getName());
            dataLoan.setDate_approved_by_finance(new Date());
            dataLoan.setStatus("d");
            dataLoan.setIsActive("0");
            Loan upDataLoan = loanService.update(dataLoan);
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
                }
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Loand id null, Cannot Access This feature");
                CreateLog.createJson(rs, "loan-approve-Byfinance");
            } else {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Loand id null, Cannot Access This feature");
                CreateLog.createJson(rs, "loan-approve-Byfinance");
            }
        }
        return rs;
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

    @RequestMapping(value = "/view/list-loan", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<String> viewLoan() {
        try {
            int max = loanService.count();
            int start = 0;
            List<Loan> entityList = null;
            if (start == 0) {
                entityList = this.loanService.listLoan(0, 0);
            } else {
                entityList = this.loanService.listLoan(max, start);
            }

            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                JSONObject jsonobj = new JSONObject();
                Loan entity = (Loan) entityList.get(i);
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
                array.put(jsonobj);
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/view/loans/{id_employee}", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<String> viewLoans(@PathVariable("id_employee") Long id_employee) {
        try {

            List<Loan> entityList = this.loanService.findByEmployee(id_employee.toString());

            JSONArray array = new JSONArray();
            for (int i = 0; i < entityList.size(); i++) {
                JSONObject jsonobj = new JSONObject();
                Loan entity = (Loan) entityList.get(i);
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
                array.put(jsonobj);
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity(new CustomErrorType("Data Not Found "),
//                HttpStatus.NOT_FOUND);
    }
}
