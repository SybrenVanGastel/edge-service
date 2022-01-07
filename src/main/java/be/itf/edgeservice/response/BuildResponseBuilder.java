package be.itf.edgeservice.response;

import be.itf.edgeservice.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuildResponseBuilder {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String USERNAME = "username";
    private static final String TAG = "tag";
    private static final String PRIMARY_WEAPON_NAME = "primaryWeaponName";
    private static final String SECONDARY_WEAPON_NAME = "secondaryWeaponName";
    private static final String PRIMARY_WEAPON = "primaryWeapon";
    private static final String SECONDARY_WEAPON = "secondaryWeapon";
    private static final String STRENGTH = "strength";
    private static final String DEXTERITY = "dexterity";
    private static final String FOCUS = "focus";
    private static final String INTELLIGENCE = "intelligence";
    private static final String CONSTITUTION = "constitution";
    private static final String ATTRIBUTES = "attributes";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE_URL = "imageUrl";
    private static final String IS_SELECTED = "isSelected";
    private static final String CATEGORY = "category";
    private static final String COLOR = "color";
    private static final String ABILITIES = "abilities";
    private static final String SCALE_FACTOR = "scaleFactor";

    private BuildResponseBuilder() {
    }

    public static ResponseEntity<Object> generateBuildsOverview(HttpStatus status, List<Build> builds) {

        List<Object> buildList = new ArrayList<>();

        for (Build build : builds) {
            Map<String, Object> buildMap = new LinkedHashMap<>();

            buildMap.put(ID, build.getId());
            buildMap.put(NAME, build.getName());
            buildMap.put(USERNAME, build.getUsername());
            buildMap.put(TAG, build.getTag());
            buildMap.put(PRIMARY_WEAPON_NAME, build.getPrimaryWeaponName());
            buildMap.put(SECONDARY_WEAPON_NAME, build.getSecondaryWeaponName());

            buildList.add(buildMap);
        }

        return new ResponseEntity<>(buildList, status);
    }

    public static ResponseEntity<Object> generateGetBuildByName(HttpStatus status, FilledBuild build) {
        Map<String, Object> buildMap = new LinkedHashMap<>();

        // Basic information
        buildMap.put(NAME, build.getName());
        buildMap.put(USERNAME, build.getUsername());
        buildMap.put(TAG, build.getTag());

        // Attributes information
        Map<String, Object> attributesMap = new LinkedHashMap<>();
        List<Integer> attributes = build.getAttributeOptions();

        attributesMap.put(STRENGTH, attributes.get(0));
        attributesMap.put(DEXTERITY, attributes.get(1));
        attributesMap.put(INTELLIGENCE, attributes.get(2));
        attributesMap.put(FOCUS, attributes.get(3));
        attributesMap.put(CONSTITUTION, attributes.get(4));

        buildMap.put(ATTRIBUTES, attributesMap);

        // Primary Weapon
        Map<String, Object> primaryWeaponMap = new LinkedHashMap<>();
        Weapon primaryWeapon = build.getPrimaryWeapon();

        primaryWeaponMap.put(ID, primaryWeapon.getId());
        primaryWeaponMap.put(NAME, primaryWeapon.getName());
        primaryWeaponMap.put(DESCRIPTION, primaryWeapon.getDescription());
        primaryWeaponMap.put(IMAGE_URL, primaryWeapon.getImageUrl());

        List<Object> abilitiesPrimaryWeapon = new ArrayList<>();
        for (Ability ability : primaryWeapon.getAbilities()) {
            Map<String, Object> abilityMap = getAbilitiesFromWeapon(ability);
            abilityMap.put(IS_SELECTED, build.getSelectedAbilitiesWeapon1().contains(ability.getId()));

            abilitiesPrimaryWeapon.add(abilityMap);
        }
        primaryWeaponMap.put(ABILITIES, abilitiesPrimaryWeapon);

        List<Object> scaleWithAttributeList = new ArrayList<>();
        for (Attribute attribute : primaryWeapon.getAttributes()) {
            if (attribute.getScaleFactor() != 0) {
                Map<String, Object> attributeMap = new LinkedHashMap<>();

                attributeMap.put(NAME, attribute.getName());
                attributeMap.put(SCALE_FACTOR, attribute.getScaleFactor());

                scaleWithAttributeList.add(attributeMap);
            }
        }
        primaryWeaponMap.put(ATTRIBUTES, scaleWithAttributeList);

        buildMap.put(PRIMARY_WEAPON, primaryWeaponMap);

        // Secondary Weapon
        Map<String, Object> secondaryWeaponMap = new LinkedHashMap<>();
        Weapon secondaryWeapon = build.getSecondaryWeapon();

        secondaryWeaponMap.put(ID, secondaryWeapon.getId());
        secondaryWeaponMap.put(NAME, secondaryWeapon.getName());
        secondaryWeaponMap.put(DESCRIPTION, secondaryWeapon.getDescription());
        secondaryWeaponMap.put(IMAGE_URL, secondaryWeapon.getImageUrl());

        List<Object> abilitiesSecondaryWeapon = new ArrayList<>();
        for (Ability ability : secondaryWeapon.getAbilities()) {
            Map<String, Object> abilityMap = getAbilitiesFromWeapon(ability);
            abilityMap.put(IS_SELECTED, build.getSelectedAbilitiesWeapon2().contains(ability.getId()));

            abilitiesSecondaryWeapon.add(abilityMap);
        }
        secondaryWeaponMap.put(ABILITIES, abilitiesSecondaryWeapon);

        scaleWithAttributeList = new ArrayList<>();
        for (Attribute attribute : secondaryWeapon.getAttributes()) {
            if (attribute.getScaleFactor() != 0) {
                Map<String, Object> attributeMap = new LinkedHashMap<>();

                attributeMap.put(NAME, attribute.getName());
                attributeMap.put(SCALE_FACTOR, attribute.getScaleFactor());

                scaleWithAttributeList.add(attributeMap);
            }
        }
        secondaryWeaponMap.put(ATTRIBUTES, scaleWithAttributeList);
        buildMap.put(SECONDARY_WEAPON, secondaryWeaponMap);

        return new ResponseEntity<>(buildMap, status);
    }

    public static ResponseEntity<Object> generateGetWeapon(HttpStatus status, Weapon weapon) {
        Map<String, Object> weaponMap = new LinkedHashMap<>();
        List<Object> abilityList = new ArrayList<>();

        weaponMap.put(ID, weapon.getId());
        weaponMap.put(NAME, weapon.getName());

        for (Ability ability : weapon.getAbilities()) {
            Map<String, Object> abilityMap = getAbilitiesFromWeapon(ability);
            abilityList.add(abilityMap);
        }
        weaponMap.put(ABILITIES, abilityList);

        return new ResponseEntity<>(weaponMap, status);
    }

    public static ResponseEntity<Object> generateGetWeapons(HttpStatus status, List<Weapon> weapons) {
        List<Object> weaponList = new ArrayList<>();

        for (Weapon weapon : weapons) {
            Map<String, Object> weaponMap = new LinkedHashMap<>();

            weaponMap.put(ID, weapon.getId());
            weaponMap.put(NAME, weapon.getName());

            weaponList.add(weaponMap);
        }

        return new ResponseEntity<>(weaponList, status);
    }

    private static Map<String, Object> getAbilitiesFromWeapon(Ability ability) {
        Map<String, Object> abilityMap = new LinkedHashMap<>();

        abilityMap.put(ID, ability.getId());
        abilityMap.put(NAME, ability.getName());
        abilityMap.put(DESCRIPTION, ability.getDescription());
        abilityMap.put(IMAGE_URL, ability.getImageUrl());
        abilityMap.put(CATEGORY, ability.getCategory());
        abilityMap.put(COLOR, ability.getColor());

        return abilityMap;
    }
}
