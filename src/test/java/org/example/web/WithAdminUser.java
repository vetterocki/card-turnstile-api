package org.example.web;

import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

@Sql(value = "/sql/users.sql")
@SqlMergeMode(MERGE)
@WithUserDetails(value = "admin@email.com")
@ActiveProfiles("test")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WithAdminUser {
}
