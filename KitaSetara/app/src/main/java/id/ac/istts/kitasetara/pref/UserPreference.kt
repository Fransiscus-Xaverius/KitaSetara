package id.ac.istts.kitasetara.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import id.ac.istts.kitasetara.model.forum.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: User) {
        //.edit untuk memperbarui data ke dalam datastore
        //cara perbarui nilai seperti update nilai array
        dataStore.edit { preferences ->

            preferences[USER_ID] = user.id!!
            preferences[USERNAME] = user.username
            preferences[NAME] = user.name
            preferences[PASSWORD] = user.password
            preferences[EMAIL_KEY] = user.email
            preferences[IS_LOGIN_KEY] = true //login status
            preferences[IMAGE] = user.imageUrl!!
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[USER_ID] ?: "",
                preferences[USERNAME] ?:"",
                preferences[NAME] ?:"",
                preferences[PASSWORD] ?:"",
                preferences[EMAIL_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
                preferences[IMAGE] ?:""
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    //singleton supaya instance bisa diakses dari mana saja
    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        //tentukan nilai bertipe data apa saja yang dapat disimpan dalam datastore
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val USER_ID = stringPreferencesKey("id")
        private val USERNAME = stringPreferencesKey("username")
        private val NAME = stringPreferencesKey("name")
        private val PASSWORD = stringPreferencesKey("password")
        private val IMAGE = stringPreferencesKey("image")
        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}