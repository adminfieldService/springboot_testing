/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.CaseDocument;
import com.lawfirm.apps.model.ClientData;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.Engagement;
import com.lawfirm.apps.model.EngagementHistory;
import com.lawfirm.apps.model.Events;
import com.lawfirm.apps.model.Member;
import com.lawfirm.apps.model.TeamMember;
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
import com.lawfirm.apps.support.api.EngagementApi;
import com.lawfirm.apps.response.Response;
import com.lawfirm.apps.service.interfaces.EngagementHistoryServiceIface;
import com.lawfirm.apps.service.interfaces.EventServiceIface;
import com.lawfirm.apps.service.interfaces.MemberServiceIface;
import com.lawfirm.apps.support.api.EventsApi;
import com.lawfirm.apps.utils.CreateLog;
import com.lawfirm.apps.utils.CustomErrorType;
import com.lawfirm.apps.utils.Util;
import com.xss.filter.annotation.XxsFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    EngagementHistoryServiceIface engagementHistoryService;
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
    MemberServiceIface memberService;
    @Autowired
    EventServiceIface eventService;

    public EngagementController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yy");
        this.sdfMonth = new SimpleDateFormat("MM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
    }

//    @PermitAll
    @RequestMapping(value = "/manage-engagement", method = RequestMethod.POST, produces = {"application/json"})
    public Response createEngagement(@RequestBody final EngagementApi object, Authentication authentication) {
        try {
            Date now = new Date();
            System.out.print("isi object" + object.toString());
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't createEngagement :");
                CreateLog.createJson(rs, "createEngagement");
                process = false;
                return rs;
            }

//            Engagement dataEngagement = new Engagement();
            if (object.getClient_name() == null) {
                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, Client name filed can't be empty");
                CreateLog.createJson(rs, "createEngagement");
                process = false;
                return rs;
            }
            if (object.getClient_name().length() > 80) {

                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, Client name filed max 80");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getAddress() == null) {

                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, ADDRESS filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getAddress().length() > 200) {
                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, ADDRESS  filed max 200");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getNpwp() == null) {

                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, NPWP filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getNpwp().length() > 30) {
                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, ADDRESS  filed max 20");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }

            if (object.getPic() == null) {

                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, PIC filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getPic().length() > 20) {
                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, PIC  filed max 20");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getProfesional_fee() == null) {

                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, PERSONAL FEE filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getCase_over_view() == null) {

                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, CASE OVERVIEW filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }

            Employee cekEMP = employeeService.findById(entityEmp.getIdEmployee());
            if (!"dmp".equals(cekEMP.getRoleName())) {
                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, CASE OVERVIEW filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (process) {

                log.info("process");

                ClientData dataClient = clientDataService.findBydataClient(object.getClient_name(), object.getAddress(), object.getNpwp());
//                Integer numberClient = 0;
                String client_id = "CLIENT";
                if (dataClient != null) {
                    log.info("dataClient : " + dataClient);
                    Integer numberClient = clientDataService.generateCleintId(object.getNpwp());
                    if (numberClient == 0) {
                        numberClient = 1;
                    } else {
                        numberClient = numberClient + 1;
                    }
                    ClientData check = clientDataService.checkCI(client_id + Util.setNumber(numberClient.toString()));
                    if (check == null) {
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
                    dataCaseDetails.setStrategy(object.getStrategy());
                    dataCaseDetails.setPanitera(object.getPanitera());
//                    dataCaseDetails.setOperational_cost(object.getOperational_cost());
                    dataCaseDetails.setEmployee(cekEMP);
                    dataCaseDetails.setClient(dataClient);
                    dataCaseDetails.setTahun_input(sdfYear.format(now));
                    dataCaseDetails.setStatus("s");
                    dataCaseDetails = this.caseDetailsService.create(dataCaseDetails);
                    if (dataCaseDetails != null) {
//                        rs.setResponse_code("01");
//                        rs.setInfo("success");
//                        rs.setResponse("Create Engagement Success");
//                        return rs;
                        EngagementApi ObjectEngagement = new EngagementApi();
                        ObjectEngagement.setEngagement_id(dataCaseDetails.getEngagementId());
                        ObjectEngagement.setDescription(object.getDescription());
                        ObjectEngagement.setId_employee(cekEMP.getIdEmployee());
                        ObjectEngagement.setEmployee_id(object.getEmployee_id());
                        ObjectEngagement.setEmployee_name(object.getEmployee_name());
                        ObjectEngagement.setDmp_fee(object.getDmp_fee());
                        ObjectEngagement.setFee_share(object.getFee_share());
                        ObjectEngagement.setPanitera(object.getPanitera());
                        ObjectEngagement.setStrategy(object.getStrategy());

                        addTeamMember(ObjectEngagement);
                    }
                } else {

                    log.info("dataClient nulls");
                    ClientData newClient = new ClientData();
                    newClient.setClientName(object.getClient_name());
                    newClient.setAddress(object.getAddress());
                    newClient.setNpwp(object.getNpwp());
                    newClient.setPic(object.getPic());
                    Integer numberClient = clientDataService.generateCleintId(object.getNpwp());
                    if (numberClient == 0) {
                        numberClient = 1;
                    } else {
                        numberClient = numberClient + 1;
                    }
                    ClientData check = clientDataService.checkCI(client_id + Util.setNumber(numberClient.toString()));
                    if (check == null) {
                        newClient.setClientId(client_id + Util.setNumber(numberClient.toString()));
                    } else {
                        numberClient = numberClient + 1;
                        newClient.setClientId(client_id + Util.setNumber(numberClient.toString()));
                    }

                    log.info("newClient : " + newClient);

                    CaseDetails dataCaseDetails = new CaseDetails();

                    dataCaseDetails.setProfesionalFee(object.getProfesional_fee());
                    dataCaseDetails.setCaseOverview(object.getCase_over_view());
                    dataCaseDetails.setNote(object.getNotes());
                    dataCaseDetails.setStrategy(object.getStrategy());
                    dataCaseDetails.setPanitera(object.getPanitera());
//                    dataCaseDetails.setOperational_cost(object.getOperational_cost());
                    dataCaseDetails.setProfesionalFeeNet(object.getProfesional_fee() * (0.75));
                    dataCaseDetails.setTahun_input(sdfYear.format(now));
                    dataCaseDetails.setEmployee(cekEMP);
                    dataCaseDetails.setStatus("s");
//                    dataCaseDetails.setClient(newClient);
//                    dataCaseDetails = this.caseDetailsService.create(dataCaseDetails);
                    newClient.addEngagement(dataCaseDetails);
                    ClientData dClient = clientDataService.create(newClient);
                    log.info("isi : " + dClient.getClientName());

                    if (dClient != null) {
//                        rs.setResponse_code("01");
//                        rs.setInfo("success");
//                        rs.setResponse("Create Engagement Success");
//                        return rs;
//                        EngagementApi postObjcet = new EngagementApi();
                        EngagementApi ObjectEngagement = new EngagementApi();
                        ObjectEngagement.setEngagement_id(dataCaseDetails.getEngagementId());
                        ObjectEngagement.setDescription(object.getDescription());
                        ObjectEngagement.setId_employee(cekEMP.getIdEmployee());
                        ObjectEngagement.setEmployee_id(object.getEmployee_id());
                        ObjectEngagement.setEmployee_name(object.getEmployee_name());
                        ObjectEngagement.setDmp_fee(object.getDmp_fee());
                        ObjectEngagement.setFee_share(object.getFee_share());
//                        ObjectEngagement.setPanitera(object.getPanitera());
//                        ObjectEngagement.setStrategy();
                        addTeamMember(ObjectEngagement);
                    } else {
                        rs.setResponse_code("55");
                        rs.setInfo("failed");
                        rs.setResponse("Create Engagement Failed");
                        CreateLog.createJson(rs, "createEngagement");
                        return rs;

                    }
//                    }

                }
//                rs.setResponse_code("55");
//                rs.setInfo("failed");
//                rs.setResponse("Create Engagement Failed");
//                CreateLog.createJson(rs, "createEngagement");
//                return rs;
            } else {
                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
//                return rs;
            }

            return rs;
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "manage-engagement");
            rs.setResponse_code("55");
            rs.setInfo("failed");
            rs.setResponse(ex.getMessage());
            return rs;
        }
//        rs.setResponse_code("55");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");

    }
//    @PutMapping(value = "/add-member/{engagement_id}", produces = {"application/json"})
//    public Response addTeamMember(@RequestBody final EngagementApi object, @PathVariable("engagement_id") Long engagement_id) {
//    @PutMapping(value = "/add-member/", produces = {"application/json"})

    @RequestMapping(value = "/add-member", method = RequestMethod.POST, produces = {"application/json"})
    public Response addTeamMember(@RequestBody final EngagementApi object) {
        try {
            Date now = new Date();
            log.info("msg obj : " + object);
            String[] employeeId = null;
            String[] employeeName = null;
            String[] feeSahre = null;
            Object emp_id = null;
            Object emp_name = null;
            Object fee_share = null;
            Boolean process = true;
            int nilai = 0;
//        Long engagement_id = object.getEngagement_id();
            TeamMember dataTeam = new TeamMember();
//        Employee dataEmp = new Employee();
            Engagement dataEngagement = engagementService.findById(object.getEngagement_id());
            if (dataEngagement == null) {
                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Failed Ad Team Member dataEngagement NULL ");
                process = false;
                CreateLog.createJson(rs, "add-team-member");
                return rs;
            }
            if (object.getEmployee_id() != null) {
                emp_id = Arrays.toString(object.getEmployee_id()).trim().replaceAll("['\":<>\\[\\]\\r\\n-]", "");
                log.info("emp_id == " + emp_id);
            } else {
                emp_id = null;
                log.info("emp_id == null ");
                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Employee Not Found emp_id NULL");
                process = false;
                CreateLog.createJson(rs, "add-team-member");
                return rs;
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
                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Employee Not Found");
                process = false;
                CreateLog.createJson(rs, "add-team-member");
                return rs;
            }
            if (object.getFee_share() != null) {
                fee_share = Arrays.toString(object.getFee_share()).trim().replaceAll("['\":<>\\[\\]\\r\\n-]", "");
//            for (int i = 0; i < object.getEmployee_id().length; i++) {
//                emp_id = object.getEmployee_id();
//            }
//            log.info("fee_share == " + fee_share);
            } else {
                fee_share = null;
                log.info("emp_id == null ");
                rs.setResponse_code("55");
                rs.setInfo("failed");
                rs.setResponse("Employee Not Found");
                process = false;
                CreateLog.createJson(rs, "add-team-member");
                return rs;
            }

            if (process) {
                Member member = null;

                dataTeam.setEngagement(dataEngagement);
                dataTeam.setDmpId(object.getId_employee());
                dataTeam.setFeeShare(object.getDmp_fee());
                dataTeam.setTahun_input(sdfYear.format(now));
                dataTeam.setDescription("TMCS" + sdfYear.format(now));
//                if (!object.getDescription().isEmpty()) {
//                    dataTeam.setDescription(object.getDescription());
//                }
                dataTeam.setIsActive(Boolean.TRUE);
                TeamMember team = this.teamMemberService.create(dataTeam);
                if (team == null) {
                    rs.setResponse_code("55");
                    rs.setInfo("failed");
                    rs.setResponse("team null");
                    CreateLog.createJson(rs, "add-team-member");
                    return rs;
                }
                employeeId = emp_id.toString().split(",");
                for (int l = 0; l < employeeId.length; l++) {
                    Member dataM = new Member();
                    String part = employeeId[l];
                    System.out.println("@Check Part emp_id " + l + " :" + part.trim().replaceAll("['\":<>\\[\\],-]", ""));
                    if (employeeId.length == 1) {
                        Employee dataEmployee = employeeService.findByEmployeeId(part.trim().replaceAll("['\":<>\\[\\],-]", ""));
                        dataM.setEmployee(dataEmployee);

                    } else {
                        Employee dataEmployee = employeeService.findByEmployeeId(part.trim().replaceAll("['\":<>\\[\\],-]", ""));
                        dataM.setEmployee(dataEmployee);
                    }
//            }
                    if (emp_name != null) {
                        employeeName = emp_name.toString().split(",");
//                   for (int l = 0; l < employeeName.length; l++) {
                        String part_emp = employeeName[l].trim().replaceAll("['\":<>\\[\\],-]", "");
                        System.out.println("@Check Part emp_name " + l + " :" + part_emp);
                        if (employeeName.length == 1) {

                        }
                    }

//            for (int l = 0; l < feeSahre.length; l++) {
                    feeSahre = fee_share.toString().split(",");
                    String part_fee = feeSahre[l].trim().replaceAll("['\":<>\\[\\],-]", "");
                    System.out.println("@Check Part fee_share " + l + " :" + part.trim().replaceAll("['\":<>\\[\\],-]", ""));
                    if (feeSahre.length == 1) {
                        dataM.setFeeShare(Double.parseDouble(part_fee));
                    } else {
                        dataM.setFeeShare(Double.parseDouble(part_fee));
                    }
//                dataTeam.addMember(dataM);
                    dataM.setTeamMember(team);
                    member = memberService.create(dataM);
                }

                if (member != null) {
                    rs.setResponse_code("00");
                    rs.setInfo("Sucess");
                    rs.setResponse("Success Create Team Member :");
                    CreateLog.createJson(rs, "add-team-member");
//                return rs;
                }

            }
            return rs;

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "add-team-member");
            rs.setResponse_code("55");
            rs.setInfo("failed");
            rs.setResponse(ex.getMessage());
            return rs;
        }
//        rs.setResponse_code("55");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");

    }

    @RequestMapping(value = "/manage-engagement/{engagement_id}", method = RequestMethod.POST, produces = {"application/json"})
    public Response updateEngagement(@RequestBody final EngagementApi object, @PathVariable("engagement_id") Long engagement_id, Authentication authentication) {
        try {
            Date now = new Date();
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            log.info("engagement_id : " + engagement_id);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't update data Engagement :");
                process = false;
                CreateLog.createJson(rs, "updateEngagement");
                return rs;
            }
            if (!entityEmp.getRoleName().contentEquals("dmp")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + authentication.getAuthorities() + ", cannot access update-engagement, Permission denied");
                process = false;
                CreateLog.createJson(rs, "updateEngagement");
                return rs;
            }

            Engagement entity = this.engagementService.findById(engagement_id);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Engagement data Null");
                process = false;
                CreateLog.createJson(rs, "updateEngagement");
            }
            if (process) {

                Engagement update = this.engagementService.update(entity);
                if (update != null) {
                    rs.setResponse_code("55");
                    rs.setInfo("Success");
                    rs.setResponse("Success update data Engagement ");
                    CreateLog.createJson(rs, "updateEngagement");
                }
            }
            return rs;
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "updateEngagement");
            rs.setResponse_code("55");
            rs.setInfo("failed");
            rs.setResponse(ex.getMessage());
            return rs;
        }

    }

    @RequestMapping(value = "/approval/{engagement_id}/by-admin", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response approveByAdmin(@RequestBody final EngagementApi object, @PathVariable("engagement_id") Long engagement_id, Authentication authentication) {
        try {
            Date now = new Date();
//            Integer number = null;
            EngagementHistory enHistory = new EngagementHistory();
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            log.info("engagement_id : " + engagement_id);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't approveByAdmin :");
                CreateLog.createJson(rs, "approval-ByAdmin");
                return rs;
            }
            if (!entityEmp.getRoleName().contentEquals("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entityEmp.getRoleName() + " permission deny ");
                CreateLog.createJson(rs, "approval-ByAdmin");
                return rs;
            }
            CaseDetails entity = caseDetailsService.findById(engagement_id);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't approveByAdmin :");
                CreateLog.createJson(rs, "approval-ByAdmin");
                return rs;
            }
//            if (entity.getStatus().contains("r")) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("reject ByAdmin :");
//                CreateLog.createJson(rs, "approval-ByAdmin");
//                return rs;
//            }
            if (entity.getIsActive().contains("1")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Already approve By Admin :");
                CreateLog.createJson(rs, "approval-ByAdmin");
                return rs;
            }
            if (entity.getIsActive().contains("2")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("reject ByAdmin :");
                CreateLog.createJson(rs, "approval-ByAdmin");
                return rs;
            }
            if (entity.getIsActive().contains("4")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Case Status Close :");
                CreateLog.createJson(rs, "approval-ByAdmin");
                return rs;
            }

//            if (entity.getStatus().contains("a")) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("Alredy approve ByAdmin :");
//                CreateLog.createJson(rs, "approval-ByAdmin");
//                return rs;
//            }
            List<CaseDetails> generateCaseId = caseDetailsService.generateCaseId(entity.getTahun_input());
            Integer number = 0;
            if (generateCaseId != null || !generateCaseId.isEmpty()) {
                number = generateCaseId.size();
            }
            System.out.println("number size : " + number);
            String check_caseId = null;
            Log.info("number : " + number);
            if (number == 0) {
                number = 1;
                check_caseId = "CASE" + entity.getTahun_input() + Util.setNumbering(number.toString());
            } else {
                number = number + 1;
                check_caseId = "CASE" + entity.getTahun_input() + Util.setNumbering(number.toString());
            }
//            String caseId = null;

            Log.info("check_caseId : " + check_caseId);
            CaseDetails checkCaseId = caseDetailsService.findByCaseId(check_caseId, entity.getTahun_input());

            if (checkCaseId != null) {
                number = number + 1;
                check_caseId = "CASE" + entity.getTahun_input() + Util.setNumber(number.toString());
            }
            Log.info("check_caseId  findByCaseId : " + check_caseId);

            if (object.getDecision().contains("a")) {

                Integer numberTeam = 0;
                String team_Id = null;
//                List<TeamMember> generateTeamCaseId = teamMemberService.generateTeamCaseId(entity.getTahun_input());
//                if (generateTeamCaseId != null || !generateTeamCaseId.isEmpty()) {
//                    numberTeam = generateTeamCaseId.size();
//                }
                TeamMember dataTeamMember = teamMemberService.findByEngId(entity.getEngagementId());
//                Log.info("checkTeamMember.isPresent() : " + checkTeamMember.isPresent());
                if (dataTeamMember == null) {
                    rs.setResponse_code("50");
                    rs.setInfo("Error");
                    rs.setResponse("TeamMember EngagagemenId  : " + entity.getEngagementId() + " Not Found ");
                    CreateLog.createJson(rs, "approval-ByAdmin");
                    return rs;
                }

                entity.setCaseID(check_caseId);
                entity.setStatus(object.getDecision());
                entity.setIsActive("1");
//                entity.setProfesionalFeeNet(entity.getProfesionalFee() * (0.75));
                entity.setApproved_date(now);
                entity.setTahun_input(sdfYear.format(now));
                entity.setApprovedBy(entityEmp.getIdEmployee().toString());
//                
                enHistory.setEngagement(entity);
                enHistory.setResponse("approve");
                enHistory.setUserId(entityEmp.getIdEmployee());
                team_Id = Util.changeCase(check_caseId);

                Log.info("check_caseId  findByCaseId : " + team_Id);
                dataTeamMember.setDescription(team_Id);
                this.teamMemberService.update(dataTeamMember);
            }

            if (object.getDecision().contains("r")) {
                entity.setStatus(object.getDecision());
                entity.setIsActive("2");
                entity.setProfesionalFee(0d);
                entity.setApproved_date(now);
                entity.setTahun_input(sdfYear.format(now));
                entity.setApprovedBy(entityEmp.getIdEmployee().toString());
                enHistory.setEngagement(entity);
                enHistory.setUserId(entityEmp.getIdEmployee());
                enHistory.setResponse(object.getRemarks());

            }

            CaseDetails updateEng = caseDetailsService.update(entity);
            this.engagementHistoryService.create(enHistory);
            if (updateEng != null) {
                if (object.getDecision().contains("r")) {
                    rs.setResponse_code("00");
                    rs.setInfo("Sucess");
                    rs.setResponse("Reject BY : " + entityEmp.getEmployeeId());
                    CreateLog.createJson(rs, "approval-ByAdmin");
                }
                if (object.getDecision().contains("a")) {

                    rs.setResponse_code("00");
                    rs.setInfo("Sucess");
                    rs.setResponse("approval BY : " + entityEmp.getEmployeeId());
                    CreateLog.createJson(rs, "approval-ByAdmin");
                }
            }
            return rs;

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: " + ex.getMessage());

            rs.setResponse_code("55");
            rs.setInfo("failed");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(rs, "approval-ByAdmin");
            return rs;
        }
//        rs.setResponse_code("55");
//        rs.setInfo("Data null");
//        rs.setResponse("Create Employee Failed");

    }

    @RequestMapping(value = "/manage-engagement/view/by-employee", method = RequestMethod.GET, produces = {"application/json"})///{id_employee}
    @XxsFilter
//    public ResponseEntity<String> byEmployee(@PathVariable("id_employee") Long id_employee) {
    public ResponseEntity<String> byEmployee(Long id_employee, Authentication authentication) {
        try {
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entity = employeeService.findByEmployee(name);
            log.info("entity : " + entity);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "by-employee");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (entity.getRoleName().contains("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entity.getRoleName() + " permission deny ");
                CreateLog.createJson(rs, "by-employee");
                return new ResponseEntity(new CustomErrorType("55", "Error", "role : " + entity.getRoleName() + " permission deny "),
                        HttpStatus.NOT_FOUND);
            }
            if (entity.getRoleName().contains("sysadmin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entity.getRoleName() + " permission deny ");
                CreateLog.createJson(rs, "by-employee");
                return new ResponseEntity(new CustomErrorType("55", "Error", "role : " + entity.getRoleName() + " permission deny "),
                        HttpStatus.NOT_FOUND);
            }
//            Employee entity = employeeService.findById(id_employee);
//            Employee entity = employeeService.findById(entityE.getIdEmployee());
            System.out.println("entity.getIdEmployee() : " + entity.getIdEmployee());
            if (entity != null) {
                List<CaseDetails> listData = caseDetailsService.findByEmployee(entity.getIdEmployee());
                JSONArray array = new JSONArray();
//                JSONArray arrayM = new JSONArray();
                Long id_team = 0l;
                if (listData != null) {
                    for (int i = 0; i < listData.size(); i++) {
                        JSONObject obj = new JSONObject();
                        JSONObject objMember = new JSONObject();
                        JSONArray arrayM = new JSONArray();
                        CaseDetails data = (CaseDetails) listData.get(i);
                        if (data.getEngagementId() == null) {
                            obj.put("engagement_id", "");
                        } else {
                            obj.put("engagement_id", data.getEngagementId());
                        }
                        if (data.getCaseID() == null) {
                            obj.put("case_id", "");
                        } else {
                            obj.put("case_id", data.getCaseID());
                        }
                        if (data.getApprovedBy() == null) {
                            obj.put("approved_by", "");
                        } else {
                            Employee entityEmp = employeeService.findById(Long.parseLong(data.getApprovedBy()));
                            obj.put("approved_by", entityEmp.getName());

                        }
//                        if (data.getOperational_cost() == null) {
//                            obj.put("operational_cost", "");
//                        } else {
//                            obj.put("operational_cost", data.getOperational_cost());
//
//                        }
                        if (data.getProfesionalFee() == null) {
                            obj.put("professionalFee", "");
                        } else {
                            obj.put("professionalFee", data.getProfesionalFee());

                        }
                        if (data.getProfesionalFee() == null) {
                            obj.put("professional_fee", "");
                        } else {
                            obj.put("professional_fee", data.getProfesionalFee());

                        }
                        if (data.getProfesionalFeeNet() == null) {
                            obj.put("professional_fee_net", "");
                        } else {
                            obj.put("professional_fee_net", data.getProfesionalFeeNet());

                        }
                        if (data.getApproved_date() == null) {
                            obj.put("approved_date", "");
                        } else {
                            obj.put("approved_date", dateFormat.format(data.getApproved_date()));
                        }
                        if (data.getCaseOverview() == null) {
                            obj.put("case_over_view", "");
                        } else {
                            obj.put("case_over_view", data.getCaseOverview());
                        }
                        if (data.getStrategy() == null) {
                            obj.put("strategy", "");
                        } else {
                            obj.put("strategy", data.getStrategy());
                        }
                        if (data.getNote() == null) {
                            obj.put("notes", "");
                        } else {
                            obj.put("notes", data.getNote());
                        }
                        if (data.getPanitera() == null) {
                            obj.put("panitera", "");
                        } else {
                            obj.put("panitera", data.getPanitera());
                        }
                        if (data.getStatus() == null) {
                            obj.put("status", "");
                        } else {
                            obj.put("status", data.getStatus());
                        }
                        if (data.getClient() == null) {
                            obj.put("id_client", "");
                            obj.put("client_id", "");
                            obj.put("address", "");
                            obj.put("client_name", "");
                            obj.put("npwp", "");
                            obj.put("pic", "");
                        } else {
                            obj.put("id_client", data.getClient().getIdClient());
                            obj.put("client_id", data.getClient().getClientId());
                            obj.put("npwp", data.getClient().getNpwp());
                            obj.put("pic", data.getClient().getPic());
                            obj.put("address", data.getClient().getAddress());
                            obj.put("client_name", data.getClient().getClientName());
                        }

                        List<TeamMember> entityTeam = teamMemberService.listTeamMemberByEngagement(data.getEngagementId());
                        for (int j = 0; j < entityTeam.size(); j++) {
                            JSONObject objTeam = new JSONObject();
//                      
                            TeamMember dataTeam = entityTeam.get(j);
                            if (dataTeam == null) {
                                obj.put("description", "");
                            } else {
                                Employee getDmp = employeeService.findById(dataTeam.getDmpId());
                                if (getDmp == null) {
                                    obj.put("employee_id_dmp", "");
                                    obj.put("dmp_name", "");
                                    obj.put("description", "");
                                    obj.put("fee_share_dmp", "");
                                    obj.put("member_name", "");
                                    obj.put("employee_id", "");
                                    obj.put("fee_share", "");
                                } else {
                                    if (dataTeam.getTeamMemberId() != null) {
                                        id_team = dataTeam.getTeamMemberId();
                                    }

                                    obj.put("employee_id_dmp", getDmp.getEmployeeId());
                                    obj.put("dmp_name", getDmp.getName());
                                    obj.put("description", dataTeam.getDescription());
                                    obj.put("fee_share_dmp", dataTeam.getFeeShare());
                                }

                            }

                        }
//                        if (i != 0 || i == 1) {
                        if (id_team != null) {

                            List<Member> entityMember = memberService.findByIdTeam(id_team);
                            System.out.println("member : " + entityMember.size());
                            System.out.println("i : " + i);
                            for (int k = i; k < entityMember.size(); k++) {
//                            JSONObject objMember = new JSONObject();
                                objMember = new JSONObject();
                                Member dataMember = entityMember.get(k);

                                if (dataMember == null) {
                                    objMember.put("member_name", "");
                                    objMember.put("employee_id", "");
                                    objMember.put("fee_share", "");
                                } else {
                                    objMember.put("member_name", dataMember.getEmployee().getName());
                                    objMember.put("employee_id", dataMember.getEmployee().getEmployeeId());
                                    objMember.put("fee_share", dataMember.getFeeShare());

                                }
                                arrayM.put(objMember);
//                                    
                            }
                        } else {
                            objMember.put("member_name", "");
                            objMember.put("employee_id", "");
                            objMember.put("fee_share", "");
                        }
//                        }
                        obj.put("members", arrayM);
                        array.put(obj);
                    }
                }
                return ResponseEntity.ok(array.toString());
            }
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "view-by-employee");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
        CreateLog.createJson(HttpStatus.NOT_FOUND, "view-by-employee");
        return new ResponseEntity(new CustomErrorType("55", "Error", "NOT FOUND"),
                HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/manage-engagement/list-of-engagement", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> litEngagement(Authentication authentication) {
        try {

            String name = authentication.getName();
            log.info("name : " + name);
            Employee entity = employeeService.findByEmployee(name);
            log.info("entity : " + entity);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "lit-engagement");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
//            Employee entity = employeeService.findById(id_employee);
//            if (entity != null) {
            List<CaseDetails> listData = caseDetailsService.listCaseDetails();
            JSONArray array = new JSONArray();
            if (listData != null) {
                for (int i = 0; i < listData.size(); i++) {

                    JSONObject obj = new JSONObject();
                    CaseDetails data = (CaseDetails) listData.get(i);

                    if (data.getEngagementId() == null) {
                        obj.put("engagement_id", "");
                    } else {
                        obj.put("engagement_id", data.getEngagementId());
                    }
                    if (data.getCaseID() == null) {
                        obj.put("case_id", "");
                    } else {
                        obj.put("case_id", data.getCaseID());
                    }
//                    if (data.getOperational_cost() == null) {
//                        obj.put("operational_cost", "");
//                    } else {
//                        obj.put("operational_cost", data.getOperational_cost());
//
//                    }
                    if (data.getProfesionalFee() == null) {
                        obj.put("professionalFee", "");
                    } else {
                        obj.put("professionalFee", data.getProfesionalFee());

                    }
                    if (data.getProfesionalFee() == null) {
                        obj.put("professional_fee", "");
                    } else {
                        obj.put("professional_fee", data.getProfesionalFee());

                    }
                    if (data.getProfesionalFeeNet() == null) {
                        obj.put("professional_fee_net", "");
                    } else {
                        obj.put("professional_fee_net", data.getProfesionalFeeNet());

                    }
                    if (data.getApprovedBy() == null) {
                        obj.put("approved_by", "");
                    } else {
//                        Employee entityEmp = employeeService.findByEmployee(data.getApprovedBy());
                        Employee entityEmp = employeeService.findById(Long.parseLong(data.getApprovedBy()));
                        obj.put("approved_by", entityEmp.getName());
                    }
                    if (data.getApproved_date() == null) {
                        obj.put("approved_date", "");
                    } else {
                        obj.put("approved_date", dateFormat.format(data.getApproved_date()));
                    }
                    if (data.getCaseOverview() == null) {
                        obj.put("case_over_view", "");
                    } else {
                        obj.put("case_over_view", data.getCaseOverview());
                    }
                    if (data.getClient() == null) {
                        obj.put("id_client", "");
                        obj.put("client_id", "");
                        obj.put("address", "");
                        obj.put("client_name", "");
                        obj.put("npwp", "");
                        obj.put("pic", "");
                    } else {
                        obj.put("id_client", data.getClient().getIdClient());
                        obj.put("client_id", data.getClient().getClientId());
                        obj.put("npwp", data.getClient().getNpwp());
                        obj.put("pic", data.getClient().getPic());
                        obj.put("address", data.getClient().getAddress());
                        obj.put("client_name", data.getClient().getClientName());
                    }

                    if (data.getStrategy() == null) {
                        obj.put("strategy", "");
                    } else {
                        obj.put("strategy", data.getStrategy());
                    }
                    if (data.getNote() == null) {
                        obj.put("notes", "");
                    } else {
                        obj.put("notes", data.getNote());
                    }
                    if (data.getPanitera() == null) {
                        obj.put("panitera", "");
                    } else {
                        obj.put("panitera", data.getPanitera());
                    }
                    if (data.getTargetAchievement() == null) {
                        obj.put("target_achievement", "");
                    } else {
                        obj.put("target_achievement", data.getTargetAchievement());
                    }

                    if (data.getCaseID() == null) {
                        obj.put("case_id", "");
                    } else {
                        obj.put("case_id", data.getCaseID());
                    }
                    if (data.getStatus() == null) {
                        obj.put("status", "");
                    } else {
                        obj.put("status", data.getStatus());
                    }

//                    obj.put("approved_by", entityTeam.get(i));
                    array.put(obj);
                }
            }
            return ResponseEntity.ok(array.toString());
//            }
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "list-of-engagement");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/manage-engagement/{engagement_id}", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> listByEngagementId(@PathVariable("engagement_id") Long engagement_id) {
        try {

//            Employee entity = employeeService.findById(id_employee);
//            if (entity != null) {
            List<CaseDetails> listData = caseDetailsService.findByEngagementId(engagement_id);
            JSONArray array = new JSONArray();
//            JSONArray arrayM = new JSONArray();
            Long id_team = 0l;
            if (listData != null) {
                for (int i = 0; i < listData.size(); i++) {
                    JSONObject obj = new JSONObject();
                    JSONArray arrayM = new JSONArray();
                    JSONObject objMember = new JSONObject();
                    CaseDetails data = (CaseDetails) listData.get(i);
                    if (data.getEngagementId() == null) {
                        obj.put("engagement_id", "");
                    } else {
                        obj.put("engagement_id", data.getEngagementId());
                    }
                    if (data.getCaseID() == null) {
                        obj.put("case_id", "");
                    } else {
                        obj.put("case_id", data.getCaseID());
                    }
//                    if (data.getOperational_cost() == null) {
//                        obj.put("operational_cost", "");
//                    } else {
//                        obj.put("operational_cost", data.getOperational_cost());
//
//                    }
                    if (data.getProfesionalFee() == null) {
                        obj.put("professionalFee", "");
                    } else {
                        obj.put("professionalFee", data.getProfesionalFee());

                    }
                    if (data.getProfesionalFee() == null) {
                        obj.put("professional_fee", "");
                    } else {
                        obj.put("professional_fee", data.getProfesionalFee());

                    }
                    if (data.getProfesionalFeeNet() == null) {
                        obj.put("professional_fee_net", "");
                    } else {
                        obj.put("professional_fee_net", data.getProfesionalFeeNet());

                    }
                    if (data.getApprovedBy() == null) {
                        obj.put("approved_by", "");
                    } else {
//                        Employee entityEmp = employeeService.findByEmployee(data.getApprovedBy());
                        Employee entityEmp = employeeService.findById(Long.parseLong(data.getApprovedBy()));
                        obj.put("approved_by", entityEmp.getName());
                    }
                    if (data.getApproved_date() == null) {
                        obj.put("approved_date", "");
                    } else {
                        obj.put("approved_date", dateFormat.format(data.getApproved_date()));
                    }
                    if (data.getCaseOverview() == null) {
                        obj.put("case_over_view", "");
                    } else {
                        obj.put("case_over_view", data.getCaseOverview());
                    }
                    if (data.getClient() == null) {
                        obj.put("id_client", "");
                    } else {
                        obj.put("id_client", data.getClient().getIdClient());
                    }
                    if (data.getClient() == null) {
                        obj.put("client_id", "");
                    } else {
                        obj.put("client_id", data.getClient().getClientId());
                    }
                    if (data.getClient() == null) {
                        obj.put("npwp", "");
                    } else {
                        obj.put("npwp", data.getClient().getNpwp());
                    }
                    if (data.getClient() == null) {
                        obj.put("pic", "");
                    } else {
                        obj.put("pic", data.getClient().getPic());
                    }
                    if (data.getClient() == null) {
                        obj.put("address", "");
                    } else {
                        obj.put("address", data.getClient().getAddress());
                    }
                    if (data.getClient() == null) {
                        obj.put("client_name", "");
                    } else {
                        obj.put("client_name", data.getClient().getClientName());
                    }
                    if (data.getStrategy() == null) {
                        obj.put("strategy", "");
                    } else {
                        obj.put("strategy", data.getStrategy());
                    }
                    if (data.getPanitera() == null) {
                        obj.put("panitera", "");
                    } else {
                        obj.put("panitera", data.getPanitera());
                    }
                    if (data.getNote() == null) {
                        obj.put("notes", "");
                    } else {
                        obj.put("notes", data.getNote());
                    }
                    if (data.getStatus() == null) {
                        obj.put("status", "");
                    } else {
                        obj.put("status", data.getStatus());
                    }
                    List<TeamMember> entityTeam = teamMemberService.listTeamMemberByEngagement(data.getEngagementId());
                    for (int j = 0; j < entityTeam.size(); j++) {
                        JSONObject objTeam = new JSONObject();
//                      
                        TeamMember dataTeam = entityTeam.get(j);
                        if (dataTeam == null) {
                            obj.put("description", "");
                        } else {
                            Employee getDmp = employeeService.findById(dataTeam.getDmpId());
                            if (getDmp == null) {
                                obj.put("employee_id_dmp", "");
                                obj.put("dmp_name", "");
                                obj.put("description", "");
                                obj.put("fee_share_dmp", "");
                                obj.put("member_name", "");
                                obj.put("employee_id", "");
                                obj.put("fee_share", "");
                            } else {
                                if (dataTeam.getTeamMemberId() != null) {
                                    id_team = dataTeam.getTeamMemberId();
                                }

                                obj.put("employee_id_dmp", getDmp.getEmployeeId());
                                obj.put("dmp_name", getDmp.getName());
                                obj.put("description", dataTeam.getDescription());
                                obj.put("fee_share_dmp", dataTeam.getFeeShare());

//                            break;
//                            obj.put("member_name", entityMember.getDescription());
//                            arrayM.put(objMember);
                            }

                        }
//                        arrayM.put(objMember);
//                        obj.put("team", objTeam);
//                        obj.put("members", arrayM);

                    }
//                    if (i != 0 || i == 1) {
                    if (id_team != null) {

                        List<Member> entityMember = memberService.findByIdTeam(id_team);
                        System.out.println("member : " + entityMember.size());
                        System.out.println("i : " + i);
                        for (int k = i; k < entityMember.size(); k++) {
//                            JSONObject objMember = new JSONObject();
                            objMember = new JSONObject();
                            Member dataMember = entityMember.get(k);

                            if (dataMember == null) {
                                objMember.put("member_name", "");
                                objMember.put("employee_id", "");
                                objMember.put("fee_share", "");
                            } else {
                                objMember.put("member_name", dataMember.getEmployee().getName());
                                objMember.put("employee_id", dataMember.getEmployee().getEmployeeId());
                                objMember.put("fee_share", dataMember.getFeeShare());

                            }
                            arrayM.put(objMember);
//                                    
                        }
                    } else {
                        objMember.put("member_name", "");
                        objMember.put("employee_id", "");
                        objMember.put("fee_share", "");
                    }
//                    }
                    obj.put("members", arrayM);
                    array.put(obj);
                }
            }
            return ResponseEntity.ok(array.toString());
//            }
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "view-by-employee");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/manage-engagement/aproval-list/by-admin/{id_employee_admin}", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> byAdmin(@PathVariable("id_employee_admin") Long id_employee_admin, Authentication authentication) {
        try {
//          Engagement entity = engagementService.findById(id_employee_admin);
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entity = employeeService.findByEmployee(name);
            log.info("entity : " + entity);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "view-by-admin");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!entity.getRoleName().contentEquals("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entity.getRoleName() + " permission deny ");
                CreateLog.createJson(rs, "view-by-admin");
                return new ResponseEntity(new CustomErrorType("55", "Error", "role : " + entity.getRoleName() + " permission deny "),
                        HttpStatus.NOT_FOUND);
            }

//            List<CaseDetails> listData = caseDetailsService.findByAdmin(id_employee_admin);
            List<CaseDetails> listData = caseDetailsService.findByAdmin(entity.getIdEmployee());
            JSONArray array = new JSONArray();
//            JSONArray arrayM = new JSONArray();
            Long id_team = 0l;
            if (listData != null) {
                for (int i = 0; i < listData.size(); i++) {

                    JSONObject obj = new JSONObject();
                    JSONArray arrayM = new JSONArray();
                    JSONObject objMember = new JSONObject();

                    CaseDetails data = (CaseDetails) listData.get(i);
                    if (data.getEngagementId() == null) {
                        obj.put("engagement_id", "");
                    } else {
                        obj.put("engagement_id", data.getEngagementId());
                    }
                    if (data.getCaseID() == null) {
                        obj.put("case_id", "");
                    } else {
                        obj.put("case_id", data.getCaseID());
                    }
//                    if (data.getOperational_cost() == null) {
//                        obj.put("operational_cost", "");
//                    } else {
//                        obj.put("operational_cost", data.getOperational_cost());
//
//                    }
                    if (data.getProfesionalFee() == null) {
                        obj.put("professionalFee", "");
                    } else {
                        obj.put("professionalFee", data.getProfesionalFee());

                    }
                    if (data.getProfesionalFee() == null) {
                        obj.put("professional_fee", "");
                    } else {
                        obj.put("professional_fee", data.getProfesionalFee());

                    }
                    if (data.getProfesionalFeeNet() == null) {
                        obj.put("professional_fee_net", "");
                    } else {
                        obj.put("professional_fee_net", data.getProfesionalFeeNet());

                    }
                    if (data.getApprovedBy() == null) {
                        obj.put("approved_by", "");
                    } else {
                        Employee entityEmp = employeeService.findById(Long.parseLong(data.getApprovedBy()));
                        obj.put("approved_by", entityEmp.getName());
                    }
                    if (data.getApproved_date() == null) {
                        obj.put("approved_date", "");
                    } else {
                        obj.put("approved_date", dateFormat.format(data.getApproved_date()));
                    }
                    if (data.getCaseOverview() == null) {
                        obj.put("case_over_view", "");
                    } else {
                        obj.put("case_over_view", data.getCaseOverview());
                    }
                    if (data.getClient() == null) {
                        obj.put("id_client", "");
                    } else {
                        obj.put("id_client", data.getClient().getIdClient());
                    }
                    if (data.getClient() == null) {
                        obj.put("address", "");
                    } else {
                        obj.put("address", data.getClient().getAddress());
                    }
                    if (data.getClient() == null) {
                        obj.put("client_name", "");
                    } else {
                        obj.put("client_name", data.getClient().getClientName());
                    }
                    if (data.getClient() == null) {
                        obj.put("npwp", "");
                    } else {
                        obj.put("npwp", data.getClient().getNpwp());
                    }
                    if (data.getClient() == null) {
                        obj.put("pic", "");
                    } else {
                        obj.put("pic", data.getClient().getPic());
                    }
                    if (data.getStrategy() == null) {
                        obj.put("strategy", "");
                    } else {
                        obj.put("strategy", data.getStrategy());
                    }
                    if (data.getPanitera() == null) {
                        obj.put("panitera", "");
                    } else {
                        obj.put("panitera", data.getPanitera());
                    }
                    if (data.getNote() == null) {
                        obj.put("notes", "");
                    } else {
                        obj.put("notes", data.getNote());
                    }
                    if (data.getStatus() == null) {
                        obj.put("status", "");
                    } else {
                        obj.put("status", data.getStatus());
                    }
                    List<TeamMember> entityTeam = teamMemberService.listTeamMemberByEngagement(data.getEngagementId());
                    for (int j = 0; j < entityTeam.size(); j++) {
                        JSONObject objTeam = new JSONObject();
//                      
                        TeamMember dataTeam = entityTeam.get(j);
                        if (dataTeam == null) {
                            obj.put("description", "");
                        } else {
                            Employee getDmp = employeeService.findById(dataTeam.getDmpId());
                            if (getDmp == null) {
                                obj.put("employee_id_dmp", "");
                                obj.put("dmp_name", "");
                                obj.put("description", "");
                                obj.put("fee_share_dmp", "");
                                obj.put("member_name", "");
                                obj.put("employee_id", "");
                                obj.put("fee_share", "");
                            } else {
                                if (dataTeam.getTeamMemberId() != null) {
                                    id_team = dataTeam.getTeamMemberId();
                                }

                                obj.put("employee_id_dmp", getDmp.getEmployeeId());
                                obj.put("dmp_name", getDmp.getName());
                                obj.put("description", dataTeam.getDescription());
                                obj.put("fee_share_dmp", dataTeam.getFeeShare());

//                                break;
//                            obj.put("member_name", entityMember.getDescription());
//                                arrayM.put(objMember);
                            }

                        }
//                        arrayM.put(objMember);
//                        obj.put("team", objTeam);
//                        obj.put("members", arrayM);

                    }

                    if (id_team != null) {
                        List<Member> entityMember = memberService.findByIdTeam(id_team);
                        System.out.println("member : " + entityMember.size());
//                        System.out.println("i : " + i);

                        for (int k = 0; k < entityMember.size(); k++) {
//                            JSONObject objMember = new JSONObject();

                            objMember = new JSONObject();
                            Member dataMember = entityMember.get(k);

                            if (dataMember == null) {

                                objMember.put("member_name", "");
                                objMember.put("employee_id", "");
                                objMember.put("fee_share", "");

                            } else {
                                objMember.put("member_name", dataMember.getEmployee().getName());
                                objMember.put("employee_id", dataMember.getEmployee().getEmployeeId());
                                objMember.put("fee_share", dataMember.getFeeShare());
                            }
                            arrayM.put(objMember);
                            obj.put("members", arrayM);
                        }
                    } else {
                        objMember.put("member_name", "");
                        objMember.put("employee_id", "");
                        objMember.put("fee_share", "");
                        arrayM.put(objMember);
                    }
//                    }
//                    arrayM.put(objMember);

                    array.put(obj);
                }
            }
            return ResponseEntity.ok(array.toString());

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "view-by-admin");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/manage-engagement/{engagement_id}/event", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response createEvent(@PathVariable("engagement_id") Long engagement_id, @RequestBody EventsApi object, Authentication authentication) {
        try {

            Events enEvent = new Events();
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entity = employeeService.findByEmployee(name);
            log.info("entity : " + entity);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "create-event");
                process = false;
                return rs;
            }
            if (!entity.getRoleName().contentEquals("dmp")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entity.getRoleName() + " permission deny ");
                CreateLog.createJson(rs, "create-event");
                process = false;
                return rs;
            }

            CaseDetails dataCase = caseDetailsService.findById(engagement_id);
            if (dataCase == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature Engagement Id Not Found : " + engagement_id);
                CreateLog.createJson(rs, "create-event");
                process = false;
                return rs;
            }
            System.out.println("isi : " + object.getSchedule_date());
            String dt = dateFormat.format(object.getSchedule_date());
            Date schedule = dateFormat.parse(dt);
            System.out.println("dateFormat.format(object.getSchedule_date()) : " + schedule);
            if (schedule == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Field schedule date can't be NULL");
                CreateLog.createJson(rs, "create-event");
                process = false;
                return rs;
            }
            if (object.getEvent_name() == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Field Event Name, can't be NULL");
                CreateLog.createJson(rs, "create-event");
                process = false;
                return rs;
            }
            if (object.getEvent_name().length() > 30) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Field Event Name Max Length 30");
                CreateLog.createJson(rs, "create-event");
                process = false;
                return rs;
            }
            if (object.getEvent_type() == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Field Event Type, can't be NULL");
                CreateLog.createJson(rs, "create-event");
                process = false;
                return rs;
            }
            if (process) {
                enEvent.setCaseDetails(dataCase);
                enEvent.setScheduleDate(schedule);
                enEvent.setScheduleTime(object.getSchedule_time());
                enEvent.setEventName(object.getEvent_name());
                if (object.getEvent_type().equalsIgnoreCase("trial") || object.getEvent_type().equalsIgnoreCase("Trial")) {
                    enEvent.setEventType("t");
                }
                if (object.getEvent_type().equalsIgnoreCase("meeting") || object.getEvent_type().equalsIgnoreCase("Meeting")) {
                    enEvent.setEventType("m");
                }

                Events newEvents = this.eventService.create(enEvent);
                if (newEvents != null) {
                    rs.setResponse_code("00");
                    rs.setInfo("Succes");
                    rs.setResponse("Succes create new events");
                    CreateLog.createJson(rs, "create-event");
                    return rs;
                }
            }
            return rs;
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "create-event");
            return rs;
        } catch (ParseException ex) {
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "create-event");
            Logger.getLogger(EngagementController.class.getName()).log(Level.SEVERE, null, ex);
            return rs;

        }

    }

    @RequestMapping(value = "/manage-engagement/event/{event_id}", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response updateEvent(@PathVariable("event_id") String event_id, @RequestBody EventsApi object, Authentication authentication) {
        try {

//            Events enEvent = new Events();
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entity = employeeService.findByEmployee(name);
            log.info("entity : " + entity);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "update-event");
                process = false;
                return rs;
            }
            if (!entity.getRoleName().contentEquals("dmp")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entity.getRoleName() + " permission deny ");
                CreateLog.createJson(rs, "update-event");
                process = false;
                return rs;
            }
            System.out.println("isi : " + object.getSchedule_date());
            String dt = dateFormat.format(object.getSchedule_date());
            Date schedule = dateFormat.parse(dt);
            System.out.println("dateFormat.format(object.getSchedule_date()) : " + schedule);
            if (schedule == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Field schedule date can't be NULL");
                CreateLog.createJson(rs, "update-event");
                process = false;
                return rs;
            }
            Events enEvent = this.eventService.findById(event_id);

            if (process) {
//              enEvent.setCaseDetails(dataCase);
                enEvent.setScheduleDate(schedule);
                enEvent.setScheduleTime(object.getSchedule_time());
                enEvent.setEventName(object.getEvent_name());
                if (object.getEvent_type().equalsIgnoreCase("trial") || object.getEvent_type().equalsIgnoreCase("Trial")) {
                    enEvent.setEventType("t");
                }
                if (object.getEvent_type().equalsIgnoreCase("meeting") || object.getEvent_type().equalsIgnoreCase("Meeting")) {
                    enEvent.setEventType("m");
                }

                Events newEvents = this.eventService.update(enEvent);
                if (newEvents != null) {
                    rs.setResponse_code("00");
                    rs.setInfo("Succes");
                    rs.setResponse("Succes Update events");
                    CreateLog.createJson(rs, "update-event");
                    return rs;
                }
            }
            return rs;
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "update-event");
            return rs;
        } catch (ParseException ex) {
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "update-event");
            Logger.getLogger(EngagementController.class.getName()).log(Level.SEVERE, null, ex);
            return rs;

        }

    }

    @RequestMapping(value = "/manage-engagement/{engagement_id}/view-event", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> viewEvent(@PathVariable("engagement_id") Long engagement_id, Authentication authentication) {
        try {

//            Events enEvent = new Events();
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entity = employeeService.findByEmployee(name);
            log.info("entity : " + entity);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "view-Event");
                process = false;
//                return rs;
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.FORBIDDEN);
            }

            CaseDetails dataCase = caseDetailsService.findById(engagement_id);
            if (dataCase == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature Engagement Id Not Found : " + engagement_id);
                CreateLog.createJson(rs, "view-Event");
                process = false;
//                return rs;
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature Engagement Id Not Found : " + engagement_id),
                        HttpStatus.FORBIDDEN);
            }

            List<Events> listEvent = eventService.findByCaseId(dataCase.getCaseID());
            if (listEvent == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("CaseID == NULL");
                CreateLog.createJson(rs, "view-Event");
                process = false;
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature Engagement Id Not Found : " + engagement_id),
                        HttpStatus.FORBIDDEN);

            }

            JSONArray array = new JSONArray();
            if (process) {

                for (int i = 0; i < listEvent.size(); i++) {
                    JSONObject obj = new JSONObject();
                    Events events = listEvent.get(i);

                    if (events.getEventId() == null) {
                        obj.put("event_id", "");
                    } else {
                        obj.put("event_id", events.getEventId());
                    }
                    if (events.getEventName() == null) {
                        obj.put("event_name", "");
                    } else {
                        obj.put("event_name", events.getEventName());
                    }
                    if (events.getEventType() == null) {
                        obj.put("event_type", "");
                    } else {
                        obj.put("event_type", events.getEventType());
                    }
                    if (events.getScheduleDate() == null) {
                        obj.put("schedule_date", "");
                    } else {
                        obj.put("schedule_date", dateFormat.format(events.getScheduleDate()));
                    }
                    if ("0".equals(events.getIsActive())) {
                        obj.put("status", "done");
                    } else {
                        obj.put("status", "active");
                    }
                    if (events.getCaseDetails() == null) {
                        obj.put("case_id", "");
                    } else {
                        obj.put("case_id", events.getCaseDetails().getCaseID());
                    }
                    array.put(obj);
                }
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "view-Event");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/manage-engagement/events", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public Response listEvents(@PathVariable("engagement_id") Long engagement_id, Authentication authentication) {
        Boolean process = true;
        String name = authentication.getName();
        log.info("name : " + name);
        Employee entity = employeeService.findByEmployee(name);
        log.info("entity : " + entity);
        if (entity == null) {
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse("can't acces this feature :");
            CreateLog.createJson(rs, "listEvents");
            process = false;
            return rs;
        }
        return rs;
    }

//    @RequestMapping(value = "/manage-engagement/{engagement_id}/document", method = RequestMethod.POST, produces = {"application/json"})
//    @XxsFilter
//    public Response uploadCaseDocument(@RequestPart("case_doc") MultipartFile file, @PathVariable("engagement_id") Long engagement_id, Authentication authentication) throws IOException {
//        try {
//
//            CaseDocument entCaseDocument = new CaseDocument();
//
//            Date todayDate = new Date();
//            Date now = new Date();
//            String pathDoc = null;
//            Boolean process = true;
//            String name = authentication.getName();
//            log.info("name : " + name);
//            Employee entity = employeeService.findByEmployee(name);
//            log.info("entity : " + entity);
//            if (entity == null) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("can't acces this feature :");
//                process = false;
//                CreateLog.createJson(rs, "upload-case-document");
//                return rs;
//            }
//            if (!entity.getRoleName().contentEquals("dmp")) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("role : " + entity.getRoleName() + " permission deny ");
//                process = false;
//                CreateLog.createJson(rs, "upload-case-document");
//                return rs;
//            }
//
//            CaseDetails caseDetails = caseDetailsService.findById(engagement_id);
//            if (caseDetails == null) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("case Id Not Found ");
//                process = false;
//                CreateLog.createJson(rs, "upload-case-document");
//                return rs;
//            }
//            pathDoc = basepathUpload + "/" + "engagemet" + caseDetails.getCaseID() + "/" + "file" + "/";
//
//            if (process) {
//                if (!file.isEmpty()) {
//
//                    File newFolder = new File(pathDoc);
//                    boolean created = newFolder.mkdir();
//                    if (!newFolder.getParentFile().exists()) {
//                        newFolder.getParentFile().mkdirs();
//                    } else {
//                        newFolder.getParentFile().mkdirs();
//                    }
//                    if (!newFolder.exists()) {
//                        if (created) {
//                            System.out.println("Folder was created !");
//                        } else {
//                            System.out.println("Folder exists");
//                        }
//                    }
//
//                    byte[] bytes = file.getBytes();
//                    Path path = Paths.get(pathDoc + file.getOriginalFilename().replaceAll(" ", ""));
//                    Files.write(path, bytes);
//                    String fileName = file.getOriginalFilename().replaceAll(" ", "");
//                    log.info("file getOriginalFilename : " + file.getOriginalFilename().replaceAll(" ", ""));
//                    entCaseDocument.setLinkDocument(pathDoc + file.getOriginalFilename().replaceAll(" ", ""));
//                    entCaseDocument.setCaseDetails(caseDetails);
//                    CaseDocument cerateCaseDocument = this.caseDocumentService.create(entCaseDocument);
//                    if (cerateCaseDocument != null) {
//                        rs.setResponse_code("00");
//                        rs.setInfo("Succes");
//                        rs.setResponse("Upload: Succes : " + fileName);
//                        CreateLog.createJson(rs, "upload-case-document");
//                        return rs;
//                    } else {
//                        rs.setResponse_code("55");
//                        rs.setInfo("Error");
//                        rs.setResponse("Error Upload Document");
//                        CreateLog.createJson(rs, "upload-case-document");
//                        return rs;
//                    }
//
//                }
//            }
//            return rs;
//        } catch (JSONException ex) {
//            // TODO Auto-generated catch block
////            e.printStackTrace();
//            rs.setResponse_code("55");
//            rs.setInfo("Error");
//            rs.setResponse(ex.getMessage());
//            CreateLog.createJson(ex.getMessage(), "upload-case-document");
//            return rs;
//
//        }
//
//    }

//    @RequestMapping(value = "/manage-engagement/{engagement_id}/documents", method = RequestMethod.GET, produces = {"application/json"})
//    @XxsFilter
//    public Response listDocument(@PathVariable("engagement_id") Long engagement_id, Authentication authentication) throws IOException {
//        Date todayDate = new Date();
//        Date now = new Date();
//        String pathDoc = null;
//        Boolean process = true;
//        String name = authentication.getName();
//        log.info("name : " + name);
//        Employee entity = employeeService.findByEmployee(name);
//        log.info("entity : " + entity);
//        if (entity == null) {
//            rs.setResponse_code("55");
//            rs.setInfo("Failed");
//            rs.setResponse("can't acces this feature :");
//            process = false;
//            CreateLog.createJson(rs, "upload-case-document");
//            return rs;
//        }
//        return null;
//    }

//    @RequestMapping(value = "/manage-engagement/{engagement_id}/done", method = RequestMethod.POST, produces = {"application/json"})
//    @XxsFilter
//    public Response closingCase(@RequestBody final EngagementApi object, @PathVariable("engagement_id") Long engagement_id, Authentication authentication) {
//        try {
//            Date now = new Date();
////          Integer number = null;
//            EngagementHistory enHistory = new EngagementHistory();
//            String name = authentication.getName();
//            log.info("name : " + name);
//            Employee entityEmp = employeeService.findByEmployee(name);
//            log.info("entityEmp : " + entityEmp);
//            log.info("engagement_id : " + engagement_id);
//            if (entityEmp == null) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("can't closing case :");
//                CreateLog.createJson(rs, "closing-Case");
//                return rs;
//            }
//            if (!entityEmp.getRoleName().contentEquals("dmp")) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("role : " + entityEmp.getRoleName() + " permission deny ");
//                CreateLog.createJson(rs, "closing-Case");
//                return rs;
//            }
//            CaseDetails entity = caseDetailsService.findById(engagement_id);
//            if (entity == null) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("can't closing case engagement_id " + engagement_id + "Not Found");
//                CreateLog.createJson(rs, "closing-Case");
//                return rs;
//            }
//            if (entity.getStatus().contains("4")) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("case Case Id : " + entity.getCaseID() + " Status Closed ");
//                CreateLog.createJson(rs, "closing-Case");
//                return rs;
//            }
//
//            entity.setIsActive("4");
//            entity.setStatus("closed");
//            enHistory.setEngagement(entity);
//            enHistory.setUserId(entityEmp.getIdEmployee());
//            enHistory.setResponse("closed By : " + entityEmp.getEmployeeId());
//            CaseDetails closeCase = this.caseDetailsService.update(entity);
//            if (closeCase != null) {
//                this.engagementHistoryService.create(enHistory);
//                rs.setResponse_code("00");
//                rs.setInfo("Success");
//                rs.setResponse("closing case engagement_id " + engagement_id + "by : " + entityEmp.getEmployeeId());
//                CreateLog.createJson(rs, "closing-Case");
//                return rs;
//            }
//
//        } catch (JSONException ex) {
//            // TODO Auto-generated catch block
////            e.printStackTrace();
//            rs.setResponse_code("55");
//            rs.setInfo("Error");
//            rs.setResponse(ex.getMessage());
//            CreateLog.createJson(ex.getMessage(), "upload-case-document");
//            return rs;
//
//        }
//        return null;
//    }
}
