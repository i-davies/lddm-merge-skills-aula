package com.fatec.lddm_merge_skills.db

import io.github.cdimascio.dotenv.dotenv
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseFactory {
    val client: SupabaseClient by lazy {
        val dotenv = dotenv()

        createSupabaseClient(
            supabaseUrl = dotenv["SUPABASE_URL"] ?: "",
            supabaseKey = dotenv["SUPABASE_KEY"] ?: ""
        ) {
            install(Postgrest)
        }
    }
}