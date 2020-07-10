/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.ClientData;
import com.lawfirm.apps.model.Engagement;
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
import com.lawfirm.apps.support.api.EngagementApi;
import com.lawfirm.apps.response.Response;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping({"/engagement"})
public class EngagementController {

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

    public EngagementController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("ddMMyyyy");
    }

    @PermitAll
    @RequestMapping(value = "/manage-engagement", method = RequestMethod.POST, produces = {"application/json"})
    public Response createEngagement(@RequestBody final EngagementApi object) {
        try {
            System.out.print("isi object" + object.toString());
            Boolean process = true;
            Engagement dataEngagement = new Engagement();
            if (object.getClient_name() == null) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, Client name filed can't be empty");
            }
            if (object.getClient_name().length() > 80) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, Client name filed max 80");
                process = false;
            }
            if (object.getAddress() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, ADDRESS filed can't be empty");
                process = false;
            }
            if (object.getAddress().length() > 200) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, ADDRESS  filed max 200");
                process = false;
            }
            if (object.getNpwp() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, NPWP filed can't be empty");
                process = false;
            }
            if (object.getNpwp().length() > 30) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, ADDRESS  filed max 20");
                process = false;
            }

            if (object.getPic() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, PIC filed can't be empty");
                process = false;
            }
            if (object.getPic().length() > 20) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, PIC  filed max 20");
                process = false;
            }
            if (object.getProfesional_fee() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, PERSONAL FEE filed can't be empty");
                process = false;
            }
            if (object.getCase_over_view() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, CASE OVERVIEW filed can't be empty");
                process = false;
            }

            if (process) {
                log.info("process");

                ClientData dataClient = clientDataService.findBydataClient(object.getClient_name(), object.getAddress(), object.getNpwp());

                if (dataClient != null) {
                    log.info("dataClient : " + dataClient);
                    CaseDetails dataCaseDetails = new CaseDetails();
                    dataCaseDetails.setProfesionalFee(object.getProfesional_fee());
                    dataCaseDetails.setCaseOverview(object.getCase_over_view());
                    dataCaseDetails.setNote(object.getNotes());
                    dataCaseDetails.setClient(dataClient);
                    dataCaseDetails = this.caseDetailsService.create(dataCaseDetails);
                    if (dataCaseDetails != null) {
                        rs.setResponse_code("01");
                        rs.setInfo("success");
                        rs.setResponse("Create Engagement Success");
                        return rs;
                    }
                } else {
                    log.info("dataClient null ");
                    ClientData newClient = new ClientData();
                    newClient.setClientName(object.getClient_name());
                    newClient.setAddress(object.getAddress());
                    newClient.setNpwp(object.getNpwp());
                    newClient.setPic(object.getPic());

//                    if (newClient != null) {
                    log.info("newClient : " + newClient);

                    CaseDetails dataCaseDetails = new CaseDetails();

                    dataCaseDetails.setProfesionalFee(object.getProfesional_fee());
                    dataCaseDetails.setCaseOverview(object.getCase_over_view());
                    dataCaseDetails.setNote(object.getNotes());
//                    dataCaseDetails.setClient(newClient);
//                    dataCaseDetails = this.caseDetailsService.create(dataCaseDetails);

                    newClient.addEngagement(dataCaseDetails);
                    ClientData dClient = clientDataService.create(newClient);
                    log.info("isi : " + dClient.getClientName());

                    if (dClient != null) {
                        rs.setResponse_code("01");
                        rs.setInfo("success");
                        rs.setResponse("Create Engagement Success");
                        return rs;
                    }
//                    }

                }
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed");
            } else {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed");
            }

            return rs;
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());

            rs.setResponse_code("05");
            rs.setInfo("failed");
            rs.setResponse(ex.getMessage());
            return rs;
        }
//        rs.setResponse_code("05");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");

    }

    @PermitAll
    @PutMapping(value = "/add-member/{engagement_id}", produces = {"application/json"})
    public Response addTeamMember(@RequestBody final EngagementApi object, @PathVariable("engagement_id") Long engagement_id) {
        String[] employeeId = null;
        String[] employeeName = null;
        String[] feeSahre = null;
        Object emp_id = null;
        Object emp_name = null;
        Object fee_share = null;
        Boolean process = true;
//        Long engagement_id = object.getEngagement_id();
        Engagement dataEngagement = engagementService.findById(engagement_id);
        if (dataEngagement == null) {
            rs.setResponse_code("05");
            rs.setInfo("failed");
            rs.setResponse("Failed Ad Team Member");
            process = false;
        }
        if (object.getEmployee_id() != null) {
            emp_id = Arrays.toString(object.getEmployee_id()).trim().replaceAll("['\":<>\\[\\]\\r\\n-]", "");
//            for (int i = 0; i < object.getEmployee_id().length; i++) {
//                emp_id = object.getEmployee_id();
//            }
            log.info("emp_id == " + emp_id);
        } else {
            emp_id = null;
            log.info("emp_id == null ");
            rs.setResponse_code("05");
            rs.setInfo("failed");
            rs.setResponse("Employee Not Found");
            process = false;
        }
        if (object.getEmployee_name() != null) {
            emp_name = Arrays.toString(object.getEmployee_name()).trim().replaceAll("['\":<>\\[\\]\\r\\n-]", "");
//            for (int i = 0; i < object.getEmployee_id().length; i++) {
//                emp_id = object.getEmployee_id();
//            }
            log.info("emp_name == " + emp_name);
        } else {
            emp_name = null;
            log.info("emp_id == null ");
            rs.setResponse_code("05");
            rs.setInfo("failed");
            rs.setResponse("Employee Not Found");
            process = false;
        }
        if (object.getFee_share() != null) {
            fee_share = Arrays.toString(object.getFee_share()).trim().replaceAll("['\":<>\\[\\]\\r\\n-]", "");
//            for (int i = 0; i < object.getEmployee_id().length; i++) {
//                emp_id = object.getEmployee_id();
//            }
            log.info("fee_share == " + fee_share);
        } else {
            fee_share = null;
            log.info("emp_id == null ");
            rs.setResponse_code("05");
            rs.setInfo("failed");
            rs.setResponse("Employee Not Found");
            process = false;
        }

        if (process) {
            employeeId = emp_id.toString().split(",");
            for (int l = 0; l < employeeId.length; l++) {
                String part = employeeId[l];
                System.out.println("@Check Part emp_id " + l + " :" + part.trim().replaceAll("['\":<>\\[\\],-]", ""));
                if (employeeId.length == 1) {

                }
            }
            employeeName = emp_name.toString().split(",");
            for (int l = 0; l < employeeName.length; l++) {
                String part = employeeName[l];
                System.out.println("@Check Part emp_name " + l + " :" + part.trim().replaceAll("['\":<>\\[\\],-]", ""));
                if (employeeName.length == 1) {

                }
            }
            feeSahre = fee_share.toString().split(",");
            for (int l = 0; l < feeSahre.length; l++) {
                String part = feeSahre[l];
                System.out.println("@Check Part fee_share " + l + " :" + part.trim().replaceAll("['\":<>\\[\\],-]", ""));
                if (feeSahre.length == 1) {

                }
            }
        }

        return rs;
    }

}
