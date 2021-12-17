package be.itf.edgeservice.response;

import be.itf.edgeservice.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuildResponseBuilder {

    private final static String attributesStr = "attributes";
    private final static String descriptionStr = "description";
    private final static String imageUrlStr = "imageUrl";
    private final static String isSelectedStr = "isSelected";

    private BuildResponseBuilder() {
    }

    public static ResponseEntity<Object> generateBuildsOverview(HttpStatus status, List<Build> builds) {

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

    public static ResponseEntity<Object> generateGetBuildByName(HttpStatus status, FilledBuild build) {
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

        buildMap.put(attributesStr, attributesMap);

        // Primary Weapon
        Map<String, Object> primaryWeaponMap = new LinkedHashMap<>();
        Weapon primaryWeapon = build.getPrimaryWeapon();

        primaryWeaponMap.put("id", primaryWeapon.getId());
        primaryWeaponMap.put("name", primaryWeapon.getName());
        primaryWeaponMap.put(descriptionStr, primaryWeapon.getDescription());
        primaryWeaponMap.put(imageUrlStr, primaryWeapon.getImageUrl());

        List<Object> abilitiesPrimaryWeapon = new ArrayList<>();
        for(Ability ability : primaryWeapon.getAbilities()) {
            Map<String, Object> abilityMap = new LinkedHashMap<>();

            abilityMap.put("id", ability.getId());
            abilityMap.put("name", ability.getName());
            abilityMap.put(descriptionStr, ability.getDescription());
            abilityMap.put(imageUrlStr, ability.getImageUrl());
            abilityMap.put("category", ability.getCategory());
            abilityMap.put("color", ability.getColor());
            abilityMap.put(isSelectedStr, build.getSelectedAbilitiesWeapon1().contains(ability.getId()));

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
        primaryWeaponMap.put(attributesStr, scaleWithAttributeList);

        buildMap.put("primaryWeapon", primaryWeaponMap);

        // Secondary Weapon
        Map<String, Object> secondaryWeaponMap = new LinkedHashMap<>();
        Weapon secondaryWeapon = build.getSecondaryWeapon();

        secondaryWeaponMap.put("id", secondaryWeapon.getId());
        secondaryWeaponMap.put("name", secondaryWeapon.getName());
        secondaryWeaponMap.put(descriptionStr, secondaryWeapon.getDescription());
        secondaryWeaponMap.put(imageUrlStr, secondaryWeapon.getImageUrl());

        List<Object> abilitiesSecondaryWeapon = new ArrayList<>();
        for(Ability ability : secondaryWeapon.getAbilities()) {
            Map<String, Object> abilityMap = new LinkedHashMap<>();

            abilityMap.put("id", ability.getId());
            abilityMap.put("name", ability.getName());
            abilityMap.put(descriptionStr, ability.getDescription());
            abilityMap.put(imageUrlStr, ability.getImageUrl());
            abilityMap.put("category", ability.getCategory());
            abilityMap.put("color", ability.getColor());
            abilityMap.put(isSelectedStr, build.getSelectedAbilitiesWeapon2().contains(ability.getId()));

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
        secondaryWeaponMap.put(attributesStr, scaleWithAttributeList);
        buildMap.put("secondaryWeapon", secondaryWeaponMap);

        return new ResponseEntity<>(buildMap, status);
    }

    public static ResponseEntity<Object> generateGetWeapon(HttpStatus status, Weapon weapon) {
        Map<String, Object> weaponMap = new LinkedHashMap<>();
        List<Object> abilityList = new ArrayList<>();

        weaponMap.put("id", weapon.getId());
        weaponMap.put("name", weapon.getName());

        for(Ability ability : weapon.getAbilities()) {
            Map<String, Object> abilityMap = new LinkedHashMap<>();

            abilityMap.put("id", ability.getId());
            abilityMap.put("name", ability.getName());
            abilityMap.put("description", ability.getDescription());
            abilityMap.put("imageUrl", ability.getImageUrl());
            abilityMap.put("category", ability.getCategory());
            abilityMap.put("color", ability.getColor());

            abilityList.add(abilityMap);
        }
        weaponMap.put("abilities", abilityList);

        return new ResponseEntity<>(weaponMap, status);
    }

    public static ResponseEntity<Object> generateGetWeapons(HttpStatus status, List<Weapon> weapons) {
        List<Object> weaponList = new ArrayList<>();

        for(Weapon weapon : weapons) {
            Map<String, Object> weaponMap = new LinkedHashMap<>();

            weaponMap.put("id", weapon.getId());
            weaponMap.put("name", weapon.getName());

            weaponList.add(weaponMap);
        }

        return new ResponseEntity<>(weaponList, status);
    }
}
