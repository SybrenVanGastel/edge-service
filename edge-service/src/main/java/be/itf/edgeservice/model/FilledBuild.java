package be.itf.edgeservice.model;

import java.util.ArrayList;
import java.util.List;

public class FilledBuild {

    private String name;
    private String username;
    private Tag tag;
    private List<Integer> selectedAbilitiesWeapon1 = new ArrayList<>();
    private List<Integer> selectedAbilitiesWeapon2 = new ArrayList<>();
    private List<Integer> attributeOptions = new ArrayList<>();
    private Weapon primaryWeapon;
    private Weapon secondaryWeapon;

    public FilledBuild() {
    }

    public FilledBuild(Weapon primaryWeapon, Weapon secondaryWeapon, String name, String username, Tag tag, List<Integer> selectedAbilitiesWeapon1, List<Integer> selectedAbilitiesWeapon2, List<Integer> attributeOptions) {
        this.primaryWeapon = primaryWeapon;
        this.secondaryWeapon = secondaryWeapon;
        this.name = name;
        this.username = username;
        this.tag = tag;
        this.selectedAbilitiesWeapon1 = selectedAbilitiesWeapon1;
        this.selectedAbilitiesWeapon2 = selectedAbilitiesWeapon2;
        this.attributeOptions = attributeOptions;
    }

    public Weapon getPrimaryWeapon() {
        return primaryWeapon;
    }

    public void setPrimaryWeapon(Weapon primaryWeapon) {
        this.primaryWeapon = primaryWeapon;
    }

    public Weapon getSecondaryWeapon() {
        return secondaryWeapon;
    }

    public void setSecondaryWeapon(Weapon secondaryWeapon) {
        this.secondaryWeapon = secondaryWeapon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public List<Integer> getSelectedAbilitiesWeapon1() {
        return selectedAbilitiesWeapon1;
    }

    public void setSelectedAbilitiesWeapon1(List<Integer> selectedAbilitiesWeapon1) {
        this.selectedAbilitiesWeapon1 = selectedAbilitiesWeapon1;
    }

    public List<Integer> getSelectedAbilitiesWeapon2() {
        return selectedAbilitiesWeapon2;
    }

    public void setSelectedAbilitiesWeapon2(List<Integer> selectedAbilitiesWeapon2) {
        this.selectedAbilitiesWeapon2 = selectedAbilitiesWeapon2;
    }

    public List<Integer> getAttributeOptions() {
        return attributeOptions;
    }

    public void setAttributeOptions(List<Integer> attributeOptions) {
        this.attributeOptions = attributeOptions;
    }
}
