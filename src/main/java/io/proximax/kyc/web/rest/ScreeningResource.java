package io.proximax.kyc.web.rest;

import io.proximax.kyc.domain.BatchScreeningData;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.constants.ScreeningTypeConstants;
import io.proximax.kyc.repository.ScreeningSubmissionRepository;
import io.proximax.kyc.security.AuthoritiesConstants;
import io.proximax.kyc.service.ScreeningService;
import io.proximax.kyc.service.SubmissionService;
import io.proximax.kyc.service.UserService;
import io.proximax.kyc.service.util.EncryptionUtil;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/screening")
public class ScreeningResource {

    private final Logger log = LoggerFactory.getLogger(SubmissionsResource.class);

    private final SubmissionService submissionService;
    private final ScreeningService screeningService;
    private final UserService userService;
    private final ScreeningSubmissionRepository screeningSubmissionRepository;
    private final EncryptionUtil encryptionUtil;

    public ScreeningResource(SubmissionService submissionService, ScreeningService screeningService, ScreeningSubmissionRepository screeningSubmissionRepository,
                             EncryptionUtil encryptionUtil, UserService userService) {
        this.submissionService = submissionService;
        this.userService = userService;
        this.screeningService = screeningService;
        this.screeningSubmissionRepository = screeningSubmissionRepository;
        this.encryptionUtil = encryptionUtil;
    }


    @PostMapping("/{type}/{referenceId}")
    @Secured({AuthoritiesConstants.OBTEAM, AuthoritiesConstants.ORG_ADMIN})
    public JSONObject createCustomerData(HttpServletRequest request,
                                         @PathVariable(value = "referenceId") String submissionId,
                                         @PathVariable(value = "type") String type) throws URISyntaxException {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        switch (type) {
            case ScreeningTypeConstants.IDM:
                return screeningService.requestIDMScreening(submissionId, user);
            case ScreeningTypeConstants.SHUFTI_PRO:
                return screeningService.initiateShuftiProScreening(submissionId, user);
            case ScreeningTypeConstants.THOMSON_REUTERS:
                return screeningService.requestThomsonReutersScreening(submissionId, user);
            case ScreeningTypeConstants.JUMIO:
                return screeningService.requestJumioScreening(submissionId, user);
        }
        return null;
    }

    @PostMapping("/batch")
    @Secured({AuthoritiesConstants.OBTEAM, AuthoritiesConstants.ORG_ADMIN})
    public void createBatchCustomerData(HttpServletRequest request, @Valid @RequestBody BatchScreeningData batchScreeningData) throws URISyntaxException {
        ArrayList<String> submittedIds = batchScreeningData.getSubmittedIds();
        String screeningType = batchScreeningData.getScreeningType();
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        log.debug("batch_screening screeningType {}", screeningType);
        int numThread = submittedIds.size();
        ExecutorService executor = Executors.newFixedThreadPool(numThread);
        for (String id : submittedIds) {

//            BatchScreening batch_screening = new BatchScreening(id);
//            batch_screening.setPk(pk);
//            batch_screening.setScreeningType(screeningType);
//            batch_screening.setUser(user);
//            executor.execute(batch_screening);

        }
        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {

        }
    }
//    public  class BatchScreening implements Runnable {
//		private String batch_submittedId;
//                private User batch_user;
//                private String batch_screeningType;
//                private String batch_pk;
//
//		BatchScreening(String submittedId) {
//                    this.batch_submittedId = submittedId;
//		}
//                void setUser(User user){
//                    this.batch_user = user;
//                }
//                void setScreeningType(String screeningType){
//                    this.batch_screeningType = screeningType;
//                }
//                void setPk(String pk){
//                    this.batch_pk = pk;
//                }
//		@Override
//		public void run() {
//                    JSONObject screeningData = new JSONObject();
//                    /*start screening*/
//                    switch (batch_screeningType) {
//                        case ScreeningTypeConstants.IDM:
//                            screeningData = screeningService.requestIDMScreening(batch_submittedId, batch_user, batch_pk);
//                            break;
//                        case ScreeningTypeConstants.SHUFTI_PRO:
//                            screeningData = screeningService.initiateShuftiProScreening(batch_submittedId, batch_user, batch_pk);
//                            break;
//                        case ScreeningTypeConstants.THOMSON_REUTERS:
//                            screeningData =  screeningService.requestThomsonReutersScreening(batch_submittedId, batch_user, batch_pk);
//                            break;
//                        case ScreeningTypeConstants.JUMIO:
//                            screeningData =  screeningService.requestJumioScreening(batch_submittedId, batch_user, batch_pk);
//                            break;
//                    }
//
//                    /*Create new form and update?*/
//                    //String screeningResult = screeningData.toJSONString();
//                    submissionService.createScreeningSubmission(batch_submittedId, batch_user, batch_pk);
//
//                    Optional<ScreeningSubmission> dbScreeningSubmission = screeningSubmissionRepository.findBySubmissionReferenceId(batch_submittedId);
//                    if (screeningData !=  null && dbScreeningSubmission.isPresent()) {
//                        ScreeningSubmission screeningSubmission = dbScreeningSubmission.get();
//                        String screeningResultHash = this.encryptionUtility.encryptJSONObject(screeningData);
//                        screeningSubmission.setScreeningDataHash(screeningResultHash);
//                        screeningSubmission.setScreeningDate(Instant.now());
//                        screeningSubmission.setScreeningType(batch_screeningType);
//                        screeningSubmissionRepository.save(screeningSubmission);
//                    }
//
//
//		}
//	}
}
