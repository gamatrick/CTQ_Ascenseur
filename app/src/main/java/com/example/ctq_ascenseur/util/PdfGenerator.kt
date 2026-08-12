package com.example.ctq_ascenseur.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.ctq_ascenseur.data.model.Elevator
import com.example.ctq_ascenseur.data.model.Inspection
import com.example.ctq_ascenseur.data.model.InspectionResult
import java.io.File
import java.io.FileOutputStream

object PdfGenerator {
    fun generateReport(
        context: Context,
        elevator: Elevator,
        inspection: Inspection,
        results: List<InspectionResult>
    ): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        // Color setup
        val greenPrimary = 0xFF2E7D32.toInt()
        
        // Header
        paint.color = greenPrimary
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText("RAPPORT CTQ ASCENSEUR", 50f, 60f, paint)

        paint.color = 0xFF000000.toInt()
        paint.textSize = 12f
        paint.isFakeBoldText = false
        canvas.drawText("A.C.T.I.V - Ascenseurs Contrôles Techniques", 50f, 85f, paint)
        
        canvas.drawLine(50f, 100f, 545f, 100f, paint)

        // Content
        paint.isFakeBoldText = true
        canvas.drawText("INSTALLATION", 50f, 130f, paint)
        paint.isFakeBoldText = false
        canvas.drawText("Adresse: ${elevator.address}", 50f, 150f, paint)
        canvas.drawText("Marque/Modèle: ${elevator.brand} / ${elevator.type}", 50f, 165f, paint)
        canvas.drawText("N° Série: ${elevator.serialNumber}", 50f, 180f, paint)

        paint.isFakeBoldText = true
        canvas.drawText("CONTRÔLE", 300f, 130f, paint)
        paint.isFakeBoldText = false
        canvas.drawText("Date: ${java.text.SimpleDateFormat("dd/MM/yyyy").format(inspection.date)}", 300f, 150f, paint)
        canvas.drawText("ID Rapport: ${inspection.id}", 300f, 165f, paint)

        canvas.drawLine(50f, 200f, 545f, 200f, paint)

        // Results table header
        paint.isFakeBoldText = true
        canvas.drawText("POINT DE CONTRÔLE", 50f, 220f, paint)
        canvas.drawText("RÉSULTAT", 450f, 220f, paint)
        paint.isFakeBoldText = false

        var y = 245f
        results.take(25).forEach { result -> // Limit to one page for MVP
            canvas.drawText("Point #${result.controlPointId}", 50f, y, paint)
            canvas.drawText(result.status.name, 450f, y, paint)
            y += 20f
        }

        pdfDocument.finishPage(page)

        val file = File(context.getExternalFilesDir(null), "Rapport_CTQ_${inspection.id}.pdf")
        return try {
            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
