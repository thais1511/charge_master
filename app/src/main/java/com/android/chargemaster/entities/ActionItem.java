package com.android.chargemaster.entities;

public class ActionItem {
    private int icon;
    private String name;
    private String description;
    private String processText;

    public ActionItem() {
    }

    public ActionItem(int icon, String name, String description, String processText) {
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.processText = processText;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProcessText() {
        return processText;
    }

    public void setProcessText(String processText) {
        this.processText = processText;
    }
}
