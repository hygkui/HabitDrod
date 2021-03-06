package com.ghoulmind.habitica

import android.content.Context
import com.ghoulmind.habitdrod.R
import khttp.get
import khttp.post
import org.json.JSONArray
import org.json.JSONObject

class HabiticaClient {
    var api_base: String = "https://habitica.com/api/v3"

    constructor(api_base: String? = null) {
        if (api_base != null) {
            this.api_base = api_base
        }
    }

    fun endpoint(path: String): String {
        return "$api_base$path"
    }

    fun login(username: String, password: String): JSONObject {
        val result = post(endpoint("/user/auth/local/login"), data = mapOf("username" to username, "password" to password)).jsonObject

        if (result.getBoolean("success")) {
            return result.getJSONObject("data")
        } else {
            val error = result.getString("error")
            val message = result.getString("message")
            throw HabiticaException("$error: $message")
        }
    }

    fun getTasks(apiKey: String, apiUser: String): JSONArray {

        val result = get(endpoint("/tasks/user"), headers = mapOf("x-api-key" to apiKey, "x-api-user" to apiUser)).jsonObject

        if (result.getBoolean("success")) {
            var data = result.getJSONArray("data")
            return data
        } else {
            val error = result.getString("error")
            val message = result.getString("message")
            throw HabiticaException("$error: $message")
        }
    }
}