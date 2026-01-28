package com.app.hinariovirtual.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.hinariovirtual.model.Hino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hinos.db";
    private static final int DATABASE_VERSION = 1;

    // Nome da tabela e colunas
    private static final String TABLE_HINO = "hino";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CODIGO = "codigo";
    private static final String COLUMN_TITULO = "titulo";
    private static final String COLUMN_TEXTO = "texto";

    // SQL para criar a tabela
    private static final String CREATE_TABLE_HINO =
            "CREATE TABLE " + TABLE_HINO + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CODIGO + " TEXT NOT NULL, " +
                    COLUMN_TITULO + " TEXT NOT NULL, " +
                    COLUMN_TEXTO + " TEXT" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HINO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HINO);
        onCreate(db);
    }

    public long adicionar(Hino cadastro) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CODIGO, cadastro.getCodigo());
        values.put(COLUMN_TITULO, cadastro.getTitulo());
        values.put(COLUMN_TEXTO, cadastro.getTexto());

        long id = db.insert(TABLE_HINO, null, values);
        db.close();

        return id;
    }

    public int atualizar(Hino cadastro) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CODIGO, cadastro.getCodigo());
        values.put(COLUMN_TITULO, cadastro.getTitulo());
        values.put(COLUMN_TEXTO, cadastro.getTexto());

        int rowsAffected = db.update(TABLE_HINO, values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(cadastro.getId())});

        db.close();
        return rowsAffected;
    }

    public void remover(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HINO, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * Busca um cadastro por ID
     */
    public Hino buscarPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HINO,
                new String[]{COLUMN_ID, COLUMN_CODIGO, COLUMN_TITULO, COLUMN_TEXTO},
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        Hino cadastro = null;
        if (cursor != null && cursor.moveToFirst()) {
            cadastro = new Hino(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            cursor.close();
        }

        db.close();
        return cadastro;
    }

    /**
     * Lista todos os cadastros ordenados por código
     */
    public List<Hino> listarTodos() {
        List<Hino> listaHino = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HINO, null);

        if (cursor.moveToFirst()) {
            do {
                Hino cadastro = new Hino(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                listaHino.add(cadastro);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // Ordena por código
        Collections.sort(listaHino);

        return listaHino;
    }
}