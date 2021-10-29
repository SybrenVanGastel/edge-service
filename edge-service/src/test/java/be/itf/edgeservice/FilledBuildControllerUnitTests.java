package be.itf.edgeservice;

import be.itf.edgeservice.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FilledBuildControllerUnitTests {

    @Value("http://localhost:8081")
    private String weaponServiceBaseUrl;

    @Value("http://localhost:8082")
    private String builderServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    List<Ability> abilities = Arrays.asList(new Ability(1, "ability1", "desc", "url", "category", "color"), new Ability(2, "ability2", "desc", "url", "category", "color"));
    List<Attribute> attributes = Arrays.asList(new Attribute("attribute1", 1), new Attribute("attribute2", 0.5));

    private Weapon weapon1 = new Weapon("weapon1", "desc", "url", abilities, attributes);
    private Weapon weapon2 = new Weapon("weapon2", "desc", "url", abilities, attributes);

    List<Integer> selectedAbilities = Arrays.asList(1, 2);
    List<Integer> selectedAttributes = Arrays.asList(50, 0, 0, 150, 0);

    private Build build1 = new Build("weapon1", "weapon2", "build1", "user1", Tag.PvE, selectedAbilities, selectedAbilities, selectedAttributes);
    private Build build2 = new Build("weapon2", "weapon1", "build2", "user2", Tag.General, selectedAbilities, selectedAbilities, selectedAttributes);

    private List<Build> allBuilds = Arrays.asList(build1, build2);

    @BeforeEach
    public void initializeMockserver() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void whenGetBuilds_thenReturnBuildsJson() throws Exception {

        // GET all builds
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(builderServiceBaseUrl + "/builders")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allBuilds))
                );

        mockMvc.perform(get("/builds"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("build1")))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[0].tag", is("PvE")))
                .andExpect(jsonPath("$[0].primaryWeaponName", is("weapon1")))
                .andExpect(jsonPath("$[0].secondaryWeaponName", is("weapon2")))
                .andExpect(jsonPath("$[1].name", is("build2")))
                .andExpect(jsonPath("$[1].username", is("user2")))
                .andExpect(jsonPath("$[1].tag", is("General")))
                .andExpect(jsonPath("$[1].primaryWeaponName", is("weapon2")))
                .andExpect(jsonPath("$[1].secondaryWeaponName", is("weapon1")));
    }

    @Test
    public void whenGetBuildByName_thenReturnBuildJson() throws Exception {

        // GET build with name "build1"
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(builderServiceBaseUrl + "/builder/build1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(build1))
                );

        // GET primary weapon
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(weaponServiceBaseUrl + "/weapon/weapon1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(weapon1))
                );

        // GET secondary weapon
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(weaponServiceBaseUrl + "/weapon/weapon2")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(weapon2))
                );

        mockMvc.perform(get("/build/{name}", "build1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("build1")))
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.tag", is("PvE")))
                .andExpect(jsonPath("$.attributes", aMapWithSize(5)))
                .andExpect(jsonPath("$.attributes.[\"strength\"]", is(50)))
                .andExpect(jsonPath("$.attributes.[\"dexterity\"]", is(0)))
                .andExpect(jsonPath("$.attributes.[\"intelligence\"]", is(0)))
                .andExpect(jsonPath("$.attributes.[\"focus\"]", is(150)))
                .andExpect(jsonPath("$.attributes.[\"constitution\"]", is(0)))
                .andExpect(jsonPath("$.primaryWeapon.name", is("weapon1")))
                .andExpect(jsonPath("$.primaryWeapon.description", is("desc")))
                .andExpect(jsonPath("$.primaryWeapon.imageUrl", is("url")))
                .andExpect(jsonPath("$.primaryWeapon.abilities[0].name", is("ability1")))
                .andExpect(jsonPath("$.primaryWeapon.abilities[0].description", is("desc")))
                .andExpect(jsonPath("$.primaryWeapon.abilities[0].imageUrl", is("url")))
                .andExpect(jsonPath("$.primaryWeapon.abilities[0].category", is("category")))
                .andExpect(jsonPath("$.primaryWeapon.abilities[0].color", is("color")))
                .andExpect(jsonPath("$.primaryWeapon.abilities[0].isSelected", is(true)))
                .andExpect(jsonPath("$.primaryWeapon.abilities[1].name", is("ability2")))
                .andExpect(jsonPath("$.primaryWeapon.abilities[1].description", is("desc")))
                .andExpect(jsonPath("$.primaryWeapon.abilities[1].imageUrl", is("url")))
                .andExpect(jsonPath("$.primaryWeapon.abilities[1].category", is("category")))
                .andExpect(jsonPath("$.primaryWeapon.abilities[1].color", is("color")))
                .andExpect(jsonPath("$.primaryWeapon.abilities[1].isSelected", is(true)))
                .andExpect(jsonPath("$.primaryWeapon.attributes[0].name", is("attribute1")))
                .andExpect(jsonPath("$.primaryWeapon.attributes[0].scaleFactor", is(1.0)))
                .andExpect(jsonPath("$.primaryWeapon.attributes[1].name", is("attribute2")))
                .andExpect(jsonPath("$.primaryWeapon.attributes[1].scaleFactor", is(0.5)))
                .andExpect(jsonPath("$.secondaryWeapon.name", is("weapon2")))
                .andExpect(jsonPath("$.secondaryWeapon.description", is("desc")))
                .andExpect(jsonPath("$.secondaryWeapon.imageUrl", is("url")))
                .andExpect(jsonPath("$.secondaryWeapon.abilities[0].name", is("ability1")))
                .andExpect(jsonPath("$.secondaryWeapon.abilities[0].description", is("desc")))
                .andExpect(jsonPath("$.secondaryWeapon.abilities[0].imageUrl", is("url")))
                .andExpect(jsonPath("$.secondaryWeapon.abilities[0].category", is("category")))
                .andExpect(jsonPath("$.secondaryWeapon.abilities[0].color", is("color")))
                .andExpect(jsonPath("$.secondaryWeapon.abilities[0].isSelected", is(true)))
                .andExpect(jsonPath("$.secondaryWeapon.abilities[1].name", is("ability2")))
                .andExpect(jsonPath("$.secondaryWeapon.abilities[1].description", is("desc")))
                .andExpect(jsonPath("$.secondaryWeapon.abilities[1].imageUrl", is("url")))
                .andExpect(jsonPath("$.secondaryWeapon.abilities[1].category", is("category")))
                .andExpect(jsonPath("$.secondaryWeapon.abilities[1].color", is("color")))
                .andExpect(jsonPath("$.secondaryWeapon.abilities[1].isSelected", is(true)))
                .andExpect(jsonPath("$.secondaryWeapon.attributes[0].name", is("attribute1")))
                .andExpect(jsonPath("$.secondaryWeapon.attributes[0].scaleFactor", is(1.0)))
                .andExpect(jsonPath("$.secondaryWeapon.attributes[1].name", is("attribute2")))
                .andExpect(jsonPath("$.secondaryWeapon.attributes[1].scaleFactor", is(0.5)));
    }

    @Test
    public void whenGetBuildsByName_thenReturnBuildsJson() throws Exception {

        // GET builds by name
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(builderServiceBaseUrl + "/builders/build")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allBuilds))
                );

        mockMvc.perform(get("/builds/{name}", "build"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("build1")))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[0].tag", is("PvE")))
                .andExpect(jsonPath("$[0].primaryWeaponName", is("weapon1")))
                .andExpect(jsonPath("$[0].secondaryWeaponName", is("weapon2")))
                .andExpect(jsonPath("$[1].name", is("build2")))
                .andExpect(jsonPath("$[1].username", is("user2")))
                .andExpect(jsonPath("$[1].tag", is("General")))
                .andExpect(jsonPath("$[1].primaryWeaponName", is("weapon2")))
                .andExpect(jsonPath("$[1].secondaryWeaponName", is("weapon1")));
    }

    @Test
    public void whenGetBuildsByUsername_thenReturnBuildsJson() throws Exception {

        // GET builds by name
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(builderServiceBaseUrl + "/builders/user/user1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Collections.singletonList(build1)))
                );

        mockMvc.perform(get("/builds/user/{name}", "user1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("build1")))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[0].tag", is("PvE")))
                .andExpect(jsonPath("$[0].primaryWeaponName", is("weapon1")))
                .andExpect(jsonPath("$[0].secondaryWeaponName", is("weapon2")));
    }

    @Test
    public void whenGetBuildsByWeapon_thenReturnBuildsJson() throws Exception {

        // GET builds by name
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(builderServiceBaseUrl + "/builders/weapon/weapon1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allBuilds))
                );

        mockMvc.perform(get("/builds/weapon/{name}", "weapon1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("build1")))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[0].tag", is("PvE")))
                .andExpect(jsonPath("$[0].primaryWeaponName", is("weapon1")))
                .andExpect(jsonPath("$[0].secondaryWeaponName", is("weapon2")))
                .andExpect(jsonPath("$[1].name", is("build2")))
                .andExpect(jsonPath("$[1].username", is("user2")))
                .andExpect(jsonPath("$[1].tag", is("General")))
                .andExpect(jsonPath("$[1].primaryWeaponName", is("weapon2")))
                .andExpect(jsonPath("$[1].secondaryWeaponName", is("weapon1")));
    }

    @Test
    public void whenGetBuildsByTag_thenReturnBuildsJson() throws Exception {

        // GET builds by name
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(builderServiceBaseUrl + "/builders/tag/PvE")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Collections.singletonList(build1)))
                );

        mockMvc.perform(get("/builds/tag/{name}", "PvE"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("build1")))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[0].tag", is("PvE")))
                .andExpect(jsonPath("$[0].primaryWeaponName", is("weapon1")))
                .andExpect(jsonPath("$[0].secondaryWeaponName", is("weapon2")));
    }

    @Test
    public void whenAddBuild_thenReturnBuildJson() throws Exception {

        Build build = new Build("weapon1", "weapon2", "create", "user1", Tag.PvE, selectedAbilities, selectedAbilities, selectedAttributes);

        // POST create build
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(builderServiceBaseUrl + "/builder")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(build))
                );

        mockMvc.perform(post("/build")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(build)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.primaryWeaponName", is("weapon1")))
                .andExpect(jsonPath("$.secondaryWeaponName", is("weapon2")))
                .andExpect(jsonPath("$.name", is("create")))
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.tag", is("PvE")))
                .andExpect(jsonPath("$.selectedAbilitiesWeapon1", hasSize(2)))
                .andExpect(jsonPath("$.selectedAbilitiesWeapon1[0]", is(1)))
                .andExpect(jsonPath("$.selectedAbilitiesWeapon1[1]", is(2)))
                .andExpect(jsonPath("$.selectedAbilitiesWeapon2", hasSize(2)))
                .andExpect(jsonPath("$.selectedAbilitiesWeapon2[0]", is(1)))
                .andExpect(jsonPath("$.selectedAbilitiesWeapon2[1]", is(2)))
                .andExpect(jsonPath("$.attributeOptions", hasSize(5)))
                .andExpect(jsonPath("$.attributeOptions[0]", is(50)))
                .andExpect(jsonPath("$.attributeOptions[1]", is(0)))
                .andExpect(jsonPath("$.attributeOptions[2]", is(0)))
                .andExpect(jsonPath("$.attributeOptions[3]", is(150)))
                .andExpect(jsonPath("$.attributeOptions[4]", is(0)))
        ;
    }

    @Test
    public void whenUpdateBuild_thenReturnBuildJson() throws Exception {

        Build build = new Build("weapon1", "weapon2", "update", "user1", Tag.PvE, selectedAbilities, selectedAbilities, selectedAttributes);

        // GET build with name "build2"
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(builderServiceBaseUrl + "/builder/build2")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(build2))
                );

        // PUT update build2
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(builderServiceBaseUrl + "/builder/build2")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(build))
                );

        mockMvc.perform(put("/builder/{name}", "build2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(build)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.primaryWeaponName", is("weapon1")))
                .andExpect(jsonPath("$.secondaryWeaponName", is("weapon2")))
                .andExpect(jsonPath("$.name", is("update")))
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.tag", is("PvE")))
                .andExpect(jsonPath("$.selectedAbilitiesWeapon1", hasSize(2)))
                .andExpect(jsonPath("$.selectedAbilitiesWeapon1[0]", is(1)))
                .andExpect(jsonPath("$.selectedAbilitiesWeapon1[1]", is(2)))
                .andExpect(jsonPath("$.selectedAbilitiesWeapon2", hasSize(2)))
                .andExpect(jsonPath("$.selectedAbilitiesWeapon2[0]", is(1)))
                .andExpect(jsonPath("$.selectedAbilitiesWeapon2[1]", is(2)))
                .andExpect(jsonPath("$.attributeOptions", hasSize(5)))
                .andExpect(jsonPath("$.attributeOptions[0]", is(50)))
                .andExpect(jsonPath("$.attributeOptions[1]", is(0)))
                .andExpect(jsonPath("$.attributeOptions[2]", is(0)))
                .andExpect(jsonPath("$.attributeOptions[3]", is(150)))
                .andExpect(jsonPath("$.attributeOptions[4]", is(0)));
    }

    @Test
    public void whenDeleteBuild_thenReturnStatusOk() throws Exception {

        // DELETE build2
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(builderServiceBaseUrl + "/builder/build2")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                );

        mockMvc.perform(delete("/build/{name}", "build2"))
                .andExpect(status().isOk());
    }

}
