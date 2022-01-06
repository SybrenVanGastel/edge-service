package be.itf.edgeservice.controller;

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

import java.util.List;

@CrossOrigin
@RestController
public class FilledBuildController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("http://${weaponservice.baseurl}")
    private String weaponServiceBaseUrl;

    @Value("http://${builderservice.baseurl}")
    private String builderServiceBaseUrl;

    private ResponseEntity<Object> getBuildsParameterized(String url, String vars) {
        ResponseEntity<List<Build>> responseEntityBuilds =
                restTemplate.exchange(builderServiceBaseUrl + url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Build>>() {
                        }, vars);

        List<Build> builds = responseEntityBuilds.getBody();

        assert builds != null;
        return BuildResponseBuilder.generateBuildsOverview(HttpStatus.OK, builds);
    }

    @GetMapping("/builds")
    public ResponseEntity<Object> getBuilds() {
        try {
            return getBuildsParameterized("/builders", null);
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

            return BuildResponseBuilder.generateGetBuildByName(HttpStatus.OK, returnBuild);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/builds/{name}")
    public ResponseEntity<Object> getBuildsByName(@PathVariable String name) {
        try {
            return getBuildsParameterized("/builders/{name}", name);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/builds/user/{name}")
    public ResponseEntity<Object> getBuildsByUsername(@PathVariable String name) {
        try {
            return getBuildsParameterized("/builders/user/{name}", name);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/builds/weapon/{name}")
    public ResponseEntity<Object> getBuildsByWeapon(@PathVariable String name) {
        try {
            return getBuildsParameterized("/builders/weapon/{name}", name);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/builds/tag/{name}")
    public ResponseEntity<Object> getBuildsByTag(@PathVariable String name) {
        try {
            return getBuildsParameterized("/builders/tag/{name}", name);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/weapons")
    public ResponseEntity<Object> getWeapons() {
        try {
            ResponseEntity<List<Weapon>> responseEntityWeapons =
                    restTemplate.exchange(weaponServiceBaseUrl + "/weapons", HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<Weapon>>() {
                            });

            List<Weapon> weapons = responseEntityWeapons.getBody();

            assert weapons != null;
            return BuildResponseBuilder.generateGetWeapons(HttpStatus.OK, weapons);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/weapon/{name}")
    public ResponseEntity<Object> getWeaponByName(@PathVariable String name) {
        try {
            Weapon weapon = restTemplate.getForObject(weaponServiceBaseUrl + "/weapon/{name}", Weapon.class, name);

            assert weapon != null;
            return BuildResponseBuilder.generateGetWeapon(HttpStatus.OK, weapon);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/build")
    public ResponseEntity<Object> createBuild(@RequestBody Build build) {
        try {
            Build createdBuild = restTemplate.postForObject(builderServiceBaseUrl + "/builder", build, Build.class);
            return new ResponseEntity<>(createdBuild, HttpStatus.OK);
        } catch (Exception e) {
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

            return new ResponseEntity<>(buildToUpdate, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/build/{name}")
    public ResponseEntity<Object> deleteBuild(@PathVariable String name) {
        try {
            restTemplate.delete(builderServiceBaseUrl + "/builder/{name}", name);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
