package com.example.aishwarya.monvoix;

/**
 * Created by aishwarya on 19/02/17.
 */

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import static com.example.aishwarya.monvoix.R.id.imageView;


public class tab1ttg extends Fragment {
    ImageButton imageButton;
    EditText edittext;
    ImageView iv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1ttg, container, false);
        imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
        edittext=(EditText) rootView.findViewById(R.id.editText2);
        iv=(ImageView) rootView.findViewById(imageView);
        addListenerOnButton();
        return rootView;
    }
    public void addListenerOnButton() {



        imageButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View arg0) {

                Context context=iv.getContext();
                String image_name=edittext.getText().toString();
                // Resources resources = getContext().getResources();
                int resourceId = context.getResources().getIdentifier(image_name,"drawable",context.getPackageName());
                //Drawable drawable=resources.getDrawable(resourceId, getContext().getTheme());
                iv.setImageResource(resourceId);
                //to retrieve image

            }

        });


    }}



