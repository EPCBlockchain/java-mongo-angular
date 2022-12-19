package io.proximax.kyc.domain.screening.keys;

import org.springframework.data.mongodb.core.mapping.Field;

public class ShuftiProKeyPair {
    @Field("client_id")
    private String clientId;

    @Field("secret_key")
    private String secretKey;

    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getClientId() { return this.clientId; }

    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getSecretKey() { return this.secretKey; }
}
