package com.haochen.pokedexgo.util;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.haochen.pokedexgo.sqlite.DBHelper;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by Haochen on 2016/7/19.
 */
public class DataImporter {
    private Workbook workbook;
    private SQLiteDatabase db;

    public void load(File xlsFile) {
        if (workbook != null) {
            release();
        }
        try {
            workbook = Workbook.getWorkbook(xlsFile);
            db = DBHelper.getInstance(null).getWritableDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }
    
    public void importAllData() throws SQLException {
        importType();
        importTypeChart();
        importPokemon();
        importBasicSkill();
        importChargeSkill();
        importPokemonBasicSkill();
        importPokemonChargeSkill();
    }

    public void release() {
        workbook.close();
        db.close();
        workbook = null;
        db = null;
    }

    public void importType() throws SQLException {
        String[] types = {
                "普", "火", "水", "草", "电", "冰", "飞", "毒", "斗",
                "恶", "超", "地", "岩", "虫", "鬼", "钢", "龙", "妖"
        };
        for (String type : types) {
            db.execSQL("INSERT INTO type VALUES (?, ?)",
                    new Object[]{
                            TypeUtil.toTypeId(type),
                            TypeUtil.toTypeResName(type)
                    });
        }
    }

    public void importTypeChart() throws SQLException {
        Sheet sheet = workbook.getSheet(5);
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM type", null);
        cursor.moveToNext();
        int typeNum = cursor.getInt(0);
        cursor.close();
        Cell[] atkCells = sheet.getColumn(1);
        Cell[] defCells = sheet.getRow(1);
        int[] atkIds = new int[typeNum];
        int[] defIds = new int[typeNum];
        for (int i = 0; i < typeNum; ++i) {
            atkIds[i] = TypeUtil.toTypeId(atkCells[i + 2].getContents());
            defIds[i] = TypeUtil.toTypeId(defCells[i + 2].getContents());
        }
        Cell[] row;
        for (int i = 0; i < typeNum; ++i) {
            row = sheet.getRow(i + 2);
            for (int j = 0; j < typeNum; ++j) {
                db.execSQL("INSERT INTO type_chart VALUES (?, ?, ?)",
                        new Object[]{atkIds[i], defIds[j], row[j + 2].getContents()});
            }
        }
    }

    public void importPokemon() throws SQLException {
        Sheet sheet = workbook.getSheet(0);
        for (int i = 1; i < sheet.getRows(); ++i) {
            Cell[] cells = sheet.getRow(i);
            String[] str = new String[cells.length];
            for (int j = 0; j < str.length; ++j) {
                str[j] = cells[j].getContents();
            }
            db.execSQL("INSERT INTO pokemon VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{
                            str[0],
                            str[1],
                            str[2],
                            "".equals(str[3]) ? null : TypeUtil.toTypeId(str[3]),
                            "".equals(str[4]) ? null : TypeUtil.toTypeId(str[4]),
                            Integer.parseInt(str[5]),
                            Integer.parseInt(str[6]),
                            Integer.parseInt(str[7]),
                            Integer.parseInt(str[8]),
                            Double.parseDouble(str[9]),
                            Double.parseDouble(str[10]),
                            "".equals(str[11]) ? null : str[11],
                            "".equals(str[12]) ? null : str[12],
                            "".equals(str[13]) ? null : str[13]
                    });
        }
    }

    public void importBasicSkill() throws SQLException {
        Sheet sheet = workbook.getSheet(1);
        for (int i = 1; i < sheet.getRows(); ++i) {
            Cell[] cells = sheet.getRow(i);
            db.execSQL("INSERT INTO basic_skill VALUES (?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{
                            cells[0].getContents(),
                            cells[1].getContents(),
                            cells[2].getContents(),
                            TypeUtil.toTypeId(cells[3].getContents()),
                            cells[4].getContents(),
                            cells[5].getContents(),
                            cells[6].getContents(),
                    });
        }
    }

    public void importChargeSkill() throws SQLException {
        Sheet sheet = workbook.getSheet(2);
        for (int i = 1; i < sheet.getRows(); ++i) {
            Cell[] cells = sheet.getRow(i);
            String str6 = cells[6].getContents();
            String str7 = cells[7].getContents();
            db.execSQL("INSERT INTO charge_skill VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{
                            cells[0].getContents(),
                            cells[1].getContents(),
                            cells[2].getContents(),
                            TypeUtil.toTypeId(cells[3].getContents()),
                            cells[4].getContents(),
                            cells[5].getContents(),
                            "".equals(str6) ? null : str6,
                            "".equals(str7) ? null : str7,
                            cells[8].getContents(),
                            cells[9].getContents(),
                            cells[10].getContents(),
                    });
        }
    }

    public void importPokemonBasicSkill() throws SQLException {
        Sheet sheet = workbook.getSheet(3);
        for (int i = 1; i < sheet.getRows(); ++i) {
            Cell[] cells = sheet.getRow(i);
            int col = 3;
            while (col < cells.length && !"".equals(cells[col].getContents())) {
                db.execSQL("INSERT INTO pokemon_basic_skill VALUES (?, ?)",
                        new Object[]{
                                cells[0].getContents(),
                                cells[col].getContents(),
                        });
                ++col;
            }
        }
    }

    public void importPokemonChargeSkill() throws SQLException {
        Sheet sheet = workbook.getSheet(4);
        for (int i = 1; i < sheet.getRows(); ++i) {
            Cell[] cells = sheet.getRow(i);
            int col = 3;
            while (col < cells.length && !"".equals(cells[col].getContents())) {
                db.execSQL("INSERT INTO pokemon_charge_skill VALUES (?, ?)",
                        new Object[]{
                                cells[0].getContents(),
                                cells[col].getContents(),
                        });
                ++col;
            }
        }
    }
}
