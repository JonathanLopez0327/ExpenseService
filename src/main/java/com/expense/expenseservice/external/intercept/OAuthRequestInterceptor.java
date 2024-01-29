package com.expense.expenseservice.external.intercept;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.util.Objects;

@Configuration
public class OAuthRequestInterceptor implements RequestInterceptor {
    private final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    @Autowired
    public OAuthRequestInterceptor(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
        this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", "Bearer "
                + Objects.requireNonNull(oAuth2AuthorizedClientManager
                        .authorize(OAuth2AuthorizeRequest
                                .withClientRegistrationId("internal-client")
                                .principal("internal")
                                .build()))
                .getAccessToken().getTokenValue());
    }
}
