package io.proximax.kyc.web.rest;

import io.proximax.kyc.domain.SS; // Model
import io.proximax.kyc.repository.SSRepository; // Repository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import javax.validation.Valid;
import java.util.List;
import org.bson.types.ObjectId;

@RestController
@RequestMapping("/api")

public class SSController {
   @Autowired
   private SSRepository repository;

   @RequestMapping(value = "/SS/", method = RequestMethod.GET)
   public List getAllSS() {
       // return repository.findAll(); 
       // return repository.findAll(Sort.by(Sort.Direction.ASC, "last_update"));
       return repository.findAll(new Sort(Sort.Direction.ASC, "last_update"));
   }
  
   @RequestMapping(value = "/SS/{id}", method = RequestMethod.GET)
   public SS getSSById(@PathVariable("id") ObjectId id) {
       return repository.findBy_id(id);
       // return repository.findBy_user_id(user_id);
   }

   @RequestMapping(value = "/SS/{id}", method = RequestMethod.PUT)
   public void modifySSById(@PathVariable("id") ObjectId id, @Valid @RequestBody SS ag) { 
       ag.set_id(id);
       repository.save(ag);
   }

   @RequestMapping(value = "/SS/", method = RequestMethod.POST)
   public SS createSS(@Valid @RequestBody SS ag) {
       ag.set_id(ObjectId.get());
       repository.save(ag);
       return ag;
   }

    @RequestMapping(value = "/SS/{id}", method = RequestMethod.DELETE)
    public void deleteSS(@PathVariable ObjectId id) {
       // repository.delete(repository.findBy_user_id(id));
       repository.delete(repository.findBy_id(id));
    }

}