package io.proximax.kyc.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import io.proximax.kyc.domain.constants.EmailTemplateTypeConstants;
import io.proximax.kyc.domain.globalsettings.EmailTemplate;

import io.proximax.kyc.domain.globalsettings.GSApplicationGeneral; // Model
import io.proximax.kyc.repository.GSApplicationGeneralRepository;

import io.proximax.kyc.domain.globalsettings.SecuritySettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import java.util.Optional;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


/**
 * Service Implementation for managing Organization.
 */
/*
@Repository
public class GSApplicationGeneralImpl implements GSApplicationGeneralRepository{

    private final MongoOperations mongoOperations;

    @Autowired
    public GSApplicationGeneralImpl(MongoOperations mongoOperations) {
        Assert.notNull(mongoOperations);
        this.mongoOperations = mongoOperations;
    }
  
    //Find all GSApplicationGeneral
    public Optional<GSApplicationGeneral> findAll() {
    	List<GSApplicationGeneral> gsag = this.mongoOperations.find(new Query(), GSApplicationGeneral.class);
        // List<GSApplicationGeneral> optionalU = Optional.ofNullable(gsag);
        return gsag;
	}

    public Optional<GSApplicationGeneral> findOne(String id) {
        GSApplicationGeneral d = this.mongoOperations.findOne(new Query(Criteria.where("id").is(id)), GSApplicationGeneral.class);
        Optional<GSApplicationGeneral> gsag = Optional.ofNullable(d);
        return gsag;
    }

    public GSApplicationGeneral saveGSAG(GSApplicationGeneral gsag) {
        this.mongoOperations.save(gsag);
        return findOne(gsag.getId()).get();
    }
    
    public void updateGSAG(GSApplicationGeneral gsag) {
        this.mongoOperations.save(gsag);
    }

    public void deleteGSAG(String id) {
        this.mongoOperations.findAndRemove(new Query(Criteria.where("id").is(id)), GSApplicationGeneral.class);
    }
}
*/