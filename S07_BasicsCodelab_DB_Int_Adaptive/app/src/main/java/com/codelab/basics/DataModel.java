package com.codelab.basics;

// Object DB ... see Room for Android Studio
// https://developer.android.com/training/data-storage/room
public class DataModel {

    private String PokeName;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Integer getLevel() {
        return Level;
    }

    public void setLevel(Integer level) {
        Level = level;
    }

    private String Description;
    private Integer Level;
    private Integer accessCount;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    // Change to reflect Pokemon
//    private long id;
//    private String Pokemon_Name;
//    private String Pokemon_Type;
//    private Integer Pokemon_Number;
    // ...


    public DataModel() {
        this.setAccessCount(0);
        this.setPokeName("");
        this.setModelName("default modelName");
        this.setModelNumber(0);
        this.setId(0);
    }

    public DataModel(String PokeName, String Description, Integer Level, Integer accessCount, long id) {
        this.setId(id);
        this.setAccessCount(accessCount);
        this.setPokeName(PokeName);
        this.setDescription(Description);
        this.setLevel(Level);

    }

    @Override
    public String toString() {
        return "DataModel{" +
                "id=" + getId() +
                "PokeName=" + getPokeName() +
                ", Description='" + getDescription() + '\'' +
                ", Level=" + getLevel() + '\'' +
                ", accessCount" + getAccessCount();
    }
    public void setAccessCount(Integer accessCount) {
        this.accessCount = accessCount;
    }
    public Integer getAccessCount() {
        return accessCount;
    }
    public String getPokeName() {
        return PokeName;
    }

    public void setPokeName(String pokeName) {
        this.PokeName = pokeName;
    }

    public String getModelName() {
        return Description;
    }

    public void setModelName(String modelName) {
        this.Description = modelName;
    }

    public Integer getModelNumber() {
        return Level;
    }

    public void setModelNumber(Integer modelNumber) {
        this.Level = modelNumber;
    }

}
