package com.khedr.ecommerce.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.TypeConverter;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khedr.ecommerce.database.entities.RecentlyViewedEntity;
import com.khedr.ecommerce.pojo.product.Product;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public String fromProductDBToString(RecentlyViewedEntity product){
        return new Gson().toJson(product);
    }
    @TypeConverter
    public RecentlyViewedEntity fromStringToProductDB(String product){
        return new Gson().fromJson(product, RecentlyViewedEntity.class);
    }
    @TypeConverter
    public String fromStringArrayListToString(ArrayList<String> arrayList){
        return new Gson().toJson(arrayList);
    }
    @TypeConverter
    public ArrayList<String> fromStringToStringArrayList(String images){
        return new Gson().fromJson(images, new TypeToken<ArrayList<String>>(){}.getType());
    }
    @TypeConverter
    public static String fromProductArrayListToString(ArrayList<Product> arrayList){
        return new Gson().toJson(arrayList);
    }
    @TypeConverter
    public static ArrayList<Product> fromStringToProductArrayList(String images){
        return new Gson().fromJson(images, new TypeToken<ArrayList<Product>>(){}.getType());
    }
    @TypeConverter
    public String fromProductToString(Product product){
        return new Gson().toJson(product);
    }
    @TypeConverter
    public Product fromStringToProduct(String product){
        return new Gson().fromJson(product,Product.class);
    }


    @TypeConverter
    public static String fromBitmapToString(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 50;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    @TypeConverter
    public static Bitmap fromStringToBitmap(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
