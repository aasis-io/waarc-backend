/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.waarc.config;

/**
 *
 * @author Sachin Kumar Singh <sachin.singh@moco.com.np>
 * @Creation 24-Nov-2025 3:14:48 pm
 */
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
    public static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", "dijsedbpd",
        "api_key", "175829371611712",
        "api_secret", "652oW1cIJEX9h1pb1FXxOldzOnM"
    ));
}
