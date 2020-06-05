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
import com.lawfirm.apps.utils.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/createEngagement", method = RequestMethod.POST, produces = {"application/json"})
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
            if (object.getNpwp().length() > 20) {
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

                ClientData dataClient = clientDataService.findBydataClient(object.getClient_name(), object.getAddress(), object.getNpwp());
                if (dataClient != null) {
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
                    }
                } else {
                    ClientData newClient = new ClientData();
                    newClient.setClientName(object.getClient_name());
                    newClient.setAddress(object.getAddress());
                    newClient.setNpwp(object.getNpwp());
                    newClient.setPic(object.getPic());
                    newClient = clientDataService.create(newClient);
                    if (newClient != null) {
                        CaseDetails dataCaseDetails = new CaseDetails();
                        dataCaseDetails.setProfesionalFee(object.getProfesional_fee());
                        dataCaseDetails.setCaseOverview(object.getCase_over_view());
                        dataCaseDetails.setNote(object.getNotes());
                        dataCaseDetails.setClient(newClient);
                        dataCaseDetails = this.caseDetailsService.create(dataCaseDetails);
                        if (dataCaseDetails != null) {
                            rs.setResponse_code("01");
                            rs.setInfo("success");
                            rs.setResponse("Create Engagement Success");
                        }
                    }

                }
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

}
