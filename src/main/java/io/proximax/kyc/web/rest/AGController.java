package io.proximax.kyc.web.rest;

import io.proximax.kyc.domain.AG; // Model
import io.proximax.kyc.repository.AGRepository; // Repository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;
import org.bson.types.ObjectId;

import org.json.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.springframework.security.crypto.password.PasswordEncoder;


@RestController
@RequestMapping("/api")

public class AGController {

   private final PasswordEncoder passwordEncoder;

   public AGController(PasswordEncoder passwordEncoder) {
      this.passwordEncoder = passwordEncoder;
   }

   @Autowired
   private AGRepository repository;

   @RequestMapping(value = "/AG/", method = RequestMethod.GET)
   public List getAllAG() {
       return repository.findAll();
   }
  
   @RequestMapping(value = "/AG/pass/{str}", method = RequestMethod.GET)
   public JSONObject generatePassword(@PathVariable("str") String str) {
       JSONObject jsonBody = new JSONObject();
       jsonBody.put("password", passwordEncoder.encode(str));
       return jsonBody;
   }


   @RequestMapping(value = "/AG/{id}", method = RequestMethod.PUT)
   public void modifyAGById(@PathVariable("id") ObjectId id, @Valid @RequestBody AG ag) { 
       ag.set_id(id);
       repository.save(ag);
   }

   @RequestMapping(value = "/AG/", method = RequestMethod.POST)
   public AG createAG(@Valid @RequestBody AG ag) {
       ag.set_id(ObjectId.get());
       repository.save(ag);
       return ag;
   }

    @RequestMapping(value = "/AG/{id}", method = RequestMethod.DELETE)
    public void deleteAG(@PathVariable ObjectId id) {
       repository.delete(repository.findBy_id(id));
    }

}