package com.cegep.foodie.ui.auth;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.cegep.foodie.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {

    private static final String TAG = "SignInFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.sign_up_text).setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity instanceof AuthActivity) {
                ((AuthActivity) activity).flipCard();
            }
        });

        EditText emailInput = view.findViewById(R.id.email_input);
        EditText passwordInput = view.findViewById(R.id.password_input);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        view.findViewById(R.id.sign_in_button).setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Activity activity = getActivity();
                            if (activity instanceof AuthActivity) {
                                ((AuthActivity) activity).completeLogin();
                            }
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Sign In failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
