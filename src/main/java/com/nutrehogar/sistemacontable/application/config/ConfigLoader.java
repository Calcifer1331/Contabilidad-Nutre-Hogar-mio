package com.nutrehogar.sistemacontable.application.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;

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
    public static final String DIR_PROGRAM_NAME = "SistemaContable";
    public static final String DB_NAME = "sistemacontable.sqlite";
    public static final String DIR_BACKUP_NAME = "backup";
    public static final String DIR_LOG_NAME = "logs";
    public static final String DIR_REPORTS_NAME = "reports";
    public static final String DIR_REPORTS_TEMPLATE_NAME = "template";
    public static final String DIR_REPORTS_TEMPLATE_IMG_NAME = "img";
    public static final String DIR_PAYMENT_VOUCHER_NAME = "payment_voucher";
    public static final String DIR_REGISTRATION_FORM_NAME = "registration_form";

    static {
        createDirectories(
                getAbsoluteProgramPath(),
                getBackupPath(),
                getLogsPath(),
                getReportsPath(),
                getRegistrationFormPath(),
                getPaymentVoucherPath(),
                getReportsTemplatePath(),
                getReportsTemplateImgPath()
        );
    }

    private static void createDirectories(String @NotNull ... paths) {
        for (String path : paths) {
            createDirectory(path);
        }
    }

    private ConfigLoader() {
        throw new IllegalStateException("Configure Class");
    }


    /**
     * Crea un directorio en la ruta especificada si no existe.
     *
     * @param path Ruta del directorio a crear.
     */
    private static void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Directorias guardada con exito: " + path + ".");
            }
        }
    }

    public static @NotNull String getAbsoluteProgramPath() {
        return System.getProperty("user.home") + File.separator + DIR_PROGRAM_NAME;
    }

    /**
     * Obtiene la ruta completa de la base de datos.
     *
     * @return Ruta de la base de datos.
     */
    public static @NotNull String getDbPath() {
        return getAbsoluteProgramPath() + File.separator + DB_NAME;
    }

    /**
     * Obtiene la ruta completa para los respaldos de la base de datos.
     *
     * @return Ruta de los respaldos.
     */
    public static @NotNull String getBackupPath() {
        return getAbsoluteProgramPath() + File.separator + DIR_BACKUP_NAME;
    }

    public static @NotNull String getLogsPath() {
        return getAbsoluteProgramPath() + File.separator + DIR_LOG_NAME;
    }

    public static @NotNull String getReportsPath() {
        return getAbsoluteProgramPath() + File.separator + DIR_REPORTS_NAME;
    }

    public static @NotNull String getPaymentVoucherPath() {
        return getReportsPath() + File.separator + DIR_PAYMENT_VOUCHER_NAME;
    }

    public static @NotNull String getRegistrationFormPath() {
        return getReportsPath() + File.separator + DIR_REGISTRATION_FORM_NAME;
    }
    public static @NotNull String getReportsTemplatePath() {
        return getReportsPath() + File.separator + DIR_REPORTS_TEMPLATE_NAME;
    }
    public static @NotNull String getReportsTemplateImgPath() {
        return getReportsTemplatePath() + File.separator + DIR_REPORTS_TEMPLATE_IMG_NAME+ File.separator;
    }
}
