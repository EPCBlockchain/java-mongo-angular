package io.proximax.kyc.service;

import io.proximax.kyc.domain.User;
import org.json.simple.JSONObject;

public interface ScreeningService {
    JSONObject requestIDMScreening(String submissionId, User user);
    JSONObject requestThomsonReutersScreening(String submissionId, User user);
    JSONObject requestJumioScreening(String submissionId, User user);
    JSONObject initiateShuftiProScreening(String submissionId, User user);
}
