package ru.samitin.lesson7;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_settings,container,false);
        initView(view);
        return view;
    }

    private void initView(View view){
        initRadioReplace(view);
        initRadioAdd(view);
        initSwichBackStack(view);
        initSwichBackAsRemove(view);
        initSwichDeliteBeforeAdd(view);
    }
    private void initRadioReplace(View view){
        RadioButton radioButtonReplace = view.findViewById(R.id.radioButtonReplace);
        radioButtonReplace.setChecked(!Settings.isAddFragment);
        radioButtonReplace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.isAddFragment = !isChecked;
                writeSettings();
            }
        });

    }
    private void initRadioAdd(View view){
        RadioButton radioButtonAdd = view.findViewById(R.id.radioButtonAdd);
        radioButtonAdd.setChecked(Settings.isAddFragment);
        radioButtonAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.isAddFragment = isChecked;
                writeSettings();
            }
        });

    }
    private void initSwichBackStack(View view){
        // Элемент пользовательского интерфейса — переключатель
        // По функционалу очень похож на CheckBox, но имеет другой дизайн
        SwitchCompat switchUseBackStack = view.findViewById(R.id.switchBackStack);
        switchUseBackStack.setChecked(Settings.isBackStack);
        switchUseBackStack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.isBackStack = isChecked;
                writeSettings();
            }
        });

    }
    private void initSwichBackAsRemove(View view){
        SwitchCompat switchBackAsRemove = view.findViewById(R.id.switchBackAsRemove);
        switchBackAsRemove.setChecked(Settings.isBackAsRemove);
        switchBackAsRemove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.isBackAsRemove = isChecked;
                writeSettings();
            }
        });

    }
    private void initSwichDeliteBeforeAdd(View view){
        SwitchCompat switchDeleteBeforeAdd = view.findViewById(R.id.switchDeleteBeforeAdd);
        switchDeleteBeforeAdd.setChecked(Settings.isDeleteBeforeAdd);
        switchDeleteBeforeAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.isDeleteBeforeAdd = isChecked;
                writeSettings();
            }
        });

    }
    private void writeSettings(){
        // Специальный класс для хранения настроек
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(Settings.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        // Настройки сохраняются посредством специального класса editor
        SharedPreferences.Editor editor = sharedPref.edit();
        // Задаём значения настроек
        editor.putBoolean(Settings.IS_BACK_STACK_USED, Settings.isBackStack);
        editor.putBoolean(Settings.IS_ADD_FRAGMENT_USED, Settings.isAddFragment);
        editor.putBoolean(Settings.IS_BACK_AS_REMOVE_FRAGMENT, Settings.isBackAsRemove);
        editor.putBoolean(Settings.IS_DELETE_FRAGMENT_BEFORE_ADD, Settings.isDeleteBeforeAdd);
        // Сохраняем значения настроек
        editor.apply();

    }
}
