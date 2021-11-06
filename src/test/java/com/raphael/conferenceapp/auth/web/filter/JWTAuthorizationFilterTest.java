package com.raphael.conferenceapp.auth.web.filter;

import com.raphael.conferenceapp.auth.exception.InvalidTokenException;
import com.raphael.conferenceapp.auth.web.TokenExtractor;
import com.raphael.conferenceapp.auth.mock.AuthMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTAuthorizationFilterTest {
    @Mock
    private TokenExtractor extractor;

    @InjectMocks
    private JWTAuthorizationFilter filter;

    @Nested
    @DisplayName("method: shouldNotFilter(HttpServletRequest)")
    class ShouldNotFilterMethod {

        @Mock
        private HttpServletRequest request;

        @ParameterizedTest
        @CsvSource(value = {
                "/auth/login,true",
                "/auth/register,true",
                "/admin/events,false"
        })
        @DisplayName("when request uri starts with /auth, should return true, otherwise false")
        void whenRequestUriStartsWithAuth_shouldReturnTrue_otherwiseFalse(String uri, boolean expected) {
            when(request.getRequestURI()).thenReturn(uri);

            assertThat(filter.shouldNotFilter(request)).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method: doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)")
    class DoFilterInternalMethod {
        @Mock
        private HttpServletRequest request;

        @Mock
        private HttpServletResponse response;

        @Mock
        private FilterChain chain;

        @Test
        @DisplayName("when Authorization header is not present, then it should throw an InvalidTokenException")
        void whenAuthorizationHeaderIsNotPresent_shouldThrowAnInvalidTokenException() {
            when(request.getHeader("Authorization")).thenReturn(null);

            assertThatThrownBy(() -> filter.doFilterInternal(request, response, chain))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("The 'Authorization' header must be in the following format: 'Bearer token'");

        }

        @Test
        @DisplayName("when Authorization header is not properly formatted, then it should throw an InvalidTokenException")
        void whenAuthorizationHeaderIsNotProperlyFormatted_shouldThrowAnInvalidTokenException() {
            when(request.getHeader("Authorization")).thenReturn("some-token");

            assertThatThrownBy(() -> filter.doFilterInternal(request, response, chain))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("The 'Authorization' header must be in the following format: 'Bearer token'");

        }

        @Test
        @DisplayName("when Authorization header is properly formatted, then it should not throw any exception")
        void whenAuthorizationHeaderIsProperlyFormatted_shouldNotThrowAnyException() {
            when(request.getHeader("Authorization")).thenReturn("Bearer token");
            when(extractor.extractUserFromToken("token")).thenReturn(AuthMock.newUserDomain());

            assertThatCode(() -> filter.doFilterInternal(request, response, chain))
                    .doesNotThrowAnyException();

        }
    }
}