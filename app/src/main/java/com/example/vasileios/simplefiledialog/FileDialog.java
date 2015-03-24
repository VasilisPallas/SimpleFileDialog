package com.example.vasileios.simplefiledialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class FileDialog extends Activity {

    private ExpandableHeightListView listView;

    private ListViewAdapter customListAdapter;

    // Source path
    private File rootPath = new File(Environment.getExternalStorageDirectory() + "");

    private ArrayList<ListItems> imageItems;
    private ArrayList<String> fileNames = new ArrayList<String>();

    // Check if the first level of the directory structure is the one showing
    private Boolean isTopParent = true;

    private File selectedFile;

    private String chosenFile;

    // Stores names of traversed directories
    private ArrayList<String> dirNames = new ArrayList<String>();

    private Bitmap bitmap;

    private LinearLayout menu;

    EditText searchText;

    ImageView deleteSearchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_dialog);

        listView = (ExpandableHeightListView) findViewById(R.id.phone_files);
        listView.setExpanded(true);

        menu = (LinearLayout) findViewById(R.id.file_manager_menu);

        searchText = (EditText) findViewById(R.id.search_text);

        searchText.addTextChangedListener(searchListView);

        loadFiles(null);
        fillList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_dialog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFiles(final String s) {
        try {
            rootPath.mkdirs();
        } catch (SecurityException e) {
            Log.e("Error", "unable to write on the SD card.");
        }


        // Checks if rootPath exists
        if (rootPath.exists()) {

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    if (s == null) {
                        // Filters based on whether the file is hidden or not
                        return (sel.isFile() || sel.isDirectory())
                                && !sel.isHidden();
                    }
                    return (sel.isFile() || sel.isDirectory())
                            && !sel.isHidden() && sel.getName().toLowerCase().contains(s.toLowerCase());
                }
            };

            String[] fileList = rootPath.list(filter);
            imageItems = new ArrayList<ListItems>();
            for (int i = 0; i < fileList.length; i++) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.file_icon);
                imageItems.add(i, new ListItems(bitmap, fileList[i]));

                // Convert into file rootPath
                File selelected = new File(rootPath, fileList[i]);
                String extension = selelected.getName().substring(selelected.getName().indexOf('.') + 1).toLowerCase();

                if (selelected.isDirectory()) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.directory);
                    imageItems.set(i, new ListItems(bitmap, fileList[i]));
                } else if (extension.equals("pdf")) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pdf);
                    imageItems.set(i, new ListItems(bitmap, fileList[i]));
                } else if (extension.equals("doc") || extension.equals("docx")) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.word);
                    imageItems.set(i, new ListItems(bitmap, fileList[i]));
                } else if (extension.equals("xls") || extension.equals("xlsx")) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.excel);
                    imageItems.set(i, new ListItems(bitmap, fileList[i]));
                } else if (extension.equals("ppt") || extension.equals("pptx")) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.powerpoint);
                    imageItems.set(i, new ListItems(bitmap, fileList[i]));
                } else if (extension.equals("wma") || extension.equals("wav") || extension.equals("mp3") || extension.equals("mid")
                        || extension.equals("m4a")) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.music);
                    imageItems.set(i, new ListItems(bitmap, fileList[i]));
                } else if (extension.equals("extension") || extension.equals("zip") || extension.equals("tar")) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rar);
                    imageItems.set(i, new ListItems(bitmap, fileList[i]));
                } else if (extension.equals("apk")) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
                    imageItems.set(i, new ListItems(bitmap, fileList[i]));
                } else if (extension.equals("gif") || extension.equals("jpeg") || extension.equals("jpg") || extension.equals("jif")
                        || extension.equals("jfif") || extension.equals("jp2") || extension.equals("jpx") || extension.equals("j2k")
                        || extension.equals("j2c") || extension.equals("fpx") || extension.equals("png") || extension.equals("dng")) {

                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.file_manager_image);
                    imageItems.set(i, new ListItems(bitmap, fileList[i]));
                } else if (extension.equals("mkv") || extension.equals("flv") || extension.equals("ogv") || extension.equals("ogg")
                        || extension.equals("avi") || extension.equals("mov") || extension.equals("wmv") || extension.equals("mp4")
                        || extension.equals("m4p ") || extension.equals("m4v ") || extension.equals("mpg ")
                        || extension.equals("mp2 ") || extension.equals("mpeg")) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.file_manager_video);
                    imageItems.set(i, new ListItems(bitmap, fileList[i]));
                } else {
                    Log.i("", "Other File");
                }

            }


            if (!isTopParent) {
                ArrayList temp = new ArrayList();
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.directory);
                temp.add(0, new ListItems(bitmap, ".."));
                for (int i = 0; i < imageItems.size(); i++) {
                    temp.add(i + 1, imageItems.get(i));
                }

                imageItems = temp;
            }

        } else

        {
            Log.e("Error", "rootPath does not exists");
        }

        Collections.sort(imageItems);

        customListAdapter = new ListViewAdapter(this, R.layout.list_row, imageItems, fileNames);

        listView.setAdapter(customListAdapter);
    }

    private void fillList() {

        if (imageItems == null) {
            return;
        }

        listView.setAdapter(customListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                chosenFile = imageItems.get(position).getTitle();
                selectedFile = new File(rootPath + "/" + chosenFile);

                if (selectedFile.isDirectory() && !chosenFile.equalsIgnoreCase("..")) {
                    isTopParent = false;

                    // Adds chosen directory to list
                    dirNames.add(chosenFile);
                    imageItems = null;
                    rootPath = new File(selectedFile + "");


                    loadFiles(null);

                    listView.setAdapter(null);
                    listView.setAdapter(customListAdapter);
                }// Checks if '..' was clicked
                else if (chosenFile.equalsIgnoreCase("..")) {
                    backButtonPresses();
                } // Do something with the selected file here
                else {

                    Toast.makeText(getApplicationContext(), "You have selected " + rootPath + "/" + chosenFile, Toast.LENGTH_SHORT).show();
                }

                if (!searchText.getText().equals("")) {
                    searchText.setText("");
                    deleteSearchIcon.setVisibility(View.GONE);
                }
            }
        });
    }

    public void showOptions(View v) {
        if (menu.getVisibility() == View.VISIBLE) {
            menu.bringToFront();
            hideView(menu);
            listView.setEnabled(true);
        } else {
            menu.bringToFront();
            showView(menu);
            listView.setEnabled(false);
        }
    }

    private void hideView(final View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out);

        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }
        });

        view.startAnimation(animation);
    }

    private void showView(final View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in);

        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });

        view.startAnimation(animation);
    }

    public void changePath(View v) {

        if (!searchText.getText().equals("")) {
            searchText.setText("");
            deleteSearchIcon.setVisibility(View.GONE);
        }
        isTopParent = true;
        switch (v.getId()) {
            case R.id.internal_storage_layout:
                rootPath = new File(Environment.getExternalStorageDirectory() + "");
                loadFiles(null);
                listView.setAdapter(customListAdapter);
                hideView(menu);
                listView.setEnabled(true);
                break;
            case R.id.sdcard_storage_layout:
                if (android.os.Build.DEVICE.contains("samsung")
                        || android.os.Build.MANUFACTURER.contains("samsung")) {
                    rootPath = new File("/mnt/extSdCard/");
                } else {
                    rootPath = new File("/mnt/external_sd/");
                }
                if (rootPath.exists()) {
                    loadFiles(null);
                    listView.setAdapter(customListAdapter);
                    hideView(menu);
                    listView.setEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(), "You don't have a SD card", Toast.LENGTH_SHORT).show();
                    rootPath = new File(Environment.getExternalStorageDirectory() + "");
                    loadFiles(null);
                    listView.setAdapter(customListAdapter);
                    listView.setEnabled(true);
                }
                break;
            default:
                break;
        }

    }

    public void deleteSearchText(View v) {
        searchText.setText("");
    }

    private TextWatcher searchListView = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            deleteSearchIcon = (ImageView) findViewById(R.id.delete_search_icon);
            if (count > 0) {
                deleteSearchIcon.setVisibility(View.VISIBLE);
            } else {
                deleteSearchIcon.setVisibility(View.GONE);
            }

            loadFiles(s.toString().trim());

            //fillList();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void backButtonPresses()
    {
        if (!isTopParent) {
            // present directory removed from list
            String s = dirNames.remove(dirNames.size() - 1);

            // rootPath modified to exclude present directory
            rootPath = new File(rootPath.toString().substring(0,
                    rootPath.toString().lastIndexOf(s)));
            imageItems = null;

            // if there are no more directories in the list, then
            // its the first level

            if (dirNames.isEmpty() || rootPath.equals(Environment.getExternalStorageDirectory() + "")
                    || rootPath.equals("/mnt/extSdCard/")
                    || rootPath.equals("/mnt/external_sd/")) {
                isTopParent = true;
            }

            loadFiles(null);

            listView.setAdapter(null);
            listView.setAdapter(customListAdapter);
        }else
        {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        backButtonPresses();
    }
}
