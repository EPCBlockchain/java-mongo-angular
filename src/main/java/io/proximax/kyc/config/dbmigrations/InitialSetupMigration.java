package io.proximax.kyc.config.dbmigrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import io.proximax.kyc.domain.Authority;
import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.globalsettings.EmailSettings;
import io.proximax.kyc.domain.globalsettings.GlobalSettings;
import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.security.AuthoritiesConstants;
import org.apache.tika.io.IOUtils;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Creates the initial database setup
 */
@ChangeLog(order = "001")
public class InitialSetupMigration {

    @ChangeSet(order = "01", author = "initiator", id = "01-addAuthorities")
    public void addAuthorities(MongoTemplate mongoTemplate) {
        Authority admin = new Authority();
        admin.setName(AuthoritiesConstants.ADMIN);
        admin.setLabel("Administrator");
        Authority user = new Authority();
        user.setName(AuthoritiesConstants.USER);
        user.setLabel("User");
        Authority obteam = new Authority();
        obteam.setName(AuthoritiesConstants.OBTEAM);
        obteam.setLabel("Onboarding Officer");
        Authority orgAdmin = new Authority();
        orgAdmin.setName(AuthoritiesConstants.ORG_ADMIN);
        orgAdmin.setLabel("Organization Admin");

        mongoTemplate.save(admin);
        mongoTemplate.save(user);
        mongoTemplate.save(obteam);
        mongoTemplate.save(orgAdmin);
    }
    @ChangeSet(order = "02", author = "initiator", id = "02-addOrganizations")
    public void addOrganizations(MongoTemplate mongoTemplate, Environment environment) {
        String OrganizationName = environment.getProperty("application.organization.name");
        Organization org = new Organization();
        ObjectId id = new org.bson.types.ObjectId();
        org.setId(id.toString());
        org.setName(OrganizationName);

        /*Set shuftipro screening key*/
        org.setCreationDate(Instant.now());
        String key = environment.getProperty("application.security.key");
        org.getSecurity().setApiKey(UUID.randomUUID().toString());
        org.setStatus("active");
        mongoTemplate.save(org);
    }
    @ChangeSet(order = "03", author = "initiator", id = "03-addUsers")
    public void addUsers(MongoTemplate mongoTemplate) {

        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);
        Authority obteamAuthority = new Authority();
        obteamAuthority.setName(AuthoritiesConstants.OBTEAM);
        Authority obteamAdminAuthority = new Authority();
        obteamAdminAuthority.setName(AuthoritiesConstants.ORG_ADMIN);

        Organization organization = mongoTemplate.findAll(Organization.class, "organizations").get(0);
        String originId = organization.getId();
        User systemUser = new User();
        ObjectId id = new org.bson.types.ObjectId();
        systemUser.setId(id.toString());
        systemUser.setLogin("system");
        systemUser.setPassword("$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG");
        systemUser.setFirstName("");
        systemUser.setLastName("System");
        systemUser.setEmail("system@localhost");
        systemUser.setActivated(true);
        systemUser.setLangKey("en");
        systemUser.setCreatedBy(systemUser.getLogin());
        systemUser.setCreatedDate(Instant.now());
        systemUser.getAuthorities().add(adminAuthority);
        systemUser.setOrganizationId(originId);
        mongoTemplate.save(systemUser);

        User adminUser = new User();
        id = new org.bson.types.ObjectId();
        adminUser.setId(id.toString());
        adminUser.setLogin("admin");
        adminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        adminUser.setFirstName("admin");
        adminUser.setLastName("Administrator");
        adminUser.setEmail("admin@localhost");
        adminUser.setActivated(true);
        adminUser.setLangKey("en");
        adminUser.setCreatedBy(systemUser.getLogin());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.setOrganizationId(originId);
        mongoTemplate.save(adminUser);


        User obTeamUser = new User();
        id = new org.bson.types.ObjectId();
        obTeamUser.setId(id.toString());
        obTeamUser.setLogin("obteam");
        obTeamUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        obTeamUser.setFirstName("obteam");
        obTeamUser.setLastName("OBTeam");
        obTeamUser.setEmail("obteam@localhost");
        obTeamUser.setActivated(true);
        obTeamUser.setLangKey("en");
        obTeamUser.setCreatedBy(systemUser.getLogin());
        obTeamUser.setCreatedDate(Instant.now());
        obTeamUser.getAuthorities().add(obteamAuthority);
        obTeamUser.setOrganizationId(originId);
        mongoTemplate.save(obTeamUser);

        User obTeamAdminUser = new User();
        id = new org.bson.types.ObjectId();
        obTeamAdminUser.setId(id.toString());
        obTeamAdminUser.setLogin("obteamAdmin");
        obTeamAdminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        obTeamAdminUser.setFirstName("obteam");
        obTeamAdminUser.setLastName("admin");
        obTeamAdminUser.setEmail("obteamadmin@localhost");
        obTeamAdminUser.setActivated(true);
        obTeamAdminUser.setLangKey("en");
        obTeamAdminUser.setCreatedBy(systemUser.getLogin());
        obTeamAdminUser.setCreatedDate(Instant.now());
        obTeamAdminUser.getAuthorities().add(obteamAdminAuthority);
        obTeamAdminUser.setOrganizationId(originId);
        mongoTemplate.save(obTeamAdminUser);

    }
    @ChangeSet(order = "04", author = "initiator", id = "04-addForms2")
    public void addForm2(MongoTemplate mongoTemplate) {

        Organization organization = mongoTemplate.findAll(Organization.class, "organizations").get(0);
        String originId = organization.getId();

        Form form = new Form();
        form.setOrganizationId(originId);
        form.setName("screeningAndChecksForm");
        form.setCreationDate(Instant.now());
        form.setTitle("Screening and Checks Form");
        form.setType("form");
        form.setPath("screeningandchecksform");
        List<String> tags = new ArrayList<String>();
        tags.add("common");
        tags.add("screening");
        form.setTags(tags);
        form.setDisplay("form");

        ClassLoader classLoader = getClass().getClassLoader();
        try{
            String test = IOUtils.toString(classLoader.getResourceAsStream("config/forms/screeningandchecksform.json"));
            JSONParser jsonParser = new JSONParser();
            try{
                JSONObject jsonObject = (JSONObject)jsonParser.parse(test);
                form.setComponents((JSONArray)jsonObject.get("components"));
            }catch(ParseException ex){
                System.out.println(ex);
            }


        }catch(IOException ex){
            System.out.println(ex);
        }
        mongoTemplate.save(form);
    }
    @ChangeSet(order = "05", author = "initiator", id = "05-addForms1")
    public void addForm1(MongoTemplate mongoTemplate) {
        Organization organization = mongoTemplate.findAll(Organization.class, "organizations").get(0);
        String screeningFormId = mongoTemplate.findAll(Form.class, "forms").get(0).getId();
        String originId = mongoTemplate.findAll(Organization.class, "organizations").get(0).getId();
        Form form = new Form();
        form.setOrganizationId(originId);
        form.setName("icoRegistrationForm");
        form.setCreationDate(Instant.now());
        form.setTitle("ICO Registration Form");
        form.setType("form");
        form.setDisplay("wizard");
        form.setPath("icoregistrationform");
        form.setScreeningFormId(screeningFormId);
        List<String> tags = new ArrayList<>();
        tags.add("common");
        tags.add("customer");
        form.setTags(tags);

        ClassLoader classLoader = getClass().getClassLoader();
        try{
            String test = IOUtils.toString(classLoader.getResourceAsStream("config/forms/icoregistrationform.json"));
            JSONParser jsonParser = new JSONParser();
            try{
                JSONObject jsonObject = (JSONObject)jsonParser.parse(test);
                form.setComponents((JSONArray)jsonObject.get("components"));
            }catch(ParseException ex){
                System.out.println(ex);
            }


        }catch(IOException ex){
            System.out.println(ex);
        }
        mongoTemplate.save(form);
    }

    @ChangeSet(order = "06", author = "initiator", id = "06-addSettings")
    public void addSettings(MongoTemplate mongoTemplate) {
        GlobalSettings settings = new GlobalSettings();
        settings.setEmailSettings(new EmailSettings());
        mongoTemplate.save(settings);
    }
}
