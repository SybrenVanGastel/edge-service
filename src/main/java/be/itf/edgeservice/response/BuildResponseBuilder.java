package be.itf.edgeservice.response;

import be.itf.edgeservice.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuildResponseBuilder {
    public static ResponseEntity<Object> generateBuildsOverview(String message, HttpStatus status, List<Build> builds) {

        List<Object> buildList = new ArrayList<>();

        for(Build build : builds) {
            Map<String, Object> buildMap = new LinkedHashMap<>();

            buildMap.put("id", build.getId());
            buildMap.put("name", build.getName());
            buildMap.put("username", build.getUsername());
            buildMap.put("tag", build.getTag());
            buildMap.put("primaryWeaponName", build.getPrimaryWeaponName());
            buildMap.put("secondaryWeaponName", build.getSecondaryWeaponName());

            buildList.add(buildMap);
        }

        return new ResponseEntity<>(buildList, status);
    }

    public static ResponseEntity<Object> generateGetBuildByName(String message, HttpStatus status, FilledBuild build) {
        Map<String, Object> buildMap = new LinkedHashMap<>();

        // Basic information
        buildMap.put("name", build.getName());
        buildMap.put("username", build.getUsername());
        buildMap.put("tag", build.getTag());

        // Attributes information
        Map<String, Object> attributesMap = new LinkedHashMap<>();
        List<Integer> attributes = build.getAttributeOptions();

        attributesMap.put("strength", attributes.get(0));
        attributesMap.put("dexterity", attributes.get(1));
        attributesMap.put("intelligence", attributes.get(2));
        attributesMap.put("focus", attributes.get(3));
        attributesMap.put("constitution", attributes.get(4));

        buildMap.put("attributes", attributesMap);

        // Primary Weapon
        Map<String, Object> primaryWeaponMap = new LinkedHashMap<>();
        Weapon primaryWeapon = build.getPrimaryWeapon();

        primaryWeaponMap.put("id", primaryWeapon.getId());
        primaryWeaponMap.put("name", primaryWeapon.getName());
        primaryWeaponMap.put("description", primaryWeapon.getDescription());
        primaryWeaponMap.put("imageUrl", primaryWeapon.getImageUrl());

        List<Object> abilitiesPrimaryWeapon = new ArrayList<>();
        for(Ability ability : primaryWeapon.getAbilities()) {
            Map<String, Object> abilityMap = new LinkedHashMap<>();

            abilityMap.put("id", ability.getId());
            abilityMap.put("name", ability.getName());
            abilityMap.put("description", ability.getDescription());
            abilityMap.put("imageUrl", ability.getImageUrl());
            abilityMap.put("category", ability.getCategory());
            abilityMap.put("color", ability.getColor());
            if(build.getSelectedAbilitiesWeapon1().contains(ability.getId())) {
                abilityMap.put("isSelected", true);
            } else {
                abilityMap.put("isSelected", false);
            }

            abilitiesPrimaryWeapon.add(abilityMap);
        }
        primaryWeaponMap.put("abilities", abilitiesPrimaryWeapon);

        List<Object> scaleWithAttributeList = new ArrayList<>();
        for(Attribute attribute : primaryWeapon.getAttributes()) {
            if(attribute.getScaleFactor() != 0) {
                Map<String, Object> attributeMap = new LinkedHashMap<>();

                attributeMap.put("name", attribute.getName());
                attributeMap.put("scaleFactor", attribute.getScaleFactor());

                scaleWithAttributeList.add(attributeMap);
            }
        }
        primaryWeaponMap.put("attributes", scaleWithAttributeList);

        buildMap.put("primaryWeapon", primaryWeaponMap);

        // Secondary Weapon
        Map<String, Object> secondaryWeaponMap = new LinkedHashMap<>();
        Weapon secondaryWeapon = build.getSecondaryWeapon();

        secondaryWeaponMap.put("id", secondaryWeapon.getId());
        secondaryWeaponMap.put("name", secondaryWeapon.getName());
        secondaryWeaponMap.put("description", secondaryWeapon.getDescription());
        secondaryWeaponMap.put("imageUrl", secondaryWeapon.getImageUrl());

        List<Object> abilitiesSecondaryWeapon = new ArrayList<>();
        for(Ability ability : secondaryWeapon.getAbilities()) {
            Map<String, Object> abilityMap = new LinkedHashMap<>();

            abilityMap.put("id", ability.getId());
            abilityMap.put("name", ability.getName());
            abilityMap.put("description", ability.getDescription());
            abilityMap.put("imageUrl", ability.getImageUrl());
            abilityMap.put("category", ability.getCategory());
            abilityMap.put("color", ability.getColor());
            if(build.getSelectedAbilitiesWeapon2().contains(ability.getId())) {
                abilityMap.put("isSelected", true);
            } else {
                abilityMap.put("isSelected", false);
            }

            abilitiesSecondaryWeapon.add(abilityMap);
        }
        secondaryWeaponMap.put("abilities", abilitiesSecondaryWeapon);

        scaleWithAttributeList = new ArrayList<>();
        for(Attribute attribute : secondaryWeapon.getAttributes()) {
            if(attribute.getScaleFactor() != 0) {
                Map<String, Object> attributeMap = new LinkedHashMap<>();

                attributeMap.put("name", attribute.getName());
                attributeMap.put("scaleFactor", attribute.getScaleFactor());

                scaleWithAttributeList.add(attributeMap);
            }
        }
        secondaryWeaponMap.put("attributes", scaleWithAttributeList);
        buildMap.put("secondaryWeapon", secondaryWeaponMap);

        return new ResponseEntity<>(buildMap, status);
    }
}
