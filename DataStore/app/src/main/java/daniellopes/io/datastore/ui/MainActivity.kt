package daniellopes.io.datastore.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import daniellopes.io.datastore.databinding.ActivityMainBinding
import daniellopes.io.datastore.other.Constants.AGE
import daniellopes.io.datastore.other.Constants.FEMALE
import daniellopes.io.datastore.other.Constants.MALE
import daniellopes.io.datastore.other.Constants.NAME
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "myData")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        binding.apply {
            btSave.setOnClickListener {
                lifecycleScope.launch {
                    val name = etName.text.toString()
                    val age = etAge.text.toString().toInt()
                    val male = cbMale.isChecked
                    val female = cbFemale.isChecked
                    saveData(name, age, male, female)
                }
            }

            btLoad.setOnClickListener {
                lifecycleScope.launch {
                    readData()
                }
            }
        }

    }

    private suspend fun saveData(name: String, age: Int, male: Boolean, female: Boolean) {
        val keyName = stringPreferencesKey(NAME)
        val keyAge = intPreferencesKey(AGE)
        val keyMale = booleanPreferencesKey(MALE)
        val keyFemale = booleanPreferencesKey(FEMALE)
        applicationContext.dataStore.edit { myData ->
            myData[keyName] = name
            myData[keyAge] = age
            myData[keyMale] = male
            myData[keyFemale] = female
        }
    }

    private suspend fun readData() {
        val keyName = stringPreferencesKey(NAME)
        val keyAge = intPreferencesKey(AGE)
        val keyMale = booleanPreferencesKey(MALE)
        val keyFemale = booleanPreferencesKey(FEMALE)
        return applicationContext.dataStore.data.collect { preferences ->
            val name = preferences[keyName]
            val age = preferences[keyAge]
            val male = preferences[keyMale]
            val female = preferences[keyFemale]
            binding.apply {
                etName.setText(name)
                etAge.setText(age.toString())
                cbMale.isChecked = male ?: false
                cbFemale.isChecked = female ?: false
            }
        }
    }
}