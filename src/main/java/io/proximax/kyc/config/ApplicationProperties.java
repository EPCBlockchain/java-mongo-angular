package io.proximax.kyc.config;

import io.proximax.kyc.domain.application.*;
import io.proximax.kyc.domain.application.screening.IDMProperties;
import io.proximax.kyc.domain.application.screening.JumioProperties;
import io.proximax.kyc.domain.application.screening.ShuftiProProperties;
import io.proximax.kyc.domain.application.screening.ThomsonReutersProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Proxi KYC.
 * <p>
 * Properties are configured in the application.yml file. See
 * {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private OrganizationProperties organization = new OrganizationProperties();
    private IPFSProperties ipfs = new IPFSProperties();
    private SecurityProperties security = new SecurityProperties();
    private MailgunProperties mailgun = new MailgunProperties();
    private MigrationProperties migration = new MigrationProperties();
    private JumioProperties jumio = new JumioProperties();
    private ShuftiProProperties shuftiPro = new ShuftiProProperties();
    private ThomsonReutersProperties thomson = new ThomsonReutersProperties();
    private IDMProperties idm = new IDMProperties();
    private FormProperties form = new FormProperties();
    private AmazonProperties amazonProperties = new AmazonProperties();
    private String storageselection;
    private String basehost;

    public String getStorageSelection() {
        return this.storageselection;
    }
    public void setStorageSelection(String value) { this.storageselection = value; }

    public String getBaseHost() { return this.basehost; }
    public void setBaseHost(String value) { this.basehost = value; }

    public AmazonProperties getAmazonProperties() {
        return this.amazonProperties;
    }

    public IPFSProperties getIPFS() {
        return this.ipfs;
    }

    public OrganizationProperties getOrganization() {
        return this.organization;
    }

    public SecurityProperties getSecurity() {
        return this.security;
    }

    public MailgunProperties getMailgun() {
        return this.mailgun;
    }

    public MigrationProperties getMigration() {
        return this.migration;
    }

    public JumioProperties getJumio() {
        return this.jumio;
    }

    public ShuftiProProperties getShuftiPro() {
        return this.shuftiPro;
    }

    public ThomsonReutersProperties getThomson() {
        return this.thomson;
    }

    public IDMProperties getIDM() {
        return this.idm;
    }

    public FormProperties getForm() {
        return this.form;
    }
}
