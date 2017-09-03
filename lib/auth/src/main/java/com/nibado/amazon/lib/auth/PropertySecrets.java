package com.nibado.amazon.lib.auth;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import lombok.Getter;

import java.io.IOException;
import java.util.Properties;

@Getter
public class PropertySecrets implements AWSCredentials, AWSCredentialsProvider {
    private String key;
    private String secret;

    public void load() {
        Properties properties = new Properties();

        try {
            properties.load(PropertySecrets.class.getResourceAsStream("/keys.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        key = properties.getProperty("key");
        secret = properties.getProperty("secret");
    }

    @Override
    public String getAWSAccessKeyId() {
        return key;
    }

    @Override
    public String getAWSSecretKey() {
        return secret;
    }

    @Override
    public AWSCredentials getCredentials() {
        load();
        return this;
    }

    @Override
    public void refresh() {
        load();
    }
}
