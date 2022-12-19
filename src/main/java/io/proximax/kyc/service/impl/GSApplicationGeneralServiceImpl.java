package io.proximax.kyc.service.impl;


// import net.robertocrespo.microservices.users.exception.UserNotFoundException;
// import net.robertocrespo.microservices.users.model.User;
/*
import io.proximax.kyc.domain.globalsettings.GSApplicationGeneral; // model
import io.proximax.kyc.repository.GSApplicationGeneralRepository; // repository
import io.proximax.kyc.service.GSApplicationGeneralService; // Service

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("gsapplicationGeneralService")
@Transactional
public class GSApplicationGeneralServiceImpl implements GSApplicationGeneralService {


	private static final Log log = LogFactory.getLog(GSApplicationGeneralServiceImpl.class);
	
	private GSApplicationGeneralRepository gsagr;

    @Autowired
    public GSApplicationGeneralServiceImpl(GSApplicationGeneralRepository gsagr){
        this.gsagr = gsagr;
    }

    public GSApplicationGeneral findById(String id) {
        Optional<GSApplicationGeneral> ugsagr = gsagr.findOne(id);
        if (ugsagr.isPresent()) {
            log.debug(String.format("Read userId '{}'", id));
            return ugsagr.get();
        } // else
            //throw new UserNotFoundException(userId);
          
    }
   
    public List<GSApplicationGeneral> findAll() {
        Optional<List<GSApplicationGeneral>> gsagru = gsagr.findAll();
        return gsagru.get();      
    }    
   

    public GSApplicationGeneral saveGSAG(GSApplicationGeneral gsagr) {
        return gsagr.saveGSAG(gsagr);
    }
 
    public void updateGSAG(GSApplicationGeneral gsagr) {
        gsagr.updateGSAG(gsagr);
    }

    
    public void deleteGSAG(String id) {
        gsagr.deleteGSAG(id);
    }


}
*/