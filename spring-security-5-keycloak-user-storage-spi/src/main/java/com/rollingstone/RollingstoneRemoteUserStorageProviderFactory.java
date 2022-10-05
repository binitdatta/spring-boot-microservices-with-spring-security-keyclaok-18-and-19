package com.rollingstone;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

public class RollingstoneRemoteUserStorageProviderFactory implements UserStorageProviderFactory<RollingstoneRemoteUserStorageProvider>
{
    public  final String PROVIDER_NAME = "rollingstone-storage-provider";

    @Override public RollingstoneRemoteUserStorageProvider create(KeycloakSession keycloakSession, ComponentModel componentModel)
    {
        return new RollingstoneRemoteUserStorageProvider(keycloakSession,
                componentModel);
    }

    @Override public String getId()
    {
        return PROVIDER_NAME;
    }

}
