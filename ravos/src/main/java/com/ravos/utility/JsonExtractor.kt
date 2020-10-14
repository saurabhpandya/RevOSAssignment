package com.ravos.utility

import android.content.Context
import org.json.JSONException
import java.io.IOException
import java.io.InputStream

class JsonExtractor(private val mContext: Context) {

    fun loadJSONFromAsset(jsonFileName: String): String? {
        var json: String? = null
        json = try {
            val inputStream: InputStream = mContext.assets.open(jsonFileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}