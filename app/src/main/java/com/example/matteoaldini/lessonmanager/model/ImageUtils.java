package com.example.matteoaldini.lessonmanager.model;

import android.widget.ImageView;

import com.example.matteoaldini.lessonmanager.R;

/**
 * Created by matteo.aldini on 08/05/2015.
 */
public class ImageUtils {
    public static void setImageFromPosition(ImageView image, int position){
        switch (position){
            case 0:
                image.setImageResource(R.drawable.person_blue_0f00b0);
                break;
            case 1:
                image.setImageResource(R.drawable.person_light_blue_0073bd);
                break;
            case 2:
                image.setImageResource(R.drawable.person_cyano_465974);
                break;
            case 3:
                image.setImageResource(R.drawable.person_dark_green_569a4a);
                break;
            case 4:
                image.setImageResource(R.drawable.person_green_3eb900);
                break;
            case 5:
                image.setImageResource(R.drawable.person_magenta_bb003c);
                break;
            case 6:
                image.setImageResource(R.drawable.person_olive_green_6abd00);
                break;
            case 7:
                image.setImageResource(R.drawable.person_orange_bd4a00);
                break;
            case 8:
                image.setImageResource(R.drawable.person_purple_bd00a9);
                break;
            case 9:
                image.setImageResource(R.drawable.person_red_bd0500);
                break;
            case 10:
                image.setImageResource(R.drawable.person_sand_756147);
                break;
            case 11:
                image.setImageResource(R.drawable.person_turquoise_00bda1);
                break;
            case 12:
                image.setImageResource(R.drawable.person_violet_5400bc);
                break;
            case 13:
                image.setImageResource(R.drawable.person_yellow_cbb81d);
                break;
            case 14:
                image.setImageResource(R.drawable.person_sky_blue_00b1bc);
                break;
        }
    }

    public static void setImageSubject(ImageView img, String s){
        switch (s){
            case "Biology":
                img.setImageResource(R.drawable.biology);
                break;
            case "Chemistry":
                img.setImageResource(R.drawable.chemistry);
                break;
            case "Computer Science":
                img.setImageResource(R.drawable.computerscience);
                break;
            case "Economy":
                img.setImageResource(R.drawable.economy);
                break;
            case "English":
                img.setImageResource(R.drawable.english);
                break;
            case "Geography":
                img.setImageResource(R.drawable.geography);
                break;
            case "Greek":
                img.setImageResource(R.drawable.greek);
                break;
            case "History":
                img.setImageResource(R.drawable.history);
                break;
            case "Italian":
                img.setImageResource(R.drawable.italian);
                break;
            case "Languages":
                img.setImageResource(R.drawable.language);
                break;
            case "Latin":
                img.setImageResource(R.drawable.latin);
                break;
            case "Law":
                img.setImageResource(R.drawable.law);
                break;
            case "Maths":
                img.setImageResource(R.drawable.maths);
                break;
            case "Music":
                img.setImageResource(R.drawable.music);
                break;
            case "Physics":
                img.setImageResource(R.drawable.physics);
                break;
            case "Science":
                img.setImageResource(R.drawable.science);
                break;
        }
    }
}
