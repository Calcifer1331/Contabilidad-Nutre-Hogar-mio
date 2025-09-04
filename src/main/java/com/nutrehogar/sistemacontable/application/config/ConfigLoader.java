package com.nutrehogar.sistemacontable.application.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Clase encargada de gestionar la carga, el almacenamiento y la inicialización de configuraciones
 * mediante el uso de un archivo de propiedades. Utiliza un patrón Singleton para garantizar que solo
 * exista una instancia de esta clase.
 * </p>
 * Las propiedades se cargan del archivo {@code config.properties} que debe estar en los recursos del programa {@code src/main/resources/}.
 * Si no existe se asignarán unas propiedades por defecto, se creará él {@code .properties} y se guardara la configuración.
 *
 * @author Calcifer1331
 */
public class ConfigLoader {
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public enum Props {
        DIR_PROGRAM_NAME(Path.of(System.getProperty("user.home"), "Sistema_Contable")),
        DB_NAME(DIR_PROGRAM_NAME.path.resolve("app_data_base.sqlite")),
        DIR_BACKUP_NAME(DIR_PROGRAM_NAME.path.resolve("backup")),
        DIR_LOG_NAME(DIR_PROGRAM_NAME.path.resolve("logs")),
        DIR_REPORTS_TEMPLATE_NAME(DIR_PROGRAM_NAME.path.resolve("template")),
        DIR_REPORTS_NAME(Path.of(System.getProperty("user.home"), ("Reportes"))),
        DIR_PAYMENT_VOUCHER_NAME(DIR_REPORTS_NAME.path.resolve("Comprobantes")),
        DIR_REGISTRATION_FORM_NAME(DIR_REPORTS_NAME.path.resolve("Formularios")),
        DIR_JOURNAL_NAME(DIR_REPORTS_NAME.path.resolve("Libro Diario")),
        DIR_TRIAL_BALANCE_NAME(DIR_REPORTS_NAME.path.resolve("Balance de Comprobación")),
        DIR_GENERAL_LEDGER_NAME(DIR_REPORTS_NAME.path.resolve("Mayor General")),;

        Path path;

    }


    private ConfigLoader() {
        throw new IllegalStateException("Configure Class");
    }

    public static void createDirectories() {
        for (Props prop : Props.values()) {
            if (prop == Props.DB_NAME) continue;
            try {
                Files.createDirectories(prop.getPath());
            } catch (IOException e) {
                System.err.println("Error al crear directorio: " + prop.getPath());
                e.printStackTrace();
            }
        }
    }

}
