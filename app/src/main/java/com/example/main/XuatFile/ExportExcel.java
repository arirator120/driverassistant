package com.example.main.XuatFile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.example.main.R;

public class ExportExcel extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuatfile);

        Button button=findViewById(R.id.ExelWrite);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Workbook wb=new HSSFWorkbook();
        Cell cell=null;
        CellStyle cellStyle=wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        //Now we are creating sheet
        Sheet sheet=null;
        sheet = wb.createSheet("Name of sheet");
        //Now column and row
        Row row =sheet.createRow(0);
        cell=row.createCell(0);
        cell.setCellValue("Name");
        cell.setCellStyle(cellStyle);

        cell=row.createCell(1);
        cell.setCellValue("Number");
        cell.setCellStyle(cellStyle);

        sheet.setColumnWidth(0,(10*200));
        sheet.setColumnWidth(1,(10*200));

        File file = new File(getExternalFilesDir(null),"plik.xls");
        FileOutputStream outputStream =null;

            try {
                outputStream=new FileOutputStream(file);
                wb.write(outputStream);
                Toast.makeText(getApplicationContext(),"Xuất file thành công",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();

                Toast.makeText(getApplicationContext(),"Xuất file không thành công",Toast.LENGTH_LONG).show();
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }


    }
}
