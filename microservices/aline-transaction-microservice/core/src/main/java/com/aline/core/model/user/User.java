package com.aline.core.model.user;

import com.aline.core.validation.annotation.Username;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

/**
 *  User Entity
 *  <p> User class for authentication </p>
 */
@Getter
@Setter
@ToString
@Entity
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Username
    private String username;

    @NotNull
    private String password;

    /**
     * Retrieves the role that is set
     * in the {@link DiscriminatorValue} annotation.
     * @return User role string or <code>null</code> if {@link DiscriminatorValue} annotation does not exist.
     */
    @Transient
    public String getRole() {
        DiscriminatorValue annotation = this.getClass().getAnnotation(DiscriminatorValue.class);
        return Optional.of(annotation.value()).orElse(null);
    }

    /**
     * Retrieves the role that is set in the {@link DiscriminatorValue} annotation.
     * @return UserRole enum value or <code>null</code> if {@link DiscriminatorValue} does not exist.
     */
    @Transient
    public UserRole getUserRole() {
        return Optional.of(UserRole.valueOf(getRole().toUpperCase())).orElse(null);
    }

    private boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;

        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return 562048007;
    }
}
