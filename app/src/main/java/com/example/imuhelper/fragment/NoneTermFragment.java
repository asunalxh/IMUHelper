package com.example.imuhelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.imuhelper.R;
import com.example.imuhelper.activities.AddTermActivity;
import com.example.imuhelper.utils.IntentTool;
import com.example.imuhelper.utils.ProgressDialog;
import com.example.imuhelper.utils.TipDialog;


/**
 * 课程不存在时显示的碎片
 */
public class NoneTermFragment extends Fragment {

    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noneterm, container, false);
        button = view.findViewById(R.id.noneTerm_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddTermActivity.class);
                getActivity().startActivityForResult(intent, IntentTool.NoneTerm_To_AddTerm);
            }
        });
    }


}
