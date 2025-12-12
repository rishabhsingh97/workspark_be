package com.workspark.ConfigService;


import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DatabaseConfigTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Mock
    private ResultSet resultSet;

    @Test
    public void testFetchConfiguration() throws Exception {
        // Input parameters
        String application = "testApp";
        String profile = "default";
        String label = "main";
        String sql = "SELECT PROPERTY_KEY, PROPERTY_VALUE FROM CONFIG_PROPERTIES WHERE APPLICATION_NAME=:app AND PROFILE=:profile AND LABEL=:label";
        Map<String, Object> params = Map.of("app", application, "profile", profile, "label", label);

        // Expected result
        Map<String, String> expectedResult = Map.of(
                "property1", "value1",
                "property2", "value2"
        );

        // Mock ResultSet behavior
        Mockito.when(resultSet.next())
                .thenReturn(true) // First row
                .thenReturn(true) // Second row
                .thenReturn(false); // No more rows
        Mockito.when(resultSet.getString("PROPERTY_KEY"))
                .thenReturn("property1")
                .thenReturn("property2");
        Mockito.when(resultSet.getString("PROPERTY_VALUE"))
                .thenReturn("value1")
                .thenReturn("value2");

        // Mock NamedParameterJdbcTemplate behavior
        Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(sql), Mockito.eq(params), Mockito.any(ResultSetExtractor.class)))
                .thenAnswer(invocation -> {
                    ResultSetExtractor<Map<String, String>> extractor = invocation.getArgument(2);
                    return extractor.extractData(resultSet);
                });

        // Execute the query
        Map<String, String> actualResult = namedParameterJdbcTemplate.query(
                sql,
                params,
                rs -> {
                    Map<String, String> result = new HashMap<>();
                    while (rs.next()) {
                        result.put(rs.getString("PROPERTY_KEY"), rs.getString("PROPERTY_VALUE"));
                    }
                    return result;
                });

        // Assert the result
        assertEquals(expectedResult, actualResult, "The fetched configuration should match the expected result.");
    }
}

