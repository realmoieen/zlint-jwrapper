package com.moieen.zlint.jwrapper;


import com.google.gson.Gson;

public class Lint {
    private String name;
    private String description;
    private String citation;
    private Source source;
    private String result;

    /**
     * Parse the json string to {@link Lint}
     *
     * @param jsonString json to be converted into {@link Lint}
     * @return resultant object
     */
    public static Lint fromJson(String jsonString) {
        return new Gson().fromJson(jsonString, Lint.class);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public String getCitation() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    /**
     * Return json string representation of this object
     *
     * @return
     */
    public String toJsonString() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}
