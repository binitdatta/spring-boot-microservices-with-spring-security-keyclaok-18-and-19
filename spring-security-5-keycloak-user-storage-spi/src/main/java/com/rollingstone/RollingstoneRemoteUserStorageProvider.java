package com.rollingstone;


import com.rollingstone.dto.PasswordResponseDTO;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.UserCredentialStore;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;


import static com.rollingstone.util.RollingStoneRemoteStorageUtil.*;

public class RollingstoneRemoteUserStorageProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator
{
    Logger logger  = LoggerFactory.getLogger("RollingstoneRemoteUserStorageProvider");

    private KeycloakSession keycloakSession;
    private ComponentModel componentModel;

    public RollingstoneRemoteUserStorageProvider(KeycloakSession keycloakSession, ComponentModel componentModel) {
        this.keycloakSession = keycloakSession;
        this.componentModel = componentModel;
    }

    @Override public void close()
    {
    }

    @Override public UserModel getUserById(RealmModel realm, String id)
    {
        return UserLookupProvider.super.getUserById(realm, id);
    }

    @Override public UserModel getUserById(String s, RealmModel realmModel)
    {
        return null;
    }

    @Override public UserModel getUserByUsername(RealmModel realm, String username)
    {
        return UserLookupProvider.super.getUserByUsername(realm, username);
    }

    @Override public UserModel getUserByUsername(String userName, RealmModel realmModel)
    {
        UserModel userModel = null;

        logger.info(" getUserByUsername : " + userName);
         try
        {
           UserDTO userDto = getUser(userName);
           logger.info(userDto.toString());

            if (userDto != null) {
                userModel = createUserModel(userDto, realmModel);
                logUser(userModel);
            }

        }
        catch (IOException ioe) {
            logger.info(ioe.getMessage());
        }

        return userModel;
    }

    private UserModel createUserModel(UserDTO user, RealmModel realm) {
        return new AbstractUserAdapter(keycloakSession, realm, componentModel) {
            @Override
            public String getUsername() {
                logger.info("createUserModel user.getUserName() :" + user.getUserName());
                return user.getUserName();
            }
            @Override
            public String getFirstName() {
                logger.info("createUserModel user.getFirstName() :" + user.getFirstName());
                return user.getFirstName();
            }
            @Override
            public String getLastName() {
                logger.info("createUserModel user.getLastName() :" + user.getLastName());
                return user.getLastName();
            }
            @Override
            public String getEmail() {
                logger.info("createUserModel user.getEmail() :" + user.getEmail());
                return user.getEmail();
            }
        };
    }

    @Override public UserModel getUserByEmail(RealmModel realm, String email)
    {
        return UserLookupProvider.super.getUserByEmail(realm, email);
    }

    @Override public UserModel getUserByEmail(String s, RealmModel realmModel)
    {
        return null;
    }

    @Override public boolean supportsCredentialType(String credentialType)
    {
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        logger.info("credentialType : " + credentialType);
        if (!supportsCredentialType(credentialType)) {
            return false;
        }
        return !getCredentialStore().getStoredCredentialsByType(realm, user, credentialType).isEmpty();
    }

    private UserCredentialStore getCredentialStore() {
        return keycloakSession.userCredentialManager();
    }

    @Override public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput)
    {
        PasswordResponseDTO passwordResponseDTO = getPasswordResponse(userModel, credentialInput.getChallengeResponse());
        return passwordResponseDTO.getResult();
    }
}
