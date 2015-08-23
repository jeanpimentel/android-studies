package br.com.jeanpimentel.usergenerator.fragments;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.seismic.ShakeDetector;

import br.com.jeanpimentel.usergenerator.R;
import br.com.jeanpimentel.usergenerator.model.User;
import br.com.jeanpimentel.usergenerator.repository.UserRepository;

public class UserFragment extends Fragment implements ShakeDetector.Listener {

    TextView name;
    TextView email;
    TextView username;
    TextView password;
    ImageView photo;

    boolean isLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = (TextView)view.findViewById(R.id.name);
        email = (TextView)view.findViewById(R.id.email);
        username = (TextView)view.findViewById(R.id.username);
        password = (TextView)view.findViewById(R.id.password);
        photo = (ImageView)view.findViewById(R.id.image);
    }

    @Override
    public void onStart() {
        super.onStart();

        SensorManager sensorManager = (SensorManager)this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.setSensitivity(ShakeDetector.SENSITIVITY_LIGHT);
        sd.start(sensorManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadUser();
    }

    @Override
    public void hearShake() {
        loadUser();
    }

    protected void loadUser() {

        // para evitar multiplas chamadas simultaneamente
        if (isLoading) {
            return;
        }

        isLoading = true;

        name.setText("Loading...");
        email.setText("");
        username.setText("");
        password.setText("");
        photo.setImageResource(android.R.color.transparent);

        UserRepository.getUser(getContext(), new FutureCallback<User>() {
            @Override
            public void onCompleted(Exception e, User result) {
                isLoading = false;
                if (result != null) {

                    Ion.with(getContext()).load(result.photo).intoImageView(photo);

                    name.setText(result.name);
                    email.setText(result.email);
                    username.setText(result.username);
                    password.setText(result.password);
                }
            }
        });

    }
}
