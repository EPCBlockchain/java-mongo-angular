package io.proximax.kyc.config;

import io.github.jhipster.config.JHipsterConstants;
import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import io.github.jhipster.domain.util.JSR310DateConverters.DateToZonedDateTimeConverter;
import io.github.jhipster.domain.util.JSR310DateConverters.ZonedDateTimeToDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@EnableMongoRepositories("io.proximax.kyc.repository")
@Profile("!" + JHipsterConstants.SPRING_PROFILE_CLOUD)
@Import(value = MongoAutoConfiguration.class)
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableConfigurationProperties({ApplicationProperties.class})
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(DateToZonedDateTimeConverter.INSTANCE);
        converters.add(ZonedDateTimeToDateConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    @Bean
    public Mongobee mongobee(MongoClient mongoClient, MongoTemplate mongoTemplate, MongoProperties mongoProperties,final Environment environment) {
        log.debug("Configuring Mongobee");
        Mongobee mongobee = new Mongobee(mongoClient);
        mongobee.setDbName(mongoProperties.getMongoClientDatabase());
        mongobee.setSpringEnvironment(environment);
        mongobee.setMongoTemplate(mongoTemplate);
        mongobee.setChangeLogsScanPackage("io.proximax.kyc.config.dbmigrations");
        mongobee.setEnabled(applicationProperties.getMigration().isEnabled());
        return mongobee;
    }
}
