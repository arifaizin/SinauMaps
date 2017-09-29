package id.co.imastudio1.sinaumaps.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.widget.Toast;


public class MyFunction extends AppCompatActivity {
    Animation animation;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=MyFunction.this;
    }

    public void mytoast(String isipesan){
        Toast.makeText(this, isipesan, Toast.LENGTH_SHORT).show();
    }
    public void aksesclass(Class kelastujuan){
        startActivity(new Intent(c,kelastujuan));
    }




}
