package io.proximax.kyc.service.impl;

import com.google.common.base.Strings;
import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.form.FormGridDTO;
import io.proximax.kyc.domain.form.FormVersion;
import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.repository.FormRepository;
import io.proximax.kyc.repository.OrganizationRepository;
import io.proximax.kyc.repository.UserRepository;
import io.proximax.kyc.service.FormService;
import io.proximax.kyc.service.SubmissionService;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
/**
 * Service Implementation for managing Form.
 */
@Service
public class FormServiceImpl implements FormService {

    private final Logger log = LoggerFactory.getLogger(FormServiceImpl.class);

    private final FormRepository formRepository;
    private final SubmissionService submissionService;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public FormServiceImpl(FormRepository formRepository, MongoTemplate mongoTemplate, OrganizationRepository organizationRepository,
                           UserRepository userRepository, SubmissionService submissionService) {
        this.formRepository = formRepository;
        this.submissionService = submissionService;
        this.mongoTemplate = mongoTemplate;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Form save(Form Form) {
        log.debug("Request to save Form : {}", Form);
        return formRepository.save(Form);
    }

    @Override
    public List<Form> findAll() {
        log.debug("Request to get all Forms");
        return formRepository.findAll();
    }

    @Override
    public Optional<Form> findOne(String id) {
        log.debug("Request to get Form : {}", id);
        Optional<Form> form = formRepository.findById(id);
        log.debug("Request to get Form : {}", form.get().getLastUpdatedBy());
        if (form.get().getLastUpdatedBy() != null && !form.get().getLastUpdatedBy().isEmpty()) {
            Optional<User> user = userRepository.findById(form.get().getLastUpdatedBy());
            form.get().setLastUpdatedBy(user.get().getFullName());
        }
        return form;
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Form : {}", id);
        formRepository.deleteById(id);
    }

    @Override
    public Page<Form> findAllByOrganizationIdAndTags(String orgId, String tag, HashMap<String, String> filters, Pageable pageable) {
        String id = "", title = "";
        String dateCreated = null;
        if (filters != null) {
            if (filters.containsKey("id")) {
                id = filters.get("id");
            }
            if (filters.containsKey("title")) {
                title = filters.get("title");
            }
            if (filters.containsKey("creationDate")) {
                dateCreated = filters.get("creationDate");
            }
        }
        Page<Form> forms;
        List<String> tags = new ArrayList<>();
        tags.add(tag);
        if (!Strings.isNullOrEmpty(id))
            forms = formRepository.findByOrganizationIdAndTagsInAndId(orgId, tags, id, pageable);
        else if (!Strings.isNullOrEmpty(id) && !Strings.isNullOrEmpty(title))
            forms = formRepository.findByOrganizationIdAndTagsInAndIdAndTitleContainingAllIgnoreCase(orgId, tags, id, title, pageable);
        else if (!Strings.isNullOrEmpty(id) && !Strings.isNullOrEmpty(title) && !Strings.isNullOrEmpty(dateCreated))
            forms = formRepository.findByOrganizationIdAndTagsInAndIdAndTitleContainingIgnoreCaseAndCreationDate(orgId, tags, id, title, dateCreated, pageable);
        else if (!Strings.isNullOrEmpty(id) && !Strings.isNullOrEmpty(dateCreated))
            forms = formRepository.findByOrganizationIdAndTagsInAndIdAndCreationDate(orgId, tags, id, dateCreated, pageable);
        else if (!Strings.isNullOrEmpty(dateCreated) && !Strings.isNullOrEmpty(title))
            forms = formRepository.findByOrganizationIdAndTagsInAndTitleContainingIgnoreCaseAndCreationDate(orgId, tags, title, dateCreated, pageable);
        else if (!Strings.isNullOrEmpty(dateCreated))
            forms = formRepository.findByOrganizationIdAndTagsInAndCreationDate(orgId, tags, dateCreated, pageable);
        else if (!Strings.isNullOrEmpty(title))
            forms = formRepository.findByOrganizationIdAndTagsInAndTitleContainingIgnoreCase(orgId, tags, title, pageable);
        else forms = formRepository.findByOrganizationIdAndTagsIn(orgId, tags, pageable);
        return forms;
    }

    @Override
    public Page<Form> findAllbyFilters(String organizationId, HashMap<String, String> filters, Pageable pageable) {
        List<Organization> organizations = this.organizationRepository.findAll();
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        
        if (filters != null) {
            if (filters.containsKey("organizationName")) {
                Optional<Organization> org = organizationRepository.findOneByName(filters.get("organizationName"));
                if (org.isPresent()) {
                    aggregationOperations.add(Aggregation.match(new Criteria("organization_id").is(org.get().getId())));
                } else {
                    return new PageImpl<>(new ArrayList<Form>(), pageable, 0);
                }
            }
            if (filters.containsKey("id")) {
                aggregationOperations.add(Aggregation.match(Criteria.where("_id").is(new ObjectId(filters.get("id")))));
            }
            if (filters.containsKey("title")) {
                aggregationOperations.add(Aggregation.match(new Criteria("title").regex(filters.get("title"), "i")));
            }
            if (filters.containsKey("creationDate")) {
                LocalDate creationDate = LocalDate.parse(filters.get("creationDate"), DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withZone(ZoneId.systemDefault()));
                aggregationOperations.add(Aggregation.match(new Criteria("creation_date").lte(creationDate.atTime(23, 59, 59)).gte(creationDate.atStartOfDay().toInstant(ZoneOffset.UTC))));
            }
            if (filters.containsKey("lastUpdated")) {
                LocalDate lastUpdate = LocalDate.parse(filters.get("lastUpdated"), DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withZone(ZoneId.systemDefault()));
                aggregationOperations.add(Aggregation.match(new Criteria("last_updated").lte(lastUpdate.atTime(23, 59, 59)).gte(lastUpdate.atStartOfDay().toInstant(ZoneOffset.UTC))));
            }
        }

        if (organizationId != null) {
            aggregationOperations.add(Aggregation.match(new Criteria("organization_id").is(organizationId)));
        }

        List<String> tags = new ArrayList<>();
        if (filters != null) {
            tags.add(filters.get("tags"));
        } else {
            tags.add("common");
            tags.add("customer");
            tags.add("screening");
        }
        
        aggregationOperations.add(Aggregation.match(new Criteria("tags").in(tags)));
        aggregationOperations.add(Aggregation.group().count().as("count").push("$$ROOT").as("results"));
        aggregationOperations.add(Aggregation.project("count").and("results").slice(pageable.getPageSize(), pageable.getPageNumber() * pageable.getPageSize()).as("rows"));

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        AggregationResults<FormGridDTO> aggregationResults = mongoTemplate.aggregate(aggregation, "forms", FormGridDTO.class);
        List<FormGridDTO> gridDTO = aggregationResults.getMappedResults();
        if (gridDTO.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        List<Form> rows = gridDTO.get(0).getRows();
        rows.forEach(form -> {
            Organization selectedOrganization = organizations.stream().filter(
                organization -> organization.getId().equals(form.getOrganizationId())).findFirst().get();
            Organization org = new Organization();
            org.setName(selectedOrganization.getName());
            org.setId(selectedOrganization.getId());
            form.setOrganization(org);
        });
        return new PageImpl<>(rows, pageable, gridDTO.get(0).getCount());
    }


    public FormRepository getFormRepository() {
        return formRepository;
    }
    /*
     *  New service implementations
     */

    @Override
    public Form createVersion(Form form, User user) {
        FormVersion version = new FormVersion();
        Long submissionCount = submissionService.countByForm(form);
        if (Strings.isNullOrEmpty(form.getId())) {
            form.setCreationDate(Instant.now());
            form.setOrganizationId(form.getOrganizationId());
            form.setLastUpdatedBy(user.getId());
            form.setLastUpdated(Instant.now());
            form.setVersionDetail(version);
            return formRepository.save(form);
        } else if (submissionCount == 0 || submissionCount > 0) {
            form.setLastUpdatedBy(user.getId());
            form.setLastUpdated(Instant.now());
            return formRepository.save(form);
        }  else {
            if (form.getVersionDetail() == null) {
                form.setVersionDetail(new FormVersion());
            }
            Form newForm = copyForm(form);
            newForm.setId(null);
            newForm.setScreeningFormId(form.getScreeningFormId());
            newForm.setCreationDate(Instant.now());

            version.setVersion(form.getVersionDetail().getVersion() + 1);
            version.setReferenceFormId(form.getId());
            version.setCode(form.getVersionDetail().getCode());
            newForm.setVersionDetail(version);
            newForm.setLastUpdatedBy(user.getId());
            newForm.setLastUpdated(Instant.now());

            formRepository.save(newForm);

            form.getVersionDetail().setVersionCreated(newForm.getCreationDate());
            formRepository.save(form);
            return newForm;
        }
    }

    private Form copyForm(Form form) {
        Form newForm = new Form();
        newForm.setName(form.getName());
        newForm.setPath(form.getPath());
        newForm.setTitle(form.getTitle());
        newForm.setComponents(form.getComponents());
        newForm.setDisplay(form.getDisplay());
        newForm.setType(form.getType());
        newForm.setProject(form.getProject());
        newForm.setTemplate(form.getTemplate());
        newForm.setTags(form.getTags());
        newForm.setAccess(form.getAccess());
        newForm.setSubmissionAccess(form.getSubmissionAccess());
        newForm.setOrganizationId(form.getOrganizationId());
        newForm.setScreeningFormId(form.getScreeningFormId());
        newForm.setCreationDate(form.getCreationDate());
        newForm.setPostBackURL(form.getPostBackURL());
        return newForm;
    }

    @Override
    public Boolean remove(String formId, User user) {
        Optional<Form> form = this.findOne(formId);
        if (form.isPresent()) {
            Long count = submissionService.countByForm(form.get());
            if (count == 0 && form.get().getOrganizationId().equals(user.getOrganizationId())) {
                this.delete(formId);
                return true;
            }
        }
        return false;
    }
}
