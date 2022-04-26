package dev.jmsaez.florafragments.view.fragments;

import static android.content.Context.UI_MODE_SERVICE;

import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import dev.jmsaez.florafragments.R;
import dev.jmsaez.florafragments.databinding.FragmentFirstBinding;
import dev.jmsaez.florafragments.model.entity.Flora;
import dev.jmsaez.florafragments.view.adapter.FloraAdapter;
import dev.jmsaez.florafragments.view.adapter.LookupClass;
import dev.jmsaez.florafragments.viewmodel.MainActivityViewModel;

public class FirstFragment extends Fragment {

    private Toolbar toolbar;
    private FloatingActionButton fabAdd, fabImg;
    private RecyclerView rvFlora;
    private SelectionTracker<Long> tracker;
    private ActionMode actionMode;
    private FloraAdapter floraAdapter;
    private MainActivityViewModel mavm;
    private MutableLiveData<ArrayList<Flora>> floraList;
    private ActionMode.Callback actionModeCallback;
    private ProgressBar progressBar;
    private UiModeManager uiModeManager;
    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initialize(View view) {

        uiModeManager = (UiModeManager) view.getContext().getSystemService(UI_MODE_SERVICE);
        toolbar = view.findViewById(R.id.toolbar);
        inflateMenu();

        progressBar = view.findViewById(R.id.progressBar);
        mavm = new ViewModelProvider(this).get(MainActivityViewModel.class);
        rvFlora = view.findViewById(R.id.rvFlora);
        rvFlora.setLayoutManager(new LinearLayoutManager(this.getContext()));
        floraAdapter = new FloraAdapter(this.getContext(), mavm);

        rvFlora.setAdapter(floraAdapter);

        navigation(view);
        createTracker();
        createObserver(view);
        observeList();


        fabAdd = view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_FirstFragment_to_addFloraFragment);
        });



        MutableLiveData<Integer> delete = mavm.getSecondDelete();
        delete.observe(FirstFragment.this, integer -> {
            Toast.makeText(getContext(), "Floras borrada(s)", Toast.LENGTH_SHORT).show();
            refreshFragment();
        });


    }

    void createTracker(){
        tracker = new SelectionTracker.Builder<>("selection-1",
                rvFlora,
                new StableIdKeyProvider(rvFlora),
                new LookupClass(rvFlora),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();

        floraAdapter.setTracker(tracker);
    }

    void createObserver(View view){
        tracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onSelectionChanged() {
                if(tracker.hasSelection()){
                    if(actionMode == null){
                        actionMode = view.startActionMode(actionModeCallback());
                    }
                    updateContextualTitle();
                } else {
                    actionMode.finish();
                }
            }
        });
    }

    void observeList(){
        mavm.getFlora();
        floraList = mavm.getFloraLiveData();
        floraList.observe(this, floras -> {
            floraAdapter.setListFlora(floras);
            progressBar.setVisibility(View.GONE);
        });
    }


    private void updateContextualTitle(){
        this.actionMode.setTitle(tracker.getSelection().size()+"");
    }

    private ActionMode.Callback actionModeCallback(){
        return actionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
                toolbar.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.opt_delete:{
                        floraAdapter.delete();
                        tracker.clearSelection();
                        actionMode.finish();
                        return true;
                    }

                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode p0) {
                toolbar.setVisibility(View.VISIBLE);
                tracker.clearSelection();
                actionMode = null;
            }
        };
    }

    public void refreshFragment(){
        NavHostFragment.findNavController(FirstFragment.this).popBackStack();
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.main_fragment);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mode_opt){
            switch (uiModeManager.getNightMode()) {
                case UiModeManager.MODE_NIGHT_YES: {
                    NightModeOFF();
                    return true;
                }
                case UiModeManager.MODE_NIGHT_NO: {
                    NightModeON();
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void NightModeON(){
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
    }

    public void NightModeOFF(){
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
    }

    void inflateMenu(){
        if (Build.VERSION.SDK_INT <= 29)
            toolbar.inflateMenu(R.menu.app_mode);
        toolbar.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));
    }


    void navigation(View view){
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

}