package com.app.hinariovirtual.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.hinariovirtual.model.Evento;
import com.app.hinariovirtual.model.Hino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hinos.db";
    private static final int DATABASE_VERSION = 2;

    // Tabela hino
    private static final String TABLE_HINO = "hino";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CODIGO = "codigo";
    private static final String COLUMN_TITULO = "titulo";
    private static final String COLUMN_TEXTO = "texto";
    private static final String COLUMN_FAVORITO = "favorito";

    // Tabela evento
    private static final String TABLE_EVENTO = "evento";
    private static final String COLUMN_EVENTO_ID = "id";
    private static final String COLUMN_EVENTO_DESCRICAO = "descricao";

    // Tabela associativa evento_hino
    private static final String TABLE_EVENTO_HINO = "evento_hino";
    private static final String COLUMN_EH_EVENTO_ID = "evento_id";
    private static final String COLUMN_EH_HINO_ID = "hino_id";

    // SQL para criar as tabelas
    private static final String CREATE_TABLE_HINO =
            "CREATE TABLE " + TABLE_HINO + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CODIGO + " TEXT NOT NULL, " +
                    COLUMN_TITULO + " TEXT NOT NULL, " +
                    COLUMN_TEXTO + " TEXT, " +
                    COLUMN_FAVORITO + " INTEGER DEFAULT 0" +
                    ")";

    private static final String CREATE_TABLE_EVENTO =
            "CREATE TABLE " + TABLE_EVENTO + " (" +
                    COLUMN_EVENTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EVENTO_DESCRICAO + " TEXT NOT NULL" +
                    ")";

    private static final String CREATE_TABLE_EVENTO_HINO =
            "CREATE TABLE " + TABLE_EVENTO_HINO + " (" +
                    COLUMN_EH_EVENTO_ID + " INTEGER NOT NULL, " +
                    COLUMN_EH_HINO_ID + " INTEGER NOT NULL, " +
                    "PRIMARY KEY (" + COLUMN_EH_EVENTO_ID + ", " + COLUMN_EH_HINO_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_EH_EVENTO_ID + ") REFERENCES " + TABLE_EVENTO + "(" + COLUMN_EVENTO_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY (" + COLUMN_EH_HINO_ID + ") REFERENCES " + TABLE_HINO + "(" + COLUMN_ID + ") ON DELETE CASCADE" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HINO);
        db.execSQL(CREATE_TABLE_EVENTO);
        db.execSQL(CREATE_TABLE_EVENTO_HINO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Adiciona coluna favorito na tabela hino
            db.execSQL("ALTER TABLE " + TABLE_HINO + " ADD COLUMN " + COLUMN_FAVORITO + " INTEGER DEFAULT 0");
            // Cria novas tabelas
            db.execSQL(CREATE_TABLE_EVENTO);
            db.execSQL(CREATE_TABLE_EVENTO_HINO);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    // ==================== HINO ====================

    public long adicionar(Hino cadastro) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CODIGO, cadastro.getCodigo());
        values.put(COLUMN_TITULO, cadastro.getTitulo());
        values.put(COLUMN_TEXTO, cadastro.getTexto());
        values.put(COLUMN_FAVORITO, cadastro.isFavorito() ? 1 : 0);

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
        values.put(COLUMN_FAVORITO, cadastro.isFavorito() ? 1 : 0);

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

    public Hino buscarPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HINO,
                new String[]{COLUMN_ID, COLUMN_CODIGO, COLUMN_TITULO, COLUMN_TEXTO, COLUMN_FAVORITO},
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        Hino cadastro = null;
        if (cursor != null && cursor.moveToFirst()) {
            cadastro = new Hino(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4) == 1
            );
            cursor.close();
        }

        db.close();
        return cadastro;
    }

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
                        cursor.getString(3),
                        cursor.getInt(4) == 1
                );
                listaHino.add(cadastro);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        Collections.sort(listaHino);

        return listaHino;
    }

    public void toggleFavorito(int hinoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_HINO + " SET " + COLUMN_FAVORITO +
                " = CASE WHEN " + COLUMN_FAVORITO + " = 1 THEN 0 ELSE 1 END WHERE " +
                COLUMN_ID + " = ?", new Object[]{hinoId});
        db.close();
    }

    public List<Hino> listarFavoritos() {
        List<Hino> listaHino = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HINO +
                " WHERE " + COLUMN_FAVORITO + " = 1", null);

        if (cursor.moveToFirst()) {
            do {
                Hino cadastro = new Hino(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4) == 1
                );
                listaHino.add(cadastro);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        Collections.sort(listaHino);

        return listaHino;
    }

    // ==================== EVENTO ====================

    public long adicionarEvento(Evento evento) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENTO_DESCRICAO, evento.getDescricao());

        long id = db.insert(TABLE_EVENTO, null, values);
        db.close();

        return id;
    }

    public List<Evento> listarEventos() {
        List<Evento> listaEventos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENTO + " ORDER BY " + COLUMN_EVENTO_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Evento evento = new Evento(
                        cursor.getInt(0),
                        cursor.getString(1)
                );
                listaEventos.add(evento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaEventos;
    }

    public void removerEvento(int eventoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Foreign keys com ON DELETE CASCADE cuidam da tabela associativa
        db.delete(TABLE_EVENTO, COLUMN_EVENTO_ID + " = ?",
                new String[]{String.valueOf(eventoId)});
        db.close();
    }

    public void associarHinoAoEvento(int eventoId, int hinoId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EH_EVENTO_ID, eventoId);
        values.put(COLUMN_EH_HINO_ID, hinoId);

        db.insertWithOnConflict(TABLE_EVENTO_HINO, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    public List<Hino> listarHinosDoEvento(int eventoId) {
        List<Hino> listaHino = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT h.* FROM " + TABLE_HINO + " h " +
                "INNER JOIN " + TABLE_EVENTO_HINO + " eh ON h." + COLUMN_ID + " = eh." + COLUMN_EH_HINO_ID +
                " WHERE eh." + COLUMN_EH_EVENTO_ID + " = ? ORDER BY h." + COLUMN_CODIGO;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(eventoId)});

        if (cursor.moveToFirst()) {
            do {
                Hino hino = new Hino(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4) == 1
                );
                listaHino.add(hino);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaHino;
    }
}