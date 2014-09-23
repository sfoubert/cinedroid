package fr.ippon.cinedroid.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.ippon.cinedroid.CineActivity;
import fr.ippon.cinedroid.R;
import fr.ippon.cinedroid.task.CineTask;

/**
 * Created by sfoubert on 23/09/2014.
 */
public class CineFragment extends Fragment {
        public static final String MENU_SELECTED = "menu_selected";

        public CineFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_recommandations, container, false);
            int i = getArguments().getInt(MENU_SELECTED);

        // Call Async webService

        new CineTask((CineActivity)getActivity()).execute();
            
//            String planet = getResources().getStringArray(R.array.planets_array)[i];
//
//            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
//                    "drawable", getActivity().getPackageName());
//            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
//            getActivity().setTitle(planet);
            return rootView;
        }
    }
