package com.example.evaluacion_t2.ui.Reporte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;



public class ReporteFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReporteViewModel homeViewModel =
                new ViewModelProvider(this).get(ReporteViewModel.class);



        return  null;
    }


}