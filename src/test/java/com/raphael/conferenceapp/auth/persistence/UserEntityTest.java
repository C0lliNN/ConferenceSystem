package com.raphael.conferenceapp.auth.persistence;

import com.raphael.conferenceapp.auth.domain.User;
import com.raphael.conferenceapp.auth.mock.AuthMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    @Nested
    @DisplayName("method: toDomain()")
    class ToDomainMethod {

        @Test
        @DisplayName("when called, then it should convert the entity to a domain object")
        void whenCalled_shouldConvertTheEntityToDomainObject() {
            UserEntity entity = AuthMock.newUserEntity();

            User expected = User.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .email(entity.getEmail())
                    .password(entity.getPassword())
                    .build();

            User actual = entity.toDomain();

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method: fromDomain(User)")
    class FromDomainMethod {

        @Test
        @DisplayName("when called, then it should create an entity from a domain object")
        void whenCalled_shouldCreateAnEntityFromADomainObject() {
            User domain = AuthMock.newUserDomain();

            UserEntity expected = new UserEntity(
                    domain.getId(),
                    domain.getName(),
                    domain.getEmail(),
                    domain.getPassword()
            );

            UserEntity actual = UserEntity.fromDomain(domain);

            assertThat(actual).isEqualTo(expected);
        }
    }
}