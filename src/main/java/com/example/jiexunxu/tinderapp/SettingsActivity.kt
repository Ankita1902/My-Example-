package com.example.jiexunxu.tinderapp


import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.widget.CompoundButtonCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Spinner

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.Serializable


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        setContentView(R.layout.activity_settings)
        initUI()
        initButtons()
    }

    override fun onStart() {
        super.onStart()
        val settings = SettingsParams()
        settings.readSettingsFromFile(this.applicationContext)
        setSettingValuesToUI(settings)
    }

    override fun onBackPressed() {
        saveAndBack()
    }

    private fun initUI() {
        title = "Custom Search"
        val layout = findViewById(R.id.settingsLayout) as ConstraintLayout
        val keywordsEditText = findViewById(R.id.keywordSearchTextbox) as EditText
        val addressEditText = findViewById(R.id.addressTextbox) as EditText
        val maxResultsSpinner = findViewById(R.id.maxResultsSpinner) as Spinner
        val sortBySpinner = findViewById(R.id.sortBySpinner) as Spinner
        val searchRangeSpinner = findViewById(R.id.searchRangeSpinner) as Spinner
        val themeSpinner = findViewById(R.id.colorThemeSpinner) as Spinner
        val saveButton = findViewById(R.id.saveAndBackButton) as Button
        val resetDefaultsButton = findViewById(R.id.resetDefaultButton) as Button
        val price1CheckBox = findViewById(R.id.price1CheckBox) as CheckBox
        val price2CheckBox = findViewById(R.id.price2CheckBox) as CheckBox
        val price3CheckBox = findViewById(R.id.price3CheckBox) as CheckBox
        val price4CheckBox = findViewById(R.id.price4CheckBox) as CheckBox
        val openNowCheckBox = findViewById(R.id.openNowCheckBox) as CheckBox
        val opts = AppOptions.getUIOptions(SettingsParams.themeID)
        layout.setBackgroundColor(applicationContext.resources.getColor(opts.backgroundColor))
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(applicationContext.resources.getColor(opts.primaryColor)))
        keywordsEditText.setBackgroundResource(opts.editTextStyle)
        addressEditText.setBackgroundResource(opts.editTextStyle)
        maxResultsSpinner.setBackgroundResource(opts.spinnerStyle)
        maxResultsSpinner.setPopupBackgroundResource(opts.spinnerPopupStyle)
        sortBySpinner.setBackgroundResource(opts.spinnerStyle)
        sortBySpinner.setPopupBackgroundResource(opts.spinnerPopupStyle)
        searchRangeSpinner.setBackgroundResource(opts.spinnerStyle)
        searchRangeSpinner.setPopupBackgroundResource(opts.spinnerPopupStyle)
        themeSpinner.setBackgroundResource(opts.spinnerStyle)
        themeSpinner.setPopupBackgroundResource(opts.spinnerPopupStyle)
        saveButton.setBackgroundResource(opts.buttonStyle)
        resetDefaultsButton.setBackgroundResource(opts.buttonStyle)
        CompoundButtonCompat.setButtonTintList(price1CheckBox, ColorStateList.valueOf(resources.getColor(opts.primaryColorDark)))
        CompoundButtonCompat.setButtonTintList(price2CheckBox, ColorStateList.valueOf(resources.getColor(opts.primaryColorDark)))
        CompoundButtonCompat.setButtonTintList(price3CheckBox, ColorStateList.valueOf(resources.getColor(opts.primaryColorDark)))
        CompoundButtonCompat.setButtonTintList(price4CheckBox, ColorStateList.valueOf(resources.getColor(opts.primaryColorDark)))
        CompoundButtonCompat.setButtonTintList(openNowCheckBox, ColorStateList.valueOf(resources.getColor(opts.primaryColorDark)))
    }

    private fun initButtons() {
        val maxResultsSpinner = findViewById(R.id.maxResultsSpinner) as Spinner
        val maxResultsItems = arrayOf("1", "5", "10", "20", "50")
        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, maxResultsItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        maxResultsSpinner.setAdapter(adapter)
        val sortBySpinner = findViewById(R.id.sortBySpinner) as Spinner
        val sortByItems = arrayOf("Best Match", "Price (low to high)", "Price (high to low)", "Distance", "Rating", "Review Count")
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortByItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortBySpinner.setAdapter(adapter)
        val searchRangeSpinner = findViewById(R.id.searchRangeSpinner) as Spinner
        val searchRangeItems = arrayOf("1", "5", "10", "25")
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, searchRangeItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchRangeSpinner.setAdapter(adapter)
        val saveButton = findViewById(R.id.saveAndBackButton) as Button
        val resetDefaultsButton = findViewById(R.id.resetDefaultButton) as Button
        val themeSpinner = findViewById(R.id.colorThemeSpinner) as Spinner
        val themeSpinnerItems = arrayOf("Default", "Ever Green", "Deep Blue", "Crimson Red")
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, themeSpinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        themeSpinner.setAdapter(adapter)

        saveButton.setOnClickListener(View.OnClickListener { saveAndBack() })

        resetDefaultsButton.setOnClickListener(View.OnClickListener {
            val settings = SettingsParams()
            settings.getDefaultSettings()
            setSettingValuesToUI(settings)
        })
    }

    private fun saveAndBack() {
        val settings = UIToSettingsParams()
        settings.writeSettingsToFile(this.applicationContext)
        val intent = Intent(this@SettingsActivity, MainActivity::class.java)
        this@SettingsActivity.startActivity(intent)
    }

    private fun UIToSettingsParams(): SettingsParams {
        val keywordsText = findViewById(R.id.keywordSearchTextbox) as EditText
        val addressText = findViewById(R.id.addressTextbox) as EditText
        val maxResultsSpinner = findViewById(R.id.maxResultsSpinner) as Spinner
        val sortBySpinner = findViewById(R.id.sortBySpinner) as Spinner
        val searchRangeSpinner = findViewById(R.id.searchRangeSpinner) as Spinner
        val price1CheckBox = findViewById(R.id.price1CheckBox) as CheckBox
        val price2CheckBox = findViewById(R.id.price2CheckBox) as CheckBox
        val price3CheckBox = findViewById(R.id.price3CheckBox) as CheckBox
        val price4CheckBox = findViewById(R.id.price4CheckBox) as CheckBox
        val mustBeOpenCheckBox = findViewById(R.id.openNowCheckBox) as CheckBox
        val themeSpinner = findViewById(R.id.colorThemeSpinner) as Spinner

        val settings = SettingsParams()
        settings.keywords = keywordsText.getText().toString()
        settings.address = addressText.getText().toString()
        when (maxResultsSpinner.getSelectedItemPosition()) {
            0 -> settings.maxResults = 1
            1 -> settings.maxResults = 5
            2 -> settings.maxResults = 10
            3 -> settings.maxResults = 20
            4 -> settings.maxResults = 50
            else -> settings.maxResults = 20
        }
        when (sortBySpinner.getSelectedItemPosition()) {
            0 -> settings.sortingMethod = 0
            1 -> settings.sortingMethod = 1
            2 -> settings.sortingMethod = 2
            3 -> settings.sortingMethod = 3
            4 -> settings.sortingMethod = 4
            5 -> settings.sortingMethod = 5
            else -> settings.sortingMethod = 0
        }
        when (searchRangeSpinner.getSelectedItemPosition()) {
            0 -> settings.searchRange = 1600
            1 -> settings.searchRange = 8000
            2 -> settings.searchRange = 16000
            3 -> settings.searchRange = 40000
            else -> settings.searchRange = 8000
        }
        settings.prices[0] = price1CheckBox.isChecked()
        settings.prices[1] = price2CheckBox.isChecked()
        settings.prices[2] = price3CheckBox.isChecked()
        settings.prices[3] = price4CheckBox.isChecked()
        settings.mustBeOpenNow = mustBeOpenCheckBox.isChecked()
        when (themeSpinner.getSelectedItemPosition()) {
            0 -> SettingsParams.themeID = 0
            1 -> SettingsParams.themeID = 1
            2 -> SettingsParams.themeID = 2
            3 -> SettingsParams.themeID = 3
            else -> SettingsParams.themeID = 0
        }
        return settings
    }

    private fun setSettingValuesToUI(settings: SettingsParams) {
        val keywordsText = findViewById(R.id.keywordSearchTextbox) as EditText
        val addressText = findViewById(R.id.addressTextbox) as EditText
        val maxResultsSpinner = findViewById(R.id.maxResultsSpinner) as Spinner
        val sortBySpinner = findViewById(R.id.sortBySpinner) as Spinner
        val searchRangeSpinner = findViewById(R.id.searchRangeSpinner) as Spinner
        val price1CheckBox = findViewById(R.id.price1CheckBox) as CheckBox
        val price2CheckBox = findViewById(R.id.price2CheckBox) as CheckBox
        val price3CheckBox = findViewById(R.id.price3CheckBox) as CheckBox
        val price4CheckBox = findViewById(R.id.price4CheckBox) as CheckBox
        val mustBeOpenCheckBox = findViewById(R.id.openNowCheckBox) as CheckBox
        val themeSpinner = findViewById(R.id.colorThemeSpinner) as Spinner

        keywordsText.setText(settings.keywords)
        addressText.setText(settings.address)
        when (settings.maxResults) {
            1 -> maxResultsSpinner.setSelection(0)
            5 -> maxResultsSpinner.setSelection(1)
            10 -> maxResultsSpinner.setSelection(2)
            20 -> maxResultsSpinner.setSelection(3)
            50 -> maxResultsSpinner.setSelection(4)
            else -> maxResultsSpinner.setSelection(3)
        }
        when (settings.sortingMethod) {
            0 -> sortBySpinner.setSelection(0)
            1 -> sortBySpinner.setSelection(1)
            2 -> sortBySpinner.setSelection(2)
            3 -> sortBySpinner.setSelection(3)
            4 -> sortBySpinner.setSelection(4)
            5 -> sortBySpinner.setSelection(5)
            else -> sortBySpinner.setSelection(0)
        }
        when (settings.searchRange) {
            1600 -> searchRangeSpinner.setSelection(0)
            8000 -> searchRangeSpinner.setSelection(1)
            16000 -> searchRangeSpinner.setSelection(2)
            40000 -> searchRangeSpinner.setSelection(3)
            else -> searchRangeSpinner.setSelection(1)
        }
        price1CheckBox.setChecked(settings.prices[0])
        price2CheckBox.setChecked(settings.prices[1])
        price3CheckBox.setChecked(settings.prices[2])
        price4CheckBox.setChecked(settings.prices[3])
        mustBeOpenCheckBox.setChecked(settings.mustBeOpenNow)
        when (SettingsParams.themeID) {
            0 -> themeSpinner.setSelection(0)
            1 -> themeSpinner.setSelection(1)
            2 -> themeSpinner.setSelection(2)
            3 -> themeSpinner.setSelection(3)
            else -> themeSpinner.setSelection(0)
        }
    }
}