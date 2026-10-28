package com.carbon.quiz.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.carbon.quiz.security.response.GoUserResponse;

@Service
public class TokenVerifier {
    private final RestClient goAuthClient;

    public TokenVerifier(RestClient goAuthClient) {
        this.goAuthClient = goAuthClient;
    }

    public Long verifyAndGetUserId(String token) {
        try {
            GoUserResponse user = goAuthClient.get()
                    .uri("/api/v1/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .body(GoUserResponse.class);

            /* Debug thing, leave it here */
            /* System.out.println(goAuthClient.get()
                    .uri("/api/v1/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve().body(String.class)
                ); */
            
            if (user == null || user.getId() == null || user.getId() <= 0) {
                throw new AuthenticationServiceException(
                        "The Go authentication API returned no valid user id");
            }

            return user.getId();
        } catch (HttpClientErrorException.Unauthorized
                 | HttpClientErrorException.Forbidden exception) {
            throw new BadCredentialsException(
                    "The access token is invalid or expired", exception);
        } catch (RestClientException exception) {
            throw new AuthenticationServiceException(
                    "Could not verify the access token with the Go authentication API",
                    exception);
        }
    }
}