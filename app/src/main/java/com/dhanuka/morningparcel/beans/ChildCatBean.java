package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChildCatBean implements Serializable {


    @SerializedName("GroupId")
    String GroupId;
    @SerializedName("Description")
    String Description;
    @SerializedName("ImageName")
    String ImageName;
    @SerializedName("Weight")
    String Weight;
    @SerializedName("GroupType")
    String GroupType;
    @SerializedName("filepath")
    String filepath;
    @SerializedName("IsSubSubGroup")
    String IsSubSubGroup;

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getGroupType() {
        return GroupType;
    }

    public void setGroupType(String groupType) {
        GroupType = groupType;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getIsSubSubGroup() {
        return IsSubSubGroup;
    }

    public void setIsSubSubGroup(String isSubSubGroup) {
        IsSubSubGroup = isSubSubGroup;
    }
}
