/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.Employee;
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
import com.lawfirm.apps.service.interfaces.EngagementHistoryServiceIface;
import com.lawfirm.apps.service.interfaces.EngagementServiceIface;
import com.lawfirm.apps.service.interfaces.EventServiceIface;
import com.lawfirm.apps.service.interfaces.FinancialServiceIface;
import com.lawfirm.apps.service.interfaces.LoanServiceIface;
import com.lawfirm.apps.service.interfaces.LoanTypeServiceIface;
import com.lawfirm.apps.service.interfaces.MemberServiceIface;
import com.lawfirm.apps.service.interfaces.ProfessionalServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import com.lawfirm.apps.utils.CreateLog;
import com.lawfirm.apps.utils.CustomErrorType;
import com.xss.filter.annotation.XxsFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
public class TeamController {

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

    public TeamController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yy");
        this.sdfMonth = new SimpleDateFormat("MM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
    }

    @RequestMapping(value = "/team-member/{engagement_id}", method = RequestMethod.GET, produces = {"application/json"})///{id_employee}
    @XxsFilter
//    public ResponseEntity<String> byEmployee(@PathVariable("id_employee") Long id_employee) {
    public ResponseEntity<?> byEmployee(@PathVariable("engagement_id") Long engagement_id, Authentication authentication) {
        try {
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entity = employeeService.findByEmployee(name);
            log.info("entity : " + entity);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "team-member-by-employee");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
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
                    List<TeamMember> entityTeam = teamMemberService.listTeamMemberByEngagement(data.getEngagementId());
                    for (int j = 0; j < entityTeam.size(); j++) {
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
                                objMember.put("fee_share", "");//portion
                            } else {
                                objMember.put("member_name", dataMember.getEmployee().getName());
                                objMember.put("employee_id", dataMember.getEmployee().getEmployeeId());
                                objMember.put("fee_share", dataMember.getFeeShare());//portion

                            }
                            arrayM.put(objMember);
//                                    
                        }
                    } else {
                        objMember.put("member_name", "");
                        objMember.put("employee_id", "");
                        objMember.put("fee_share", "");
                    }
                    obj.put("members", arrayM);
                    array.put(obj);
                }
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "team-member-by-employee");
            return new ResponseEntity(new CustomErrorType("05", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

}
