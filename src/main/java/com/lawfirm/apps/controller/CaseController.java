/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.CaseDocument;
import com.lawfirm.apps.model.Disbursement;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.model.EngagementHistory;
import com.lawfirm.apps.model.EntityPTKP;
import com.lawfirm.apps.model.EntityPeriod;
import com.lawfirm.apps.model.Loan;
import com.lawfirm.apps.model.Member;
import com.lawfirm.apps.model.OutStandingLoanA;
import com.lawfirm.apps.model.OutStandingLoanB;
import com.lawfirm.apps.model.Reimbursement;
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
import com.lawfirm.apps.service.interfaces.EngagementHistoryServiceIface;
import com.lawfirm.apps.service.interfaces.EngagementServiceIface;
import com.lawfirm.apps.service.interfaces.EntityPeriodServiceIface;
import com.lawfirm.apps.service.interfaces.EventServiceIface;
import com.lawfirm.apps.service.interfaces.LoanServiceIface;
import com.lawfirm.apps.service.interfaces.LoanTypeServiceIface;
import com.lawfirm.apps.service.interfaces.MemberServiceIface;
import com.lawfirm.apps.service.interfaces.OutStandingLoanAServiceIface;
import com.lawfirm.apps.service.interfaces.OutStandingLoanBServiceIface;
import com.lawfirm.apps.service.interfaces.ProfessionalServiceIface;
import com.lawfirm.apps.service.interfaces.PtkpServiceIface;
import com.lawfirm.apps.service.interfaces.ReimbursementServiceIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import com.lawfirm.apps.support.api.CaseApi;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
//@Slf4j
public class CaseController {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
    static String basepathUpload = "/opt/lawfirm/UploadFile/";
    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;
    SimpleDateFormat sdfYear;
    SimpleDateFormat sdfMonth;
    SimpleDateFormat sdfM;
    SimpleDateFormat sdfMY;
    SimpleDateFormat sdfDisbursM;
    SimpleDateFormat sdfDisbursMY;
    SimpleDateFormat sdfDisburse;

    Date now;
    String date_now;
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
    @Autowired
    DisbursementServiceIface disbursementService;
    @Autowired
    OutStandingLoanBServiceIface outStandingLoanBService;
    @Autowired
    OutStandingLoanAServiceIface outStandingLoanAService;
    @Autowired
    PtkpServiceIface ptkpService;
    @Autowired
    EntityPeriodServiceIface entityPeriodService;

    public CaseController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yyyy");
        this.sdfM = new SimpleDateFormat("MM");
        this.sdfMonth = new SimpleDateFormat("MMMM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
        this.sdfDisbursM = new SimpleDateFormat("MMM");
        this.sdfDisbursMY = new SimpleDateFormat("MMMyyyy");
        this.sdfDisburse = new SimpleDateFormat("dd-MMMM-yyyy");
    }

    @RequestMapping(value = "/case/{engagement_id}/document", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response uploadCaseDocument(@RequestPart("case_doc") MultipartFile file, @RequestParam("title") String title,
            @PathVariable("engagement_id") Long engagement_id, Authentication authentication) throws IOException {
        try {
            rs.setResponse_code("00");
            rs.setInfo("uploadCaseDocument Acces by : " + authentication.getName());
            rs.setResponse("engagement_id : " + engagement_id);
            CreateLog.createJson(rs, "upload-case-document");
            CaseDocument entCaseDocument = new CaseDocument();
            log.info("message" + rs.toString());
            Date todayDate = new Date();
            Date now = new Date();
            String pathDoc = null;
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entity = employeeService.findByEmployee(name);
            log.info("entity : " + entity);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                process = false;
                CreateLog.createJson(rs, "upload-case-document");
                log.error("upload-case-document : " + rs.toString());
                return rs;
            }
            if (!entity.getRoleName().contentEquals("dmp")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entity.getRoleName() + " permission deny ");
                process = false;
                CreateLog.createJson(rs, "upload-case-document");
                log.error("upload-case-document : " + rs.toString());
                return rs;
            }

            CaseDetails caseDetails = caseDetailsService.findById(engagement_id);
            if (caseDetails == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Id Not Found ");
                process = false;
                CreateLog.createJson(rs, "upload-case-document");
                log.error("upload-case-document : " + rs.toString());
                return rs;
            }
            if (!caseDetails.getStatus().contentEquals("a")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Case Status : " + caseDetails.getStatus());
                CreateLog.createJson(rs, "create-event");
                log.error("upload-case-document : " + rs.toString());
                process = false;
                return rs;
            }
            if (caseDetails.getIsActive().contains("4")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + caseDetails.getCaseID() + " Status Closed ");
                CreateLog.createJson(rs, "upload-case-document");
                log.error("upload-case-document : " + rs.toString());
                return rs;
            }
            pathDoc = basepathUpload + "engagemet" + caseDetails.getCaseID() + "/" + "file" + "/";

            if (process) {
                if (!file.isEmpty()) {

                    File newFolder = new File(pathDoc);
                    boolean created = newFolder.mkdir();
                    if (!newFolder.getParentFile().exists()) {
                        newFolder.getParentFile().mkdirs();
                    } else {
                        newFolder.getParentFile().mkdirs();
                    }
                    if (!newFolder.exists()) {
                        if (created) {
                            System.out.println("Folder was created !");
                        } else {
                            System.out.println("Folder exists");
                        }
                    }

                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(pathDoc + file.getOriginalFilename().replaceAll(" ", ""));
                    Files.write(path, bytes);
                    String fileName = file.getOriginalFilename().replaceAll(" ", "");
                    log.info("file getOriginalFilename : " + file.getOriginalFilename().replaceAll(" ", ""));
                    entCaseDocument.setLinkDocument(pathDoc + file.getOriginalFilename().replaceAll(" ", ""));
                    entCaseDocument.setTitle(title);
                    entCaseDocument.setCaseDetails(caseDetails);
                    CaseDocument cerateCaseDocument = this.caseDocumentService.create(entCaseDocument);
                    if (cerateCaseDocument != null) {
                        rs.setResponse_code("00");
                        rs.setInfo("Succes");
                        rs.setResponse("Upload: Succes : " + fileName);
                        CreateLog.createJson(rs, "upload-case-document");
                        log.info("upload-case-document : " + rs.toString());
                        return rs;
                    } else {
                        rs.setResponse_code("55");
                        rs.setInfo("Error");
                        rs.setResponse("Error Upload Document");
                        CreateLog.createJson(rs, "upload-case-document");
                        log.error("upload-case-document : " + rs.toString());
                        return rs;
                    }

                }
            }
            return rs;
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "upload-case-document");
            log.error("upload-case-document : " + ex.getMessage());
            return rs;

        }

    }

    @RequestMapping(value = "/case/{engagement_id}/upload", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response uploadMultipleDocument(@RequestPart("case_doc") MultipartFile[] files, @PathVariable("engagement_id") Long engagement_id, Authentication authentication) throws IOException {
        try {
            rs.setResponse_code("00");
            rs.setInfo("uploadMultipleDocument Acces by : " + authentication.getName());
            rs.setResponse("engagement_id : " + engagement_id);
            log.info("uploadMultipleDocument : " + rs.toString());
            CreateLog.createJson(rs, "uploadMultipleDocument");
            CaseDocument entCaseDocument = new CaseDocument();

            Date todayDate = new Date();
            Date now = new Date();
            String pathDoc = null;
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entity = employeeService.findByEmployee(name);
            log.info("entity : " + entity);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                process = false;
                CreateLog.createJson(rs, "uploadMultipleDocument");
                log.error("uploadMultipleDocument : " + rs.toString());
                return rs;
            }
            if (!entity.getRoleName().contentEquals("dmp")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entity.getRoleName() + " permission deny ");
                process = false;
                CreateLog.createJson(rs, "uploadMultipleDocument");
                log.error("uploadMultipleDocument : " + rs.toString());
                return rs;
            }

            CaseDetails caseDetails = caseDetailsService.findById(engagement_id);
            if (caseDetails == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Id Not Found ");
                process = false;
                CreateLog.createJson(rs, "uploadMultipleDocument");
                log.error("uploadMultipleDocument : " + rs.toString());
                return rs;
            }
            if (caseDetails.getStatus().contains("4")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + caseDetails.getCaseID() + " Status Closed ");
                CreateLog.createJson(rs, "uploadMultipleDocument");
                log.error("uploadMultipleDocument : " + rs.toString());
                return rs;
            }
            pathDoc = basepathUpload + "engagemet" + caseDetails.getCaseID() + "/" + "file" + "/";

            if (process) {
//                
                List<String> fileNames = new ArrayList<>();

            }
            return rs;
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "upload-case-document");
            log.error("uploadMultipleDocument : " + ex.getMessage());
            return rs;

        }

    }

    @RequestMapping(value = "/case/documents", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> listDocument(Authentication authentication) {
        try {
            rs.setResponse_code("00");
            rs.setInfo("listDocument Acces by : " + authentication.getName());
            rs.setResponse("");
            log.info("list-case-document : " + rs.toString());
            CreateLog.createJson(rs, "list-case-document");
            Date todayDate = new Date();
            Date now = new Date();
            String pathDoc = null;
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entity = employeeService.findByEmployee(name);
            log.info("entity : " + entity);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("can't acces this feature :");
                process = false;
                CreateLog.createJson(rs, "list-case-document");
                log.error("list-case-document : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }

            List<CaseDocument> listDoc = caseDocumentService.findDocByCaseId(0l);
            if (listDoc == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("can't acces this feature :");
                process = false;
                CreateLog.createJson(rs, "list-case-document");
                log.error("list-case-document : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            JSONArray array = new JSONArray();
            if (process) {

                for (int i = 0; i < listDoc.size(); i++) {
                    JSONObject obj = new JSONObject();
                    CaseDocument data = listDoc.get(i);

                    if (data.getCaseDetails() == null) {
                        obj.put("engagement_id", "");
                    } else {
                        obj.put("engagement_id", data.getCaseDetails().getEngagementId());
                        obj.put("case_id", data.getCaseDetails().getCaseID());
                    }
                    if (data.getCase_document_id() == null) {
                        obj.put("case_document_id", "");
                    } else {
                        obj.put("case_document_id", data.getCase_document_id());
                    }
                    if (data.getTitle() == null) {
                        obj.put("title", "");
                    } else {
                        obj.put("title", data.getTitle());
                    }
                    if (data.getDate_input() == null) {
                        obj.put("upload_date", "");
                    } else {
                        obj.put("upload_date", data.getDate_input());
                    }
                    if (data.getLinkDocument() == null) {
                        obj.put("link_document", "");
                    } else {
                        obj.put("link_document", data.getLinkDocument());
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
            CreateLog.createJson(ex.getMessage(), "list-case-document");
            log.error("list-case-document : " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/case/{engagement_id}/documents", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> listDocumentByEngId(@PathVariable("engagement_id") Long engagement_id, Authentication authentication) throws IOException {
        try {
            rs.setResponse_code("00");
            rs.setInfo("listDocumentByEngId Acces by : " + authentication.getName());
            rs.setResponse("engagement_id : " + engagement_id);
            log.info("list-case-document-by-engagement_id : " + rs.toString());
            CreateLog.createJson(rs, "list-case-document-by-engagement_id");
            Date todayDate = new Date();
            Date now = new Date();
            String pathDoc = null;
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entity = employeeService.findByEmployee(name);
            log.info("entity : " + entity);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("can't acces this feature :");
                process = false;
                CreateLog.createJson(rs, "list-case-document-by-engagement_id");
                log.error("list-case-document-by-engagement_id : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }

            List<CaseDocument> listDoc = caseDocumentService.findDocByCaseId(engagement_id);
            if (listDoc == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("engagement_id : " + engagement_id + " Not Found");
                process = false;
                CreateLog.createJson(rs, "list-case-document-by-engagement_id");
                log.error("list-case-document-by-engagement_id : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            JSONArray array = new JSONArray();
            if (process) {

                for (int i = 0; i < listDoc.size(); i++) {
                    JSONObject obj = new JSONObject();
                    CaseDocument data = listDoc.get(i);

                    if (data.getCaseDetails() == null) {
                        obj.put("engagement_id", "");
                    } else {
                        obj.put("engagement_id", data.getCaseDetails().getEngagementId());
                        obj.put("case_id", data.getCaseDetails().getCaseID());
                    }
                    if (data.getCase_document_id() == null) {
                        obj.put("case_document_id", "");
                    } else {
                        obj.put("case_document_id", data.getCase_document_id());
                    }
                    if (data.getTitle() == null) {
                        obj.put("title", "");
                    } else {
                        obj.put("title", data.getTitle());
                    }
                    if (data.getDate_input() == null) {
                        obj.put("upload_date", "");
                    } else {
                        obj.put("upload_date", data.getDate_input());
                    }
                    if (data.getLinkDocument() == null) {
                        obj.put("link_document", "");
                    } else {
                        obj.put("link_document", data.getLinkDocument());
                    }
                    array.put(obj);
                }
            }
            log.info("list-case-document-by-engagement_id : " + array.toString());
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "list-case-document-by-engagement_id");
            log.error("list-case-document-by-engagement_id : " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/case/document/{case_document_id}/view", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> viewDocument(ServletRequest request, HttpServletResponse response, @PathVariable("case_document_id") String case_document_id, Authentication authentication) throws IOException {
        try {
            rs.setResponse_code("00");
            rs.setInfo("viewDocument access By : " + authentication.getName());
            rs.setResponse("case_document_id : " + case_document_id);
            log.error("view-Document : " + rs.toString());
            CreateLog.createJson(rs, "view-Document");
            JSONObject jsonobj = new JSONObject();
            Date todayDate = new Date();
            Date now = new Date();
            String pathDoc = null;
            Boolean process = true;
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entity = employeeService.findByEmployee(name);
            log.info("entity : " + entity);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("can't acces this feature");
                process = false;
                log.error("view-Document : " + rs.toString());
                CreateLog.createJson(rs, "view-Document");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            CaseDocument dataDocument = caseDocumentService.findById(case_document_id);
            if (dataDocument == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("case_document_id : " + case_document_id + "Not Found");
                log.error("view-Document : " + rs.toString());
                process = false;
                CreateLog.createJson(rs, "view-Document");
                return new ResponseEntity(new CustomErrorType("55", "Error", "case_document_id : " + case_document_id + "Not Found"),
                        HttpStatus.NOT_FOUND);
            }
            if (process) {
                try {
                    log.info("dataDocument.getLinkDocument()" + dataDocument.getLinkDocument());
                    byte[] input_file = Files.readAllBytes(Paths.get(dataDocument.getLinkDocument()));
                    String linkDoc = new String(Base64.getEncoder().encode(input_file));

//                    rs.setResponse_code("00");
//                    rs.setResponse("success");
//                    rs.setInfo(linkDoc);
                    jsonobj.put("response_code", "00");
                    jsonobj.put("response", "success");
                    jsonobj.put("info", linkDoc);
                } catch (JSONException ex) {
                    Logger.getLogger(CaseController.class.getName()).log(Level.SEVERE, null, ex);
                    log.error("view-Document : " + ex.getMessage());
                    return new ResponseEntity(new CustomErrorType("55", "Error", "case_document_id : " + case_document_id + "Not Found"),
                            HttpStatus.NOT_FOUND);
                }
                log.info("view-Document : Success");
                return ResponseEntity.ok(jsonobj.toString());
            }
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse("case_document_id : " + case_document_id + "Not Found");
            log.error("view-Document : " + rs.toString());
            CreateLog.createJson(rs, "view-Document");
            return new ResponseEntity(new CustomErrorType("55", "Error", "case_document_id : " + case_document_id + "Not Found"),
                    HttpStatus.NOT_FOUND);
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            log.error("view-Document : " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "view-Document");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/case/close", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> closingCase(@RequestBody CaseApi object, Authentication authentication) {//@RequestBody final EngagementApi object, 
        try {
            rs.setResponse_code("00");
            rs.setInfo("closingCase access By : " + authentication.getName());
            rs.setResponse("engagement_id : " + object.getEngagement_id().toString());
            log.info("closing-Case : " + rs.toString());
            CreateLog.createJson(rs, "closing-Case");
            Date now = new Date();
//          Integer number = null;
            Integer number = 0;
            EngagementHistory enHistory = new EngagementHistory();
            Disbursement disbursement = new Disbursement();
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            log.info("engagement_id : " + object.getEngagement_id());
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't closing case :");
                CreateLog.createJson(rs, "closing-Case");
                log.error("closing-Case : " + rs.toString());

                return new ResponseEntity(new CustomErrorType("55", "Error", rs.toString()),
                        HttpStatus.NOT_FOUND);
            }
            if (!entityEmp.getRoleName().contentEquals("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entityEmp.getRoleName() + " permission deny ");
                log.error("closing-Case : " + rs.toString());
                CreateLog.createJson(rs, "closing-Case");

                return new ResponseEntity(new CustomErrorType("55", "Error", rs.toString()),
                        HttpStatus.NOT_FOUND);
            }
            CaseDetails entityCase = caseDetailsService.findById(object.getEngagement_id());
            if (entityCase == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't closing case engagement_id " + object.getEngagement_id() + "Not Found");
                CreateLog.createJson(rs, "closing-Case");
                log.error("closing-Case : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't closing case engagement_id " + object.getEngagement_id() + "Not Found"),
                        HttpStatus.NOT_FOUND);
            }
            if (entityCase.getStatus().contains("s")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + entityCase.getCaseID() + " Need Admin Approve ");
                CreateLog.createJson(rs, "closing-Case");
                log.error("closing-Case : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "case Case Id : " + entityCase.getCaseID() + " Need Admin Approve "),
                        HttpStatus.NOT_FOUND);
            }
            if (entityCase.getStatus().contains("closed")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + entityCase.getCaseID() + " Status Closed ");
                CreateLog.createJson(rs, "closing-Case");
                log.error("closing-Case : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Case Id : " + entityCase.getCaseID() + " Status Closed "),
                        HttpStatus.NOT_FOUND);
            }
            if (entityCase.getDisburseBy() != null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + entityCase.getCaseID() + " Already Disburse by : " + entityCase.getDisburseBy());
                CreateLog.createJson(rs, "closing-Case");
                log.error("closing-Case : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Case Id : " + entityCase.getCaseID() + " Already Disburse "),
                        HttpStatus.NOT_FOUND);
            }
            if (entityCase.getIsActive().contains("4")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + entityCase.getCaseID() + " Status Closed ");
                CreateLog.createJson(rs, "closing-Case");
                log.error("closing-Case : " + rs.toString());

                return new ResponseEntity(new CustomErrorType("55", "Error", "Case Id : " + entityCase.getCaseID() + " Status Closed "),
                        HttpStatus.NOT_FOUND);
            }

            String close_date = dateFormat.format(now);
            Date closeDate = dateFormat.parse(close_date);
            String cutOffDateVal = null;
            Date cutOffDate = null;
            entityCase.setIsActive("4");
            entityCase.setStatus("closed");
            entityCase.setClosedBy(entityEmp.getEmployeeId());
            entityCase.setClosed_date(closeDate);
            enHistory.setEngagement(entityCase);
            enHistory.setUserId(entityEmp.getIdEmployee());
            disbursement.setEngagement(entityCase);
            enHistory.setResponse("closed By : " + entityEmp.getEmployeeId());
            if (object.getCut_off_date() != null) {
                cutOffDateVal = dateFormat.format(object.getCut_off_date());
                cutOffDate = dateFormat.parse(cutOffDateVal);
            }
            List<Loan> listLoanB = loanService.geLoanBCaseId(entityCase.getCaseID());
            if (listLoanB.isEmpty()) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse(" Loan B Case Id : " + entityCase.getCaseID() + " Not Found");
                CreateLog.createJson(rs, "closing-Case");
                log.error("closing-Case : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", " Loan B Case Id : " + entityCase.getCaseID() + " Not Found"),
                        HttpStatus.NOT_FOUND
                );
            }

            List<Loan> cekloanb = loanService.cekLoanBStatusApprove(entityCase.getCaseID());
            if (cekloanb.size() > 0) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Chek Loan B Case Id " + entityCase.getCaseID());
                CreateLog.createJson(rs, "closing-Case");
                log.error("closing-Case : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Chek Loan B Case Id : " + entityCase.getCaseID()),
                        HttpStatus.NOT_FOUND);
            }
            List<Reimbursement> listReimbursement = reimbursementService.getReimbusementByCseId(entityCase.getCaseID());
            if (listReimbursement.isEmpty()) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Reimbursement Case Id : " + entityCase.getCaseID() + " Not Found");
                CreateLog.createJson(rs, "closing-Case");
                log.error("closing-Case : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", " Reimbursement Case Id : " + entityCase.getCaseID() + " Not Found"),
                        HttpStatus.NOT_FOUND
                );
            }
            List<Reimbursement> cekReimbursement = reimbursementService.cekReimbusementStatusApprove(entityCase.getCaseID());
            if (cekReimbursement.size() > 0) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Cek Reimbursement Case Id " + entityCase.getCaseID());
                CreateLog.createJson(rs, "closing-Case");
                log.error("closing-Case : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Chek Reimbursement Case Id : " + entityCase.getCaseID()),
                        HttpStatus.NOT_FOUND);
            }

            CaseDetails closeCase = this.caseDetailsService.update(entityCase);

            if (closeCase != null) {
                this.engagementHistoryService.create(enHistory);
                return disburse(closeCase.getEngagementId(), cutOffDate, authentication);
//                
            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't closing case engagement_id " + object.getEngagement_id() + "Not Found");
                CreateLog.createJson(rs, "closing-Case");
                log.error("closing-Case : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't closing case engagement_id " + object.getEngagement_id() + "Not Found"),
                        HttpStatus.NOT_FOUND);

            }

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "closing-Case");
            log.error("closing-Case : " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);

        } catch (ParseException ex) {
            Logger.getLogger(CaseController.class.getName()).log(Level.SEVERE, null, ex);
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "closing-Case");
            log.error("closing-Case : " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @XxsFilter
    public ResponseEntity<?> disburse(Long engagement_id, Date cutOffDate, Authentication authentication) {
        try {
            rs.setResponse_code("00");
            rs.setInfo("disburse access By : " + authentication.getName());
            rs.setResponse("engagement_id : " + engagement_id.toString());
            CreateLog.createJson(rs, "disburse");
            log.info("disburse PathVariable engagement_id : " + engagement_id);
            log.info("disburse cutOffDate: " + cutOffDate);
            Date now = new Date();
            Integer number = 0;
            OutStandingLoanA outStandingLoanA = new OutStandingLoanA();

            String name = authentication.getName();
            String caseId = null;

            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entityEmp : " + entityEmp);
            log.info("engagement_id : " + engagement_id);
            Boolean process = true;
            CaseDetails entityCase = caseDetailsService.findById(engagement_id);
            if (entityCase == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't closing case engagement_id " + engagement_id + "Not Found");
                CreateLog.createJson(rs, "disburse");
                process = false;
                log.error("disbursement : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't closing case engagement_id " + engagement_id + "Not Found"),
                        HttpStatus.NOT_FOUND);
            }

            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't disbursement :");
                CreateLog.createJson(rs, "disburse");
                log.error("disburse jsonObject: " + rs.toString());
                entityCase.setStatus("a");
                entityCase.setIsActive("1");
                this.caseDetailsService.update(entityCase);
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't disbursement "),
                        HttpStatus.NOT_FOUND);
            }
            if (!entityEmp.getRoleName().contentEquals("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entityEmp.getRoleName() + " permission deny ");
                CreateLog.createJson(rs, "disburse");
                process = false;
                log.error("disbursement jsonObject: " + rs.toString());
                entityCase.setStatus("a");
                entityCase.setIsActive("1");
                this.caseDetailsService.update(entityCase);
                return new ResponseEntity(new CustomErrorType("55", "Error", "role : " + entityEmp.getRoleName() + " permission deny "),
                        HttpStatus.NOT_FOUND);
            }
            if (entityCase.getStatus().contentEquals("s")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + entityCase.getCaseID() + " Need Approve By  Admin");
                CreateLog.createJson(rs, "disburse");
                process = false;
                log.error("disburse : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "case Case Id : " + entityCase.getCaseID() + " Need Approve By  Admin"),
                        HttpStatus.NOT_FOUND);
            }
//            if (entityCase.getStatus().contentEquals("closed")) {
//                rs.setResponse_code("55");
//                rs.setInfo("Failed");
//                rs.setResponse("case Case Id : " + entityCase.getCaseID() + " Already Closed");
//                CreateLog.createJson(rs, "disburse");
//                process = false;
//                log.error("disburse : " + rs.toString());
//                return new ResponseEntity(new CustomErrorType("55", "Error", "case Case Id : " + entityCase.getCaseID() + " Already Closed"),
//                        HttpStatus.NOT_FOUND);
//            }
            if (entityCase.getStatus().contentEquals("r")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + entityCase.getCaseID() + " Rejected By  Admin");
                CreateLog.createJson(rs, "disburse");
                process = false;
                log.error("disburse : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "case Case Id : " + entityCase.getCaseID() + " Rejected By  Admin"),
                        HttpStatus.NOT_FOUND);
            }
            Disbursement disbursementFindbyCaseId = this.disbursementService.disbursementFindbyCaseId(entityCase.getCaseID());
            if (disbursementFindbyCaseId != null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + entityCase.getCaseID() + " Already Close By Admin");
                CreateLog.createJson(rs, "disburse");
                process = false;
                log.error("disburse : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Case Id : " + entityCase.getCaseID() + " Already Close By Admin"),
                        HttpStatus.NOT_FOUND);
            }
            if (process) {

                Disbursement disbursement = new Disbursement();
                disbursement.setEngagement(entityCase);

                Integer bulan = Integer.parseInt(sdfM.format(cutOffDate));

                if (bulan <= 03) {
                    number = 1;
                    log.info("number : " + number);
                }
                if (bulan > 03 && bulan <= 07) {
                    number = 2;
                    log.info("number : " + number);
                }
                if (bulan > 07 && bulan <= 11) {
                    number = 3;
                    log.info("number : " + number);
                }

                String dt = dateFormat.format(new Date());
                Date disburse = dateFormat.parse(dt);
                String disburseM = sdfDisbursM.format(new Date());
                String disburseMy = sdfDisbursMY.format(new Date());
                System.out.println("isi disburseM : " + disburseM);
                disbursement.setBulanInput(disburseM);

                String dsb_id = number.toString();

                disbursement.setDisbursementId(dsb_id);
                disbursement.setDisburse_date(disburse);

                if (number == 1) {
                    disbursement.setOldCutOffDate(cutOffDate);
                }
                if (number == 2) {
                    List<Disbursement> endisbursement = disbursementService.disbursement(1, sdfYear.format(now));
                    if (endisbursement != null) {
                        String oldCutOffdate = dateFormat.format(endisbursement.get(0).getCutOffDate());
                        Date oldCutOffDate = dateFormat.parse(oldCutOffdate);
                        disbursement.setOldCutOffDate(oldCutOffDate);
                    } else {
                        disbursement.setOldCutOffDate(cutOffDate);
                    }
                }
                if (number == 3) {
                    List<Disbursement> endisbursement = disbursementService.disbursement(2, sdfYear.format(now));
                    if (endisbursement != null) {
                        String oldCutOffdate = dateFormat.format(endisbursement.get(0).getCutOffDate());
                        Date oldCutOffDate = dateFormat.parse(oldCutOffdate);
                        disbursement.setOldCutOffDate(oldCutOffDate);
                    } else {
                        disbursement.setOldCutOffDate(cutOffDate);
                    }
                }
                disbursement.setCutOffDate(cutOffDate);
                disbursement.setNumberOfDisbursement(number);
                disbursement.setBulanInput(sdfMonth.format(now));
                disbursement.setTahunInput(sdfYear.format(now));
                Disbursement endisbursement = this.disbursementService.create(disbursement);

//                
//                
                if (endisbursement != null) {

                    List<TeamMember> entityTeam = teamMemberService.listTeamMemberByEngagement(engagement_id);
                    if (entityTeam == null) {
                        disbursementService.remove(endisbursement);
                        rs.setResponse_code("55");
                        rs.setInfo("Failed");
                        rs.setResponse("can't disburse engagement_id " + engagement_id + "Not Found");
                        CreateLog.createJson(rs, "disburse");
                        log.error("disburse : " + rs.toString());
                        entityCase.setStatus("a");
                        entityCase.setIsActive("1");
                        this.caseDetailsService.update(entityCase);
                        return new ResponseEntity(new CustomErrorType("55", "Error", "can't disburse engagement_id " + engagement_id + "Not Found"),
                                HttpStatus.NOT_FOUND);
                    }
                    Long id_team = 0l;

                    Double total_income_fortax_purpose_dmp = 0d;
                    Double total_income_fortax_purpose_team = 0d;

                    Double outstanding_loan_a_dmp = 0d;
                    Double outstanding_loan_a_team = 0d;
                    String oldclosedate = null;
                    Date oldCloseDate = null;
//                    
                    Double taxable_income_dmp = 0d;
                    Double taxable_income_team = 0d;
                    Double dmpPortion = 0d;
                    Double amount_portion_dmp = 0d;
                    Double amount_portion_team = 0d;
                    Integer anual_salary_team = (4000000 * 13);
                    int a_jabatan = (60000000 * 5) / 100;
                    int b_jabatan = ((anual_salary_team * 5) / 100) / 13;
                    int masa_kerja = 0;
                    String jabatan_per_bulan = String.format("%d", Math.min(a_jabatan, b_jabatan));
                    Integer jabatan_per_tahun = (masa_kerja * Integer.parseInt(jabatan_per_bulan));

                    for (int j = 0; j < entityTeam.size(); j++) {
                        CaseDetails caseDetails = caseDetailsService.findById(engagement_id);
                        if (caseDetails != null) {
                            dmpPortion = caseDetails.getDmpPortion();
                        } else {
                            dmpPortion = 0d;
                        }
                        EntityPeriod entityPeriodDmp = new EntityPeriod();
                        EntityPeriod entityPeriod = new EntityPeriod();
                        TeamMember dataTeam = entityTeam.get(j);

                        if (dataTeam.getTeamMemberId() != null) {
                            id_team = dataTeam.getTeamMemberId();
                        }

                        Employee getDmp = employeeService.findById(dataTeam.getDmpId());
                        String dmp_tax_status = getDmp.getTaxStatus();
                        Double ptkp = 0d;
                        log.info("dmp_tax_status : " + dmp_tax_status);
                        EntityPTKP ptkpDmp = this.ptkpService.findPTKPByTaxStatus(dmp_tax_status);
                        log.info("ptkpDmp : " + ptkpDmp);
                        if (ptkpDmp != null) {
                            ptkp = ptkpDmp.getPtkp();
                        } else {
                            disbursementService.remove(endisbursement);
                            rs.setResponse_code("55");
                            rs.setInfo("Failed");
                            rs.setResponse("can't disburse engagement_id " + engagement_id + "Not Found");
                            CreateLog.createJson(rs, "disburse");
                            log.error("disburse : " + rs.toString());
                            entityCase.setStatus("a");
                            entityCase.setIsActive("1");
                            this.caseDetailsService.update(entityCase);
                            return new ResponseEntity(new CustomErrorType("55", "Error", "can't disburse engagement_id " + engagement_id + "Not Found"),
                                    HttpStatus.NOT_FOUND);
                        }

                        taxable_income_dmp = (jabatan_per_tahun - ptkp);

                        List<Member> entityMember = memberService.findByIdTeam(id_team);
                        if (entityMember == null) {
                            disbursementService.remove(endisbursement);
                            rs.setResponse_code("55");
                            rs.setInfo("Failed");
                            rs.setResponse("can't disburse engagement_id " + engagement_id + "Not Found");
                            CreateLog.createJson(rs, "disburse");
                            log.error("disburse : " + rs.toString());
                            entityCase.setStatus("a");
                            entityCase.setIsActive("1");
                            this.caseDetailsService.update(entityCase);
                            return new ResponseEntity(new CustomErrorType("55", "Error", "can't disburse engagement_id " + engagement_id + "Not Found"),
                                    HttpStatus.NOT_FOUND);
                        }

                        for (int k = 0; k < entityMember.size(); k++) {
                            Member dataMember = entityMember.get(k);
                            String team_tax_status = dataMember.getEmployee().getTaxStatus();
                            Double ptkp_team = 0d;
                            log.info("team_tax_status : " + team_tax_status);
//                                    String jabatan_perbulan_dmp = String.format("%d", (((4000000 * 13) * 5) / 100) / 13);
                            EntityPTKP ptkpTeam = this.ptkpService.findPTKPByTaxStatus(team_tax_status);
                            log.info("ptkpTeam : " + ptkpTeam);
                            if (ptkpDmp != null) {
                                ptkp_team = ptkpTeam.getPtkp();
                            }
                            taxable_income_team = (jabatan_per_tahun - ptkp_team);
                            entityPeriod.setTaxYear(sdfYear.format(now));
                            entityPeriod.setCaseId(entityCase.getCaseID());
                            entityPeriod.setDisburseId(dsb_id);
                            entityPeriod.setNumberDisbursement(number);
                            entityPeriod.setBulanDisburse(sdfMonth.format(now));
                            amount_portion_team = (dmpPortion * dataMember.getFeeShare()) / 100;
                            log.info("amount_portion_team" + String.format("%.0f", amount_portion_team));
                            entityPeriod.setIdEmployee(dataMember.getEmployee().getIdEmployee());
                            entityPeriod.setEmployeeId(dataMember.getEmployee().getEmployeeId());

                            if (number == 1) {
                                Double outStandingAteam = this.loanService.sumLoanA(dataMember.getEmployee().getIdEmployee(), sdfYear.format(now), cutOffDate);
                                outstanding_loan_a_team = outStandingAteam;
                                log.info("outstanding_loan_a_team 1 " + String.format("%.0f", outstanding_loan_a_team));
                                outStandingLoanA.setIdEmployee(dataMember.getEmployee().getIdEmployee());
                                outStandingLoanA.setTaxYear(sdfYear.format(now));
                                outStandingLoanA.setDisburseId(dsb_id);
                                outStandingLoanA.setCutOffDate(cutOffDate);
                                outStandingLoanA.setLoanAmount(outstanding_loan_a_team);
                                outStandingLoanA.setNumberDisbursement(number.longValue());
                                entityPeriod.setPrevDisbursement(amount_portion_team);
                                entityPeriod.setAmountPortion(amount_portion_team);
                                this.outStandingLoanAService.create(outStandingLoanA);
                            }
                            if (number == 2) {

                                oldclosedate = dateFormat.format(endisbursement.getOldCutOffDate());
                                oldCloseDate = dateFormat.parse(oldclosedate);
                                Double outStandingAteam = this.loanService.sumLoanA2(dataMember.getEmployee().getIdEmployee(), sdfYear.format(now), cutOffDate, oldCloseDate);
                                if (outStandingAteam == null) {
                                    disbursementService.remove(endisbursement);
                                    entityCase.setStatus("a");
                                    entityCase.setIsActive("1");
                                    this.caseDetailsService.update(entityCase);
                                    rs.setResponse_code("55");
                                    rs.setInfo("Failed");
                                    rs.setResponse("can't Sum Loan A cek Your Cut Off Date " + cutOffDate + "Not Found");
                                    CreateLog.createJson(rs, "disburse");
                                    log.error("disburse : " + rs.toString());
                                    return new ResponseEntity(new CustomErrorType("55", "Error", "can't Sum Loan A cek Your Cut Off Date " + cutOffDate + "Not Found"),
                                            HttpStatus.NOT_FOUND
                                    );
                                }
                                outstanding_loan_a_team = outStandingAteam;
                                log.info("outstanding_loan_a_team 2 " + String.format("%.0f", outstanding_loan_a_team));
                                outStandingLoanA.setIdEmployee(dataMember.getEmployee().getIdEmployee());
                                outStandingLoanA.setTaxYear(sdfYear.format(now));
                                outStandingLoanA.setDisburseId(dsb_id);
                                outStandingLoanA.setCutOffDate(cutOffDate);
                                outStandingLoanA.setLoanAmount(outstanding_loan_a_team);
                                outStandingLoanA.setNumberDisbursement(number.longValue());
                                Double prevDisbursement = this.entityPeriodService.getPreviousDisbursement(2, dataMember.getEmployee().getIdEmployee(), sdfYear.format(now));
                                entityPeriod.setPrevDisbursement(prevDisbursement);
                                entityPeriod.setAmountPortion(amount_portion_team);
                                this.outStandingLoanAService.create(outStandingLoanA);

                            }
                            if (number == 3) {
                                oldclosedate = dateFormat.format(endisbursement.getOldCutOffDate());
                                oldCloseDate = dateFormat.parse(oldclosedate);
                                Double outStandingAteam = this.loanService.sumLoanA2(dataMember.getEmployee().getIdEmployee(), sdfYear.format(now), cutOffDate, oldCloseDate);
                                if (outStandingAteam == null) {
                                    disbursementService.remove(endisbursement);
                                    entityCase.setStatus("a");
                                    entityCase.setIsActive("1");
                                    this.caseDetailsService.update(entityCase);
                                    rs.setResponse_code("55");
                                    rs.setInfo("Failed");
                                    rs.setResponse("can't Sum Loan A cek Your Cut Off Date " + cutOffDate + "Not Found");
                                    CreateLog.createJson(rs, "disburse");
                                    log.error("disburse : " + rs.toString());
                                    return new ResponseEntity(new CustomErrorType("55", "Error", "can't Sum Loan A cek Your Cut Off Date " + cutOffDate + "Not Found"),
                                            HttpStatus.NOT_FOUND
                                    );
                                }
                                outstanding_loan_a_team = outStandingAteam;
                                log.info("outstanding_loan_a_team 3 " + String.format("%.0f", outstanding_loan_a_team));
                                outStandingLoanA.setIdEmployee(dataMember.getEmployee().getIdEmployee());
                                outStandingLoanA.setTaxYear(sdfYear.format(now));
                                outStandingLoanA.setDisburseId(dsb_id);
                                outStandingLoanA.setCutOffDate(cutOffDate);
                                outStandingLoanA.setLoanAmount(outstanding_loan_a_team);
                                outStandingLoanA.setNumberDisbursement(number.longValue());
                                EntityPeriod prevDisbursement = this.entityPeriodService.getPrevDisbursement(3, dataMember.getEmployee().getIdEmployee(), sdfYear.format(now));
                                entityPeriod.setPrevDisbursement(prevDisbursement.getPrevDisbursement() + prevDisbursement.getPrevDisbursement());
                                this.outStandingLoanAService.create(outStandingLoanA);
                            }
                            this.entityPeriodService.create(entityPeriod);
                        }
                        caseId = entityPeriod.getCaseId();
                        return this.disbursement(caseId, authentication);
                    }
                } else {
                    disbursementService.remove(endisbursement);
                    entityCase.setStatus("a");
                    entityCase.setIsActive("1");
                    this.caseDetailsService.update(entityCase);
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("can't disburse engagement_id " + engagement_id + "Not Found");
                    CreateLog.createJson(rs, "disburse");
                    log.error("disburse : " + rs.toString());
                    return new ResponseEntity(new CustomErrorType("55", "Error", "can't disburse engagement_id " + engagement_id + "Not Found"),
                            HttpStatus.NOT_FOUND
                    );
                }
            }
        } catch (JSONException | ParseException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            log.error("disbursement : " + ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "disbursement");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
        rs.setResponse_code("55");
        rs.setInfo("Failed");
        rs.setResponse("can't disbursement engagemet " + engagement_id + "Not Found");
        CreateLog.createJson(rs, "disbursement");
        log.error("disbursement : " + rs.toString());
        return new ResponseEntity(new CustomErrorType("55", "Error", "can't disbursement engagemet " + engagement_id + "Not Found"),
                HttpStatus.NOT_FOUND);

    }

    @XxsFilter
    public ResponseEntity<?> disbursement(String caseId, Authentication authentication) {
        try {
            log.info("disbursement " + caseId);
            Date now = new Date();
            Date dateDisburs = new Date();
            Boolean process = true;
            String name = authentication.getName();
//            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            CaseDetails caseDetail = this.caseDetailsService.findByCaseId(caseId, "0");
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "disbursementbyCaseId");
                log.error("disbursement : " + rs.toString());
                process = false;
                caseDetail.setStatus("a");
                caseDetail.setIsActive("1");
                this.caseDetailsService.update(caseDetail);
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            Employee dataEMploye = employeeService.findById(entityEmp.getIdEmployee());
            if (dataEMploye == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Employee Id Not found");
                CreateLog.createJson(rs, "disbursementbyCaseId");
                process = false;
                caseDetail.setStatus("a");
                caseDetail.setIsActive("1");
                this.caseDetailsService.update(caseDetail);
                log.error("disbursement : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "Employee Id Not found"),
                        HttpStatus.NOT_FOUND);

            }
            if (process) {

                String closedate = null;
                Date closeDate = null;
                String oldclosedate = null;
                Date oldCloseDate = null;
                String tax_year = null;
                String param = null;
                log.info("case_id : " + caseId);
//                List<Disbursement> disbursementlist = disbursementService.disbursementbyCaseId(object.getCase_id());
                Disbursement dataDisbursement = disbursementService.disbursementFindbyCaseId(caseId);
                if (dataDisbursement == null) {
                    rs.setResponse_code("55");
                    rs.setInfo("Failed");
                    rs.setResponse("caseId : " + caseId + " Not found");
                    CreateLog.createJson(rs, "disbursement");
                    process = false;
                    caseDetail.setStatus("a");
                    caseDetail.setIsActive("1");
                    this.caseDetailsService.update(caseDetail);
                    return new ResponseEntity(new CustomErrorType("55", "Error", "caseId : " + caseId + " Not found"),
                            HttpStatus.NOT_FOUND);
                }
//                JSONArray array = new JSONArray();

                JSONObject obj = new JSONObject();
                JSONArray arrayM = new JSONArray();
                Long EngagementId = 0l;
                Long idLoanB = 0l;
                Long id_team = 0l;
                Double dmpPortion = 0d;
                Double outStandingLoanB = 0d;
//                String caseId = null;
                Integer number_of_disbursement = 0;
                String disbursement_id = null;
                if (dataDisbursement.getDisbursementId() == null) {
                    obj.put("disbursement_id", "");
                } else {
                    obj.put("disbursement_id", dataDisbursement.getDisbursementId());
                    closedate = dateFormat.format(dataDisbursement.getCutOffDate());
                    closeDate = dateFormat.parse(closedate);
                    oldclosedate = dateFormat.format(dataDisbursement.getOldCutOffDate());
                    oldCloseDate = dateFormat.parse(oldclosedate);
                    disbursement_id = dataDisbursement.getDisbursementId();
                }
                if (dataDisbursement.getNumberOfDisbursement() == null) {
                    obj.put("number_of_disbursement", "");
                } else {
                    obj.put("number_of_disbursement", dataDisbursement.getNumberOfDisbursement());
                    number_of_disbursement = dataDisbursement.getNumberOfDisbursement();
                }
                if (dataDisbursement.getTahunInput() == null) {
                    obj.put("tax_year", "");
                } else {
                    obj.put("tax_year", dataDisbursement.getTahunInput());
                    tax_year = dataDisbursement.getTahunInput();
                }
                if (dataDisbursement.getDisburse_date() == null) {
                    obj.put("disbursement_date", "");
                } else {
                    obj.put("disbursement_date", sdfDisburse.format(dataDisbursement.getDisburse_date()));
                }
                if (dataDisbursement.getEngagement() == null) {
                    obj.put("case_id", "");
                } else {
                    obj.put("case_id", caseId);// disbursements.getLoan().getEngagement().getCaseID()
                    EngagementId = dataDisbursement.getEngagement().getEngagementId();
//                    caseId = object.getCase_id();
                }
                CaseDetails caseDetails = caseDetailsService.findById(EngagementId);
                if (caseDetails != null) {
//                        obj.put("professionalFee", caseDetails.getProfesionalFee());
                    obj.put("dmp_portion_case_id", String.format("%.0f", (caseDetails.getDmpPortion())));
                    dmpPortion = caseDetails.getDmpPortion();
                } else {
//                        obj.put("professionalFee", "");
                    obj.put("dmp_portion_case_id", "");
                    dmpPortion = 0d;
                }
                Double outStandingB = 0.0;
//                OutStandingLoanB outStandingB = this.outStandingLoanBService.findByCaseId(caseId);
                outStandingB = this.outStandingLoanBService.sumLoanByCaseId(caseId);

                outStandingLoanB = outStandingB;
                obj.put("out_standing_loan_b", String.format("%.0f", outStandingLoanB));

                List<TeamMember> entityTeam = teamMemberService.listTeamMemberByEngagement(EngagementId);
                if (entityTeam != null) {
                    for (int j = 0; j < entityTeam.size(); j++) {
//                                JSONObject objTeam = new JSONObject();
                        TeamMember dataTeam = entityTeam.get(j);
                        Employee getDmp = employeeService.findById(dataTeam.getDmpId());
                        if (getDmp == null) {
                            obj.put("employee_id_dmp", "");
                            obj.put("fee_share_dmp", "");

                            obj.put("amount_portion_dmp", "");
                            obj.put("previous_disbursement_dmp", "");

                            obj.put("anual_salary_dmp", "");
                            obj.put("total_income_fortax_purpose_dmp", "");
                            obj.put("masa_kerja_dmp", "");
                            obj.put("jabatan_per_bulan_dmp", "");
                            obj.put("jabatan_per_tahun_dmp", "");
                            obj.put("tax_status_dmp", "");
                            obj.put("ptkp_dmp", "");
                            obj.put("taxable_income_dmp", "");
                            obj.put("income_tax_dmp", "");
                            obj.put("income_tax_paid_on_prior_period_dmp", "");
                            obj.put("net_income_tax_deducted_dmp", "");
                            obj.put("net_income_dmp", "");
                            obj.put("outstanding_loan_b_dmp", "");
                            obj.put("disbursable_amount_dmp", "");
                            obj.put("outstanding_loan_a_dmp", "");
                            obj.put("disbursed_amount_dmp", "");
                        } else {
                            if (dataTeam.getTeamMemberId() != null) {
                                id_team = dataTeam.getTeamMemberId();
                            }
                            Integer anual_salary_dmp = (4000000 * 13);
                            int a_jabatan_dmp = (60000000 * 5) / 100;
                            int b_jabatan_dmp = ((anual_salary_dmp * 5) / 100) / 13;
                            int masa_kerja_dmp = 0;
                            Double amount_portion_dmp = 0d;

                            Double previous_disbursement_dmp = 0d;
                            Double total_income_fortax_purpose_dmp = 0d;
                            String dmp_tax_status = getDmp.getTaxStatus();
                            Double income_tax_dmp = 0d;
                            Double taxable_income_dmp = 0d;
                            Double income_tax_paid_on_prior_period_dmp = 0d;
                            Double net_income_tax_deducted_dmp = 0d;
                            Double net_income_dmp = 0d;
                            Double outstanding_loan_b_dmp = 0d;
                            Double disbursable_amount_dmp = 0d;
                            Double outstanding_loan_a_dmp = 0d;
                            Double disbursed_amount_dmp = 0d;
                            Double ptkp = 0d;
                            log.info("dmp_tax_status : " + dmp_tax_status);
//                                    String jabatan_perbulan_dmp = String.format("%d", (((4000000 * 13) * 5) / 100) / 13);
                            EntityPTKP ptkpDmp = this.ptkpService.findPTKPByTaxStatus(dmp_tax_status);
                            log.info("ptkpDmp : " + ptkpDmp);
                            if (ptkpDmp != null) {
                                ptkp = ptkpDmp.getPtkp();
                            }
                            obj.put("employee_id_dmp", getDmp.getEmployeeId());
                            obj.put("fee_share_dmp", dataTeam.getFeeShare());
                            amount_portion_dmp = (dmpPortion * dataTeam.getFeeShare()) / 100;
                            obj.put("amount_portion_dmp", String.format("%.0f", amount_portion_dmp));
                            if (number_of_disbursement == 1) {
                                previous_disbursement_dmp = 0d;
                            }
                            if (number_of_disbursement == 2) {
                                previous_disbursement_dmp = entityPeriodService.previousDisbursement(2, getDmp.getIdEmployee(), tax_year);
                                if (previous_disbursement_dmp == null || previous_disbursement_dmp == 0d) {
                                    previous_disbursement_dmp = 0d;
                                }
                            }
                            if (number_of_disbursement == 3) {
                                previous_disbursement_dmp = entityPeriodService.previousDisbursement(3, getDmp.getIdEmployee(), tax_year);
                                if (previous_disbursement_dmp == null || previous_disbursement_dmp == 0d) {
                                    previous_disbursement_dmp = 0d;
                                } else if (previous_disbursement_dmp.equals(amount_portion_dmp)) {
                                    previous_disbursement_dmp = 0d;
                                }
                            }
                            obj.put("previous_disbursement_dmp", previous_disbursement_dmp);

                            obj.put("anual_salary_dmp", String.format("%d", anual_salary_dmp));
                            total_income_fortax_purpose_dmp = amount_portion_dmp + previous_disbursement_dmp + anual_salary_dmp;
                            obj.put("total_income_fortax_purpose_dmp", String.format("%.0f", total_income_fortax_purpose_dmp));
                            masa_kerja_dmp = Util.hitungMasakerja(getDmp.getDateRegister());
                            obj.put("masa_kerja_dmp", masa_kerja_dmp);
                            String jabatan_per_bulan_dmp = String.format("%d", Math.min(a_jabatan_dmp, b_jabatan_dmp));
                            obj.put("jabatan_per_bulan_dmp", jabatan_per_bulan_dmp);
                            Integer jabatan_per_tahun_dmp = (masa_kerja_dmp * Integer.parseInt(jabatan_per_bulan_dmp));
                            obj.put("jabatan_per_tahun_dmp", String.format("%d", jabatan_per_tahun_dmp));
                            obj.put("tax_status_dmp", dmp_tax_status);
                            obj.put("ptkp_dmp", String.format("%.0f", ptkp));
                            if (total_income_fortax_purpose_dmp == 0) {
                                taxable_income_dmp = (jabatan_per_tahun_dmp - ptkp);
                            } else {
                                taxable_income_dmp = (total_income_fortax_purpose_dmp - jabatan_per_tahun_dmp - ptkp);
                            }

                            obj.put("taxable_income_dmp", String.format("%.0f", Math.max(taxable_income_dmp, 0)));
                            income_tax_dmp = Util.hitungPajak(taxable_income_dmp);
                            obj.put("income_tax_dmp", String.format("%.0f", income_tax_dmp));

                            if (number_of_disbursement == 1) {

                                income_tax_paid_on_prior_period_dmp = 0d;
                                EntityPeriod entityPeriod = entityPeriodService.findBy(getDmp.getIdEmployee(), caseId, tax_year);
                                if (entityPeriod == null) {
                                    Double income_tax = Util.hitungPajak(taxable_income_dmp);
                                    entityPeriod.setIncomeTaxPaidOnPriorPeriod(income_tax);
                                    this.entityPeriodService.update(entityPeriod);
                                }
                            }
                            if (number_of_disbursement == 2) {

                                EntityPeriod entityPeriod = entityPeriodService.findBy(getDmp.getIdEmployee(), caseId, tax_year);
                                if (entityPeriod == null) {
                                    Double income_tax = Util.hitungPajak(taxable_income_dmp);
                                    entityPeriod.setIncomeTaxPaidOnPriorPeriod(income_tax);
                                    this.entityPeriodService.update(entityPeriod);
                                }
                                Double incomeTaxPaidOnPriorPeriodOld = entityPeriodService.incomeTaxPaidOnPriorPeriod(1, getDmp.getIdEmployee(), tax_year);
                                if (incomeTaxPaidOnPriorPeriodOld != null) {
                                    income_tax_paid_on_prior_period_dmp = incomeTaxPaidOnPriorPeriodOld;
                                } else {
                                    income_tax_paid_on_prior_period_dmp = 0d;
                                }
                            }
                            if (number_of_disbursement == 3) {
//                                EntityPeriod entityPeriod = entityPeriodService.findBy(getDmp.getIdEmployee(), caseId, tax_year);
//                                if (entityPeriod != null) {
//                                    Double income_tax = Util.hitungPajak(taxable_income_dmp);
//                                    entityPeriod.setIncome_tax_paid_on_prior_period(income_tax);
//                                    this.entityPeriodService.update(entityPeriod);
//                                }
                                Double incomeTaxPaidOnPriorPeriodOld = entityPeriodService.incomeTaxPaidOnPriorPeriod(3, getDmp.getIdEmployee(), tax_year);
                                if (incomeTaxPaidOnPriorPeriodOld != null) {
                                    income_tax_paid_on_prior_period_dmp = incomeTaxPaidOnPriorPeriodOld;
                                } else {
                                    income_tax_paid_on_prior_period_dmp = 0d;
                                }
                            }
                            obj.put("income_tax_paid_on_prior_period_dmp", String.format("%.0f", Math.max(income_tax_paid_on_prior_period_dmp, 0)));
                            net_income_tax_deducted_dmp = (income_tax_dmp - income_tax_paid_on_prior_period_dmp);
                            obj.put("net_income_tax_deducted_dmp", String.format("%.0f", net_income_tax_deducted_dmp));
                            net_income_dmp = Math.max((amount_portion_dmp - net_income_tax_deducted_dmp), 0);
                            obj.put("net_income_dmp", String.format("%.0f", net_income_dmp));
                            outstanding_loan_b_dmp = (outStandingLoanB * dataTeam.getFeeShare()) / 100;;
                            obj.put("outstanding_loan_b_dmp", String.format("%.0f", outstanding_loan_b_dmp));
                            disbursable_amount_dmp = (net_income_dmp - outstanding_loan_b_dmp);
                            obj.put("disbursable_amount_dmp", String.format("%.0f", Math.abs(disbursable_amount_dmp)));
                            if (number_of_disbursement == 1) {
                                Double outstanding_after_disbursement_amount = 0d;
                                Double outStandingAteam = this.loanService.sumLoanA(getDmp.getIdEmployee(), tax_year, closeDate);
                                outstanding_loan_a_dmp = outStandingAteam;
                                OutStandingLoanA updateOutStandingLoanA = this.outStandingLoanAService.findBy(getDmp.getIdEmployee(), tax_year, number_of_disbursement.longValue());
                                updateOutStandingLoanA.setDisburseableAmount(disbursable_amount_dmp);
                                outstanding_after_disbursement_amount = Math.max((updateOutStandingLoanA.getLoanAmount() - disbursable_amount_dmp), 0);
                                updateOutStandingLoanA.setOutstandingADisbursement(outstanding_after_disbursement_amount);
                                this.outStandingLoanAService.update(updateOutStandingLoanA);
                            }
                            if (number_of_disbursement == 2) {
                                Double outstanding_after_disbursement_amount = 0d;
                                Double outStandingAteam = null;
                                if (oldCloseDate.compareTo(closeDate) == 0) {
                                    outStandingAteam = this.loanService.sumLoanA(getDmp.getIdEmployee(), tax_year, closeDate);
                                } else {
                                    outStandingAteam = this.loanService.sumLoanA2(getDmp.getIdEmployee(), tax_year, closeDate, oldCloseDate);
                                }

                                outstanding_loan_a_dmp = outStandingAteam;
                                OutStandingLoanA updateOutStandingLoanA = this.outStandingLoanAService.findBy(getDmp.getIdEmployee(), tax_year, number_of_disbursement.longValue());
                                if (updateOutStandingLoanA != null) {
                                    updateOutStandingLoanA.setDisburseableAmount(disbursable_amount_dmp);
                                    outstanding_after_disbursement_amount = Math.max((updateOutStandingLoanA.getLoanAmount() - disbursable_amount_dmp), 0);
                                    updateOutStandingLoanA.setOutstandingADisbursement(outstanding_after_disbursement_amount);
                                    this.outStandingLoanAService.update(updateOutStandingLoanA);
                                }

                            }
                            if (number_of_disbursement == 3) {
                                Double outstanding_after_disbursement_amount = 0d;
                                Double outStandingAteam = null;
                                if (oldCloseDate.compareTo(closeDate) == 0) {
                                    outStandingAteam = this.loanService.sumLoanA(getDmp.getIdEmployee(), tax_year, closeDate);
                                } else {
                                    outStandingAteam = this.loanService.sumLoanA2(getDmp.getIdEmployee(), tax_year, closeDate, oldCloseDate);
                                }
                                outstanding_loan_a_dmp = outStandingAteam;
                                OutStandingLoanA updateOutStandingLoanA = this.outStandingLoanAService.findBy(getDmp.getIdEmployee(), tax_year, number_of_disbursement.longValue());
                                if (updateOutStandingLoanA != null) {
                                    updateOutStandingLoanA.setDisburseableAmount(disbursable_amount_dmp);
                                    outstanding_after_disbursement_amount = Math.max((updateOutStandingLoanA.getLoanAmount() - disbursable_amount_dmp), 0);
                                    updateOutStandingLoanA.setOutstandingADisbursement(outstanding_after_disbursement_amount);
                                    this.outStandingLoanAService.update(updateOutStandingLoanA);
                                }
                            }

                            obj.put("outstanding_loan_a_dmp", String.format("%.0f", outstanding_loan_a_dmp));
                            disbursed_amount_dmp = (disbursable_amount_dmp - outstanding_loan_a_dmp);
                            obj.put("disbursed_amount_dmp", String.format("%.0f", Math.max(disbursed_amount_dmp, 0)));

                        }
                    }
                    if (id_team != null) {
                        List<Member> entityMember = memberService.findByIdTeam(id_team);
                        log.info("entityMember size : " + entityMember.size());
                        for (int k = 0; k < entityMember.size(); k++) {
                            JSONObject objMember = new JSONObject();
                            Member dataMember = entityMember.get(k);
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
                                    previous_disbursement_team = entityPeriodService.previousDisbursement(2, dataMember.getEmployee().getIdEmployee(), tax_year);
                                    if (previous_disbursement_team == null || previous_disbursement_team == 0d) {
                                        previous_disbursement_team = 0d;
                                    }
                                    income_tax_paid_on_prior_period_team = income_tax_paid_on_prior_period_team + 0d;

                                }
                                if (number_of_disbursement == 3) {
                                    previous_disbursement_team = entityPeriodService.previousDisbursement(3, dataMember.getEmployee().getIdEmployee(), tax_year);
                                    if (previous_disbursement_team == null || previous_disbursement_team == 0d) {
                                        previous_disbursement_team = 0d;
                                    } else if (previous_disbursement_team.equals(amount_portion_team)) {
                                        previous_disbursement_team = 0d;
                                    }
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
                                if (number_of_disbursement == 1) {
                                    taxable_income_team = (0 - jabatan_per_tahun_team - ptkp);
//                                    taxable_income_team_val = Math.abs(taxable_income_team);
                                }
                                if (number_of_disbursement == 2) {
                                    taxable_income_team = (total_income_fortax_purpose_team - jabatan_per_tahun_team - ptkp);
//                                    taxable_income_team_val = Math.abs(taxable_income_team);
                                }
                                if (number_of_disbursement == 3) {
                                    taxable_income_team = (total_income_fortax_purpose_team - jabatan_per_tahun_team - ptkp);
//                                    taxable_income_team_val = Math.abs(taxable_income_team);
                                }

                                objMember.put("taxable_income_team", String.format("%.0f", Math.max(taxable_income_team, 0)));
                                income_tax_team = Util.hitungPajak(taxable_income_team);
                                objMember.put("income_tax_team", String.format("%.0f", income_tax_team));
                                if (number_of_disbursement == 1) {

                                    income_tax_paid_on_prior_period_team = 0d;
                                    EntityPeriod entityPeriod = entityPeriodService.findBy(dataMember.getEmployee().getIdEmployee(), caseId, tax_year);
                                    if (entityPeriod != null) {
//                                        Double income_tax = Util.hitungPajak(income_tax_team);
                                        entityPeriod.setIncomeTaxPaidOnPriorPeriod(income_tax_team);
                                        this.entityPeriodService.update(entityPeriod);
                                    }
                                }
                                if (number_of_disbursement == 2) {

                                    EntityPeriod entityPeriod = entityPeriodService.findBy(dataMember.getEmployee().getIdEmployee(), caseId, tax_year);
                                    if (entityPeriod != null) {
//                                        Double income_tax = Util.hitungPajak(income_tax_team);
                                        entityPeriod.setIncomeTaxPaidOnPriorPeriod(income_tax_team);
                                        this.entityPeriodService.update(entityPeriod);
                                    }
                                    Double incomeTaxPaidOnPriorPeriod = entityPeriodService.incomeTaxPaidOnPriorPeriod(1, dataMember.getEmployee().getIdEmployee(), tax_year);
                                    if (incomeTaxPaidOnPriorPeriod != null || incomeTaxPaidOnPriorPeriod == 0d) {
                                        income_tax_paid_on_prior_period_team = incomeTaxPaidOnPriorPeriod;
                                    } else {
                                        income_tax_paid_on_prior_period_team = 0d;
                                    }

                                }
                                if (number_of_disbursement != 3) {

                                    Double incomeTaxPaidOnPriorPeriod = entityPeriodService.incomeTaxPaidOnPriorPeriod(3, dataMember.getEmployee().getIdEmployee(), tax_year);
                                    if (incomeTaxPaidOnPriorPeriod != null || incomeTaxPaidOnPriorPeriod == 0d) {
                                        income_tax_paid_on_prior_period_team = incomeTaxPaidOnPriorPeriod;
                                    } else {
                                        income_tax_paid_on_prior_period_team = 0d;
                                    }
                                }
                                objMember.put("income_tax_paid_on_prior_period_team", String.format("%.0f", Math.max(income_tax_paid_on_prior_period_team, 0)));
                                net_income_tax_deducted_team = income_tax_team + income_tax_paid_on_prior_period_team;
                                objMember.put("net_income_tax_deducted_team", String.format("%.0f", net_income_tax_deducted_team));
                                net_income_team = Math.abs(amount_portion_team - net_income_tax_deducted_team);
                                objMember.put("net_income_team", String.format("%.0f", net_income_team));
                                outstanding_loan_b_team = (outStandingLoanB * dataMember.getFeeShare()) / 100;;
                                objMember.put("outstanding_loan_b_team", String.format("%.0f", outstanding_loan_b_team));
                                disbursable_amount_team = (net_income_team - outstanding_loan_b_team);
                                objMember.put("disbursable_amount_team", String.format("%.0f", Math.abs(disbursable_amount_team)));
                                if (number_of_disbursement == 1) {
                                    Double outstanding_after_disbursement_amount = 0d;
                                    Double outStandingAteam = this.loanService.sumLoanA(dataMember.getEmployee().getIdEmployee(), tax_year, closeDate);
                                    outstanding_loan_a_team = outStandingAteam;
                                    OutStandingLoanA updateOutStandingLoanA = this.outStandingLoanAService.findBy(dataMember.getEmployee().getIdEmployee(), tax_year, number_of_disbursement.longValue());
                                    updateOutStandingLoanA.setDisburseableAmount(disbursable_amount_team);
                                    outstanding_after_disbursement_amount = Math.max((updateOutStandingLoanA.getLoanAmount() - disbursable_amount_team), 0);
                                    updateOutStandingLoanA.setOutstandingADisbursement(outstanding_after_disbursement_amount);
                                    this.outStandingLoanAService.update(updateOutStandingLoanA);
                                }
                                if (number_of_disbursement == 2) {
                                    Double outstanding_after_disbursement_amount = 0d;
                                    Double outStandingAteam = null;
                                    if (oldCloseDate.compareTo(closeDate) == 0) {
                                        outStandingAteam = this.loanService.sumLoanA(dataMember.getEmployee().getIdEmployee(), tax_year, closeDate);
                                    } else {
                                        outStandingAteam = this.loanService.sumLoanA2(dataMember.getEmployee().getIdEmployee(), tax_year, closeDate, oldCloseDate);
                                    }

                                    outstanding_loan_a_team = outStandingAteam;
                                    OutStandingLoanA updateOutStandingLoanA = this.outStandingLoanAService.findBy(dataMember.getEmployee().getIdEmployee(), tax_year, number_of_disbursement.longValue());
                                    if (updateOutStandingLoanA != null) {
                                        updateOutStandingLoanA.setDisburseableAmount(disbursable_amount_team);
                                        outstanding_after_disbursement_amount = Math.max((updateOutStandingLoanA.getLoanAmount() - disbursable_amount_team), 0);
                                        updateOutStandingLoanA.setOutstandingADisbursement(outstanding_after_disbursement_amount);
                                        this.outStandingLoanAService.update(updateOutStandingLoanA);
                                    }
                                }
                                if (number_of_disbursement == 3) {
                                    Double outstanding_after_disbursement_amount = 0d;
                                    Double outStandingAteam = null;
                                    if (oldCloseDate.compareTo(closeDate) == 0) {
                                        outStandingAteam = this.loanService.sumLoanA(dataMember.getEmployee().getIdEmployee(), tax_year, closeDate);
                                    } else {
                                        outStandingAteam = this.loanService.sumLoanA2(dataMember.getEmployee().getIdEmployee(), tax_year, closeDate, oldCloseDate);
                                    }
                                    outstanding_loan_a_team = outStandingAteam;
                                    OutStandingLoanA updateOutStandingLoanA = this.outStandingLoanAService.findBy(dataMember.getEmployee().getIdEmployee(), tax_year, number_of_disbursement.longValue());
                                    if (updateOutStandingLoanA != null) {
                                        updateOutStandingLoanA.setDisburseableAmount(disbursable_amount_team);
                                        outstanding_after_disbursement_amount = Math.max((updateOutStandingLoanA.getLoanAmount() - disbursable_amount_team), 0);
                                        updateOutStandingLoanA.setOutstandingADisbursement(outstanding_after_disbursement_amount);
                                        this.outStandingLoanAService.update(updateOutStandingLoanA);
                                    }

                                }

                                objMember.put("outstanding_loan_a_team", String.format("%.0f", outstanding_loan_a_team));
                                disbursed_amount_team = (disbursable_amount_team - outstanding_loan_a_team);
                                objMember.put("disbursed_amount_team", String.format("%.0f", Math.max(disbursed_amount_team, 0)));

                            }
                            arrayM.put(objMember);

                        }
                    } else {
//                                objMember.put("member_name", "");
//                                objMember.put("employee_id", "");
//                                objMember.put("fee_share", "");
//                                arrayM.put(objMember);
                    }
                    obj.put("members", arrayM);
//                    array.put(obj);
//                    log.info("disbursement : " + obj.toString());
                    rs.setResponse_code("00");
                    rs.setInfo("Success");
                    rs.setResponse("closing case engagement_id " + caseDetails.getCaseID() + "by : " + entityEmp.getEmployeeId());
                    CreateLog.createJson(rs, "closing-Case");
                    log.info("closing-Case : " + rs.toString());
                    return new ResponseEntity(new CustomErrorType("00", "Success", "closing case id " + caseDetails.getCaseID() + "by : " + entityEmp.getEmployeeId()),
                            HttpStatus.ACCEPTED);
                }
            }
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "disbursementbyCaseId");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (ParseException ex) {
            log.error("disbursement : " + ex.getMessage());
            Logger.getLogger(CaseController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
//
        return null;
    }

    @RequestMapping(value = "/case/case-id", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> getCaseId(Authentication authentication) {
        try {
            rs.setResponse_code("00");
            rs.setInfo("getCaseId acces By : " + authentication.getName());
            rs.setResponse("");
            CreateLog.createJson(rs, "getCaseId");
            log.info("getCaseId : " + rs.toString());
            String name = authentication.getName();
            log.info("name : " + name);
            Employee entityEmp = employeeService.findByEmployee(name);
            log.info("entity : " + entityEmp);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("can't acces this feature :");
                CreateLog.createJson(rs, "getCaseId");
//                process = false;
                log.error("getCaseId : " + rs.toString());
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            List<CaseDetails> listCaseId = this.caseDetailsService.getCaseId();
            JSONArray array = new JSONArray();
            for (int i = 0; i < listCaseId.size(); i++) {
                CaseDetails data = listCaseId.get(i);
                JSONObject obj = new JSONObject();
                if (data.getCaseID() == null) {
                    obj.put("cae_id", "");
                } else {
                    obj.put("cae_id", data.getCaseID());
                }
                array.put(obj);
            }
            return ResponseEntity.ok(array.toString());
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            CreateLog.createJson(ex.getMessage(), "find-by-employee-id");
            log.error("getCaseId : " + ex.getMessage());
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);

        }

    }
}
