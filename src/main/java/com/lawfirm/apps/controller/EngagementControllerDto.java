/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.ClientData;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.Engagement;
import com.lawfirm.apps.model.Member;
import com.lawfirm.apps.model.TeamMember;
import com.lawfirm.apps.response.Response;
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
import com.lawfirm.apps.support.api.EngagementDto;
import com.lawfirm.apps.support.api.MemberDto;
import com.lawfirm.apps.utils.CreateLog;
import com.lawfirm.apps.utils.Util;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping({"/engagement-dto"})
public class EngagementControllerDto {

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

    public EngagementControllerDto() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yy");
        this.sdfMonth = new SimpleDateFormat("MM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
    }

    @PermitAll
    @RequestMapping(value = "/manage-engagement-dto", method = RequestMethod.POST, produces = {"application/json"})
    public Response createEngagement(@RequestBody final EngagementDto object, Authentication authentication) {
        try {
            Date now = new Date();
//            System.out.print(" isi object " + object.toString());
            log.info(" isi object 2 " + object.toString());
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't approveByAdmin :");
                CreateLog.createJson(rs, "manage-engagement-dto");
                process = false;
                return rs;
            }

//            Engagement dataEngagement = new Engagement();
            if (object.getClient_name() == null) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, Client name filed can't be empty");
                CreateLog.createJson(rs, "createEngagement-dto");
                process = false;
                return rs;
            }
            if (object.getClient_name().length() > 80) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, Client name filed max 80");
                process = false;
                CreateLog.createJson(rs, "createEngagement-dto");
                return rs;
            }
            if (object.getAddress() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, ADDRESS filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement-dto");
                return rs;
            }
            if (object.getAddress().length() > 200) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, ADDRESS  filed max 200");
                process = false;
                CreateLog.createJson(rs, "createEngagement-dto");
                return rs;
            }
            if (object.getNpwp() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, NPWP filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement-dto");
                return rs;
            }
            if (object.getNpwp().length() > 30) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, ADDRESS  filed max 20");
                process = false;
                CreateLog.createJson(rs, "createEngagement-dto");
                return rs;
            }

            if (object.getPic() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, PIC filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement-dto");
                return rs;
            }
            if (object.getPic().length() > 20) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, PIC  filed max 20");
                process = false;
                CreateLog.createJson(rs, "createEngagement-dto");
                return rs;
            }
            if (object.getProfesional_fee() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, PERSONAL FEE filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement-dto");
                return rs;
            }
            if (object.getCase_over_view() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, CASE OVERVIEW filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement-dto");
                return rs;
            }

            Employee cekEMP = employeeService.findById(entityEmp.getIdEmployee());
            if (!"dmp".equals(cekEMP.getRoleName())) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, CASE OVERVIEW filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement-dto");
                return rs;
            }
            if (process) {

                ClientData dataClient = clientDataService.findBydataClient(object.getClient_name(), object.getAddress(), object.getNpwp());
                Integer numberClient = 0;
                String client_id = "CLIENT";
                if (dataClient != null) {
                    log.info("dataClient : " + dataClient);
                    //chek by npwp
                    numberClient = clientDataService.generateCleintId(object.getNpwp());
                    if (numberClient == 0) {
                        numberClient = 1;
                    } else {
                        numberClient = numberClient + 1;
                    }
                    //chek by cleintid
                    //on proggress
                    ClientData check = clientDataService.checkCI(client_id + Util.setNumber(numberClient.toString()));
                    if (check == null) {
                        numberClient = 1;

                        dataClient.setClientId(client_id + Util.setNumber(numberClient.toString()));
                    } else {
                        numberClient = numberClient + 1;
                        dataClient.setClientId(client_id + Util.setNumber(numberClient.toString()));
                    }
                    this.clientDataService.update(dataClient);
//                    
                    CaseDetails dataCaseDetails = new CaseDetails();
                    dataCaseDetails.setProfesionalFee(object.getProfesional_fee());
                    dataCaseDetails.setCaseOverview(object.getCase_over_view());
                    dataCaseDetails.setNote(object.getNotes());
                    dataCaseDetails.setEmployee(cekEMP);
                    dataCaseDetails.setClient(dataClient);
                    dataCaseDetails.setOperational_cost(object.getOperational_cost());
                    dataCaseDetails.setTgl_input(sdfYear.format(now));
                    CaseDetails caseDetails = this.caseDetailsService.create(dataCaseDetails);

                    if (caseDetails != null) {
                        TeamMember dataTeam = new TeamMember();
                        Member member = new Member();
                        log.info("process");
//                        Engagement dataEngagement = engagementService.findById(caseDetails.getEngagementId());
                        dataTeam.setEngagement(caseDetails);
                        dataTeam.setDmpId(cekEMP.getIdEmployee());
                        dataTeam.setFeeShare(object.getDmp_fee());
                        if (!object.getDescription().isEmpty()) {
                            dataTeam.setDescription(object.getDescription());
                        }
                        dataTeam.setIsActive(Boolean.TRUE);
                        TeamMember team = this.teamMemberService.create(dataTeam);
                        if (team == null) {
                            rs.setResponse_code("55");
                            rs.setInfo("failed");
                            rs.setResponse("team null");
                            CreateLog.createJson(rs, "add-team-member-dto");
                            return rs;
                        }
//                        EngagementDto ObjectEngagement = new EngagementDto();
//                        ObjectEngagement.setEngagement_id(dataCaseDetails.getEngagementId());
//                        ObjectEngagement.setDescription(object.getDescription());
//                        ObjectEngagement.setId_employee(cekEMP.getIdEmployee());
//                        ObjectEngagement.setEmployee_id(object.getEmployee_id());
//                        ObjectEngagement.setEmployee_name(object.getEmployee_name());
//                        ObjectEngagement.setDmp_fee(object.getDmp_fee());
//                        ObjectEngagement.setFee_share(object.getFee_share());
//                        addTeamMember(ObjectEngagement);
                        for (int i = 0; i < object.getMembers().size(); i++) {
                            Member dataM = new Member();
                            MemberDto memberDto = object.getMembers().get(i);
                            Employee dataEmployee = employeeService.findByEmployeeId(memberDto.getEmployee_id());
                            dataM.setEmployee(dataEmployee);
                            dataM.setFeeShare(memberDto.getFee_share());
                            dataM.setTeamMember(team);
                            member = memberServiceIface.create(dataM);
                        }
                        if (member != null) {
                            rs.setResponse_code("00");
                            rs.setInfo("Sucess");
                            rs.setResponse("Success Create Team Member :");
                            CreateLog.createJson(rs, "add-team-member-dto");
                            return rs;
                        }
                    }
                } else {

                    log.info("dataClient nulls");
                    ClientData newClient = new ClientData();
                    newClient.setClientName(object.getClient_name());
                    newClient.setAddress(object.getAddress());
                    newClient.setNpwp(object.getNpwp());
                    newClient.setPic(object.getPic());
                    //chek by npwp
                    numberClient = clientDataService.generateCleintId(object.getNpwp());
                    if (numberClient == 0) {
                        numberClient = 1;
                    } else {
                        numberClient = numberClient + 1;
                    }
                    //chek by cleintid
                    ClientData check = clientDataService.checkCI(client_id + Util.setNumber(numberClient.toString()));
                    if (check == null) {
                        //chek by cleintid
                        //on proggress
                        numberClient = 1;
                        newClient.setClientId(client_id + Util.setNumber(numberClient.toString()));
                    } else {
                        numberClient = numberClient + 1;
                        newClient.setClientId(client_id + Util.setNumber(numberClient.toString()));
                    }

//                    if (newClient != null) {
                    log.info("newClient : " + newClient);
                    CaseDetails dataCaseDetails = new CaseDetails();
                    dataCaseDetails.setProfesionalFee(object.getProfesional_fee());
                    dataCaseDetails.setCaseOverview(object.getCase_over_view());
                    dataCaseDetails.setNote(object.getNotes());
                    dataCaseDetails.setEmployee(cekEMP);
                    dataCaseDetails.setClient(dataClient);
                    dataCaseDetails.setOperational_cost(object.getOperational_cost());
                    dataCaseDetails.setTgl_input(sdfYear.format(now));
//                    dataCaseDetails.setClient(newClient);
                    CaseDetails caseDetails = this.caseDetailsService.create(dataCaseDetails);
                    ClientData dClient = clientDataService.create(newClient);
                    log.info("isi : " + dClient.getClientName());
                    if (dClient != null) {
//                        rs.setResponse_code("01");
//                        rs.setInfo("success");
//                        rs.setResponse("Create Engagement Success");
//                        return rs;
//                        EngagementApi postObjcet = new EngagementApi();
//                        EngagementDto ObjectEngagement = new EngagementDto();
//                        ObjectEngagement.setEngagement_id(dataCaseDetails.getEngagementId());
//                        ObjectEngagement.setDescription(object.getDescription());
//                        ObjectEngagement.setId_employee(cekEMP.getIdEmployee());
//                        ObjectEngagement.setEmployee_id(object.getEmployee_id());
//                        ObjectEngagement.setEmployee_name(object.getEmployee_name());
//                        ObjectEngagement.setDmp_fee(object.getDmp_fee());
//                        ObjectEngagement.setFee_share(object.getFee_share());
//                        addTeamMember(ObjectEngagement);
                        TeamMember dataTeam = new TeamMember();
                        Member member = new Member();
                        log.info("process");
//                        Engagement dataEngagement = engagementService.findById(caseDetails.getEngagementId());
                        dataTeam.setEngagement(caseDetails);
                        dataTeam.setDmpId(cekEMP.getIdEmployee());
                        dataTeam.setFeeShare(object.getDmp_fee());
                        if (!object.getDescription().isEmpty()) {
                            dataTeam.setDescription(object.getDescription());
                        }
                        dataTeam.setIsActive(Boolean.TRUE);
                        TeamMember team = this.teamMemberService.create(dataTeam);
                        if (team == null) {
                            rs.setResponse_code("55");
                            rs.setInfo("failed");
                            rs.setResponse("team null");
                            CreateLog.createJson(rs, "add-team-member-dto");
                            return rs;
                        }
//                        EngagementDto ObjectEngagement = new EngagementDto();
//                        ObjectEngagement.setEngagement_id(dataCaseDetails.getEngagementId());
//                        ObjectEngagement.setDescription(object.getDescription());
//                        ObjectEngagement.setId_employee(cekEMP.getIdEmployee());
//                        ObjectEngagement.setEmployee_id(object.getEmployee_id());
//                        ObjectEngagement.setEmployee_name(object.getEmployee_name());
//                        ObjectEngagement.setDmp_fee(object.getDmp_fee());
//                        ObjectEngagement.setFee_share(object.getFee_share());
//                        addTeamMember(ObjectEngagement);
                        for (int i = 0; i < object.getMembers().size(); i++) {
                            Member dataM = new Member();
                            MemberDto memberDto = object.getMembers().get(i);
                            Employee dataEmployee = employeeService.findByEmployeeId(memberDto.getEmployee_id());
                            dataM.setEmployee(dataEmployee);
                            dataM.setFeeShare(memberDto.getFee_share());
                            dataM.setTeamMember(team);
                            member = memberServiceIface.create(dataM);
                        }
                        if (member != null) {
                            rs.setResponse_code("00");
                            rs.setInfo("Sucess");
                            rs.setResponse("Success Create Team Member :");
                            CreateLog.createJson(rs, "add-team-member-dto");
                            return rs;
                        }
                    } else {
                        rs.setResponse_code("05");
                        rs.setInfo("failed");
                        rs.setResponse("Create Engagement Failed");
                        CreateLog.createJson(rs, "createEngagement-dto");
                        return rs;

                    }
//                    }

                }
//                rs.setResponse_code("05");
//                rs.setInfo("failed");
//                rs.setResponse("Create Engagement Failed");
//                CreateLog.createJson(rs, "createEngagement");
//                return rs;
            } else {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed");
                process = false;
                CreateLog.createJson(rs, "createEngagement-dto");
//                return rs;
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
