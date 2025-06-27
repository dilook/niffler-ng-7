package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AllureDockerApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.allure.AllureProject;
import guru.qa.niffler.model.allure.AllureResponse;
import guru.qa.niffler.model.allure.AllureResults;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllureApiClient extends RestClient {

    private final AllureDockerApi allureDockerApi;


    public AllureApiClient(String baseUrl) {
        super(baseUrl);
        allureDockerApi = create(AllureDockerApi.class);
    }

    public void createProject(@Nonnull String name) {
        Response<AllureResponse> response;
        try {
            response = allureDockerApi.createProject(new AllureProject(name)).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.code());
        assertEquals("Project successfully created", response.body().metaData().get("message"));
    }

    public void sendResults(String projectId, @Nonnull AllureResults results) {
        Response<AllureResponse> response;
        try {
            response = allureDockerApi.sendResults(projectId, false, results).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
    }

    public void generateReport(@Nonnull String projectId,
                               @Nonnull String executionName,
                               String executionFrom,
                               String executionType) {
        Response<AllureResponse> response;
        try {
            response = allureDockerApi.generateReport(projectId, executionName, executionFrom, executionType).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
    }
}
