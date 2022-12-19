package io.proximax.kyc.repository;
       
import io.proximax.kyc.domain.SS; // Model
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.Instant;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public interface SSRepository extends MongoRepository < SS, String > {
 // SS findBy_user_id(String user_id);
 SS findBy_id(ObjectId _id);
}