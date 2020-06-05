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
import com.lawfirm.apps.utils.CustomErrorType;
import com.lawfirm.apps.utils.Response;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Slf4j
@RequestMapping({"/loan"})
public class LoanController {
    
    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;
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
    }
    
    @RequestMapping(value = "/createLoan", method = RequestMethod.POST, produces = {"application/json"})
    public Response createLoan(@RequestBody final LoanApi object) {
        try {
            Date todayDate = new Date();
            
            log.info("isi object " + object.toString());
            Boolean process = true;
            Loan dataLoan = new Loan();
//            LoanType typeLoan = new LoanType();
            Financial dataFinance = new Financial();
            LoanType typeLoan = loanTypeService.findByName(object.getLoan_type());
            if (typeLoan == null) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Loan Type Not found");
                process = false;
            }
            if ("A".equalsIgnoreCase(typeLoan.getTypeLoan())) {

//                Loan dataLoan = new Loan();
//                LoanType typeLoan = new LoanType();
//                Financial dataFinance = new Financial();
                Employee dataEMploye = employeeService.findById(object.getId_employee());
                log.info("isi dataEMploye " + dataEMploye.toString());
                if (dataEMploye == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Employee Id Not found");
                    process = false;
                }
                if (dateFormat.format(todayDate).compareTo(dateFormat.format(object.getRepayment_date())) == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("repayment_date not permission, " + "repayment_date : " + dateFormat.format(object.getRepayment_date()) + " equals  todayDate : " + dateFormat.format(todayDate));
                    process = false;
                }
                if (dateFormat.format(todayDate).compareTo(dateFormat.format(object.getRepayment_date())) > 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("repayment_date : " + dateFormat.format(object.getRepayment_date()) + " before  todayDate : " + dateFormat.format(todayDate));
                    process = false;
                }
                if (process) {
                    
                    dataLoan.setEmployee(dataEMploye);
                    String isi_tgl_repayment = null;
                    Date tgl_repayment = null;
                    if (object.getRepayment_date() != null) {
                        isi_tgl_repayment = dateFormat.format(object.getRepayment_date());
                        
                        tgl_repayment = dateFormat.parse(isi_tgl_repayment);
                    }
                    dataLoan.setRepayment_date(tgl_repayment);
                    dataLoan.setStatus("pending");
                    dataLoan.setLoanAmount(object.getLoan_amount());
                    dataLoan = loanService.create(dataLoan);
//                typeLoan.addLoan(dataLoan);
                    if (dataLoan != null) {
                        dataFinance.setLoan(dataLoan);
                        dataFinance.setCreated_date(now);
                        Financial dFinancial = financialService.create(dataFinance);
                        if (dFinancial != null) {
                            log.info("isi dLoan " + dataLoan.getLoanId().toString());
                            log.info("isi dFinancial " + dFinancial.getFinancialId().toString());
                            rs.setResponse_code("01");
                            rs.setInfo("Success");
                            rs.setResponse("Create Loan Apps Success");
                        }
                    }
                } else {
                    return rs;
                }
                
            }
            
            if ("B".equalsIgnoreCase(typeLoan.getTypeLoan())) {
                if (object.getCase_id() == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Create Loan Apps Type B Failed");
                    process = false;
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
                }
//                CaseDetails dataCase = new CaseDetails();
                if (object.getCase_id() == null) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Can't Access Loan Apps Type Type B");
                    process = false;
                }
                if (!dataEMploye.getRoleName().equalsIgnoreCase("lawyer")) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("Can't Access Loan Apps Type B");
                    process = false;
                }
                if (dateFormat.format(todayDate).compareTo(dateFormat.format(object.getRepayment_date())) == 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("repayment_date not permission" + "repayment_date : " + dateFormat.format(object.getRepayment_date()) + " equals  todayDate : " + dateFormat.format(todayDate));
                    process = false;
                }
                if (dateFormat.format(todayDate).compareTo(dateFormat.format(object.getRepayment_date())) > 0) {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("repayment_date : " + dateFormat.format(object.getRepayment_date()) + " before  todayDate : " + dateFormat.format(todayDate));
                    process = false;
                }
                if (process) {
                    
                    CaseDetails dataCase = caseDetailsService.findById(object.getCase_id());
                    dataLoan.setEmployee(dataEMploye);
                    String isi_tgl_repayment = null;
                    Date tgl_repayment = null;
                    if (object.getRepayment_date() != null) {
                        isi_tgl_repayment = dateFormat.format(object.getRepayment_date());
                        tgl_repayment = dateFormat.parse(isi_tgl_repayment);
                    }
                    dataLoan.setRepayment_date(tgl_repayment);
                    dataLoan.setLoanAmount(object.getLoan_amount());
                    dataLoan.setStatus("pending");
                    dataLoan.setEngagement(dataCase);
                    Loan dLoan = loanService.create(dataLoan);
                    if (dLoan != null) {
                        dataFinance.setLoan(dLoan);
                        dataFinance.setCreated_date(now);
                        Financial dFinancial = financialService.create(dataFinance);
                        if (dFinancial != null) {
                            log.info("isi dLoan " + dLoan.getLoanId().toString());
                            log.info("isi dFinancial " + dFinancial.getFinancialId().toString());
                            rs.setResponse_code("01");
                            rs.setInfo("Success");
                            rs.setResponse("Create Loan Apps Type B  Succes");
                        }
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
            return rs;
        }
        
    }

//    @RequestMapping(value = "/approveByAdmin", method = RequestMethod.POST, produces = {"application/json"})
    @PutMapping(value = "/approveByAdmin/{loan_id}", produces = {"application/json"})
    public Response approveByAdmin(@RequestBody final LoanApi object, @PathVariable("loan_id") Long loan_id) {
//  public Response approveByAdmin(@RequestBody final LoanApi object) {

        Boolean process = true;
//        Loan dataLoan = new Loan();
        LoanType typeLoan = new LoanType();
        Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
        Financial dataFinance = new Financial();
        if (dataEMploye == null) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Employee Id Not found");
            process = false;
        }
        if (dataEMploye.getRoleName().contentEquals("admin")) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Cannot Access This feature");
            process = false;
        }
        Loan dataLoan = loanService.findById(object.getLoan_id());
        if (dataLoan == null) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Loand id null, Cannot Access This feature");
            process = false;
        }
        if (process) {
            dataLoan.setAprovedByAdmin(dataEMploye.getName());
            dataLoan.setDate_approved(now);
            dataLoan.setStatus("approved by admin");
//        dataLoan.setIsActive("0");
            Loan upDataLoan = loanService.update(dataLoan);
            if (upDataLoan != null) {
                rs.setResponse_code("01");
                rs.setInfo("Success");
                rs.setResponse("Loan apps : " + dataLoan.getEmployee().getName() + " Approved");
            } else {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Loand id null, Cannot Access This feature");
            }
        }
        return rs;
    }

//  @RequestMapping(value = "/approveByFinance", method = RequestMethod.POST, produces = {"application/json"})
    @PutMapping(value = "/approveByFinance/{loan_id}", produces = {"application/json"})
    public Response approveByFinance(@RequestBody final LoanApi object, @PathVariable("loan_id") Long loan_id) {
        Boolean process = true;
        Loan dataLoan = new Loan();
        LoanType typeLoan = new LoanType();
        Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
        Financial dataFinance = financialService.findById(object.getFinancial_id());
        if (dataFinance == null) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Finance Id Not found");
            process = false;
        }
        if (dataEMploye == null) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Employee Id Not found");
            process = false;
        }
        if (dataEMploye.getRoleName().contentEquals("finance")) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Cannot Access This feature");
            process = false;
        }
        dataLoan = loanService.findById(object.getLoan_id());
        if (dataLoan == null) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Loand id null, Cannot Access This feature");
            process = false;
        }
        if (process) {
            dataLoan.setAprovedByFinance(dataEMploye.getName());
            dataLoan.setDate_approved_by_finance(now);
            dataLoan.setStatus("approved by finance");
            dataLoan.setIsActive("0");
            Loan upDataLoan = loanService.update(dataLoan);
            if (upDataLoan != null) {
                dataFinance.setDisburse_date(now);
                if (object.getOut_standing() != null) {
                    dataFinance.setOutStanding(object.getOut_standing());
                } else {
                    rs.setResponse_code("05");
                    rs.setInfo("Failed");
                    rs.setResponse("out Standing Field can't be null");
                }
                Financial upd_finance = financialService.update(dataFinance);
                if (upd_finance != null) {
                    rs.setResponse_code("01");
                    rs.setInfo("Success");
                    rs.setResponse("Loan apps Approved By :" + dataLoan.getAprovedByFinance());
                }
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Loand id null, Cannot Access This feature");
            } else {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Loand id null, Cannot Access This feature");
            }
        }
        return rs;
    }
    
    @PermitAll
    @PutMapping(value = "/deleteLoan/{loan_id}", produces = {"application/json"})
    public Response deleteLoan(@RequestBody final LoanApi object, @PathVariable("loan_id") Long loan_id) {
        Loan entity = loanService.findById(loan_id);
        Employee dataEMploye = employeeService.findById(object.getId_employee_admin());
        if (dataEMploye == null) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Employee Id Not found");
        }
        if (dataEMploye.getRoleName().contentEquals("admin")) {
            rs.setResponse_code("05");
            rs.setInfo("Failed");
            rs.setResponse("Cannot Access This feature");
        }
        if (entity != null) {
            entity.setIsDelete(true);
            entity.setIsActive("0");
            
            rs.setResponse_code("01");
            rs.setInfo("Succes");
            rs.setResponse("Loan Deleted");
        }
        return rs;
    }
    
    @PermitAll
    @RequestMapping(value = "/viewLoan", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<String> viewLoan() {
        try {
            int max = loanService.count();
            int start = 0;
            List<Loan> entityList = null;
            if (start == 0) {
                entityList = this.loanService.listLoan();
            } else {
                entityList = this.loanService.listLoanPaging(max, start);
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
                    jsonobj.put("aproved_by_finance", "");
                } else {
                    jsonobj.put("aproved_by_finance", entity.getAprovedByFinance());
                }
                if (entity.getDate_approved_by_finance() == null) {
                    jsonobj.put("date_approve_by_finance", "");
                } else {
                    jsonobj.put("date_approve_by_finance", dateFormat.format(entity.getDate_approved_by_finance()));
                }
                if (entity.getEngagement().getCaseID() == null) {
                    jsonobj.put("case_id", "");
                } else {
                    jsonobj.put("case_id", entity.getEngagement().getCaseID());
                }
                if (entity.getLoantype().getTypeLoan() == null) {
                    jsonobj.put("loan_type", "");
                } else {
                    jsonobj.put("loan_type", entity.getLoantype().getTypeLoan());
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
        }
        return new ResponseEntity(new CustomErrorType("Data Not Found "),
                HttpStatus.NOT_FOUND);
    }
}
