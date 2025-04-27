package com.example.snapjer1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Recipe implements Serializable {

    private String recipeID;
    private String recipeName;
    private String recipeType;
    private String recipeDescription;
    private String recipeImage;
    private String recipeDifficulty;
    private String recipeCookTime;
    private Map<String, Boolean> recipeIngredients;
    private List<String> recipeIngredientsWithMeasurement;
    private List<String> recipeSteps;


    public Recipe() {}

    public Recipe(String recipeID, String recipeName, String recipeType, String recipeDescription, String recipeImage,
                  List<String> recipeIngredientsWithMeasurement, List<String> recipeSteps, String recipeDifficulty, String recipeCookTime){
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.recipeType = recipeType;
        this.recipeDescription = recipeDescription;
        this.recipeImage = recipeImage;
        this.recipeIngredientsWithMeasurement = recipeIngredientsWithMeasurement;
        this.recipeSteps = recipeSteps;
        this.recipeCookTime = recipeCookTime;
        this.recipeDifficulty = recipeDifficulty;
    }



    public String getRecipeName() {
        return recipeName;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public String getRecipeImageURL() { return recipeImage; }

    public String getRecipeDifficulty() { return recipeDifficulty; }

    public String getRecipeCookTime() { return recipeCookTime; }

    public Map<String, Boolean> getRecipeIngredients() {
        return recipeIngredients;
    }

    public List<String> getRecipeIngredientsWithMeasurement() {
        return recipeIngredientsWithMeasurement;
    }

    public List<String> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }
    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }



}

