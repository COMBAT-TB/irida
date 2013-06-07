package ca.corefacility.bioinformatics.irida.web.controller.test.integration;


import com.google.common.net.HttpHeaders;
import com.jayway.restassured.response.Response;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.path.json.JsonPath.from;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Integration tests for projects.
 *
 * @author Franklin Bristow <franklin.bristow@phac-aspc.gc.ca>
 */
public class ProjectIntegrationTest {

    private static final String PROJECTS = "/projects";

    /**
     * If I try to issue a create request for an object with an invalid field name, the server should respond with 400.
     */
    @Test
    public void testCreateProjectBadFieldName() {
        Response r = given().body("{ \"projectName\": \"some stupid project\" }").
                expect().response().statusCode(HttpStatus.BAD_REQUEST.value()).when().post(PROJECTS);
        assertTrue(r.getBody().asString().contains("Unrecognized property [projectName]"));
    }

    /**
     * Field names should be quoted. We should handle that failure gracefully.
     */
    @Test
    public void testCreateProjectNoQuotes() {
        Response r = given().body("{ name: \"some stupid project\" }").
                expect().response().statusCode(HttpStatus.BAD_REQUEST.value()).when().post(PROJECTS);
        assertTrue(r.getBody().asString().contains("double quotes"));
    }

    @Test
    public void testCreateProject() {
        Map<String, String> project = new HashMap<>();
        project.put("name", "new project");

        Response r = given().body(project).expect().response()
                .statusCode(HttpStatus.CREATED.value()).when().post(PROJECTS);
        String location = r.getHeader(HttpHeaders.LOCATION);
        assertNotNull(location);
        assertTrue(location.startsWith("http://localhost:8080/api/projects/"));
    }

    @Test
    public void testGetProject() {
        Map<String, String> project = new HashMap<>();
        String projectName = "new project";
        project.put("name", projectName);
        Response r = given().body(project).post(PROJECTS);
        String location = r.getHeader(HttpHeaders.LOCATION);
        expect().body("resource.name", equalTo(projectName)).and()
                .body("resource.links.rel", hasItems("self", "project/users", "project/samples", "project/sequenceFiles"))
                .when().get(location);
    }

    @Test
    public void testUpdateProjectName() {
        Map<String, String> project = new HashMap<>();
        String projectName = "new project";
        String updatedName = "updated new project";
        project.put("name", projectName);
        Response r = given().body(project).post(PROJECTS);
        String location = r.getHeader(HttpHeaders.LOCATION);
        project.put("name", updatedName);
        given().body(project).expect().statusCode(HttpStatus.OK.value()).when().patch(location);
        expect().body("resource.name", equalTo(updatedName)).when().get(location);
    }

    @Test
    public void testGetProjects() {
        // first page shouldn't have prev link, default view returns 20 projects
        String[] expectedProjectNames = new String[20];
        for (int i = 0; i < expectedProjectNames.length; i++) {
            expectedProjectNames[i] = "Project " + (i + 1);
        }
        expect().body("resource.links.rel", hasItems("self", "first", "next", "last")).and()
                .body("resource.links.rel", not(hasItem("prev"))).and()
                .body("resource.totalResources", isA(Integer.class)).and()
                .body("resource.resources.name", hasItems(expectedProjectNames)).when().get(PROJECTS);
    }

    @Test
    public void testDeleteProject() {
        String projectsJson = get(PROJECTS).asString();
        String projectUri = from(projectsJson).get("resource.resources[0].links[0].href");
        expect().body("resource.links.rel", hasItems("collection")).and()
                .body("resource.links.href", hasItems("http://localhost:8080/api/projects")).when().delete(projectUri);
    }

    @Test
    public void verifyRelatedResources() {
        // project should have the following related resource names: samples, users, sequenceFiles
        String projectsJson = get(PROJECTS).asString();
        String projectUri = from(projectsJson).get("resource.resources[0].links[0].href");
        expect().body("relatedResources.samples.links.rel", hasItem("project/samples")).and()
                .body("relatedResources.users.links.rel", hasItem("project/users")).and()
                .body("relatedResources.sequenceFiles.links.rel", hasItem("project/sequenceFiles")).when().get(projectUri);
    }
}
