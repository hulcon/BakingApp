/**
 * This class is simply a POJO for holding Recipe data as well as its associated methods.
 * This class implements Parcelable which enables me to pass the whole Recipe object
 * from one activity to another easily through the {@link android.content.Intent#putExtra(java.lang.String, android.os.Parcelable)}
 * method in the source activity and calling the {@link android.content.Intent#getParcelableExtra(java.lang.String)}
 * method in the destination activity. Implementing the parcelable makes passing the recipe object
 * a whole lot easier.
 *
 * This class contains two other nested classes, one for "Steps" and the other for "Instructions" in order
 * to organise the data efficiently and maintain readability.
 **/

package com.example.irshad.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recipe implements Parcelable{
    private String id;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private double servings;
    private String image;

    private static final String TAG = Recipe.class.getSimpleName();
    public static final String RECIPE_JSON_KEY_ID = "id";
    public static final String RECIPE_JSON_KEY_NAME = "name";
    public static final String RECIPE_JSON_KEY_INGREDIENTS = "ingredients";
    public static final String RECIPE_JSON_KEY_INGREDIENTS_NAME = "ingredient";
    public static final String RECIPE_JSON_KEY_INGREDIENTS_MEASURENT_UNIT = "measure";
    public static final String RECIPE_JSON_KEY_INGREDIENTS_QUANTITY = "quantity";
    public static final String RECIPE_JSON_KEY_STEPS = "steps";
    public static final String RECIPE_JSON_KEY_STEPS_ID = "id";
    public static final String RECIPE_JSON_KEY_STEPS_SHORT_DESCRIPTION = "shortDescription";
    public static final String RECIPE_JSON_KEY_STEPS_DESCRIPTION = "description";
    public static final String RECIPE_JSON_KEY_STEPS_VIDEO_URL = "videoURL";
    public static final String RECIPE_JSON_KEY_STEPS_THUMBNAIL_URL = "thumbnailURL";
    public static final String RECIPE_JSON_KEY_SERVINGS = "servings";
    public static final String RECIPE_JSON_KEY_IMAGE = "image";

    protected Recipe(Parcel in) {
        id = in.readString();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        servings = in.readDouble();
        image = in.readString();
    }

    public Recipe(){

    }

    public static ArrayList<Recipe> createRecipeListFromJson(String stringJSONArray){
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();

        JSONArray recipeJsonArray = null;
        try {
            recipeJsonArray = new JSONArray(stringJSONArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(recipeJsonArray == null){
            return null;
        }

        for(int i=0;i<recipeJsonArray.length();i++){
            try {
                JSONObject singleRecipeObject = recipeJsonArray.getJSONObject(i);
                Recipe recipe = new Recipe();
                recipe.setId(singleRecipeObject.getString(RECIPE_JSON_KEY_ID));
                recipe.setName(singleRecipeObject.getString(RECIPE_JSON_KEY_NAME));
                recipe.setImage(singleRecipeObject.getString(RECIPE_JSON_KEY_IMAGE));
                recipe.setServings(singleRecipeObject.getDouble(RECIPE_JSON_KEY_SERVINGS));
                recipe.setIngredients(Ingredient.createIngredientsListFromJson(singleRecipeObject.getJSONArray(RECIPE_JSON_KEY_INGREDIENTS)));
                recipe.setSteps(Step.createStepsListFromJson(singleRecipeObject.getJSONArray(RECIPE_JSON_KEY_STEPS)));
                recipeArrayList.add(recipe);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG,"Found Recipes in JSON: " + recipeArrayList.size());
        return recipeArrayList;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeDouble(servings);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public double getServings() {
        return servings;
    }

    public void setServings(double servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    /* Inner class for holding information regarding ingredients */
    public static class Ingredient implements Parcelable{
        private double quantity;
        private String measurementUnit;
        private String ingredientName;



        protected Ingredient(Parcel in) {
            quantity = in.readDouble();
            measurementUnit = in.readString();
            ingredientName = in.readString();
        }

        public Ingredient(){

        }

        public static ArrayList<Ingredient> createIngredientsListFromJson(JSONArray ingredientsJsonArray){
            ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
            for(int i=0;i<ingredientsJsonArray.length();i++){
                try {
                    JSONObject singleIngredientObject = ingredientsJsonArray.getJSONObject(i);
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIngredientName(singleIngredientObject.getString(RECIPE_JSON_KEY_INGREDIENTS_NAME));
                    ingredient.setMeasurementUnit(singleIngredientObject.getString(RECIPE_JSON_KEY_INGREDIENTS_MEASURENT_UNIT));
                    ingredient.setQuantity(singleIngredientObject.getDouble(RECIPE_JSON_KEY_INGREDIENTS_QUANTITY));
                    ingredientArrayList.add(ingredient);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return ingredientArrayList;
        }

        public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
            @Override
            public Ingredient createFromParcel(Parcel in) {
                return new Ingredient(in);
            }

            @Override
            public Ingredient[] newArray(int size) {
                return new Ingredient[size];
            }
        };

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public String getMeasurementUnit() {
            return measurementUnit;
        }

        public void setMeasurementUnit(String measurementUnit) {
            this.measurementUnit = measurementUnit;
        }

        public String getIngredientName() {
            return ingredientName;
        }

        public void setIngredientName(String ingredientName) {
            this.ingredientName = ingredientName;
        }


        @Override
        public int describeContents() {
            return hashCode();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(quantity);
            dest.writeString(measurementUnit);
            dest.writeString(ingredientName);
        }
    }

    /* Inner class for holding information regarding steps for
     * preparation of the recipe
     */
    public static class Step implements Parcelable{
        private String id;
        private String shortDescription;
        private String description;
        private String videoUrl;
        private String thumbnailUrl;

        protected Step(Parcel in) {
            id = in.readString();
            shortDescription = in.readString();
            description = in.readString();
            videoUrl = in.readString();
            thumbnailUrl = in.readString();
        }

        public Step(){

        }

        public static ArrayList<Step> createStepsListFromJson(JSONArray stepsJsonArray){
            ArrayList<Step> stepArrayList = new ArrayList<>();
            for(int i=0;i<stepsJsonArray.length();i++){
                try {
                    JSONObject singleStepObject = stepsJsonArray.getJSONObject(i);
                    Step step = new Step();
                    step.setId(singleStepObject.getString(RECIPE_JSON_KEY_STEPS_ID));
                    step.setDescription(singleStepObject.getString(RECIPE_JSON_KEY_STEPS_DESCRIPTION));
                    step.setShortDescription(singleStepObject.getString(RECIPE_JSON_KEY_STEPS_SHORT_DESCRIPTION));
                    step.setThumbnailUrl(singleStepObject.getString(RECIPE_JSON_KEY_STEPS_THUMBNAIL_URL));
                    step.setVideoUrl(singleStepObject.getString(RECIPE_JSON_KEY_STEPS_VIDEO_URL));
                    stepArrayList.add(step);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return stepArrayList;
        }

        public static final Creator<Step> CREATOR = new Creator<Step>() {
            @Override
            public Step createFromParcel(Parcel in) {
                return new Step(in);
            }

            @Override
            public Step[] newArray(int size) {
                return new Step[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        @Override
        public int describeContents() {
            return hashCode();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(shortDescription);
            dest.writeString(description);
            dest.writeString(videoUrl);
            dest.writeString(thumbnailUrl);
        }
    }

}
