package com.example.liftvectr.database;

import androidx.room.TypeConverter;

import com.example.liftvectr.data.IMUData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

// For converting types that aren't compatible with the database
// Android TypeConverter documentation
// https://developer.android.com/training/data-storage/room/referencing-data
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static ArrayList<IMUData> jsonToIMUDataArrayList(String json) {
        if (json == null) {
            return null;
        }
        Type listType = new TypeToken<ArrayList<IMUData>>() {}.getType();
        return new Gson().fromJson(json, listType);
    }

    @TypeConverter
    public static String IMUDataArrayListToJson(ArrayList<IMUData> list) {
        if (list == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<IMUData>>() {}.getType();
        return gson.toJson(list, listType);
    }

    @TypeConverter
    public static String floatToString(float value) {
        return String.valueOf(value);
    }

    @TypeConverter
    public static float stringToFloat(String value) {
        return Float.parseFloat(value);
    }

    @TypeConverter
    public static ArrayList<Float> jsonToFloatArrayList(String json) {
        if (json == null) {
            return null;
        }
        Type listType = new TypeToken<ArrayList<Float>>() {}.getType();
        return new Gson().fromJson(json, listType);
    }

    @TypeConverter
    public static String floatArrayListToJson(ArrayList<Float> list) {
        if (list == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Float>>() {}.getType();
        return gson.toJson(list, listType);
    }

    @TypeConverter
    public static ArrayList<ArrayList<Float>> jsonToFloatArrayArrayList(String json) {
        if (json == null) {
            return null;
        }
        Type listType = new TypeToken<ArrayList<ArrayList<Float>>>() {}.getType();
        return new Gson().fromJson(json, listType);
    }

    @TypeConverter
    public static String floatArrayArrayListToJson(ArrayList<ArrayList<Float>> list) {
        if (list == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<ArrayList<Float>>>() {}.getType();
        return gson.toJson(list, listType);
    }

}