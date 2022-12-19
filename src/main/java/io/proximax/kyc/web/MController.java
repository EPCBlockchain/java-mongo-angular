package io.proximax.kyc.web.rest;

// import io.proximax.kyc.domain.AG; // Model
// import io.proximax.kyc.repository.AGRepository; // Repository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.List;
import org.bson.types.ObjectId;
import java.io.*; 


// import java.io.File;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


import java.net.InetAddress;
import java.util.Properties;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.mail.*;
import javax.mail.internet.*;
import com.sun.mail.smtp.*;


@RestController
@RequestMapping("/api")

public class MController {

  private static final Logger log = LoggerFactory.getLogger(MController.class);

    @Autowired


    @RequestMapping(value = "/M/{id1}/{id2}/{id3}", method = RequestMethod.GET)
    public static JsonNode sendMail(@PathVariable("id1") String id1, @PathVariable("id2") String id2, @PathVariable("id3") String id3) throws UnirestException {
        
        Date date = new Date();
      
        String strMail = "<h3>KYC Forms</h3><br><b>E: </b>victor.suarez@proximax.io><br><b>W: </b>www.proximax.ltd<br><br>";
        strMail = strMail + "<img src='https://www.proximax.io/user/themes/proximaxvrs1/images/logo.png' style='height: 50px;' ><br><b>Blockchain Reimagined and Evolved</b><br><br>";
      
      
        // type 1
        String strSubjet = ""; 
        if ( id3.equals("1") ) {
            strSubjet = id2;
            strMail = strMail + "<h3>" + id2 + "</h3>";
            strMail = strMail + "<b>Status: </b>" + id2 + "<br><br>";
            strMail = strMail + "<b>Remarks: </b>" + id1 + "<br><br>";
        }


        strMail = strMail + date;       
        
        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + "cf.proximax.ltd" + "/messages")
            .basicAuth("api", "d9139b610a40341a394713f0e491e45d-de7062c6-995d9de5")
            .field("from", "KYC Forms <victor.suarez@proximax.io>")
            .field("to", "victormst@gmail.com")
            .field("subject", strSubjet)
            .field("html", strMail)
            .asJson();

        return request.getBody();
    }
}