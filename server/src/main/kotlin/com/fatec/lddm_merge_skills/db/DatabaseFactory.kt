package com.fatec.lddm_merge_skills.db

import io.github.cdimascio.dotenv.dotenv
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun init() {
        val dotenv = dotenv()
        val dbUrl = dotenv["DB_URL"] ?: "jdbc:postgresql://localhost:5432/mergeskills"
        val dbUser = dotenv["DB_USER"] ?: "devuser"
        val dbPassword = dotenv["DB_PASSWORD"] ?: "devpassword"

        println("Conectando ao banco: $dbUrl")

        // Início da etapa de verificação de esquema
        val flyway = Flyway.configure()
            .dataSource(dbUrl, dbUser, dbPassword)
            .locations("classpath:com/fatec/lddm_merge_skills/db/migration")
            .baselineOnMigrate(true)
            .load()

        // Caso necessário reparar
        //flyway.repair()

        val result = flyway.migrate()
        println("Flyway executou: ${result.migrationsExecuted} relatórios")

        // Ligar o Exposed ao contexto migrado
        Database.connect(
            url = dbUrl,
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )
    }
}