/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.Disbursement;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.EntityPTKP;
import com.lawfirm.apps.model.EntityPeriod;
import com.lawfirm.apps.model.Member;
import com.lawfirm.apps.model.OutStandingLoanA;
import com.lawfirm.apps.model.OutStandingLoanB;
import com.lawfirm.apps.model.TeamMember;
import com.lawfirm.apps.response.Response;
import com.lawfirm.apps.service.CaseDocumentService;
import com.lawfirm.apps.service.interfaces.AccountServiceIface;
import com.lawfirm.apps.service.interfaces.CaseDetailsServiceIface;
import com.lawfirm.apps.service.interfaces.ClientDataServiceIface;
import com.lawfirm.apps.service.interfaces.DisbursementServiceIface;
import com.lawfirm.apps.service.interfaces.DocumentReimburseServiceIface;
import com.lawfirm.apps.service.interfaces.EmployeeRoleServiceIface;
import com.lawfirm.apps.service.interfaces.EmployeeServiceIface;
import com.lawfirm.apps.service.interfaces.EngagementServiceIface;
import com.lawfirm.apps.service.interfaces.EntityPeriodServiceIface;
import com.lawfirm.apps.service.interfaces.EventServiceIface;
import com.lawfirm.apps.service.interfaces.LoanHistoryServiceIface;
import com.lawfirm.apps.service.interfaces.LoanServiceIface;
import com.lawfirm.apps.service.interfaces.LoanTypeServiceIface;
import com.lawfirm.apps.service.interfaces.MemberServiceIface;
import com.lawfirm.apps.service.interfaces.OutStandingLoanAServiceIface;
import com.lawfirm.apps.service.interfaces.OutStandingLoanBServiceIface;
import com.lawfirm.apps.service.interfaces.ProfessionalServiceIface;
import com.lawfirm.apps.service.interfaces.PtkpServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import com.lawfirm.apps.support.api.DisbursementCaseIdDto;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class DisbursementDtoController {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
    static String basepathUpload = "/opt/lawfirm/UploadFile/";

    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;
    SimpleDateFormat sdfYear;
    SimpleDateFormat sdfMonth;
    SimpleDateFormat sdfMY;
    SimpleDateFormat sdfDisbursM;
    SimpleDateFormat sdfDisbursMY;
    SimpleDateFormat sdfDisburse;

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
    @Autowired
    DisbursementServiceIface disbursementService;
    @Autowired
    LoanHistoryServiceIface loanHistoryService;
    @Autowired
    OutStandingLoanBServiceIface outStandingLoanBService;
    @Autowired
    OutStandingLoanAServiceIface outStandingLoanAService;
    @Autowired
    MemberServiceIface memberService;
    @Autowired
    PtkpServiceIface ptkpService;
    @Autowired
    EntityPeriodServiceIface entityPeriodService;

    public DisbursementDtoController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yyyy");
        this.sdfMonth = new SimpleDateFormat("MM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
        this.sdfDisbursM = new SimpleDateFormat("MMM");
        this.sdfDisbursMY = new SimpleDateFormat("MMMyyyy");
        this.sdfDisburse = new SimpleDateFormat("dd-MMMM-yyyy");
    }

    @RequestMapping(value = "/disbursement-dto/case-id", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<String> listDisbursement(@RequestBody final DisbursementCaseIdDto object, Authentication authentication) {
        try {
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                log.error("listDisbursement" + rs.toString());
                CreateLog.createJson(rs, "listDisbursement");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!entityEmp.getRoleName().contentEquals("finance")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                log.error("listDisbursement" + rs.toString());
                CreateLog.createJson(rs, "listDisbursement");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            String caseId = object.getCase_id();
            CaseDetails dataCase = caseDetailsService.findCaseId(caseId);
            if (dataCase == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature case Id : " + object.getCase_id() + " Not Found");
                log.error("listDisbursement" + rs.toString());
                CreateLog.createJson(rs, "listDisbursement");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }

            Long engId = dataCase.getEngagementId();
            List<Member> memberList = memberService.listMemberDisburse(engId);
            JSONArray array = new JSONArray();
            JSONObject objDmp = new JSONObject();
            TeamMember entityTeam = teamMemberService.teamMemberByEngagement(engId);
            if (entityTeam == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature case Id : " + object.getCase_id() + " Not Found");
                log.error("listDisbursement" + rs.toString());
                CreateLog.createJson(rs, "listDisbursement");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            Long dmpId = 0l;
            dmpId = entityTeam.getDmpId();
            Employee getDmp = employeeService.findById(dmpId);
            if (entityTeam.getEngagement() == null) {
                objDmp.put("dmp_portion", "");
            } else {
                objDmp.put("dmp_portion", String.format("%.0f", (entityTeam.getEngagement().getDmpPortion())));
            }
//            objDmp.put("employee_id_dmp", getDmp.getIdEmployee());
//            objDmp.put("dmp_name", getDmp.getName());
//            objDmp.put("fee_share_dmp", entityTeam.getFeeShare());
//            objDmp.put("is_disburse_dmp", entityTeam.getStatus());
            JSONObject obj = null;
            for (int m = 0; m < memberList.size(); m++) {
                obj = new JSONObject();
                Member dataMember = memberList.get(m);

                if (dataMember.getTeamMember() == null) {

                } else {

                    if (dataMember.getTeamMember().getEngagement() == null) {
                        obj.put("case_id", "");
                        obj.put("engagement_id", "");
                        obj.put("employee_id", "");
                        obj.put("member_name", "");
                        obj.put("fee_share", "");
                        obj.put("is_disburse", "0");
                    } else {
                        obj.put("case_id", dataMember.getTeamMember().getEngagement().getCaseID());
                        obj.put("engagement_id", dataMember.getTeamMember().getEngagement().getEngagementId());
                        obj.put("employee_id", dataMember.getEmployee().getIdEmployee());
                        obj.put("member_name", dataMember.getEmployee().getName());
                        obj.put("fee_share", dataMember.getFeeShare());
                        if (dataMember.getStatus() == null) {
                            obj.put("is_disburse", "0");
                        } else {
                            obj.put("is_disburse", dataMember.getStatus());
                        }

                    }
                }
                array.put(obj);
            }
            objDmp.put("members", array);
            return ResponseEntity.ok(objDmp.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "listDisbursement");
            log.error("listDisbursement" + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/disbursement-dto/employee", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> viewburseByEmployee(@RequestBody final DisbursementCaseIdDto object, Authentication authentication) {
        try {
//            Date now = new Date();
//            Date dateDisburse = new Date();
            Boolean process = true;
            String name = authentication.getName();
            log.info("viewburseByEmployee json : " + object);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                log.error("viewburseByEmployee" + rs.toString());
                CreateLog.createJson(rs, "viewburseByEmployee");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            if (!entityEmp.getRoleName().contentEquals("finance")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                log.error("listDisbursement" + rs.toString());
                CreateLog.createJson(rs, "viewburseByEmployee");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            String caseId = object.getCase_id();
            CaseDetails dataCase = caseDetailsService.findCaseId(caseId);
            if (dataCase == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature case Id : " + object.getCase_id() + " Not Found");
                log.error("viewburseByEmployee" + rs.toString());
                CreateLog.createJson(rs, "viewburseByEmployee");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature case Id : " + object.getCase_id() + " Not Found"),
                        HttpStatus.NOT_FOUND);
            }
            Long engId = dataCase.getEngagementId();
            Member dataMember = memberService.memberDisburse(engId, object.getId_employee());
            TeamMember entityTeam = teamMemberService.teamMemberByEngagement(engId);
            Disbursement dataDisbursement = disbursementService.disbursementFindbyCaseId(caseId);

//            JSONArray array = new JSONArray();
            JSONObject objMember = new JSONObject();
            Integer number_of_disbursement = 0;
            Double dmpPortion = 0d;
            Double outStandingLoanB = 0d;
            String tax_year = null;
            String closedate = null;
            Date closeDate = null;
            String oldclosedate = null;
            Date oldCloseDate = null;
            String disbursement_id = null;
            if (process) {
                dmpPortion = dataCase.getDmpPortion();
                if (dataDisbursement != null) {
                    closedate = dateFormat.format(dataDisbursement.getCutOffDate());
                    closeDate = dateFormat.parse(closedate);
                    oldclosedate = dateFormat.format(dataDisbursement.getOldCutOffDate());
                    oldCloseDate = dateFormat.parse(oldclosedate);
                    disbursement_id = dataDisbursement.getDisbursementId();
                    number_of_disbursement = dataDisbursement.getNumberOfDisbursement();
                    tax_year = dataDisbursement.getTahunInput();
                }
                Double outStandingB = 0.0;
                Double totalReimburse = 0.0;
                Double loanAmount = 0.0;
                log.info("caseId" + caseId);
                List<OutStandingLoanB> listOutStanding = this.outStandingLoanBService.lsitByCaseId(caseId);
                if (listOutStanding.size() > 0) {
                    loanAmount = listOutStanding.get(0).getLoanAmount();
                }
                totalReimburse = this.outStandingLoanBService.sumLoanByCaseId(caseId);
                outStandingB = loanAmount - totalReimburse;
                outStandingLoanB = outStandingB;
                log.info("dataMember" + dataMember);
                if (dataMember == null) {
                    objMember.put("employee_id_team", "");
                    objMember.put("fee_share_team", "");//"fee_share_team
                    objMember.put("amount_portion_team", "");
                    objMember.put("previous_disbursement_team", "");
                    objMember.put("anual_salary_team", "");
                    objMember.put("total_income_fortax_purpose_team", "");
                    objMember.put("masa_kerja_team", "");
                    objMember.put("jabatan_per_bulan_team", "");
                    objMember.put("jabatan_per_tahun_team", "");
                    objMember.put("tax_status_team", "");
                    objMember.put("ptkp_team", "");
                    objMember.put("taxable_income_team", "");
                    objMember.put("income_tax_team", "");
                    objMember.put("income_tax_paid_on_prior_period_team", "");
                    objMember.put("net_income_tax_deducted_team", "");
                    objMember.put("net_income_team", "");
                    objMember.put("outstanding_loan_b_team", "");
                    objMember.put("disbursable_amount_team", "");
                    objMember.put("outstanding_loan_a_team", "");
                    objMember.put("disbursed_amount_team", "");

                } else {
                    Integer anual_salary_team = (4000000 * 13);
                    int a_jabatan_team = (60000000 * 5) / 100;
                    int b_jabatan_team = ((anual_salary_team * 5) / 100) / 13;
                    int masa_kerja_team = 0;
                    Double amount_portion_team = 0d;
                    Double previous_disbursement_team = 0d;

                    Double total_income_fortax_purpose_team = 0d;
                    String team_tax_status = dataMember.getEmployee().getTaxStatus();
                    Double taxable_income_team = 0d;
                    Double income_tax_team = 0d;
                    Double income_tax_paid_on_prior_period_team = 0d;
                    Double net_income_tax_deducted_team = 0d;
                    Double net_income_team = 0d;
                    Double outstanding_loan_b_team = 0d;
                    Double disbursable_amount_team = 0d;
                    Double outstanding_loan_a_team = 0d;
                    Double disbursed_amount_team = 0d;
                    Double ptkp = 0d;
//                                        log.info("team_tax_status : " + team_tax_status);
//                                      String jabatan_perbulan_team = String.format("%d", (((4000000 * 13) * 5) / 100) / 13);
                    EntityPTKP ptkpTeam = this.ptkpService.findPTKPByTaxStatus(team_tax_status);
                    log.info("ptkpTeam : " + ptkpTeam);
                    if (ptkpTeam != null) {
                        ptkp = ptkpTeam.getPtkp();
                    }
                    objMember.put("employee_id_team", dataMember.getEmployee().getEmployeeId());
                    objMember.put("fee_share_team", dataMember.getFeeShare());//"fee_share_team
                    amount_portion_team = ((dmpPortion * dataMember.getFeeShare()) / 100);
                    objMember.put("amount_portion_team", String.format("%.0f", (amount_portion_team)));
//                                
                    if (number_of_disbursement == 1) {
                        previous_disbursement_team = 0d;
                        income_tax_paid_on_prior_period_team = 0d;
                    }
                    if (number_of_disbursement == 2) {
                        previous_disbursement_team = entityPeriodService.getPreviousDisbursement(2, dataMember.getEmployee().getIdEmployee(), tax_year);
                        if (previous_disbursement_team == null || previous_disbursement_team == 0d) {
                            previous_disbursement_team = 0d;
                        }
                        income_tax_paid_on_prior_period_team = income_tax_paid_on_prior_period_team + 0d;
                    }
                    if (number_of_disbursement == 3) {
                        EntityPeriod previousDisbursement = entityPeriodService.getPrevDisbursement(3, dataMember.getEmployee().getIdEmployee(), tax_year);
//                                    if (previous_disbursement_team == null || previous_disbursement_team == 0d) {
//                                        previous_disbursement_team = 0d;
//                                    } else if (previous_disbursement_team.equals(amount_portion_team)) {
//                                        previous_disbursement_team = 0d;
//                                    }
                        previous_disbursement_team = previousDisbursement.getPrevDisbursement();
                        log.info("previous_disbursement_team 3" + previous_disbursement_team);
                        income_tax_paid_on_prior_period_team = income_tax_paid_on_prior_period_team++;
                    }
//                                
                    objMember.put("previous_disbursement_team", String.format("%.0f", previous_disbursement_team));

                    objMember.put("anual_salary_team", String.format("%d", anual_salary_team));

                    total_income_fortax_purpose_team = amount_portion_team + previous_disbursement_team + anual_salary_team;
                    objMember.put("total_income_fortax_purpose_team", String.format("%.0f", (total_income_fortax_purpose_team)));
                    masa_kerja_team = Util.hitungMasakerja(dataMember.getEmployee().getDateRegister());
                    objMember.put("masa_kerja_team", masa_kerja_team);
                    String jabatan_per_bulan_team = String.format("%d", Math.min(a_jabatan_team, b_jabatan_team));
                    Integer jabatan_per_tahun_team = (masa_kerja_team * Integer.parseInt(jabatan_per_bulan_team));
                    objMember.put("jabatan_per_bulan_team", jabatan_per_bulan_team);
                    objMember.put("jabatan_per_tahun_team", String.format("%d", jabatan_per_tahun_team));
                    objMember.put("tax_status_team", team_tax_status);
                    objMember.put("ptkp_team", String.format("%.0f", ptkp));
                    Double taxable_income_team_val = null;
                    if (number_of_disbursement == 1) {
                        taxable_income_team = (0 - jabatan_per_tahun_team - ptkp);
                        income_tax_team = Util.hitungPajak(taxable_income_team);
                    }
                    if (number_of_disbursement == 2) {
                        taxable_income_team = (total_income_fortax_purpose_team - jabatan_per_tahun_team - ptkp);
                        income_tax_team = Util.hitungPajak(taxable_income_team);
                    }
                    if (number_of_disbursement == 3) {
                        taxable_income_team = (total_income_fortax_purpose_team - jabatan_per_tahun_team - ptkp);
                        income_tax_team = Util.hitungPajak(taxable_income_team);
                    }

                    objMember.put("taxable_income_team", String.format("%.0f", Math.abs(taxable_income_team)));
                    objMember.put("income_tax_team", String.format("%.0f", Math.abs(income_tax_team)));
                    log.info("income_tax_team 2 : " + income_tax_team);
                    log.info("taxable_income_team 2 : " + taxable_income_team);

                    if (number_of_disbursement == 1) {

                        income_tax_paid_on_prior_period_team = 0d;
                    }
                    if (number_of_disbursement == 2) {
//                                    net_income_team = amount_portion_team - net_income_tax_deducted_team;
                        log.info("1, dataMember.getEmployee().getIdEmployee() : tax_year" + dataMember.getEmployee().getIdEmployee() + ":" + caseId + ":" + tax_year);
//                                    EntityPeriod entityPeriod = entityPeriodService.findBy(dataMember.getEmployee().getIdEmployee(), caseId, tax_year);
//                                    if (entityPeriod != null) {
//                                        Double income_tax = Util.hitungPajak(income_tax_team);
//                                        entityPeriod.setIncomeTaxPaidOnPriorPeriod(income_tax);
//                                        this.entityPeriodService.update(entityPeriod);
//                                    }
                        log.info("1, dataMember.getEmployee().getIdEmployee() : tax_year" + dataMember.getEmployee().getIdEmployee() + ":" + tax_year);
                        Double incomeTaxPaidOnPriorPeriod = entityPeriodService.incomeTaxPaidOnPriorPeriod(1, dataMember.getEmployee().getIdEmployee(), tax_year);
                        if (incomeTaxPaidOnPriorPeriod != null || incomeTaxPaidOnPriorPeriod == 0d) {
                            income_tax_paid_on_prior_period_team = incomeTaxPaidOnPriorPeriod;
                        } else {
                            income_tax_paid_on_prior_period_team = 0d;
                        }

                    }
                    if (number_of_disbursement == 3) {
//                                    net_income_team = amount_portion_team - net_income_tax_deducted_team;
                        log.info("3, dataMember.getEmployee().getIdEmployee() : tax_year" + dataMember.getEmployee().getIdEmployee() + ":" + tax_year);
                        Double incomeTaxPaidOnPriorPeriod = entityPeriodService.incomeTaxPaidOnPriorPeriod(3, dataMember.getEmployee().getIdEmployee(), tax_year);
                        if (incomeTaxPaidOnPriorPeriod != null || incomeTaxPaidOnPriorPeriod == 0d) {
                            income_tax_paid_on_prior_period_team = incomeTaxPaidOnPriorPeriod;
                        } else {
                            income_tax_paid_on_prior_period_team = 0d;
                        }
                    }

                    log.info("income_tax_paid_on_prior_period_team : " + income_tax_paid_on_prior_period_team);

                    objMember.put("income_tax_paid_on_prior_period_team", String.format("%.0f", Math.abs(income_tax_paid_on_prior_period_team)));
//                                if (Math.max(income_tax_paid_on_prior_period_team, 0) == 0) {
//                                    net_income_tax_deducted_team = income_tax_team;
//                                } else {
//                                   net_income_tax_deducted_team = income_tax_team - (income_tax_paid_on_prior_period_team);
//                                }
                    net_income_tax_deducted_team = income_tax_team - (income_tax_paid_on_prior_period_team);
                    if (number_of_disbursement == 1) {

                        income_tax_paid_on_prior_period_team = 0d;
                        EntityPeriod entityPeriod = entityPeriodService.findBy(dataMember.getEmployee().getIdEmployee(), caseId, tax_year);// change to findBY number_disbursement, idEmployee and year ?
                        if (entityPeriod != null) {
//                                        Double income_tax = Util.hitungPajak(taxable_income_team);
                            if (entityPeriod.getIncomeTaxPaidOnPriorPeriod() == null) {
                                entityPeriod.setIncomeTaxPaidOnPriorPeriod(Math.abs(income_tax_team));
                                this.entityPeriodService.update(entityPeriod);
                            }

                        }
                    }
                    if (number_of_disbursement == 2) {
                        log.info("1, dataMember.getEmployee().getIdEmployee() : tax_year" + dataMember.getEmployee().getIdEmployee() + ":" + caseId + ":" + tax_year);
                        EntityPeriod entityPeriod = entityPeriodService.findBy(dataMember.getEmployee().getIdEmployee(), caseId, tax_year);
                        if (entityPeriod != null) {
                            Double income_tax = Util.hitungPajak(income_tax_team);
                            entityPeriod.setIncomeTaxPaidOnPriorPeriod(income_tax_team);
                            this.entityPeriodService.update(entityPeriod);
                        }

                    }
                    if (number_of_disbursement == 3) {
                        log.info("3, dataMember.getEmployee().getIdEmployee() : tax_year" + dataMember.getEmployee().getIdEmployee() + ":" + tax_year);
                        Double incomeTaxPaidOnPriorPeriod = entityPeriodService.incomeTaxPaidOnPriorPeriod(3, dataMember.getEmployee().getIdEmployee(), tax_year);

                    }
                    log.info("net_income_tax_deducted_team : " + String.format("%.0f", net_income_tax_deducted_team));
                    objMember.put("net_income_tax_deducted_team", String.format("%.0f", Math.abs(net_income_tax_deducted_team)));
                    net_income_team = amount_portion_team - net_income_tax_deducted_team;
                    objMember.put("net_income_team", String.format("%.0f", net_income_team));
                    log.info("out_standing_loan_ b" + String.format("%.0f", outStandingLoanB));
                    log.info(" dataMember.getFeeShare() : " + dataMember.getFeeShare());
                    outstanding_loan_b_team = ((outStandingLoanB * dataMember.getFeeShare()) / 100);
                    objMember.put("outstanding_loan_b_team", String.format("%.0f", outstanding_loan_b_team));
                    disbursable_amount_team = (net_income_team - outstanding_loan_b_team);
                    objMember.put("disbursable_amount_team", String.format("%.0f", Math.abs(disbursable_amount_team)));

                    if (number_of_disbursement == 1) {

                        OutStandingLoanA oustStandingLoanAfter = this.outStandingLoanAService.sumLoanA(dataMember.getEmployee().getIdEmployee(), tax_year, 1);
                        Double oustStandingLoanAfterVal = 0d;
                        if (oustStandingLoanAfter == null) {
                            oustStandingLoanAfterVal = 0d;
                        } else {
                            oustStandingLoanAfterVal = oustStandingLoanAfter.getOutstandingADisbursement();
                        }
                        Double outstanding_after_disbursement_amount = 0d;
                        Double outStandingAteam = this.loanService.sumLoanA(dataMember.getEmployee().getIdEmployee(), tax_year, closeDate);
//                                    outstanding_loan_a_team = outStandingAteam;
                        outstanding_loan_a_team = outStandingAteam + oustStandingLoanAfterVal;

                        OutStandingLoanA updateOutStandingLoanA = this.outStandingLoanAService.findBy(dataMember.getEmployee().getIdEmployee(), tax_year, number_of_disbursement.longValue());
                        updateOutStandingLoanA.setDisburseableAmount(disbursable_amount_team);
                        outstanding_after_disbursement_amount = Math.max((updateOutStandingLoanA.getLoanAmount() - disbursable_amount_team), 0);
                        updateOutStandingLoanA.setOutstandingADisbursement(outstanding_after_disbursement_amount);
                        this.outStandingLoanAService.update(updateOutStandingLoanA);
                    }
                    if (number_of_disbursement == 2) {

                        OutStandingLoanA oustStandingLoanAfter = this.outStandingLoanAService.sumLoanA(dataMember.getEmployee().getIdEmployee(), tax_year, 1);
                        Double oustStandingLoanAfterVal = 0d;
                        if (oustStandingLoanAfter == null) {
                            oustStandingLoanAfterVal = 0d;
                        } else {
                            oustStandingLoanAfterVal = oustStandingLoanAfter.getOutstandingADisbursement();
                        }
                        Double outstanding_after_disbursement_amount = 0d;
                        Double outStandingAteam = null;
                        if (oldCloseDate.compareTo(closeDate) == 0) {
                            outStandingAteam = this.loanService.sumLoanA(dataMember.getEmployee().getIdEmployee(), tax_year, closeDate);
                        } else {
                            outStandingAteam = this.loanService.sumLoanA2(dataMember.getEmployee().getIdEmployee(), tax_year, closeDate, oldCloseDate);
                        }
//                                    outstanding_loan_a_team = outStandingAteam;
                        outstanding_loan_a_team = outStandingAteam + oustStandingLoanAfterVal;

                        OutStandingLoanA updateOutStandingLoanA = this.outStandingLoanAService.findBy(dataMember.getEmployee().getIdEmployee(), tax_year, number_of_disbursement.longValue());
                        if (updateOutStandingLoanA != null) {
                            updateOutStandingLoanA.setDisburseableAmount(disbursable_amount_team);
                            outstanding_after_disbursement_amount = Math.max((updateOutStandingLoanA.getLoanAmount() - disbursable_amount_team), 0);
                            updateOutStandingLoanA.setOutstandingADisbursement(outstanding_after_disbursement_amount);
                            this.outStandingLoanAService.update(updateOutStandingLoanA);
                        }
                    }
                    if (number_of_disbursement == 3) {

                        OutStandingLoanA oustStandingLoanAfter = this.outStandingLoanAService.sumLoanA(dataMember.getEmployee().getIdEmployee(), tax_year, 2);

                        Double oustStandingLoanAfterVal = 0d;
                        if (oustStandingLoanAfter == null) {
                            oustStandingLoanAfterVal = 0d;
                        } else {
                            oustStandingLoanAfterVal = oustStandingLoanAfter.getOutstandingADisbursement();
                        }
                        Double outstanding_after_disbursement_amount = 0d;
                        Double outStandingAteam = null;
                        if (oldCloseDate.compareTo(closeDate) == 0) {
                            outStandingAteam = this.loanService.sumLoanA(dataMember.getEmployee().getIdEmployee(), tax_year, closeDate);
                        } else {
                            outStandingAteam = this.loanService.sumLoanA2(dataMember.getEmployee().getIdEmployee(), tax_year, closeDate, oldCloseDate);
                        }
//                                    outstanding_loan_a_team = outStandingAteam;
                        outstanding_loan_a_team = outStandingAteam + oustStandingLoanAfterVal;

                        OutStandingLoanA updateOutStandingLoanA = this.outStandingLoanAService.findBy(dataMember.getEmployee().getIdEmployee(), tax_year, number_of_disbursement.longValue());
                        if (updateOutStandingLoanA != null) {
                            updateOutStandingLoanA.setDisburseableAmount(disbursable_amount_team);
                            outstanding_after_disbursement_amount = Math.max((updateOutStandingLoanA.getLoanAmount() - disbursable_amount_team), 0);
                            updateOutStandingLoanA.setOutstandingADisbursement(outstanding_after_disbursement_amount);
                            this.outStandingLoanAService.update(updateOutStandingLoanA);
                        }
                    }
                    log.info("outstanding_loan_a_team" + outstanding_loan_a_team);
                    objMember.put("outstanding_loan_a_team", String.format("%.0f", outstanding_loan_a_team));
                    disbursed_amount_team = (disbursable_amount_team - outstanding_loan_a_team);
                    objMember.put("disbursed_amount_team", String.format("%.0f", Math.max(disbursed_amount_team, 0)));

                }
            }
            return ResponseEntity.ok(objMember.toString());
        } catch (JSONException ex) {
            CreateLog.createJson(ex.getMessage(), "viewburseByEmployee");
            return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature case Id : " + object.getCase_id() + " Not Found"),
                    HttpStatus.NOT_FOUND);
        } catch (ParseException ex) {
            log.error("viewburseByEmployee : " + ex.getMessage());
            Logger.getLogger(DisbursementDtoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/disbursement-dto/disburse-employee", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response disbursementEmployee(@RequestBody final DisbursementCaseIdDto object, Authentication authentication) {
        Date now = new Date();
        Date dateDisburse = new Date();
        Boolean process = true;
        String name = authentication.getName();
        log.info("disbursementEmployee json : " + object);
        Employee entityEmp = employeeService.findByEmployee(name);
        log.info("entity : " + entityEmp);
        if (entityEmp == null) {
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse("can't acces this feature :");
            log.error("disbursementEmployee" + rs.toString());
            CreateLog.createJson(rs, "viewburseByEmployee");
            return rs;
        }
        if (!entityEmp.getRoleName().contentEquals("finance")) {
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse("can't acces this feature :");
            log.error("listDisbursement" + rs.toString());
            CreateLog.createJson(rs, "listDisbursement");
            return rs;
        }
        String caseId = object.getCase_id();
        CaseDetails dataCase = caseDetailsService.findCaseId(caseId);
        if (dataCase == null) {
            rs.setResponse_code("55");
            rs.setInfo("Failed");
            rs.setResponse("can't acces this feature case Id : " + object.getCase_id() + " Not Found");
            log.error("listDisbursement" + rs.toString());
            CreateLog.createJson(rs, "listDisbursement");
            return rs;
        }

        Long engId = dataCase.getEngagementId();
        List<Member> memberList = memberService.listMemberDisburse(engId);
        JSONArray array = new JSONArray();
        JSONObject objDmp = new JSONObject();
        TeamMember entityTeam = teamMemberService.teamMemberByEngagement(engId);
        return null;

    }
}
