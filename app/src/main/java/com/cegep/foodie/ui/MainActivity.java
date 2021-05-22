package com.cegep.foodie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.cegep.foodie.R;
import com.cegep.foodie.ui.auth.AuthActivity;
import com.cegep.foodie.ui.chat.ChatActivity;
import com.cegep.foodie.ui.chat.room.SelectRoomActivity;
import com.cegep.foodie.ui.createrecipe.CreateRecipeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.home);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.chat) {
                    Intent intent = new Intent(MainActivity.this, SelectRoomActivity.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });

        Fragment fragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).commit();

        findViewById(R.id.add_recipe_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateRecipeActivity.class);
                startActivity(intent);
            }
        });
    }
}