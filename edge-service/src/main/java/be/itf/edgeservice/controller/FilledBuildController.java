package be.itf.edgeservice.controller;

import be.itf.edgeservice.model.Ability;
import be.itf.edgeservice.model.Build;
import be.itf.edgeservice.model.FilledBuild;
import be.itf.edgeservice.model.Weapon;
import be.itf.edgeservice.response.BuildResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FilledBuildController {
    // RestTemplate
    private RestTemplate restTemplate = new RestTemplate();

    @Value("http://localhost:8081")
    private String weaponServiceBaseUrl;

    @Value("http://localhost:8082")
    private String builderServiceBaseUrl;

    @GetMapping("/builds")
    public ResponseEntity<Object> getBuilds() {
        try {
            ResponseEntity<List<Build>> responseEntityBuilds =
                    restTemplate.exchange(builderServiceBaseUrl + "/builders", HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<Build>>() {
                            });

            List<Build> builds = responseEntityBuilds.getBody();

            assert builds != null;
            return BuildResponseBuilder.generateBuildsOverview("Successfully returned all builds", HttpStatus.OK, builds);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/build/{name}")
    public ResponseEntity<Object> getBuildByName(@PathVariable String name) {
        try {
            Build build = restTemplate.getForObject(builderServiceBaseUrl + "/builder/{name}", Build.class, name);

            assert build != null;
            Weapon primaryWeapon = restTemplate.getForObject(weaponServiceBaseUrl + "/weapon/{name}", Weapon.class, build.getPrimaryWeaponName());
            Weapon secondaryWeapon = restTemplate.getForObject(weaponServiceBaseUrl + "/weapon/{name}", Weapon.class, build.getSecondaryWeaponName());

            FilledBuild returnBuild = new FilledBuild(primaryWeapon, secondaryWeapon, build.getName(), build.getUsername(), build.getTag(), build.getSelectedAbilitiesWeapon1(), build.getSelectedAbilitiesWeapon2(), build.getAttributeOptions());

            return BuildResponseBuilder.generateGetBuildByName("Successfully returned build: " + name, HttpStatus.OK, returnBuild);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/builds/{name}")
    public ResponseEntity<Object> getBuildsByName(@PathVariable String name) {
        try {
            ResponseEntity<List<Build>> responseEntityBuilds =
                    restTemplate.exchange(builderServiceBaseUrl + "/builders/{name}", HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<Build>>() {
                            }, name);

            List<Build> builds = responseEntityBuilds.getBody();

            assert builds != null;
            return BuildResponseBuilder.generateBuildsOverview("Successfully returned builds where name matches: " + name, HttpStatus.OK, builds);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/builds/user/{name}")
    public ResponseEntity<Object> getBuildsByUsername(@PathVariable String name) {
        try {
            ResponseEntity<List<Build>> responseEntityBuilds =
                    restTemplate.exchange(builderServiceBaseUrl + "/builders/user/{name}", HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<Build>>() {
                            }, name);

            List<Build> builds = responseEntityBuilds.getBody();

            assert builds != null;
            return BuildResponseBuilder.generateBuildsOverview("Successfully returned builds where username matches: " + name, HttpStatus.OK, builds);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/builds/weapon/{name}")
    public ResponseEntity<Object> getBuildsByWeapon(@PathVariable String name) {
        try {
            ResponseEntity<List<Build>> responseEntityBuilds =
                    restTemplate.exchange(builderServiceBaseUrl + "/builders/weapon/{name}", HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<Build>>() {
                            }, name);

            List<Build> builds = responseEntityBuilds.getBody();

            assert builds != null;
            return BuildResponseBuilder.generateBuildsOverview("Successfully returned builds where weapon matches: " + name, HttpStatus.OK, builds);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/builds/tag/{name}")
    public ResponseEntity<Object> getBuildsByTag(@PathVariable String name) {
        try {
            ResponseEntity<List<Build>> responseEntityBuilds =
                    restTemplate.exchange(builderServiceBaseUrl + "/builders/tag/{name}", HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<Build>>() {
                            }, name);

            List<Build> builds = responseEntityBuilds.getBody();

            assert builds != null;
            return BuildResponseBuilder.generateBuildsOverview("Successfully returned builds where tag matches: " + name, HttpStatus.OK, builds);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/build")
    public ResponseEntity<Object> createBuild(@RequestBody Build build) {
        try {
            Build createdBuild = restTemplate.postForObject(builderServiceBaseUrl + "/builder", build, Build.class);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/build/{name}")
    public ResponseEntity<Object> putBuild(@PathVariable String name, @RequestBody Build build) {
        try {
            Build buildToUpdate = restTemplate.getForObject(builderServiceBaseUrl + "/builder/{name}", Build.class, name);

            assert buildToUpdate != null;
            buildToUpdate.setName(build.getName());
            buildToUpdate.setUsername(build.getUsername());
            buildToUpdate.setPrimaryWeaponName(build.getPrimaryWeaponName());
            buildToUpdate.setSecondaryWeaponName(build.getSecondaryWeaponName());
            buildToUpdate.setTag(build.getTag());
            buildToUpdate.setSelectedAbilitiesWeapon1(build.getSelectedAbilitiesWeapon1());
            buildToUpdate.setSelectedAbilitiesWeapon2(build.getSelectedAbilitiesWeapon2());
            buildToUpdate.setAttributeOptions(build.getAttributeOptions());

            restTemplate.exchange(builderServiceBaseUrl + "/builder/{name}", HttpMethod.PUT, new HttpEntity<>(buildToUpdate), Build.class, name);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/build/{name}")
    public ResponseEntity<Object> deleteBuild(@PathVariable String name) {
        try {
            restTemplate.delete(builderServiceBaseUrl + "/builder/{name}", name);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
