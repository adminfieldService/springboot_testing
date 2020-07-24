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
import com.lawfirm.apps.service.interfaces.MemberServiceIface;
import com.lawfirm.apps.utils.CreateLog;
import com.lawfirm.apps.utils.CustomErrorType;
import com.lawfirm.apps.utils.Util;
import com.xss.filter.annotation.XxsFilter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.security.PermitAll;
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

    public EngagementController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yy");
        this.sdfMonth = new SimpleDateFormat("MM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
    }

    @PermitAll
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
                rs.setResponse("can't approveByAdmin :");
                CreateLog.createJson(rs, "manage-engagement");
                process = false;
                return rs;
            }

//            Engagement dataEngagement = new Engagement();
            if (object.getClient_name() == null) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, Client name filed can't be empty");
                CreateLog.createJson(rs, "createEngagement");
                process = false;
                return rs;
            }
            if (object.getClient_name().length() > 80) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, Client name filed max 80");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getAddress() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, ADDRESS filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getAddress().length() > 200) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, ADDRESS  filed max 200");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getNpwp() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, NPWP filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getNpwp().length() > 30) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, ADDRESS  filed max 20");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }

            if (object.getPic() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, PIC filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getPic().length() > 20) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, PIC  filed max 20");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getProfesional_fee() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, PERSONAL FEE filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (object.getCase_over_view() == null) {

                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, CASE OVERVIEW filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }

            Employee cekEMP = employeeService.findById(entityEmp.getIdEmployee());
            if (!"dmp".equals(cekEMP.getRoleName())) {
                rs.setResponse_code("05");
                rs.setInfo("failed");
                rs.setResponse("Create Engagement Failed, CASE OVERVIEW filed can't be empty");
                process = false;
                CreateLog.createJson(rs, "createEngagement");
                return rs;
            }
            if (process) {

                log.info("process");

                ClientData dataClient = clientDataService.findBydataClient(object.getClient_name(), object.getAddress(), object.getNpwp());
                Integer numberClient = 0;
                String client_id = "CLIENT";
                if (dataClient != null) {
                    log.info("dataClient : " + dataClient);
                    numberClient = clientDataService.generateCleintId(object.getNpwp());
                    if (numberClient == 0) {
                        numberClient = 1;
                    }
                    ClientData check = clientDataService.checkCI(client_id + Util.setNumber(numberClient.toString()));
                    if (check == null) {
                        dataClient.setClientId(client_id + Util.setNumber(numberClient.toString()));
                    } else {
                        numberClient = 1;
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
                    dataCaseDetails.setTgl_input(sdfYear.format(now));
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
                        addTeamMember(ObjectEngagement);
                    }
                } else {

                    log.info("dataClient nulls");
                    ClientData newClient = new ClientData();
                    newClient.setClientName(object.getClient_name());
                    newClient.setAddress(object.getAddress());
                    newClient.setNpwp(object.getNpwp());
                    newClient.setPic(object.getPic());
                    numberClient = clientDataService.generateCleintId(object.getNpwp());
                    if (numberClient == 0) {
                        numberClient = 1;
                    }
                    ClientData check = clientDataService.checkCI(client_id + Util.setNumber(numberClient.toString()));
                    if (check == null) {
                        newClient.setClientId(client_id + Util.setNumber(numberClient.toString()));
                    } else {
                        numberClient = 1;
                        newClient.setClientId(client_id + Util.setNumber(numberClient.toString()));
                    }

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
                        addTeamMember(ObjectEngagement);
                    } else {
                        rs.setResponse_code("05");
                        rs.setInfo("failed");
                        rs.setResponse("Create Engagement Failed");
                        CreateLog.createJson(rs, "createEngagement");
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
                CreateLog.createJson(rs, "createEngagement");
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

//    @PutMapping(value = "/add-member/{engagement_id}", produces = {"application/json"})
//    public Response addTeamMember(@RequestBody final EngagementApi object, @PathVariable("engagement_id") Long engagement_id) {
//    @PutMapping(value = "/add-member/", produces = {"application/json"})
    @RequestMapping(value = "/add-member", method = RequestMethod.POST, produces = {"application/json"})
    public Response addTeamMember(@RequestBody final EngagementApi object) {
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
            rs.setResponse_code("05");
            rs.setInfo("failed");
            rs.setResponse("Failed Ad Team Member");
            CreateLog.createJson(rs, "add-team-member");
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
            rs.setResponse_code("05");
            rs.setInfo("failed");
            rs.setResponse("Employee Not Found");
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
            rs.setResponse_code("05");
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
            rs.setResponse_code("05");
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
            if (!object.getDescription().isEmpty()) {
                dataTeam.setDescription(object.getDescription());
            }
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
                member = memberServiceIface.create(dataM);
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

    }

    @RequestMapping(value = "/approval/{engagement_id}/by-admin", method = RequestMethod.PATCH, produces = {"application/json"})
    @XxsFilter
//    @PutMapping(value = "/approved/by-admin/{loan_id}", produces = {"application/json"})
//    @XxsFilter
    public Response approveByAdmin(@RequestBody final EngagementApi object, @PathVariable("engagement_id") Long engagement_id, Authentication authentication) {
        try {
            Date now = new Date();
//            Integer number = null;
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
                rs.setResponse_code("00");
                rs.setInfo("Sucess");
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
            Integer number = caseDetailsService.generateCaseId(entity.getTgl_input());
            if (number == null) {
                number = 1;
            } else {
                number = number++;
            }
            String caseId = null;
            String check_caseId = "CASEID" + Util.setNumber(number.toString());
            CaseDetails findByCaseId = caseDetailsService.findByCaseId(check_caseId, entity.getTgl_input());
            if (findByCaseId == null) {
                number = 1;
                caseId = "CASEID" + Util.setNumber(number.toString());
            } else {
                number = number++;
                caseId = "CASEID" + Util.setNumber(number.toString());
            }
            if (object.getDecision().contains("a")) {
                entity.setCaseID(caseId);
                entity.setStatus(object.getDecision());
                entity.setIsActive("1");
                entity.setApproved_date(now);
                entity.setTgl_input(sdfYear.format(now));
                entity.setApprovedBy(entityEmp.getIdEmployee().toString());
            }

            if (object.getDecision().contains("r")) {
                entity.setStatus(object.getDecision());
                entity.setIsActive("2");
                entity.setApproved_date(now);
                entity.setTgl_input(sdfYear.format(now));

                entity.setApprovedBy(entityEmp.getIdEmployee().toString());
            }

            CaseDetails updateEng = caseDetailsService.update(entity);
            if (updateEng != null) {
                rs.setResponse_code("00");
                rs.setInfo("Sucess");
                rs.setResponse("approval BY : " + entityEmp.getEmployeeId());
                CreateLog.createJson(rs, "approveByAdmin");
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

    @RequestMapping(value = "/manage-engagement/view/by-employee/{id_employee}", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> byEmployee(@PathVariable("id_employee") Long id_employee) {
        try {

            Employee entity = employeeService.findById(id_employee);
            if (entity != null) {
                List<CaseDetails> listData = caseDetailsService.findByEmployee(entity.getIdEmployee());
                JSONObject obj = new JSONObject();
                JSONArray array = new JSONArray();
                if (listData != null) {
                    for (int i = 0; i < listData.size(); i++) {
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
                            obj.put("address", "");
                        } else {
                            obj.put("address", data.getClient().getAddress());
                        }
                        if (data.getClient() == null) {
                            obj.put("client_name", "");
                        } else {
                            obj.put("client_name", data.getClient().getClientName());
                        }
                        array.put(obj);
                    }
                }
                return ResponseEntity.ok(array.toString());
            }
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "view-by-employee");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
        CreateLog.createJson(HttpStatus.NOT_FOUND, "view-by-employee");
        return new ResponseEntity(new CustomErrorType("05", "Error", "NOT FOUND"),
                HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/manage-engagement/list-of-engagement", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> litEngagement() {
        try {

//            Employee entity = employeeService.findById(id_employee);
//            if (entity != null) {
            List<CaseDetails> listData = caseDetailsService.listCaseDetails();
            JSONObject obj = new JSONObject();
            JSONArray array = new JSONArray();
            if (listData != null) {
                for (int i = 0; i < listData.size(); i++) {
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
                        Employee entityEmp = employeeService.findByEmployee(data.getApprovedBy());
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
                        obj.put("address", "");
                    } else {
                        obj.put("address", data.getClient().getAddress());
                    }
                    if (data.getClient() == null) {
                        obj.put("client_name", "");
                    } else {
                        obj.put("client_name", data.getClient().getClientName());
                    }
                    array.put(obj);
                }
            }
            return ResponseEntity.ok(array.toString());
//            }
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "view-by-employee");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/manage-engagement/aproval-list/by-admin/{id_employee_admin}", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> byAdmin(@PathVariable("id_employee_admin") Long id_employee_admin) {
        try {
//            Engagement entity = engagementService.findById(id_employee_admin);

            List<CaseDetails> listData = caseDetailsService.findByEmployee(id_employee_admin);
            JSONObject obj = new JSONObject();
            JSONArray array = new JSONArray();
            if (listData != null) {
                for (int i = 0; i < listData.size(); i++) {
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
                        Employee entityEmp = employeeService.findByEmployee(data.getApprovedBy());
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
                        obj.put("address", "");
                    } else {
                        obj.put("address", data.getClient().getAddress());
                    }
                    if (data.getClient() == null) {
                        obj.put("client_name", "");
                    } else {
                        obj.put("client_name", data.getClient().getClientName());
                    }
                    array.put(obj);
                }
            }
            return ResponseEntity.ok(array.toString());

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "view-by-employee");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }
}
