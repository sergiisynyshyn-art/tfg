package com.example.serenum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DatabaseHelper proporciona la gestión de la base de datos SQLite para la app Serenum.
 * Se encarga de crear, actualizar y gestionar la tabla de usuarios.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Constantes para el nombre de la BD y versión
    private static final String DATABASE_NAME = "serenum.db";
    private static final int DATABASE_VERSION = 2;  // ← Aumentado para migración

    // Constantes para la tabla de usuarios
    private static final String TABLE_USUARIOS = "usuarios";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";  // ← NUEVA COLUMNA
    private static final String COLUMN_GOOGLE_ID = "googleId";
    private static final String COLUMN_TIPO = "tipo";
    private static final String COLUMN_FECHA_REGISTRO = "fechaRegistro";

    /**
     * Constructor del DatabaseHelper
     *
     * @param context Contexto de la aplicación
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Se ejecuta cuando se crea la BD por primera vez.
     * Crea la estructura de la tabla usuarios.
     *
     * @param db Base de datos SQLite
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Consulta SQL para crear la tabla de usuarios
        String createTableSQL = "CREATE TABLE " + TABLE_USUARIOS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +  // ← Nueva columna en la creación
                COLUMN_GOOGLE_ID + " TEXT UNIQUE NOT NULL, " +
                COLUMN_TIPO + " TEXT DEFAULT 'usuario', " +
                COLUMN_FECHA_REGISTRO + " TEXT NOT NULL)";

        db.execSQL(createTableSQL);
    }

    /**
     * Se ejecuta cuando se actualiza la versión de la BD.
     * Maneja la migración de la versión 1 a 2 agregando la columna password.
     *
     * @param db Base de datos SQLite
     * @param oldVersion Versión anterior de la BD
     * @param newVersion Nueva versión de la BD
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Agregar columna password a la tabla existente
            db.execSQL("ALTER TABLE " + TABLE_USUARIOS + " ADD COLUMN " + COLUMN_PASSWORD + " TEXT");
        }
    }

    /**
     * Verifica si un usuario ya existe en la BD por su email.
     *
     * @param email Email del usuario a buscar
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean usuarioExiste(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USUARIOS,
                null,
                COLUMN_EMAIL + " = ?",
                new String[]{email},
                null,
                null,
                null
        );

        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return existe;
    }

    /**
     * Inserta un nuevo usuario en la BD si no existe uno con el mismo email.
     *
     * @param nombre Nombre del usuario
     * @param email Email del usuario
     * @param googleId ID de Google del usuario
     * @return El ID del usuario insertado, -1 si el usuario ya existe o error
     */
    public long insertarUsuario(String nombre, String email, String googleId) {
        // Verificar si el usuario ya existe
        if (usuarioExiste(email)) {
            return -1; // Usuario ya existe
        }

        SQLiteDatabase db = this.getWritableDatabase();

        // Crear un ContentValues para insertar los datos
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_GOOGLE_ID, googleId);
        values.put(COLUMN_TIPO, "usuario");
        values.put(COLUMN_FECHA_REGISTRO, obtenerFechaActual());

        // Insertar el nuevo usuario
        long resultado = db.insert(TABLE_USUARIOS, null, values);
        db.close();

        return resultado;
    }

    /**
     * Registra un nuevo usuario con email y contraseña.
     *
     * @param nombre Nombre del usuario
     * @param email Email del usuario
     * @param password Contraseña del usuario (se encriptará)
     * @return El ID del usuario insertado, -1 si el usuario ya existe o error
     */
    public long registrarUsuario(String nombre, String email, String password) {
        // Verificar si el usuario ya existe
        if (usuarioExiste(email)) {
            return -1; // Usuario ya existe
        }

        SQLiteDatabase db = this.getWritableDatabase();

        // Crear un ContentValues para insertar los datos
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, encriptarPassword(password)); // ← Encriptar contraseña
        values.put(COLUMN_GOOGLE_ID, ""); // Campo vacío para usuarios no de Google
        values.put(COLUMN_TIPO, "usuario");
        values.put(COLUMN_FECHA_REGISTRO, obtenerFechaActual());

        // Insertar el nuevo usuario
        long resultado = db.insert(TABLE_USUARIOS, null, values);
        db.close();

        return resultado;
    }

    /**
     * Verifica las credenciales de un usuario para login.
     *
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Array de String con [id, nombre, email] si login exitoso, null si falla
     */
    public String[] loginUsuario(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Encriptar la contraseña para comparar
        String passwordEncriptada = encriptarPassword(password);

        Cursor cursor = db.query(
                TABLE_USUARIOS,
                new String[]{COLUMN_ID, COLUMN_NOMBRE, COLUMN_EMAIL},
                COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{email, passwordEncriptada},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            String[] usuario = new String[]{
                    String.valueOf(cursor.getInt(0)), // id
                    cursor.getString(1), // nombre
                    cursor.getString(2)  // email
            };
            cursor.close();
            db.close();
            return usuario;
        }

        cursor.close();
        db.close();
        return null; // Login fallido
    }

    /**
     * Encripta una contraseña usando SHA-256.
     *
     * @param password Contraseña a encriptar
     * @return Contraseña encriptada en formato hexadecimal
     */
    private String encriptarPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            // Convertir bytes a formato hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // En caso de error, devolver sin encriptar (no recomendado)
        }
    }

    /**
     * Obtiene los datos de un usuario por su email (versión actualizada con password).
     *
     * @param email Email del usuario a buscar
     * @return Array de String con [id, nombre, email, googleId], null si no existe
     */
    public String[] obtenerUsuarioPorEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USUARIOS,
                new String[]{COLUMN_ID, COLUMN_NOMBRE, COLUMN_EMAIL, COLUMN_GOOGLE_ID},
                COLUMN_EMAIL + " = ?",
                new String[]{email},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            String[] usuario = new String[]{
                    String.valueOf(cursor.getInt(0)), // id
                    cursor.getString(1), // nombre
                    cursor.getString(2), // email
                    cursor.getString(3)  // googleId
            };
            cursor.close();
            db.close();
            return usuario;
        }

        cursor.close();
        db.close();
        return null;
    }

    /**
     * Obtiene la fecha actual en formato ISO (YYYY-MM-DD HH:mm:ss).
     *
     * @return String con la fecha actual
     */
    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}

