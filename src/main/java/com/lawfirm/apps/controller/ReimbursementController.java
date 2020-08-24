/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.Reimbursement;
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
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import com.lawfirm.apps.support.api.DataEmployee;
import com.lawfirm.apps.support.api.ReimbursementApi;
import com.lawfirm.apps.utils.CreateLog;
import com.xss.filter.annotation.XxsFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(path = "/managed-reimbursement/{case_id}", produces = {"application/json"})
    @XxsFilter
    public Response createReimburse(@RequestBody final ReimbursementApi object, @PathVariable("case_id") String case_id, Authentication authentication) {
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
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }
//            Employee dataEmp = employeeService.findById(id_employee_admin); 
            Employee dataEmp = employeeService.findById(entityEmp.getIdEmployee());

            log.info("dataEmp : " + dataEmp.getRoleName());
            if (dataEmp == null) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }
            if (!dataEmp.getRoleName().matches("dmp")) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Cannot Access This feature");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }
            CaseDetails entity = caseDetailsService.findCaseId(case_id);
            if (entity == null) {
                rs.setResponse_code("05");
                rs.setInfo("Failed");
                rs.setResponse("Case ID : " + case_id + " not Found");
                CreateLog.createJson(rs, "create-reimburse");
                process = false;
                return rs;
            }
            if (process) {
                Reimbursement dataReimbursement = new Reimbursement();
            }

        } catch (Exception ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "create-reimburse");
        }
//        rs.setResponse_code("05");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");
        return rs;
    }

}
