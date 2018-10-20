package com.example.kevin.witness

import com.example.kevin.witness.Utils.currentUtcTime8601
import com.goebl.david.Webb
import java.net.URL

class AzureParams(connectionString: String) {
    val proto: String
    val accountName: String
    val key: String
    val endpointSuffix: String

    init {
        val params = mutableMapOf<String, String>()
        for (param in connectionString.split(';')) {
            val (name, value) = param.split('=', ignoreCase = false, limit = 1)
            params[name] = value
        }
        proto = params["DefaultEndpointsProtocol"]!!
        accountName = params["AccountName"]!!
        key = params["AccountKey"]!!
        endpointSuffix = params["EndpointSuffix"]!!
    }
}

object Keys {
    private val blobParams = AzureParams("DefaultEndpointsProtocol=https;AccountName=pplz3d;AccountKey=nNxy1m/tomLzj2XbwWbsmsgOxpkpvjsgZWvCSy5YLpqGj7vtaLRjxHaTX338KXUDwNpv/UXYFJ4bpPfghiqTMg==;EndpointSuffix=core.windows.net")

    val blobRequestor: Webb
        get() {
            val webb = Webb.create()
            webb.setDefaultHeader("Authorization", blobParams.key)
            webb.setDefaultHeader("x-ms-date", currentUtcTime8601())
            webb.setDefaultHeader("x-ms-version", "2018-10-20")
            webb.baseUri = "${blobParams.proto}://${blobParams.accountName}.${blobParams.endpointSuffix}/"
            return webb
        }
}