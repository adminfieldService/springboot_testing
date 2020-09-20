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
@Slf4j
public class CaseController {

    static String basepathUpload = "/opt/lawfirm/UploadFile/";
    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;
    SimpleDateFormat sdfYear;
    SimpleDateFormat sdfMonth;
    SimpleDateFormat sdfMY;
    SimpleDateFormat sdfDisbursM;
    SimpleDateFormat sdfDisbursMY;
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
    @Autowired
    DisbursementServiceIface disbursementService;

    public CaseController() {
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfYear = new SimpleDateFormat("yyyy");
        this.sdfMonth = new SimpleDateFormat("MMMM");
        this.sdfMY = new SimpleDateFormat("MMyyyy");
        this.sdfDisbursM = new SimpleDateFormat("MMM");
        this.sdfDisbursMY = new SimpleDateFormat("MMMyyyy");
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
                return rs;
            }
            if (!entity.getRoleName().contentEquals("dmp")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entity.getRoleName() + " permission deny ");
                process = false;
                CreateLog.createJson(rs, "upload-case-document");
                return rs;
            }

            CaseDetails caseDetails = caseDetailsService.findById(engagement_id);
            if (caseDetails == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Id Not Found ");
                process = false;
                CreateLog.createJson(rs, "upload-case-document");
                return rs;
            }
            if (!caseDetails.getStatus().contentEquals("a")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("Case Status : " + caseDetails.getStatus());
                CreateLog.createJson(rs, "create-event");
                process = false;
                return rs;
            }
            if (caseDetails.getIsActive().contains("4")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + caseDetails.getCaseID() + " Status Closed ");
                CreateLog.createJson(rs, "upload-case-document");
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
                        return rs;
                    } else {
                        rs.setResponse_code("55");
                        rs.setInfo("Error");
                        rs.setResponse("Error Upload Document");
                        CreateLog.createJson(rs, "upload-case-document");
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
                return rs;
            }
            if (!entity.getRoleName().contentEquals("dmp")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entity.getRoleName() + " permission deny ");
                process = false;
                CreateLog.createJson(rs, "uploadMultipleDocument");
                return rs;
            }

            CaseDetails caseDetails = caseDetailsService.findById(engagement_id);
            if (caseDetails == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Id Not Found ");
                process = false;
                CreateLog.createJson(rs, "uploadMultipleDocument");
                return rs;
            }
            if (caseDetails.getStatus().contains("4")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + caseDetails.getCaseID() + " Status Closed ");
                CreateLog.createJson(rs, "uploadMultipleDocument");
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
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/case/{engagement_id}/documents", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> listDocumentByEngId(@PathVariable("engagement_id") Long engagement_id, Authentication authentication) {
        try {
            rs.setResponse_code("00");
            rs.setInfo("listDocumentByEngId Acces by : " + authentication.getName());
            rs.setResponse("engagement_id : " + engagement_id);
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
            CreateLog.createJson(ex.getMessage(), "list-case-document-by-engagement_id");
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
                CreateLog.createJson(rs, "view-Document");
                return new ResponseEntity(new CustomErrorType("55", "Error", "can't acces this feature"),
                        HttpStatus.NOT_FOUND);
            }
            CaseDocument dataDocument = caseDocumentService.findById(case_document_id);
            if (dataDocument == null) {
                rs.setResponse_code("55");
                rs.setInfo("Error");
                rs.setResponse("case_document_id : " + case_document_id + "Not Found");
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
                    return new ResponseEntity(new CustomErrorType("55", "Error", "case_document_id : " + case_document_id + "Not Found"),
                            HttpStatus.NOT_FOUND);
                }

                return ResponseEntity.ok(jsonobj.toString());
            }
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse("case_document_id : " + case_document_id + "Not Found");
            CreateLog.createJson(rs, "view-Document");
            return new ResponseEntity(new CustomErrorType("55", "Error", "case_document_id : " + case_document_id + "Not Found"),
                    HttpStatus.NOT_FOUND);
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "view-Document");
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/case/{engagement_id}/done", method = RequestMethod.POST, produces = {"application/json"})
    @XxsFilter
    public Response closingCase(@PathVariable("engagement_id") Long engagement_id, Authentication authentication) {//@RequestBody final EngagementApi object, 
        try {
            rs.setResponse_code("00");
            rs.setInfo("closingCase access By : " + authentication.getName());
            rs.setResponse("engagement_id : " + engagement_id.toString());
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
            log.info("engagement_id : " + engagement_id);
            if (entityEmp == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't closing case :");
                CreateLog.createJson(rs, "closing-Case");
                return rs;
            }
            if (!entityEmp.getRoleName().contentEquals("admin")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("role : " + entityEmp.getRoleName() + " permission deny ");
                CreateLog.createJson(rs, "closing-Case");
                return rs;
            }
            CaseDetails entity = caseDetailsService.findById(engagement_id);
            if (entity == null) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't closing case engagement_id " + engagement_id + "Not Found");
                CreateLog.createJson(rs, "closing-Case");
                return rs;
            }
            if (entity.getStatus().contains("closed")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + entity.getCaseID() + " Status Closed ");
                CreateLog.createJson(rs, "closing-Case");
                return rs;
            }
            if (entity.getIsActive().contains("4")) {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("case Case Id : " + entity.getCaseID() + " Status Closed ");
                CreateLog.createJson(rs, "closing-Case");
                return rs;
            }

            entity.setIsActive("4");
            entity.setStatus("closed");
            entity.setClosedBy(entityEmp.getEmployeeId());
            entity.setClosed_date(now);
            enHistory.setEngagement(entity);
            enHistory.setUserId(entityEmp.getIdEmployee());
            disbursement.setEngagement(entity);
            List<Disbursement> disburseList = this.disbursementService.numOfDisbursement(sdfYear.format(now));
            if (disburseList == null || disburseList.isEmpty()) {
                number = 1;
            } else {
                number = disburseList.size() + 1;
            }
//            System.out.println("isi : " + object.getDisburse_date());
            String dt = dateFormat.format(new Date());
            Date disburse = dateFormat.parse(dt);
//            dataLoan.setDisburse_date(disburse);
            String disburseM = sdfDisbursM.format(new Date());
            String disburseMy = sdfDisbursMY.format(new Date());
            System.out.println("isi disburseM : " + disburseM);
            disbursement.setBulanInput(disburseM);
            String dsb_id = "DSB" + disburseMy;
            disbursement.setDisbursementId(dsb_id);
            disbursement.setDisburse_date(disburse);
            disbursement.setNumberOfDisbursement(number);
            disbursement.setBulanInput(sdfMonth.format(now));
            disbursement.setTahunInput(sdfYear.format(now));
            enHistory.setResponse("closed By : " + entityEmp.getEmployeeId());
            CaseDetails closeCase = this.caseDetailsService.update(entity);
            if (closeCase != null) {
                this.engagementHistoryService.create(enHistory);
                this.disbursementService.create(disbursement);
                rs.setResponse_code("00");
                rs.setInfo("Success");
                rs.setResponse("closing case engagement_id " + engagement_id + "by : " + entityEmp.getEmployeeId());
                CreateLog.createJson(rs, "closing-Case");
                return rs;
            } else {
                rs.setResponse_code("55");
                rs.setInfo("Failed");
                rs.setResponse("can't closing case engagement_id " + engagement_id + "Not Found");
                CreateLog.createJson(rs, "closing-Case");
                return rs;

            }

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            rs.setResponse_code("55");
            rs.setInfo("Error");
            rs.setResponse(ex.getMessage());
            CreateLog.createJson(ex.getMessage(), "closing-Case");
            return rs;

        } catch (ParseException ex) {
            Logger.getLogger(CaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        rs.setResponse_code("55");
        rs.setInfo("Failed");
        rs.setResponse("can't closing case engagement_id " + engagement_id + "Not Found");
        CreateLog.createJson(rs, "closing-Case");
        return rs;
    }

    @RequestMapping(value = "/case/case-id", method = RequestMethod.GET, produces = {"application/json"})
    @XxsFilter
    public ResponseEntity<?> getCaseId(Authentication authentication) {
        try {
            rs.setResponse_code("00");
            rs.setInfo("getCaseId acces By : " + authentication.getName());
            rs.setResponse("");
            CreateLog.createJson(rs, "getCaseId");
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
            return new ResponseEntity(new CustomErrorType("55", "Error", ex.getMessage()),
                    HttpStatus.NOT_FOUND);

        }

    }
}
